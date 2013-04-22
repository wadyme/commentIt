package cn.ls.integrator.components.manager;

import java.util.List;
import java.util.Map;

public interface ExchangeErrorLogManager {

	public boolean hasErrorLog(String scheduleName, String taskName, String sendNodeName, String recvNodeName, String logType);
	
	public List<Map<?,?>> getLogList(Map<String,Object> parameters, int offset, int limit);
	
	public void delete(String scheduleName, String taskName, String sendNodeName, String recvNodeName, String logType);
}
