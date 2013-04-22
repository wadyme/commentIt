/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.handler;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageDeliveryException;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.history.MessageHistory;
import org.springframework.integration.history.TrackableComponent;
import org.springframework.integration.message.ErrorMessage;

/**
 * A support class for producer endpoints that provides a setter for the output
 * channel and a convenience method for sending Messages.<br>
 * copy from
 * {@link org.springframework.integration.endpoint.MessageProducerSupport} <br>
 * and use {@link org.springframework.integration.channel.NullChannel} as
 * default output channel
 * 
 * @author zhoumb 2011-5-3
 */
public abstract class SafeMessageProducerSupport extends AbstractEndpoint implements MessageProducer,
		TrackableComponent {
	
	protected Logger logger = Logger.getLogger(SafeMessageProducerSupport.class);
	
	private String adapterType ;

	public void setAdapterType(String adapaterType) {
		this.adapterType = adapaterType;
	}
	
	public String getAdapterType() {
		return adapterType;
	}
	
	private volatile MessageChannel outputChannel;

	private volatile MessageChannel errorChannel;

	private volatile boolean shouldTrack = false;

	private final MessagingTemplate messagingTemplate = new MessagingTemplate();

	public void setOutputChannel(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}

	public void setErrorChannel(MessageChannel errorChannel) {
		this.errorChannel = errorChannel;
	}

	public void setSendTimeout(long sendTimeout) {
		this.messagingTemplate.setSendTimeout(sendTimeout);
	}

	public void setShouldTrack(boolean shouldTrack) {
		this.shouldTrack = shouldTrack;
	}

	@Override
	protected void onInit() {
		// set default output channel
		if (this.outputChannel == null) {
			setOutputChannel(new NullChannel());
		}
	}

	/**
	 * Takes no action by default. Subclasses may override this if they need
	 * lifecycle-managed behavior.
	 */
	@Override
	protected void doStart() {
	}

	/**
	 * Takes no action by default. Subclasses may override this if they need
	 * lifecycle-managed behavior.
	 */
	@Override
	protected void doStop() {
	}

	protected void sendMessage(Message<?> message) {
		if (message == null) {
			throw new MessagingException("cannot send a null message");
		}
		if (this.shouldTrack) {
			message = MessageHistory.write(message, this);
		}
		try {
			this.messagingTemplate.send(this.outputChannel, message);
		} catch (Exception e) {
			if (this.errorChannel != null) {
				this.messagingTemplate.send(this.errorChannel, new ErrorMessage(e));
			} else if (e instanceof MessagingException) {
				MessagingException me = (MessagingException)e;
				if(me.getFailedMessage() == null){
					logger.error(null, e);
				} else {
//					Message<?> errorMessage = MessageBuilder.fromMessage(me.getFailedMessage()).copyHeaders(message.getHeaders()).build();
					throw new MessageDeliveryException(me.getFailedMessage(), "failed to send message", e);
				}
			} else {
				throw new MessageDeliveryException(message, "failed to send message", e);
			}
		}
	}
}
