package cn.ls.integrator.core.log.message;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

public class FileExchangeLog {
	
	@Id
	public ObjectId id;
	
	public String integratorId;
	
	public String groupId;
	
	public String fileGroupId;
	
	public String sendNodeName;
	
	public String recvNodeName;
	
	public String logType;
	
	public String scheduleName;
	
	public String taskName;
	
	public long percentage;
	
	public String fileName;
	
	public long lastModified;
	
	public boolean isCompleteExchange;
	
	public String recieveDate;
	
	public String produceDate;
	
	public String fileDir;
	
	public long totalSize;
	
	public long completeSize;
	
	@Version
	public long version;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFileGroupId() {
		return fileGroupId;
	}

	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	public String getSendNodeName() {
		return sendNodeName;
	}

	public void setSendNodeName(String sendNodeName) {
		this.sendNodeName = sendNodeName;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
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

	public long getPercentage() {
		return percentage;
	}

	public void setPercentage(long percentage) {
		this.percentage = percentage;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isCompleteExchange() {
		return isCompleteExchange;
	}

	public void setCompleteExchange(boolean isCompleteExchange) {
		this.isCompleteExchange = isCompleteExchange;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getRecvNodeName() {
		return recvNodeName;
	}

	public void setRecvNodeName(String recvNodeName) {
		this.recvNodeName = recvNodeName;
	}

	public String getRecieveDate() {
		return recieveDate;
	}

	public void setRecieveDate(String recieveDate) {
		this.recieveDate = recieveDate;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getIntegratorId() {
		return integratorId;
	}

	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}
	
}
