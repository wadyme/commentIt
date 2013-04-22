package cn.ls.integrator.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.common.TaskUtility;
import cn.ls.integrator.components.manager.ExchangeErrorLogManager;
import cn.ls.integrator.components.manager.ExchangeStateManager;
import cn.ls.integrator.components.manager.FileExchangeLogManager;
import cn.ls.integrator.components.manager.FileStateManager;
import cn.ls.integrator.components.manager.LastUpdateTimeManager;
import cn.ls.integrator.components.manager.MessageLogManager;
import cn.ls.integrator.components.manager.impl.ExchangeErrorLogManagerImpl;
import cn.ls.integrator.components.manager.impl.ExchangeStateManagerImpl;
import cn.ls.integrator.components.manager.impl.FileExchangeLogManagerImpl;
import cn.ls.integrator.components.manager.impl.FileStateManagerImpl;
import cn.ls.integrator.components.manager.impl.LastUpdateTimeManagerImpl;
import cn.ls.integrator.components.manager.impl.MessageLogManagerImpl;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.ExchangeState;
import cn.ls.integrator.core.log.message.FileExchangeLog;
import cn.ls.integrator.core.log.message.MessageLog;
import cn.ls.integrator.core.manager.ConnectionManager;
import cn.ls.integrator.core.manager.RecvScheduleManager;
import cn.ls.integrator.core.manager.SendScheduleManager;
import cn.ls.integrator.core.manager.impl.ConnectionManagerImpl;
import cn.ls.integrator.core.manager.impl.RecvScheduleManagerImpl;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.ConnectionType;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.model.Timer;
import cn.ls.integrator.core.qscanner.Queue;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.server.qscanner.tongq.TongQueue;
import cn.ls.integrator.server.qscanner.utils.QueueScannerUtils;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@WebService
public class ScheduleService {

	private ExchangeErrorLogManager getExchangeErrorLogManager() {
		return ExchangeErrorLogManagerImpl.getInstance();
	}

	private LastUpdateTimeManager getLastUpdateTimeManager() {
		return LastUpdateTimeManagerImpl.getInstance();
	}

	private ExchangeStateManager getExchangeStateManager() {
		return ExchangeStateManagerImpl.getInstance();
	}
	
	private FileExchangeLogManager getFileExchangeLogManager(){
		return FileExchangeLogManagerImpl.getInstance();
	}
	
	private FileStateManager getFileStateManager(){
		return FileStateManagerImpl.getInstance();
	}

	private RecvScheduleManager getRecvScheduleManager() {
		return RecvScheduleManagerImpl.getInstance();
	}

	private SendScheduleManager getSendScheduleManager() {
		return SendScheduleManagerImpl.getInstance();
	}
	
	private ConnectionManager getConnectionManager(){
		return ConnectionManagerImpl.getInstance();
	}
	private MessageLogManager<MessageLog> getMessageLogManager(){
		return new MessageLogManagerImpl();
	}

