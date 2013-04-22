package cn.ls.integrator.core.task;

import java.util.Map;

public class TaskExecuterThread extends Thread{
	
	String scheduleName;
	String taskName;
	Map<String, Object> headMessageParam;
	
	public TaskExecuterThread(String scheduleName, String taskName,
			Map<String, Object> headMessageParam) {
		super();
		this.scheduleName = scheduleName;
		this.taskName = taskName;
		this.headMessageParam = headMessageParam;
	}
	
	@Override
	public void run() {
		SendExecuter.execute(scheduleName, taskName, headMessageParam);
	}
}
