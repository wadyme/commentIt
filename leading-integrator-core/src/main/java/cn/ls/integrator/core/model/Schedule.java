/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 任务调度
 * 
 * @author zhoumb 2011-5-5
 */
@XStreamAlias("schedule")
public class Schedule implements Serializable {

	private static final long serialVersionUID = 319315606906379447L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 数据库连接
	 */
	private Connection connection;
	
	private String parameters;
	
	private String adapterType;
	
	/**
	 * 任务列表
	 */
	private List<Task> tasks;

	/**
	 * 时间策略列表
	 */
	private List<Timer> timers;

	/**
	 * 关联关系列表
	 */
	private List<Associate> associates;

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setTimers(List<Timer> timers) {
		this.timers = timers;
	}

	public void setAssociates(List<Associate> associates) {
		this.associates = associates;
	}

	public List<Associate> getAssociates() {
		return associates;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public List<Timer> getTimers() {
		return timers;
	}

	public String getTitle() {
		return title;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getAdapterType() {
		return adapterType;
	}

	public void setAdapterType(String adapterType) {
		this.adapterType = adapterType;
	}

}
