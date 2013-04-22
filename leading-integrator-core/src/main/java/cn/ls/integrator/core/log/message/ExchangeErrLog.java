package cn.ls.integrator.core.log.message;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

/**
 * 
 * @ClassName: ExchangeErrLog
 * @Description: 错误日志对象
 * @author wanl
 * @date 2011-4-15 上午09:28:12
 * @version V1.0
 */
public class ExchangeErrLog {
	@Id
	public ObjectId id;	
	public String integratorId;
	public String taskName;
	public int errNum ;
	public String errMsg ;
	public Date errDate;
	@Version
	public Long version;
	public Map<String, Object> messageHeaders;//这里有一个拼写错误
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getErrNum() {
		return errNum;
	}
	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public Date getErrDate() {
		return errDate;
	}
	public void setErrDate(Date errDate) {
		this.errDate = errDate;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}

	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public Map<String, Object> getMessageHeaders() {
		return messageHeaders;
	}
	public void setMessageHeaders(Map<String, Object> messageHeaders) {
		this.messageHeaders = messageHeaders;
	}
	public String getIntegratorId() {
		return integratorId;
	}
	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}
}
