package cn.ls.integrator.components.manager.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.ExchangeErrorLogManager;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.ExchangeErrLog;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class ExchangeErrorLogManagerImpl extends AbstractMessageLogManagerImpl<ExchangeErrLog>
		implements ExchangeErrorLogManager{

	private static ExchangeErrorLogManager instance;
	
	private ExchangeErrorLogManagerImpl(){}
	
	public static ExchangeErrorLogManager getInstance(){
		if(instance == null){
			instance = new ExchangeErrorLogManagerImpl();
		}
		return instance;
	} 
	
	@Override
	public Class<ExchangeErrLog> getClazz() {
		return ExchangeErrLog.class;
	}
	
	public boolean hasErrorLog(String scheduleName, String taskName, String sendNodeName, String recvNodeName, String logType){
		if (StringUtility.isBlank(scheduleName) || StringUtility.isBlank(taskName)) {
			throw new IntegratorException("scheduleName and taskName can not blank");
		}
		Query<ExchangeErrLog> query = mongoDataStore.createQuery(ExchangeErrLog.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		
		if(StringUtility.isNotBlank(sendNodeName) || StringUtility.isNotBlank(recvNodeName)){
			if(StringUtility.isNotBlank(sendNodeName)){
				query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}
			if(StringUtility.isNotBlank(recvNodeName)){
				query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}
		}else {
			query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
		}
		if(query.countAll()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public List<Map<?,?>> getLogList(Map<String,Object> parameters, int offset, int limit){
		List<ExchangeErrLog> list = getList(parameters, offset, limit);
		List<Map<?,?>> mapList = new ArrayList<Map<?,?>>();
		if(list != null){
			for(ExchangeErrLog log : list){
				Map<String,Object> map = log.getMessageHeaders();
				map.put("errDate", log.getErrDate());
				map.put("bussnessName", log.getTaskName());
				map.put("errMsg", log.getErrMsg());
				mapList.add(map);
			}
		}
		return mapList;
	}
	public List<ExchangeErrLog> getList(Map<String,Object> parameters, int offset, int limit){
		Query<ExchangeErrLog> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
		if(parameters!=null){
			Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String,Object> entry =  it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();	
				query.filter("messageHeaders."+key.toString(), getPatternValue(value));	
			}	
		}
		if(offset >=0 && limit > 0){
			query.order("-$natural").offset(offset).limit(limit);
		}
		return query.asList();
	}
	
	public void delete(String scheduleName, String taskName, String sendNodeName, String recvNodeName, String logType){
		if (StringUtility.isBlank(scheduleName) || StringUtility.isBlank(taskName)) {
			throw new IntegratorException("scheduleName and taskName can not blank");
		}
		Query<ExchangeErrLog> query = mongoDataStore.createQuery(ExchangeErrLog.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME, scheduleName);
		query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		
		if(StringUtility.isNotBlank(sendNodeName) || StringUtility.isNotBlank(recvNodeName)){
			if(StringUtility.isNotBlank(sendNodeName)){
				query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			}
			if(StringUtility.isNotBlank(recvNodeName)){
				query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
			}
		}else {
			query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
			query.filter("messageHeaders." + IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
		}
		
		mongoDataStore.delete(query);
	}
}
