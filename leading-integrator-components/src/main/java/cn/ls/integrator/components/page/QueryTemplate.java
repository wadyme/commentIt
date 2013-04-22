package cn.ls.integrator.components.page;

import java.util.List;

/**
 * 通用的查询工具类
 * 
 * @author wanl
 * 
 * @since 1.0
 */
public interface QueryTemplate {

	/**
	 * 执行查询并返回只有一个数据页
	 * 
	 * @param pageSize
	 *            number of records per page
	 * @param pageNo
	 *            index of current page,index begins with 0.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List query(QueryCallback callback, long pageSize, long pageNo);

	/**
	 * 查询记录总数搜索
	 * 
	 * @return
	 */
	public long countRows(QueryCallback callback);

}
