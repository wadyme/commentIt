package cn.ls.integrator.components.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;


import cn.ls.integrator.components.metadata.DataSourceMetadata;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.ScheduleManager;
import cn.ls.integrator.core.manager.impl.ScheduleManagerImpl;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.utils.SystemEnvUtility;

/**
 * 缓存DataSource的池
 * 
 * @author 赵飞
 */
public class DataSourcePools {

	private static Map<DataSourceMetadata, DataSource> pool = new HashMap<DataSourceMetadata, DataSource>();

	private static Map<String, DataSourceMetadata> metaDataPool = new HashMap<String, DataSourceMetadata>();
	
	private static final String DATA_SOURCE_FILE_NAME = "dataSource.properties";

	private static final String PRO_SPLIT_STR = ",";
	
	public static synchronized DataSource getDataSource(DataSourceMetadata metadata){
		if(metadata == null){
			throw new IntegratorException("数据源为空，请检查数据源配置文件");
		}
		DataSource dataSource = pool.get(metadata);
		if(dataSource != null){
			return dataSource;
		} else {
			try {
				dataSource = BasicDataSourceFactory.createDataSource(metadata.getProperties());
			} catch (Exception e) {
				throw new IntegratorException("请检查数据源配置文件", e);
			}
			pool.put(metadata, dataSource);
		}
		return dataSource;
	}
	
	public static DataSourceMetadata initDataSourceMetadata(String scheduleName, String sourceName, TaskType taskType){
		String key = scheduleName + "." + taskType + "." + sourceName;
		DataSourceMetadata metaData = metaDataPool.get(key);
		if(metaData != null){
			return metaData;
		}
		metaData = getMetaDataByConnection(scheduleName,taskType);
		if(metaData == null){
			metaData = getMetaDataFromPro(sourceName);
		}
		metaData.setDefaultAutoCommit("true");
		metaData.setMaxActive("20");
		metaData.setMaxWait("1000");
		metaData.setPoolPreparedStatements("false");
		metaDataPool.put(key, metaData);
		return metaData;
	}

	private static DataSourceMetadata getMetaDataByConnection(String scheduleName,TaskType taskType) {
		ScheduleManager scheduleManager = ScheduleManagerImpl.getInstance(taskType);
		Schedule schedule = scheduleManager.getSchedule(scheduleName);
		if(schedule == null){
			return null;
		}
		Connection connection = schedule.getConnection();
		DataSourceMetadata metaData = null;
		if(connection != null){
			metaData =  new DataSourceMetadata();
			metaData.setDbType(connection.getDbtype());
			metaData.setUrl(connection.getUrl());
			metaData.setDriverClassName(connection.getDriver());
			metaData.setUsername(connection.getUsername());
			metaData.setPassword(connection.getPassword());
		}
		return metaData;
	}

	/**
	 * 从dataSource.properties文件中读取数据源相关信息
	 * @param queueName
	 * @return
	 */
	private static DataSourceMetadata getMetaDataFromPro(String sourceName) {
		
		String metaString = getMetaString(sourceName);
		DataSourceMetadata metaData = null;
		if(metaString != null){
			metaString = metaString.replaceAll(" +", "");
			String[] arr = metaString.split(PRO_SPLIT_STR);
			if(arr.length != 5){
				throw new IntegratorException("的数据源配置文件" + DATA_SOURCE_FILE_NAME + "中" + 
						sourceName + "相应的参数配置有误");
			}
			metaData = new DataSourceMetadata();
			metaData.setDbType(arr[0]);
			metaData.setUrl(arr[1]);
			metaData.setDriverClassName(arr[2]);
			metaData.setUsername(arr[3]);
			metaData.setPassword(arr[4]);
		}
		return metaData;
	}

	private static String getMetaString(String sourceName) {
		String filePath = SystemEnvUtility.getDBConfigPath() + SystemEnvUtility.pathSeparator + DATA_SOURCE_FILE_NAME;
		File file = new File(filePath);
		String metaString = null;
		FileInputStream fis = null;
		if(file.exists()){
			try {
				Properties pro = new Properties();
				fis = new FileInputStream(file);
				pro.load(fis);
				metaString = pro.getProperty(sourceName);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return metaString;
	}
}
