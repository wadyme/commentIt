package cn.ls.integrator.core.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.ls.integrator.components.manager.FileExchangeLogManager;
import cn.ls.integrator.components.manager.impl.FileExchangeLogManagerImpl;
import cn.ls.integrator.core.log.message.FileExchangeLog;
import cn.ls.integrator.server.service.ScheduleService;

public class TestScheduleService {
	@Test
	public void test(){
		FileExchangeLogManager manager = FileExchangeLogManagerImpl.getInstance();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sendNodeName", "152");
		List<FileExchangeLog> result = manager.getList(map, -1, -1);
		System.out.println(result.size());
	}
	@Test
	public void testservice(){
		ScheduleService service = new ScheduleService();
		String size = service.getMessageLogByTaskName("EPM.WF_WATER_OUTPUT_POLLUTANT_HOUR_DATA","国控污染源华南大区",
				"2012-05-24 00:00:00", "2012-07-24 23:59:59");
		System.out.println(size);
	}

}
