/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.manager.impl;


import java.util.ArrayList;
import java.util.List;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.RecvScheduleManager;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.utils.StringUtility;

/**
 * 任务调度接口实现类(接收任务)
 * 
 * @author zhoumb 2011-5-31
 */
public class RecvScheduleManagerImpl extends ScheduleManagerImpl implements RecvScheduleManager {

	private static RecvScheduleManager instance ;
	
	public RecvScheduleManagerImpl(){
		super(TaskType.recv);
	}

	public static synchronized RecvScheduleManager getInstance() {
		if (instance == null) {
			instance = new RecvScheduleManagerImpl();
		}
		return instance;
	}

	@Override
	public Schedule addTask(Schedule schedule, Task task, String taskContext) {
		List<Task> tasks = schedule.getTasks();
		if (tasks == null) {
			tasks = new ArrayList<Task>();
			tasks.add(task);
		} else {
			boolean isExists = false;
			for (Task existsTask : tasks) {
				if (StringUtility.equals(existsTask.getName(), task.getName())) {
					isExists = true;
					throw new IntegratorException("重复的任务名：" + task.getName());
				}
			}
			if (!isExists) {
				tasks.add(task);
				deployTask(schedule.getName(), task,taskContext);
			}
		}
		schedule.setTasks(tasks);
		return schedule;
	}

}
