package cn.ls.integrator.core.log.message;

import java.util.Map;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Version;

/**
 * @ClassName: MessageLog
 * @Description: 发送日志对象
 * @author wanl
 * @date 2011-4-15 上午09:39:03
 * @version V1.0
 */

public class MessageLog {
	@Id
	public ObjectId id;
	
	public String integratorId;
	/** 任务名 **/
	public String exchangeTaskName;
	/**
	 * 日志内容 
	 */
	public Map<String, Object> messageHerders;
	/** 版本 */
	@Version
	public Long version;
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}

	public Map<String, Object> getMessageHeaders() {
		return messageHerders;
	}
	public void setMessageHeaders(Map<String, Object> messageHerders) {
		this.messageHerders = messageHerders;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getExchangeTaskName() {
		return exchangeTaskName;
	}
	public void setExchangeTaskName(String exchangeTaskName) {
		this.exchangeTaskName = exchangeTaskName;
	}
	public String getIntegratorId() {
		return integratorId;
	}
	public void setIntegratorId(String integratorId) {
		this.integratorId = integratorId;
	}
	



}
