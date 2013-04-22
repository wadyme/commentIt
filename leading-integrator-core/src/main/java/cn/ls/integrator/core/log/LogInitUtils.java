package cn.ls.integrator.core.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import cn.ls.integrator.core.utils.SystemEnvUtility;

public class LogInitUtils {
	private final static String configFilename = SystemEnvUtility.getConfigFilePath()+ "/log4j.xml";
	public static void initLog() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(new File(configFilename));
			props.load(is);
			
			// 系统环境变量的装入
			System.setProperty("LI_HOME", SystemEnvUtility.getLiHomePath());
			
			System.setProperty("START_TIME", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			// mongo连接池数量设置
			System.setProperty("MONGO.POOLSIZE", "200");
			
			// 装入log4j配置信息}
			DOMConfigurator.configureAndWatch(configFilename);

		} catch (FileNotFoundException e) {
			System.out.println("Not found configuration file .");
		} catch (IOException e) {
			System.out.println("Could not read configuration file .");
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭文件流错误");
			}
		}
	}
}
