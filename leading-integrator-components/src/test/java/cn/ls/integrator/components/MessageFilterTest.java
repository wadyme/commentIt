package cn.ls.integrator.components;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.integration.Message;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.filter.MessageFilter;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.core.handler.PayloadFilterSelector;



public class MessageFilterTest {


	@Test
	public void filterExpressionMessage() {
		//PayloadRootUtils.getPayloadRootQName(source, transformerFactory)
		MessageFilter filter = new MessageFilter(new PayloadFilterSelector("payload.name eq 'record1'"));
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for(int i=0;i<10;i++){
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("id", i+1);
			record.put("name", "record"+(i+1));
			record.put("value", 10000+i+1);
			data.add(record);
		}
		Message<?> message = MessageBuilder.withPayload(data).build();
		QueueChannel output = new QueueChannel();
		filter.setOutputChannel(output);
		filter.handleMessage(message);
		//Message<?> received = output.receive(0);
		if(message!=null){
			System.out.println("message.getHeaders:  "+message.getHeaders().toString());
			System.out.println("message.getPayload:  "+message.getPayload().toString());
		
		}else{
			System.out.println("message.getHeaders:  no data! ");
		}
		
	}
	
	@Test
	public void filterExpressionMessage2() {
		//PayloadRootUtils.getPayloadRootQName(source, transformerFactory)
		MessageFilter filter = new MessageFilter(new PayloadFilterSelector("payload.name eq 'record1'"));
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for(int i=0;i<10;i++){
			Map<String,Object> record = new HashMap<String,Object>();
			record.put("id", i+1);
			record.put("name", "record"+(i+1));
			record.put("value", 10000+i+1);
			data.add(record);
		}
		Message<?> message = MessageBuilder.withPayload(data).build();
		QueueChannel output = new QueueChannel();
		filter.setOutputChannel(output);
		filter.handleMessage(message);
		Message<?> received = output.receive(0);
		if(received!=null){
			System.out.println("message.getHeaders:  "+received.getHeaders().toString());
			System.out.println("message.getPayload:  "+received.getPayload().toString());
		
		}else{
			System.out.println("message.getHeaders:  no data! ");
		}
		
	}
	public void writeNumberToFile(String targetPath){
		String filePath = "D:\\aa.txt";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = br.readLine();
			String[] arrStr = line.split(",");
			Arrays.sort(arrStr);
			pw = new PrintWriter(targetPath);
			for(int i=0;i<arrStr.length;i++){
				pw.println(arrStr[i]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	


}
