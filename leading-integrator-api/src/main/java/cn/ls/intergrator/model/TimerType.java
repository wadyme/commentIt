/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.intergrator.model;

/**
 * 时间策略类型
 * 
 * @author zhoumb 2011-5-10
 */
public enum TimerType {
	
//	year,
	/**
	 * 月
	 */
	month,
	/**
	 * 天
	 */
	day,
	/**
	 * 周
	 */
	week,
	/**
	 * 间隔<br>
	 * 单位：s-秒 m-分钟 h-小时
	 */
	interval
}
