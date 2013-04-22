package cn.ls.integrator.core.mongo;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.morphia.query.Query;

public class UpdateJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("start update job");
		Query<TestEntity> query = TestMongo.dataStore.createQuery(TestEntity.class);
		query.where("this.num%1000 == 0");
		List<TestEntity> list = query.asList();
		if(list == null || list.size() == 0){
			TestEntity entity = new TestEntity();
			entity.setName("new one");
			entity.setTime(new Date());
			entity.setNum(0);
			TestMongo.dataStore.save(entity);
		}else{
			TestMongo.dataStore.delete(query);
			for(TestEntity entity:list){
				entity.setNum(-1);
				TestMongo.dataStore.save(entity);
			}
		}

	}
}