	/**
	 * 增加一个空的任务调度，不包括相应的任务、时间策略、关联关系 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param scheduleTitle
	 *            任务调度标题
	 * @param scheduleDescription
	 *            任务调度描述
	 */
	@WebMethod(exclude = true)
	protected void addEmptyRecvSchedule(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "scheduleTitle") String scheduleTitle,
			@WebParam(name = "scheduleDescription") String scheduleDescription) {
		getRecvScheduleManager().addSchedule(scheduleName, scheduleTitle,
				scheduleDescription);
	}

	/**
	 * 增加一个空的任务调度，不包括相应的任务、时间策略、关联关系 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param scheduleTitle
	 *            任务调度标题
	 * @param scheduleDescription
	 *            任务调度描述
	 */
	@WebMethod(exclude = true)
	protected void addEmptySendSchedule(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "scheduleTitle") String scheduleTitle,
			@WebParam(name = "scheduleDescription") String scheduleDescription) {
		getSendScheduleManager().addSchedule(scheduleName, scheduleTitle,
				scheduleDescription);
	}

	/**
	 * 增加任务调度(接收任务)
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 */
	@WebMethod(exclude = true)
	protected void addRecvSchedule(
			@WebParam(name = "schedule") Schedule schedule) {
		getRecvScheduleManager().addSchedule(schedule);
	}

	/**
	 * 增加任务调度(发送任务)
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 */
	@WebMethod(exclude = true)
	protected void addSendSchedule(
			@WebParam(name = "schedule") Schedule schedule) {
		getSendScheduleManager().addSchedule(schedule);
	}

	/**
	 * 增加任务、并在该任务上添加多个时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param task
	 *            任务信息
	 * @param timers
	 *            需要在该任务上添加的时间策略
	 * @param taskContext
	 *            任务文件内容
	 */
	@WebMethod(exclude = true)
	protected void addSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "timers") List<Timer> timers,
			@WebParam(name = "taskContext") String taskContext) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().addTask(schedule, task, timers,
				taskContext);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 只增加一个时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param newTimer
	 *            时间策略
	 */
	@WebMethod(exclude = true)
	protected void addTimer(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "newTimer") Timer newTimer) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		getSendScheduleManager().assertScheduleIsNotNull(schedule, scheduleName);
		schedule = getSendScheduleManager().addTimerToSchedule(schedule, newTimer);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 增加时间策略并保存响应的交换任务文件 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timer
	 *            时间策略
	 * @param tasks
	 *            交换任务列表
	 * @param taskContexts
	 *            交换任务内容列表
	 */

	@WebMethod(exclude = true)
	protected void addTimerWithTasks(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timer") Timer timer,
			@WebParam(name = "tasks") List<Task> tasks,
			@WebParam(name = "taskContexts") List<String> taskContexts) {
		getSendScheduleManager().stopSchedule(scheduleName);
		deploySendTasks(scheduleName, tasks, taskContexts);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().addTimer(schedule, timer, tasks);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 删除关联关系 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timerName
	 *            时间策略名
	 * @param taskName
	 *            交换任务名
	 */
	@WebMethod(exclude = true)
	protected void deleteAssociate(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timerName") String timerName,
			@WebParam(name = "taskName") String taskName) {
		getSendScheduleManager().stopSchedule(scheduleName);
		// TODO 如果只有一个任务，删除任务？如果只有一个时间策略，删除时间策略？
		Schedule schedule = getSendSchedule(scheduleName);
		List<Associate> associates = schedule.getAssociates();
		List<Associate> newAssociates = new ArrayList<Associate>();
		for (Associate associate : associates) {
			if (StringUtility.equals(associate.getTaskName(), taskName)
					&& StringUtility
							.equals(associate.getTimerName(), timerName)) {
				continue;
			}
			newAssociates.add(associate);
		}
		schedule.setAssociates(newAssociates);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 删除任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 */
	@WebMethod(exclude = true)
	protected void deleteSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().deleteTask(schedule, taskName);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 删除时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timerName
	 *            时间策略名
	 */
	@WebMethod(exclude = true)
	protected void deleteTimer(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timerName") String timerName) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().deleteTimer(schedule, timerName);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 部署多个交换任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param tasks
	 *            交换任务信息列表
	 * @param contexts
	 *            交换任务内容列表
	 */
	@WebMethod(exclude = true)
	protected void deployRecvTasks(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "tasks") List<Task> tasks,
			@WebParam(name = "contexts") List<String> contexts) {
		if (tasks == null || tasks.size() == 0 || contexts == null
				|| contexts.size() == 0 || tasks.size() != contexts.size()) {
			throw new RuntimeException(
					"tasks and contexts can not be null and has a same size");
		}
		for (int i = 0; i < tasks.size(); i++) {
			getRecvScheduleManager().deployTask(scheduleName, tasks.get(i),
					contexts.get(i));
		}
	}

	/**
	 * 部署交换任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务信息
	 * @param context
	 *            交换任务内容
	 */
	@WebMethod(exclude = true)
	protected void deploySendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "taskContext") String taskContext) {
		getSendScheduleManager().deployTask(scheduleName, task, taskContext);
	}

	/**
	 * 部署多个交换任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param tasks
	 *            交换任务信息列表
	 * @param contexts
	 *            交换任务内容列表
	 */
	@WebMethod(exclude = true)
	protected void deploySendTasks(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "tasks") List<Task> tasks,
			@WebParam(name = "contexts") List<String> contexts) {
		if (tasks == null || tasks.size() == 0 || contexts == null
				|| contexts.size() == 0 || tasks.size() != contexts.size()) {
			throw new RuntimeException(
					"tasks and contexts can not be null and has a same size");
		}
		for (int i = 0; i < tasks.size(); i++) {
			getSendScheduleManager().deployTask(scheduleName, tasks.get(i),
					contexts.get(i));
		}
	}

	/**
	 * 时间策略信息 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timerName
	 *            时间策略名
	 * @return 时间策略信息
	 */
	@WebMethod(exclude = true)
	protected Timer getTimer(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timerName") String timerName) {
		Schedule schedule = getSendSchedule(scheduleName);
		return getSendScheduleManager().getTimer(schedule,
				timerName);
	}

	/**
	 * 获得时间策略列表 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 时间策略列表
	 */
	@WebMethod(exclude = true)
	protected List<Timer> getTimerList(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getSendScheduleManager().getTimerList(scheduleName);
	}

	/**
	 * 保存任务调度信息 (接收任务)
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 * @param oldName
	 *            任务调度原名
	 */
	@WebMethod(exclude = true)
	protected void updateRecvSchedule(
			@WebParam(name = "schedule") Schedule schedule,
			@WebParam(name = "oldName") String oldName) {
		getRecvScheduleManager().updateSchedule(schedule, oldName);
	}

	/**
	 * 启动交换任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 */
	@WebMethod(exclude = true)
	protected void startSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		getSendScheduleManager().startTask(scheduleName, taskName);
	}

	/**
	 * 停止任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 */
	@WebMethod(exclude = true)
	protected void stopSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		getSendScheduleManager().stopTask(scheduleName, taskName);
	}

	/**
	 * 增加任务调度 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param scheduleTitle
	 *            任务调度标题
	 * @param scheduleDescription
	 *            任务调度描述
	 * @param tasks
	 *            任务列表
	 * @param taskContexts
	 *            任务文件内容列表
	 */
	public void addRecvSchedule(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "scheduleTitle") String scheduleTitle,
			@WebParam(name = "scheduleDescription") String scheduleDescription,
			@WebParam(name = "tasks") List<Task> tasks,
			@WebParam(name = "taskContexts") List<String> taskContexts) {
		Schedule schedule = new Schedule();
		schedule.setName(scheduleName);
		schedule.setTitle(scheduleTitle);
		schedule.setDescription(scheduleDescription);
		schedule.setTasks(tasks);
		deployRecvTasks(scheduleName, tasks, taskContexts);
		getRecvScheduleManager().addSchedule(schedule);
	}
