package cn.ls.integrator.components.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.integration.MessageHeaders;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.ExchangeStateManager;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.ExchangeState;
import cn.ls.integrator.core.model.MessageType;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class ExchangeStateManagerImpl extends AbstractMessageLogManagerImpl<ExchangeState>
		implements ExchangeStateManager{

	private static ExchangeStateManager instance;
	
	public static ExchangeStateManager getInstance (){
		if(instance == null){
			instance = new ExchangeStateManagerImpl();
		}
		return instance;
	}
	
	public ExchangeStateManagerImpl(){}
	
	@Override
	public Class<ExchangeState> getClazz() {
		return ExchangeState.class;
	}
	
	public List<ExchangeState> getList(Map<String,Object> parameters, int offset, int limit){
		List<ExchangeState> list = null;
		Query<ExchangeState> query = mongoDataStore.createQuery(getClazz());
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		//query.disableValidation();
		//TODO  需要检查parameters参数  是否为空  
		if (parameters != null) {
			Set<Entry<String, Object>> entrySet = parameters.entrySet();
			for (Entry<String,Object> entry : entrySet) {
				Object value = entry.getValue();
				if (value != null) {
					String valueStr = value.toString();
					Pattern pattern = Pattern.compile(valueStr , Pattern.CASE_INSENSITIVE);
					query.filter(entry.getKey(), pattern);
//					if(valueStr.startsWith("^") && valueStr.endsWith("$")){
//						Pattern pattern = Pattern.compile(".*" + 
//								valueStr.substring(1, valueStr.length()-1)+ ".*", Pattern.CASE_INSENSITIVE);
//						query.filter(entry.getKey(), pattern);
//					} else {
//						query.filter(entry.getKey(), valueStr);
//					}
				}
			}
		}
		if(offset >= 0 && limit > 0){
			query.order("-$natural").offset(offset).limit(limit);
		}
		list = query.asList();
		return list;
	}
	
	@Test
	public void testList(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("recvNodeName", "^Test151$");
		map.put("groupId", "5ca0cb18-9056-48fd-b461-8e2d08c5d0d7");
		//map.put("scheduleName", "^file$");
		//map.put("taskName", "^TEST$");
		map.put("sendNodeName", "^Test151$");
		map.put("logType", "^recv$");
		List<ExchangeState> logStr = getList(map, -1, -1);
		System.out.println(logStr.size());
	}
	
	public void beforeRecive (MessageHeaders messageHeaders){
		String taskName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String taskTitle = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE));
		String scheduleName =CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String scheduleTitle =CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_TITLE));
		String newGroupId= CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID));
		String sendNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		String logType = "recv";
		String fileName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		String messageType = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE));
		Query<ExchangeState> query = mongoDataStore.createQuery(ExchangeState.class);
		query.disableValidation();
		//TODO 猜测此处引起有0记录的情况
