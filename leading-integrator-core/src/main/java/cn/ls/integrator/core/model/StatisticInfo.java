package cn.ls.integrator.core.model;

import java.io.Serializable;

public class StatisticInfo implements Serializable{

	private static final long serialVersionUID = -4616113175978523970L;
	
	private Long groupSize = 0L;
	
	private int percentage = 0;

	public Long getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(Long groupSize) {
		this.groupSize = groupSize;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	
	
}
