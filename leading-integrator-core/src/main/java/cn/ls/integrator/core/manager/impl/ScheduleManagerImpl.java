package cn.ls.integrator.core.manager.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.ScheduleManager;
import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.model.Timer;
import cn.ls.integrator.core.task.TaskJob;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.TaskParameterUtility;
import cn.ls.integrator.core.utils.TriggerNameUtility;

public abstract class ScheduleManagerImpl implements ScheduleManager {

	private Logger logger = Logger.getLogger(ScheduleManager.class);

	protected Map<String, Schedule> schedulesMap;

	protected List<Schedule> schedulesList;

	protected TaskType type;
	
	protected static Scheduler scheduler;
	public ScheduleManagerImpl(TaskType type) {
		this.type = type;
	}

	protected void startJob(String scheduleName, String timerName,
			String taskName, String expression) throws SchedulerException,
			ParseException {
		Scheduler sched = getScheduler();
		String triggerName = TriggerNameUtility.getTriggerName(taskName,
				timerName);
		JobKey jobKey = new JobKey(triggerName, scheduleName);
		if (!sched.checkExists(jobKey)) {
			JobDetail jobDetail = JobBuilder.newJob(TaskJob.class)
					.withIdentity(jobKey).build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(
					triggerName, scheduleName).withSchedule(
					CronScheduleBuilder.cronSchedule(expression)).build();
			sched.scheduleJob(jobDetail, trigger);
//			System.out.println("StartTrigger:----------------------------------" + triggerName);
		}else {
			logger.warn("重复启动：" + triggerName);
		}
		if (!sched.isStarted()) {
			sched.start();
		}
	}

	protected void stopJob(String scheduleName, String taskName,
			String timerName) throws SchedulerException {
		Scheduler sched = getScheduler();
		String triggerName = TriggerNameUtility.getTriggerName(taskName,
				timerName);
		// Trigger existTrigger = sched.getTrigger(new TriggerKey(triggerName,
		// scheduleName));
//		System.out.println("StopTrigger:----------------------------------" + triggerName);
		JobKey jobKey = new JobKey(triggerName, scheduleName);
		if (sched.checkExists(jobKey)) {
			sched.deleteJob(new JobKey(triggerName, scheduleName));
		} else {
			logger.warn("停止失败，任务没有启动：" + triggerName);
		}
	}

	protected final void refreshScheduleMap() {
		schedulesList = FileUtility.getInstance().getSchedulesFromFiles(type);
		schedulesMap = new HashMap<String, Schedule>();
		for (Schedule schedule : schedulesList) {
			String scheduleName = schedule.getName();
			schedulesMap.put(scheduleName, schedule);
		}
	}

	private synchronized Map<String, Schedule> getScheduleMap() {
		if (schedulesMap == null) {
			refreshScheduleMap();
		}
		return schedulesMap;
	}

	public void assertScheduleIsNotNull(Schedule schedule, String scheduleName) {
		if (schedule == null) {
			throw new IntegratorException("schedule with name " + scheduleName
					+ " is null");
		}
	}

	protected void assertTaskIsNotNull(Task task) {
		if (task == null) {
			throw new IntegratorException("任务不能为空");
		}
	}

