package cn.ls.integrator.core.utils;

import cn.ls.integrator.core.exception.IntegratorException;
/**
 * 系统环境变量工具类
 */
public class SystemEnvUtility {

	public static final String pathSeparator = System.getProperty("file.separator");
	
	/**
	 * 获得系统环境变量 LI_HOME
	 * @return
	 */
	public static String getLiHomePath() {
		String liHomePath = System.getenv().get("LI_HOME");
		if(liHomePath == null){
			throw new IntegratorException("请配置环境变量 'LI_HOME'");
		} else {
			return liHomePath;
		}
	}
	/**
	 * 获得LI的配置文件路径
	 * @return 
	 */
	public static String getConfigFilePath(){
		return getLiHomePath() + pathSeparator + "conf";
	}
	/**
	 * 获得LI的日志文件路径
	 * @return 
	 */
	public static String getConfigLogPath(){
		return getLiHomePath() + pathSeparator + "logs";
	}
	/**
	 * 获得LI的消息文件路径
	 * @return 
	 */
	public static String getMessagePath(){
		return getLiHomePath() + pathSeparator + "messages";
	}
	public static String getAdapterPath(){
		return getLiHomePath() + pathSeparator + "adapter";
	}
	/**
	 * 获得数据源配置文件路径
	 * @return
	 */
	public static String getDBConfigPath(){
		return getLiHomePath() + pathSeparator + "db" + pathSeparator + "conf";
	}
}
