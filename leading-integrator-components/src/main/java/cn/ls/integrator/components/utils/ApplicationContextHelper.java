package cn.ls.integrator.components.utils;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




/**
 * 应用环境工具类
 * @since 2011-03-14
 * @version 1.0
 * @author wanl
 *
 */
public class ApplicationContextHelper {

	//public static  String[] configFiles = {"applicationContext-intergration.xml"/*,"applicationContext-ws.xml"*/};  
	
	private final static String[] configFiles = {"applicationContext-intergration.xml"};
	
	private static ApplicationContextHelper applicationContextHelper= null;
	
	private static AbstractApplicationContext context ;
	private static Logger logger = Logger.getLogger(ApplicationContextHelper.class);
	
	private ApplicationContextHelper(){
		
	}
	
	public synchronized static ApplicationContextHelper getInstance() {
		if (applicationContextHelper == null) {
			applicationContextHelper = new ApplicationContextHelper();
		}
		return applicationContextHelper;
	}

	public static synchronized AbstractApplicationContext getApplicationContext() {
		if (context == null) {
			try {				
				context = new ClassPathXmlApplicationContext(configFiles, ApplicationContextHelper.class);
			}catch(Exception e){
				logger.error("加载Spring配置文件失败",e);
			}
		}
		return context;
	}
	
}
