/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.model.Timer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 文件处理类
 * 
 * @author zhoumb 2011-5-13
 */
public class FileUtility {

	private static Logger logger = Logger.getLogger(FileUtility.class);

	public static final String pathSeparator = System
			.getProperty("file.separator");

	private static FileUtility instance = new FileUtility();

	private static String settingPath;
	private static final String ADAPTER_STRING = "adapter";

	public static synchronized FileUtility getInstance() {
		if (instance == null) {
			instance = new FileUtility();
		}
		return instance;
	}

	/**
	 * 创建一个文件
	 * 
	 * @param context
	 *            文件内容
	 * @param path
	 *            文件路径
	 */
	private void createFileByString(String context, String path) {
		File writefile = null;
		OutputStreamWriter writer = null;
		try {
			writefile = new File(path);
			if (!writefile.exists()) {
				if (writefile.createNewFile()) {
					writefile = new File(path);
				}
			}
			writer = new OutputStreamWriter(new FileOutputStream(writefile), "UTF-8");
			writer.write(context);
			writer.flush();
		} catch (Exception e) {
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
	 * 创建一个任务调度文件
	 * 
	 * @param scheduleInfo
	 *            任务调度对象
	 * @param path
	 *            任务调度文件路径
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void createScheduleFile(Schedule scheduleInfo, String path){
		FileOutputStream fs = null;
		Writer writer = null;
		try {
			XStream xstream = new XStream(new DomDriver());
			xstream.processAnnotations(
					new Class[] { Task.class,Connection.class, Schedule.class, Associate.class, Timer.class });
			String context = xstream.toXML(scheduleInfo);
			context = context.replaceAll("&quot;", "\"");
			fs = new FileOutputStream(path);
			writer = new OutputStreamWriter(fs, "UTF-8");
			try {
				writer.write(context);
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 删除一个文件
	 * 
	 * @param path
	 *            String 文件路径
	 * @return
	 */
	private boolean deleteFile(String path) {
		return new File(path).delete();
	}

	/**
	 * 获得文件内容
	 * 
	 * @param filePath
	 *            String 文件路径
	 * @return String 文件内容
	 */
	private String getFileContext(String filePath) {
		StringBuilder context = new StringBuilder();
		File file = new File(filePath);
		if (file.exists()) {
			InputStreamReader reader = null;
			BufferedReader bufread = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file),
						"UTF-8");
				bufread = new BufferedReader(reader);
				String read = "";
				while ((read = bufread.readLine()) != null) {
					context.append(read).append(" \r\n ");
				}
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (bufread != null) {
					try {
						bufread.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return context.toString();
	}

	/**
	 * 根据任务调度名，生成任务调度文件名(接收任务)
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return 任务调度文件名
	 */
	private String getScheduleFilePath(String scheduleName, TaskType type) {
		return type.getTaskForderPath(getSettingFloderPath()) + pathSeparator
				+ scheduleName + ".schedule";
	}

	/**
	 * 根据任务调度文件对象，解析出任务调度对象
	 * 
	 * @param bFile
	 *            任务调度文件对象
	 * @return 任务调度对象
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private Object getScheduleFromFile(File bFile) {
		Object obj = null;
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(new Class[] { Schedule.class, Connection.class,Task.class,
				 Associate.class, Timer.class });
		FileInputStream in = null;
		try {
			in = new FileInputStream(bFile);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(in, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		obj = xstream.fromXML(reader);
		try {
			if (in != null) {
				in.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 获得调度文件路径
	 * 
	 * @return 得到的路径
	 */
	public String getSettingFloderPath() {
		if (StringUtility.isBlank(settingPath)) {
			String path = null;
			try {
				path = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.SCHEDULES_PATH);
			} catch (IntegratorException e) {
			}
			if (!StringUtility.isBlank(path)) {
				File file = new File(path);
				if (file.exists() && file.isDirectory()) {
					settingPath = path;
				} else {
					logger.info("配置文件 conf/system.properties 中配置的schedulesPath目录不存在");
				}
			}

			if (StringUtility.isBlank(settingPath)) {
				settingPath = SystemEnvUtility.getLiHomePath() + pathSeparator
						+ "schedules";
			}

			File sendFileForder = new File(settingPath + pathSeparator
					+ TaskType.SENDFORDERNAME);
			if (!sendFileForder.exists()) {
				sendFileForder.mkdirs();
			}
			File recvFileForder = new File(settingPath + pathSeparator
					+ TaskType.RECVFORDERNAME);
			if (!recvFileForder.exists()) {
				recvFileForder.mkdirs();
			}
		}
		return settingPath;
	}

	/**
	 * 获得任务文件路径
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @param taskName
	 *            任务名
	 * @param type
	 *            任务类型
	 * @return
	 */
	public String getTaskFilePath(String scheduleName, String taskName, TaskType type) {
		String parentPath = getSettingFloderPath();
		return type.getTaskForderPath(parentPath) + pathSeparator
				+ scheduleName + "." + taskName + ".task";
	}

	/**
	 * 删除任务调度文件(接收任务)<br>
	 * step1: 根据sheduleName生成任务调度文件名<br/>
	 * step2: 删除文件
	 * 
	 * @param scheduleName
	 *            任务调度名
	 * @return
	 */
	public boolean deleteScheduleFile(String scheduleName, TaskType type) {
		String path = getScheduleFilePath(scheduleName, type);
		return deleteFile(path);
	}

	/**
	 * 删除任务文件(发送任务)<br/>
	 * step1: 根据sheduleName和taskName生成任务文件名<br/>
	 * step2: 删除文件
	 * 
	 * @param scheduleName
	 *            String 任务调度名
	 * @param taskName
	 *            String 任务名
	 * @param type
	 *            任务类型
	 * @return
	 */
	public boolean deleteTaskFile(String scheduleName, String taskName, TaskType type) {
		String path = getTaskFilePath(scheduleName, taskName, type);
		return deleteFile(path);
	}

	/**
	 * 得到任务调度列表(接收任务)<br/>
	 * step1: 获得任务调度文件目录<br/>
	 * step2: 获得目录中所有文件<br/>
	 * step3: 遍历文件，获得*.scheudle文件<br/>
	 * step4: 根据文件获得Schedule对象 并组装成List
	 * 
	 * @return List&lt;ScheduleInfo&gt; 任务调度列表
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public List<Schedule> getSchedulesFromFiles(TaskType type) {
		List<Schedule> scheduleInfos = new ArrayList<Schedule>();
		File floader = new File(type.getTaskForderPath(getSettingFloderPath())
				+ pathSeparator);
		File[] files = floader.listFiles();
		if (files != null) {
			for (File bFile : files) {
				if (bFile.getName().endsWith(".schedule")) {
					Schedule schedule = (Schedule) getScheduleFromFile(bFile);
					scheduleInfos.add(schedule);
				}
			}
		}
		return scheduleInfos;
	}

	/**
	 * 获得适配器内容
	 * @param adapterType 适配器类型
	 * @return 适配器内容的String对象
	 */
	public String getAdapterContext(String adapterType) {
		String path = getAdapterFilePath(adapterType);
		return getFileContext(path);
	}
	/**
	 * 获得适配器的路径
	 * @param adapterType 适配器类型
	 * @return
	 */

	private String getAdapterFilePath(String adapterType) {
		StringBuilder path = new StringBuilder(getSettingFloderPath());
		path.append(pathSeparator);
		path.append(ADAPTER_STRING);
		path.append(pathSeparator);
		path.append(adapterType);
		path.append(".");
		path.append(ADAPTER_STRING);
		return path.toString();
	}

	/**
	 * 保存任务调度文件 <br/>
	 * step1:生成任务调度文件名<br/>
	 * step2:调用创建文件的方法，生成文件
	 * 
	 * @param schedule
	 *            ScheduleInfo 任务调度对象
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void saveScheduleFile(Schedule schedule, TaskType type){
		String path = getScheduleFilePath(schedule.getName(), type);
		createScheduleFile(schedule, path);
	}

	/**
	 * 保存任务调度文件 <br/>
	 * step1:生成任务调度文件名 <br/>
	 * step2:调用创建文件的方法，生成文件
	 * 
	 * @param task
	 *            TaskInfo 交换任务
	 * @param scheduleName
	 *            任务调度名
	 * @param taskContext
	 *            任务内容
	 * @param type
	 *            任务类型
	 * @throws FileNotFoundException
	 */
	public String saveTaskFile(Task task, String scheduleName,String taskContext, TaskType type){
		String path = getTaskFilePath(scheduleName, task.getName(), type);
		createFileByString(taskContext, path);
		return path;
	}
}
