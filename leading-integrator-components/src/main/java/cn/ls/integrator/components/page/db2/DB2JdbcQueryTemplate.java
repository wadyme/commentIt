package cn.ls.integrator.components.page.db2;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * 
 * 针对DB2运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class DB2JdbcQueryTemplate extends JdbcQueryTemplate {
	public DB2JdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}

	protected String convertPaginationSql(String sql, long startIndex, long lastIndex) {
		return DB2QuerySqlUtils.convertPaginationSql(sql, startIndex, lastIndex);
	}
}
