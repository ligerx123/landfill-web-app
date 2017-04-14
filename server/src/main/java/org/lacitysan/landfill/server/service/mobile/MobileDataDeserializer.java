package org.lacitysan.landfill.server.service.mobile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.lacitysan.landfill.server.config.app.ApplicationConstant;
import org.lacitysan.landfill.server.persistence.dao.instantaneous.ImeNumberDao;
import org.lacitysan.landfill.server.persistence.dao.instantaneous.WarmspotDataDao;
import org.lacitysan.landfill.server.persistence.dao.unverified.UnverifiedDataSetDao;
import org.lacitysan.landfill.server.persistence.dao.user.UserDao;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.ImeData;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.ImeNumber;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedDataSet;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedInstantaneousData;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedWarmspotData;
import org.lacitysan.landfill.server.persistence.entity.user.User;
import org.lacitysan.landfill.server.persistence.enums.ExceedanceStatus;
import org.lacitysan.landfill.server.persistence.enums.MonitoringPoint;
import org.lacitysan.landfill.server.persistence.enums.Site;
import org.lacitysan.landfill.server.service.MonitoringPointService;
import org.lacitysan.landfill.server.service.instantaneous.ImeService;
import org.lacitysan.landfill.server.service.mobile.model.MobileDataContainer;
import org.lacitysan.landfill.server.service.mobile.model.MobileImeData;
import org.lacitysan.landfill.server.service.mobile.model.MobileInstantaneousData;
import org.lacitysan.landfill.server.service.mobile.model.MobileWarmspotData;
import org.lacitysan.landfill.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Deserializer service for converting data models from the mobile app to the web server.
 * @author Alvin Quach
 */
@Service
public class MobileDataDeserializer {
	
	@Autowired
	ImeNumberDao imeNumberDao;
	
	@Autowired
	WarmspotDataDao warmspotDataDao;
	
	@Autowired
	UnverifiedDataSetDao unverifiedDataSetDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ImeService imeService;
	
	@Autowired
	MonitoringPointService monitoringPointService;
	
