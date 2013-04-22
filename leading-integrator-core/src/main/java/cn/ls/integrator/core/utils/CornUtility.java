/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.utils;

import cn.ls.integrator.core.model.Timer;

/**
 * corn表达式工具类
 * 
 * @author zhoumb 2011-5-17
 */
public class CornUtility {

	/**
	 * 获得时间策略对应的cron表达式<br/>
	 * @param timer 时间策略对象
	 * @return 时间策略对应的cron表达式
	 */
	public static String getExpression(Timer timer) {
		StringBuilder expression = new StringBuilder();
		switch (timer.getType()) {
		case month: {
			// 每月4号"1 2 3 4 * ?"
			splitTimeString(expression, timer);
			expression.append(timer.getValue()).append(" * ?");
			break;
		}
		case week: {
			// 每周3"1 2 3 ? * 4"
			splitTimeString(expression, timer);
			expression.append("? * ").append(timer.getValue());
			break;
		}
		case day: {
			// 每天"1 2 3 * * ?"
			splitTimeString(expression, timer);
			expression.append("* * ?");
			break;
		}
		case interval: {
			createIntervalExpression(expression, timer);
			break;
		}
		default:
			// TODO 默认的时间策略
			break;
		}
		return expression.toString();
	}

	private static void splitTimeString(StringBuilder expression, Timer timer) {
		// 3点2分1秒 "1 2 3 "
		String[] time = timer.getTime().split(":");
		if (time.length == 3) {
			expression.append(time[2]).append(" ").append(time[1]).append(" ").append(time[0]).append(" ");
		} else if (time.length == 2) {
			expression.append("0 ").append(time[1]).append(" ").append(time[0]).append(" ");
		} else if (time.length == 1) {
			expression.append("0 0 ").append(time[0]).append(" ");
		} else {
			expression.append("0 0 0 ");
		}
	}

	private static void createIntervalExpression(StringBuilder expression, Timer timer) {
		String intervalType = timer.getValue().toLowerCase();
		if ("s".equals(intervalType)) {
			expression.append("0/").append(timer.getTime()).append(" * * * * ?");
		} else if ("m".equals(intervalType)) {
			expression.append("0 0/").append(timer.getTime()).append(" * * * ?");
		} else if ("h".equals(intervalType)) {
			expression.append("0 0 0/").append(timer.getTime()).append(" * * ?");
		}
	}
}
