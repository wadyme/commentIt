package cn.ls.integrator.components.page.support;

import javax.sql.DataSource;

import cn.ls.integrator.components.page.PaginationException;

/**
 * The type of <code>JdbcQueryTemplate</code> to run a pagination based query against database which supports
 * standard syntax (ROW_NUMBER() OVER ) for pagination
 * @author wanl
 *
 * @since 1.0
 */
public class StandardJdbcQueryTemplate extends JdbcQueryTemplate {
	public StandardJdbcQueryTemplate(DataSource dataSource) {
		super(dataSource);
	}
	
	
	protected String convertPaginationSql(String sql, long pageNo, long pageSize) {
		//Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> 
		// bit, that goes in the select list
		int orderByIndex = sql.toLowerCase().indexOf("order by");
		if (orderByIndex <= 0) {
//			throw new PaginationException("Clause 'ORDER BY' must be included in Sql. Use AbstractJdbcQueryCallback (or its subclasses)'s construts " +
//					"to add 'ORDER BY' clause, rather than append it to SQL directly.");
			throw new PaginationException("Clause 'ORDER BY' must be included in Sql");
		}
		StringBuilder rownumber = new StringBuilder(50).append("ROW_NUMBER() OVER(");
		rownumber.append( sql.substring(orderByIndex) );
		rownumber.append(") as num,");
		if ( hasDistinct(sql) ) {
			throw new PaginationException("'DISTINCT' is not supported for pagination query by this database");
		}
		
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 )
				.append( sql.substring(0, startOfSelect) ) //add the comment
				.append("select * from ( select ") //nest the main query in an outer select
				.append(rownumber.toString()); //add the rownnumber bit into the outer query select list

		
		pagingSelect.append( sql.substring( startOfSelect + 6,  orderByIndex) ); //add the main query without 'ORDER BY' clause
			
		pagingSelect.append(" ) as temp_ where rownumber_ ");
		pagingSelect.append("between "+(pageNo*pageSize + 1) + "  and "+(pageNo+1)*pageSize);
		
		return pagingSelect.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}
	
	
	protected String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+") as count_";
	}
		
}
