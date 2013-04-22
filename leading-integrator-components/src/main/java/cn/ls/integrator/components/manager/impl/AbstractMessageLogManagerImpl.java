package cn.ls.integrator.components.manager.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import cn.ls.integrator.components.manager.MessageLogManager;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;
/**
 * 日志接口实现类
 * 
 * @author Administrator
 * @param <T>
 *
 */
public abstract class AbstractMessageLogManagerImpl<T> implements MessageLogManager<T>{
//	private static Logger logger = Logger.getLogger(AbstractMessageLogManagerImpl.class);
	
	public static MongoDataStore mongoDataStore = MongoDataStore.getInstance();

	abstract public Class<T> getClazz() ;
	
	@Override
	public List<T> getLogList() {
		Query<T> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
		query.or(
			query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
			query.criteria("integratorId").doesNotExist(),
			query.criteria("integratorId").equal("")
		);
		List<T> list =query.asList();
		return list;
	}

	@Override
	public List<T> getList(String exchangeTaskName) {
		return getList(exchangeTaskName,-1,-1);
	}

	@Override
	public List<T> getList(String exchangeTaskName,
			Map<String, Object> parameter) {
		return getList(exchangeTaskName,parameter,-1,-1);
	}

	@Override
	public List<T> getLogList( int offset, int limit) {
		Query<T> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.order("-$natural").offset(offset).limit(limit);
		return query.asList();
	}

	@Override
	public List<T> getList(String exchangeTaskName,  int offset, int limit) {
		Query<T> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
		query.filter("exchangeTaskName" ,exchangeTaskName);
//		query.or(
//				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
//				query.criteria("integratorId").doesNotExist(),
//				query.criteria("integratorId").equal("")
//			);
		query.order("-$natural").offset(offset).limit(limit);
		return query.asList();
	}
	/**
	 * 根据任务名，获得当月一号到今天的交换日志
	 * @param exchangeTaskName
	 * @return
	 */
//	public List<T> getCompleteList(String exchangeTaskName,String startTime,String endTime){
//		Query<T> query = mongoDataStore.createQuery(getClazz());
//		query.disableValidation();
//		query.filter("messageHerders.logType", "recv");
//		query.filter("messageHerders.receiveDate >=", startTime);
//		query.filter("messageHerders.receiveDate <=", endTime);
//		//下面的方法也可实现
//		query.and(query.criteria("messageHerders.logType").equal("recv")
//				,query.criteria("messageHerders.receiveDate").greaterThanOrEq(startTime)
//				,query.criteria("messageHerders.receiveDate").lessThanOrEq(endTime));
//		return query.asList();
//	}

	@Override
	public List<T> getList(String exchangeTaskName,
			Map<String, Object> parameter, int offset, int limit) {
		Query<T> query = mongoDataStore.createQuery(getClazz());
		query.disableValidation();
//		query.or(
//			query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
//			query.criteria("integratorId").doesNotExist(),
//			query.criteria("integratorId").equal("")
//		);
		if(StringUtility.isNotBlank(exchangeTaskName)){
			query.filter("exchangeTaskName", getPatternValue(exchangeTaskName));
		}
		if(parameter!=null){
			Iterator<Entry<String, Object>> it = parameter.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();	
				
				query.filter("messageHerders."+key.toString(), getPatternValue(value));	
			}	
		}
		if(offset >=0 && limit > 0){
			query.order("-$natural").offset(offset).limit(limit);
		}
		return query.asList();
	}
	
	protected Object getPatternValue(Object value) {
		//通过正则模糊查询
		if(value.toString().startsWith("^")){
			return Pattern.compile(value.toString());
		}
		return value;
	}

	@Override
	public List<T> getList(
			Map<String, Object> parameter, int offset, int limit) {
		return getList(null,parameter, offset,  limit);
	}
	
	@Override
	public List<T> getList(
			Map<String, Object> parameter) {
		return getList(null,parameter);
	}
}
