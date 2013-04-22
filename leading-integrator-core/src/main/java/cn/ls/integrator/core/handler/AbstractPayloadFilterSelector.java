package cn.ls.integrator.core.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.integration.Message;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.handler.AbstractMessageProcessor;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;

public abstract class AbstractPayloadFilterSelector implements MessageSelector, BeanFactoryAware {

	private final MessageProcessor<Boolean> messageProcessor;

	public AbstractPayloadFilterSelector(MessageProcessor<Boolean> messageProcessor) {
		Assert.notNull(messageProcessor, "messageProcessor must not be null");
		this.messageProcessor = messageProcessor;
	}


	@SuppressWarnings("unchecked")
	protected void setConversionService(ConversionService conversionService) {
		if (this.messageProcessor instanceof AbstractMessageProcessor) {
			((AbstractMessageProcessor<Boolean>) this.messageProcessor).setConversionService(conversionService);
		}
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.messageProcessor instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.messageProcessor).setBeanFactory(beanFactory);
		}
	}

	public final boolean accept(Message<?> message) {
		List<?> payload = null;
		if(message.getPayload() instanceof List<?>){
			payload = (List<?>)message.getPayload();
			for(int i=0;i<payload.size();i++){
				Map<?,?> map = (HashMap<?,?>)payload.get(i);
				Message<?> msg = MessageBuilder.withPayload(map).build();
			    if(!this.messageProcessor.processMessage(msg)){
			    	payload.remove(i--);
			    }
			}
		}
		return (payload==null || payload.isEmpty()) ? false : true;
	}

}
