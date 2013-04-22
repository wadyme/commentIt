package cn.ls.integrator.core.log.mongo;

import java.io.File;
import java.io.IOException;

import cn.ls.integrator.core.utils.SystemEnvUtility;

/**
 * MongoDB服务控制
 * @author zhaofei
 *
 */
public class MongoServiceUtils {
	
	private static final String startMongoCommand =  "net start MongoDB";
	private static final String stopMongoCommand = "net stop MongoDB";
	private static final String mongoLockFile = SystemEnvUtility.getLiHomePath() 
			+ SystemEnvUtility.pathSeparator + "data" + SystemEnvUtility.pathSeparator + "mongod.lock";
	public static void startMongoDB() throws IOException {
		stopMongoDB();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		new File(mongoLockFile).delete();
		execuCommand(startMongoCommand);
	}
	
	public static void stopMongoDB() throws IOException{
		execuCommand(stopMongoCommand);
	}
	
	private static void execuCommand(String command) throws IOException{
		Runtime.getRuntime().exec(command);
	}
}
