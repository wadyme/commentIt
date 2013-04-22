/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.manager;

import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;

/**
 * 任务调度接口(接收任务)<br/>
 * 
 * @author zhoumb 2011-5-31
 */
public interface RecvScheduleManager extends ScheduleManager{

	Schedule addTask(Schedule schedule, Task task, String taskContext);
	

}
