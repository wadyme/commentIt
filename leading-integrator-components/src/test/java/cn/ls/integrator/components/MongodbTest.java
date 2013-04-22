package cn.ls.integrator.components;

import java.util.List;

import org.junit.Test;

import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.core.log.message.ExchangeState;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class MongodbTest {
	@Test
	public void test() {
		MongoDataStore mongo = MongoDataStore.getInstance();
		Query<ExchangeState> query = mongo.createQuery(ExchangeState.class);
		query.filter("scheduleName", "file");
		query.filter("logType", "send");
		List<ExchangeState> list = query.asList();
		ExchangeState stat = list.get(0);
		stat.setCompleteSize(1000000);
		mongo.updateFirst(query, stat, true);
	}
   
	@Test
	public void testOr() {
		MongoDataStore mongo = MongoDataStore.getInstance();
		Query<ExchangeState> query = mongo.createQuery(ExchangeState.class);
		query.or(query.criteria("integratorId").equal(""),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()));
		List<ExchangeState> list = query.asList();
		System.out.println(list.size());
	}
}
