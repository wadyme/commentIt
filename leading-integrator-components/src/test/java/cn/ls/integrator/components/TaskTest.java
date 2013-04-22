/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.components;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.handler.MessageHandlerChain;
import org.springframework.integration.support.MessageBuilder;

/**
 * 
 * 
 * @author zhoumb 2011-4-28
 */
public class TaskTest {

	private MessageChannel input;

	private String path = "TestFilter.xml";

	private Message<?> getMessage() {
		Map<String, Object> headMessageMap = new HashMap<String, Object>();
		headMessageMap.put("packagesize", 2);
		headMessageMap.put("messagetype", "command");
		headMessageMap.put("replyChannel", new NullChannel());
		Map<Object, Object> bodyMessageMap = new HashMap<Object, Object>();
		return MessageBuilder.withPayload(bodyMessageMap).copyHeaders(headMessageMap).build();
	}

	@Test
	public void chainTest() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
			MessageHandlerChain chainDefinition = context.getBean(MessageHandlerChain.class);
			Message<?> message = getMessage();
			chainDefinition.handleMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void channelTest() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
			input = (MessageChannel) context.getBean("input");
			Message<?> message = getMessage();
			input.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
