package cn.ls.integrator.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
/**
 * 
* @ClassName: TongQueTest 
* @Description: TongQue 接受发送测试
* @author wanl
* @date 2011-4-22 上午10:08:27 
* @version V1.0
 */
public class TongQueTest   {
	 @Test 
	 public void test(){
		 Map<String, Object> map = new  HashMap<String, Object>();
	    	map.put("id", 111111);
	    	map.put("id2", 222222);
	    	List<Object> list = new ArrayList<Object>();
	    	list.add("中");
	    	list.add("2222我");
	    	Message<?> m = MessageBuilder.withPayload(list).copyHeaders(map).build();
	    	
	    	TongTargetComponent adapter = new TongTargetComponent();
	    	adapter.setQcuName("qcu1");
	    	adapter.setQName("lq");
	    	adapter.handleMessage(m);
	    	
	    	
	    	
	    	Message<?> m2 = MessageBuilder.withPayload("").build();
	    	TongSourceComponent  in = new TongSourceComponent();
	    	in.setQcuName("qcu1");
	    	in.setQName("lq");
	    	in.setWaitInterval(1000);
	    	in.handleMessage(m2);	 
	 }
}
