package cn.ls.integrator.components;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.backport.java.util.Collections;
import com.tongtech.org.apache.log4j.Logger;

public class DataFileSourceComponent extends SafeMessageProducerSupport implements
		MessageHandler {
	private String sourcePath;
	private String errorPath;
	private int packageLimitSize = 20;
	private int dataCount = 0;
	private static final String ROOT_ELEMENT = "message";
	private static final String HEADERS_ELEMENT = "headers";
	private static final String DATA_ELEMENT = "datas";
	private static final String VALUE_ELEMENT = "value";
	private Logger logger = Logger.getLogger(DataFileSourceComponent.class);
	/**
	 * 是否正在运行
	 */
	private boolean isRuning = false;

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public int getPackageLimitSize() {
		return packageLimitSize;
	}

	public void setPackageLimitSize(int packageLimitSize) {
		this.packageLimitSize = packageLimitSize;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// 如果程序正在运行
		if (isRuning)
			return;
		isRuning = true;
		try {

			putMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRuning = false;
		}

	}

	private void putMessage(Message<?> message) {
		List<File> fileList = getFiles();
		if (fileList == null) {
			throw new IntegratorException("sourcePath参数未配置！！");
		}
		for (File file : fileList) {
			if (!file.exists()) {
				// 记录错误，并继续执行
				logger.error("文件不存在，可能已被删除");
			}
			try {
				ThreadUtils.checkThreadInterrupted();
				sendFileMessage(file, message);
			} catch (InterruptedRuntimeException e) {
				logger.error("线程已停止", e);
				throw e;
			} catch (Exception e) {
				try {
					removeFileToErrorPath(file);
				} catch (Exception e1) {
					logger.error("参数errorPath未配置", e1);
				}
			} finally {
				dataCount = 0;
				file.delete();
			}
		}
	}

	private void removeFileToErrorPath(File file) {
		String fileName = file.getName();
		if (StringUtility.isBlank(errorPath)) {
			throw new IntegratorException("参数errorPath未配置");
		}
		File parent = new File(errorPath);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		FileInputStream fis = null;
		BufferedInputStream reader = null;
		FileOutputStream fos = null;
		PrintStream writer = null;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedInputStream(fis);
			fos = new FileOutputStream(new File(errorPath, fileName));
			writer = new PrintStream(fos);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = reader.read(buff)) != -1) {
				writer.write(buff, 0, len);
			}
			writer.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStream(fis);
			closeStream(fos);
			closeStream(reader);
			closeStream(writer);
		}
	}

	private void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void sendFileMessage(File file, Message<?> message)
			throws Exception {
		Element root = getRoot(file);
		if (!root.getName().equals(ROOT_ELEMENT)) {
			throw new Exception("文件格式不正确！！");
		}
		List<Element> headers = root.elements(HEADERS_ELEMENT);
		List<Element> datas = root.elements(DATA_ELEMENT);
		if (headers.size() != 1 || datas.size() != 1) {
			throw new Exception("文件格式不正确！！");
		}
		Map<String, Object> headersMaps = getHeadersMap(headers.get(0));
		send(headersMaps, datas.get(0), message);
	}

	@SuppressWarnings("unchecked")
	private void send(Map<String, Object> headersMaps, Element datas,
			Message<?> message) throws Exception {
		List<Element> elements = datas.elements();
		int size = elements.size();
		while (dataCount < size) {
			List<Map<Object, Object>> dataMap = new ArrayList<Map<Object, Object>>();
			// 遍历所有的value标签
			for (int i = dataCount; i < elements.size(); i++) {
				Element element = elements.get(i);
				if (!element.getName().equals(VALUE_ELEMENT)) {
					throw new Exception("文件格式不正确！！");
				}
				dataCount++;
				Map<Object, Object> map = new HashMap<Object, Object>();
				// 获得value标签的所有子标签信息，并将其放入消息体中
				List<Element> els = element.elements();
				if (els.size() == 0) {
					throw new Exception("文件格式不正确！！");
				}
				for (Element el : els) {
					map.put(el.getName(), el.getText());
				}
				dataMap.add(map);
				if ((dataCount) % packageLimitSize == 0) {
					break;
				}
			}
			Message<?> adapterMessage = MessageBuilder.withPayload(dataMap)
					.copyHeaders(headersMaps).copyHeaders(message.getHeaders())
					.build();
			sendMessage(adapterMessage);
		}
		dataCount = 0;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getHeadersMap(Element headers) {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		Iterator<Element> it = headers.elementIterator();
		while (it.hasNext()) {
			Element element = it.next();
			headerMap.put(element.getName(), element.getTextTrim());
		}
		return headerMap;
	}

	private Element getRoot(File file) throws DocumentException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			SAXReader reader = new SAXReader();
			Document document = reader.read(fis);
			return document.getRootElement();
		} catch (FileNotFoundException e) {
			System.out.println("未找到文件！！！");
		} finally {
			closeStream(fis);
		}
		return null;
	}

	private List<File> getFiles() {
		List<File> fileList = null;
		if (StringUtility.isNotBlank(sourcePath)) {
			File file = new File(sourcePath);
			if (file.exists()) {
				File[] files = file.listFiles();
				fileList = new ArrayList<File>();
				Collections.addAll(fileList, files);
			}
		}
		return fileList;
	}

}
