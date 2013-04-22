package cn.ls.integrator.server.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.xml.ws.Endpoint;

import cn.ls.integrator.core.file.FileMoniter;
import cn.ls.integrator.core.manager.impl.RecvScheduleManagerImpl;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.task.TaskExecuter;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;

public class Server {

	public static void main(String[] args) {
	    Server server = new Server();
	    String port = "11818";
		//server.startService();
		String filePath = FileUtility.getInstance().getSettingFloderPath();
		new FileMoniter(filePath + SystemEnvUtility.pathSeparator + "send", 5000L){
			@Override
			protected void fileChanged(){
				SendScheduleManagerImpl.getInstance().clearTaskTemp();
				TaskExecuter.clearTakMap();
			}
		};
		new FileMoniter(filePath + SystemEnvUtility.pathSeparator + "recv", 5000L){
			@Override
			protected void fileChanged(){
				RecvScheduleManagerImpl.getInstance().clearTaskTemp();
				TaskExecuter.clearTakMap();
			}
		};
		server.startWebService(port);
		
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
			e.printStackTrace();
			return new ArrayList<String>();
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
	@SuppressWarnings("unused")
	private void startService(){
		ScheduleService service = new ScheduleService();
		try {
			service.startAllSendSchedule();
		} catch (Exception e1) {
			e1.printStackTrace();
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
					new StartServiceThread(url).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
		}
	}
	
	static class StartServiceThread extends Thread{
		private String url;
		public StartServiceThread(String url) {
			this.url = url;
		}
		@Override
		public void run() {
			Endpoint.publish(url,new ScheduleService());
		}
	}

}
