package org.lacitysan.landfill.server.service.surfaceemission;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.lacitysan.landfill.server.persistence.dao.surfaceemission.SurfaceEmissionExceedanceNumberDao;
import org.lacitysan.landfill.server.persistence.entity.surfaceemission.SurfaceEmissionExceedanceData;
import org.lacitysan.landfill.server.persistence.entity.surfaceemission.SurfaceEmissionExceedanceNumber;
import org.lacitysan.landfill.server.persistence.entity.surfaceemission.SurfaceEmissionExceedanceRepairData;
import org.lacitysan.landfill.server.persistence.enums.exceedance.ExceedanceStatus;
import org.lacitysan.landfill.server.persistence.enums.location.Site;
import org.lacitysan.landfill.server.service.MonitoringPointService;
import org.lacitysan.landfill.server.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the logical operations for surface emissions exceedance (exceedance and ISE) related classes.
 * @param <T> The type of the surface emissions exceedance number (ie. <code>exceedanceNumber</code>).
 * @author Alvin Quach
 */
public abstract class SurfaceEmissionExceedanceNumberService<T extends SurfaceEmissionExceedanceNumber> {

	@Autowired
	protected MonitoringPointService monitoringPointService;

	abstract public List<T> getBySite(String exceedanceNumber);
	
	abstract public List<T> getBySiteAndDateCode(String exceedanceNumber, Short dateCode);

	public List<T> getVerifiedBySiteAndDateRange(Site site, Long start, Long end) {
		return getCrudRepository().getVerifiedBySiteAndDateCodeRange(site, generateDateCodeFromLong(start), generateDateCodeFromLong(end)).stream()
				.filter(e -> {
					SurfaceEmissionExceedanceData initial = findInitialDataEntry(e);
					if (initial == null) {
						return false;
					}
					Timestamp date = initial.getDateTime();
					return start <= date.getTime() && DateTimeUtils.addDay(end) >= date.getTime();
				})
				.collect(Collectors.toList());
	}

	public T createUnverified(T exceedanceNumber) {

		// Use TreeSet to store existing unverified exceedance/ISE numbers so that they are in order by sequence number.
		Set<T> existingSet = new TreeSet<>(); 
		existingSet.addAll(getCrudRepository().getBySiteAndDateCode(exceedanceNumber.getSite(), exceedanceNumber.getDateCode()));

		short i = 1;
		for (T existing : existingSet) {
			if (existing.getSequence() > i) {
				break;
			}
			i++;
		}

		exceedanceNumber.setSequence(i);
		exceedanceNumber.setStatus(ExceedanceStatus.UNVERIFIED);
		return getCrudRepository().create(exceedanceNumber);

	}

	// TODO Change this to work with a batch of exceedance numbers at once for more efficiency.
	public T verify(T exceedanceNumber) {
		
		// This method is only used for unverified exceedance numbers.
		if (exceedanceNumber.getStatus() != ExceedanceStatus.UNVERIFIED) {
			return exceedanceNumber;
		}
		else {
			exceedanceNumber.setStatus(ExceedanceStatus.ACTIVE);
		}
		
		short originalSequence = exceedanceNumber.getSequence();
		
		// We use a list here so that we can access the indices directly.
		List<T> existingExceedanceNumbers = getCrudRepository()
				.getBySiteAndDateCode(exceedanceNumber.getSite(), exceedanceNumber.getDateCode())
				.stream()
				.sorted()
				.collect(Collectors.toList());
		
		boolean shift = false; // Whether we will need to shift the sequences of some of the unverified exceedance numbers.
		
		short i = 1;
		for (T existingExceedanceNumber : existingExceedanceNumbers) {
			if (existingExceedanceNumber.getSequence() > i) {
				break;
			}
			if (existingExceedanceNumber.getStatus() == ExceedanceStatus.UNVERIFIED) {
				shift = true;
				break;
			}
			i++;
		}
		
		exceedanceNumber.setSequence(i);
		exceedanceNumber.setUnverifiedDataSet(null);
		getCrudRepository().update(exceedanceNumber);

		if (shift) {
			for (int j = originalSequence - 1; j >= i; j--) {
				T existingExceedanceNumber = existingExceedanceNumbers.get(j - 1);
				if (existingExceedanceNumber.getStatus() == ExceedanceStatus.UNVERIFIED && existingExceedanceNumber.getSequence() < originalSequence) {
					existingExceedanceNumber.setSequence((short)(existingExceedanceNumber.getSequence() + 1));
					getCrudRepository().update(existingExceedanceNumber);
				}
			}
		}
		
		return exceedanceNumber;
		
	}

