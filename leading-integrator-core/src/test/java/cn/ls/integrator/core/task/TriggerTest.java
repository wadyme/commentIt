/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


/**
 * 
 * 
 * @author zhoumb 2011-5-5
 */
public class TriggerTest {

	@Test
	public void baseExecuteTest() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("job key").build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(
					CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			sched.scheduleJob(jobDetail, trigger);
			sched.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
