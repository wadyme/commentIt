package cn.ls.integrator.components.page.postgresql;

import javax.sql.DataSource;
import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * 
 * 针对 PostgreSQL运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class PostgreSQLQueryTemplate extends JdbcQueryTemplate {
	
	public PostgreSQLQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}
	

	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		return sql + " LIMIT " + pageSize + " OFFSET " + (pageNo*pageSize);
	}
	

	protected String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+") as _hdp_query";
	}

}
