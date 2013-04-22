package cn.ls.integrator.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.handler.MessageHandlerChain;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.components.utils.ApplicationContextHelper;



public class AppTest {
	 @Test  
	 public void test() throws IOException{
		 
//		 AbstractApplicationContext context = ApplicationContextHelper.getApplicationContext();
//		 BasicDataSource dataSource = context.getBean("dataSource", BasicDataSource.class);
//		 MongoDataStore mongoDataStore = context.getBean("mongoDatastore", MongoDataStore.class);
//		 Properties properties = new Properties();
//		 properties.load(this.getClass().getResourceAsStream("application.properties"));
//		 Map<String,Object> headersToCopy = new HashMap<String,Object>();
//		 copyMap(headersToCopy,properties);
//		 Message<?> message = MessageBuilder.withPayload("").copyHeaders(headersToCopy).build();
//
//		 List<MessageHandler> handlers = new ArrayList<MessageHandler>();
//		 
//		 DBInputAdapter dBInputAdapter = new DBInputAdapter(dataSource);
//		 
//		 MessageSendLogComponent sendLogCompinent = new MessageSendLogComponent(mongoDataStore);
//		 
//		 JmsOutputAdapter jmsOutputAdapter = new JmsOutputAdapter();
//		 
//		 JmsInputAdapter jmsInputAdapter = new JmsInputAdapter();
//		 
//		 JdbcTargetComponent dBOutputAdapter = new JdbcTargetComponent(dataSource,mongoDataStore);
//		 dBOutputAdapter.setEncountered(true);
//		 
//		 MessageReceiveLogComponent receiveLogComponent = new MessageReceiveLogComponent(mongoDataStore);
////		 
//		 ExchangeErrLogComponent exchangeErrLogComponent = new ExchangeErrLogComponent(mongoDataStore);
//		 //exchangeErrLogComponent.setErrorChannel(errorChannel)
//		 
//		 //TestAdapter test = new TestAdapter();
//		 
//		 QueueChannel outputChannel = new QueueChannel();
//		
//		 handlers.add(dBInputAdapter);	 
//		 //handlers.add(test);
//		 handlers.add(jmsOutputAdapter);
//		
//		 handlers.add(sendLogCompinent);
//		 handlers.add(jmsInputAdapter);
//		 handlers.add(receiveLogComponent);
//		 handlers.add(dBOutputAdapter);
//		 handlers.add(exchangeErrLogComponent);
//		
//		
//		
//		 
//		 MessageHandlerChain chain = new MessageHandlerChain();
//		 chain.setBeanName("testChain");
//		 chain.setHandlers(handlers);
//		 chain.setOutputChannel(outputChannel); 
//		 chain.handleMessage(message);
//		
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
//		 
		 
		 
		// PollerMetadata
		 
 
		 
	 }
	 
	 @Test  
	 public void test2() throws IOException{
		 
		 AbstractApplicationContext context = ApplicationContextHelper.getApplicationContext();
		 
		 //exchangeErrLogComponent.setErrorChannel(errorChannel)
		 
		 //TestAdapter test = new TestAdapter();
		 MessageHandlerChain chain = new MessageHandlerChain();
		 List<MessageHandler> handlers = new ArrayList<MessageHandler>();
		 QueueChannel outputChannel = new QueueChannel();
		
		 
		 //packageLimitSize
		 Map<String, Object> headers = new HashMap<String, Object>();
		 headers.put("packageLimitSize", 2);
		 headers.put("message_type", "command");
		 
		 
		 
		 Message<?> cmessage = MessageBuilder.withPayload(headers).copyHeaders(headers).build();
		 
		 JdbcSourceComponent jdbcSourceComponent = context.getBean("jdbcSourceComponent", JdbcSourceComponent.class);
		  
		 handlers.add(jdbcSourceComponent);
		 //handlers.add(tongQueueOutputAdapter);
		 
		
		 chain.setOutputChannel(outputChannel); 
		 chain.setHandlers(handlers);
		 chain.handleMessage(cmessage);
		 
		
		 
		
		 
		
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	
 
		 
	 }
	 
	 @Test  
	 public void test3() throws IOException{
		 
		 AbstractApplicationContext context = ApplicationContextHelper.getApplicationContext();
		 
		 //exchangeErrLogComponent.setErrorChannel(errorChannel)
		 
		 //TestAdapter test = new TestAdapter();
		 MessageHandlerChain chain = new MessageHandlerChain();
		 List<MessageHandler> handlers = new ArrayList<MessageHandler>();
		 QueueChannel outputChannel = new QueueChannel();
		
		 
		 //packageLimitSize
		 Map<String, Object> headers = new HashMap<String, Object>();
		 headers.put("packagesize", 2);
		 //headers.put(IntegratorConstants.MESSAGETYPE, "command");
		 
		 
		 
		 Message<?> cmessage = MessageBuilder.withPayload(headers).copyHeaders(headers).build();
		 
		 JdbcSourceComponent jdbcSourceComponent = context.getBean("jdbcSourceComponent", JdbcSourceComponent.class);
		 CatchExceptionComponent catchExceptionComponent = context.getBean("catchExceptionComponent", CatchExceptionComponent.class);
		 JdbcTargetComponent jdbcTargetComponent = context.getBean("jdbcTargetComponent",JdbcTargetComponent.class); 
		 handlers.add(jdbcSourceComponent);
		 handlers.add(catchExceptionComponent);
		 handlers.add(jdbcTargetComponent);
		 
		
		 chain.setOutputChannel(outputChannel); 
		 chain.setHandlers(handlers);
		 chain.handleMessage(cmessage);
	 }
	 
	 
	 public static void main(String[] args) throws IOException {
		 AppTest appTest = new AppTest();
		 appTest.test3();
	}
	 
	 
	 @SuppressWarnings("unchecked")
	 public static Map<String, ?> copyMap(Map<String, Object> target, Properties sourse) {
			    Enumeration  msgIter = sourse.keys();
			    String key = null;
		          while (msgIter.hasMoreElements()) {
		           key = (String) msgIter.nextElement();
		           if (!MessageHeaders.ID.equals(key) && !MessageHeaders.TIMESTAMP.equals(key)) {
						target.put(key, sourse.getProperty(key));
				   }
		        }
				return target;
	  }
	 
	 
	 
	 

	
}