	public Map<String, Task> getTaskMap(List<Task> taskList) {
		Map<String, Task> taskMap = new HashMap<String, Task>();
		if (taskList == null) {
			return taskMap;
		}
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			String taskName = task.getName();
			if (StringUtility.isBlank(taskName)) {
				throw new IntegratorException("任务调度中第" + (i + 1)
						+ "个task的name为空");
			} else {
				assertTaskAllowed(task);
				taskMap.put(taskName, task);
			}
		}
		return taskMap;
	}

	public Map<String, Timer> getTimerMap(List<Timer> timerList) {
		Map<String, Timer> timerMap = new HashMap<String, Timer>();
		if (timerMap == null) {
			return timerMap;
		}
		for (int i = 0; i < timerList.size(); i++) {
			Timer timer = timerList.get(i);
			String timerName = timer.getName();
			if (StringUtility.isBlank(timerName)) {
				throw new IntegratorException("任务调度中第" + (i + 1)
						+ "个timer的name为空");
			} else {
				assertTimerAllowed(timer);
				timerMap.put(timerName, timer);
			}
		}
		return timerMap;
	}

	protected List<Associate> filterAssociatesByTaskName(
			List<Associate> associates, String taskName) {
		if (associates == null || taskName == null) {
			return null;
		}
		List<Associate> resultList = new ArrayList<Associate>();
		for (Associate associate : associates) {
			if (StringUtility.equals(associate.getTaskName(), taskName)) {
				resultList.add(associate);
			}
		}
		return resultList;
	}

	protected List<Associate> filterAssociatesByTimerName(
			List<Associate> associates, String timerName) {
		if (associates == null || timerName == null) {
			return null;
		}
		List<Associate> resultList = new ArrayList<Associate>();
		for (Associate associate : associates) {
			if (StringUtility.equals(associate.getTimerName(), timerName)) {
				resultList.add(associate);
			}
		}
		return resultList;
	}

	protected List<Task> filterTaskByTaskName(List<Task> tasks, String taskName) {
		if (tasks == null || taskName == null) {
			return null;
		}
		List<Task> resultList = new ArrayList<Task>();
		for (Task task : tasks) {
			if (StringUtility.equals(task.getName(), taskName)) {
				resultList.add(task);
			}
		}
		return resultList;
	}

	protected void assertTaskAllowed(Task task) {
		if (task == null) {
			throw new IntegratorException("task 不能为空");
		}
		// TODO 验证task 参数的合法性
	}

	protected void assertTimerAllowed(Timer timer) {
		if (timer == null) {
			throw new IntegratorException("timer 不能为空");
		}
		// TODO 验证timer类型、参数的合法性
	}

	public void assertScheduleAllowed(Schedule schedule) {
		assertScheduleIsNotNull(schedule, null);
		String scheduleName = schedule.getName();
		if (StringUtility.isBlank(scheduleName)) {
			throw new IntegratorException("任务调度名不能为空");
		}
		List<Associate> associates = schedule.getAssociates();
		if (associates != null && associates.size() != 0) {
			Map<String, Task> taskMap = getTaskMap(schedule.getTasks());
			Map<String, Timer> timerMap = getTimerMap(schedule.getTimers());
			for (int i = 0; i < associates.size(); i++) {
				Associate associate = associates.get(i);
				String taskName = associate.getTaskName();
				if (StringUtility.isBlank(taskName)) {
					throw new IntegratorException("任务调度" + scheduleName + "中第"
							+ (i + 1) + "个associate没有定义taskName");
				}
				Task task = taskMap.get(taskName);
				if (task == null) {
					throw new IntegratorException("任务调度" + scheduleName + "中第"
							+ (i + 1) + "个associate对应的task不存在");
				}

				String timerName = associate.getTimerName();
				if (StringUtility.isBlank(timerName)) {
					throw new IntegratorException("任务调度" + scheduleName + "中第"
							+ (i + 1) + "个associate没有定义timerName");
				}
				Timer timer = timerMap.get(timerName);
				if (timer == null) {
					throw new IntegratorException("任务调度" + scheduleName + "中第"
							+ (i + 1) + "个associate对应的timer不存在");
				}
			}
		}
	}

	@Override
	public void addSchedule(Schedule schedule) {
		assertScheduleAllowed(schedule);
		Schedule sc = getSchedule(schedule.getName());
		if (sc != null) {
			throw new IntegratorException("已存在该任务调度名:" + schedule.getName());
		}

		try {
			FileUtility fileUtility = FileUtility.getInstance();
			fileUtility.saveScheduleFile(schedule, type);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void clearTaskTemp() {
		schedulesMap = null;
		schedulesList = null;
		TaskParameterUtility.getInstance().clearParametersMap();
	}

	@Override
	public Schedule addSchedule(String name, String title, String description) {
		if (StringUtility.isBlank(name)) {
			throw new IntegratorException("调度名不能为空");
		}
		Schedule sc = getSchedule(name);
		if (sc != null) {
			throw new IntegratorException("已存在该任务调度名:" + name);
		}
		Schedule schedule = new Schedule();
		schedule.setName(name);
		schedule.setTitle(title);
		schedule.setDescription(description);
		addSchedule(schedule);
		clearTaskTemp();
		return schedule;
	}

	@Override
	public void deleteSchedule(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		List<Task> tasks = schedule.getTasks();
		FileUtility fileUtility = FileUtility.getInstance();
		if (tasks == null) {
			return;
		}
		for (Task task : tasks) {
			if (StringUtility.isBlank(task.getName())) {
				continue;
			}
			fileUtility.deleteTaskFile(scheduleName, task.getName(), type);
		}
		fileUtility.deleteScheduleFile(scheduleName, type);
		clearTaskTemp();
	}

	@Override
	public Schedule deleteTask(Schedule schedule, String taskName) {
		if (getTask(schedule, taskName) == null) {
			throw new IntegratorException("任务调度" + schedule.getName()
					+ "下不存在名为" + taskName + "的任务");
		}

		// delete join
		List<Associate> existJoins = schedule.getAssociates();
		if (existJoins != null && existJoins.size() > 0) {
			List<Associate> associates = filterAssociatesByTaskName(existJoins,
					taskName);
			for (Associate join : associates) {
				existJoins.remove(join);
			}
			schedule.setAssociates(existJoins);
		}

		// delete task
		List<Task> existTasks = schedule.getTasks();
		if (existTasks != null) {
			Task task = getTask(schedule, taskName);
			existTasks.remove(task);
		}

		schedule.setTasks(existTasks);
		// delete file
		FileUtility fileUtility = FileUtility.getInstance();
		fileUtility.deleteTaskFile(schedule.getName(), taskName, type);
		return schedule;
	}

	@Override
	public void deployTask(String scheduleName, Task task, String taskContext) {
		if (taskContext != null) {
			FileUtility fileUtility = FileUtility.getInstance();
			fileUtility.saveTaskFile(task, scheduleName, taskContext, type);
		}
		clearTaskTemp();
	}

	@Override
	public List<Schedule> getList() {
		if (schedulesList == null) {
			refreshScheduleMap();
		}
		return schedulesList;
	}

	@Override
	public Schedule getSchedule(String scheduleName) {
		if (StringUtility.isBlank(scheduleName)) {
			throw new IntegratorException("任务调度名不可为空");
		}
		Schedule schedule = getScheduleMap().get(scheduleName);
		return schedule;
	}

	@Override
	public Task getTask(Schedule schedule, String taskName) {
		List<Task> tasks = schedule.getTasks();
		if (tasks != null) {
			List<Task> taskList = filterTaskByTaskName(tasks, taskName);
			if (taskList.size() == 0) {
				return null;
			} else if (taskList.size() == 1) {
				return taskList.get(0);
			} else {
				logger.error(schedule.getName() + "中出现多个相同名字的任务：" + taskName);
				return taskList.get(0);
			}
		} else {
			return null;
		}
	}

	@Override
	public String getTaskContext(String scheduleName, String taskName) {
		String taskContext = null;
		FileUtility fileUtility = FileUtility.getInstance();
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		taskContext = fileUtility.getTaskFilePath(scheduleName, taskName, type);
		return taskContext;
	}

	@Override
	public List<Task> getTaskList(String scheduleName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		return schedule.getTasks();
	}

	@Override
	public void updateSchedule(Schedule schedule, String oldName) {
		FileUtility fileUtility = FileUtility.getInstance();
		fileUtility.saveScheduleFile(schedule, type);
		List<Schedule> schedules = new ArrayList<Schedule>();
		if (getList().size() > 0) {
			for (Schedule exist : schedulesList) {
				if (exist.getName().equals(oldName)) {
					schedules.add(schedule);
				} else {
					schedules.add(exist);
				}
			}
		} else {
			schedules.add(schedule);
		}
		clearTaskTemp();
	}

	@Override
	public Schedule updateTask(Schedule schedule, String oldName, Task newTask,
			String taskContext) {
		assertTaskAllowed(newTask);
		// tasks
		List<Task> tasks = schedule.getTasks();
		if (tasks != null) {
			Task task = getTask(schedule, oldName);
			tasks.remove(task);
		} else {
			tasks = new ArrayList<Task>();
		}
		tasks.add(newTask);
		schedule.setTasks(tasks);

		// join
		List<Associate> existJoins = schedule.getAssociates();
		if (existJoins != null) {
			List<Associate> associates = filterAssociatesByTaskName(existJoins,
					oldName);
			for (Associate associate : associates) {
				existJoins.remove(associate);
				existJoins.add(new Associate(newTask.getName(), associate
						.getTimerName()));
			}
		}
		String scheduleName = schedule.getName();
		// addSchedule(schedule);
		FileUtility fileUtility = FileUtility.getInstance();
		fileUtility.saveScheduleFile(schedule, type);
		if (!oldName.equals(newTask.getName())) {
			try {
				fileUtility.deleteTaskFile(scheduleName, oldName, type);
			} catch (Exception e) {
				logger.error("删除任务文件时发生异常。scheduleName:" + scheduleName
						+ ", taskName:" + oldName + ", type:" + type, e);
			}
		}
		if (StringUtility.isBlank(taskContext)) {
			throw new IntegratorException("文件内容不可为空");
		}
		fileUtility.saveTaskFile(newTask, scheduleName, taskContext, type);
		clearTaskTemp();
		return schedule;
	}

	@Override
	public Schedule updateTask(Schedule schedule, String oldName, Task newTask) {
		// tasks
		List<Task> tasks = schedule.getTasks();
		if (tasks != null) {
			Task task = getTask(schedule, oldName);
			tasks.remove(task);
		} else {
			tasks = new ArrayList<Task>();
		}
		tasks.add(newTask);
		schedule.setTasks(tasks);

		// join
		List<Associate> existJoins = schedule.getAssociates();
		if (existJoins != null) {
			List<Associate> associates = filterAssociatesByTaskName(existJoins,
					oldName);
			for (Associate associate : associates) {
				existJoins.remove(associate);
				existJoins.add(new Associate(newTask.getName(), associate
						.getTimerName()));
			}
		}

		clearTaskTemp();
		return schedule;
	}

	public Schedule addTimerToSchedule(Schedule schedule, Timer newTimer) {
		List<Timer> timers = schedule.getTimers();
		if (timers == null) {
			timers = new ArrayList<Timer>();
		}
		boolean flag = true;
		for (Timer timer : timers) {
			if (StringUtility.equals(timer.getName(), newTimer.getName())) {
				flag = false;
			}
		}
		if (flag) {
			timers.add(newTimer);
		}
		schedule.setTimers(timers);
		return schedule;
	}

	@Override
	public Map<String, String> getParameters(String scheduleName,
			String taskName) {
		Map<String, String> parameters = null;
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		Task task = getTask(schedule, taskName);
		parameters = TaskParameterUtility.getInstance().getParameters(scheduleName, task, type);
		return parameters;
	}

	@Override
	public Map<String, String> getParameterValues(String scheduleName,
			String taskName) {
		return TaskParameterUtility.getInstance().getParameterValues(
				scheduleName, taskName, type);
	}

	public Schedule setParameterValues(Schedule schedule, String taskName,
			String values) {
		Map<String, String> parameters = getParameters(schedule.getName(),
				taskName);
		if (parameters == null || parameters.size() == 0) {
			return schedule;
		}
		Task task = getTask(schedule, taskName);
		//检查参数的有效性
		TaskParameterUtility.getInstance().assertParameters(schedule.getName(), task, this.type);
		//去掉和公共参数重复的部分
		values = TaskParameterUtility.getInstance()
				.removeSameParam(schedule.getParameters(), values);
		if (StringUtility.isNotBlank(values)) {
			if (task == null) {
				throw new IntegratorException("任务" + taskName + "不存在");
			}
			task.setParameters(values);
			updateTask(schedule, taskName, task);
			clearTaskTemp();
			return schedule;
		} else {
			throw new IntegratorException("参数值不应为空");
		}
	}
	
	public  Map<String, String> getPublicParameters(String scheduleName){
		Map<String, String> parameters = null;
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		parameters = TaskParameterUtility.getInstance().getPublicParameters(scheduleName, type);
		return parameters;
	}
	
	public Map<String, String> getPublicParameterValues(String scheduleName) {
		return TaskParameterUtility.getInstance().getPublicParametersValues(
				scheduleName, type);
	}

	public Schedule setPublicParameterValues(Schedule schedule, String values) {
		Map<String, String> parameters = getPublicParameters(schedule.getName());
		if (parameters == null || parameters.size() == 0) {
			return schedule;
		}
		if (StringUtility.isNotBlank(values)) {
			schedule.setParameters(values);
			return schedule;
		} else {
			throw new IntegratorException("参数值不应为空");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ls.integrator.core.manager.impl.ScheduleManager#stopTask(java.lang
	 * .String, java.lang.String)
	 */
	public void stopTask(String scheduleName, String taskName) {
		Schedule schedule = getSchedule(scheduleName);
		assertScheduleIsNotNull(schedule, scheduleName);
		List<Associate> associates = schedule.getAssociates();
		if (associates == null || associates.size() == 0) {
			return;
		}
		for (Associate associate : associates) {
			if (associate.getTaskName().equals(taskName)) {
				try {
					stopJob(scheduleName, taskName, associate.getTimerName());
				} catch (SchedulerException e) {
					logger.error(e.getMessage(), e);
					continue;
				}
			}
		}
	}
	
	@Override
	public Connection getDefaultConnection(Schedule schedule) {
		return schedule.getConnection();
	}
	public synchronized Scheduler getScheduler() throws SchedulerException{
		if(scheduler == null){
			scheduler = new StdSchedulerFactory().getScheduler();
		}
		return scheduler;
	}

	public static synchronized ScheduleManager getInstance(TaskType taskType) {
		ScheduleManagerImpl instance = null;
		if(TaskType.send.equals(taskType)){
			instance = new SendScheduleManagerImpl();
		}else{
			instance = new RecvScheduleManagerImpl();
		}
		return instance;
	}
}
