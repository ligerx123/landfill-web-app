package org.lacitysan.landfill.server.config;

import org.lacitysan.landfill.server.service.scheduled.ScheduledTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Alvin Quach
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
	
	@Autowired
	ScheduledTaskService scheduledTaskService;
	
	@Scheduled(fixedRate=60000)
	public void runScheduledTasks() {
		scheduledTaskService.runScheduledTasks();
	}

	@Bean
	public TaskScheduler poolScheduler() {
		return new ThreadPoolTaskScheduler();
	}

}
