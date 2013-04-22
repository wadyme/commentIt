package cn.ls.integrator.components.page.hsqldb;

/**
 * 构造HSQLDB数据库的sql语句
 * 
 * @author wanl
 * 
 */
public class HSQLDBQuerySqlUtils {

	public static String convertPaginationSql(String sql, long pageNo,
			long pageSize) {
		return new StringBuilder(sql.length() + 10)
				.append(sql)
				.insert(sql.toLowerCase().indexOf("select") + 6,
						" limit " + (pageNo * pageSize) + " " + pageSize)
				.toString();
	}
}
