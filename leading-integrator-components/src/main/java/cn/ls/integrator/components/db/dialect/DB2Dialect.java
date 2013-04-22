package cn.ls.integrator.components.db.dialect;


import cn.ls.integrator.components.page.db2.DB2QuerySqlUtils;

public class DB2Dialect extends Dialect{
	

	@Override
	public String buildSelectSql(String taskSQL, String itemList,
			String whereClause, String orderByClause, String timestampField,
			String timestampOrder) {
		String sql = super.buildSelectSql(taskSQL, itemList, whereClause, orderByClause, timestampField, timestampOrder);
		log.info("sql :" + sql); 
		return sql;
	}

	@Override
	public String paginationSQL(long startIndex, long lastIndex, String sql) {
		return DB2QuerySqlUtils.convertPaginationSql(sql, startIndex, lastIndex);
	}

}
