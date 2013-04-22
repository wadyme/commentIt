package cn.ls.integrator.components.page.mysql;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * 
 * 针对 MySql运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class MySqlJdbcQueryTemplate extends JdbcQueryTemplate {
	
	public MySqlJdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}
	

	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		return sql + " LIMIT " + (pageNo*pageSize) + ", " + pageSize;
	}
	
	protected String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+") as _hdp_query";
	}

}
