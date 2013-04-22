package cn.ls.integrator.components;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.log.message.ExchangeErrLog;
import cn.ls.integrator.core.utils.ThreadUtils;
import cn.ls.integrator.core.version.UniquelyIdentifies;

/**
 * 
* @ClassName: Error2MongoComponent 
* @Description: 错误到mongodb
* @author wanl
* @date 2011-5-3 上午12:41:48 
* @version V1.0
 */
public class Error2MongoComponent extends SafeMessageProducerSupport implements MessageHandler {
	
	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();
	
	@Override
	public void handleMessage(Message<?> failedMessage) throws MessagingException {
		try {
			ThreadUtils.checkThreadInterrupted();
			saveErrLog(failedMessage);
		} catch (InterruptedRuntimeException e) {
    		logger.error("线程已停止", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error2MongoComponent is fail !",e);
		}
	}
	
	/**
	 * 
	* @Title: saveErrLog 
	* @Description: 保存错误日志
	* @param message 
	* @return void    返回类型 
	* @throws
	 */
	private void saveErrLog(Message<?> failedMessage) {
		try {
			ThreadUtils.checkThreadInterrupted();
			MessageHeaders messageHeaders = failedMessage.getHeaders();
			Object errObj = failedMessage.getPayload();
			ExchangeErrLog errlog = new ExchangeErrLog();
			Date errDate = new Date();
			errlog.setId(null);
			errlog.setIntegratorId(UniquelyIdentifies.getId());
			errlog.setTaskName(CommonHelper.Null2String(messageHeaders
					.get(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME)));
//			Object recordObj = messageHeaders.get(IntegratorConstants.MESSAGE_HEADER_ERROR_RECORD);
//			if(recordObj != null) {
//				Map<String,Object> record = (Map<String,Object>)recordObj;
//				errlog.setRecord(record);
//			}
			int errNum = -1;
			String errMsg = "";
			errlog.setErrDate(errDate);
			if (errObj instanceof SQLException) {
				SQLException se = (SQLException) errObj;
				errMsg = se.getMessage();
				errNum = se.getErrorCode();
			} else if (errObj instanceof Exception) {
				Exception e = (Exception) errObj;
				if (e.getCause() instanceof SQLException) {
					SQLException se = (SQLException) e.getCause();
					errNum = se.getErrorCode();
				}
				errMsg = e.toString();
			} else {
				errMsg = errObj.toString();
			}
			errlog.setMessageHeaders(failedMessage.getHeaders());
			errlog.setErrNum(errNum);
			errlog.setErrMsg(errMsg);
			mongoDataStore.save(errlog);
		} catch (InterruptedRuntimeException e) {
    		logger.error("线程已停止", e);
			throw e;
		}catch (Exception e) {
			logger.error("保存错误日志错误",e);
		}
		
	}
	
}
