/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.chain;

import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;

/**
 * 
 * 
 * @author zhoumb 2011-5-3
 */
public class CustomChannel implements PollableChannel {

	@Override
	public Message<?> receive() {
		System.out.println("receive called on custom channel");
		return null;
	}

	@Override
	public Message<?> receive(long timeout) {
		return this.receive();
	}

	@Override
	public boolean send(Message<?> message) {
		System.out.println("message sent to custom channel: " + message);
		return true;
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		this.send(message);
		return true;
	}

}
