/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.manager.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.SendScheduleManager;
import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskCacheKey;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.model.Timer;
import cn.ls.integrator.core.model.TimerType;
import cn.ls.integrator.core.task.SendExecuter;
import cn.ls.integrator.core.task.TaskExecuterThread;
import cn.ls.integrator.core.utils.CornUtility;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.TaskParameterUtility;
import cn.ls.integrator.core.utils.TriggerNameUtility;

/**
 * 任务调度接口实现类(发送任务)
 * 
 * @author zhoumb 2011-5-5
 */
public class SendScheduleManagerImpl extends ScheduleManagerImpl implements
		SendScheduleManager {

	private Logger logger = Logger.getLogger(SendScheduleManagerImpl.class);
	private static SendScheduleManager instance;

	public SendScheduleManagerImpl() {
		super(TaskType.send);
	}

	public static synchronized SendScheduleManager getInstance() {
		if (instance == null) {
			instance = new SendScheduleManagerImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#addTask(java.lang.
	 * String, cn.ls.integrator.core.model.Task, java.util.List,
	 * java.lang.String)
	 */
	public Schedule addTask(Schedule schedule, Task task, List<Timer> timers,
			String taskContext) {
		if (task == null || StringUtility.isBlank(task.getName())) {
			throw new IntegratorException("task or taskName can't be null");
		}
		// tasks
		List<Task> existTasks = schedule.getTasks();
		if (existTasks == null) {
			existTasks = new ArrayList<Task>();
		}
		Task existTask = getTask(schedule, task.getName());
		if (existTask == null) {
			existTasks.add(task);
		} else {
			throw new IntegratorException("已存在的任务名——" + task.getName());
		}
		schedule.setTasks(existTasks);
		// associates
		List<Associate> existAssociate = schedule.getAssociates();
		if (existAssociate == null) {
			existAssociate = new ArrayList<Associate>();
		}
		// timers
		List<Timer> existTimers = schedule.getTimers();
		if (existTimers == null) {
			existTimers = new ArrayList<Timer>();
		}
		if (timers != null && timers.size() > 0) {
			for (Timer timer : timers) {
				boolean flag = true;
				for(Timer exTimer : existTimers){
					if(StringUtility.equals(timer.getName(), exTimer.getName())){
						flag = false;
						break;
					}
				}
				if (flag) {
					existTimers.add(timer);
				}
				existAssociate.add(new Associate(task.getName(), timer
						.getName()));
			}

			schedule.setTimers(existTimers);
			schedule.setAssociates(existAssociate);
		}
		// create file
		FileUtility fileUtility = FileUtility.getInstance();
		fileUtility.saveTaskFile(task, schedule.getName(),taskContext, TaskType.send);

		clearTaskTemp();
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#addTimer(java.lang
	 * .String, cn.ls.integrator.core.model.Timer, java.util.List)
	 */
	public Schedule addTimer(Schedule schedule, Timer timer, List<Task> tasks) {
		// timers
		List<Timer> existTimers = schedule.getTimers();
		if (existTimers == null) {
			existTimers = new ArrayList<Timer>();
		}
		for(Timer existTimer : existTimers){
			if(StringUtility.equals(timer.getName(), existTimer.getName())){
				throw new IntegratorException("已存在的时间策略 :" + timer.getName());
			}
		}
		existTimers.add(timer);
		schedule.setTimers(existTimers);

		// associates
		List<Associate> existAssociate = schedule.getAssociates();
		if (existAssociate == null) {
			existAssociate = new ArrayList<Associate>();
		}
		if (tasks != null && tasks.size() > 0) {
			for (Task task : tasks) {
				existAssociate.add(new Associate(task.getName(), timer.getName()));
			}
			schedule.setAssociates(existAssociate);
		}
		clearTaskTemp();
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#deleteSchedule(java
	 * .lang.String)
	 */
	public void deleteSchedule(String scheduleName) {
		stopSchedule(scheduleName);
		super.deleteSchedule(scheduleName);
		clearTaskTemp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#deleteTimer(java.lang
	 * .String, java.lang.String)
	 */
	public Schedule deleteTimer(Schedule schedule, String timerName) {
		List<Timer> existTimers = schedule.getTimers();
		if (existTimers != null && existTimers.size() > 0) {
			List<Associate> existJoins = schedule.getAssociates();
			if (existJoins != null && existJoins.size() > 0) {
				
				// delete TaskSchedule
				for (Associate join : schedule.getAssociates()) {
					if (join.getTimerName().equals(timerName)) {
						existJoins.remove(join);
					}
				}
				schedule.setAssociates(existJoins);
			}

			// delete timer
			for (Timer timer : schedule.getTimers()) {
				if (timer.getName().equals(timerName)) {
					existTimers.remove(timer);
					break;
				}
			}
			schedule.setTimers(existTimers);
			// save schedule
		}
		clearTaskTemp();
		return schedule;
	}

	public void executeCommond(String scheduleName, String taskName,
			Map<String, Object> headMessageParam) {
		TaskCacheKey key = new TaskCacheKey(scheduleName, taskName, TaskType.send);
		TaskCacheKey synObj = SendExecuter.getTaskCacheMap().get(key);
		if(synObj == null){
			new TaskExecuterThread(scheduleName, taskName, headMessageParam).start();
		}else {
			throw new IntegratorException("该任务正在执行，不需要再次执行");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#getAssociates(java
	 * .lang.String)
	 */
	public List<Associate> getAssociates(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		return schedule.getAssociates();
	}

	public boolean getScheduleState(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		try {
			Scheduler scheduler = getScheduler();
			List<Associate> associates = schedule.getAssociates();
			if (associates == null) {
				return false;
			}
			for (Associate ai : associates) {
				JobKey jobKey = new JobKey(TriggerNameUtility.getTriggerName(ai
						.getTaskName(), ai.getTimerName()), scheduleName);
				if (scheduler.checkExists(jobKey)) {
					return true;
				}
			}
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#getTimer(java.lang
	 * .String, java.lang.String)
	 */
	public Timer getTimer(Schedule schedule, String timerName) {
		Timer timer = null;
		List<Timer> timers = schedule.getTimers();
		for (Timer existTimer : timers) {
			if (existTimer.getName().equals(timerName)) {
				timer = existTimer;
				break;
			}
		}
		return timer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#getTimerList(java.
	 * lang.String)
	 */
	public List<Timer> getTimerList(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		if (schedule != null) {
			return schedule.getTimers();
		}
		return new ArrayList<Timer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#saveTimer(java.lang
	 * .String, java.lang.String, cn.ls.integrator.core.model.Timer)
	 */
	public Schedule updateTimer(Schedule schedule, String oldName, Timer timer) {
		// timers
		List<Timer> existTimers = schedule.getTimers();
		List<Timer> timers = new ArrayList<Timer>();
		if (existTimers != null && existTimers.size() > 0) {
			for (Timer existTimer : existTimers) {
				if (existTimer.getName().equals(oldName)) {
					timers.add(timer);
				} else {
					timers.add(existTimer);
				}
			}
		} else {
			timers.add(timer);
		}
		schedule.setTimers(timers);

		// joins
		List<Associate> existJoins = schedule.getAssociates();
		List<Associate> joins = new ArrayList<Associate>();
		if (existJoins != null && existJoins.size() > 0) {
			for (Associate associate : existJoins) {
				if (associate.getTimerName().equals(oldName)) {
					joins.add(new Associate(associate.getTaskName(), timer.getName()));
				} else {
					joins.add(associate);
				}
			}
		}
		schedule.setAssociates(joins);

		clearTaskTemp();
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#startSchedule(java
	 * .lang.String)
	 */
	public String startSchedule(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		assertScheduleAllowed(schedule);
		List<Associate> joins = schedule.getAssociates();
		if (joins == null || joins.size() == 0) {
			return "启动调度失败——该调度下的关联关系不存在";
		}
		Map<String, Task> taskMap = getTaskMap(schedule.getTasks());
		Map<String, Timer> timerMap = getTimerMap(schedule.getTimers());
		StringBuilder errorString = new StringBuilder();
		for (Associate join : joins) {
			Timer todoTimer = timerMap.get(join.getTimerName());
			if (todoTimer == null|| TimerType.manual.equals(todoTimer.getType())) {
				continue;
			}
			Task todoTask = taskMap.get(join.getTaskName());
			if (todoTask == null) {
				continue;
			}
			String expression = CornUtility.getExpression(todoTimer);
			if (expression.length() > 6) {
				String taskName = todoTask.getName();
				try {
					TaskParameterUtility.getInstance().assertParameters(scheduleName, todoTask, type);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					errorString.append("任务调度名：");
					errorString.append(scheduleName);
					errorString.append(",");
					errorString.append("任务名：");
					errorString.append(",");
					errorString.append(taskName);
					errorString.append(",");
					errorString.append("时间策略名：");
					errorString.append(todoTimer.getName());
					errorString.append(",");
					errorString.append("失败原因：参数未配置");
					errorString.append("\n");
					continue;
				}
				// 判断模板参数是否已经配置
				try {
					startJob(scheduleName, todoTimer.getName(), taskName,expression);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					errorString.append("任务调度名：");
					errorString.append(scheduleName);
					errorString.append(",");
					errorString.append("任务名：");
					errorString.append(",");
					errorString.append(taskName);
					errorString.append(",");
					errorString.append("时间策略名：");
					errorString.append(todoTimer.getName());
					errorString.append(",");
					errorString.append("失败原因：已经启动");
					errorString.append("\n");
				}
			}
		}
		if (errorString.length() == 0) {
			return "启动成功";
		}
		errorString.insert(0, "启动失败的任务有：\n");
		return errorString.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#startTask(java.lang
	 * .String, java.lang.String)
	 */
	public void startTask(String scheduleName, String taskName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleAllowed(schedule);
		Task todoTask = getTask(schedule, taskName);
		if (todoTask == null) {
			throw new IntegratorException("不存在名为" + taskName + "的任务");
		}
		TaskParameterUtility.getInstance().assertParameters(scheduleName, todoTask, type);
		List<Timer> timers = schedule.getTimers();
		Map<String, Timer> timerMap = new HashMap<String, Timer>();
		if (timers != null && timers.size() > 0) {
			for (Timer timer : timers) {
				timerMap.put(timer.getName(), timer);
			}
		}
		List<Associate> joins = schedule.getAssociates();
		if (joins == null || joins.size() == 0 || timerMap.size() == 0) {
			return;
		}
		List<Associate> associates = filterAssociatesByTaskName(joins, taskName);
		for (Associate associate : associates) {
			try {
				Timer todoTimer = timerMap.get(associate.getTimerName());
				if (todoTimer == null) {
					continue;
				}
				String expression = CornUtility.getExpression(todoTimer);
				if (expression.length() > 6) {
					startJob(scheduleName, todoTimer.getName(),	taskName, expression);
				}
			} catch (SchedulerException e) {
				logger.error("任务调度异常" , e);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#stopSchedule(java.
	 * lang.String)
	 */
	public void stopSchedule(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		List<Associate> joins = schedule.getAssociates();
		if (joins == null || joins.size() == 0) {
			return;
		}
		for (Associate associate : joins) {
			try {
				stopJob(scheduleName, associate.getTaskName(), associate.getTimerName());
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public Schedule updateTimerOnTask(Schedule schedule, Timer timer, Task task,String oldTimerName) {
		List<Associate> existAssociates = schedule.getAssociates();
		if (existAssociates == null) {
			existAssociates = new ArrayList<Associate>();
		}
		Associate associate = new Associate(task.getName(), timer.getName());
		if(existAssociates.contains(associate)){
			throw new IntegratorException("已存在的关联关系: " + task.getName() + "关联"  + timer.getName());
		}
		if(!existAssociates.remove(new Associate(task.getName(), oldTimerName))){
			throw new IntegratorException("不存在" + task.getName() + "关联" + oldTimerName + "的关联关系");
		}
		existAssociates.add(associate);
		schedule.setAssociates(existAssociates);
		schedule = addTimerToSchedule(schedule, timer);
		return schedule;
	}

}
