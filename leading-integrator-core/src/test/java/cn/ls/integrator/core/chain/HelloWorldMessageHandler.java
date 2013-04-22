/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.chain;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

/**
 * 
 * 
 * @author zhoumb 2011-4-28
 */
public class HelloWorldMessageHandler implements MessageHandler{

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		System.out.println("Hello World !");
	}

}
