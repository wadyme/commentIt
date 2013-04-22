package cn.ls.integrator.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.filter.MessageFilter;
import org.springframework.integration.support.MessageBuilder;

import com.mchange.v2.c3p0.impl.NewPooledConnection;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.handler.PayloadFilterSelector;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.model.StatisticInfo;
import cn.ls.integrator.core.utils.StringUtility;

/**
 *
 * 过滤器组件
 * 
 * 根据配置的条件表达式，对消息进行过滤后，发送给下个组件
 * 
 * @author liuyf
 *
 */
public class MessageFilterComponent extends SafeMessageProducerSupport implements MessageHandler {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private volatile String expression;
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	private String recvNodeName; //解决数据库分发数据，新添加属性
	
	public String getRecvNodeName() {
		return recvNodeName;
	}

	public void setRecvNodeName(String recvNodeName) {
		this.recvNodeName = recvNodeName;
	}

	public static Map<String,Object> groupsMap = new HashMap<String,Object>();
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if(StringUtility.isBlank(expression)){
			throw new IntegratorException("过滤器表达式不可为空");
		}
		
		if(StringUtility.isBlank(recvNodeName)){
			throw new IntegratorException("接收节点名不可为空");
		}
		
		MessageFilter filter = new MessageFilter(new PayloadFilterSelector(expression));
		filter.setOutputChannel(new NullChannel());
		Map<String,Object> headers = new HashMap<String,Object>();
		headers.putAll(message.getHeaders());
		Object payload = message.getPayload();
		List<Object> newPayload = null;
		if(payload instanceof List<?>){
			newPayload = new ArrayList<Object>();
			newPayload.addAll((List<?>)payload);
			Message<?> newMessage = MessageBuilder.withPayload(newPayload).copyHeaders(headers).build();
			filter.handleMessage(newMessage);
			Map<String, Object> newHeaders = new HashMap<String, Object>();
			int recordSize = ((List<?>)newMessage.getPayload()).size();
//			if(recordSize == 0){
//				return;
//			}
			newHeaders.putAll(newMessage.getHeaders());
			newHeaders.put(IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE, recordSize);
			
			//解决数据库分发百分百，“本次交换总数”》“ 本次已交换数量”问题 
			newHeaders.put(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, calculateGroupSizeHandler(message,recordSize));
			
			sendMessage(MessageBuilder.withPayload(newMessage.getPayload()).copyHeaders(newHeaders).build());
		}
	}
	/**
	 * 每次分包后，计算数据库分发条数的处理方法
	 * @param newMessage 消息头
	 * @param recordSize 每次分包后，按条件过滤的条数
	 * @return
	 */
	private long calculateGroupSizeHandler(Message<?> newMessage,int recordSize){
		logger.debug(newMessage.getHeaders().toString());
		Long groupSizeOriginalData = (Long)newMessage.getHeaders().get(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE);
		String groupId =(String)newMessage.getHeaders().get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID);
		String percentString = newMessage.getHeaders().get(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE).toString();
		int percentage =  Integer.valueOf(percentString);
		long groupSize = calculateGroupSize(groupSizeOriginalData,groupId,percentage,recordSize);
		return groupSize;
	}
	/**
	 * 每次分包后，计算数据库分发到各chain链上应传输的数据条数
	 * @param groupSizeOriginalData 数据库未分发前抽取数据的总条数
	 * @param groupId 组ID
	 * @param percentage 传输百分比（整数）
	 * @param recordSize 每次分包后，按条件过滤的条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private long calculateGroupSize(Long groupSizeOriginalData,String groupId,int percentage,int recordSize){
		long groupSize = 0;
		Map<String,StatisticInfo> sendMap = null;
		
		if (groupsMap.get(groupId)==null) {
			sendMap = new HashMap<String,StatisticInfo>();
			StatisticInfo stInfo = new StatisticInfo();
			stInfo.setGroupSize(recordSize+0L);
			stInfo.setPercentage(percentage);
			sendMap.put(recvNodeName, stInfo);
			groupSize = recordSize;
		}else {
			sendMap = (Map<String,StatisticInfo> )groupsMap.get(groupId);
			if(sendMap.get(recvNodeName)==null){
				StatisticInfo stInfo = new StatisticInfo();
				stInfo.setGroupSize(recordSize+0L);
				stInfo.setPercentage(percentage);
				sendMap.put(recvNodeName, stInfo);
				groupSize = recordSize;
			}else {
				StatisticInfo stInfo = sendMap.get(recvNodeName);
				groupSize = stInfo.getGroupSize()+recordSize;
				stInfo.setGroupSize(groupSize);
				stInfo.setPercentage(percentage);
				sendMap.put(recvNodeName, stInfo);
			}
		}
		groupsMap.put(groupId, sendMap);
		handleGroupsMap(groupSizeOriginalData,groupId);
		logger.debug("groupSize："+groupSize);
		return groupSize;
	}
	/**
	 * 任务传输完毕后，释放GroupsMap中使用的资源
	 * @param groupSizeOriginalData
	 * @param groupId
	 */
	@SuppressWarnings("unchecked")
	private void handleGroupsMap(Long groupSizeOriginalData,String groupId){
		Map<String,StatisticInfo> sendMap = null;
		long groupSizeFact = 0L;
		boolean flag = true;
		if (groupsMap.get(groupId)==null) {
			return;
		}else{
			sendMap = (Map<String,StatisticInfo>)groupsMap.get(groupId);
			Iterator<String> iterator= sendMap.keySet().iterator();
			while (iterator.hasNext()) {
				String recvNodeName = (String) iterator.next();
				StatisticInfo stInfo = (StatisticInfo)sendMap.get(recvNodeName);
				groupSizeFact = groupSizeFact + stInfo.getGroupSize();
				if(stInfo.getPercentage()!=100) flag = false;
			}
			if(groupSizeFact==groupSizeOriginalData&&flag){
				sendMap.clear();
				groupsMap.remove(groupId);
				logger.debug("groupsMap中移除groupId为："+groupId);
			}
		}
		return;
	}
}
