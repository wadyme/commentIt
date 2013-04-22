package cn.ls.integrator.core.log.message;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

/**
 * 
* @ClassName: LastUpdateTime 
* @Description: 最后更新的
* @author wanl
* @date 2011-4-21 上午12:04:37 
* @version V1.0
 */
public class LastUpdateTime{
	@Id
	public ObjectId id;
	
	public String integratorId;
	/** 任务表名 **/
	public String taskName;
	/**最后更新时间*/
	public long lastUpdateTime; 
	
	public long sameNumber;
	@Version
	public long version;

	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public long getSameNumber() {
		return sameNumber;
	}
	public void setSameNumber(long sameNumber) {
		this.sameNumber = sameNumber;
	}
	public String getIntegratorId() {
		return integratorId;
	}
	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}
	 
}
