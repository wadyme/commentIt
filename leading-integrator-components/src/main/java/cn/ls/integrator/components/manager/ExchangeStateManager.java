package cn.ls.integrator.components.manager;

import java.util.List;
import java.util.Map;

import org.springframework.integration.MessageHeaders;

import cn.ls.integrator.core.log.message.ExchangeState;

public interface ExchangeStateManager {
	
	public List<ExchangeState> getList(Map<String,Object> parameters, int offset, int limit);
	
	/**
	 * 执行接收任务之前，判断消息是否是该组第一条消息，如果是第一条则初始化一条记录
	 * @param messageHeaders
	 */
	public void beforeRecive(MessageHeaders messageHeaders);
	
	/**
	 * 执行任务失败，删除0数据
	 * @param messageHeaders
	 */
	public void deleteZeroData (MessageHeaders messageHeaders);
	
	/**
	 * 保存或更新交换日志（当前交换状态）
	 * @param messageHeaders
	 */
	public void updateDataMessageState(MessageHeaders messageHeaders);
	
	/**
	 * 删除交换日志
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 */
	public void deleteState(String scheduleName, String taskName,String logType);

	public void updateFileMessageState(MessageHeaders newHeader);
}
