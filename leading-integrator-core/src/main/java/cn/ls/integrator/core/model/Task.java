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
 * 交换任务类
 * 
 * @author zhoumb 2011-4-28
 */
@XStreamAlias("task")
public class Task implements Serializable{

	private static final long serialVersionUID = 8818532273955117434L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 标题
	 */
	private String description;
	/**
	 * 适配器类型
	 */
	
	private String adapterType;
	
	/**
	 * 模版参数值，其格式为“"name1":"value1","name2":"value2",...”
	 */
	private String parameters;
	
	public String getAdapterType() {
		return adapterType;
	}

	public void setAdapterType(String adapterType) {
		this.adapterType = adapterType;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
