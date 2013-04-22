/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.log.TaskExecuterLog;
import cn.ls.integrator.core.manager.SendScheduleManager;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskCacheKey;
import cn.ls.integrator.core.model.TaskType;

/**
 * 交换任务执行器类(发送任务)<br/>
 * 两种触发方式：<br/>
 * &nbsp;&nbsp;1. 时间策略到达时由TaskJob.execute()触发<br/>
 * &nbsp;&nbsp;2. 由ScheduleManagerImpl.executeCommond执行<br/>
 * 
 * @author zhoumb 2011-4-27
 */
public class SendExecuter extends TaskExecuter {

	private static Map<TaskCacheKey, TaskCacheKey> taskCacheMap;
	
	
	/**
	 * 执行一个交换任务<br/>
	 * step1: 根据scheduleName和taskName获得交换任务内容<br/>
	 * step2: 根据交换任务内容生成一个 StringXmlApplicationContext对象<br/>
	 * step3: 由StringXmlApplicationContext对象得到"agrsChannel"对应的MessageChannel<br/>
	 * step4: MessageChannel.send(message)开始一个交换任务的交换过程
	 * 
	 * @param scheduleName
	 *            任务调度名 String
	 * @param taskName
	 *            交换任务名 String
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void execute(String scheduleName, String taskName, Map<String, Object> headMessageParam){
		Map<String, Object> headMessageMap = packHeadMessage(scheduleName, taskName, headMessageParam);
		Map<Object, Object> bodyMessageMap = new HashMap<Object, Object>();
		Message<?> message = MessageBuilder.withPayload(bodyMessageMap).copyHeaders(headMessageMap).build();
		TaskCacheKey key = new TaskCacheKey(scheduleName, taskName, TaskType.send);
		TaskCacheKey synObj = getTaskCacheMap().get(key);
		if(synObj == null){
			synObj = key;
			getTaskCacheMap().put(key, synObj);
			TaskExecuterLog.logTaskStart(scheduleName, taskName);
			execute(scheduleName, taskName, message, TaskType.send);
			TaskExecuterLog.logTaskEnd(scheduleName, taskName);
			getTaskCacheMap().remove(key);
		}
	}

	/**
	 * 包装头信息<br/>
	 * 往代表包装头信息的headMessageMap里面存放执行时间以及交换任务名。<br/>
	 */
	private static Map<String, Object> packHeadMessage(String scheduleName, String taskName, Map<String, Object> headMessageParam){
		Map<String, Object> headMessageMap = null;
		if(headMessageParam == null){
			headMessageMap = new HashMap<String, Object>();
		}else {
			headMessageMap = headMessageParam;
		}
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		Task task = getSendScheduleManager().getTask(schedule, taskName);
		String scheduleTitle = schedule.getTitle();
		String taskTitle = task.getTitle();
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE, 5);
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE, new Date());
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_TITLE, scheduleTitle);
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		headMessageMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE, taskTitle);
		return headMessageMap;
	}

	private static SendScheduleManager getSendScheduleManager() {
		return SendScheduleManagerImpl.getInstance();
	}

	public static synchronized Map<TaskCacheKey, TaskCacheKey> getTaskCacheMap() {
		if(taskCacheMap == null){
			taskCacheMap = new HashMap<TaskCacheKey, TaskCacheKey>();
		}
		return taskCacheMap;
	}
}
