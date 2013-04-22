package cn.ls.integrator.components.manager.impl;

import java.util.List;

import cn.ls.integrator.components.manager.LastUpdateTimeManager;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.LastUpdateTime;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class LastUpdateTimeManagerImpl extends
		AbstractMessageLogManagerImpl<LastUpdateTime> implements
		LastUpdateTimeManager {

	private static LastUpdateTimeManager instance;
	
	private LastUpdateTimeManagerImpl(){}
	
	public static LastUpdateTimeManager getInstance(){
		if(instance == null){
			instance = new LastUpdateTimeManagerImpl();
		}
		return instance;
	} 
	
	public Long get(String schema, String tableName) {
		if (StringUtility.isBlank(schema) || StringUtility.isBlank(tableName)) {
			return null;
		}
		Query<LastUpdateTime> query = mongoDataStore
				.createQuery(LastUpdateTime.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter("taskName", schema + "." + tableName);
		List<LastUpdateTime> list = query.asList();
		if (list != null && list.size() > 0) {
			return list.get(0).getLastUpdateTime();
		} else {
			return null;
		}
	}

	public void delete(String schema, String tableName) {
		if (StringUtility.isBlank(schema) || StringUtility.isBlank(tableName)) {
			throw new IntegratorException("schema and tableName can not blank");
		}
		Query<LastUpdateTime> query = mongoDataStore
				.createQuery(LastUpdateTime.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter("taskName", schema + "." + tableName);
		mongoDataStore.delete(query);
	}

	@Override
	public Class<LastUpdateTime> getClazz() {
		return LastUpdateTime.class;
	}
}
