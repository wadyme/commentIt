package cn.ls.integrator.components.page.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A subclass of <code>AbstractJdbcQueryCallback</code> providing empty implementation of
 * <code>setValues(PreparedStatement ps)</code>. It suits for query scenario where no parameters are
 * required to bind with. 
 * 
 * @author wanl
 *
 * @since 1.0
 */
public abstract class AbstractNoParamBindJdbcQueryCallback extends AbstractJdbcQueryCallback {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5094913875682655088L;


	/**
	 * 
	 * @param queryStmt The SQL statement to query
	 */
    public AbstractNoParamBindJdbcQueryCallback(String queryStmt) {
        super(queryStmt);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     */
    public AbstractNoParamBindJdbcQueryCallback(String queryStmt, String orderBy) {
        super(queryStmt, orderBy);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     * @param ascending if sorted by ascending order
     */
    public AbstractNoParamBindJdbcQueryCallback(String queryStmt, String orderBy, boolean ascending) {
        super(queryStmt, orderBy, ascending);
    }
    
    /**
     * Do nothing
     */
    public void setValues(PreparedStatement ps) throws SQLException {
        
    }
    
   
    abstract public Object processRow(ResultSet rs) throws SQLException;
    
}
