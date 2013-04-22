package cn.ls.integrator.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.ls.integrator.core.log.LogInitUtils;
import cn.ls.integrator.core.log.mongo.MongoServiceUtils;
import cn.ls.integrator.core.utils.CryptUtil;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemConfigUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;
import cn.ls.integrator.server.utils.IntegratorWinServiceUtils;

public class IntegratorWinService {
	
	private static org.eclipse.jetty.server.Server server;
	
	private static final int DEFAULT_PORT = 8080;
	
	private static final int DEFAULT_SHUTDOWN_PORT = 8702;
	
	private static Logger log;

	public static void start(String[] args0) throws Exception {
		initLog();
		int port = getWebappPort();
		log.fatal("LI编号:" + UniquelyIdentifies.getId());
		try {
			portBindTest(port);
		} catch (Exception e) {
			log.error(port + "端口已被占用", e);
			IntegratorWinServiceUtils.stopIntegratorService();
			return;
		}
		if(isLocalMongoDB()){
			MongoServiceUtils.startMongoDB();
		}
		startShutdownListener(); //启动监听
		
		server = new org.eclipse.jetty.server.Server(port);
		String liHomePath = SystemEnvUtility.getLiHomePath();
		WebAppContext context = new WebAppContext();
		context.setDescriptor(liHomePath + "/server/WebRoot/WEB-INF/web.xml");
		context.setResourceBase(liHomePath + "/server/WebRoot");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		server.setHandler(context);
		server.start();
		server.join();
	}
	
	/**
	 * 获取配置文件中配置的system.webapp.port值 
	 * @return
	 */
	private static int getWebappPort(){
		String portStr = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.SYSTEM_WEBAPP_PORT);
		int port;
		if(StringUtility.isBlank(portStr)){
			port = DEFAULT_PORT;
		} else {
			try {
				port = Integer.parseInt(portStr);
			} catch (NumberFormatException e) {
				log.error("请检查" + SystemConfigUtility.SYSTEM_WEBAPP_PORT + "属性");
				port = DEFAULT_PORT;
			}
		}
		return port;
	}
	
	/**
	 * 获取配置文件中 system.shutdown.port的值
	 * @return
	 */
	private static int getShutdownPort(){
		String portStr = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.SYSTEM_SHUTDOWN_PORT);
		int port;
		if(StringUtility.isBlank(portStr)){
			port = DEFAULT_SHUTDOWN_PORT;
		} else {
			try {
				port = Integer.parseInt(portStr);
			} catch (NumberFormatException e) {
				log.error("请检查" + SystemConfigUtility.SYSTEM_SHUTDOWN_PORT + "属性");
				port = DEFAULT_SHUTDOWN_PORT;
			}
		}
		return port;
	}
	
	/**
	 * 确定是否为本地Mongodb, 默认返回true，当mongodb.isLocal=false时返回false 
	 * @return 
	 */
	private static boolean isLocalMongoDB(){
		String islocal = SystemConfigUtility
				.getSystemConfigProperty(SystemConfigUtility.MONGODB_ISLOCAL);
		if(StringUtility.equalsIgnoreCase(islocal, "false")){
			return false;
		}else {
			return true;
		}
	}

	public static void stop(String[] args0) throws Exception {
		try {
			Socket socket = new Socket("127.0.0.1", getShutdownPort());
			OutputStream out = socket.getOutputStream();
			DataOutputStream dis = new DataOutputStream(out);
			String commond = encodeCommond("stop");
			System.out.println(commond);
			dis.writeUTF(commond);
			dis.close();
			socket.close();
		} catch (ConnectException connExc) {
			System.err.println("服务器连接失败！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void doStop() throws Exception {
		if (server != null) {
			server.stop();
		}
		if(isLocalMongoDB()){
			MongoServiceUtils.stopMongoDB();
		}
		System.exit(0);
	}
	
	private static void portBindTest(int port) throws IOException{
		ServerSocket socket = new ServerSocket(port);
		socket.close();
	}
	
	private static void startShutdownListener() throws Exception{
		new ShutDownPortListener(getShutdownPort()).start();
	}
	
	private static void initLog(){
		LogInitUtils.initLog();
		log = Logger.getLogger(IntegratorAppServer.class);
	}
	
	static class ShutDownPortListener extends Thread{
		private int port;
		
		ShutDownPortListener(int port){
			this.port = port;
		}
		
		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				while(true){
					Socket socket = serverSocket.accept();
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					String commond = dis.readUTF();
					commond = decodeCommond(commond);
					System.out.println(commond);
					if("stop".equalsIgnoreCase(commond)){
						IntegratorWinService.doStop();
						break;
					}
				}
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将命令加密
	 * @param commond
	 * @return
	 */
	private static String encodeCommond(String commond){
		String integratorId = UniquelyIdentifies.getId();
		return CryptUtil.getInstance().encryptAES(commond, integratorId);
	}
	
	/**
	 * 解密命令
	 * @param commond
	 * @return
	 */
	private static String decodeCommond(String commond){
		String integratorId = UniquelyIdentifies.getId();
		return CryptUtil.getInstance().decryptAES(commond, integratorId);
	}

}
