/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.manager;

import java.util.List;
import java.util.Map;

import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.Timer;

/**
 * 任务调度接口(发送任务)<br/>
 * 包括任务调度、任务、时间策略的增删改查以及任务调度和任务的启动和停止
 * @author zhoumb 2011-5-5
 */
public interface SendScheduleManager extends ScheduleManager{

	
	
	/**
	 * 增加任务、并在该任务上添加多个时间策略
	 * 
	 * @param schedule 任务调度
	 * @param task 任务信息
	 * @param timers  需要在该任务上添加的时间策略
	 * @param taskContext 任务文件内容
	 * @return 编辑后的Schedule对象
	 */
	Schedule addTask(Schedule schedule, Task task, List<Timer> timers, String taskContext);

	/**
	 * 增加时间策略，只保存任务调度文件，不存任务文件
	 * 
	 * @param schedule 任务调度
	 * @param timer 时间策略信息
	 * @param tasks 任务列表
	 * @return 编辑后的Schedule对象
	 */
	Schedule addTimer(Schedule schedule, Timer timer, List<Task> tasks);


	/**
	 * 删除时间策略
	 * 
	 * @param schedule 任务调度
	 * @param timerName 时间策略名
	 * @return 编辑后的Schedule对象
	 */
	Schedule deleteTimer(Schedule schedule, String timerName);

	/**
	 * 立即执行一个任务
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 * @param headMessageParam 命令参数
	 */
	void executeCommond(String scheduleName, String taskName, Map<String, Object> headMessageParam);

	/**
	 * 获得任务调度下所有关联信息
	 * 
	 * @param scheduleName 任务调度名
	 * @return 任务调度下所有关联信息
	 */
	List<Associate> getAssociates(String scheduleName);

	/**
	 * 获得任务调度的状态
	 * @param scheduleName
	 * @return 状态 true 正在运行 ; false 停止 
	 */
	boolean getScheduleState(String scheduleName);
	

	/**
	 * 时间策略信息
	 * 
	 * @param schedule 任务调度名
	 * @param timerName 时间策略名
	 * @return 时间策略信息
	 */
	Timer getTimer(Schedule schedule, String timerName);

	/**
	 * 获得时间策略列表
	 * 
	 * @param scheduleName 任务调度名
	 * @return 时间策略列表
	 */
	List<Timer> getTimerList(String scheduleName);
	

	/**
	 * 保存时间策略
	 * 
	 * @param schedule 任务调度
	 * @param oldName 时间策略原名
	 * @param timer 保存的时间策略
	 *  @return 编辑后的Schedule对象
	 */
	Schedule updateTimer(Schedule schedule, String oldName, Timer timer);

	/**
	 * 启动任务调度
	 * 
	 * @param scheduleName 任务调度名
	 */
	String startSchedule(String scheduleName);

	/**
	 * 启动交换任务
	 * 
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 */
	void startTask(String scheduleName, String taskName);

	/**
	 * 停止任务调度
	 * 
	 * @param scheduleName 任务调度名
	 */
	void stopSchedule(String scheduleName);
	
	/**
	 * 将某个task关联的timer修改掉，但是其他task下相应的timer不变
	 * @param schedule 任务调度
	 * @param timer 新的时间策略
	 * @param task 任务
	 * @param oldTimerName 以前的时间策略名
	 * @return 编辑后的Schedule对象
	 */
	Schedule updateTimerOnTask(Schedule schedule, Timer timer, Task task,String oldTimerName);

}
