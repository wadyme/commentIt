package cn.ls.integrator.components.page.db2;

/**
 * 构造DB2数据库的sql语句
 * 
 * @author wanl
 * 
 */
public class DB2QuerySqlUtils {

	/**
	 * 构造DB2数据库的分页语句
	 * 
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static String convertPaginationSql(String sql, long startIndex, long lastIndex) {
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuilder pagingSelect = new StringBuilder(sql.length() + 100)
				.append(sql.substring(0, startOfSelect)) // add the comment
				.append("select * from ( select ") // nest the main query in an
													// outer select
				.append(getRowNumber(sql)); // add the rownnumber bit into the
											// outer query select list

		if (!hasDistinct(sql)) {
			pagingSelect.append(" row_.* from ( ") // add another (inner) nested
													// select
					.append(sql.substring(startOfSelect)) // add the main query
					.append(" ) as row_"); // close off the inner nested select
		} else {
			pagingSelect.append(" row_.* from ( ") // add another (inner) nested
													// select
					.append(sql.substring(startOfSelect + 6))// add the main
																// query
					.append(" ) as row_"); // close off the inner nested select
		}
		pagingSelect.append(" ) as temp_ where num ");
		pagingSelect.append("between " + startIndex + "  and "
				+ lastIndex);

		return pagingSelect.toString();
	}

	public static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct") >= 0;
	}

	public static String getRowNumber(String sql) {
		StringBuilder rownumber = new StringBuilder(50)
				.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if (orderByIndex > 0 && !hasDistinct(sql)) {
			rownumber.append(sql.substring(orderByIndex));
		}

		rownumber.append(") as num,");

		return rownumber.toString();
	}
}
