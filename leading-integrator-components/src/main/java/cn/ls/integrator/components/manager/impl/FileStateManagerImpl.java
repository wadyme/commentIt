package cn.ls.integrator.components.manager.impl;

import cn.ls.integrator.components.manager.FileStateManager;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.log.message.FileState;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class FileStateManagerImpl extends
		AbstractMessageLogManagerImpl<FileState> implements FileStateManager {

	private static FileStateManager instance;
	
	private FileStateManagerImpl(){}
	
	public static FileStateManager getInstance(){
		if(instance == null){
			instance = new FileStateManagerImpl();
		}
		return instance;
	} 
	
	public void delete(String scheduleName, String taskName) {
		if (StringUtility.isBlank(scheduleName) || StringUtility.isBlank(taskName)) {
			throw new IntegratorException("scheduleName and taskName can not blank");
		}
		Query<FileState> query = mongoDataStore.createQuery(FileState.class);
		query.disableValidation();
		query.or(
				query.criteria("integratorId").equal(UniquelyIdentifies.getId()),
				query.criteria("integratorId").doesNotExist(),
				query.criteria("integratorId").equal("")
			);
		query.filter("scheduleName", scheduleName);
		query.filter("taskName", taskName);
		mongoDataStore.delete(query);
	}

	@Override
	public Class<FileState> getClazz() {
		return FileState.class;
	}
}
