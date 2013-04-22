package cn.ls.integrator.core.version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;

/**
 * 获得LI的唯一标识。
 * 
 * @author 赵飞
 * 
 */
public class UniquelyIdentifies {

	private static Logger logger = Logger.getLogger(UniquelyIdentifies.class);

	private static String id = null;

	private static final String FILE_NAME = "id.dat";

	public static synchronized String getId() {
		if (id == null) {

			// 从文件中获取
			Properties props = getFromFile(getFullFileName(FILE_NAME));
			String idTemp = null;
			String macTemp = null;
			
			// 如果文件中没有，则生成保存到文件中
			if(props != null){
				idTemp = (String)props.get("id");
				macTemp = (String)props.get("MAC");
			}
			
			List<String> macList = getAllMacAddr();
			if(macList == null || macList.size() == 0){
				throw new IntegratorException("没有获取到本机的MAC地址");
			}
			if(StringUtility.isBlank(idTemp)){
				//生成ID
				idTemp = getUuid();
				macTemp = macList.get(0);
				writeToFile(idTemp, macTemp, getFullFileName(FILE_NAME));
			} else if(StringUtility.isBlank(macTemp)) {
				//生成MAC
				macTemp = macList.get(0);
				writeToFile(idTemp, macTemp, getFullFileName(FILE_NAME));
			} else {
				//检查MAC 如果MAC 不正确 抛出异常
				if(!macList.contains(macTemp)){
					logger.error("请确认LI是否安装成功。如果是以COPY方式安装，请删除" 
							+ FILE_NAME + "文件。如果是网卡有变动，请将" 
							+ FILE_NAME + "文件中的MAC属性修改成一个可用MAC地址");
				}
			}
			id = idTemp;
		}
		return id;
	}
	
	/**
	 * 将id和 mac 写入到文件fileName中
	 * @param id
	 * @param mac
	 * @param fileName
	 */
	private static void writeToFile(String id, String mac, String fileName){
		if(StringUtility.isBlank(id) || StringUtility.isBlank(mac)){
			throw new NullPointerException("id and mac can not be blank");
		}
		File file = new File(fileName);
		FileWriter writer = null;
		Properties props = new Properties();
		props.put("id", id);
		props.put("MAC", mac);
		try {
			writer = new FileWriter(file);
			props.store(writer, "UTF-8");
		} catch (IOException e) {
			logger.error("写入文件" + fileName + "失败");
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 得到文件全名
	 * @param fileName
	 * @return
	 */
	private static String getFullFileName(String fileName) {
		return SystemEnvUtility.getLiHomePath()
				+ SystemEnvUtility.pathSeparator + fileName;
	}

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	private static String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 从文件中获取ID
	 * 
	 * @return
	 */
	private static Properties getFromFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		FileReader reader = null;
		Properties props = new Properties();
		try {
			reader = new FileReader(file);
			props.load(reader);
			return props;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			logger.error("读取文件" + FILE_NAME + "异常", e);
			return null;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("流关闭异常", e);
			}
		}
	}

	
	/**
	 * 获取本机所有网卡Mac地址
	 * @return
	 */
	private static List<String> getAllMacAddr() {
		List<String> list = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			logger.error("获取网卡信息异常", e);
			return new ArrayList<String>();
		}
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface network = (NetworkInterface) netInterfaces.nextElement();
			try {
				if(network.isVirtual()){ //虚拟网卡
					continue;
				}
				byte[] arr = network.getHardwareAddress();
				if(arr == null || arr.length != 6) {
					continue;
				}
				StringBuilder address = new StringBuilder();
				for(byte b : arr){
					String strTemp = Integer.toHexString(b);
					if(strTemp.length() == 0){
						strTemp = "00";
					} else if (strTemp.length() == 1){
						strTemp = "0" + strTemp;
					} 
					address.append(strTemp.substring(strTemp.length()-2)).append("-");
					
				}
				
//				System.out.println(address);
				list.add(address.substring(0, address.length() - 1));
			} catch (SocketException e) {
				logger.error("获取网卡信息异常", e);
			}
		}
		return list;
	}
	
}
