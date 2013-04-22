package cn.ls.integrator.core.server;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.ls.integrator.server.service.ScheduleService;

public class TestService {

	@Test
	public void testTestCase() {
		ScheduleService service = new ScheduleService();
//		String string = service.getFileExchangeNumByScheduleName("EIA_FILE", "建管", "2012-08-27 10:16:56", "2012-08-27 18:16:56");
//		String string = service.getFileExchangeNumByScheduleName("ESTA_FILE", "建管及环统", "2012-08-16 00:00:00", "2012-08-16 23:59:59");//
		String string = service.getMessageLogByTaskName("EPM.PS_BASE_INFO","重庆","2012-08-16 00:00:00","2012-08-16 23:59:59");
		System.out.println(string);
	}

}
