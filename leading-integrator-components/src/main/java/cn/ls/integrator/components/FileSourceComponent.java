package cn.ls.integrator.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.file.AFFile;
import cn.ls.integrator.components.file.AFSmbFile;
import cn.ls.integrator.components.file.AbstractFile;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.log.message.FileState;
import cn.ls.integrator.core.model.MessageType;
import cn.ls.integrator.core.utils.FileUtility;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemEnvUtility;
import cn.ls.integrator.core.utils.ThreadUtils;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class FileSourceComponent extends SafeMessageProducerSupport implements
		MessageHandler {
	protected final Logger log = Logger.getLogger(FileSourceComponent.class);
	private String sourcePath;
	// 是否删除源文件
	private boolean deleteSourceFile = false;
	private boolean isRunning;
	// 是否备份源文件
	private boolean backup;
	// 是否迭代交换子目录
	private boolean iterator;
	// 备份目录
	private String backDir;

	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if (isRunning)
			return;
		isRunning = true;
		try {
			ThreadUtils.checkThreadInterrupted();
			putMessage(message);
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			log.error("线程中断", e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("file源组件错误异常", e);
		} finally {
			isRunning = false;
		}

	}

	protected void putMessage(Message<?> message) throws Exception {
		MessageHeaders headers = message.getHeaders();
		String businessName = headers
				.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME)
				+ "."
				+ headers.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
		if (StringUtility.isBlank(this.sourcePath)) {
			throw new IntegratorException("名为" + businessName
					+ "的任务文件配置的sourcePath为空");
		}
		AbstractFile sourceDir = getAbstractFile(this.sourcePath);
		if (!sourceDir.exists()) {
			throw new IntegratorException("任务" + businessName
					+ "配置的sourcePath‘" + this.sourcePath + "’ 不存在");
		} else {
			Map<String, AbstractFile> fileMap = new HashMap<String, AbstractFile>();
			getFileMap(this.sourcePath, fileMap);
			fileMap = filterFileByComplete(fileMap, businessName);
			Iterator<Entry<String, AbstractFile>> fileIterator = fileMap
					.entrySet().iterator();
			String groupId = UUID.randomUUID().toString();
			long fileCompleteNumber = 0;
			long completeSize = 0;
			long groupSize = getGroupSize(fileMap.values());
			while (fileIterator.hasNext()) {
				String fileGroupId = UUID.randomUUID().toString();
				Entry<String, AbstractFile> entry = fileIterator.next();
				AbstractFile file = entry.getValue();
				file = checkFileReady(file);
				if (file == null) {
					return;
				}
				String fileName = file.getName();
				FileState fes = getFileState(file, fileName, businessName);
				long fileCompleteSize = 0;
				if (fes != null) {
					fileCompleteSize = fes.getCompleteSize();
					fileGroupId = fes.getFileGroupId();
				}
				if (file.length() > 0 && file.length() > fileCompleteSize) {
					Map<String, Object> newHeaders = getFileMessage(
							businessName, fileMap.size(), groupId, fileGroupId,
							fileName, file, fileCompleteSize,
							fileCompleteNumber++, completeSize, groupSize);
					Message<?> sendMessage = MessageBuilder.withPayload(file)
							.copyHeaders(headers).copyHeaders(newHeaders)
							.build();
					completeSize += file.length();
					this.sendMessage(sendMessage);
					if (backup) {
						backUp(file);
					}
					if (deleteSourceFile) {
						file.delete();
					}
				}
			}
		}
	}

	private void backUp(AbstractFile file) throws Exception {
		String newFileName = getbackupFileName(file.getName());
		InputStream fis = file.getInputStream();
		FileOutputStream fos = new FileOutputStream(newFileName);
		byte[] buff = new byte[1024];
		int i;
		while ((i = fis.read(buff)) != -1) {
			fos.write(buff, 0, i);
		}
		fos.flush();
		fos.close();
		fis.close();

	}

	private String getbackupFileName(String name) {
		String path = getBackDir();
		if (StringUtility.isBlank(path)) {
			path = SystemEnvUtility.getLiHomePath() + FileUtility.pathSeparator
					+ "file" + FileUtility.pathSeparator + "backup";
		}
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		String fileName = path + FileUtility.pathSeparator + name;
		if (new File(fileName).exists()) {
			int index = name.lastIndexOf(".");
			fileName = path + FileUtility.pathSeparator
					+ name.substring(0, index) + "_"
					+ new SimpleDateFormat("yyyyMMddHHmmSS").format(new Date())
					+ name.substring(index);
		}
		return fileName;
	}

	private AbstractFile checkFileReady(AbstractFile file) throws Exception {
		AbstractFile inteFile = null;
		String url = file.getAbsoluteURL();
		String endTag = ".li";
		if (url.endsWith(endTag)) {
			inteFile = getAbstractFile(url.replace(endTag, ""));
			file.renameTo(inteFile);
		} else {
			String urlWithEndTag = url + endTag;
			file.renameTo(getAbstractFile(urlWithEndTag));
			try {
				inteFile = getAbstractFile(url);
				getAbstractFile(urlWithEndTag).renameTo(inteFile);
			} catch (Exception e) {
				logger.warn(file.getName() + " is unwritable.. :", e);
			}
		}
		return inteFile;
	}

	/**
	 * 获取 files 中所有文件的总长度
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	private long getGroupSize(Collection<AbstractFile> files) throws Exception {
		long fileTotalSize = 0;
		for (AbstractFile file : files) {
			fileTotalSize += file.length();
		}
		return fileTotalSize;
	}

	private Map<String, Object> getFileMessage(String businessName,
			long fileSize, String groupId, String fileGroupId, String fileName,
			AbstractFile file, long fileCompleteSize, long fileCompleteNumber,
			long completeSize, long groupSize) throws Exception {
		Map<String, Object> newHeaders = new HashMap<String, Object>();
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_NAME, fileName);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_LAST_MODIFIED,
				file.lastModified());
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_SIMPLE_FILE_NAME,
				file.getName());
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID, groupId);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_GROUP_ID,
				fileGroupId);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_NUMBER,
				fileCompleteNumber);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_NUMBER,
				fileSize);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME,
				businessName);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE,
				MessageType.fileMessage);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_TOTAL_BYTES,
				file.length());
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_COMPLETE_BYTES,
				fileCompleteSize);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_COMPLETE_SIZE,
				completeSize);
		newHeaders
				.put(IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, groupSize);
		newHeaders.put(IntegratorConstants.MESSAGE_HEADER_FILE_SOURCE_PATH,
				sourcePath);
		return newHeaders;
	}

	/**
	 * 过滤已经完成的文件
	 * 
	 * @param fileMap
	 * @param businessName
	 * @return
	 * @throws Exception
	 */
	private Map<String, AbstractFile> filterFileByComplete(
			Map<String, AbstractFile> fileMap, String businessName)
			throws Exception {
		Map<String, AbstractFile> resultFileMap = new HashMap<String, AbstractFile>();
		Set<Entry<String, AbstractFile>> fileMapSet = fileMap.entrySet();
		for (Entry<String, AbstractFile> entry : fileMapSet) {
			AbstractFile file = entry.getValue();
			String key = entry.getKey();
			FileState fileState = getFileState(file, key, businessName);
			if (fileState == null || !fileState.isCompleteExchange) {
				resultFileMap.put(key, file);
			}
		}
		return resultFileMap;
	}

	private void getFileMap(String filePath, Map<String, AbstractFile> fileMap)
			throws Exception {
		if (StringUtility.isBlank(filePath)) {
			throw new IntegratorException("给定的路径不能为空");
		}
		AbstractFile parentFile = getAbstractFile(filePath);
		if (parentFile.isFile()) {
			throw new IntegratorException("给定的路径不是文件夹路径");
		}
		AbstractFile[] files = parentFile.listFiles();
		for (AbstractFile file : files) {
			if (file.isDirectory()) {
				if (iterator) {
					getFileMap(file.getAbsoluteURL(), fileMap);
				}
			} else {
				fileMap.put(
						filePath.replace(this.sourcePath, "") + file.getName(),
						file);
			}
		}
	}

	private AbstractFile getAbstractFile(String filePath) throws Exception {
		if (filePath.startsWith("smb://")) {
			return new AFSmbFile(filePath);
		} else if (filePath.startsWith("file:/")) {
			return AFFile.fromURL(filePath);
		} else {
			return new AFFile(filePath);
		}
	}

	private FileState getFileState(AbstractFile file, String fileName,
			String businessName) throws Exception {
		Query<FileState> query = mongoDataStore.createQuery(FileState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId")
						.equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal(""));
		query.filter("fileName", fileName);
		query.filter("lastModified", file.lastModified());
		query.filter("businessName", businessName);
		List<FileState> fesList = query.asList();
		if (fesList.size() == 0) {
			return null;
		} else {
			return fesList.get(0);
		}
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public boolean isDeleteSourceFile() {
		return deleteSourceFile;
	}

	public void setDeleteSourceFile(boolean deleteSourceFile) {
		this.deleteSourceFile = deleteSourceFile;
	}

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public boolean isIterator() {
		return iterator;
	}

	public void setIterator(boolean iterator) {
		this.iterator = iterator;
	}

	public String getBackDir() {
		return backDir;
	}

	public void setBackDir(String backDir) {
		this.backDir = backDir;
	}
}
