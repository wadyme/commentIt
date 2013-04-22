/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.file;

import org.junit.Test;

/**
 * 文件路径测试类
 * 
 * @author zhoumb 2011-5-21
 */
public class FilePathTest {

	String path;

	@Test
	public void evnPathTest() {
		String name = "LI_HOME";
		path = System.getenv(name);
		System.out.println(path);
	}

	@Test
	public void jarPathTest() {
		String strTarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		System.out.println(strTarPath.substring(strTarPath.indexOf("/") + 1, strTarPath.length()));
		path = strTarPath;
	}

}