//		query.or(
//				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
//				query.criteria("integratorId").doesNotExist(),
//				query.criteria("integratorId").equal("")
//			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		long historyTotal = 0;
		ExchangeState exchangeState = null;
		List<ExchangeState> list = query.asList();
		if(list != null && list.size() != 0){
			exchangeState = list.get(0);
			String groupId = exchangeState.getGroupId();
			if(StringUtility.equals(newGroupId, groupId)){
				return ;
			} else {
				historyTotal = exchangeState.getHistoryTotal();
			}
		}
		// 写
		ExchangeState newExchangeState = new ExchangeState();
		newExchangeState.setIntegratorId(UniquelyIdentifies.getId());
		newExchangeState.setScheduleName(scheduleName);
		newExchangeState.setScheduleTitle(scheduleTitle);
		newExchangeState.setTaskName(taskName);
		newExchangeState.setTaskTitle(taskTitle);
		newExchangeState.setLogType(logType);
		newExchangeState.setGroupId(newGroupId);
		if(StringUtils.equals(messageType, MessageType.dataMessage.toString())){
			newExchangeState.setHistoryTotal(historyTotal);
		}else {
			newExchangeState.setFileName(fileName);
			newExchangeState.setPackageByteSize(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_PACKAGE_BYTE_SIZE)));
		}
		newExchangeState.setPercentage(0);
		newExchangeState.setCompleteSize(0);
		newExchangeState.setSendNodeName(sendNodeName);
		mongoDataStore.delete(query);
		mongoDataStore.save(newExchangeState);
	}
	
	public void deleteZeroData (MessageHeaders messageHeaders){
		String taskName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String taskTitle = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE));
		String scheduleName =CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String scheduleTitle =CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_TITLE));
		String newGroupId= CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID));
		String sendNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		String logType = "recv";
		String fileName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		String messageType = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE));
		Query<ExchangeState> query = mongoDataStore.createQuery(ExchangeState.class);
		query.disableValidation();
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		
//		query.filter(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, 0);
//		query.filter(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE, 0);
		query.filter(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE, 0);
		
		mongoDataStore.delete(query);
	}
	
	public void deleteState(String scheduleName,String taskName,String logType){
		if (StringUtility.isBlank(scheduleName) || StringUtility.isBlank(taskName)) {
			throw new IntegratorException("scheduleName and taskName can not blank");
		}
		if(StringUtility.isBlank(logType)){
			throw new IntegratorException("logType can not blank");
		}
		Query<ExchangeState> query = mongoDataStore.createQuery(ExchangeState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		mongoDataStore.delete(query);
	}
	
	public void updateDataMessageState(MessageHeaders messageHeaders){
		String scheduleName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String taskName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String logType = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE));
		String sendNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		String recvNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME));
		
		Query<ExchangeState> query = mongoDataStore.createQuery(ExchangeState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		if(StringUtility.equals(logType, "send")){
			if(StringUtility.isNotBlank(recvNodeName)){
				query.filter(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}else {
				query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}
		}else {
			if(StringUtility.isNotBlank(sendNodeName)){
				query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}else {
				query.filter(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}
		}
		List<ExchangeState> list = query.asList();
		int messageSize = CommonHelper.Null2Int(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE));
		long historyTotal = 0;
		long completeSize = 0;
		if(list == null || list.size() == 0){
			completeSize = messageSize;
			historyTotal = messageSize;
		}else {
			ExchangeState existLog = list.get(0);
			completeSize = existLog.getCompleteSize();
			String groupId = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID));
			String newGroutId = existLog.getGroupId();
			//如果当前Message中groupId和日志中的groupId不同
			if(!StringUtility.equals(groupId, newGroutId)){
				completeSize = messageSize;
			}else{
				completeSize += messageSize;
			}
			historyTotal = messageSize + existLog.getHistoryTotal();
		}
		ExchangeState newExchangeState = new ExchangeState();
		newExchangeState.setIntegratorId(UniquelyIdentifies.getId());
		setNewExchangeState(newExchangeState,messageHeaders);
		newExchangeState.setHistoryTotal(historyTotal);
		newExchangeState.setCompleteSize(completeSize);
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.putAll(messageHeaders);
		newExchangeState.setMessageDetail(headers);
		mongoDataStore.updateFirst(query, newExchangeState, true);
	}

	private void setNewExchangeState(ExchangeState newExchangeState,MessageHeaders messageHeaders) {
		newExchangeState.setId(null);
		newExchangeState.setExchangeTaskName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME)));
		newExchangeState.setGroupId(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID)));
		newExchangeState.setGuoupSize(CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE)));
		newExchangeState.setLogType(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE)));
		newExchangeState.setSendNodeName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME)));
		newExchangeState.setRecvNodeName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME)));
		newExchangeState.setReceiveDate(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE)));
		newExchangeState.setPercentage(CommonHelper.Null2Int(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE)));
		newExchangeState.setScheduleName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME)));
		newExchangeState.setScheduleTitle(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_TITLE)));
		newExchangeState.setTaskName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME)));
		newExchangeState.setTaskTitle(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_TITLE)));
		newExchangeState.setProduceDate(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE)));
		newExchangeState.setMessageType(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE)));
	}

	@Override
	public void updateFileMessageState(MessageHeaders messageHeaders) {
		String scheduleName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String taskName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String logType = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE));
		String sendNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		String recvNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME));
		Query<ExchangeState> query = mongoDataStore.createQuery(ExchangeState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		if(StringUtility.equals(logType, "send")){
			if(StringUtility.isNotBlank(recvNodeName)){
				query.filter(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}else {
				query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}
		}else {
			if(StringUtility.isNotBlank(sendNodeName)){
				query.filter(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}else {
				query.filter(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}
		}
		ExchangeState newExchangeState = new ExchangeState();
		newExchangeState.setIntegratorId(UniquelyIdentifies.getId());
		setNewExchangeState(newExchangeState,messageHeaders);
		newExchangeState.setFileName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME)));
		newExchangeState.setFileTotalNumber(CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_NUMBER)));
		long fileCompleteNumber = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_NUMBER));
		long fileCompleteBytes = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES));
		long fileTotalBytes = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES));
		long completeSize = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE));
		long groupSize = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE));
		//当文件的最后一个包到来时，文件完成数+1
		if(fileTotalBytes == fileCompleteBytes){
			fileCompleteNumber ++;
		}
		//以文件总Byte 计算交换百分比
		int percentage = (int)(completeSize * 100 / groupSize);
		newExchangeState.setFileCompleteNumber(fileCompleteNumber);
		newExchangeState.setPercentage(percentage);
		newExchangeState.setCompleteSize(CommonHelper.Null2Int(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE)));
		newExchangeState.setPackageByteSize(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_PACKAGE_BYTE_SIZE)));
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.putAll(messageHeaders);
		newExchangeState.setMessageDetail(headers);
		mongoDataStore.updateFirst(query, newExchangeState, true);
	}
}
