/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.intergrator.manager;

import java.util.List;

import cn.ls.intergrator.model.Associate;
import cn.ls.intergrator.model.Schedule;
import cn.ls.intergrator.model.Task;
import cn.ls.intergrator.model.Timer;

/**
 * 任务调度接口
 * 
 * @author zhoumb 2011-5-18
 */
public interface ScheduleManager {
	/**
	 * 增加任务调度
	 */
	void addSchedule(Schedule schedule);

	/**
	 * 保存任务
	 */
	void saveTask(String scheduleName, String oldName, Task task);

	/**
	 * 保存时间策略
	 */
	void saveTimer(String scheduleName, String oldName, Timer timer);

	/**
	 * 任务调度列表
	 * 
	 * @return
	 */
	List<Schedule> getList();

	/**
	 * 任务调度信息
	 * 
	 * @param scheduleName
	 * @return
	 */
	Schedule getSchedule(String scheduleName);

	/**
	 * 删除任务调度
	 * 
	 * @param scheduleName
	 */
	void deleteSchedule(String scheduleName);

	/**
	 * 保存任务调度信息
	 * 
	 * @param schedule
	 */
	void save(Schedule schedule, String oldName);

	/**
	 * 启动任务调度
	 * 
	 * @param scheduleName
	 */
	void startSchedule(String scheduleName);

	/**
	 * 停止任务调度
	 * 
	 * @param scheduleName
	 */
	void stopSchedule(String scheduleName);

	/**
	 * 任务列表
	 * 
	 * @param scheduleName
	 * @return
	 */
	List<Task> getTaskList(String scheduleName);

	/***
	 * 任务信息
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @return
	 */
	Task getTask(String scheduleName, String taskName);

	/**
	 * 部署交换任务
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param context
	 */
	void deployTask(String scheduleName, Task task);

	/**
	 * 增加任务
	 * 
	 * @param scheduleName
	 * @param task
	 * @param timers
	 */
	void addTask(String scheduleName, Task task, List<Timer> timers);

	/**
	 * 删除任务
	 * 
	 * @param scheduleName
	 * @param taskName
	 */
	void deleteTask(String scheduleName, String taskName);

	/**
	 * 启动交换任务
	 * 
	 * @param scheduleName
	 * @param taskName
	 */
	void startTask(String scheduleName, String taskName);

	/**
	 * 停止任务
	 * 
	 * @param scheduleName
	 * @param taskName
	 */
	void stopTask(String scheduleName, String taskName);

	/**
	 * 时间策略列表
	 * 
	 * @param scheduleName
	 * @return
	 */
	List<Timer> getTimerList(String scheduleName);

	/**
	 * 时间策略信息
	 * 
	 * @param scheduleName
	 * @param timerName
	 * @return
	 */
	Timer getTimer(String scheduleName, String timerName);

	/**
	 * 增加时间策略
	 * 
	 * @param scheduleName
	 * @param timer
	 * @param tasks
	 */
	void addTimer(String scheduleName, Timer timer, List<Task> tasks);

	/**
	 * 删除时间策略
	 * 
	 * @param scheduleName
	 * @param timerName
	 */
	void deleteTimer(String scheduleName, String timerName);

	/**
	 * 关联信息
	 * 
	 * @param scheduleName
	 * @return
	 */
	List<Associate> getAssociates(String scheduleName);

}
