/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.file;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.ls.integrator.core.manager.impl.SendScheduleManagerImpl;
import cn.ls.integrator.core.model.Associate;
import cn.ls.integrator.core.model.Schedule;
import cn.ls.integrator.core.model.Task;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.model.Timer;
import cn.ls.integrator.core.model.TimerType;
import cn.ls.integrator.core.utils.FileUtility;

/**
 * {@link com.thoughtworks.xstream.XStream}读写测试类
 * 
 * @author zhoumb 2011-5-10
 */
public class XStreamTest {
	String fileContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<beans:beans xmlns=\"http://www.springframework.org/schema/integration\"	" + "\r\n" + "\t\t"
		+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + "\r\n" + "\t\t"
		+ "xmlns:beans=\"http://www.springframework.org/schema/beans\"	" + "\r\n" + "\t\t"
		+ "xsi:schemaLocation=\"http://www.springframework.org/schema/beans			" + "\r\n" + "\t\t"
		+ "http://www.springframework.org/schema/beans/spring-beans.xsd			" + "\r\n" + "\t\t"
		+ "http://www.springframework.org/schema/integration			" + "\r\n" + "\t\t"
		+ "http://www.springframework.org/schema/integration/spring-integration.xsd\">	" + "\r\n" + "\t"
		+ "<chain input-channel=\"agrsChannel\">" + "<service-activator ref=\"helloworld\" />" + "\r\n" + "\t"
		+ "</chain>" + "\r\n" + "\t"
		+ "<beans:bean id=\"helloworld\" class=\"cn.ls.integrator.core.HelloWorldMessageHandler\" />" + "\r\n"
		+ "</beans:beans>";

	@Test
	public void taskFileTest() throws FileNotFoundException {
//		TaskInfo taskInfo = new TaskInfo();
//		taskInfo.setContext(fileContext);
//		taskInfo.setDescription("交换任务....................................................");
//		taskInfo.setTitle("交换任务标题");
//		taskInfo.setName("testTask");
//		FileUtility.getInstance().saveTaskFile(taskInfo, "oa");
	}

	@Test
	public void sendScheduleFileTest() throws FileNotFoundException {
		Schedule scheduleInfo = new Schedule();
		scheduleInfo.setDescription("测试");
		scheduleInfo.setName("oa");
		scheduleInfo.setTitle("标题");
		Task taskInfo = new Task();
		taskInfo.setDescription("交换任务....................................................");
		taskInfo.setTitle("交换任务标题");
		taskInfo.setName("testTask");
		//taskInfo.setContext(fileContext);
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(taskInfo);
		scheduleInfo.setTasks(tasks);
		Timer timer = new Timer();
		timer.setName("timer1");
		timer.setTime("2:10:00");
		timer.setTitle("时间策略标题");
		timer.setType(TimerType.day);
		timer.setValue("1");
		List<Timer> timers = new ArrayList<Timer>();
		timers.add(timer);
		scheduleInfo.setTimers(timers);
		Associate join = new Associate(taskInfo.getName(), timer.getName());
		List<Associate> joins = new ArrayList<Associate>();
		joins.add(join);
		scheduleInfo.setAssociates(joins);
		FileUtility.getInstance().saveScheduleFile(scheduleInfo, TaskType.send);
	}

	@Test
	public void getFromFilesTest() {
		List<Schedule> list = SendScheduleManagerImpl.getInstance().getList();
		Assert.assertNotNull(list);
		System.out.println(list.size());
		Schedule schedule = list.get(0);
		Task task = schedule.getTasks().get(0);
		System.out.println(task.getDescription());

		task = SendScheduleManagerImpl.getInstance().getTask(schedule, task.getName());
		System.out.println(task);
	}
}
