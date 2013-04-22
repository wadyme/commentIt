package cn.ls.integrator.core.file;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FileMoniter {
	
	private Timer timer = new Timer();
	/**
	 * 文件监听的任务
	 */
	private TimerTask task;
	/**
	 * 创建一个文件监听类，根据路径指定监听任务，由Timer按传入的时间间隔执行任务
	 * @param filePath 文件路径
	 * @param period 时间间隔毫秒值
	 */
	public FileMoniter(String filePath, long period ){
		task = new FileMonitorTask(filePath);
		timer.schedule(task, period, period);
	}
	
	/**
	 * 当被监控的目录改变时执行的方法。
	 */
	abstract protected void fileChanged();
	/**
	 * 停止timer的任务
	 */
    public void stop(){
    	timer.cancel();
    }
	/**
	 *监听文件任务具体实现类
	 */
	class FileMonitorTask extends TimerTask {

		long lastChanged; 
		
		String filePath;
		
		FileMonitorTask(String filePath){
			lastChanged = new File(filePath).lastModified();
			this.filePath = filePath;
		}
		
		@Override
		public void run() {
			long lastModified = new File(filePath).lastModified();
			if(lastModified != lastChanged){
				lastChanged = lastModified;
				synchronized (TimerTask.class) {
					fileChanged();
				}
			}
		}
	}
}
