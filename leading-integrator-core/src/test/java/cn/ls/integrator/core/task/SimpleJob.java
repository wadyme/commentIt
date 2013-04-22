/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * 
 * @author zhoumb 2011-5-5
 */
public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("execute job with key : " + context.getJobDetail().getKey());
		System.out.println("execute triggrt with key: " + context.getTrigger().getJobKey());
	}

}
