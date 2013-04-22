package cn.ls.integrator.core.manager;

import java.util.List;

import org.junit.Test;

import cn.ls.integrator.core.manager.impl.ConnectionManagerImpl;
import cn.ls.integrator.core.model.ConnectionType;

public class ConnectionManagerImplTest {

	@Test
	public void testGetConnectionTypeList(){
		ConnectionManager manager = ConnectionManagerImpl.getInstance();
		List<ConnectionType> list = manager.getConnectionTypeList();
		for(ConnectionType type : list){
			System.out.println(type.getName() + "  " + type.getDriver() + "  " + type.getUrl());
		}
	}
}
