package cn.ls.integrator.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.ls.integrator.common.TaskUtility;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.manager.ScheduleManager;
import cn.ls.integrator.core.manager.impl.RecvScheduleManagerImpl;
import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;

public class TaskParameterUtility {

	private static Logger logger = Logger.getLogger(TaskParameterUtility.class);

	private static final String REPLACE_START = "$${";

	private static final String REPLACE_END = "}";
	private static final String PARAMETER_VALUES_SPLIT = ":";
	// 可配置参数的标识
	private static final String VARIABLE = "variable";
	// 不可配置参数的标识
	private static final String INVARIABLE = "invariable";
	// 公共参数标识
	private static final String DB = "db";

	// 不可配置参数文件的根标签
	private static final String ROOT_ELEMENT = "params";

	private static TaskParameterUtility instance;
	// 可配置参数及参数描述
	private Map<String, Map<String, String>> parametersMap;
	// 固定的参数及参数描述
	private Map<String, Map<String, String>> invariableParametersMap;
	// 可配置参数值
	private Map<String, Map<String, String>> parameterValuesMap;
	// 固定的参数值
	private Map<String, Map<String, String>> invariableParameterValuesMap;
	// DB参数及描述
	private Map<String, Map<String, String>> DBParametersMap;
	// DB参数的值
	private Map<String, Map<String, String>> DBParametersValuesMap;
	// 公共参数及描述
	private Map<String, Map<String, String>> publicParametersMap;
	// 公共参数的值
	private Map<String, Map<String, String>> publicParametersValuesMap;
	//数据库配置文件
//	private Map<String, Map<String, String>> dataBaseConfMap;
	private FileUtility fileUtility = FileUtility.getInstance();

	private TaskParameterUtility() {
	}

	public static synchronized TaskParameterUtility getInstance() {
		if (instance == null) {
			instance = new TaskParameterUtility();
		}
		return instance;
	}
	
//	private static ConnectionManager getConnectionManager(){
//		return ConnectionManagerImpl.getInstance();
//	}

	private String getParameterKey(String scheduleName, String taskName,
			TaskType type) {
		StringBuilder str = new StringBuilder();
		str.append(scheduleName);
		str.append(".");
		str.append(taskName);
		str.append(".");
		str.append(type);
		return str.toString();
	}

	private String getParameterKeyWithoutTask(String scheduleName, TaskType type) {
		StringBuilder str = new StringBuilder();
		str.append(scheduleName);
		str.append(".");
		str.append(type);
		return str.toString();
	}
	
	public Map<String, String> getParameters(String scheduleName, Task task,
			TaskType type) {
		if (parametersMap == null) {
			parametersMap = new HashMap<String, Map<String, String>>();
		}
		String parameterKey = getParameterKey(scheduleName, task.getName(),type);
		Map<String, String> parameters = parametersMap.get(parameterKey);
		if (parameters == null) {
			putParametersInMap(scheduleName, task.getName(), type);
		}
		return parametersMap.get(parameterKey);
	}

	public Map<String, String> getInvariableParameters(String scheduleName,
			Task task, TaskType type) {
		if (invariableParametersMap == null) {
			invariableParametersMap = new HashMap<String, Map<String, String>>();
		}
		String parameterKey = getParameterKey(scheduleName, task.getName(),
				type);
		Map<String, String> parameters = invariableParametersMap
				.get(parameterKey);
		if (parameters == null) {
			putParametersInMap(scheduleName, task.getName(), type);
		}
		return invariableParametersMap.get(parameterKey);
	}

