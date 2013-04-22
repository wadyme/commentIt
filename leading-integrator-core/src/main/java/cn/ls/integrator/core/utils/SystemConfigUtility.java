package cn.ls.integrator.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;

/**
 * 系统配置读取工具类<br/>
 * 路径  LI_HOME/conf/system.properties
 * @author zhaofei
 *
 */
public class SystemConfigUtility {

	private static Logger logger = Logger.getLogger(SystemConfigUtility.class) ;
	
	/**配置文件名**/
	private static final String CONFIG_FILE_NAME = "li.properties";
	
	/**任务文件路径**/
	public static final String SCHEDULES_PATH = "schedules.path";
	
	/**无法解析消息志存放的路径**/
	public static final String MESSAGE_UNKNOWN_PATH = "message.unknown.path";
	
	/**WebService端口**/
	public static final String CONSOLE_WEBSERVICE_PORT = "console.webservice.port";
	
	public static final String QSCANNER_VENDEROR = "qscanner.venderor";
	
	/**MongoDB 是否为本地**/
	public static final String MONGODB_ISLOCAL = "mongodb.islocal";
	
	/**MongoDB IP**/
	public static final String MONGODB_ADDRESS =  "mongodb.address";
	
	/**MongoDB 端口**/
	public static final String MONGODB_PORT = "mongodb.port";
	
	/**MongoDB 用户名**/
	public static final String MONGODB_USERNAME = "mongodb.username";
	
	/**MongoDB 密码**/
	public static final String MONGODB_PASSWORD = "mongodb.password";
	
	/**MongoDB 数据库名**/
	public static final String MONGODB_DBNAME = "mongodb.dbname";
	
	/**MongoDB 连接池数量**/
	public static final String MONGODB_POOLSIZE = "mongodb.poolsize";
	
	/**文件适配器存放的路径**/
	public static final String FILE_ADAPTER_PATH = "adapter.file.path";
	
	/**web容器使用的端口**/
	public static final String SYSTEM_WEBAPP_PORT = "system.webapp.port";
	
	public static final String SYSTEM_SHUTDOWN_PORT = "system.shutdown.port";
	
	/**Demp的webservice服务地址**/
	public static final String THIRD_PARTY_RECEIVE_NOTIFICATION_DEMP_URL = "thirdParty.receive.notification.demp.url";
	
	/**第三方的webservice服务地址**/
	public static final String THIRD_PARTY_RECEIVE_NOTIFICATION_URL = "thirdParty.receive.notification.url";
	
	
	private static Properties systemConfigProperties;
	
	private static synchronized Properties getSystemConfigProperties(){
		InputStream inputStream = null;
		if(systemConfigProperties == null){
			try {
				String configFilePath = SystemEnvUtility.getConfigFilePath() 
							+ SystemEnvUtility.pathSeparator 
							+ CONFIG_FILE_NAME;
				inputStream = new FileInputStream(new File(configFilePath));
				systemConfigProperties = new Properties();
				systemConfigProperties.load(inputStream);
			} catch (FileNotFoundException e) {
				logger.error("读取系统配置文件发生异常", e);
			} catch (IOException e) {
				logger.error("读取系统配置文件发生异常", e);
			} finally {
				if(inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return systemConfigProperties;
	}
	/**
	 * 根据Properties的键propertyName获得值
	 * @param propertyName 
	 * @return 
	 */
	public static String getSystemConfigProperty(String propertyName){
		Properties props = getSystemConfigProperties();
		if(props == null || props.isEmpty()){
			throw new IntegratorException("读取系统配置文件异常，请检查日志");
		}
		return props.getProperty(propertyName);
	}
}
