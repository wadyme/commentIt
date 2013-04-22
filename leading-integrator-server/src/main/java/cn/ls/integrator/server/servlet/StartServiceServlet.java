package cn.ls.integrator.server.servlet;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import cn.ls.integrator.common.version.Version;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemConfigUtility;
import cn.ls.integrator.server.qscanner.utils.QueueScannerUtils;
import cn.ls.integrator.server.qscanner.utils.TaskForderScannerUtils;
import cn.ls.integrator.server.service.ScheduleService;

public class StartServiceServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private static final String WEB_SERVICE_DEFAULT_PORT = "11818";
	
	private static Logger logger = Logger.getLogger(StartServiceServlet.class);
	
	private static List<Endpoint> endPointList = new ArrayList<Endpoint>(); 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		//打印LI版本号
		logger.fatal("欢迎使用数据交换应用程序，当前版本号:" + Version.getVersion());
		String port = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.CONSOLE_WEBSERVICE_PORT);
		if(StringUtility.isBlank(port)){
			port = WEB_SERVICE_DEFAULT_PORT;
		}
		//启动任务目录监听
		TaskForderScannerUtils.getInstance().start();
		try {
			//启动接收监听
			QueueScannerUtils.startScan();
		} catch (Exception e) {
			logger.error("启动队列监听失败",e);
		}
		//启动所有发送任务调度
		startService();
		//启动WebService
		startWebService(port);
	}
	
	@Override
	public void destroy() {
		//停止所有任务
		for(Endpoint endpoint : endPointList){
			if(endpoint.isPublished()) {
				endpoint.stop();
			}
		}
		
		//停止所有队列监听
		try {
			//停止接收监听
			QueueScannerUtils.stopScan();
		} catch (Exception e) {
			logger.error("停止队列监听失败",e);
		}
	}
	
	/**
	 * 获得本地IP地址列表
	 * @return
	 */
	private static List<String> getAllLocalIP() {
		List<String> list = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			logger.error("获取网络接口失败", e);
			return list;
		}
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface network = (NetworkInterface) netInterfaces.nextElement();
			Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
			while(inetAddresses.hasMoreElements()){
				InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
				if (!inetAddress.isLoopbackAddress()
						&& inetAddress.getHostAddress().indexOf(":") == -1) {
					list.add(inetAddress.getHostAddress());
				}
			}
		}
		return list;
	}
	
	/**
	 * 启动Schedule
	 */
	private void startService(){
		ScheduleService service = new ScheduleService();
		try {
			service.startAllSendSchedule();
		} catch (Exception e) {
			logger.error("启动发送业务失败",e);
		}
	}
	
	/**
	 * 启动WebService
	 * @param port
	 */
	private void startWebService(String port){
		List<String> ipList = getAllLocalIP();
		if(ipList != null && ipList.size() > 0){
			for(String ipStr : ipList){
				String url="http://" + ipStr + ":"+ port +"/wsc/ScheduleService";
				try {
					logger.info("-----------------------" + url);
					Endpoint endpoint = Endpoint.publish(url,new ScheduleService());
					endPointList.add(endpoint);
				} catch (Exception e) {
					logger.error("启动WebService失败",e);
				}
			}
		}
	}
}
