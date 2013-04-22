package cn.ls.integrator.components.page;

import java.io.Serializable;

/**
 * <p>
 * QueryCallback类型返回自定义的Action类
 * </p>
 * 
 * @author wanl
 * 
 * @since 1.0
 */
public interface QueryCallback extends Serializable {
	/**
	 * 查询语句(e.g. sql for JDBC and queryString for Hibernate)
	 * 
	 * @return
	 */
	public String getQueryStatement();

	/**
	 * 查询语句的总记录数.
	 * 
	 * @return
	 */
	public String getCountRecordsQueryStatement();

	/**
	 * 排序信息查询语句中使用
	 * 
	 * @return
	 */
	public QueryOrder getQueryOrder();

	/**
	 * 设置排序信息查询语句中使用
	 * 
	 * @param order
	 */
	public void setQueryOrder(QueryOrder order);

}
