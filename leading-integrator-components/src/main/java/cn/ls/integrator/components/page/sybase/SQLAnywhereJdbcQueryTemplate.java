package cn.ls.integrator.components.page.sybase;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.support.JdbcQueryTemplate;

/**
 * The type of <code>JdbcQueryTemplate</code> to run a pagination based query against 
 * Sybase SQL Anywhere
 * 
 * @author wanl
 *
 * @since 1.0.1
 *
 */
public class SQLAnywhereJdbcQueryTemplate extends JdbcQueryTemplate {
	
	public SQLAnywhereJdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}
	
	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		int startOfSelect = sql.toLowerCase().indexOf("select");
		String topStartAt = getTopStartAt(pageNo, pageSize);
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 )
				.append( sql.substring(0, startOfSelect) );
		pagingSelect.append(" select ").append(topStartAt).append(" ");
		String mainQuery = sql.substring( startOfSelect + 6 );
		if ( hasDistinct(sql) ) {
			pagingSelect.append(" temp_.* from (select ") //add another (inner) nested select
				.append( mainQuery ) //add the main query
				.append(" ) temp_"); //close off the inner nested select
		}
		else {
			pagingSelect.append( mainQuery ); //add the main query
		}		
		return pagingSelect.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}
	
	/**
	 * Render the <tt>TOP ... START AT ... ,</tt> 
	 * bit, that goes in the select list
	 */
	private String getTopStartAt(long pageNo, long pageSize) {		
		return "TOP " + pageSize + " START AT " + (pageNo*pageSize + 1);		
	}
	
	
	protected String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+") as count_";
	}

}
