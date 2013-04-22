package cn.ls.integrator.components;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.utils.SystemConfigUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.org.apache.log4j.Logger;
/**
 * 负责通知第三方，文件已接受完毕组件
 * @author dengwei
 *
 */
public class ThirdPartyReceiveNotificationComponet extends SafeReplyProducingMessageHandler implements MessageHandler{

	private Logger logger = Logger.getLogger(MessageDetailComponet.class);
	
	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		String fileName = null;
		Long fileSize=0L;
		String sendNodeName = null;
		String recvNodeName = null;
		String scheduleName = null;
		String sendTime = null;
		String recvTime = null;
		Message<?> errorMessage = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//demp服务的地址
		String dempUrl = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.THIRD_PARTY_RECEIVE_NOTIFICATION_DEMP_URL);
		//第三方服务的地址
		//String thirdPartyUrl = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.THIRD_PARTY_RECEIVE_NOTIFICATION_URL);
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
			fileName = (String)newHeader.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME);
			fileSize = Long.valueOf((newHeader.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE)).toString());
			sendNodeName = (String)newHeader.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME);
			recvNodeName = (String)newHeader.get(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME);
			sendTime = sdf.format((Date)newHeader.get(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE));
			recvTime = (String)newHeader.get(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE);
			scheduleName=(String)newHeader.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程中断", e);
			throw e;	
		}
		if(errorMessage != null){
			throw new MessagingException(errorMessage, "组件执行出错");
		}
		try {
			if(StringUtils.isNotBlank(dempUrl)){
				Object[] params = new Object[]{fileName,fileSize,sendNodeName,recvNodeName,sendTime,recvTime,scheduleName};
				Object[] result = new Object[]{};
				Client client = new Client(new URL(dempUrl));
				result = client.invoke("receiveDempNoteceMessage", params);
				logger.info("ThirdPartyReceiveNotificationResult->receiveDempNoteceMessage->"+result[0]);
			}
//			if(StringUtils.isNotBlank(thirdPartyUrl)){
//				//TODO 调用第三方webservice实现
//			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestMessage;
	}

}
