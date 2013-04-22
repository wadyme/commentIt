package cn.ls.integrator.core.log.message;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

public class FileState {
	
	@Id
	public ObjectId id;
	
	public String integratorId;
	
	public String groupId;
	
	public String fileGroupId;
	
	public String scheduleName;
	
	public String taskName;
	
	public String businessName;
	
	public long percentage;
	
	public String fileName;
	
	public long lastModified;
	
	public boolean isCompleteExchange;
	
	public String fileDir;
	
	public long totalSize;
	
	private long completeSize;
	
	@Version
	public long version;
	
	public long getPercentage() {
		return percentage;
	}

	public void setPercentage(long percentage) {
		this.percentage = percentage;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getCompleteSize() {
		return completeSize;
	}

	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}

	public String getFileGroupId() {
		return fileGroupId;
	}

	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
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

	public String getIntegratorId() {
		return integratorId;
	}

	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}

}
