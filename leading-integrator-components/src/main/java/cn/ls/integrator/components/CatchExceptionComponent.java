package cn.ls.integrator.components;

import org.springframework.integration.Message;
import org.springframework.integration.MessageDeliveryException;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.utils.ThreadUtils;
/**
 * 
 * @ClassName: CatchExceptionComponent
 * @Description: 错误异常捕获
 * @author wanl
 * @date 2011-5-3 上午12:43:04
 * @version V1.0
 */
public class CatchExceptionComponent  extends SafeMessageProducerSupport implements MessageHandler {
	
	private MessageHandler errorHandler;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			ThreadUtils.checkThreadInterrupted();
			sendMessage(message);
		} catch (InterruptedRuntimeException e) {
			logger.error("线程已停止", e);
			throw e;
		} catch (MessagingException e) {
			logger.error(" CatchExceptionComponent is fail ! ",e);
			Message<?> failedMessage = e.getFailedMessage();
			if (this.errorHandler != null) {
				errorHandler.handleMessage(failedMessage);
			} else {
				throw new MessageDeliveryException(message,"failed to send message", e);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	public void setErrorHandler(MessageHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

}
