/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.model;

import cn.ls.integrator.core.utils.SystemEnvUtility;

/**
 * 任务类型
 * 
 * @author zhoumb 2011-6-6
 */
public enum TaskType {

	/**
	 * 发送
	 */
	send,
	/**
	 * 接收
	 */
	recv;
	
	
	public static final String SENDFORDERNAME = "send";
	
	public static final String RECVFORDERNAME = "recv";
	
	public String getTaskForderPath(String parentPath){
		String taskFilePath = null;
		switch (this) {
		case send:
			taskFilePath = parentPath + SystemEnvUtility.pathSeparator + SENDFORDERNAME;
			break;
		case recv:
			taskFilePath = parentPath + SystemEnvUtility.pathSeparator + RECVFORDERNAME;
			break;
		default:
			break;
		}
		return taskFilePath;
	}
}
