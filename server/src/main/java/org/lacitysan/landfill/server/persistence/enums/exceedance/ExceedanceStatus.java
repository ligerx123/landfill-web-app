package org.lacitysan.landfill.server.persistence.enums.exceedance;

import org.lacitysan.landfill.server.json.LandfillEnumDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Alvin Quach
 */
public enum ExceedanceStatus {
	
	UNVERIFIED,
	ACTIVE,
	CLEARED;
	
	@JsonCreator
	public static ExceedanceStatus deserialize(Object object) {
		return LandfillEnumDeserializer.deserialize(ExceedanceStatus.class, object);
	}
	
}
