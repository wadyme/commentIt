package cn.ls.integrator.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.ls.integrator.client.service.Associate;
import cn.ls.integrator.client.service.Schedule;
import cn.ls.integrator.client.service.ScheduleService;
import cn.ls.integrator.client.service.Task;
import cn.ls.integrator.client.service.Timer;
import cn.ls.integrator.client.service.TimerType;
import cn.ls.integrator.client.service.util.ServiceFactory;

public class TestSchedule {

	@Test
	public void  testSaveSchedule() throws IOException{
		Schedule scheduleInfo = new Schedule();
		scheduleInfo.setDescription("测试");
		scheduleInfo.setName("oa");
		scheduleInfo.setTitle("标题");
		
		Task taskInfo = new Task();
		taskInfo.setDescription("交换任务....................................................");
		taskInfo.setTitle("交换任务标题");
		taskInfo.setName("testTask");
		
		List<String> taskContext = new ArrayList<String>();
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(taskInfo);
		taskContext.add("test----------------------");
		
		Timer timer1 = new Timer();
		timer1.setName("timer1");
		timer1.setTime("2:10:00");
		timer1.setTitle("时间策略标题");
		timer1.setType(TimerType.DAY);
		timer1.setValue("1");
		
		Timer timer2 = new Timer();
		timer2.setName("timer1");
		timer2.setTime("12:10:00");
		timer2.setTitle("时间策略无标题");
		timer2.setType(TimerType.DAY);
		timer2.setValue("12");
		
		List<Timer> timers = new ArrayList<Timer>();
		timers.add(timer1);
		timers.add(timer2);
		
		Associate join1 = new Associate();
		join1.setTaskName(taskInfo.getName());
		join1.setTimerName(timer1.getName());
		
		Associate join2 = new Associate();
		join2.setTaskName(taskInfo.getName());
		join2.setTimerName(timer2.getName());
		
		List<Associate> joins = new ArrayList<Associate>();
		joins.add(join1);
		joins.add(join2);
		
		ScheduleService service = ServiceFactory.getScheduleService("192.168.1.132", 11818,5000);
	//	service.addTask(scheduleInfo.getName(), taskInfo, timers);
		service.addSendSchedule("schedule1","testTitle","testDesc",tasks,timers,taskContext,joins);
	}
}