/**
 * 增加接收任务
 * @param scheduleName 任务调度名
 * @param task 任务信息
 * @param taskContext 任务内容
 */
	public void addRecvTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "taskContext") String taskContext) {
		if (task == null || StringUtility.isBlank(task.getName())) {
			throw new IntegratorException("任务或任务名不能为空");
		}
		if(StringUtility.isBlank(taskContext)){
			throw new IntegratorException("任务文件内容不可为空");
		}
		Schedule schedule = getRecvSchedule(scheduleName);
		schedule = getRecvScheduleManager().addTask(schedule,task,taskContext);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 增加任务调度 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param scheduleTitle
	 *            任务调度标题
	 * @param scheduleDescription
	 *            任务调度描述
	 * @param tasks
	 *            任务列表
	 * @param timers
	 *            时间策略列表
	 * @param taskContexts
	 *            任务文件内容列表
	 * @param associates
	 *            关联关系列表
	 */
	public void addSendSchedule(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "scheduleTitle") String scheduleTitle,
			@WebParam(name = "scheduleDescription") String scheduleDescription,
			@WebParam(name = "tasks") List<Task> tasks,
			@WebParam(name = "timers") List<Timer> timers,
			@WebParam(name = "taskContexts") List<String> taskContexts,
			@WebParam(name = "associates") List<Associate> associates) {
		Schedule schedule = new Schedule();
		schedule.setName(scheduleName);
		schedule.setTitle(scheduleTitle);
		schedule.setDescription(scheduleDescription);
		schedule.setTasks(tasks);
		schedule.setTimers(timers);
		schedule.setAssociates(associates);
		deploySendTasks(scheduleName, tasks, taskContexts);
		getSendScheduleManager().addSchedule(schedule);
	}

	/**
	 * 在时间策略上增加任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param task
	 *            交换任务
	 * @param timer
	 *            时间策略
	 */
	public void addTaskOnTimer(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "timerName") String timerName,
			@WebParam(name = "taskContext") String taskContext) {
		Schedule schedule = getSendSchedule(scheduleName);
		Timer timer = getSendScheduleManager()
				.getTimer(schedule, timerName);
		if (timer == null) {
			throw new IntegratorException("不存在名为" + timerName + "的时间策略");
		}
		getSendScheduleManager().stopSchedule(scheduleName);
		getSendScheduleManager().deployTask(scheduleName, task, taskContext);
		List<Associate> associates = schedule.getAssociates();
		Associate associate = new Associate();
		associate.setTaskName(task.getName());
		associate.setTimerName(timerName);
		boolean flag = associates.contains(associate);
		if (flag) {
			throw new IntegratorException("已存在的关联关系：时间策略" + timerName
					+ "已关联任务任务" + task.getName());
		}
		associates.add(associate);
		schedule.setAssociates(associates);
		List<Task> tasks = schedule.getTasks();
		if (!tasks.contains(task)) {
			tasks.add(task);
		}
		schedule.setTasks(tasks);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 在任务上添加时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timer
	 *            时间策略
	 * @param taskName
	 *            任务名
	 */
	public void addTimerOnTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timer") Timer timer,
			@WebParam(name = "taskName") String taskName) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		Task task = getSendScheduleManager().getTask(schedule, taskName);
		if (task == null) {
			throw new IntegratorException("不存在名为" + taskName + "的任务");
		}
		schedule = getSendScheduleManager().addTimerToSchedule(schedule, timer);
		List<Associate> associates = schedule.getAssociates();
		if(associates == null){
			associates = new ArrayList<Associate>();
		}
		Associate associate = new Associate();
		associate.setTaskName(taskName);
		associate.setTimerName(timer.getName());
		boolean flag = associates.contains(associate);
		if (flag) {
			throw new IntegratorException("已存在的关联关系：任务" + taskName + "已关联时间策略"
					+ timer.getName());
		}
		associates.add(associate);
		schedule.setAssociates(associates);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 删除任务调度 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 */
	public void deleteRecvSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		getRecvScheduleManager().deleteSchedule(scheduleName);
	}

	/**
	 * 删除任务 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 */
	public void deleteRecvTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Schedule schedule = getRecvSchedule(scheduleName);
		schedule = getRecvScheduleManager().deleteTask(schedule, taskName);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 删除任务调度 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 */
	public void deleteSendSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		getSendScheduleManager().deleteSchedule(scheduleName);
	}

	/**
	 * 删除时间策略下的任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timerName
	 *            时间策略名
	 * @param taskName
	 *            交换任务名
	 */
	public void deleteTaskOnTimer(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "timerName") String timerName,
			@WebParam(name = "taskName") String taskName) {
		deleteAssociate(scheduleName, timerName, taskName);
	}

	/**
	 * 删除任务上的时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param timerName
	 *            时间策略名
	 * @param taskName
	 *            交换任务名
	 */
	public void deleteTimerOnTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "timerName") String timerName) {
		deleteAssociate(scheduleName, timerName, taskName);
	}

	/**
	 * 删除时间戳
	 * 
	 * @param scheduleName
	 * @param taskName
	 */
	public void deleteTimestamp(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		getLastUpdateTimeManager().delete(scheduleName, taskName);
		getExchangeStateManager().deleteState(scheduleName, taskName,"send");
	}
	
	public void initializeFileTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName){
		getFileStateManager().delete(scheduleName, taskName);
		getFileExchangeLogManager().deleteLog(scheduleName, taskName, "send");
		getExchangeStateManager().deleteState(scheduleName, taskName, "send");
	}

	/**
	 * 部署交换任务 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务信息
	 * @param context
	 *            交换任务内容
	 */
	public void deployRecvTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "taskContext") String taskContext) {
		getRecvScheduleManager().deployTask(scheduleName, task, taskContext);
	}

	/**
	 * 是否存在错误日志（接收任务）
	 * @param scheduleName
	 * @param taskName
	 * @param sendNodeName
	 * @param recvNodeName
	 */
	public boolean existRecvErrorLog(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "sendNodeName") String sendNodeName,
			@WebParam(name = "recvNodeName") String recvNodeName){
		return getExchangeErrorLogManager().hasErrorLog(scheduleName, taskName, sendNodeName, recvNodeName, "recv");
	}

	/**
	 * 是否存在错误日志（发送任务）
	 * @param scheduleName
	 * @param taskName
	 * @param sendNodeName
	 * @param recvNodeName
	 */
	public boolean existSendErrorLog(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "sendNodeName") String sendNodeName,
			@WebParam(name = "recvNodeName") String recvNodeName){
		return getExchangeErrorLogManager().hasErrorLog(scheduleName, taskName, sendNodeName, recvNodeName, "send");
	}

	/**
	 * 获得任务调度下所有关联信息 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度下所有关联信息
	 */
	public List<Associate> getAssociates(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getSendScheduleManager().getAssociates(scheduleName);
	}

	/**
	 * 获得交换状态日志列表
	 * 
	 * @param parameters
	 *            过滤参数
	 * @param offset
	 *            起始位置
	 * @param limit
	 *            查询条数
	 * @return 查询到的日志列表
	 */
	public String getLogList(@WebParam(name = "parameters") String parameters,
			@WebParam(name = "offset") int offset,
			@WebParam(name = "limit") int limit) {
		Map<String, Object> param = new JSONDeserializer<Map<String, Object>>()
				.deserialize(parameters);
		List<ExchangeState> resultLogList = getExchangeStateManager().getList(
				param, offset, limit);
		List<Map<String, Object>> relustMapList = new ArrayList<Map<String, Object>>();
		for (ExchangeState exchangeState : resultLogList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IntegratorConstants.MESSAGE_HEADER_EXCHANGE_TASK_NAME,
					exchangeState.getExchangeTaskName());
			map.put(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE,
					exchangeState.getCompleteSize());
			map.put(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, exchangeState
					.getLogType());
			map.put(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, exchangeState
					.getSendNodeName());
			map.put(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, exchangeState
					.getRecvNodeName());
			map.put(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE,
					exchangeState.getGuoupSize());
			map.put(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE,
					exchangeState.getPercentage());
			map.put(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME,
					exchangeState.getScheduleName());
			map.put(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_TITLE,
					exchangeState.getScheduleTitle());
			map.put(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE,
					exchangeState.getReceiveDate());
			map.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, exchangeState
					.getTaskName());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_NAME, exchangeState.getFileName());
			map.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE,
					exchangeState.getTaskTitle());
			map.put(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE,
					exchangeState.getProduceDate());
			map.put(IntegratorConstants.MESSAGE_HEADER_HISTORY_TOTAL, 
					exchangeState.getHistoryTotal());
			map.put(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE, 
					exchangeState.getMessageType());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_NUMBER, 
					exchangeState.getFileTotalNumber());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_NUMBER, 
					exchangeState.getFileCompleteNumber());
			map.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID, 
					exchangeState.getGroupId());
			relustMapList.add(map);
		}
		JSONSerializer serializer = new JSONSerializer();
		return serializer.deepSerialize(relustMapList);
	}
	
	/**
	 *  根据相应的参数，获得文件传输的日志记录
	 * @param parameters
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getFileExchangeLogList(@WebParam(name = "parameters") String parameters,
			@WebParam(name = "offset") int offset,
			@WebParam(name = "limit") int limit){
		Map<String, Object> param = new JSONDeserializer<Map<String, Object>>().deserialize(parameters);
		List<FileExchangeLog> resultLogList = getFileExchangeLogManager().getList(param, offset, limit);
		List<Map<String, Object>> relustMapList = new ArrayList<Map<String, Object>>();
		for (FileExchangeLog fileExchangeLog : resultLogList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID, fileExchangeLog.getGroupId());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_GROUP_ID, fileExchangeLog.getFileGroupId());
			map.put(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, fileExchangeLog.getSendNodeName());
			map.put(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, fileExchangeLog.getRecvNodeName());
			map.put(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, fileExchangeLog.getLogType());
			map.put(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, fileExchangeLog.getScheduleName());
			map.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, fileExchangeLog.getTaskName());
			map.put(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE, fileExchangeLog.getPercentage());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_NAME, fileExchangeLog.getFileName());
			map.put(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE, fileExchangeLog.getRecieveDate());
			map.put(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE, fileExchangeLog.getProduceDate());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES, fileExchangeLog.getTotalSize());
			map.put(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES, fileExchangeLog.getCompleteSize());
			relustMapList.add(map);
		}
		JSONSerializer serializer = new JSONSerializer();
		return serializer.deepSerialize(relustMapList);
	}

	/**
	 * 获得错误日志列表（接收任务）
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getErrorLogList(@WebParam(name = "parameters") String parameters,
			@WebParam(name = "offset") int offset,
			@WebParam(name = "limit") int limit) {
		Map<String, Object> param = new JSONDeserializer<Map<String, Object>>().deserialize(parameters);
		List<Map<?, ?>> resultLogList = getExchangeErrorLogManager()
				.getLogList(param, offset, limit);
		JSONSerializer serializer = new JSONSerializer();
		List<String> excludesFields = new ArrayList<String>();
		excludesFields.add("id");
		 excludesFields.add("correlationId");
		serializer.setExcludes(excludesFields);
		return serializer.deepSerialize(resultLogList);
	}
	
	/**
	 * 删除发送错误日志
	 * @param scheduleName
	 * @param taskName
	 * @param sendNodeName
	 * @param recvNodeName
	 */
	public void deleteSendErrorLog(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "sendNodeName") String sendNodeName,
			@WebParam(name = "recvNodeName") String recvNodeName){
		getExchangeErrorLogManager().delete(scheduleName, taskName, sendNodeName, recvNodeName, "send");
	}
	
	/**
	 * 删除接收错误日志
	 * @param scheduleName
	 * @param taskName
	 * @param sendNodeName
	 * @param recvNodeName
	 */
	public void deleteRecvErrorLog(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "sendNodeName") String sendNodeName,
			@WebParam(name = "recvNodeName") String recvNodeName){
		getExchangeErrorLogManager().delete(scheduleName, taskName, sendNodeName, recvNodeName, "recv");
	}

	/**
	 * 任务调度列表 (接收任务)
	 * 
	 * @return 任务调度列表
	 */
	public List<Schedule> getRecvScheduleList() {
		return getRecvScheduleManager().getList();
	}

	/***
	 * 获得任务信息 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 相应的任务信息
	 */
	public Task getRecvTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Schedule schedule = getRecvSchedule(scheduleName);
		return (Task) getRecvScheduleManager().getTask(schedule, taskName);
	}

	/**
	 * 获得任务内容 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 交换任务内容
	 */
	public String getRecvTaskContext(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		return getRecvScheduleManager().getTaskContext(scheduleName, taskName);
	}

	/**
	 * 获得任务列表 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度下的任务列表
	 */
	public List<Task> getRecvTaskList(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getRecvScheduleManager().getTaskList(scheduleName);
	}

	/**
	 * 任务调度信息 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度详细信息
	 */
	public Schedule getRecvSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		Schedule schedule = getRecvScheduleManager().getSchedule(scheduleName);
		getRecvScheduleManager()
				.assertScheduleIsNotNull(schedule, scheduleName);
		return schedule;
	}

	/**
	 * 任务调度信息 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度详细信息
	 */
	public Schedule getSendSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		getSendScheduleManager()
				.assertScheduleIsNotNull(schedule, scheduleName);
		return schedule;
	}

	/**
	 * 任务调度列表 (发送任务)
	 * 
	 * @return 任务调度列表
	 */
	public List<Schedule> getSendScheduleList() {
		return getSendScheduleManager().getList();
	}
	/**
	 * 获得发送任务调度的状态
	 * @param scheduleName 任务调度名
	 * @return true表示启动，false表示停止
	 */
	public boolean getSendScheduleState(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getSendScheduleManager().getScheduleState(scheduleName);
	}

	/***
	 * 获得任务信息 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 相应的任务信息
	 */
	public Task getSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Schedule schedule = getSendSchedule(scheduleName);
		return getSendScheduleManager().getTask(schedule, taskName);
	}

	/**
	 * 获得任务内容 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            交换任务名
	 * @return 交换任务内容
	 */
	public String getSendTaskContext(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		return getSendScheduleManager().getTaskContext(scheduleName, taskName);
	}

	/**
	 * 获得任务列表 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度下的任务列表
	 */
	public List<Task> getSendTaskList(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getSendScheduleManager().getTaskList(scheduleName);
	}

	/**
	 * 获得当前时间戳
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @return 时间毫秒值
	 */
	public Long getTimestamp(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		return getLastUpdateTimeManager().get(scheduleName, taskName);
	}

	/**
	 * 保存任务 (接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param oldName
	 *            任务原名
	 * @param task
	 *            任务信息
	 * @param taskContext
	 *            任务内容
	 */
	public void updateRecvTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "oldName") String oldName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "taskContext") String taskContext) {
		Schedule schedule = getRecvSchedule(scheduleName);
		schedule = getRecvScheduleManager().updateTask(schedule, oldName, task,
				taskContext);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 保存任务调度信息 (发送任务)
	 * 
	 * @param schedule
	 *            任务调度详细信息
	 * @param oldName
	 *            任务调度原名
	 */
	@WebMethod(exclude = true)
	public void updateSendSchedule(
			@WebParam(name = "scheduleName") Schedule schedule,
			@WebParam(name = "oldName") String oldName) {
		getSendScheduleManager().updateSchedule(schedule, oldName);
	}

	/**
	 * 保存任务 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param oldName
	 *            任务原名
	 * @param task
	 *            任务信息
	 * @param taskContext
	 *            任务内容
	 */
	public void updateSendTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "oldName") String oldName,
			@WebParam(name = "task") Task task,
			@WebParam(name = "taskContext") String taskContext) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().updateTask(schedule, oldName, task,
				taskContext);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 保存时间策略 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param oldName
	 *            时间策略原名
	 * @param timer
	 *            保存的时间策略
	 */
	public void updateTimer(@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "oldName") String oldName,
			@WebParam(name = "timer") Timer timer) {
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().updateTimer(schedule, oldName, timer);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 启动所有的Schedule (发送任务)<br/>
	 * 启动时调用
	 */
	@WebMethod(exclude = true)
	public void startAllSendSchedule() {
		List<Schedule> scheduleList = getSendScheduleManager().getList();
		for (Schedule schedule : scheduleList) {
			getSendScheduleManager().startSchedule(schedule.getName());
		}
	}

	/**
	 * 启动任务调度 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 */
	public String startSendSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		return getSendScheduleManager().startSchedule(scheduleName);
	}

	/**
	 * 启动手动执行的任务调度 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 */
	public void manualStartTask(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "condition") String condition,
			@WebParam(name = "orderby") String orderby) {
		if (StringUtility.isBlank(scheduleName)
				|| StringUtility.isBlank(taskName)) {
			throw new IntegratorException(
					"startTaskFailed : scheduleName or taskName can not be blank");
		}
		Schedule sch = getSendSchedule(scheduleName);
		Task task = getSendScheduleManager().getTask(sch, taskName);
		if (task == null) {
			throw new IntegratorException("can not find task '" + taskName
					+ "' in schedule '" + scheduleName + "'");
		}
		String timerName = null;
		List<Associate> joins = sch.getAssociates();
		if (joins != null && joins.size() > 0) {
			for (Associate a : joins) {
				if (a.getTaskName().equals(taskName)) {
					timerName = a.getTimerName();
					break;
				}
			}
		}
		if (StringUtility.isBlank(timerName)) {
			throw new IntegratorException("task '" + taskName
					+ "' can not manual start");
		} else {
			boolean hasStart = false;
			for (Timer timer : sch.getTimers()) {
				if (timer.getName().equals(timerName)) {
					Map<String, Object> headMessageParam = new HashMap<String, Object>();
//					headMessageParam.put(
//							IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE,
//							"command");
					if (!StringUtility.isBlank(condition)) {
						headMessageParam.put(
								IntegratorConstants.MESSAGE_HEADER_CONDITION,
								condition);
					}
					if (!StringUtility.isBlank(orderby)) {
						headMessageParam.put(
								IntegratorConstants.MESSAGE_HEADER_ORDER,
								orderby);
					}
					getSendScheduleManager().executeCommond(scheduleName,
								taskName, headMessageParam);
					hasStart = true;
				}
			}
			if (!hasStart) {
				throw new IntegratorException(
						"start task not success, please check the schedule file");
			}
		}
	}
