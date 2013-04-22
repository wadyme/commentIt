package cn.ls.integrator.components.manager;

import org.junit.Test;

import cn.ls.integrator.components.manager.impl.LastUpdateTimeManagerImpl;

public class TestLastUpdateTimeManager {

	@Test
	public void testGet(){
		LastUpdateTimeManager manager = LastUpdateTimeManagerImpl.getInstance();
		System.out.println(manager.get("EPMONDB", "PS_PTEINFO"));
	}
	
	@Test
	public void testDelete(){
		LastUpdateTimeManager manager = LastUpdateTimeManagerImpl.getInstance();
		manager.delete("EPMONDB", "PS_PTEINFO");
	}
	
}
