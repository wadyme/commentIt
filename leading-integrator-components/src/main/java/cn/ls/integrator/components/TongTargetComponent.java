package cn.ls.integrator.components;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.tlq.base.TlqConnection;
import com.tongtech.tlq.base.TlqException;
import com.tongtech.tlq.base.TlqMessage;
import com.tongtech.tlq.base.TlqMsgOpt;
import com.tongtech.tlq.base.TlqQCU;

/**
 * 
 * @ClassName: TongTargetComponent
 * @Description: 东方通发送组件
 * @author wanl
 * @date 2011-4-22 上午09:36:23
 * @version V1.0
 */
public class TongTargetComponent extends SafeReplyProducingMessageHandler
		implements MessageHandler {

	public String qcuName;
	public String QName;

	public String operationOnNoSpace;

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		MessageHeaders requestHeaders = requestMessage.getHeaders();
		String taskName = (String)requestHeaders.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
		String scheduleName = (String)requestHeaders.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
		Message<?> message = null;
		TlqQCU tlqQcu = null;
		TlqConnection tlqConnection = null;
		try {
			ThreadUtils.checkThreadInterrupted();
			TlqMessage msgInfo = new TlqMessage();
			TlqMsgOpt msgOpt = new TlqMsgOpt();
			tlqConnection = new TlqConnection();
			msgOpt.QueName = QName;
			tlqQcu = tlqConnection.openQCU(qcuName);
//			JsonOutboundMessageMapper messageMapper = new JsonOutboundMessageMapper();
//			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.configure(
//					SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
//					false);
//			SimpleDateFormat sdf = new SimpleDateFormat(
//					"yyyy-MM-dd#$$#HH:mm:ss:SSS");
//			objectMapper.getSerializationConfig().setDateFormat(sdf);
//			messageMapper.setObjectMapper(objectMapper);
//			String jsonMessage = messageMapper.fromMessage(requestMessage);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos); 
			oos.writeObject(requestMessage);
			byte[] msgContent = baos.toByteArray(); // 消息内容
			msgInfo.setMsgData(msgContent);
			msgInfo.MsgType = TlqMessage.BUF_MSG; // 消息类型
			msgInfo.MsgSize = msgContent.length; // 消息大小
			msgInfo.Persistence = TlqMessage.TLQPER_Y; // 持久性
			msgInfo.Priority = TlqMessage.TLQPRI_NORMAL; // 优先级
  
			while(true){
				try {
					tlqQcu.putMessage(msgInfo, msgOpt);
					break;
				} catch (TlqException e) {
					if(1017 == e.getTlqErrno()){
						if(StringUtility.equalsIgnoreCase("wait", getOperationOnNoSpace()) 
								|| StringUtility.isBlank(getOperationOnNoSpace())){
							logger.error(qcuName + "." + QName + "队列已满，等待5秒");
							Thread.sleep(5000);
						}else if(StringUtility.equalsIgnoreCase("giveup", getOperationOnNoSpace())){
							logger.error(qcuName + "." + QName + "队列已满", e);
							break;
						}else if(StringUtility.equalsIgnoreCase("stop", getOperationOnNoSpace())){
							throw e;
						} else {
							throw e;
						}
					}else {
						throw e;
					}
				}
			}
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程中断", e);
			throw e;
		} catch (Exception e) {
			logger.error("scheduleName: " + scheduleName + ", taskName: " + taskName + ": TongTargetComponent is fail ! ", e);
			throw new MessagingException(requestMessage,
					"TongTargetComponent is fail !", e);
		} finally {
			try {
				if (tlqQcu != null)
					tlqQcu.close();
				if (tlqConnection != null)
					tlqConnection.close();
			} catch (TlqException e) {
				logger.warn("tlqConnection连接关闭出现异常 ", e);
			}
		}
		Map<String, Object> headers = new HashMap<String, Object>();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 没有错误发送消息时间
		headers.put(IntegratorConstants.MESSAGE_HEADER_PRODUCE_DATE, format.format(date));
		message = MessageBuilder.fromMessage(requestMessage).copyHeaders(headers).build();
		return message;
	}


	public void setQcuName(String qcuName) {
		this.qcuName = qcuName;
	}

	public void setQName(String qName) {
		this.QName = qName;
	}

	public String getOperationOnNoSpace() {
		return operationOnNoSpace;
	}

	public void setOperationOnNoSpace(String operationOnNoSpace) {
		this.operationOnNoSpace = operationOnNoSpace;
	}
}
