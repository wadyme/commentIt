package cn.ls.integrator.components.db.dialect;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.ls.integrator.components.JdbcSourceComponent;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.utils.StringUtility;

public abstract class Dialect {
	
	public static Logger log = Logger.getLogger(JdbcSourceComponent.class);
	/**
	 * 条件的属性名
	 */
	public static final String PARAM_WHERE_CLAUSE = "where_clause";

	/**
	 * 排序的属性名
	 */
	public static final String PARAM_ORDER_BY_CLAUSE = "order_by_clause";

	/**
	 * 替换表名
	 */
	public static final String REPLACE_TABLE = "<table_list>";
	/**
	 * 替换字段名
	 */
	public static final String REPLACE_FIELDS = "<item_list>";
	/**
	 * 替换条件
	 */
	public static final String REPLACE_WHERE_CLAUSE = "<where_clause>";
	/**
	 * 替换排序
	 */
	public static final String REPLACE_ORDER_BY_CLAUSE = "<order_by_clause>";
	/**
	 * 查询的语句
	 */
	public final static String SELECT_QUERY_SQL = "select " + REPLACE_FIELDS
			+ " from " + REPLACE_TABLE + " where " + REPLACE_WHERE_CLAUSE
			+ "  order by " + REPLACE_ORDER_BY_CLAUSE;

	/**
	 * 查询的语句
	 */
	public final static String MAX_QUERY_SQL = "select " + REPLACE_FIELDS
			+ " from " + REPLACE_TABLE;
	
	/**
	 * 限制关系
	 */
	public final static String relationFalg = " >= ";
	
	public abstract String paginationSQL(long startIndex, long lastIndex, String sql);
	
	public String buildSelectSql(String taskSQL, String itemList,
			String whereClause, String orderByClause, String timestampField, String timestampOrder){
		String sql = "";
		if (!StringUtils.hasLength(itemList)) {
			itemList = "*";
		}
		if (StringUtils.hasLength(taskSQL)) {
			sql = SELECT_QUERY_SQL.replaceFirst(REPLACE_FIELDS, itemList)
					.replaceFirst(REPLACE_TABLE, taskSQL + " t ");
		}
		// 追加的限制条件
		String appendWhereClause = " ";
		if (!StringUtils.hasLength(whereClause)) {
			//判断时间戳是否为空
			if(StringUtility.isBlank(timestampField)){
				appendWhereClause = null;
			}
			//TODO 多时间戳
			else if(timestampField.contains(",")){
				appendWhereClause = " ("  + getCaseWhenString(timestampField) + " )" + relationFalg + " ? ";
			}else{
				appendWhereClause = " (" + timestampField + relationFalg
					+ "  ?  )  ";
			}
		} else {
			appendWhereClause = whereClause;
		}
		//如果时间戳为空，并且没有where子句，则去掉where子句
		if(StringUtility.isBlank(timestampField) && StringUtility.isBlank(appendWhereClause)){
			sql = sql.replace("where " + REPLACE_WHERE_CLAUSE, " ");
		}else{
			sql = sql.replaceFirst(REPLACE_WHERE_CLAUSE, appendWhereClause);
		}
		if (StringUtils.hasLength(orderByClause)) {
			sql = sql.replaceFirst(REPLACE_ORDER_BY_CLAUSE, orderByClause);
		} else {
			if(StringUtility.isNotBlank(timestampField)){
				//TODO 多时间戳
				sql = sql.replaceFirst(REPLACE_ORDER_BY_CLAUSE, getOrderByString(timestampField, timestampOrder));
			}
		}
		return sql;
	}
	
	private String getOrderByString(String timestampField,String timestampOrder) {
		StringBuilder orderStr = new StringBuilder();
		if(timestampField.contains(",")){
			orderStr.append(getCaseWhenString(timestampField));
			orderStr.append(timestampOrder);
		}else{
			//timestampField + " " + timestampOrder
			orderStr.append(timestampField);
			orderStr.append(" ");
			orderStr.append(timestampOrder);
		}
		return orderStr.toString();
	}

	private Object getCaseWhenString(String timestampField) {
		StringBuilder caseString = new StringBuilder();
		String[] arr = timestampField.split(",");
		if(arr.length!=2){
			throw new IntegratorException("配置的时间戳字段不可超过两个");
		}
		caseString.append("CASE WHEN ( ");
		caseString.append(arr[1] );
		caseString.append(" IS NOT NULL AND ");
		caseString.append(arr[1]);
		caseString.append(" > ");
		caseString.append(arr[0]);
		caseString.append(" ) THEN ");
		caseString.append(arr[1]);
		caseString.append(" ELSE ");
		caseString.append(arr[0]);
		caseString.append(" END ");
		return caseString.toString();
	}

	public String buildCountSql(String querySql){
		String sql = "select count(*) from (" + querySql + ") as count_";
		log.info("sql :" + sql);
		return sql;
	}
}
