package cn.ls.integrator.components.page.oracle;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * 
 * 针对 Oracle运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class OracleJdbcQueryTemplate extends JdbcQueryTemplate {
	
	public OracleJdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}
	
	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		return OracleQuerySqlUtils.convertPaginationSql(sql, pageNo, pageSize);
	}
		
}
