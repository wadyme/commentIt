package cn.ls.integrator.common;

import java.util.HashMap;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TaskUtility {

	private static TaskUtility instance;

	private TaskUtility() {
		super();
	}

	public static synchronized TaskUtility getInstance() {
		if (instance == null) {
			instance = new TaskUtility();
		}
		return instance;
	}

	public String formatParameter(Map<String, String> parametersMap) {
		if (parametersMap == null || parametersMap.size() == 0) {
			return null;
		}
		return new JSONSerializer().deepSerialize(parametersMap);
	}

	public Map<String, String> parseParameter(String parameterString) {
		Map<String, String> parametersMap = null;
		if (parameterString == null || parameterString.trim().equals("")) {
			return new HashMap<String,String>();
		}
		parametersMap = new JSONDeserializer<Map<String, String>>()
				.deserialize(parameterString);
		return parametersMap;
	}

}
