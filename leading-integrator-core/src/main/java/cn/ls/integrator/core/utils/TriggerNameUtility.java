package cn.ls.integrator.core.utils;

public class TriggerNameUtility {

	private static final String SEPARATOR = "$$";
	
	public static String getTriggerName (String taskName, String timerName){
		if (StringUtility.isBlank(timerName) || StringUtility.isBlank(taskName)) {
			throw new RuntimeException("taskName or timerName is blank");
		} else {
			return taskName + SEPARATOR + timerName;
		}
	}
	
	public static String getTaskName (String triggerName){
		if(!StringUtility.isBlank(triggerName)){
			return triggerName.split("\\$\\$")[0];
		}
		return null;
	}
	
	public static String getTimerName (String triggerName){
		if(!StringUtility.isBlank(triggerName)){
			return triggerName.split("\\$\\$")[1];
		}
		return null;
	}
}
