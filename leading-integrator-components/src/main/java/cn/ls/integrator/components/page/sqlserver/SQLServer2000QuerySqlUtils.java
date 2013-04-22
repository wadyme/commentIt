package cn.ls.integrator.components.page.sqlserver;

import cn.ls.integrator.components.page.PaginationException;


public class SQLServer2000QuerySqlUtils {
	
	

	

	public static String convertCountSql(String querySql) {
		int orderByIndex = querySql.indexOf("order by");
		return "select count(*) count_ from ("+querySql.substring(0,orderByIndex)+") t ";
	}

	public static String convertPaginationSql(String sql, long startIndex,
			long lastIndex) {
		String lowerSql = sql.toLowerCase();
		StringBuilder paginationSql = new StringBuilder();
		int orderByIndex = lowerSql.indexOf("order by");
		if (orderByIndex <= 0) {
			throw new PaginationException("Clause 'ORDER BY' must be included in Sql");
		}
		int orderTypeIndex = lowerSql.indexOf(" asc");
		if(orderTypeIndex < 0){
			orderTypeIndex = lowerSql.indexOf(" desc");
		}
		String orderClause = "";
		if(orderTypeIndex > 0){
			orderClause = sql.substring(orderByIndex,orderTypeIndex);
		} else {
			orderClause = sql.substring(orderByIndex);
		}
		paginationSql.append("select * from ");
		paginationSql.append("(select top ");
		paginationSql.append(lastIndex - startIndex + 1);
		paginationSql.append(" * from ");
		paginationSql.append("(select top ");
		paginationSql.append(lastIndex);
		
		paginationSql.append(sql.substring(lowerSql.indexOf("select") + 6));
		paginationSql.append(") as r ");
		paginationSql.append(orderClause);
		paginationSql.append(" desc ) as t ");
		paginationSql.append(orderClause);
		return paginationSql.toString();
	}

}
