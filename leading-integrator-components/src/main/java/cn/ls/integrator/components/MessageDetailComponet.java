package cn.ls.integrator.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.org.apache.log4j.Logger;

public class MessageDetailComponet extends SafeMessageProducerSupport implements
		MessageHandler {
	private String sendNodeName;
	private String recvNodeName;
	private String logType;
	private Logger logger = Logger.getLogger(MessageDetailComponet.class);

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		Map<String, Object> detailHeader = new HashMap<String, Object>();
		try {
			ThreadUtils.checkThreadInterrupted();
		} catch (InterruptedRuntimeException e) {
			logger.error("线程已停止", e);
			throw e;
		}
		if(StringUtility.isNotBlank(sendNodeName)){
			detailHeader.put(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME, sendNodeName);
		}
		if(StringUtility.isNotBlank(recvNodeName)){
			detailHeader.put(IntegratorConstants.MESSAGE_HEADER_RECV_NODE_NAME, recvNodeName);
		}
		if(StringUtility.isNotBlank(logType)){
			detailHeader.put(IntegratorConstants.MESSAGE_HEADER_LOG_TYPE, logType);
		}
		Message<?> newMessage = MessageBuilder.fromMessage(message)
				.copyHeaders(detailHeader).build();
		sendMessage(newMessage);
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getSendNodeName() {
		return sendNodeName;
	}

	public void setSendNodeName(String sendNodeName) {
		this.sendNodeName = sendNodeName;
	}

	public String getRecvNodeName() {
		return recvNodeName;
	}

	public void setRecvNodeName(String recvNodeName) {
		this.recvNodeName = recvNodeName;
	}

}
