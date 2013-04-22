package cn.ls.integrator.client;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;

import cn.ls.integrator.client.service.Associate;
import cn.ls.integrator.client.service.Connection;
import cn.ls.integrator.client.service.ConnectionType;
import cn.ls.integrator.client.service.Schedule;
import cn.ls.integrator.client.service.ScheduleService;
import cn.ls.integrator.client.service.Task;
import cn.ls.integrator.client.service.Timer;
import cn.ls.integrator.client.service.TimerType;
import cn.ls.integrator.client.service.util.SerializeUtils;
import cn.ls.integrator.client.service.util.ServiceFactory;
import cn.ls.integrator.common.TaskUtility;

public class TestService {
	public static ScheduleService getService() {
		ScheduleService service = null;
		try {
			service = ServiceFactory.getScheduleService("192.168.1.152", 11818,5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return service;
	}

	@Test
	public void testAddSendSchedule() {
		ScheduleService service = getService();
		List<Task> tasks = new ArrayList<Task>();
		List<Timer> timers = new ArrayList<Timer>();
		List<String> taskContexts = new ArrayList<String>();
		List<Associate> associates = new ArrayList<Associate>();
		Task task = new Task();
		task.setDescription("testTask");
		task.setName("task1");
		task.setTitle("testTask");
		task.setAdapterType("DB2Source");
		tasks.add(task);
		Timer timer = new Timer();
		timer.setDescription("testTimer");
		timer.setName("timer1");
		timer.setTime("10:00:00");
		timer.setTitle("testTimer");
		timer.setType(TimerType.DAY);
		timer.setValue("1");
		timers.add(timer);
		taskContexts.add("tttttttttttttttt");
		Associate ass = new Associate();
		ass.setTaskName("task1");
		ass.setTimerName("timer1");
		associates.add(ass);
		service.addSendSchedule("schedule1", "test", "test", tasks, timers,
				taskContexts, associates);
	}

	@Test
	public void testAddRecvSchedule() {
		ScheduleService service = getService();
		List<Task> tasks = new ArrayList<Task>();
		List<Timer> timers = new ArrayList<Timer>();
		List<String> taskContexts = new ArrayList<String>();
		List<Associate> associates = new ArrayList<Associate>();
		Task task = new Task();
		task.setDescription("testTask");
		task.setName("task1");
		task.setTitle("testTask");
		tasks.add(task);
		Timer timer = new Timer();
		timer.setDescription("testTimer");
		timer.setName("timer1");
		timer.setTime("10:00:00");
		timer.setTitle("testTimer");
		timer.setType(TimerType.DAY);
		timer.setValue("1");
		timers.add(timer);
		taskContexts.add("tttttttttttttttt");
		Associate ass = new Associate();
		ass.setTaskName("task1");
		ass.setTimerName("timer1");
		associates.add(ass);
		service.addRecvSchedule("schedule2", "test", "test", tasks,
				taskContexts);
	}

	@Test
	public void testAddRecvTask() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setDescription("testTask");
		task.setName("task3");
		task.setTitle("testTask");
		service.addRecvTask("schedule2", task, "tests");
	}

	@Test
	public void testAddTimerOnTask() {
		ScheduleService service = getService();
		Timer timer = new Timer();
		timer.setDescription("testTimer");
		timer.setName("每隔30秒执行一次");
		timer.setTitle("每隔30秒执行一次");
		timer.setType(TimerType.INTERVAL);
		timer.setValue("s");
		timer.setTime("30");
		service.addTimerOnTask("schedule1", timer, "taskx");
	}

	@Test
	public void testAddTaskOnTimer() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setDescription("testTask");
		task.setName("taskFile");
		task.setTitle("testTask1Title");
		task.setDescription("descsdfljkl");
		task.setParameters("ip:192.168.1.102,nodeName:天堂");
		service.addTaskOnTimer("schedule1", task, "每隔40秒执行一次", null);
	}