	abstract public T update(T exceedanceNumber);
	
	abstract public T clear(T exceedanceNumber);

	/**
	 * Converts a collection of exceedance numbers into a formatted string list.
	 * Assumes that all the numbers in the collection have a common site and date code.
	 * If there are multiple sites and/or date codes, the resulting string will be of no significance.
	 * @param exceedanceNumbers The collection of exceedance numbers.
	 * @return A formatted string list of exceedance numbers.
	 */
	public String stringifyCommonCollection(Collection<T> exceedanceNumbers) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (T exceedanceNumber : exceedanceNumbers) {
			if (i == 0) {
				sb.append(generateStringFromExceedanceNumber(exceedanceNumber));
			}
			else {
				sb.append(", -")
				.append(String.format("%02d", exceedanceNumber.getSequence()));
			}
			i++;
		}
		return sb.toString();
	}

	public short generateDateCodeFromLong(long date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(date);
		return (short)((calendar.get(Calendar.YEAR) - 2000) * 100 + calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * Generates a formatted string representation of a surface emissions exceedance number, based on the exceedance number's site, date, and sequence number.
	 * The format of the generated number will be AAyyMM-BB (dash included), where AA is the short name of the site, yyMM is the date format, and BB is the sequence number.
	 * @param exceedanceNumber The object that contains the surface emissions exceedance number's information. 
	 * @return The formatted surface emissions exceedance number string.
	 */
	protected String generateStringFromExceedanceNumber(T exceedanceNumber) {
		return exceedanceNumber.getSite().getShortName() + String.format("%04d", exceedanceNumber.getDateCode()) + "-" + String.format("%02d", exceedanceNumber.getSequence());
	}

	/**
	 * Creates an object that extends <code>ServiceEmissionsExceedanceNumber</code> using the given surface emissions exceedance number string.
	 * The format of the input string must be AAyyMM-BB (dashes are optional), where AA is the short name of the site, yyMM is the date format, and BB is the sequence number.
	 * If the input string is not valid, then <code>null</code> will be returned.
	 * The status of returned exceedance number will be set to <code>UNVERIFIED</code> by default.
	 * @param exceedanceNumber String representation of an surface emissions exceedance number.
	 * @return An object extending <code>ServiceEmissionsExceedanceNumber</code> based on the input surface emissions exceedance number string, or <code>null</code> if the input string was not valid.
	 */
	protected T generateExceedanceNumberFromString(String exceedanceNumber) {
		exceedanceNumber = exceedanceNumber.replaceAll("-", "").trim();
		try {
			if (exceedanceNumber.length() != 8) {
				return null; // TODO Have this throw some exceptions instead of returning null.
			}
			Site site = monitoringPointService.getSiteByShortName(exceedanceNumber.substring(0, 2));
			if (site == null) {
				return null; // TODO Have this throw some exceptions instead of returning null.
			}
			short dateCode = Short.valueOf(exceedanceNumber.substring(2,6)); // TODO Verify that this is valid.
			short sequence = Short.valueOf(exceedanceNumber.substring(6));
			T result = instantiateExceedanceNumber();
			result.setId(0); // Is this needed?
			result.setSite(site);
			result.setDateCode(dateCode);
			result.setSequence(sequence);
			result.setStatus(ExceedanceStatus.UNVERIFIED);
			return result;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	abstract public SurfaceEmissionExceedanceRepairData findLastRepair(T exceedanceNumber);

	abstract public SurfaceEmissionExceedanceData findInitialDataEntry(T exceedanceNumber);

	abstract protected SurfaceEmissionExceedanceNumberDao<T> getCrudRepository();

	@SuppressWarnings("unchecked")
	private T instantiateExceedanceNumber() {
		try {
			Class<?> exceedanceNumberGenericClass = (Class<?>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			return (T)Class.forName(exceedanceNumberGenericClass.getName()).getConstructor().newInstance();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
