/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 简单job类
 * 
 * @author zhoumb 2011-4-21
 */
public class JobInfo implements Job {

	@Override
	public void execute(JobExecutionContext jobctx) throws JobExecutionException {
		System.out.println("===============================");
		String key = jobctx.getJobDetail().getKey().toString();
		System.out.println("execute job : key=" + key);
	}

}
