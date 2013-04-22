package cn.ls.integrator.core.mongo;

import java.net.UnknownHostException;
import java.text.ParseException;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import cn.ls.integrator.core.model.Timer;
import cn.ls.integrator.core.model.TimerType;
import cn.ls.integrator.core.utils.CornUtility;
import cn.ls.integrator.core.utils.TriggerNameUtility;

import com.google.code.morphia.DatastoreImpl;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestMongo {

	/**
	 * @param args
	 */
	private static Scheduler scheduler ;
	public static DatastoreImpl dataStore;
	static{
		Morphia mor = new Morphia();
		try {
			Mongo mongo = new Mongo("127.0.0.1",27017);
			dataStore = new DatastoreImpl(mor, mongo,"li");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws SchedulerException, ParseException {
		Timer timer = new Timer();
		timer.setType(TimerType.interval);
		timer.setTime("10");
		timer.setValue("s");
		String expression1 = CornUtility.getExpression(timer);
		timer.setTime("15");
		String expression2 = CornUtility.getExpression(timer);
		for(int i=0;i<20;i++){
			startJob("test","task" + i,"每隔十秒执行一次",InsertJob.class,expression1);
			startJob("test","task0" + i,"每隔十五秒执行一次",UpdateJob.class,expression2);
		}
	}
	private static void startJob(String scheduleName,String taskName,String timerName,Class<? extends Job> clazz,String expression) throws SchedulerException, ParseException{
		Scheduler sched = getScheduler();
		String triggerName = TriggerNameUtility.getTriggerName(taskName,
				timerName);
		JobKey jobKey = new JobKey(triggerName, scheduleName);
		if (!sched.checkExists(jobKey)) {
			JobDetail jobDetail = JobBuilder.newJob(clazz)
					.withIdentity(jobKey).build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(
					triggerName, scheduleName).withSchedule(
					CronScheduleBuilder.cronSchedule(expression)).build();
			sched.scheduleJob(jobDetail, trigger);
		}else {
		}
		if (!sched.isStarted()) {
			sched.start();
		}
	}
	private static synchronized Scheduler getScheduler() {
		if(scheduler == null){
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		return scheduler;
	}

}
