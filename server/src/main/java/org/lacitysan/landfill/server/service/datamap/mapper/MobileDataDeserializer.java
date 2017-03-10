package org.lacitysan.landfill.server.service.datamap.mapper;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.lacitysan.landfill.server.persistence.dao.instantaneous.ImeNumberDao;
import org.lacitysan.landfill.server.persistence.dao.user.UserDao;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.ImeData;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.ImeNumber;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.WarmspotData;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedDataSet;
import org.lacitysan.landfill.server.persistence.entity.unverified.UnverifiedInstantaneousData;
import org.lacitysan.landfill.server.persistence.entity.user.User;
import org.lacitysan.landfill.server.persistence.enums.IMENumberStatus;
import org.lacitysan.landfill.server.persistence.enums.MonitoringPoint;
import org.lacitysan.landfill.server.persistence.enums.Site;
import org.lacitysan.landfill.server.service.ImeService;
import org.lacitysan.landfill.server.service.MonitoringPointService;
import org.lacitysan.landfill.server.service.datamap.model.mobile.MobileDataContainer;
import org.lacitysan.landfill.server.service.datamap.model.mobile.MobileImeData;
import org.lacitysan.landfill.server.service.datamap.model.mobile.MobileInstantaneousData;
import org.lacitysan.landfill.server.service.datamap.model.mobile.MobileWarmspotData;
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
	UserDao userDao;
	
	@Autowired
	ImeService imeService;
	
	@Autowired
	MonitoringPointService monitoringPointService;
	
	public Set<UnverifiedDataSet> deserializeData(MobileDataContainer mobileDataContainer) throws ParseException {
		
		// Store a map of users.
		Map<String, User> userMap = new HashMap<>();

		// Store a map of imported IME numbers. The value stored for each IME number is its original imported IME number string, if exists.
		Map<ImeNumber, String> imeNumbers = new HashMap<>();
		
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
			
			// Get next IME sequence number based on site and date code.
			short sequence = imeNumberDao.getNextSequence(imeNumber.getSite(), imeNumber.getDateCode());
			imeNumber.setSequence(sequence);
			
			// Set the status of the IME number as unverified.
			imeNumber.setStatus(IMENumberStatus.UNVERIFIED);
			
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
			imeNumbers.put(imeNumber, imeNumberString);
		}
		
		// WTF???????????????
		Map<User, Map<Site, UnverifiedDataSet>> result = new HashMap<>();
		
		// Okay, no more comments.
		for (MobileInstantaneousData mobileInstantaneousData : mobileDataContainer.getmInstantaneousDatas()) {
			UnverifiedInstantaneousData instantaneousData = deserializeInstantaneousData(mobileInstantaneousData, imeNumbers);
			User user = getUser(userMap, mobileInstantaneousData.getmInspectorUserName());
			if (user == null) {
				continue;
			}
			if (!result.containsKey(user)) {
				Map<Site, UnverifiedDataSet> wtf = new HashMap<>();
				result.put(user, wtf);
			}
			UnverifiedDataSet hello = getDataSet(result.get(user), monitoringPointService.getSiteByName(mobileInstantaneousData.getmLocation()));
			if (hello.getInspector() == null) {
				hello.setInspector(user);
			}
			if (mobileInstantaneousData.getmBarometricPressure() != null) {
				hello.setBarometricPressure((short)(mobileInstantaneousData.getmBarometricPressure() * 100));
			}
			instantaneousData.setUnverifiedDataSet(hello);
			hello.getUnverifiedInstantaneousData().add(instantaneousData);
		}
		
		for (MobileWarmspotData mobileWarmspotData : mobileDataContainer.getmWarmSpotDatas()) {
			// TODO URGENT NEED TO HANDLE UNVERIFIED WARMSPOT DATA
			WarmspotData warmspotData = new WarmspotData();
			warmspotData.setMethaneLevel((int)(mobileWarmspotData.getmMaxMethaneReading() * 100));
			warmspotData.setDescription(mobileWarmspotData.getmDescription());
			warmspotData.setSize(String.valueOf(mobileWarmspotData.getmEstimatedSize()));
			warmspotData.setDate(parseDate(mobileWarmspotData.getmDate()));
			User user = getUser(userMap, mobileWarmspotData.getmInspectorUserName());
			if (user == null || !result.containsKey(user)) {
				continue;
			}
			warmspotData.setInspector(user);
			UnverifiedDataSet hello = getDataSet(result.get(user), monitoringPointService.getSiteByName(mobileWarmspotData.getmLocation()));
			for (UnverifiedInstantaneousData instantaneousData : hello.getUnverifiedInstantaneousData()) {
				if (instantaneousData.getMethaneLevel().equals(mobileWarmspotData.getmMaxMethaneReading())) {
					warmspotData.setUnverifiedInstantaneousData(new HashSet<>(Arrays.asList(new UnverifiedInstantaneousData[] {instantaneousData})));
					instantaneousData.getWarmspotData().add(warmspotData);
					break;
				}
			}
		}
		
		// Do some bad stuff
		for (ImeNumber imeNumber : imeNumbers.keySet()) {
			Object id = imeNumberDao.create(imeNumber);
			if (id instanceof Integer) {
				imeNumber.setId((Integer)id);
			}
		}
		
		// Return the set of results.
		return result.values().stream().map(map -> map.values()).flatMap(values -> values.stream()).collect(Collectors.toSet());
	}
	
	/**
	 * Deserializes a <code>InstantaneousDataMobile</code> object into an <code>UnverifiedInstantaneousData</code> object.
	 * Currently, the user, monitoring point, and instrument data are not mapped, and will be <code>null</code> in the resulting output.
	 * @param mobileInstantaneousData The <code>InstantaneousDataMobile</code> object to be unmapped.
	 * @param imeNumbers
	 * @return The <code>InstantaneousData</code> representation of the input object.
	 * @throws ParseException 
	 */
	public UnverifiedInstantaneousData deserializeInstantaneousData(MobileInstantaneousData mobileInstantaneousData, Map<ImeNumber, String> imeNumbers) throws ParseException {

		UnverifiedInstantaneousData result = new UnverifiedInstantaneousData();
		//result.setId(0); // Is this needed?

		// TODO Implement this inside the enum.
		if (mobileInstantaneousData.getGridId() != null && !mobileInstantaneousData.getGridId().isEmpty() && mobileInstantaneousData.getmLocation() != null && !mobileInstantaneousData.getmLocation().isEmpty()) {
			MonitoringPoint grid = monitoringPointService.getGridBySiteNameAndId(monitoringPointService.getSiteByName(mobileInstantaneousData.getmLocation()), mobileInstantaneousData.getGridId());
			if (grid == null) {
				System.out.println("Error Unmapping Instantaneous Data: Grid " + mobileInstantaneousData.getGridId() + " in " + mobileInstantaneousData.getmLocation() + " not found.");
				return null;
			}
			result.setMonitoringPoint(grid);
		}
		if (mobileInstantaneousData.getImeNumber() != null && !mobileInstantaneousData.getImeNumber().isEmpty()) {
			ImeNumber imeNumber = null;
			for (ImeNumber existingImeNumber : imeNumbers.keySet()) {
				if (imeNumbers.get(existingImeNumber).equals(mobileInstantaneousData.getImeNumber())) {
					imeNumber = existingImeNumber;
					break;
				}
			}
			if (imeNumber == null) {
				ImeNumber newImeNumber = imeService.getImeNumberFromString(mobileInstantaneousData.getImeNumber());
				if (newImeNumber != null) {
					short sequence = imeNumberDao.getNextSequence(newImeNumber.getSite(), newImeNumber.getDateCode());
					newImeNumber.setSequence(sequence);
					newImeNumber.setStatus(IMENumberStatus.UNVERIFIED);
					imeNumbers.put(newImeNumber, mobileInstantaneousData.getImeNumber());
					imeNumber = newImeNumber;
				}
			}
			if (imeNumber != null) {
				imeNumber.getUnverifiedInstantaneousData().add(result);
				result.setImeNumbers(new HashSet<>(Arrays.asList(new ImeNumber[] {imeNumber}))); // TODO Change this.
			}
		}
		result.setMethaneLevel((int)(mobileInstantaneousData.getMethaneReading() * 100));
		result.setStartTime(parseDate(mobileInstantaneousData.getmStartDate()));
		result.setEndTime(parseDate(mobileInstantaneousData.getmEndDate()));

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
