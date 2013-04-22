package cn.ls.integrator.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.ExchangeStateManager;
import cn.ls.integrator.components.manager.FileExchangeLogManager;
import cn.ls.integrator.components.manager.impl.ExchangeStateManagerImpl;
import cn.ls.integrator.components.manager.impl.FileExchangeLogManagerImpl;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.log.message.MessageLog;
import cn.ls.integrator.core.model.MessageType;
import cn.ls.integrator.core.utils.ThreadUtils;
import cn.ls.integrator.core.version.UniquelyIdentifies;
/**
 *   
* @ClassName: MessageLogComponent 
* @Description: 接收日志
* @author wanl
* @date 2011-5-3 上午01:35:45 
* @version V1.0
 */
public class MessageLogComponent extends SafeReplyProducingMessageHandler implements MessageHandler{
	
	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		Message<?> errorMessage = null;
		try {
			ThreadUtils.checkThreadInterrupted();
			MessageHeaders messageHeaders = requestMessage.getHeaders();
			Map<String, Object> messageHeaderMap = new HashMap<String, Object>();
			messageHeaderMap.putAll(messageHeaders);
			Object errorObj = messageHeaderMap.remove(IntegratorConstants.MESSAGE_HEADER_ERROR_MESSAGE);
			if(errorObj != null){
				errorMessage = (Message<?>) errorObj;
			}
			MessageHeaders newHeader = MessageBuilder.withPayload(requestMessage.getPayload()).copyHeaders(messageHeaderMap).build().getHeaders();
			MessageLog log = new MessageLog();
			log.setIntegratorId(UniquelyIdentifies.getId());
			log.setId(null);
			log.setExchangeTaskName(CommonHelper.Null2String(newHeader
					.get(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME)));
			log.setMessageHeaders(newHeader);
			mongoDataStore.save(log);
			MessageType messageType = (MessageType)newHeader.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE);
			if(messageType.equals(MessageType.dataMessage)){
				getExchangeStateManager().updateDataMessageState(newHeader);
			}else if(messageType.equals(MessageType.fileMessage)){
				getExchangeStateManager().updateFileMessageState(newHeader);
				getFileExchangeLogManager().updateFileExchangeLog(newHeader);
			}
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程中断", e);
			throw e;	
		}
		if(errorMessage != null){
			throw new MessagingException(errorMessage, "组件执行出错");
		}
		return requestMessage;
	
	}
	
	private ExchangeStateManager getExchangeStateManager(){
		return ExchangeStateManagerImpl.getInstance();
	}
	
	private FileExchangeLogManager getFileExchangeLogManager(){
		return FileExchangeLogManagerImpl.getInstance();
	}
}
