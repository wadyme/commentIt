package cn.ls.integrator.components;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.file.AFFile;
import cn.ls.integrator.components.file.AFSmbFile;
import cn.ls.integrator.components.file.AbstractFile;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.MD5Encoder;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

public class FileTargetComponent extends SafeReplyProducingMessageHandler
		implements MessageHandler {
	
	private String targetPath;
	/**
	 * 当文件重复时的操作 replace 替换 ，giveup 放弃， addnumber 增加编号后缀
	 */
	private String fileExistOperation;
	/**
	 * 是否将接收的文件放入以节点名为文件夹的目录中
	 */
	private boolean hasNodeNameForder;
	
	@Override
	protected Message<?> handleRequestMessage(Message<?> requestMessage) {
		ThreadUtils.checkThreadInterrupted();
		Map<String, Object> adapterheaders = new HashMap<String, Object>();
		MessageHeaders headers = requestMessage.getHeaders();
		String simpleFileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SIMPLE_FILE_NAME));
		if(StringUtility.isBlank(simpleFileName)){
			throw new IntegratorException("FileTargetComponent组件接收到的消息头simpleFileName属性为空");
		}
		try {
			ThreadUtils.checkThreadInterrupted();
			String tmpFilePath = writeMessageToTempFile(requestMessage);
			if ((Boolean) headers
					.get(IntegratorConstants.MESSAGE_HEADER_IS_COMPLETE)) {
				String targetPath = getTargetDir(headers) + getTargetFileName(headers);
				writeTempToTargetFile(tmpFilePath, targetPath, simpleFileName);
			}
		} catch (MessagingException e) {
			logger.error("组件执行出错",e);
			Message<?> failedMessage = MessageBuilder.withPayload(e).copyHeaders(headers).build();
			adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_ERROR_MESSAGE, failedMessage);
		} catch (Exception e) {
			logger.error("组件执行出错",e);
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE, format.format(date));
		return MessageBuilder.withPayload(requestMessage).copyHeaders(adapterheaders).build();
	}
	
	/**检查该包是否正确
	 * 
	 * @param header
	 * @param file
	 * @return
	 */
	private String isRightMessage(MessageHeaders header, long length){
		long completeSize = CommonHelper.Null2Long(header.get(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES));
		long messageSize = CommonHelper.Null2Long(header.get(IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE));
		if(completeSize - messageSize == length){
			return "true";
		} else if(completeSize == length){
			logger.warn("消息重复");
			return "continue";
		} else {
			return "false";
		}
	}

	/**
	 * 将临时文件写入到目标文件
	 * @param tmpFilePath
	 * @param targetPath
	 * @throws Exception 
	 */
	private void writeTempToTargetFile(String tmpFilePath, String targetPath, String simpleFileName) throws Exception {
		AbstractFile tempfile = getAbstractFile(tmpFilePath);
		String targetPathTemp = targetPath;
		if(StringUtility.isBlank(fileExistOperation) || StringUtility.equalsIgnoreCase(fileExistOperation, "replace")){//替换
			//new File(targetPath).deleteOnExit();
			AbstractFile file = getAbstractFile(targetPath);
			if(file.exists()){
				file.delete();
			}
		} else if(StringUtility.equalsIgnoreCase(fileExistOperation, "giveup")){ //放弃
//			if(tempfile.exists()){
//				tempfile.delete();
//			}
			return;
		} else if(StringUtility.equalsIgnoreCase(fileExistOperation, "addnumber")){ //增加编号做后缀名
			int i = 0;
			while(fileExists(targetPathTemp)){
				targetPathTemp = renameFileNameWithSuffix(targetPath, simpleFileName, i++);
			}
		}
		tempfile.renameTo(getAbstractFile(targetPathTemp));
		
	}
	
	private String renameFileNameWithSuffix(String fileName, String simpleFileName, int number){
		if(!fileName.endsWith(simpleFileName)){
			throw new IntegratorException("文件名无效");
		}
		String filePath = fileName.substring(0, fileName.length() - simpleFileName.length());
		int pointIndex = simpleFileName.lastIndexOf(".");
		String newSimpleFileName = null;;
		if(pointIndex == -1){
			newSimpleFileName = simpleFileName + "_" + number;
		} else if(pointIndex == 0){
			newSimpleFileName = number + simpleFileName;
		} else {
			String temp = simpleFileName.substring(0, pointIndex);
			newSimpleFileName = temp + "_" + number + simpleFileName.substring(pointIndex);
		}
		return filePath + newSimpleFileName;
	}
	
	
	/**
	 * 检查文件是否存在
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	private boolean fileExists(String filePath) throws Exception{
		return getAbstractFile(filePath).exists();
	}

	/**
	 * 获得目标文件路径
	 * @param headers
	 * @return
	 * @throws Exception 
	 */
	private String getTargetDir(MessageHeaders headers) throws Exception {
		Object oldFileName = headers.get(IntegratorConstants.MESSAGE_HEADER_SIMPLE_FILE_NAME);
		String oldLangFileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		String sendNodeName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME));
		StringBuilder dirPathStrBuf = new StringBuilder();
		dirPathStrBuf.append(this.targetPath);
		if(hasNodeNameForder){
			if(StringUtility.isNotBlank(sendNodeName)){
				if(!dirPathStrBuf.toString().endsWith("/")){
					dirPathStrBuf.append("/");
				}
				dirPathStrBuf.append(sendNodeName);
			}
		}
		String absalutePath = oldLangFileName.replace(oldFileName.toString(), "");
		if(!absalutePath.startsWith("/")){
			dirPathStrBuf.append("/");
		}
		dirPathStrBuf.append(absalutePath);
		String targetDirPath = dirPathStrBuf.toString();
		AbstractFile targetPathFile = getAbstractFile(targetDirPath);
		if(!targetPathFile.exists()){
			targetPathFile.mkdirs();
		}
		if(oldFileName.toString().equals(oldLangFileName)){
			targetDirPath = targetDirPath + FileUtility.pathSeparator;
		}
		return targetDirPath;
	}
	
	private AbstractFile getAbstractFile(String filePath) throws Exception{
		if(filePath.startsWith("smb://")){
			return new AFSmbFile(filePath);
		} else if(filePath.startsWith("file:/")){
			return AFFile.fromURL(filePath);
		} else {
			return new AFFile(filePath);
		}
	}
	
	/**
	 * 获得目标文件名
	 * @param fileName
	 * @return
	 */
	private String getTargetFileName(MessageHeaders headers){
		String fileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SIMPLE_FILE_NAME));
		return fileName;
	}
	
	/**
	 * 获得临时文件名
	 * @param headers
	 * @return
	 */
	private String getTempFileName(MessageHeaders headers) {
		String groupId = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_GROUP_ID));
		String scheduleName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME));
		String taskName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME));
		String fileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_SIMPLE_FILE_NAME));
		StringBuilder bf = new StringBuilder("");
		bf.append(scheduleName).append("_").append(taskName).append("_").append(groupId).append("_").append(fileName);
		return MD5Encoder.getMD5(bf.toString().getBytes()) + ".tmp";
	}

	/**
	 * 写入临时文件
	 * @param requestMessage
	 * @return
	 * @throws Exception 
	 */
	private String writeMessageToTempFile(Message<?> requestMessage) throws Exception {
		MessageHeaders headers = requestMessage.getHeaders();
		String fileName = CommonHelper.Null2String(headers.get(IntegratorConstants.MESSAGE_HEADER_FILE_NAME));
		String path = getTargetDir(headers) + getTempFileName(headers);
		AbstractFile file = getAbstractFile(path);
		long fileLength = 0;
		if(file.exists()){
			fileLength = file.length();
		}
		String checkResult = isRightMessage(headers, fileLength);
		if("false".equals(checkResult)){
			throw new MessagingException(fileName + "文件接收到的数据包不正确");
		} else if("true".equals(checkResult)){
			OutputStream fos = null;
			try {
				fos = file.getOutputStream(true);
				fos.write((byte[])requestMessage.getPayload());
				fos.flush();
			} catch (FileNotFoundException e) {
				throw new MessagingException("打开临时文件出错，请检查",e);
			} catch (IOException e) {
				throw new MessagingException("写入临时文件出错",e);
			}finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return path;
	}

	public String getFileExistOperation() {
		return fileExistOperation;
	}

	public void setFileExistOperation(String fileExistOperation) {
		this.fileExistOperation = fileExistOperation;
	}
	
	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public boolean isHasNodeNameForder() {
		return hasNodeNameForder;
	}

	public void setHasNodeNameForder(boolean hasNodeNameForder) {
		this.hasNodeNameForder = hasNodeNameForder;
	}
}
