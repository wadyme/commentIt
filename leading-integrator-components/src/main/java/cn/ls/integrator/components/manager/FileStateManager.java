package cn.ls.integrator.components.manager;

public interface FileStateManager {
	/**
	 * 删除文件交换记录
	 * @param scheduleName 任务调度名
	 * @param taskName 任务名
	 */
	public void delete(String scheduleName, String taskName);
}
