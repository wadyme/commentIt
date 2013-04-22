package cn.ls.integrator.core.manager;

import java.util.List;

import cn.ls.integrator.core.model.Connection;
import cn.ls.integrator.core.model.ConnectionType;

public interface ConnectionManager {

	/**
	 * 测试该链接是否可用
	 * step1:调用getConnParam,获得连接参数
	 * step2:测试链接是否成功
	 * @param connection
	 * @return
	 */
	boolean testConnect(Connection connection);
	
	/**
	 * 获得LI_HOME/db/conf目录中数据库类型列表，（做缓存）
	 * @return
	 */
	List<ConnectionType> getConnectionTypeList();
	
}
