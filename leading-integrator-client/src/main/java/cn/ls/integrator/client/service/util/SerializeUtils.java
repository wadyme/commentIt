package cn.ls.integrator.client.service.util;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class SerializeUtils {

	@SuppressWarnings("unchecked")
	public static Object deserializer(String jsonStr){
		return new JSONDeserializer().deserialize(jsonStr);
	}
	
	public static String serializer(Object obj){
		return new JSONSerializer().deepSerialize(obj);
	}
}
