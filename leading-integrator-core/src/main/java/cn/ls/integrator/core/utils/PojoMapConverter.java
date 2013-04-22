package cn.ls.integrator.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream的Converter接口的一个实现 用于将Map对象和List对象以一定的格式写为XML格式的文件
 * 
 * @author liuyf
 * 
 */
public class PojoMapConverter implements Converter {

	public PojoMapConverter() {
		super();
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class clazz) {
		String classname = clazz.getName();
		if (classname.indexOf("Map") >= 0 || classname.indexOf("List") >= 0
				|| classname.indexOf("Bean") >= 0)
			return true;
		else
			return false;
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {

		map2xml(value, writer, context);
	}

	@SuppressWarnings("unchecked")
	protected void map2xml(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		boolean bMap = true;
		String classname = value.getClass().getName();

		bMap = (classname.indexOf("List") < 0);
		Map<Object, Object> map;
		List<Object> list;
		String key;
		Object subvalue;
		if (bMap) {
			map = (Map<Object, Object>) value;
			Iterator<Entry<Object, Object>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Object, Object> entry = (Entry<Object, Object>) iterator
						.next();
				key = String.valueOf(entry.getKey());
				subvalue = entry.getValue();
				writer.startNode(key);
				String className = subvalue.getClass().getName();
				boolean flag = className.indexOf("Map") <= 0
						&& className.indexOf("List") <= 0;
				if (flag) {
					writer.setValue(String.valueOf(subvalue));
				} else {
					map2xml(subvalue, writer, context);
				}
				writer.endNode();
			}

		} else {
			list = (List<Object>) value;
			for (Object subval : list) {
				subvalue = subval;
				writer.startNode("value");
				String className = subvalue.getClass().getName();
				boolean flag = className.indexOf("Map") <= 0
						&& className.indexOf("List") <= 0;
				if (flag) {
					writer.setValue(subvalue.toString());
				} else {
					map2xml(subvalue, writer, context);
				}
				writer.endNode();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Map<String, Object> map = (Map<String, Object>) populateMap(reader,
				context);
		return map;
	}

	protected Object populateMap(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		boolean bMap = true;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String key = reader.getNodeName();
			Object value = null;
			if (reader.hasMoreChildren()) {
				value = populateMap(reader, context);
			} else {
				value = reader.getValue();
			}
			if (bMap) {
				if (map.containsKey(key)) {
					// convert to list
					bMap = false;
					Iterator<Entry<String, Object>> iter = map.entrySet()
							.iterator();
					while (iter.hasNext())
						list.add(iter.next().getValue());
					// insert into list
					list.add(value);
				} else {
					// insert into map
					map.put(key, value);
				}
			} else {
				// insert into list
				list.add(value);
			}
			reader.moveUp();
		}
		if (bMap)
			return map;
		else
			return list;
	}
}
