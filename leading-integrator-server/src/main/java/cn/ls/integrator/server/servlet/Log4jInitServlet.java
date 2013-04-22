package cn.ls.integrator.server.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import cn.ls.integrator.core.utils.SystemEnvUtility;

public class Log4jInitServlet extends HttpServlet{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8780941597524831975L;
	private final static String configFilename = SystemEnvUtility.getConfigFilePath() + "\\log4j.properties";
	public final String LOG_FILE_PROP="log4j.appender.logfile.File";
	public final String DEFAULT_LOG_NAME="LI_LOG.log";

	public Log4jInitServlet(){		
	}
    public void init(ServletConfig config) throws ServletException { 
    	Properties props = new Properties();  	 
    	InputStream is = null;
    	URL url =null;
    	try {
    	  url = Loader.getResource(configFilename);
    	  //filePath = url.getPath();
    	  is  = new FileInputStream(new File(url.getPath()));
          props.load(is);    
          //===========================================
          String logFile = props.getProperty(LOG_FILE_PROP);
          //if(!StringUtils.hasLength(logFile)){
    	  logFile = SystemEnvUtility.getConfigLogPath() +"\\" + DEFAULT_LOG_NAME ;
    	  //设置路径
          props.setProperty("log4j.appender.logfile.File",logFile);
          //装入log4j配置信息} 
          PropertyConfigurator.configure(props);    
          // mongo连接池数量设置
          System.getProperty( "MONGO.POOLSIZE" , "200" ) ;
              
         // }  
    	} catch (FileNotFoundException e) {
    		System.out.println("Not found configuration file .");
		} catch (IOException e) { 
			System.out.println("Could not read configuration file .");
    	} finally{
    		try {
    			if(is != null){
    				is.close();
    			}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭文件流错误");
			} 

    	}
       
    }
    
    public static void main(String[] args) {
    	//PropertyConfigurator.configure("log4j.properties"); 

		//System.out.println(SystemEnvUtility.getConfigLogPath());
	}
}
