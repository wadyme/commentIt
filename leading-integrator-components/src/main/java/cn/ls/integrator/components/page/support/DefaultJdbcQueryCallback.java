package cn.ls.integrator.components.page.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A default implementing class of <code>JdbcQueryCallback</code> providing:
 * <li>
 * Default implementation of
 * <code>processRow(ResultSet rs)</code> method where it creates a <code>java.util.Map</code> 
 * for each row, representing all columns as key-value pairs: one entry for each column, 
 * with the column name (converted to lower case) as key.
 * </li>
 * <li>
 * Empty implementation of
 * <code>setValues(PreparedStatement ps)</code>
 * </li>
 * 
 * @author wanl
 *
 * @since 1.0
 */
public class DefaultJdbcQueryCallback extends AbstractColumnMapJdbcQueryCallback {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1488117012828838928L;

	/**
	 * 
	 * @param queryStmt The SQL statement to query
	 */
    public DefaultJdbcQueryCallback(String queryStmt) {
        super(queryStmt);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     */
    public DefaultJdbcQueryCallback(String queryStmt, String orderBy) {
        super(queryStmt, orderBy);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     * @param ascending if sorted by ascending order
     */
    public DefaultJdbcQueryCallback(String queryStmt, String orderBy, boolean ascending) {
        super(queryStmt, orderBy, ascending);
    }
    
    public void setValues(PreparedStatement ps) throws SQLException {
        
    }
    
}