	private void putParametersInMap(String scheduleName, String taskName,
			TaskType type) {
		if (invariableParametersMap == null) {
			invariableParametersMap = new HashMap<String, Map<String, String>>();
		}
		int start = 0;
		String adapterType = getTaskAdapterType(scheduleName, taskName, type);
		String modelString = fileUtility.getAdapterContext(adapterType);
		if (StringUtility.isBlank(modelString)) {
			throw new IntegratorException("不存在名为" + adapterType + "的模板文件");
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> invariableParameters = new HashMap<String, String>();
		Map<String, String> DBParameters = new HashMap<String, String>();
		if (DBParametersMap == null) {
			DBParametersMap = new HashMap<String, Map<String, String>>();
		}
		while (true) {
			int subFromIndex = modelString.indexOf(REPLACE_START, start);
			int subEndIndex = modelString.indexOf(REPLACE_END, subFromIndex);
			if (subFromIndex >= 0 && subEndIndex > 0) {
				String[] strArray = modelString.substring(
						subFromIndex + REPLACE_START.length(), subEndIndex)
						.split(PARAMETER_VALUES_SPLIT);
				if (StringUtility.equals(strArray[2], VARIABLE)) {
					parameters.put(strArray[0], strArray[1]);
				} else if (StringUtility.equals(strArray[2], INVARIABLE)) {
					invariableParameters.put(strArray[0], strArray[1]);
				} else if (DBParametersMap.size() == 0
						&& StringUtility.equals(strArray[2], DB)) {
					DBParameters.put(strArray[0], strArray[1]);
				}
				start = subEndIndex;
			} else {
				break;
			}
		}
		String key = getParameterKeyWithoutTask(scheduleName, type);
		String parkey = getParameterKey(scheduleName, taskName, type);
		if (parameters.size() > 0) {
			parametersMap.put(parkey, parameters);
		}
		if (invariableParameters.size() > 0) {
			invariableParametersMap.put(parkey, invariableParameters);
		}
		if (DBParameters.size() > 0) {
			DBParametersMap.put(key, DBParameters);
		}
	}

	public Map<String, String> getPublicParameters(String scheduleName,
			TaskType type) {
		if (publicParametersMap == null) {
			publicParametersMap = new HashMap<String, Map<String, String>>();
		}
		String parameterKey = getParameterKeyWithoutTask(scheduleName, type);
		Map<String, String> parameters = publicParametersMap.get(parameterKey);
		if (parameters == null) {
			putParametersInMap(scheduleName, type);
		}
		return publicParametersMap.get(parameterKey);
	}

	public Map<String, String> getDBParameters(String scheduleName,
			TaskType type) {
		if (DBParametersMap == null) {
			DBParametersMap = new HashMap<String, Map<String, String>>();
		}
		String parameterKey = getParameterKeyWithoutTask(scheduleName, type);
		Map<String, String> parameters = DBParametersMap.get(parameterKey);
		if (parameters == null) {
			putParametersInMap(scheduleName, type);
		}
		return DBParametersMap.get(parameterKey);
	}

	private void putParametersInMap(String scheduleName, TaskType type) {
		Schedule schedule = getSchedule(scheduleName, type);
		String values = schedule.getParameters();
		
		Map<String, String> parameterValues = TaskUtility.getInstance()
				.parseParameter(values);

		int start = 0;
		String modelString = fileUtility.getAdapterContext(schedule
				.getAdapterType());
		if (StringUtility.isBlank(modelString)) {
			throw new IntegratorException("不存在名为" + schedule.getAdapterType()
					+ "的模板文件");
		}
		Map<String, String> DBParameters = new HashMap<String, String>();
		Map<String, String> parameters = new HashMap<String, String>();	
		if (DBParametersMap == null) {
			DBParametersMap = new HashMap<String, Map<String, String>>();
		}
		if(publicParametersMap==null){
			publicParametersMap=new HashMap<String, Map<String, String>>();
		}
		
		String key = getParameterKeyWithoutTask(scheduleName, type);
		while (true) {
			int subFromIndex = modelString.indexOf(REPLACE_START, start);
			int subEndIndex = modelString.indexOf(REPLACE_END, subFromIndex);
			if (subFromIndex >= 0 && subEndIndex > 0) {
				String[] strArray = modelString.substring(
						subFromIndex + REPLACE_START.length(), subEndIndex)
						.split(PARAMETER_VALUES_SPLIT);
				if (DBParametersMap.get(key)==null && StringUtility.equals(strArray[2], DB)) {
					DBParameters.put(strArray[0], strArray[1]);
				}else if(publicParametersMap.get(key)==null && StringUtility.equals(strArray[2], VARIABLE)){
					parameters.put(strArray[0], strArray[1]);
				}
				start = subEndIndex;
			} else {
				break;
			}
		}
		if (DBParameters.size() > 0) {
			DBParametersMap.put(key, DBParameters);
		}
		if (parameters.size() > 0) {
			Set<String> keySet = (Set<String>)parameterValues.keySet();
			Map<String, String> params = new HashMap<String, String>();	
			for(String str: keySet){
				params.put(str, parameters.get(str));
			}
			publicParametersMap.put(key, params);
		}
	}

	public Map<String, String> getParameterValues(String scheduleName,
			String taskName, TaskType type) {
		if (parameterValuesMap == null) {
			parameterValuesMap = new HashMap<String, Map<String, String>>();
		}
		Task task = getTask(scheduleName, taskName, type);
		if (task == null) {
			throw new IntegratorException("任务" + taskName + "不存在");
		}
		String parameterKey = getParameterKey(scheduleName, taskName, type);
		Map<String, String> parameterValues = parameterValuesMap.get(parameterKey);
		if (parameterValues == null || parameterValues.size() == 0) {
			String values = task.getParameters();
			parameterValues=new HashMap<String, String>();
			parameterValues.putAll(getPublicParametersValues(scheduleName, type));
			parameterValues.putAll(TaskUtility.getInstance().parseParameter(values));
			parameterValuesMap.put(parameterKey, parameterValues);
		}
		return parameterValuesMap.get(parameterKey);
	}
	
	public Map<String, String> getDBParameterValues(String scheduleName,
			TaskType type) {
		String key = getParameterKeyWithoutTask(scheduleName, type);
		if(DBParametersValuesMap == null){
			DBParametersValuesMap=new HashMap<String, Map<String,String>>();
		}
		Map<String, String> DBparameterValues = DBParametersValuesMap.get(key);
		if(DBparameterValues == null){
			DBparameterValues = new HashMap<String, String>();
		}
		if (DBparameterValues.size() == 0) {
			Schedule schedule = getSchedule(scheduleName, type);
			Connection con = schedule.getConnection();
			if(con == null){
				DBparameterValues = null;
			}else{
				DBparameterValues.put("driver", con.getDriver());
				DBparameterValues.put("url", con.getUrl());
				DBparameterValues.put("username", con.getUsername());
				DBparameterValues.put("password", con.getPassword());
				DBparameterValues.put("dbtype", con.getDbtype().toUpperCase());
			}
			DBParametersValuesMap.put(key, DBparameterValues);
		}
		return DBParametersValuesMap.get(key);
	}

	public Map<String, String> getPublicParametersValues(String scheduleName,
			TaskType type) {
		Schedule schedule = getSchedule(scheduleName, type);
		String key = getParameterKeyWithoutTask(scheduleName, type);
		String parameters = schedule.getParameters();
		if(publicParametersValuesMap ==null ){
			publicParametersValuesMap=new HashMap<String, Map<String,String>>();
		}
		Map<String, String> publicParameterValues = publicParametersValuesMap.get(key);
		if (publicParameterValues == null || publicParameterValues.size() == 0) {
			publicParameterValues = TaskUtility.getInstance().parseParameter(parameters);
			publicParametersValuesMap.put(key, publicParameterValues);
		}
		return publicParametersValuesMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getInvariableParametersValue(
			String scheduleName, String taskName, TaskType type) {
		String parameterKey = getParameterKey(scheduleName, taskName, type);
		if (invariableParameterValuesMap == null) {
			invariableParameterValuesMap = new HashMap<String, Map<String, String>>();
		}
		Map<String, String> map = invariableParameterValuesMap
				.get(parameterKey);
		if (map == null || map.size() == 0) {
			map = new HashMap<String, String>();
			String path = fileUtility.getTaskFilePath(scheduleName, taskName,
					type);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(path);
				SAXReader reader = new SAXReader();
				Document document = reader.read(fis);
				Element root = document.getRootElement();
				if (!root.getName().equals(ROOT_ELEMENT)) {
					throw new IntegratorException(taskName + "文件的格式不正确");
				}
				List<Element> elements = root.elements();
				for (Element element : elements) {
					map.put(element.getName(), element.getText());
				}
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
				throw new IntegratorException("任务调度" + scheduleName + "下的"
						+ taskName + "对应的配置文件不存在");
			} catch (DocumentException e) {
				logger.error(e.getMessage(), e);
			}
			if (map.size() > 0) {
				invariableParameterValuesMap.put(parameterKey, map);
			}
		}
		return map;
	}

	private Task getTask(String scheduleName, String taskName, TaskType type) {
		ScheduleManager scheduleManager = null;
		if (TaskType.send.equals(type)) {
			scheduleManager = SendScheduleManagerImpl.getInstance();
		} else if (TaskType.recv.equals(type)) {
			scheduleManager = RecvScheduleManagerImpl.getInstance();
		}
		Schedule schedule = scheduleManager.getSchedule(scheduleName);
		scheduleManager.assertScheduleIsNotNull(schedule, scheduleName);
		Task task = scheduleManager.getTask(schedule, taskName);
		return task;
	}

	
	private Schedule getSchedule(String scheduleName, TaskType type) {
		ScheduleManager scheduleManager = null;
		if (TaskType.send.equals(type)) {
			scheduleManager = SendScheduleManagerImpl.getInstance();
		} else if (TaskType.recv.equals(type)) {
			scheduleManager = RecvScheduleManagerImpl.getInstance();
		}
		Schedule schedule = scheduleManager.getSchedule(scheduleName);
		scheduleManager.assertScheduleIsNotNull(schedule, scheduleName);
		return schedule;
	}

	public void assertParameters(String scheduleName, Task task, TaskType type) {
		Map<String, String> parameters = getParameters(scheduleName, task, type);
		Map<String, String> parameterValues = getParameterValues(scheduleName,
				task.getName(), type);
		if (parameterValues == null
				|| parameterValues.size() != parameters.size()) {
			throw new IntegratorException("任务" + task.getName()
					+ "对应的可配置参数值不存在或模板类型配置错误，请配置");
		}
		Map<String, String> invariableParameters = getInvariableParameters(
				scheduleName, task, type);
		Map<String, String> invariableParameterValues = getInvariableParametersValue(
				scheduleName, task.getName(), type);
		if (invariableParameterValues == null
				|| invariableParameterValues.size() != invariableParameters
						.size()) {
			throw new IntegratorException("任务" + task.getName()
					+ "对应的参数文件不存在或参数配置缺失，请配置");
		}
	}
	
	private String getTaskAdapterType(String scheduleName, String taskName, TaskType type){
		Schedule schedule = getSchedule(scheduleName, type);
		Task task = getTask(scheduleName, taskName, type);
		String adapterType = null;
		if(StringUtility.isNotBlank(task.getAdapterType())){
			adapterType = task.getAdapterType();
		} else if(StringUtility.isNotBlank(schedule.getAdapterType())){
			adapterType = schedule.getAdapterType();
		} else {
			throw new IntegratorException("schedule:" + scheduleName + ",task:" + taskName + "的adapterType不可为空");
		}
		return adapterType;
	}

	public String getTaskContextString(String scheduleName, String taskName,
			TaskType type) {
		Task task = getTask(scheduleName, taskName, type);
		String adapterType = getTaskAdapterType(scheduleName, taskName, type);
		StringBuilder modelString = new StringBuilder(fileUtility.getAdapterContext(adapterType));
		Map<String, String> parameterValues = getParameterValues(scheduleName,taskName, type);
		Map<String, String> invariableParametersValues = getInvariableParametersValue(scheduleName, taskName, type);
		Map<String, String> DBParametersValues = getDBParameterValues(scheduleName, type);
		
		assertParameters(scheduleName, task, type);
		try {
//			System.out.println(modelString);
			modelString = replaceFromValues(modelString, parameterValues);
			modelString = replaceFromValues(modelString, invariableParametersValues);
			modelString = replaceFromValues(modelString, DBParametersValues);
			/*modelString = replaceFromValues(modelString,getParameters(scheduleName, task, type));*/
		} catch (Exception e) {
			throw new IntegratorException("任务调度" + scheduleName + "下的"
					+ taskName + "参数配置文件错误，请检查");
		}
		return modelString.toString();
	}

	private StringBuilder replaceFromValues(StringBuilder modelString,
			Map<String, String> map) {
		if(map == null){
			return modelString;
		}
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String tmp = REPLACE_START + entry.getKey();
			while (true) {
				int index = modelString.indexOf(tmp);
				if (index < 0) {
					break;
				}
				int toIndex = modelString.indexOf(REPLACE_END, index) + 1;
				String value = entry.getValue();
				if (value != null) {
					modelString.replace(index, toIndex, value);
				} else {
					throw new IntegratorException();
				}
			}
		}
		return modelString;
	}
	
	/**
	 * 去掉相同的参数。 当value中对应的 <K,V>值在baseValue中存在就删除。
	 * @param baseValue
	 * @param value
	 * @return 去重之后的结果
	 */
	public String removeSameParam(String baseValue, String value){
		Map<String,String> baseMap = TaskUtility.getInstance().parseParameter(baseValue);
		Map<String,String> map = TaskUtility.getInstance().parseParameter(value);
		Set<Entry<String, String>> baseSet = baseMap.entrySet();
		Set<Entry<String, String>> set = map.entrySet();
		for(Entry<String, String> entry : baseSet){
			if(set.contains(entry)){
				map.remove(entry.getKey());
			}
		}
		return TaskUtility.getInstance().formatParameter(map);
	}
	
	public void clearParametersMap() {
		this.parametersMap = null;
		this.parameterValuesMap = null;
		this.invariableParametersMap = null;
		this.invariableParameterValuesMap = null;
		this.DBParametersMap = null;
		this.DBParametersValuesMap = null;
		this.publicParametersMap=null;
		this.publicParametersValuesMap=null;
	}

}
