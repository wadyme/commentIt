package cn.ls.integrator.components.mongo;

import java.net.UnknownHostException;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemConfigUtility;

import com.google.code.morphia.DatastoreImpl;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 操作Mongo数据仓库
 * 
 * 
 * @since 2011-04-01
 * @version 1.0
 * @author wanl
 * 
 */
public class MongoDataStore extends DatastoreImpl {

	private MongoDataStore(Morphia morphia, Mongo mongo) {
		super(morphia, mongo);
	}

	private MongoDataStore(Morphia morphia, Mongo mongo, String dbName,
			String username, char[] password) {
		super(morphia, mongo, dbName, username, password);
	}

	private MongoDataStore(Morphia morphia, Mongo mongo, String dbName) {
		super(morphia, mongo, dbName);
	}

	private static MongoDataStore instance = null;
	
	public static synchronized MongoDataStore getInstance(){
		if(instance == null){
			Morphia morphia = new Morphia();
			String address = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_ADDRESS);
			String portStr = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_PORT);
			String username = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_USERNAME);
			String password = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_PASSWORD);
			String dbName = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_DBNAME);
			String poolSize = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.MONGODB_POOLSIZE);
			int port = Integer.parseInt(portStr);
			Mongo mongo = null;
			try {
				// mongo连接池数量设置
				System.getProperty("MONGO.POOLSIZE" , poolSize) ;
				mongo = new Mongo(address, port);
				
			} catch (UnknownHostException e) {
				throw new IntegratorException("请检查li.properties中您的mongodb部分配置", e);
			} catch (MongoException e) {
				throw new IntegratorException("mongodb连接异常", e);
			}
			
			if(StringUtility.isBlank(username) ){
				instance = new MongoDataStore(morphia, mongo, dbName);
			}else {
				instance = new MongoDataStore(morphia, mongo, dbName, username, password.toCharArray());
			}
		}
		return instance;
	}
}
