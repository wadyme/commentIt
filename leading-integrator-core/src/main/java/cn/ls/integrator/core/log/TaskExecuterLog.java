package cn.ls.integrator.core.log;

import org.apache.log4j.Logger;

public class TaskExecuterLog {

	private static Logger logger = Logger.getLogger(TaskExecuterLog.class);
	
	public static void logTaskStart(String scheduleName, String taskName){
		logger.info("[START] " + scheduleName + "." + taskName);
	}
	
	public static void logTaskEnd(String scheduleName, String taskName){
		logger.info("[END]   " + scheduleName + "." + taskName);
	}
}
