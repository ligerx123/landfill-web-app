package org.lacitysan.landfill.server.persistence.dao.instantaneous;

import java.util.List;

import org.lacitysan.landfill.server.persistence.entity.instantaneous.InstantaneousData;

/**
 * @author Alvin Quach
 */
public interface InstantaneousDataDao {

	List<InstantaneousData> getBySite(String siteName);

	List<InstantaneousData> getBySiteAndDate(String siteName, Long start, Long end);

	Object create(InstantaneousData data);

}
