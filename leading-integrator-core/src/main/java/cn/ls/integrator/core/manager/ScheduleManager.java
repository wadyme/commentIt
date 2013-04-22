package cn.ls.integrator.core.manager;

import java.util.List;
import java.util.Map;

import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.Timer;

/**
 * 任务调度接口<br/>
 * 
 */
public interface ScheduleManager {
	/**
	 * 获得所有任务的Map集合
	 * @param taskList 任务的List集合
	 * @return 所有任务的Map<String, Task>
	 */
	Map<String, Task> getTaskMap(List<Task> taskList);
	/**
	 * 获得所有时间策略的Map集合
	 * @param timerList 时间侧路的List集合
	 * @return 所有时间策略的Map<String, Task>
	 */
	Map<String, Timer> getTimerMap(List<Timer> timerList);
	/**
	 * 增加任务调度
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 */
	void addSchedule(Schedule schedule);

	/**
	 * 增加一个空的任务调度，不包括相应的任务、时间策略、关联关系
	 * 
	 * @param name
	 *            任务调度名
	 * @param title
	 *            任务调度标题
	 * @param description
	 *            任务调度描述
	 * @return
	 */
	Schedule addSchedule(String name, String title, String description);
	
	/**
	 * 删除任务调度
	 * 
	 * @param scheduleName
	 *            任务调度名
	 */
	void deleteSchedule(String scheduleName);

	/**
	 * 删除任务
	 * 
	 * @param schedule
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 编辑后的Schedule对象
	 */
	Schedule deleteTask(Schedule schedule, String taskName);

	/**
	 * 部署交换任务
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param task
	 *            任务信息
	 * @param taskContext
	 *            任务内容
	 */
	void deployTask(String scheduleName, Task task, String taskContext);

	/**
	 * 任务调度列表
	 * 
	 * @return 任务调度列表
	 */
	List<Schedule> getList();

	/**
	 * 获得任务调度信息
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度详细信息
	 */
	Schedule getSchedule(String scheduleName);

	/***
	 * 获得任务信息
	 * 
	 * @param schedule
	 *            任务调度
	 * @param taskName
	 *            交换任务名
	 * @return 相应的任务信息
	 */
	Task getTask(Schedule schedule, String taskName);

	/**
	 * 获取任务内容
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 交换任务内容
	 */
	String getTaskContext(String scheduleName, String taskName);

	/**
	 * 获得任务列表
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度下的任务列表
	 */
	List<Task> getTaskList(String scheduleName);

	/**
	 * 保存任务调度信息
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 * @param oldName
	 *            任务调度原名
	 */
	void updateSchedule(Schedule schedule, String oldName);

	/**
	 * 保存任务
	 * 
	 * @param schedule
	 *            任务调度
	 * @param oldName
	 *            任务原名
	 * @param task
	 *            任务信息
	 * @param taskContext
	 *            任务内容
	 * @return 编辑后的Schedule对象
	 */
	Schedule updateTask(Schedule schedule, String oldName, Task task,
			String taskContext);

	/**
	 * 保存任务
	 * 
	 * @param schedule
	 *            任务调度
	 * @param oldName
	 *            任务原名
	 * @param task
	 *            任务信息
	 * @return 编辑后的Schedule对象
	 */
	Schedule updateTask(Schedule schedule, String oldName, Task task);

	/**
	 * 为某一个调度添加一个Timer，但不保存
	 * 
	 * @param schedule 
	 * @param newTimer
	 * @return 编辑后的Schedule对象
	 */
	Schedule addTimerToSchedule(Schedule schedule, Timer newTimer);
	
	/**
	 * 清空缓存中的调度任务
	 */
	void clearTaskTemp();

	/**
	 * 获得一个任务模版的所有参数名和参数标题
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return
	 */
	Map<String, String> getParameters(String scheduleName, String taskName);

	/**
	 * 获得一个任务模版的所有参数的值
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return 参数值的Map
	 */
	Map<String, String> getParameterValues(String scheduleName, String taskName);

	/**
	 * 为一个任务设置参数
	 * 
	 * @param schedule
	 *            任务调度
	 * @param taskName
	 *            任务名
	 * @param values
	 *            参数值
	 * @return 返回true:执行成功
	 * 
	 * @return 编辑后的Schedule对象
	 */
	Schedule setParameterValues(Schedule schedule, String taskName,
			String values);
	
	/**
	 * 若schedule为空，则抛出异常
	 * @param schedule
	 */
	void assertScheduleIsNotNull(Schedule schedule,String scheduleName);
	
	/**
	 * 停止任务
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 */
	void stopTask(String scheduleName, String taskName);
	
	void assertScheduleAllowed(Schedule schedule);

	/**
	 * 获得默认的连接
	 * @param schedule
	 * @return
	 */
	Connection getDefaultConnection(Schedule schedule);
	
	
	//9月15日添加 3个接口  zhaofei
	Map<String, String> getPublicParameters(String scheduleName);
	Map<String, String> getPublicParameterValues(String scheduleName);
	Schedule setPublicParameterValues(Schedule schedule, String values);
}
