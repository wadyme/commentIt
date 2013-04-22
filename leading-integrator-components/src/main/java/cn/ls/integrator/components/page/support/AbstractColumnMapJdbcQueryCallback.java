package cn.ls.integrator.components.page.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A subclass of <code>AbstractJdbcQueryCallback</code> providing default implementation of
 * <code>processRow(ResultSet rs)</code> method where it creates a <code>java.util.Map</code> 
 * for each row, representing all columns as key-value pairs: one entry for each column, 
 * with the column name (converted to lower case) as key.
 * 
 * @author wanl
 *
 * @since 1.0
 */
public abstract class AbstractColumnMapJdbcQueryCallback extends AbstractJdbcQueryCallback {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4478435744911126543L;


	/**
	 * 
	 * @param queryStmt The SQL statement to query
	 */
    public AbstractColumnMapJdbcQueryCallback(String queryStmt) {
        super(queryStmt);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement 
     */
    public AbstractColumnMapJdbcQueryCallback(String queryStmt, String orderBy) {
        super(queryStmt, orderBy);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     * @param ascending if sorted by ascending order
     */
    public AbstractColumnMapJdbcQueryCallback(String queryStmt, String orderBy, boolean ascending) {
        super(queryStmt, orderBy, ascending);
    }
    
    /**
     * Create a <code>java.util.Map</code> 
     * for each row, representing all columns as key-value pairs: one entry for each column, 
     * with the column name (converted to lower case) as key.
     * 
     * @param rs
     * @return a instance of <code>java.util.Map</code> representing one row of data
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
	public Object processRow(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();	        
        Map map = new HashMap();
        for (int i=1;i<=metaData.getColumnCount();i++) {
            map.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
        }	            	      
        return map;
    }
    
  
    abstract public void setValues(PreparedStatement ps) throws SQLException;

}
