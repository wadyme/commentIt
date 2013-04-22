package cn.ls.integrator.components.manager;

import java.util.List;
import java.util.Map;

import org.springframework.integration.MessageHeaders;

import cn.ls.integrator.core.log.message.FileExchangeLog;

public interface FileExchangeLogManager {
	
	public List<FileExchangeLog> getList(Map<String,Object> parameters, int offset, int limit);
	
	/**
	 * 删除交换日志
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 */
	public void deleteLog(String scheduleName, String taskName,String logType);

	public void updateFileExchangeLog(MessageHeaders newHeader);
}
