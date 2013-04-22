package cn.ls.integrator.core.log.message;

import java.util.Map;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

public class ExchangeState {
	@Id
	public ObjectId id;
	
	public String integratorId;
	
	public String exchangeTaskName;
	
	public String scheduleName;
	
	public String scheduleTitle;
	
	public String taskName;
	
	public String taskTitle;
	
	public String sendNodeName;
	
	public String recvNodeName;
	
	public String logType;
	
	public long guoupSize;
	
	public long completeSize;
	
	public int percentage;
	
	public long historyTotal;
	
	public String produceDate;
	
	public String receiveDate;
	
	public String groupId;
	
	public Map<String,Object> messageDetail;
	
	/** 版本 */
	@Version
	public Long version;
	
	public String messageType;
	
	public long fileTotalNumber;
	
	public long fileCompleteNumber;
	
	public String fileName;
	
	public String packageByteSize;

	public String getPackageByteSize() {
		return packageByteSize;
	}

	public void setPackageByteSize(String packageByteSize) {
		this.packageByteSize = packageByteSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getExchangeTaskName() {
		return exchangeTaskName;
	}
	public void setExchangeTaskName(String exchangeTaskName) {
		this.exchangeTaskName = exchangeTaskName;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public Map<String, Object> getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(Map<String, Object> messageDetail) {
		this.messageDetail = messageDetail;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getScheduleTitle() {
		return scheduleTitle;
	}
	public void setScheduleTitle(String scheduleTitle) {
		this.scheduleTitle = scheduleTitle;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
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

	public long getGuoupSize() {
		return guoupSize;
	}

	public void setGuoupSize(long guoupSize) {
		this.guoupSize = guoupSize;
	}

	public long getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}

	public long getHistoryTotal() {
		return historyTotal;
	}

	public void setHistoryTotal(long historyTotal) {
		this.historyTotal = historyTotal;
	}

	public long getFileTotalNumber() {
		return fileTotalNumber;
	}

	public void setFileTotalNumber(long fileTotalNumber) {
		this.fileTotalNumber = fileTotalNumber;
	}

	public long getFileCompleteNumber() {
		return fileCompleteNumber;
	}

	public void setFileCompleteNumber(long fileCompleteNumber) {
		this.fileCompleteNumber = fileCompleteNumber;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getIntegratorId() {
		return integratorId;
	}

	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}

}
