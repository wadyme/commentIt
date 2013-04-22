package cn.ls.integrator.components.page;

import java.io.Serializable;

/**
 * 定义相关的排序信息来生成查询语句
 * 
 * @author wanl
 * 
 * @since 1.0
 */
public interface QueryOrder extends Serializable {
	/**
	 * 获得查询结果排序的列
	 * 
	 * @return
	 */
	public String getOrderBy();

	/**
	 * 是否进行排序
	 * 
	 * @return
	 */
	public boolean isAscending();

	/**
	 * 获取 'order by' 子句中的查询语句
	 * 
	 * @return
	 */
	public String getOrderByClause();

}
