package cn.ls.integrator.server.qscanner.utils;

import com.tongtech.org.apache.log4j.Logger;

import cn.ls.integrator.core.file.FileMoniter;
import cn.ls.integrator.core.manager.impl.RecvScheduleManagerImpl;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.task.TaskExecuter;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;


/**
 * 文件监听工具类
 * @author zhaofei
 * @since 2011-6-21
 */
public class TaskForderScannerUtils {

	private static Logger logger = Logger.getLogger(TaskForderScannerUtils.class);
	
	private static TaskForderScannerUtils instance;
	
	private FileMoniter sendTaskScanner ;
	
	private FileMoniter recvTaskScanner ;
	
	private boolean isStarted;
	
	private TaskForderScannerUtils(){}
	
	public static synchronized TaskForderScannerUtils getInstance(){
		if(instance == null){
			instance = new TaskForderScannerUtils();
			instance.isStarted = false;
		}
		return instance;
	}
	/**
	 * 启动对发送任务和接收任务的文件的监听，如果文件被修改清空缓存中的任务。
	 */
	public void start(){
		if(isStarted){
			return;
		}
		isStarted = true;
		try {
			String filePath = FileUtility.getInstance().getSettingFloderPath();
			sendTaskScanner = new FileMoniter(filePath + SystemEnvUtility.pathSeparator + "send", 2000L){
				@Override
				protected void fileChanged(){
					SendScheduleManagerImpl.getInstance().clearTaskTemp();
					TaskExecuter.clearTakMap();
				}
			};
			recvTaskScanner = new FileMoniter(filePath + SystemEnvUtility.pathSeparator + "recv", 2000L){
				@Override
				protected void fileChanged(){
					RecvScheduleManagerImpl.getInstance().clearTaskTemp();
					TaskExecuter.clearTakMap();
				}
			};
		} catch (Exception e) {
			logger.error("启动任务文件目录监听失败", e);
			if(sendTaskScanner != null){
				sendTaskScanner.stop();
			}
			if(recvTaskScanner != null){
				recvTaskScanner.stop();
			}
			isStarted = false;
		}
	}
	/**
	 * 停止对任务文件的监听
	 */
	public void stop(){
		if(!isStarted){
			return;
		}
		if(sendTaskScanner != null){
			sendTaskScanner.stop();
		}
		if(recvTaskScanner != null){
			recvTaskScanner.stop();
		}
		isStarted = false;
	}
}
