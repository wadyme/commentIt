package cn.ls.integrator.components.page.sqlserver;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.StandardJdbcQueryTemplate;

/**
 * 
 * 针对 SQLServer2005运行一个分页的查询
 * 
 * @author wanl
 * @since 1.0
 */
public class SQLServer2005JdbcQueryTemplate extends StandardJdbcQueryTemplate {

	public SQLServer2005JdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}

}
