/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.chain;

import org.springframework.integration.Message;
import org.springframework.integration.core.MessageHandler;

import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;

/**
 * 
 * 
 * @author zhoumb 2011-5-3
 */
public class HelloWordReplyProducingMessageHandler extends SafeReplyProducingMessageHandler implements
		MessageHandler {

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		System.out.println("Hello World!");
		return "Hello World!";
	}

}