	@Test
	public void testUpdateTimer() {
		ScheduleService service = getService();
		Timer timer = new Timer();
		timer.setName("timer88888");
		timer.setTime("2:12:00");
		timer.setTitle("时间策略标题");
		timer.setType(TimerType.DAY);
		timer.setValue("1");
		service.updateTimer("schedule1", "timer007", timer);
	}

	@Test
	public void testUpdateSendTask() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setName("taskx");
		task.setDescription("testsaveSendTaskx");
		task.setTitle("testTaskxTitle");
		task.setAdapterType("DB2Source");
		String context = getContext("D:\\Program Files\\li\\schedules\\send\\schedule1.EPD.PS_Gas_Output_Year_Let.task");
		service.updateSendTask("schedule1", "task4", task, context);
	}

	@Test
	public void testUpdateRecvTask() {
		ScheduleService service = getService();
		Task taskInfo = new Task();
		taskInfo.setDescription("交换任务.269.....");
		taskInfo.setTitle("这是标题");
		taskInfo.setName("testTaskabc");

		service.updateRecvTask("testSchedule1", "testTask2", taskInfo, "水电费");
	}

	private String getContext(String string) {
		String modelString = null;
		try {
			FileInputStream fis = new FileInputStream(string);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int size = bis.available();
			byte[] buff = new byte[size];
			int len = 0;
			while ((len = bis.read(buff)) > 0) {
				modelString = new String(buff, 0, len, "UTF8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelString;
	}

	@Test
	public void testDeployRecvTask() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setDescription("testTask");
		task.setName("task3");
		task.setTitle("testTask");
		service.deployRecvTask("schedule2", task, "test deployTask");
	}

	@Test
	public void testDeleteRecvTask() {
		ScheduleService service = getService();
		service.deleteRecvTask("schedule2", "task3");
	}

	@Test
	public void testDeleteTaskOnTimer() {
		ScheduleService service = getService();
		service.deleteTaskOnTimer("schedule1", "每隔40秒执行一次", "taskx");
	}

	@Test
	public void testDeleteTimerOnTask() {
		ScheduleService service = getService();
		service.deleteTimerOnTask("schedule1", "task5", "每隔40秒执行一次");
	}

	@Test
	public void testDeleteRecvSchedule() {
		ScheduleService service = getService();
		service.deleteRecvSchedule("schedule2");
	}

	@Test
	public void testDeleteSendSchedule() {
		ScheduleService service = getService();
		service.deleteSendSchedule("schedule1");
	}

	@Test
	public void testDeleteTimeStamp() {
		ScheduleService service = getService();
		service.deleteTimestamp("test", "test");
	}

	@Test
	public void testStopAllQueueListener() {
		ScheduleService service = getService();
		service.stopAllQueueListener();
	}

	@Test
	public void testManualStartTask() {
		ScheduleService service = getService();
		service.manualStartTask("file", "TEST", "", "");
	}

	@Test
	public void testGetAssociates() {
		ScheduleService service = getService();
		List<Associate> associates = service.getAssociates("schedule1");
		int size = associates.size();
		System.out.println("schedule1下有" + size + "个关联关系");
		for (Associate ass : associates) {
			System.out.println(ass.getTaskName() + "-----------------------" + ass.getTimerName());
		}
	}

	@Test
	public void testGetSendSchedules() throws IOException, InterruptedException {
		ScheduleService service = ServiceFactory.getScheduleService("192.168.1.143", 11818, 5000);;
//		ScheduleService service = getService();
		System.out.print("start");
		
		Thread.sleep(6000);
		long s = System.currentTimeMillis();
		List<Schedule> sendSchedules = null;
		try {
			sendSchedules = service.getSendScheduleList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println(System.currentTimeMillis() - s);
		System.out.println("共有" + sendSchedules.size() + "个schedule");
		Schedule sc = sendSchedules.get(0);
		System.out.println(sc.getName());
		List<Timer> timers = sc.getTimers();
		System.out.println(timers.get(0).getType());
	}

	@Test
	public void testGetSendTask() {
		ScheduleService service = getService();
		Task task = service.getSendTask("schedule1", "task1");
		System.out.println(task);
	}

	@Test
	public void testGetRecvTask() {
		ScheduleService service = getService();
		Task t = service.getRecvTask("testSchedule1", "testTaskabc");
		System.out.println(t.getTitle());
	}

	@Test
	public void testGetSendScheduleList() {
		ScheduleService service = getService();
		List<Schedule> scheduleList = service.getSendScheduleList();
		scheduleList = service.getSendScheduleList();
		service = getService();
		scheduleList = service.getSendScheduleList();
		System.out.println(scheduleList);
	}

	@Test
	public void testGetRecvScheduleList() {
		ScheduleService service = getService();
		List<Schedule> lists = service.getRecvScheduleList();
		System.out.println(lists.size());
		System.out.println(lists.get(0).getDescription());
	}

	@Test
	public void testGetRecvTaskList() {
		ScheduleService service = getService();
		List<Task> lists = service.getRecvTaskList("testSchedule1");
		System.out.println(lists.size());
		System.out.println(lists.get(0).getTitle());
	}

	@Test
	public void testGetSendTaskList() {
		ScheduleService service = getService();
		List<Task> tasks = service.getSendTaskList("schedule1");
		System.out.println(tasks);
	}

	@Test
	public void testGetTimeStamp() {
		ScheduleService service = getService();
		long l = service.getTimestamp("schedule", "task1");
		System.out.println(l);
	}

	@Test
	public void testGetSendTaskContext() {
		ScheduleService service = getService();
		String context = service.getSendTaskContext("schedule1", "taskx");
		System.out.println(context);
	}

	@Test
	public void testGetRecvTaskContext() {
		ScheduleService service = getService();
		String s = service.getRecvTaskContext("testSchedule1", "testTaskabc");
		System.out.println(s);
	}

	@Test
	public void testGetSendErrorLogList() {
		ScheduleService service = getService();
		String str = service.getSendTaskContext("schedule1", "taskx");
		System.out.println(str);
	}

	@Test
	public void testGetRecvErrorLogList() {
		ScheduleService service = getService();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("scheduleName", "PME");
		map.put("taskName", "GZJDJLSJB");
		map.put("sendNodeName", "重庆");
		map.put("logType", "recv");
		String parameters = SerializeUtils.serializer(map);
		System.out.println(service.getErrorLogList(parameters, 0, 5));//"PME","GZJDJLSJB$"
	}
	
	@Test
	public void testExistsErrorLog(){
		ScheduleService service = getService();
		System.out.println(service.existRecvErrorLog("PME", "GZJDJLSJB", null, "重庆"));
	}

	@Test
	public void testStopSendSchedule() {
		ScheduleService service = getService();
		service.stopSendSchedule("schedule1");
	}

	@Test
	public void testStartSendSchedule() {
		ScheduleService service = getService();
		service.startSendSchedule("schedule1");
	}

	@Test
	public void testUpdateTimerOnTask() {
		ScheduleService service = getService();
		Timer timer = new Timer();
		timer.setDescription("testTimer");
		timer.setName("每隔40秒执行一次");
		timer.setTitle("每隔40秒执行一次");
		timer.setType(TimerType.INTERVAL);
		timer.setValue("s");
		timer.setTime("40");
		service.updateTimerOnTask("schedule1", "taskx", "每隔20秒执行一次", timer);
	}

	@Test
	public void testGetParameter() {
		ScheduleService service = getService();
		System.out.println(service.getSendTaskParameters("EPM", "PS_GAS_OUTPUT_YEAR_DATA"));
	}
	
	@Test
	public void testGetPublicParameter() {
		ScheduleService service = getService();
		System.out.println(service.getSendScheduleParameters("EPM"));
	}

	@Test
	public void testGetParameterValue() {
		ScheduleService service = getService();
		String str = service.getSendTaskParameterValues("EPM", "PS_GAS_OUTPUT_YEAR_DATA");
		System.out.println(TaskUtility.getInstance().parseParameter(str));
	}
	
	@Test
	public void testGetPublicParameterValue() {
		ScheduleService service = getService();
		String str = service.getSendScheduleParameterValues("EPM");
		System.out.println(TaskUtility.getInstance().parseParameter(str));
	}
	
	@Test
	public void testGetConnectionTypeList(){
		ScheduleService service = getService();
		List<String> list = service.getConnectionTypeStringList();
		for(String str : list){
			System.out.println(str);
		}
	}
	
	@Test
	public void testGetConnectionType(){
		ScheduleService service = getService();
		ConnectionType type = service.getConnectionType("sqlserver");
		System.out.println(type.getDriver());
	}
	
	@Test
	public void testGetSendScheduleConnection(){
		ScheduleService service = getService();
		Connection conn = service.getSendScheduleConnection("EPM");
		System.out.println(conn.getUrl());
	}
	
	@Test
	public void testUpdateSendScheduleConnection(){
		ScheduleService service = getService();
		Connection conn = service.getSendScheduleConnection("EPM");
		conn.setUrl("jdbc:db2://192.168.1.103:50000/EPMONDB");
		service.updateSendScheduleConnection("EPM", conn);
	}
	@SuppressWarnings("unchecked")
	@Test
	public void testSetSendScheduleParameterValues(){
		ScheduleService service = getService();
		String str = service.getSendScheduleParameterValues("EPM");
		Map<String,String> map = (Map<String,String>)SerializeUtils.deserializer(str);
		System.out.println(map);
		map.put("recvNodeName", "环保部");
		service.updateSendScheduleParameterValues("EPM", SerializeUtils.serializer(map));
	}

	@Test
	public void testSetParameterValue() {
		ScheduleService service = getService();
		String str = service.getSendTaskParameterValues("JIANGUAN_FILE", "BEIJING");
		Map<String, String> map = TaskUtility.getInstance().parseParameter(str);
		map.put("sourcePath", "Z:/建管");
		service.updateSendTaskParameterValues("JIANGUAN_FILE", "BEIJING", TaskUtility
				.getInstance().formatParameter(map));
	}

	@Test
	public void testStopSchedule() {
		ScheduleService service = getService();
		service.stopSendSchedule("schedule1");
	}

	@Test
	public void testGetTaskParam() {
		ScheduleService service = getService();
		System.out.println(service.getAssociateWithSendTaskParam("schedule1"));
	}

	@Test
	public void testGetOutTaskParam() {
		ScheduleService service = getService();
		System.out.println(service.getAssociateWithOutSendTaskParam("schedule1"));
	}

	@Test
	public void testExistRecvErrorLog() {
		ScheduleService service = getService();
		System.out.println(service.existRecvErrorLog("PME","GZJDJLSJB", "重庆", "重庆"));
		;
	}


	@Test
	public void testStartRecvScanner() {
		ScheduleService service = getService();
		service.startAllQueueListener();
	}

	@Test
	public void testStopQueueListener() {
		ScheduleService service = getService();
		service.stopQueueListener("qcu_123456", "LQ_test");
	}

	@Test
	public void testStartQueueListener() {
		ScheduleService service = getService();
		service.startQueueListener("qcu_123456", "LQ_test");
	}
	
	@Test
	public void testGetSendScheduleState() {
		ScheduleService service = getService();
		try{
			service.getSendScheduleState("edd");
		} catch (SOAPFaultException e){
			e.getCause().printStackTrace();
		}
	}
	
	@Test
	public void testThread() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			new ServiceThread().start();
		}
		Thread.sleep(1000000);
	}
	
	@Test
	public void testInstall(){
		ScheduleService service = getService();
		service.initializeFileTask("file", "TEST");
	}
}

class ServiceThread extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 10000; i++) {
			ScheduleService service = TestService.getService();
			System.out.println(this.getName() + "----------->"
					+ service.getAssociateWithOutSendTaskParam("schedule1"));
		}
		super.run();
	}
}