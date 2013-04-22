package cn.ls.integrator.components.manager;

public interface LastUpdateTimeManager {

	public Long get(String schema, String tableName);
	
	public void delete(String schema, String tableName);
}
