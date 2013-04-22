package cn.ls.integrator.components.page.hsqldb;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * 
 * 针对 HSQLDB运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class HSQLDBJdbcQueryTemplate extends JdbcQueryTemplate {

	public HSQLDBJdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}

	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		return HSQLDBQuerySqlUtils.convertPaginationSql(sql, pageNo, pageSize);
	}

}
