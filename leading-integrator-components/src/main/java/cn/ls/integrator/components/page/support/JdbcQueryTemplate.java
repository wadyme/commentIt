package cn.ls.integrator.components.page.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ls.integrator.components.page.PaginationException;
import cn.ls.integrator.components.page.QueryCallback;
import cn.ls.integrator.components.page.QueryOrder;
import cn.ls.integrator.components.page.QueryTemplate;

/**
 * 
 * 该类型的QueryTemplate运行基于对JDBC的分页查询。
 * @author wanl
 *
 * @since 1.0
 */
abstract public class JdbcQueryTemplate implements QueryTemplate 
{
	private final static Log log = LogFactory.getLog(JdbcQueryTemplate.class);
	
	private DataSource dataSource;
	
	/**
	 * 
	 * @param dataSource DataSource that the query is executed against
	 */
	public JdbcQueryTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

	@SuppressWarnings("unchecked")
	public List query(QueryCallback callback, long pageSize, long pageNo) 
	{
		if (log.isDebugEnabled()) {
			log.debug("JdbcQueryTemplate will try to execute pagination query. ");
			log.debug("page size is " + pageSize);
			log.debug("page No is " + pageNo);
		}	
		
		if (!(callback instanceof JdbcQueryCallback))
			throw new PaginationException("'JdbcQueryTemplate' only accepts type of 'JdbcQueryCallback' as argument.");
	   
		JdbcQueryCallback jdbcCallback = (JdbcQueryCallback)callback;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List dataList = new ArrayList();
		try	{
			con = this.dataSource.getConnection();
			String querySql = jdbcCallback.getQueryStatement();
			QueryOrder order = callback.getQueryOrder();
			if (order != null)
				querySql += (" "+order.getOrderByClause());     
			if (log.isDebugEnabled())
				log.debug("Query sql before paginal treatment: " + querySql);
			querySql = convertPaginationSql(querySql, pageNo, pageSize);
			if (log.isDebugEnabled())
				log.debug("Query sql after paginal treatment: " + querySql);
			ps = con.prepareStatement(querySql);
			jdbcCallback.setValues(ps);     
			rs = ps.executeQuery();
			while (rs.next()) {
				dataList.add(jdbcCallback.processRow(rs));
			}
		
			return dataList;
		} catch (PaginationException pe) {
			throw pe;
		} catch (Exception e) {
			throw new PaginationException("Pagination Query execution failed.", e);
		} finally {
			this.close(rs, ps, con);
		}
	}
    
	
	public long countRows(QueryCallback callback)
	{
		if (log.isDebugEnabled())
			log.debug("JdbcQueryTemplate will try to count total records");
		
		JdbcQueryCallback jdbcCallback = (JdbcQueryCallback)callback;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsCount = 0;
		try	{
			con = this.dataSource.getConnection();
			String countRecordsQueryStmt = jdbcCallback.getCountRecordsQueryStatement();
			if (countRecordsQueryStmt == null)
				countRecordsQueryStmt = this.convertCountSql(jdbcCallback.getQueryStatement());
			if (log.isDebugEnabled())
				log.debug("Sql to query all records count: " + countRecordsQueryStmt);
			
			ps = con.prepareStatement(countRecordsQueryStmt);
			jdbcCallback.setValues(ps);
			rs = ps.executeQuery();
			if (rs.next()) {
				rowsCount = rs.getInt(1);
			}
			if (log.isDebugEnabled())
				log.debug("all records count:: " + rowsCount);           
            return rowsCount;
		} catch (PaginationException pe) {
			throw pe;
		} catch (Exception e) {
          	throw new PaginationException("Pagination Count Failed.", e);
		} finally {
			this.close(rs, ps, con);
		}
	}
   
	private void close(ResultSet rs, PreparedStatement ps, Connection con) {
		try	{
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		} catch (Exception e) {
			throw new PaginationException("Failed to release connection resource.", e);
		}
	}
   
	/**
	 * Translate original query sql to pagination based sql
	 * @param sql original sql
	 * @param pageNo current page number (start from 0)
	 * @param pageSize how many records is displayed in one page
	 * @return return pagination based sql
	 */
	abstract protected String convertPaginationSql(String sql, long pageNo, long pageSize);
	
	/**
	 * Translate original query sql to the sql which can be used to query the count of total records.
	 * If this default translation does not work in one particular database, its corresponding subclass
	 * of <code>JdbcQueryTemplate</code> should override this method. 
	 * @param querySql original sql
	 * @return return sql to query the count of total records.
	 */
	protected String convertCountSql(String querySql) {
		return "select count(*) from ("+querySql+")";
	}
	
}
