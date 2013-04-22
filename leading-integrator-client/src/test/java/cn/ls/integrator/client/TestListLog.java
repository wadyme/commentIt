package cn.ls.integrator.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.ls.integrator.client.service.ScheduleService;
import cn.ls.integrator.client.service.util.SerializeUtils;
import cn.ls.integrator.client.service.util.ServiceFactory;
import flexjson.JSONDeserializer;

public class TestListLog {

	@Test
	public void testGetLogList() throws IOException{
		ScheduleService service = ServiceFactory.getScheduleService("192.168.1.152", 11818,5000);
		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("recvNodeName", "^Test151$");
//		map.put("scheduleName", "^EPM$");
//		map.put("taskName", "^PS_WATER_OUTPUT_POLLUTANT_SET$");
		map.put("sendNodeName", "^湖北$");
		map.put("logType", "^recv$");
		String parameters = SerializeUtils.serializer(map);
		String logStr = service.getLogList(parameters, -1, -1);
		List<Map<?,?>> logList = new JSONDeserializer<List<Map<?,?>>>().deserialize(logStr);
		System.out.println(logList);
	}
	private ScheduleService getScheduleService(){
		ScheduleService service = null;
		try {
			service = ServiceFactory.getScheduleService("192.168.1.152", 11818,5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return service;
	}
	@Test
	public void testGetSendErrorLogList(){
		ScheduleService service = getScheduleService();
		String logStr = service.getErrorLogList("", -1, -1);
		System.out.println(logStr);
	}
	
	@Test
	public void testGetTimestamp(){
		ScheduleService service = getScheduleService();
		long timestamp = service.getTimestamp("test", "test");
		System.out.println(timestamp);
	}
	
	@Test
	public void testDeleteTimeStamp(){
		ScheduleService service = getScheduleService();
		service.deleteTimestamp("dbo", "PS_Gas_Fac_Day_Data");
	}
	
	@Test
	public void testExistErrorLog(){
		ScheduleService service = getScheduleService();
		System.out.println(service.existSendErrorLog("ESTA", "YYFSSJB", "重庆", "重庆"));
	}
	
	@Test
	public void testErrorList(){
		ScheduleService service = getScheduleService();
		System.out.println(service.getErrorLogList("", -1, -1));
	}
	
	@Test
	public void testStartSendSchedule(){
		ScheduleService service = getScheduleService();
		service.startSendSchedule("EIA");
	}
	
	@Test
	public void testGetSendSchedule(){
		ScheduleService service = getScheduleService();
		System.out.println(service.getSendScheduleList());
	}
	
	/**
	 * 测试失败，这种方法无法重启
	 */
	@Test
	public void testRestartRecvScanner(){
		ScheduleService service = getScheduleService();
		service.startAllQueueListener();
	}
	
	@Test
	public void testStopQueueListener(){
		ScheduleService service = getScheduleService();
		service.stopQueueListener("qcu_123456", "LQ_test");
	}
	
	@Test
	public void testStartQueueListener(){
		ScheduleService service = getScheduleService();
		service.startQueueListener("qcu_123456", "LQ_test");
	}
	
	@Test
	public void testStartAllQueueListener(){
		
		
		ScheduleService service = getScheduleService();
		service.startAllQueueListener();
	}
	
	@Test
	public void testStopAllQueueListener(){
		ScheduleService service = getScheduleService();
		service.stopAllQueueListener();
	}
	
	@Test
	public void testManualStartTask(){
		ScheduleService service = getScheduleService();
		service.manualStartTask("PME", "GZJDJLSJB", "", "");
	}
	@Test
	public void testFileLog(){
		ScheduleService service = getScheduleService();
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("exchangeTaskName", "^152$");
//		String parameters = SerializeUtils.serializer(map);
		String str = service.getFileExchangeNumByExchageTaskName("EPM.PS_BASE_INFO","","2012-07-20 00:00:00","2012-07-20 23:59:59");
		System.out.println(str);
	}
	
}
