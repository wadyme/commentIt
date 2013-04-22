package cn.ls.integrator.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.ls.integrator.client.service.ScheduleService;
import cn.ls.integrator.client.service.Task;
import cn.ls.integrator.client.service.util.ServiceFactory;

public class TestServiceWithErrorData {
	
	public static ScheduleService getService() {
		ScheduleService service = null;
		try {
			service = ServiceFactory.getScheduleService("192.168.1.132", 11818,5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return service;
	}
	@Test
	public void testGetSchedule() {
		ScheduleService service = getService();
		service.getRecvSchedule("test");
	}

	@Test
	public void testGetTask() {
		ScheduleService service = getService();
		service.getSendTask("test", "task1");
	}

	@Test
	public void testGetTaskContext() {
		ScheduleService service = getService();
		System.out.println(service.getSendTaskContext("schedule1", "task1"));
	}

	@Test
	public void testStartSchedule() {
		ScheduleService service = getService();
		service.startSendSchedule("tttt");
	}

	@Test
	public void testAddSchedule() {
		ScheduleService service = getService();
		// service.addSendSchedule("schedule3", "test", "test", null, null,
		// null, null);
		Task task = new Task();
		task.setName("task2");
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		List<String> contexts = new ArrayList<String>();
		contexts.add("test");
		service.addRecvSchedule("schedule2", "test", "test", tasks, contexts);
	}

	@Test
	public void testAddTask() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setName("task2");
		service.addRecvTask("schedule2", task, "test");
	}

	@Test
	public void testSaveTask() {
		ScheduleService service = getService();
		Task task = new Task();
		task.setName("task5");
		service.updateRecvTask("schedule2", "task2", task, "tsdfsd");
	}

	@Test
	public void testStart() {
		ScheduleService service = getService();
//		service.startSendSchedule("schedule1");
		service.stopSendSchedule("schedule1");
	}
	@Test
	public void testDeleteTask(){
		ScheduleService service = getService();
		service.deleteRecvTask("schedule2", "task5");
	}
	
	@Test
	public void testDeleteSchedule(){
		ScheduleService service = getService();
		service.deleteRecvSchedule("schedule2");
	}
	

}
