package cn.ls.integrator.components.manager;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import cn.ls.integrator.components.manager.impl.MessageLogManagerImpl;
import cn.ls.integrator.core.log.message.MessageLog;

public class TestMessageLogManager {
	@Test
	public void getList(){
		MessageLogManagerImpl manager = new MessageLogManagerImpl();
		List<MessageLog> list = manager.getList("AM_AT_GAT.GAT_Dsjkzb_MN_MM_SH");
		System.out.println(list.size());
	}
	@Test
	public void testPatter(){
		System.out.println(Pattern.compile("value",Pattern.CASE_INSENSITIVE));
	}

}
