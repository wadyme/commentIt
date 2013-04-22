package cn.ls.integrator.core.file;

import cn.ls.integrator.core.file.FileMoniter;
import cn.ls.integrator.core.manager.impl.RecvScheduleManagerImpl;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.task.TaskExecuter;
import cn.ls.integrator.core.utils.SystemEnvUtility;

public class TestFileChange {

	public static void main(String[] args) {
		fileChange();
	}
	
	public static void fileChange(){
		String filePath = SystemEnvUtility.getMessagePath();
		new FileMoniter(filePath, 1000L){
			@Override
			protected void fileChanged(){
				SendScheduleManagerImpl.getInstance().clearTaskTemp();
				RecvScheduleManagerImpl.getInstance().clearTaskTemp();
				TaskExecuter.clearTakMap();
			}
		};
	}
}
