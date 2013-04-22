package cn.ls.integrator.components.page.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.ls.integrator.components.page.QueryOrder;

/**
 * Abstract class which implements some methods of <code>JdbcQueryCallback</code>
 * @author wanl
 *
 * @since 1.0
 */
public abstract class AbstractJdbcQueryCallback implements JdbcQueryCallback {
	private static final long serialVersionUID = 1L;
	
    private String queryStmt;
    private String countRowsStmt;
    private QueryOrder order;
    
    /**
     * 
     * @param queryStmt The SQL statement to query
     */
    public AbstractJdbcQueryCallback(String queryStmt) {
        this.queryStmt = queryStmt;
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement 
     */
    public AbstractJdbcQueryCallback(String queryStmt, String orderBy) {
        this.queryStmt = queryStmt;
        this.order = new QueryOrderImp(orderBy);
    }
    
    /**
     * 
     * @param queryStmt The SQL to query
     * @param orderBy column that will be added to the 'order by' clause of SQL statement
     * @param ascending if sorted by ascending order
     */
    public AbstractJdbcQueryCallback(String queryStmt, String orderBy, boolean ascending) {
        this(queryStmt);
        this.order = new QueryOrderImp(orderBy, ascending);
    }
    
  
    public void setQueryOrder(QueryOrder aOrder) {
        this.order = aOrder;
    }
    
    /**
     * Set sql statement to count total records
     * @param countRowsStmt
     */
    public void setCountRowsStmt(String countRowsStmt) {
        this.countRowsStmt = countRowsStmt;
    }

    public String getQueryStatement() {
        return this.queryStmt;
    }
    
  
    public String getCountRecordsQueryStatement() {
    	return this.countRowsStmt;
//        if (this.countRowsStmt != null)
//            return this.countRowsStmt;
//        else 
//            return " select count(*) from ("+this.queryStmt+") as _hdp_query";
    }
    
  
    public QueryOrder getQueryOrder() {
        return this.order;
    }
    
    private class QueryOrderImp implements QueryOrder { 
    	private static final long serialVersionUID = 1L;
    	
        private String orderBy;
        private boolean ascending = true;
        
        public QueryOrderImp(String sOrderBy) {
            this.orderBy = sOrderBy;
        }
        
        public QueryOrderImp(String sOrderBy, boolean bAscending) {
            this.orderBy = sOrderBy;
            this.ascending = bAscending;
        }
        
        public String getOrderBy() {
           return this.orderBy;
        }
	    
    	public boolean isAscending() {
    	    return this.ascending;
    	}
    	   
    	public String getOrderByClause() {
    	    String orderByClause = " order by "+this.orderBy;
    	    orderByClause += (this.ascending ? " asc " : " desc ");
    	    return orderByClause;
    	}
    }
    
  
    abstract public void setValues(PreparedStatement ps) throws SQLException;
    
 
    abstract public Object processRow(ResultSet rs) throws SQLException;
    
}
