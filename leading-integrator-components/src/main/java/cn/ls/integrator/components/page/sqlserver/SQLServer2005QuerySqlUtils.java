package cn.ls.integrator.components.page.sqlserver;

import cn.ls.integrator.components.page.PaginationException;

public class SQLServer2005QuerySqlUtils {
	public static String convertPaginationSql(String sql, long startIndex, long lastIndex) {

		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		pagingSelect.append("select * from (");	
		pagingSelect.append(sql);
		pagingSelect.append(" ) as temp_ where num ");
		pagingSelect.append("between "+startIndex + "  and "+lastIndex);
		
		return pagingSelect.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}
	

	public static String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+") as count_";
	}
	
	public static String convertPaginationSql(String sql) {
		//Render the <tt>rownumber() over ( .... ) as num,</tt> 
		// bit, that goes in the select list
		int orderByIndex = sql.toLowerCase().indexOf("order by");
		if (orderByIndex <= 0) {
//			throw new PaginationException("Clause 'ORDER BY' must be included in Sql. Use AbstractJdbcQueryCallback (or its subclasses)'s construts " +
//					"to add 'ORDER BY' clause, rather than append it to SQL directly.");
			throw new PaginationException("Clause 'ORDER BY' must be included in Sql");
		}
		StringBuilder rownumber = new StringBuilder(50).append("ROW_NUMBER() OVER(");
		rownumber.append( sql.substring(orderByIndex) );
		rownumber.append(") as NUM,");
		if ( hasDistinct(sql) ) {
			throw new PaginationException("'DISTINCT' is not supported for pagination query by this database");
		}
		
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 )
				.append( sql.substring(0, startOfSelect) ) //add the comment
				.append("select ") //nest the main query in an outer select
				.append(rownumber.toString()); //add the rownnumber bit into the outer query select list

		
		pagingSelect.append( sql.substring( startOfSelect + 6,  orderByIndex) ); //add the main query without 'ORDER BY' clause
			
		pagingSelect.append("  ");
		
		
		return pagingSelect.toString();
	}
	



}
