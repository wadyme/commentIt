/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 任务和时间策略关联
 * 
 * @author zhoumb 2011-5-19
 */
@XStreamAlias("associate")
public class Associate implements Serializable {

	private static final long serialVersionUID = 5810845943191230967L;

	/**
	 * 任务名称
	 */
	@XStreamAlias("task-name")
	private String taskName;

	/**
	 * 时间策略名称
	 */
	@XStreamAlias("timer-name")
	private String timerName;

	public Associate() {

	}

	public Associate(String taskName, String timerName) {
		this.taskName = taskName;
		this.timerName = timerName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTimerName(String timerName) {
		this.timerName = timerName;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTimerName() {
		return timerName;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
