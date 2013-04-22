package cn.ls.integrator.core.mongo;

import java.util.Date;
import java.util.Random;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class InsertJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("Start insert Job");
		TestEntity entity = new TestEntity();
		long count = new Random().nextLong();
		entity.setName("name" + count);
		entity.setNum(count);
		entity.setTime(new Date());
		TestMongo.dataStore.save(entity);
	}

}
