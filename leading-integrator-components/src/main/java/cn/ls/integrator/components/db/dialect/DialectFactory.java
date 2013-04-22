package cn.ls.integrator.components.db.dialect;

import java.util.HashMap;
import java.util.Map;

import cn.ls.integrator.core.exception.IntegratorException;

public class DialectFactory {
	public static final String DB2="DB2";
	public static final String ORACLE="ORACLE";
	public static final String SQLSERVER="SQLSERVER";
	public static final String SQLSERVER2000="SQLSERVER2000";
	private static final Map<String, String> MAPPERS = new HashMap<String, String>();
	static {
		MAPPERS.put(DB2, "cn.ls.integrator.components.db.dialect.DB2Dialect");
		MAPPERS.put(ORACLE, "cn.ls.integrator.components.db.dialect.OracleDialect");
		MAPPERS.put(SQLSERVER, "cn.ls.integrator.components.db.dialect.SqlServer2005Dialect");
		MAPPERS.put(SQLSERVER2000, "cn.ls.integrator.components.db.dialect.SqlServer2000Dialect");
	}
	
	public static Dialect buildDialect(String dialectName) {
		String dialectClassName = MAPPERS.get(dialectName.toUpperCase());
		try {
			return (Dialect) Class.forName(dialectClassName).newInstance();
		} catch (ClassNotFoundException cnfe) {
			throw new IntegratorException("Dialect class not found: " + dialectName);
		} catch (Exception e) {
			throw new IntegratorException("Could not instantiate dialect class", e);
		}
	}
	
}
