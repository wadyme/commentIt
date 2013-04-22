package cn.ls.integrator.components.manager;

import java.util.Map;
import java.util.List;

public interface MessageLogManager<T> {
	 /**
     * 获取全部日志列表
     * @param exchangeTaskName
     * @return
     */
    List<T> getLogList();
    /**
     * 获取全部日志列表
     * @param exchangeTaskName
     * @return
     */
    List<T> getLogList( int offset, int limit);
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @return
     */
    List<T> getList(String exchangeTaskName);
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @return
     */
    List<T> getList(String exchangeTaskName, int offset, int limit);
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
    List<T> getList(String exchangeTaskName,Map<String,Object> parameter);
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
    List<T> getList(String exchangeTaskName,Map<String,Object> parameter, int offset, int limit);
    
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
    List<T> getList(Map<String,Object> parameter, int offset, int limit);
    /**
     * 获取日志列表
     * @param exchangeTaskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
	public List<T> getList(Map<String, Object> parameter) ;
	/**
	 * 根据任务名，获得当月一号到今天的交换日志
	 * @param exchangeTaskName
	 * @return
	 */
//	public List<T> getCompleteList(String exchangeTaskName,String startTime,String endTime);
	
}
