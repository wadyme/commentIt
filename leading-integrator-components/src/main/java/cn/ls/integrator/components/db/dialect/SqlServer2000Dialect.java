package cn.ls.integrator.components.db.dialect;


import cn.ls.integrator.components.page.sqlserver.SQLServer2000QuerySqlUtils;

public class SqlServer2000Dialect extends Dialect{

	@Override
	public String paginationSQL(long startIndex, long lastIndex, String sql) {
		return SQLServer2000QuerySqlUtils.convertPaginationSql(sql, startIndex, lastIndex);
	}
	
	@Override
	public String buildCountSql(String querySql) {
		return SQLServer2000QuerySqlUtils.convertCountSql(querySql);
	}


}
