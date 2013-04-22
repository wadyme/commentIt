/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.intergrator.model;

/**
 * 时间策略
 * 
 * @author zhoumb 2011-5-18
 */
public interface Timer {

	String getName();

	String getTitle();

	String getValue();

	String getTime();

	TimerType getType();

	String getDescrption();

}
