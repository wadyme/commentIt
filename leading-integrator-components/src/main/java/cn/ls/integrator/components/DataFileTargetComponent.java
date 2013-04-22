package cn.ls.integrator.components;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.message.ErrorMessage;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.MessageToXMLUtil;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.org.apache.log4j.Logger;

public class DataFileTargetComponent extends SafeReplyProducingMessageHandler
		implements MessageHandler {
	private String settingPath = SystemEnvUtility.getLiHomePath();
	private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
	private Logger logger = Logger.getLogger(DataFileTargetComponent.class);
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecn.ls.integrator.core.handler.SafeReplyProducingMessageHandler#
	 * handleRequestMessage(org.springframework.integration.Message)
	 */
	@Override
	protected Message<?> handleRequestMessage(Message<?> requestMessage) {
		// TODO Auto-generated method stub
		MessageHeaders headers = requestMessage.getHeaders();
		String file = getFilePath(headers);
		try {
			ThreadUtils.checkThreadInterrupted();
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程已停止", e);
			throw e;
		}
		MessageToXMLUtil.write(requestMessage, file);
		Message<?> message = null;
		if (!errorMessages.isEmpty()) {
			message = MessageBuilder.withPayload(errorMessages).build();
			throw new MessagingException(message);
		} else {
			Map<String, Object> adapterheaders = new HashMap<String, Object>();
			Date date = new Date();
			// 没有错误 接受消息时间
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE, format
					.format(date));
			message = MessageBuilder.withPayload(requestMessage).copyHeaders(
					headers).copyHeaders(adapterheaders).build();
		}
		return message;
	}

	private String getFilePath(MessageHeaders headers) {
		String fileName = MessageToXMLUtil.getFileName(headers);
		if (StringUtility.isBlank(filePath)) {
			filePath = getSettingFloderPath() + FileUtility.pathSeparator
					+ "messages" ;
		}
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return filePath + FileUtility.pathSeparator + fileName;
	}

	private String getSettingFloderPath() {
		settingPath += FileUtility.pathSeparator + "schedules";
		File floder = new File(settingPath);
		if (!floder.exists()) {
			floder.mkdirs();
		}
		return settingPath;
	}
}
