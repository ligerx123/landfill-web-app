package org.lacitysan.landfill.server.persistence.dao.scheduled;

import java.util.List;

import org.lacitysan.landfill.server.persistence.dao.AbstractDao;
import org.lacitysan.landfill.server.persistence.entity.scheduled.ScheduledEmail;
import org.lacitysan.landfill.server.persistence.entity.scheduled.ScheduledNotification;
import org.lacitysan.landfill.server.persistence.entity.scheduled.ScheduledReport;

/**
 * Data access object for <code>ScheduledEmail</code> entities.
 * @author Alvin Quach
 */
public interface ScheduledEmailDao extends AbstractDao<ScheduledEmail> {

	List<ScheduledReport> getAllScheduledReports();

	List<ScheduledNotification> getAllScheduledNotifications();

}
