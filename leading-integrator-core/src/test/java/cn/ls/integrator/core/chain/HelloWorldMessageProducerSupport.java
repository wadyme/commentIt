/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.chain;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

import cn.ls.integrator.core.handler.SafeMessageProducerSupport;

/**
 * 
 * 
 * @author zhoumb 2011-5-3
 */
public class HelloWorldMessageProducerSupport extends SafeMessageProducerSupport implements MessageHandler {

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		System.out.println("HelloWorldMessageProducerSupport");
	}

}