/**
 * 启动对队列的监听
 * @param qcuName QCU名称
 * @param queueName 队列名称
 */
	public void startQueueListener(@WebParam(name = "qcuName") String qcuName,
			@WebParam(name = "queueName") String queueName) {
		Queue queue = new TongQueue(qcuName, queueName);
		QueueScannerUtils.startScan(queue);
	}
/**
 * 停止对队列的监听
 * @param qcuName QCU名称
 * @param queueName 队列名称
 */
	public void stopQueueListener(@WebParam(name = "qcuName") String qcuName,
			@WebParam(name = "queueName") String queueName) {
		Queue queue = new TongQueue(qcuName, queueName);
		QueueScannerUtils.stopScan(queue);
	}
/**
 * 启动对所有队列的监听
 */
	public void startAllQueueListener() {
		QueueScannerUtils.startScan();
	}
/**
 * 停止所有队列的监听
 */
	public void stopAllQueueListener() {
		QueueScannerUtils.stopScan();
	}

	/**
	 * 停止任务调度 (发送任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 */
	public void stopSendSchedule(
			@WebParam(name = "scheduleName") String scheduleName) {
		getSendScheduleManager().stopSchedule(scheduleName);
	}

	/**
	 * 获得发送任务的参数名和参数标题
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return 参数名和参数标题组成的字符串
	 */
	public String getSendTaskParameters(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Map<String, String> parameters = getSendScheduleManager()
				.getParameters(scheduleName, taskName);
		return TaskUtility.getInstance().formatParameter(parameters);
	}

	/**
	 * 获得接收任务的参数名和参数标题
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return 参数名和参数标题组成的字符串
	 */
	public String getRecvTaskParameters(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Map<String, String> parameters = getRecvScheduleManager()
				.getParameters(scheduleName, taskName);
		return TaskUtility.getInstance().formatParameter(parameters);
	}

	/**
	 * 获得发送任务的参数名和参数值
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return 参数名和参数值组成的字符串
	 */
	public String getSendTaskParameterValues(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Map<String, String> parameterValues = getSendScheduleManager()
				.getParameterValues(scheduleName, taskName);
		return TaskUtility.getInstance().formatParameter(parameterValues);
	}

	/**
	 *获得接收任务的参数名和参数值
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @return 参数名和参数值组成的字符串
	 */
	public String getRecvTaskParameterValues(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName) {
		Map<String, String> parameterValues = getRecvScheduleManager()
				.getParameterValues(scheduleName, taskName);
		return TaskUtility.getInstance().formatParameter(parameterValues);
	}

	/**
	 * 设置发送任务的参数值
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @param values
	 *            参数值
	 * @return true 设置成功
	 * @return false 该任务不存在参数，不能进行设置
	 */
	public void updateSendTaskParameterValues(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "values") String values) {
		getSendScheduleManager().stopTask(scheduleName, taskName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().setParameterValues(
				schedule, taskName, values);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
		getSendScheduleManager().startTask(scheduleName, taskName);
	}

	/**
	 * 设接收任务的参数值
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            人物名
	 * @param values
	 *            值
	 * @return true 设置成功
	 * @return false 该任务不存在参数，不能进行设置
	 */
	public void updateRecvTaskParameterValues(
			@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "values") String values) {
		Schedule schedule = getRecvSchedule(scheduleName);
		schedule = getRecvScheduleManager().setParameterValues(schedule,
				taskName, values);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}

	/**
	 * 根据关联关系获得发送任务调度文件下配置任务的参数（包括TaskParam）
	 * @param scheduleName 任务调度名
	 * @return 任务参数的Json字符串
	 */
	public String getAssociateWithSendTaskParam(
			@WebParam(name = "scheduleName") String scheduleName) {
		List<Map<String, String>> taskParam = new ArrayList<Map<String, String>>();
		Map<String, Task> taskMap = new HashMap<String, Task>();
		Schedule schedule = getSendSchedule(scheduleName);
		for (Task task : schedule.getTasks()) {
			taskMap.put(task.getName(), task);
		}
		/*Map<String, Timer> timerMap = new HashMap<String, Timer>();
		for (Timer timer : schedule.getTimers()) {
			timerMap.put(timer.getName(), timer);
		}*/
		for (Associate associate : schedule.getAssociates()) {
			Task task = taskMap.get(associate.getTaskName());
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, String> map = getSendScheduleManager()
					.getParameterValues(scheduleName, task.getName());
			if(map != null){
				Iterator<Entry<String, String>> entryIterator = map.entrySet().iterator();
				while (entryIterator.hasNext()) {
					Entry<String,String> entry = entryIterator.next();
					paramMap.put(entry.getKey(), entry.getValue());
				}
			}
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, task
					.getName());
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE, task
					.getTitle());
			String adapterType = task.getAdapterType();
			Connection connection = schedule.getConnection();
			if(connection != null){
				paramMap.put("url",connection.getUrl());
			}
			if(adapterType == null){
				paramMap.put("adapterType", schedule.getAdapterType());
			}else {
				paramMap.put("adapterType", adapterType);
			}
			paramMap.put("timerName", associate.getTimerName());
			taskParam.add(paramMap);
		}
		JSONSerializer json = new JSONSerializer();
		return json.deepSerialize(taskParam);
	}
	/**
	 * 根据关联关系获得接收任务调度文件下配置任务的参数（包括TaskParam）
	 * @param scheduleName 任务调度名
	 * @return 任务参数的Json字符串
	 */
	public String getAssociateWithRecvTaskParam(
			@WebParam(name = "scheduleName") String scheduleName) {
		List<Map<String, String>> taskParam = new ArrayList<Map<String, String>>();
		Schedule schedule = getRecvSchedule(scheduleName);
		for (Task task: schedule.getTasks()) {
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, String> map = getRecvScheduleManager()
					.getParameterValues(scheduleName, task.getName());
			if(map != null){
				Iterator<Entry<String, String>> entryIterator = map.entrySet().iterator();
				while (entryIterator.hasNext()) {
					Entry<String,String> entry = entryIterator.next();
					paramMap.put(entry.getKey(), entry.getValue());
				}
			}
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, task
					.getName());
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE, task
					.getTitle());
			String adapterType = task.getAdapterType();
			if(adapterType == null){
				paramMap.put("adapterType", schedule.getAdapterType());
			}else {
				paramMap.put("adapterType", adapterType);
			}
			Connection connection = schedule.getConnection();
			if(connection != null){
				paramMap.put("url",connection.getUrl());
			}
			taskParam.add(paramMap);
		}
		JSONSerializer json = new JSONSerializer();
		return json.deepSerialize(taskParam);
	}
	/**
	 * 根据关联关系获得发送任务调度文件下配置任务的参数（不包括TaskParam）
	 * @param scheduleName 任务调度名
	 * @return 任务参数的Json字符串
	 */
	public String getAssociateWithOutSendTaskParam(
			@WebParam(name = "scheduleName") String scheduleName) {
		List<Map<String, Object>> taskParam = new ArrayList<Map<String, Object>>();
		Map<String, Task> taskMap = new HashMap<String, Task>();
		Schedule schedule = getSendSchedule(scheduleName);
		for (Task task : schedule.getTasks()) {
			taskMap.put(task.getName(), task);
		}
		/*Map<String, Timer> timerMap = new HashMap<String, Timer>();
		for (Timer timer : schedule.getTimers()) {
			timerMap.put(timer.getName(), timer);
		}*/
		for (Associate associate : schedule.getAssociates()) {
			Task task = taskMap.get(associate.getTaskName());
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, task.getName());
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE, task.getTitle());
			paramMap.put("timerName", associate.getTimerName());
			String adapterType = task.getAdapterType();
			if(adapterType == null){
				paramMap.put("adapterType", schedule.getAdapterType());
			}else {
				paramMap.put("adapterType", adapterType);
			}
			taskParam.add(paramMap);
		}
		JSONSerializer json = new JSONSerializer();
		return json.deepSerialize(taskParam);
	}
	/**
	 * 根据关联关系获得接收任务调度文件下配置任务的参数（不包括TaskParam）
	 * @param scheduleName 任务调度名
	 * @return 任务参数的Json字符串
	 */
	public String getAssociateWithOutRecvTaskParam(
			@WebParam(name = "scheduleName") String scheduleName) {
		List<Map<String, Object>> taskParam = new ArrayList<Map<String, Object>>();
		Map<String, Task> taskMap = new HashMap<String, Task>();
		Schedule schedule = getRecvSchedule(scheduleName);
		for (Task task : schedule.getTasks()) {
			taskMap.put(task.getName(), task);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, task.getName());
			paramMap.put(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE, task.getTitle());
			String adapterType = task.getAdapterType();
			if(adapterType == null){
				paramMap.put("adapterType", schedule.getAdapterType());
			}else {
				paramMap.put("adapterType", adapterType);
			}
			taskParam.add(paramMap);
		}
		JSONSerializer json = new JSONSerializer();
		return json.deepSerialize(taskParam);
	}
	/**
	 * 更改任务的时间策略
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 * @param oldTimerName 原来的时间策略名
	 * @param timer 时间策略
	 */
	public void updateTimerOnTask(@WebParam(name = "scheduleName") String scheduleName,
			@WebParam(name = "taskName") String taskName,
			@WebParam(name = "oldTimerName") String oldTimerName,
			@WebParam(name = "timer") Timer timer){
		Schedule schedule = getSendSchedule(scheduleName);
		Task task = getSendScheduleManager().getTask(schedule, taskName);
		if(task == null) {
			throw new IntegratorException("任务调度" + scheduleName + "下不存在名为" + taskName + "的任务");
		}
		schedule = getSendScheduleManager().updateTimerOnTask(schedule, timer, task,oldTimerName);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
		
	}
	
	/**
	 * 获得连接列表
	 * @return
	 */
	public List<String> getConnectionTypeStringList (){
		List<ConnectionType> list = getConnectionManager().getConnectionTypeList();
		List<String> connectionTypeList = new ArrayList<String>();
		for(ConnectionType connType : list){
			connectionTypeList.add(connType.getName());
		}
		return connectionTypeList;
	}
	
	/**
	 * 获得连接列表
	 * @return
	 */
	public List<ConnectionType> getConnectionTypeList (){
		List<ConnectionType> list = getConnectionManager().getConnectionTypeList();
		return list;
	}
	
	/**
	 * 根据数据库类型，获得相应的ConnectionType
	 * @param type
	 * @return
	 */
	public ConnectionType getConnectionType(
			@WebParam(name = "type") String type){
		List<ConnectionType> list = getConnectionManager().getConnectionTypeList();
		for(ConnectionType connType : list){
			if(StringUtility.equalsIgnoreCase(connType.getName(),type)){
				return connType;
			}
		}
		return null;
	}
	
	/**
	 * 根据scheduleName 获得Schedule的连接
	 * @param scheduleName
	 * @return
	 */
	public Connection getSendScheduleConnection(
			@WebParam(name = "scheduleName")String scheduleName){
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		return schedule.getConnection();
	}
	
	/**
	 * 根据scheduleName 获得Schedule的连接
	 * @param scheduleName
	 * @return
	 */
	public Connection getRecvScheduleConnection(
			@WebParam(name = "scheduleName")String scheduleName){
		Schedule schedule = getRecvScheduleManager().getSchedule(scheduleName);
		return schedule.getConnection();
	}
	
	/**
	 * 更新Connection
	 * @param scheduleName
	 * @param connection
	 */
	public void updateSendScheduleConnection(
			@WebParam(name = "scheduleName")String scheduleName, 
			@WebParam(name = "connection")Connection connection){
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		schedule.setConnection(connection);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}
	
	/**
	 * 更新Connection
	 * @param scheduleName
	 * @param connection
	 */
	public void updateRecvScheduleConnection(
			@WebParam(name = "scheduleName")String scheduleName, 
			@WebParam(name = "connection")Connection connection){
		Schedule schedule = getRecvScheduleManager().getSchedule(scheduleName);
		schedule.setConnection(connection);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}
	
	/**
	 * 获得发送Schedule中的公有参数列表
	 * @param scheduleName
	 * @return
	 */
	public String getSendScheduleParameters(
			@WebParam(name = "scheduleName")String scheduleName){
		Map<String,String> parameters = getSendScheduleManager().getPublicParameters(scheduleName);
		return TaskUtility.getInstance().formatParameter(parameters);
	}
	
	/**
	 * 获得发送Schedule中的公有参数键值对
	 * @param scheduleName
	 * @return
	 */
	public String getSendScheduleParameterValues(
			@WebParam(name = "scheduleName")String scheduleName){
		Map<String,String> parameterValues = getSendScheduleManager().getPublicParameterValues(scheduleName);
		return TaskUtility.getInstance().formatParameter(parameterValues);
	}
	/**
	 *  返回某个业务，在给定的时间内交换的文件的数量
	 * @param scheduleName 业务的名字 例如HT
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getFileExchangeNumByScheduleName(
			@WebParam(name = "scheduleName")String scheduleName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		
		return getFileExchangeNumByExchageTaskName("^" +scheduleName + ".*$",recvNodeName, startTime, endTime);
	}
	/**
	 * 返回某个交换任务，在给定的时间内交换的数量
	 * @param exchangeTaskName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getFileExchangeNumByExchageTaskName(
			@WebParam(name = "exchangeTaskName")String exchangeTaskName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		List<MessageLog> resultList = getMessageLogList(exchangeTaskName, recvNodeName, startTime, endTime, TaskType.recv,true);
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		int size = resultList.size();
		for (int i = 0; i < size; i++) {
			Map<String,Object> messageHeaders = resultList.get(i).getMessageHeaders();
			String sendNodeName = CommonHelper.Null2String(messageHeaders.
					get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
			int fileCount = 1;
			if(resultMap.containsKey(sendNodeName)){
				fileCount += resultMap.get(sendNodeName);
			}
			resultMap.put(sendNodeName, fileCount);
		}
		return  new JSONSerializer().deepSerialize(resultMap);
	}
	/**
	 * 返回某个交换任务在某一段时间内交换数据量
	 * @param exchangeTaskName 交换任务名 例如:EPM.PS_BASE_INFO
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String getMessageLogByTaskName(
			@WebParam(name = "exchangeTaskName")String exchangeTaskName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		List<MessageLog> logList = getMessageLogList(exchangeTaskName,recvNodeName,startTime,endTime,TaskType.recv,false);
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		int size = logList.size();
		for (int i = 0; i < size; i++) {
			Map<String,Object> messageHeaders = logList.get(i).getMessageHeaders();
			String sendNodeName = CommonHelper.Null2String(messageHeaders.
					get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
			int messageSize = Integer.parseInt(messageHeaders.get(
					IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE).toString());
			if(countMap.containsKey(sendNodeName)){
				messageSize += countMap.get(sendNodeName);
			}
			countMap.put(sendNodeName, messageSize);
		}
		return new JSONSerializer().deepSerialize(countMap);
	}
	private List<MessageLog> getMessageLogList(String exchangeTaskName,String recvNodeName,
			String startTime, String endTime, TaskType type,boolean isComplete) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, type.toString());
		if(isComplete){
			map.put(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE, 100);
		}
		String startKey = "";
		String endKey = "";
		if(TaskType.recv.equals(type)){
			startKey = IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE + " >=";
			endKey = IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE + " <=";
		}else{
			startKey = IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE + " >=";
			endKey = IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE + " <=";
		}
		map.put(startKey, startTime);
		map.put(endKey, endTime);
		if(StringUtility.isNotBlank(recvNodeName)){
			map.put(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
		}
		List<MessageLog> logList = getMessageLogManager().getList(exchangeTaskName,map);
		return logList;
	}

	public Long getSendMessageNum(
			@WebParam(name = "exchangeTaskName")String exchangeTaskName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		List<MessageLog> logList = getMessageLogList(exchangeTaskName,recvNodeName,startTime,endTime,TaskType.send,false);
		int size = logList.size();
		Long count = 0l;
		for (int i = 0; i < size; i++) {
			String messageSize = logList.get(i).getMessageHeaders().get(
					IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE).toString();
			count += Long.parseLong(messageSize);
		}
		return count;
	}
	
	public Long getSendFileNumByExchangeTaskName(
			@WebParam(name = "scheduleName")String exchangeTaskName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		List<MessageLog> logList = getMessageLogList(exchangeTaskName,recvNodeName,startTime,endTime,TaskType.send,true);
		return (long)logList.size();
	}
	public Long getSendFileNumByScheduleName(
			@WebParam(name = "scheduleName")String scheduleName,
			@WebParam(name = "recvNodeName")String recvNodeName,
			@WebParam(name = "startTime")String startTime,
			@WebParam(name = "endTime")String endTime){
		return getSendFileNumByExchangeTaskName("^" +scheduleName + ".*$",recvNodeName, startTime, endTime);
	}
	
	/**
	 * 更新发送业务公有参数键值对。
	 * @param scheduleName
	 * @param values
	 */
	public void updateSendScheduleParameterValues(
			@WebParam(name = "scheduleName")String scheduleName, 
			@WebParam(name = "values")String values){
		getSendScheduleManager().stopSchedule(scheduleName);
		Schedule schedule = getSendSchedule(scheduleName);
		schedule = getSendScheduleManager().setPublicParameterValues(
				schedule, values);
		getSendScheduleManager().updateSchedule(schedule, scheduleName);
	}
	
	/**
	 * 获取接收业务参数列表
	 * @param scheduleName
	 * @return
	 */
	public String getRecvScheduleParameters(
			@WebParam(name = "scheduleName")String scheduleName){
		Map<String,String> parameters = getRecvScheduleManager().getPublicParameters(scheduleName);
		return TaskUtility.getInstance().formatParameter(parameters);
	}
	
	/**
	 * 获取接收业务参数键值对
	 * @param scheduleName
	 * @return
	 */
	public String getRecvScheduleParameterValues(
			@WebParam(name = "scheduleName")String scheduleName){
		Map<String,String> parameterValues = getRecvScheduleManager().getPublicParameterValues(scheduleName);
		return TaskUtility.getInstance().formatParameter(parameterValues);
	}
	
	/**
	 * 更新接收业务参数键值对。
	 * @param scheduleName
	 * @param values
	 */
	public void updateRecvScheduleParameterValues(
			@WebParam(name = "scheduleName")String scheduleName, 
			@WebParam(name = "values")String values){
		Schedule schedule = getRecvSchedule(scheduleName);
		schedule = getRecvScheduleManager().setPublicParameterValues(
				schedule, values);
		getRecvScheduleManager().updateSchedule(schedule, scheduleName);
	}
	
	/**
	 * 测试发送数据源连接
	 * @param scheduleName
	 * @return
	 */
	public boolean testSendScheduleConnect(
			@WebParam(name = "scheduleName")String scheduleName){
		Schedule schedule = getSendScheduleManager().getSchedule(scheduleName);
		return getConnectionManager().testConnect(schedule.getConnection());
	}
	
	/**
	 * 测试接收连接
	 * @param scheduleName
	 * @return
	 */
	public boolean testRecvScheduleConnect(
			@WebParam(name = "scheduleName")String scheduleName){
		Schedule schedule = getRecvScheduleManager().getSchedule(scheduleName);
		return getConnectionManager().testConnect(schedule.getConnection());
	}
	
	/**
	 * 测试数据库连接
	 * @param scheduleName
	 * @return
	 */
	public boolean testConnection(
			@WebParam(name = "connection")Connection connection){
		return getConnectionManager().testConnect(connection);
	}
}
