/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

/**
 * 配置文件测试类
 * 
 * @author zhoumb 2011-5-2
 */
public class PropertyTest {

	private String fileName = "localinfo.properties";

	@Test
	public void loadTest() {
		try {
			Properties pro = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
			pro.load(in);
			for (String key : pro.stringPropertyNames()) {
				System.out.println(System.getProperty("file.separator"));
				System.out.println("key : " + key);
				System.out.println("value : " + pro.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
