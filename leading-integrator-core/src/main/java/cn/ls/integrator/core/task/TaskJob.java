/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.NullChannel;

import cn.ls.integrator.core.utils.TriggerNameUtility;

/**
 * 任务调度处理类,每一个对象是一个任务，在这个任务上定义一个或多个时间策略
 * 当时间策略对应的时刻到达时，将会执行这个任务。<br/>
 * 入口方法是execute方法，这个方法将会去调用交换任务执行器。
 * 
 * @author zhoumb 2011-5-17
 */
public class TaskJob implements Job {

	private Logger logger = Logger.getLogger(TaskJob.class);
	protected MessageChannel getDefaultOutputChannel() {
		return new NullChannel();
	}

	/**
	 * 实现Job类的execute方法，当时间策略定义的时刻到达时，将会执行此方法。<br/>
	 * step1: 包装头信息
	 * step2: 调用交换任务执行器 cn.li.integrator.core.task.Executer
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String triggerName = context.getJobDetail().getKey().getName();
			String taskName = TriggerNameUtility.getTaskName(triggerName);
			String scheduleName = context.getJobDetail().getKey().getGroup();
			SendExecuter.execute(scheduleName, taskName, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
