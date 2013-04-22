package cn.ls.intergrator.manager;

import java.util.List;
import java.util.Map;

public interface MessageLogManager {
	 /**
     * 获取全部日志列表
     * @param taskName
     * @return
     */
    List<Map<?,?>> getLogList();
    /**
     * 获取全部日志列表
     * @param taskName
     * @return
     */
    List<Map<?,?>> getLogList( int offset, int limit);
    /**
     * 获取日志列表
     * @param taskName 任务名称  交换表的时候是表名
     * @return
     */
    List<Map<?,?>> getList(String taskName);
    /**
     * 获取日志列表
     * @param taskName 任务名称  交换表的时候是表名
     * @return
     */
    List<Map<?,?>> getList(String taskName, int offset, int limit);
    /**
     * 获取日志列表
     * @param taskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
    List<Map<?,?>> getList(String taskName,Map<String,Object> parameter);
    /**
     * 获取日志列表
     * @param taskName 任务名称  交换表的时候是表名
     * @param parameter 参数
     * @return
     */
    List<Map<?,?>> getList(String taskName,Map<String,Object> parameter, int offset, int limit);
 
    
}
