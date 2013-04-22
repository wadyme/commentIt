package cn.ls.integrator.components.page.oracle;

public class OracleQuerySqlUtils {

	
	public static String convertPaginationSql(String sql, long startIndex, long lastIndex) {
		String str = " select * from  "+
                    "   (select a.*,rownum num from ( "+sql+" ) a   ) b "+
                    " where b.num between "+startIndex + "and " + lastIndex;
		return str;
	}
}
