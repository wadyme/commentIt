/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.context.support;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 * 
 * 
 * @author zhoumb 2011-5-1
 */
public class StringXmlApplicationContext extends AbstractXmlApplicationContext {

	private Logger logger = Logger.getLogger(StringXmlApplicationContext.class);
	private Resource[] contextResources;

	public StringXmlApplicationContext(String context) {
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(context.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(null, e);
		}
		this.contextResources = new ByteArrayResource[] { resource };
		refresh();
	}

	@Override
	protected Resource[] getConfigResources() {
		return this.contextResources;
	}
	
}
