package cn.ls.integrator.components.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.integration.MessageHeaders;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.FileExchangeLogManager;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.FileExchangeLog;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class FileExchangeLogManagerImpl extends AbstractMessageLogManagerImpl<FileExchangeLog>
		implements FileExchangeLogManager{

	private static FileExchangeLogManager instance;
	
	public static FileExchangeLogManager getInstance (){
		if(instance == null){
			instance = new FileExchangeLogManagerImpl();
		}
		return instance;
	}
	
	private FileExchangeLogManagerImpl(){}
	
	public Class<FileExchangeLog> getClazz() {
		return FileExchangeLog.class;
	}
	
	public List<FileExchangeLog> getList(Map<String,Object> parameters, int offset, int limit){
		List<FileExchangeLog> list = null;
		Query<FileExchangeLog> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
		//TODO  需要检查parameters参数  是否为空  
		if (parameters != null) {
			Set<Entry<String, Object>> entrySet = parameters.entrySet();
			for (Entry<String,Object> entry : entrySet) {
				Object value = entry.getValue();
				if (value != null) {
					Pattern pattern = Pattern.compile("^.*" + value+ ".*$");
					query.filter(entry.getKey(), pattern);
				}
			}
		}
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		if(offset >= 0 && limit > 0){
			query.order("-$natural").offset(offset).limit(limit);
		}
		list = query.asList();
		return list;
	}
	
	public void deleteLog(String scheduleName,String taskName,String logType){
		if (StringUtility.isBlank(scheduleName) || StringUtility.isBlank(taskName)) {
			throw new IntegratorException("scheduleName and taskName can not blank");
		}
		if(StringUtility.isBlank(logType)){
			throw new IntegratorException("logType can not blank");
		}
		Query<FileExchangeLog> query = mongoDataStore.createQuery(FileExchangeLog.class);
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
	

	private void setNewExchangeState(FileExchangeLog newFileExchangeLog,MessageHeaders messageHeaders) {
		newFileExchangeLog.setId(null);
		newFileExchangeLog.setGroupId(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID)));
		newFileExchangeLog.setLogType(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE)));
		newFileExchangeLog.setSendNodeName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME)));
		newFileExchangeLog.setRecvNodeName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME)));
		newFileExchangeLog.setRecieveDate(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE)));
		newFileExchangeLog.setScheduleName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME)));
		newFileExchangeLog.setTaskName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME)));
		newFileExchangeLog.setProduceDate(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE)));
	}

	public void updateFileExchangeLog(MessageHeaders messageHeaders){
		String scheduleName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String taskName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String logType = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE));
		String sendNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		String recvNodeName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME));
		String fileName = CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		
		Query<FileExchangeLog> query = mongoDataStore.createQuery(FileExchangeLog.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		query.filter(IntegratorConstants.MESSAGE_HEADER_FILE_NAME, fileName);
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
		FileExchangeLog newFileExchangeLog = new FileExchangeLog();
		newFileExchangeLog.setIntegratorId(UniquelyIdentifies.getId());
		setNewExchangeState(newFileExchangeLog,messageHeaders);
		newFileExchangeLog.setFileName(CommonHelper.Null2String(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME)));
		long fileCompleteBytes = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES));
		long fileTotalBytes = CommonHelper.Null2Long(messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES));
		//以文件总Byte 计算交换百分比
		int percentage = (int)(fileCompleteBytes * 100 / fileTotalBytes);
		newFileExchangeLog.setTotalSize(fileTotalBytes);
		newFileExchangeLog.setPercentage(percentage);
		newFileExchangeLog.setCompleteSize(fileCompleteBytes);
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.putAll(messageHeaders);
		mongoDataStore.updateFirst(query, newFileExchangeLog, true);
	}
}
