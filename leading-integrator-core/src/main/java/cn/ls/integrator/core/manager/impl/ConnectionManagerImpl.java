package cn.ls.integrator.core.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.ConnectionManager;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.ConnectionType;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;

public class ConnectionManagerImpl implements ConnectionManager {

	private static Logger logger = Logger.getLogger(ConnectionManagerImpl.class);
	
	private static List<ConnectionType> connectionTypeList = null;

	private static final String CONN_CONFIG_FORDER_NAME = "db"
			+ SystemEnvUtility.pathSeparator + "conf";
	
	private static final String CONN_CONFIG_FILE_TYPE = ".conf";
	
	private static final String CONN_CONFIG_NAME = "name";

	private static final String CONN_CONFIG_DRIVER = "driver";
	
	private static final String CONN_CONFIG_URL = "url";
	
	private ConnectionManagerImpl() {
	}
	private static ConnectionManagerImpl instance;
	public static synchronized ConnectionManagerImpl getInstance(){
		if(instance == null){
			instance = new ConnectionManagerImpl();
		}
		return instance;
	}
	
	@Override
	public synchronized List<ConnectionType> getConnectionTypeList() {
		if (connectionTypeList == null) {
			connectionTypeList = new ArrayList<ConnectionType>();
			String configForderPath = SystemEnvUtility.getLiHomePath()
					+ SystemEnvUtility.pathSeparator + CONN_CONFIG_FORDER_NAME;
			File configForder = new File(configForderPath);
			File[] configFiles = configForder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(CONN_CONFIG_FILE_TYPE);
				}
			});
			for(File configFile : configFiles){
				ConnectionType connectionType = new ConnectionType();
				InputStream in = null;
				Properties properties = new Properties();
				try {
					in = new FileInputStream(configFile);
					properties.load(in);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
				String name = (String)properties.get(CONN_CONFIG_NAME);
				String driver = (String)properties.get(CONN_CONFIG_DRIVER);
				String url = (String)properties.get(CONN_CONFIG_URL);
				if(StringUtility.isBlank(name) || StringUtility.isBlank(driver) || StringUtility.isBlank(url)){
					logger.error("请检查文件" + configFile.getAbsolutePath());
					continue;
				}
				connectionType.setName(name);
				connectionType.setDriver(driver);
				connectionType.setUrl(url);
				connectionTypeList.add(connectionType);
			}
		}
		return connectionTypeList;
	}
	
	@Override
	public boolean testConnect(Connection connection) {
		if(connection == null){
			return false;
		}
		java.sql.Connection conn = null;
		Statement stmt = null;
		try {
			Properties properties = new Properties();
			properties.put("driverClassName", connection.getDriver());
			properties.put("url", connection.getUrl());
			properties.put("username", connection.getUsername());
			properties.put("password", connection.getPassword());
			conn = BasicDataSourceFactory
						.createDataSource(properties).getConnection();
			stmt = conn.createStatement();
		} catch (Exception e) {
			throw new IntegratorException(e.getMessage());
		} finally {
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}
	
}
