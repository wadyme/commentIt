/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.NullChannel;

import cn.ls.integrator.core.context.support.StringXmlApplicationContext;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.model.TaskCacheKey;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.MessageToXMLUtil;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemConfigUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;
import cn.ls.integrator.core.utils.TaskParameterUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

/**
 * 任务执行基类
 * 
 * @author zhoumb 2011-6-2
 */
public abstract class TaskExecuter {
	
	private static final Logger logger = Logger.getLogger(TaskExecuter.class);

	protected static String inputChannelName = "agrsChannel";
	
	private static final String DEFAULT_MESSAGE_UNKNOWN_PATH = "unknown";

	protected static Map<TaskCacheKey, MessageChannel> taskChannelMap;

	protected static synchronized Map<TaskCacheKey, MessageChannel> getTaskMap() {
		if (taskChannelMap == null) {
			taskChannelMap = new HashMap<TaskCacheKey, MessageChannel>();
		}
		return taskChannelMap;
	}

	public static void removeTaskMap(String scheduleName, String taskName, TaskType type) {
		getTaskMap().remove(new TaskCacheKey(scheduleName, taskName, type));
	}
	
	public static void clearTakMap(){
		getTaskMap().clear();
	}
	
	protected static MessageChannel getTaskChannel(String scheduleName, String taskName, TaskType type) throws FileNotFoundException {
		Map<TaskCacheKey, MessageChannel> taskMap = getTaskMap();
		String taskPath = FileUtility.getInstance().getTaskFilePath(scheduleName, taskName, type);
		if(!new File(taskPath).exists()){
			throw new FileNotFoundException("系统找不到指定的路径:" + taskPath);
		}
		MessageChannel input = null;
		TaskCacheKey taskKey = new TaskCacheKey(scheduleName, taskName, type);
		if (!taskMap.containsKey(taskKey)) {
	//		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(taskPath);
			String taskContextString = TaskParameterUtility.getInstance().getTaskContextString(scheduleName, taskName, type);
			StringXmlApplicationContext context = new StringXmlApplicationContext(taskContextString);
	//		MessageHandlerChain chain = context.getBean(MessageHandlerChain.class);
	//		chain.setOutputChannel(new NullChannel());
			input = (MessageChannel) context.getBean(inputChannelName);
			taskMap.put(taskKey, input);
		} else {
			input = taskMap.get(taskKey);
		}
		return input;
	}

	protected MessageChannel createNullOutputChannel() {
		return new NullChannel();
	}
	/**
	 * 1：根据传入的参数任务调度名 scheduleName，任务名taskName和任务类型type获得MessageChannel对象input
	 * 2：发送传入参数Message对象message，用input.send(message)
	 * @param scheduleName 任务调度名，String类型
	 * @param taskName 任务名，String类型
	 * @param message 消息内容， Message类型
	 * @param type 任务的类型（发送或接收），String类型
	 */
	public static void execute(String scheduleName, String taskName, Message<?> message, TaskType type){
		try {
			MessageChannel input = getTaskChannel(scheduleName, taskName, type);
			ThreadUtils.checkThreadInterrupted();
			input.send(message);
		} catch (FileNotFoundException e){
			logger.error("执行任务时发生异常", e);
			// 将错误的消息写入到日志文件
			logger.info("开始将错误消息写入相应的目录");
			writeErrorMessage(message);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	

	/**
	 * 将错误的消息以文件的形式写入到磁盘
	 * @param inMessage
	 */
	protected static void writeErrorMessage(Message<?> inMessage) {
		String path = null;
		try {
			path = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MESSAGE_UNKNOWN_PATH);
		} catch (IntegratorException e) {
		}
		if(StringUtility.isBlank(path)){
			String messagePath = SystemEnvUtility.getMessagePath();
			path = messagePath + SystemEnvUtility.pathSeparator + DEFAULT_MESSAGE_UNKNOWN_PATH;
		}
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String filePath = path + SystemEnvUtility.pathSeparator + MessageToXMLUtil.getFileName(inMessage.getHeaders());
		MessageToXMLUtil.write(inMessage, filePath);
		
	}

}
