package cn.ls.integrator.server.qscanner.tongq;

import org.apache.commons.lang.builder.HashCodeBuilder;

import cn.ls.integrator.core.qscanner.Queue;
import cn.ls.integrator.core.utils.StringUtility;

public class TongQueue implements Queue {
	
	private String qcuName;
	
	private String queueName;
	
	public TongQueue(){
		
	}
	
	public TongQueue(String qcuName, String queueName) {
		this.qcuName = qcuName;
		this.queueName = queueName;
	}

	public String getQcuName() {
		return qcuName;
	}

	public void setQcuName(String qcuName) {
		this.qcuName = qcuName;
	}	

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj.getClass() != getClass()){
			return false;
		}
		TongQueue queueObj = (TongQueue) obj;
		return StringUtility.equals(qcuName, queueObj.getQcuName())
				&& StringUtility.equals(queueName, queueObj.getQueueName());
		
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return "[qcuName:'" + this.qcuName + "',queueName:'" + queueName + "']";
	}

}
