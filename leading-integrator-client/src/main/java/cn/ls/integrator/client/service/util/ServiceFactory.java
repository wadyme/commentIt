package cn.ls.integrator.client.service.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import cn.ls.integrator.client.exception.TiRemoteException;
import cn.ls.integrator.client.service.ScheduleService;
import cn.ls.integrator.client.service.ScheduleServiceService;


public class ServiceFactory {

	private final static Logger logger = Logger.getLogger(ServiceFactory.class.getName());
	private final static int DEFAULT_TIME_OUT = 3000;

	public static ScheduleService getScheduleService(String ip, Integer port,int timeOutMillis) throws IOException{
		if(ip == null || port == null){
			return null;
		}
		URL url = null;
    	String urlStr = "http://" + ip + ":" + port + "/wsc/ScheduleService?wsdl";
        try {
            URL baseUrl;
            baseUrl = ScheduleServiceService.class.getResource(".");
            url = new URL(baseUrl, urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(timeOutMillis);
            con.getResponseCode();
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: '" + urlStr + "', retrying as a local file");
            logger.warning(e.getMessage());
            throw new TiRemoteException("Can not connect the service", e);
        }
        
        QName qname = new QName("http://service.server.integrator.ls.cn/", "ScheduleServiceService");
        ScheduleService service = new ScheduleServiceService(url, qname).getScheduleServicePort();
//        ((BindingProvider)service).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", timeOutMillis);
		((BindingProvider)service).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", timeOutMillis);
        return service;
	}
	public static ScheduleService getScheduleService(String ip, Integer port) throws IOException{
		return getScheduleService(ip, port,DEFAULT_TIME_OUT);
	}
	
	public static Integer connectionLi(String ip, Integer port,int timeOutMillis) throws IOException{
		if(ip == null || port == null){
			return null;
		}
		URL url = null;
    	String urlStr = "http://" + ip + ":" + port + "/wsc/ScheduleService?wsdl";
        try {
            URL baseUrl;
            baseUrl = ScheduleServiceService.class.getResource(".");
            url = new URL(baseUrl, urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(timeOutMillis);
            return con.getResponseCode();
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: '" + urlStr + "', retrying as a local file");
            logger.warning(e.getMessage());
            throw new TiRemoteException("Can not connect the service", e);
        }
	}
}