	public Set<UnverifiedDataSet> deserializeData(MobileDataContainer mobileDataContainer) {
		
		// Store a map of users by their usernames.
		Map<String, User> userMap = new HashMap<>();

		// Store a map of imported IME numbers. The value stored for each IME number is its original imported IME number string, if exists.
		Map<ImeNumber, String> imeNumberMap = new HashMap<>();
		
		// Fucking want to chop my dick off.
		for (MobileImeData mobileImeData : mobileDataContainer.getmImeDatas()) {
			
			String imeNumberString = mobileImeData.getmImeNumber();

			// If the imported IME data doesn't contain an IME number, then get it from the site and date.
			if (imeNumberString == null || imeNumberString.trim().isEmpty()) {
				
				// Get site.
				Site site = monitoringPointService.getSiteByName(mobileImeData.getmLocation());
				if (site == null) {
					continue;
				}
				
				// Get date and format it into the date code.
				Timestamp date = parseDate(mobileImeData.getmDate());
				if (date == null) {
					continue;
				}
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				int dateCode = (calendar.get(Calendar.YEAR) % 2000) * 100 + calendar.get(Calendar.MONTH) + 1;
				
				// Create new IME number string based on site, date code, and next sequence number.
				imeNumberString = site.getShortName() + "-" + dateCode + "-00";
			}
			
			// Create new IME number based on the IME number string.
			ImeNumber imeNumber = imeService.getImeNumberFromString(imeNumberString);
			if (imeNumber == null) {
				continue;
			}
			
			// Set the status of the IME number as unverified.
			imeNumber.setStatus(ExceedanceStatus.UNVERIFIED);
			
			// Create initial IME data entry based on imported info.
			ImeData imeData = new ImeData();
			//imeData.setId(0);
			imeData.setDateTime(parseDate(mobileImeData.getmDate()));
			imeData.setDescription(mobileImeData.getmDescription());
			imeData.setMethaneLevel((int)(mobileImeData.getmMethaneReading() * 100));
			imeData.setInspector(getUser(userMap, mobileImeData.getmInspectorUserName()));
			imeData.setImeNumber(imeNumber);
			imeNumber.getImeData().add(imeData);
			
			// Add the IME number to the set of IME numbers.
			imeNumberMap.put(imeNumber, imeNumberString);
		}
		
		// WTF???????????????
		Map<User, Map<Site, UnverifiedDataSet>> resultMap = new HashMap<>();
		
		// Process the instantaneous data entries.
		for (MobileInstantaneousData mobileInstantaneousData : mobileDataContainer.getmInstantaneousDatas()) {

			UnverifiedInstantaneousData instantaneousData = new UnverifiedInstantaneousData();
			
			// TODO Implement this inside the enum.
			if (mobileInstantaneousData.getGridId() != null && !mobileInstantaneousData.getGridId().isEmpty() && mobileInstantaneousData.getmLocation() != null && !mobileInstantaneousData.getmLocation().isEmpty()) {
				MonitoringPoint grid = monitoringPointService.getGridBySiteNameAndId(monitoringPointService.getSiteByName(mobileInstantaneousData.getmLocation()), mobileInstantaneousData.getGridId());
				if (grid == null) {
					if (ApplicationConstant.DEBUG) System.out.println("DEBUG:\tError Unmapping Instantaneous Data: Grid " + mobileInstantaneousData.getGridId() + " in " + mobileInstantaneousData.getmLocation() + " not found.");
					return null;
				}
				instantaneousData.setMonitoringPoint(grid);
			}
			if (mobileInstantaneousData.getImeNumber() != null && !mobileInstantaneousData.getImeNumber().isEmpty()) {
				ImeNumber imeNumber = null;
				for (ImeNumber existingImeNumber : imeNumberMap.keySet()) {
					if (imeNumberMap.get(existingImeNumber).equals(mobileInstantaneousData.getImeNumber())) {
						imeNumber = existingImeNumber;
						break;
					}
				}
				if (imeNumber == null) {
					ImeNumber newImeNumber = imeService.getImeNumberFromString(mobileInstantaneousData.getImeNumber());
					if (newImeNumber != null) {
						short sequence = imeService.getNextSequence(newImeNumber.getSite(), newImeNumber.getDateCode(), false);
						newImeNumber.setSequence(sequence);
						newImeNumber.setStatus(ExceedanceStatus.UNVERIFIED);
						imeNumberMap.put(newImeNumber, mobileInstantaneousData.getImeNumber());
						imeNumber = newImeNumber;
					}
				}
				if (imeNumber != null) {
					imeNumber.getUnverifiedInstantaneousData().add(instantaneousData);
					instantaneousData.getImeNumbers().add(imeNumber);
				}
			}
			instantaneousData.setMethaneLevel((int)(mobileInstantaneousData.getMethaneReading() * 100));
			instantaneousData.setStartTime(parseDate(mobileInstantaneousData.getmStartDate()));
			instantaneousData.setEndTime(parseDate(mobileInstantaneousData.getmEndDate()));
			
			// Set barometric pressure
			if (mobileInstantaneousData.getmBarometricPressure() != null) {
				instantaneousData.setBarometricPressure((short)(mobileInstantaneousData.getmBarometricPressure() * 100));
			}
			
			// Get user information.
			User user = getUser(userMap, mobileInstantaneousData.getmInspectorUserName());
			if (user == null) {
				continue;
			}
			if (!resultMap.containsKey(user)) {
				Map<Site, UnverifiedDataSet> wtf = new HashMap<>();
				resultMap.put(user, wtf);
			}
			UnverifiedDataSet hello = getDataSet(resultMap.get(user), monitoringPointService.getSiteByName(mobileInstantaneousData.getmLocation()));
			if (hello.getInspector() == null) {
				hello.setInspector(user);
			}

			instantaneousData.setUnverifiedDataSet(hello);
			hello.getUnverifiedInstantaneousData().add(instantaneousData);
		}
		
		for (MobileWarmspotData mobileWarmspotData : mobileDataContainer.getmWarmSpotDatas()) {
			UnverifiedWarmspotData warmspotData = new UnverifiedWarmspotData();
			warmspotData.setDescription(mobileWarmspotData.getmDescription());
			warmspotData.setSize(String.valueOf(mobileWarmspotData.getmEstimatedSize()));
			User user = getUser(userMap, mobileWarmspotData.getmInspectorUserName());
			if (user == null || !resultMap.containsKey(user)) {
				continue;
			}
			Site site = monitoringPointService.getSiteByName(mobileWarmspotData.getmLocation());
			MonitoringPoint grid = monitoringPointService.getGridBySiteNameAndId(site, mobileWarmspotData.getmGridId());
			if (grid == null) {
				if (ApplicationConstant.DEBUG) System.out.println("DEBUG:\tError Unmapping Instantaneous Data: Grid " + mobileWarmspotData.getmGridId() + " in " + mobileWarmspotData.getmLocation() + " not found.");
				return null;
			}
			UnverifiedDataSet unverifiedDataSet = getDataSet(resultMap.get(user), site);
			
			// TODO Consider reversing the loops.
			for (UnverifiedInstantaneousData unverifiedInstantaneousData : unverifiedDataSet.getUnverifiedInstantaneousData()) {
				if (unverifiedInstantaneousData.getMethaneLevel() == (int)(mobileWarmspotData.getmMaxMethaneReading() * 100)) {
					unverifiedInstantaneousData.setUnverifiedWarmspotData(warmspotData);
					break;
				}
			}
		}
		
		// *** If it got to this point in the code, then that means there was no 'errors' with the mobile data.
				
		Set<UnverifiedDataSet> result = resultMap.values().stream().map(map -> map.values()).flatMap(values -> values.stream()).collect(Collectors.toSet());
		
		// Insert IME numbers into database.
		for (ImeNumber imeNumber : imeNumberMap.keySet()) {
			// TODO Update 'modified by' field.
			imeService.createUnverified(imeNumber);

		}
		
		// Insert unverified data sets into the database.
		for (UnverifiedDataSet unverifiedDataSet : result) {
			unverifiedDataSet.setUploadedBy(userService.getCurrentUser());
			unverifiedDataSet.setUploadedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			unverifiedDataSet.setFilename(mobileDataContainer.getFilename());
			unverifiedDataSetDao.create(unverifiedDataSet);
		}
		
		// Return the set of results.
		return result;
	}

	private UnverifiedDataSet getDataSet(Map<Site, UnverifiedDataSet> map, Site site) {
		
		// If a data set doesn't exist yet for the site, then create a new data set and set its id to 0.
		if (!map.containsKey(site)) {
			UnverifiedDataSet dataSet = new UnverifiedDataSet();
			//dataSet.setId(0);
			dataSet.setSite(site);
			map.put(site, dataSet);
		}
		
		return map.get(site); // Return
	}
	
	private User getUser(Map<String, User> userMap, String username) {
		for (User user : userMap.values()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		User user = userDao.getUserByUsername(username);
		if (user == null) {
			return null;
		}
		userMap.put(username, user);
		return user;
	}
	
	private Timestamp parseDate(String mobileDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
		try {
			return new Timestamp(dateFormat.parse(mobileDate).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

}
