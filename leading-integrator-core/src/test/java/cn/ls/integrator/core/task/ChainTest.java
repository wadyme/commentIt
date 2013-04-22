/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.task;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.handler.MessageHandlerChain;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.core.chain.CustomChannel;
import cn.ls.integrator.core.context.support.StringXmlApplicationContext;
import cn.ls.integrator.core.model.TaskCacheKey;
import cn.ls.integrator.core.model.TaskType;

/**
 * messagechain测试类
 * 
 * @author zhoumb 2011-5-2
 */
public class ChainTest {

	MessageChannel input;

	String path = "test.TestChainTest.xml.task";

	Map<TaskCacheKey, MessageChannel> taskChannelMap = new HashMap<TaskCacheKey, MessageChannel>();

	String inputName = "agrsChannel";

	String fileContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<beans:beans xmlns=\"http://www.springframework.org/schema/integration\"	"
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xmlns:beans=\"http://www.springframework.org/schema/beans\"	"
			+ "xsi:schemaLocation=\"http://www.springframework.org/schema/beans			"
			+ "http://www.springframework.org/schema/beans/spring-beans.xsd			"
			+ "http://www.springframework.org/schema/integration			"
			+ "http://www.springframework.org/schema/integration/spring-integration.xsd\">	"
			+ "<chain input-channel=\"agrsChannel\">" + "<service-activator ref=\"helloworld\" />" + "</chain>"
			+ "<beans:bean id=\"helloworld\" class=\"cn.ls.integrator.core.HelloWorldMessageHandler\" />"
			+ "</beans:beans>";

	private Message<?> getMessage() {
		Map<String, Object> headMessageMap = new HashMap<String, Object>();
		headMessageMap.put("packagesize", 2);
		headMessageMap.put("messagetype", "command");
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
	public void chainTestByName() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
			String[] names = context.getBeanNamesForType(MessageHandlerChain.class);
			for (String chainName : names) {
				MessageHandlerChain chainDefinition = (MessageHandlerChain) context.getBean(chainName);
				chainDefinition.setOutputChannel(new NullChannel());
				Message<?> message = getMessage();
				chainDefinition.handleMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void channelTest() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
			input = (MessageChannel) context.getBean(inputName);
			Message<?> message = getMessage();
			input.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void inputStreamTest() {
		try {
			StringXmlApplicationContext context = new StringXmlApplicationContext(fileContext);
			MessageHandlerChain chainDefinition = context.getBean(MessageHandlerChain.class);
			Message<?> message = getMessage();
			chainDefinition.handleMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void defaultChannelTestWithReplyProducingMessageHandler() {
		try {
			fileContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<beans:beans xmlns=\"http://www.springframework.org/schema/integration\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:beans=\"http://www.springframework.org/schema/beans\" "
					+ "xsi:schemaLocation=\"http://www.springframework.org/schema/beans "
					+ "http://www.springframework.org/schema/beans/spring-beans.xsd "
					+ "http://www.springframework.org/schema/integration "
					+ "http://www.springframework.org/schema/integration/spring-integration.xsd\">"
					+ "<chain input-channel=\"input\">"
					+ "<beans:bean class=\"cn.ls.integrator.core.HelloWordReplyProducingMessageHandler\" />"
					+ "</chain>" + "</beans:beans>";
			StringXmlApplicationContext context = new StringXmlApplicationContext(fileContext);
			MessageHandlerChain chain = context.getBean(MessageHandlerChain.class);
			chain.setOutputChannel(new CustomChannel());
			input = (MessageChannel) context.getBean(inputName);
			Message<?> message = getMessage();
			input.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void defaultChannelTestWithMessageProducerSupport() {
		try {
			fileContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<beans:beans xmlns=\"http://www.springframework.org/schema/integration\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:beans=\"http://www.springframework.org/schema/beans\" "
					+ "xsi:schemaLocation=\"http://www.springframework.org/schema/beans "
					+ "http://www.springframework.org/schema/beans/spring-beans.xsd "
					+ "http://www.springframework.org/schema/integration "
					+ "http://www.springframework.org/schema/integration/spring-integration.xsd\">"
					+ "<chain input-channel=\"agrsChannel\">"
					+ "<beans:bean class=\"cn.ls.integrator.core.HelloWorldMessageHandler\" />"
					// +
					// "<beans:bean class=\"cn.ls.integrator.core.HelloWorldMessageProducerSupport\" />"
					+ "</chain>" + "<beans:bean id=\"customChannel\" class=\"cn.ls.integrator.core.CustomChannel\" />"
					+ "</beans:beans>";
			StringXmlApplicationContext context = new StringXmlApplicationContext(fileContext);
			MessageHandlerChain chain = context.getBean(MessageHandlerChain.class);
			chain.setOutputChannel(new NullChannel());
			input = (MessageChannel) context.getBean(inputName);
			Message<?> message = getMessage();
			input.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void taskMapTest() {
		ClassPathXmlApplicationContext contextHello = new ClassPathXmlApplicationContext("TestChain.xml");
		MessageHandlerChain chainHello = contextHello.getBean(MessageHandlerChain.class);
		chainHello.setOutputChannel(new NullChannel());
		MessageChannel channelHello = (MessageChannel) contextHello.getBean(inputName);
		taskChannelMap.put(new TaskCacheKey("hello", "hello",TaskType.send), channelHello);

		ClassPathXmlApplicationContext contextCustom = new ClassPathXmlApplicationContext("ChannelTest.xml");
		MessageHandlerChain chainCustom = contextCustom.getBean(MessageHandlerChain.class);
		chainCustom.setOutputChannel(new NullChannel());
		MessageChannel channelCustom = (MessageChannel) contextCustom.getBean(inputName);
		taskChannelMap.put(new TaskCacheKey("custom", "custom",TaskType.send), channelCustom);

		MessageChannel testChannel = taskChannelMap.get(new TaskCacheKey("custom", "custom",TaskType.send));
		Message<?> message = getMessage();
		testChannel.send(message);
	}
}
