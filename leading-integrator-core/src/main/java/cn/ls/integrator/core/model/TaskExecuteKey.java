package cn.ls.integrator.core.model;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 缓存任务Map的Key 类
 * 
 * @author zhoumb 2011-6-2
 */
public class TaskExecuteKey {

	private String scheduleName;

	private String taskName;

	private int messageHashCode;

	public TaskExecuteKey(String scheduleName, String taskName, int messageHashCode) {
		this.scheduleName = scheduleName;
		this.taskName = taskName;
		this.messageHashCode = messageHashCode;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getMessageHashCode() {
		return messageHashCode;
	}

	public void setMessageHashCode(int messageHashCode) {
		this.messageHashCode = messageHashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj.getClass() != getClass()){
			return false;
		}
		TaskExecuteKey keyObj = (TaskExecuteKey) obj;
		if(keyObj.getScheduleName().equals(this.getScheduleName())
				&& keyObj.getTaskName().equals(this.getTaskName())
				&& keyObj.getMessageHashCode() == this.getMessageHashCode()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
}
