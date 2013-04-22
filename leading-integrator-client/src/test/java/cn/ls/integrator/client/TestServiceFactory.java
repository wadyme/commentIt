package cn.ls.integrator.client;

import java.io.IOException;

import org.junit.Test;

import cn.ls.integrator.client.service.util.ServiceFactory;

public class TestServiceFactory {

	@Test
	public void testConnectionLi() {
		try {
			Integer response =  ServiceFactory.connectionLi("192.168.1.142", 11818,5000);
			if(response==null){
				System.err.println("ip或port未设置！");
			}else if(response.intValue()==200){
				System.out.println("Li正常连接");
			}else {
				System.err.println("Li连接响应结果："+response.intValue());
			}
		} catch (IOException e) {
			System.err.println("连接异常！");
			e.printStackTrace();
		}
		System.out.println("测试结束");
	}
}
