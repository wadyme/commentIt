package cn.ls.integrator.core.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;

import cn.ls.integrator.common.IntegratorConstants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * 将Message对象以文件格式保存的工具类
 * @author liuyf
 *
 */
public class MessageToXMLUtil {
	
	private static Logger logger = Logger.getLogger(MessageToXMLUtil.class);
	
	/**
	 * 将Message对象以指定的文件名写入磁盘
	 * @param message 要写入的Message对象
	 * @param path 文件的完整路径名
	 */
	public static void write(Message<?> message, String path) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
			XStream xStream = new XStream(new DomDriver());
			XMLMessage mtx = new XMLMessage();
			mtx.setHeaders(message.getHeaders());
			mtx.setDatas(message.getPayload());
			xStream.registerConverter(new PojoMapConverter());
			xStream.processAnnotations(new Class[] { XMLMessage.class });
			xStream.toXML(mtx, writer);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据消息头生成文件名
	 * @param headers
	 * @return
	 */
	public static String getFileName(MessageHeaders headers) {
		String schedulerName = (String) headers
				.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
		schedulerName = schedulerName == null ? "" : schedulerName;
		String taskName = (String) headers.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
		taskName = taskName == null ? "" : taskName;
		String idString = headers.getId().toString();
		String sendNodeName = (String) headers.get(IntegratorConstants.MESSAGE_HEADER_SEND_NODE_NAME);
		sendNodeName = (sendNodeName == null || "".equals(sendNodeName)) ? "null"
				: sendNodeName;
		String fileName = sendNodeName + "_" + schedulerName + "_" + taskName + "_"
				+ idString + ".message";
		return fileName;
	}
}
/**
 * 包装Message的类
 * 将Message对象包装为XMLMessage对象，以便写入到文件的格式的规范
 * @author liuyf
 *
 */
@XStreamAlias("message")
class XMLMessage {
	@XStreamAlias("headers")
	private Map<String, Object> headers = new HashMap<String, Object>();
	@XStreamAlias("datas")
	private List<Object> datas = new ArrayList<Object>();

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		Iterator<Entry<String, Object>> iterator = headers.entrySet().iterator(); 
		while (iterator.hasNext()) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator
					.next();
			this.headers.put(entry.getKey(), entry.getValue());
		}
	}

	public List<Object> getDatas() {
		return datas;
	}

	@SuppressWarnings("unchecked")
	public void setDatas(Object datas) {
		if (datas.getClass().getName().indexOf("List") > 0) {
			this.datas = (List<Object>) datas;
		} else {
			this.datas.add(datas);
		}
	}
}
