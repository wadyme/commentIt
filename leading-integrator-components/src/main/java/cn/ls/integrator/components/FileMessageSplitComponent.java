package cn.ls.integrator.components;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.file.AbstractFile;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.log.message.FileState;
import cn.ls.integrator.core.utils.ThreadUtils;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class FileMessageSplitComponent extends SafeMessageProducerSupport implements MessageHandler{
	private static Logger logger = Logger.getLogger(FileMessageSplitComponent.class);
	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();
	private int packageByteSize;
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		MessageHeaders headers = message.getHeaders();
		Map<String, Object> newHeaders = new HashMap<String, Object>();
		Object messagePayload = message.getPayload();
		if(!(messagePayload instanceof AbstractFile)){
			
			throw new MessagingException(message, "FileMessageSplitCompontent 组件只能接受文件类型的消息");
		}
		AbstractFile payloadFile = (AbstractFile) messagePayload;
		int packageByteSize = getPackageByteSize();
		long fileCompleteBytes = (Long)headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES);
		long length = (Long)headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES);
		long completeSize = CommonHelper.Null2Long(headers.get(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE));
		InputStream input = null;
		try {
			input = payloadFile.getInputStream();
			// 去掉已发送完成的部分
			input.skip(fileCompleteBytes); 
			int times = (int)((length - fileCompleteBytes) / packageByteSize);
			times = ((length - fileCompleteBytes) % packageByteSize) == 0 ? times : times + 1;
			long lastTimeComplete = fileCompleteBytes;
			for(int j = 0 ; j < times  ; j++){
				ThreadUtils.checkThreadInterrupted();
				byte[] tmp = null;
				boolean newIsComplete = false;
				int newMessageSize = 0;
				long newFileCompleteSize = 0;
				long percentage = 0;
				if(j != times -1){
					tmp = new byte[packageByteSize];
					input.read(tmp);
					newMessageSize = packageByteSize;
					newIsComplete = false;
					newFileCompleteSize = lastTimeComplete + packageByteSize;
					percentage = (long)(lastTimeComplete + packageByteSize) * 100/length ;
				} else {
					newMessageSize = (int) (length - lastTimeComplete);
					tmp = new byte[newMessageSize];
					input.read(tmp);
					newIsComplete = true;
					newFileCompleteSize = length;
					percentage = 100L;
				}
				newHeaders.put(IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE, newMessageSize);
				newHeaders.put(IntegratorConstants.MESSAGE_HEADER_IS_COMPLETE, newIsComplete);
				newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES, newFileCompleteSize);
				
				newHeaders.put(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE, completeSize + newFileCompleteSize);
				newHeaders.put(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE, percentage);
				Message<?> adapterMessage = MessageBuilder.withPayload(tmp).copyHeaders(headers)
						.copyHeaders(newHeaders).build();
				ThreadUtils.checkThreadInterrupted();
				sendMessage(adapterMessage);
				updateFileState(adapterMessage.getHeaders());
				lastTimeComplete += newMessageSize;
			}
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程中断", e);
			throw e;	
		} catch (FileNotFoundException e) {
			String exceptionMessage = null;
			try {
				String abstractUrl = payloadFile.getAbsoluteURL();
				exceptionMessage = "FileMessageSplitComponent Exception : 文件" + abstractUrl + "不存在";
				logger.error(exceptionMessage, e);
			} catch (Exception e1) {
				exceptionMessage = "读取文件路径异常 ";
				logger.error(exceptionMessage, e1);
			}
			throw new MessagingException(message, exceptionMessage);
		} catch (IOException e) {
			String exceptionMessage = null;
			try {
				String abstractUrl = payloadFile.getAbsoluteURL();
				exceptionMessage = "FileMessageSplitComponent Exception : 读文件" + abstractUrl + "异常";
				logger.error(exceptionMessage, e);
			} catch (Exception e1) {
				exceptionMessage = "读取文件路径异常 ";
				logger.error(exceptionMessage, e1);
			}
			throw new MessagingException(message, exceptionMessage);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e){
			logger.error(e);
		}finally {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
		
	}
	
	private void updateFileState( Map<String, Object> headers) {
		FileState newFileState = new FileState();
		newFileState.setIntegratorId(UniquelyIdentifies.getId());
		String fileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		String taskName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME));
		long lastModified = CommonHelper.Null2Long(headers.get(IntegratorConstants.MESSAGE_HEADER_LAST_MODIFIED));
		newFileState.setScheduleName(CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME)));
		newFileState.setTaskName(CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME)));
		newFileState.setCompleteExchange(CommonHelper.Null2Boolean(headers.get(IntegratorConstants.MESSAGE_HEADER_IS_COMPLETE)));
		newFileState.setCompleteSize(CommonHelper.Null2Long(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES)));
		newFileState.setPercentage(CommonHelper.Null2Long(headers.get(IntegratorConstants.MESSAGE_HEADER_PERCENTAGE)));
		newFileState.setGroupId(CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_GROUP_ID)));
		newFileState.setFileGroupId(CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_GROUP_ID)));
		newFileState.setFileDir(CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_SOURCE_PATH)));
		newFileState.setTotalSize(CommonHelper.Null2Long(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES)));
		newFileState.setFileName(fileName);
		newFileState.setLastModified(lastModified);
		newFileState.setBusinessName(taskName);
		Query<FileState> query = mongoDataStore.createQuery(FileState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME, taskName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_FILE_NAME, fileName);
		query.filter(IntegratorConstants.MESSAGE_HEADER_LAST_MODIFIED, lastModified);
		mongoDataStore.updateFirst(query, newFileState, true);
	}

	public int getPackageByteSize() {
		return packageByteSize;
	}

	public void setPackageByteSize(int packageByteSize) {
		this.packageByteSize = packageByteSize;
	}

}
