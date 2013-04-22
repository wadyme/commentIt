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
 * 时间策略类
 * 
 * @author zhoumb 2011-5-18
 */
@XStreamAlias("timer")
public class Timer implements Serializable {

	private static final long serialVersionUID = 5678072615108698194L;

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
	 * 类型
	 * 
	 * @see TimerType
	 */
	private TimerType type;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 时间
	 */
	private String time;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(TimerType type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescrption() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getTitle() {
		return title;
	}

	public TimerType getType() {
		return type;
	}

	public String getValue() {
		return value;
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
