package cn.ls.integrator.components.db.dialect;


import cn.ls.integrator.components.page.oracle.OracleQuerySqlUtils;

public class OracleDialect extends Dialect{

	@Override
	public String paginationSQL(long startIndex, long lastIndex, String sql) {
		return OracleQuerySqlUtils.convertPaginationSql(sql, startIndex, lastIndex);
	}
	
	/**
	 * 
	 * @Title: buildSelectSql
	 * @Description: 组装查询sql
	 * @param tableList
	 * @param itemList
	 * @param whereClause
	 * @param orderByClause
	 * @return
	 * @return String 返回类型
	 * @throws
	 */
	@Override
	public String buildSelectSql(String taskSQL, String itemList,
			String whereClause, String orderByClause, String timestampField, String timestampOrder) {

		String sql = super.buildSelectSql(taskSQL, itemList, whereClause, orderByClause, timestampField, timestampOrder);
		log.info("sql :" + sql); 
		return sql;
	}
	@Override
	public String buildCountSql(String querySql) {
		String sql = "select count(*) from (" + querySql + ")  count_";
		log.info("sql :" + sql);
		return sql;
	}

}
