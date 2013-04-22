package cn.ls.integrator.components;

import java.sql.BatchUpdateException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.ExchangeStateManager;
import cn.ls.integrator.components.manager.impl.ExchangeStateManagerImpl;
import cn.ls.integrator.components.metadata.DataSourceMetadata;
import cn.ls.integrator.components.utils.CommonHelper;
import cn.ls.integrator.components.utils.DataSourcePools;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeReplyProducingMessageHandler;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

/**
 * @ClassName: JdbcTargetComponent
 * @Description: 入库组件
 * @author wanl
 * @date 2011-4-15 上午09:15:45
 * @version V1.0
 */

public class JdbcTargetComponent extends SafeReplyProducingMessageHandler
		implements MessageHandler {
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 新增字符串
	 */
	public final static String INSERT_STRING = "insert into <table_name> (<column_list>) values (<value_list>)";
	/**
	 * 修改字符串
	 */
	public final static String UPDATE_STRING = "update <table_name>  set <column_assignment> where <where_clause>";
	/**
	 * 删除字符串
	 */
	public final static String DELETE_STRING = "delete from <table_name> where <where_clause>";
	/**
	 * 替换表
	 */
	public static final String REPLACE_TABLE = "<table_name>";
	/**
	 * 替换字段
	 */
	public static final String REPLACE_COLUMN = "<column_list>";
	/**
	 * 替换值
	 */
	public static final String REPLACE_VALUE = "<value_list>";
	/**
	 * 替换修改字段
	 */
	public static final String REPLACE_COLUMN_ASSIGNMENT = "<column_assignment>";
	/**
	 * 替换条件
	 */
	public static final String REPLACE_WHERE_CLAUSE = "<where_clause>";
	
	private SimpleJdbcOperations jdbcOperations;
	//数据源的名称
	private String dataSourceName ;
	
	/**
	 * 时间戳字段
	 */
	private String timestampField;

	
	public String getTimestampField() {
		return timestampField;
	}
	private DataSourceMetadata dataSource;
	
	private String databaseType;

	private String tableName;
	
	private String taskName;
	
	private String operationType;

	private DataSource jdbcDataSource;
	/**
	 * 主键字段
	 */
	private String pkField;

	// 初始化化
	@Override
	protected void onInit() {
		super.onInit();
	}

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		MessageHeaders headers = requestMessage.getHeaders();
		Message<?> message = null;
		setTableName(taskName);
		setOperationType(CommonHelper.Null2String(headers
				.get(IntegratorConstants.MESSAGE_HEADER_OPERATION_TYPE)));
		setTimestampField(CommonHelper.Null2String(headers
				.get(IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_FIELD)));
		setPkField(CommonHelper.Null2String(headers
				.get(IntegratorConstants.MESSAGE_HEADER_PK_NAME)));
		Map<String, Object> adapterheaders = new HashMap<String, Object>();
		List<Object> result = null;
		if (requestMessage.getPayload() instanceof List<?>) {
			initDataSource(headers);
			try{
				List<?> list = (ArrayList<?>) requestMessage.getPayload();
				if (list != null && list.size() > 0) {
					ThreadUtils.checkThreadInterrupted();
					if (!StringUtils.hasLength(operationType)) {
						result = insertOrUpdateBySql(list, tableName);
					} else if (operationType.equalsIgnoreCase("insert")) {
						result = insertBySql(list, tableName);	
					} else if (operationType.equalsIgnoreCase("update")) {
						updateBySql(list, tableName);
					} else if (operationType.equalsIgnoreCase("delete")) {
						deleteBySql(list, tableName);
					} else if (operationType.equalsIgnoreCase("insertOrUpdate")) {
						result = insertOrUpdateBySql(list, tableName);
					}
					if(result != null){
						throw new IntegratorException("执行插入操作时出错");
					}
				}
			} catch (InterruptedRuntimeException e){
				//删除0数据，待测
//			} catch (IntegratorException e){
//				ExchangeStateManager exchangeStateManager = ExchangeStateManagerImpl.getInstance();
//				exchangeStateManager.deleteZeroData(headers);//删除0数据
				throw e;
			} catch (Exception e) {
				//TODO 判断异常类型，是否为CannotGetJdbcConnectionException，然后进行消息回滚。
				
				logger.error(" JdbcTargetComponent is fail ! ",e);
				Map<String,Object> errHeaders = new HashMap<String,Object>();
				errHeaders.putAll(requestMessage.getHeaders());
				Exception error = null;
				if( result != null){
					int index = result.size()-1;
					if(result.get(index) instanceof Exception){
						error = (Exception) result.get(index);
						result.remove(index);
					}
					errHeaders.put(IntegratorConstants.MESSAGE_HEADER_ERROR_RECORD, result);
				}
				errHeaders.put(IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME, tableName);
				Message<?> failedMessage = null;
				if (error != null) {
					failedMessage = MessageBuilder.withPayload(error)
							.copyHeaders(errHeaders).build();
				} else {
					failedMessage = MessageBuilder.withPayload(e).copyHeaders(
							errHeaders).build();
				}
//				throw new MessagingException(failedMessage, "jdbcTarget 组件异常", e);
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_ERROR_MESSAGE, failedMessage);
			}
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_RECEIVE_DATE, format.format(date));
		message = MessageBuilder.withPayload(requestMessage).copyHeaders(adapterheaders).build();
		return message;
	}
	
	private void initDataSource(MessageHeaders headers) {
		String scheduleName = (String) headers.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
		if(StringUtility.isBlank(this.dataSourceName)){
			dataSource = DataSourcePools.initDataSourceMetadata(scheduleName, 
					(String) headers.get(IntegratorConstants.MESSAGE_HEADER_QUEUE_NAME),TaskType.recv);
		}else{
			dataSource = DataSourcePools.initDataSourceMetadata(scheduleName, this.dataSourceName,TaskType.recv);
		}
		this.setDatabaseType(dataSource.getDbType());
		jdbcDataSource = DataSourcePools.getDataSource(dataSource);
		this.jdbcOperations = new SimpleJdbcTemplate(jdbcDataSource);
	}

	private void deleteBySql(List<?> list, String tableName) {
		String[] prikeys = pkField.toUpperCase().split(",");
		StringBuilder markSql = new StringBuilder();
		for (String pk : prikeys) {
			markSql.append(" ").append(pk).append(" = ? and");
		}
		markSql.append(" 1=1");
		String sql = DELETE_STRING.replaceFirst(REPLACE_TABLE, tableName)
			.replaceFirst(REPLACE_WHERE_CLAUSE, markSql.toString());
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			Map<?, ?> mapData = (HashMap<?, ?>) list.get(i);
			Object[] values = new Object[prikeys.length];
			for(int j = 0; j < prikeys.length; j ++){
				String pk = prikeys[j];
//				if(mapData.get(pk) == null){
//					throw new IntegratorException("您配置的主键:"+ pk + "可能不存在");
//				}
				values[j] = getDateObect(mapData.get(pk));
			}
			batchArgs.add(values);
		}
		int[] resultArr = this.jdbcOperations.batchUpdate(sql, batchArgs);
		int deleteRecord = 0;
		for(int i : resultArr){
			deleteRecord += i;
		}
		if(deleteRecord >= 0){
			logger.warn("删除数据" + deleteRecord + "条");
		}
	}

	private void updateBySql(List<?> list, String tableName) {
//		for (int i = 0; i < list.size(); i++) {
//			Map<?, ?> mapData = (HashMap<?, ?>) list.get(i);
//		}
		//TODO 
	}

	/**
	 * 
	 * @param list
	 * @param tableName
	 * @return 如果执行正常，返回null 如果执行中遇到错误，则返回错误信息
	 */
	@SuppressWarnings("unchecked")
	private List<Object> insertBySql(List<?> list, String tableName) {
//		String[] prikeys = pkField.toUpperCase().split(",");
//		filterSamePk(list, prikeys);
		Map<?, ?> exampleData = (HashMap<?, ?>) list.get(0);
		exampleData.remove("NUM");
		StringBuilder column_list = new StringBuilder();
		StringBuilder value_list = new StringBuilder();
		Set<String> keySet = (Set<String>)exampleData.keySet();
		String[] columns = initColumns(keySet,column_list,value_list);
		// sql替换
		String sql = INSERT_STRING
				.replaceFirst(REPLACE_TABLE, tableName)
				.replaceFirst(REPLACE_COLUMN,
						column_list.substring(0, column_list.length() - 1))
				.replaceFirst(REPLACE_VALUE,
						value_list.substring(0, value_list.length() - 1));
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			Map<?, ?> mapData = (HashMap<?, ?>) list.get(i);
			Object[] values = new Object[columns.length];
			for(int j = 0; j < columns.length; j++){
				String column = columns[j];
				values[j] = getColumnValue(mapData,column);
			}
			batchArgs.add(values);
		}
		try {
			this.jdbcOperations.batchUpdate(sql, batchArgs);
		}
		catch (Exception e) {
			logger.error(e);
			Throwable t = e.getCause();
			if(t instanceof BatchUpdateException){
				BatchUpdateException bue = (BatchUpdateException)t;
				return insertOneByOne(sql,batchArgs,bue);
			} else {
				List<Object> errorList = new ArrayList<Object>();
				for(Object[] objArr : batchArgs){
					errorList.add(Arrays.toString(objArr));
				}
				errorList.add(e);
				return errorList;
			}
		}
		return null;
	}

	/**
	 * 初始化字段信息
	 * @param keySet
	 * @param columnList
	 * @param valueList
	 * @return
	 */
	private String[] initColumns(Set<String> keySet, StringBuilder columnList,
			StringBuilder valueList) {
		int length = keySet.size();
		if(StringUtility.isNotBlank(timestampField)){
			if(!keySet.contains(timestampField)){
				length ++;
			}
		}
		String[] columns = new String[length];
		int index = 0;
		for (String key : keySet) {
			columnList.append(" ").append(key).append(",");
			valueList.append("?").append(",");
			columns[index++] = key;
		}
		if(StringUtility.isNotBlank(timestampField)){
			if(!keySet.contains(timestampField)){
				columnList.append(" ").append(timestampField).append(",");
				valueList.append("?").append(",");
				columns[index++] = timestampField;
			}
		}
		
		return columns;
	}

	private Object getColumnValue(Map<?, ?> mapData, String column) {
		Object value = getDateObect(mapData.get(column));
//		if(!StringUtils.hasLength(timestampField)){
//			return value;
//		}
		String[] timestampFieldArr = timestampField.split(",");
		if ((timestampFieldArr.length == 1 && StringUtility.equalsIgnoreCase(column,timestampField))
				|| (timestampFieldArr.length == 2 && 
						(timestampFieldArr[0].equalsIgnoreCase(column) || 
								timestampFieldArr[1].equalsIgnoreCase(column)))){
//			Object time = getDateObect(mapData.get(column));
			long timeMilis = System.currentTimeMillis();
			if(value == null || value instanceof Date){
				Timestamp dateTime = new Timestamp(timeMilis);//Timestamp类
				value = dateTime;
			}else{
				value = System.nanoTime();
			}
		}
		return value;
	}
	
	/**
     * 返回时间类型 对象
     * @param entryValue
     * @return
     */
	private Object getDateObect(Object entryValue) {
		Object obj = entryValue;
		if(obj == null){
			return null;
		}
		String temp =  entryValue.toString();
		if (/*entryValue.getClass().equals(java.lang.String.class)
				&&*/ temp.indexOf("#$$#") != -1) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd#$$#HH:mm:ss:SSS");
				Date timeDate = sdf.parse(temp);
				java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp类
				obj =  dateTime;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				obj = entryValue;
			}

		}
		return obj;
	}

	private List<Object> insertOneByOne(String sql, List<Object[]> batchArgs,BatchUpdateException bue) {
		List<Object> errorList = new ArrayList<Object>();
		int[] result = bue.getUpdateCounts();
		for(int i = 0; i < result.length ; i++){
			int index = result[i];
			if(index != 1){
				Object[] values = batchArgs.get(i);
				try {
					this.jdbcOperations.update(sql, values);
				} catch (DataAccessException e) {
					logger.error(e);
					errorList.add(Arrays.toString(values));
					if(!errorList.contains(e)){
						errorList.add(e);
					}
				}
			}
		}
		if(errorList.size()>0){
			return errorList;
		}
		return null;
	}

	private List<Object> insertOrUpdateBySql(List<?> list,String tableName) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jdbcDataSource);
		List<Object> result = null;
		TransactionStatus status = null;
		try{
			status = transactionManager.getTransaction(def);
			deleteBySql(list, tableName);
			result = insertBySql(list, tableName);
			transactionManager.commit(status);
		}catch (DataAccessException e){
			logger.error(e.getMessage(),e);
			if(transactionManager != null){
				transactionManager.rollback(status);
			}
			throw e;
		}
		return result;
	}
	
	public void setDataSource(DataSourceMetadata dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcOperations(SimpleJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public void setTimestampField(String timestampField) {
		this.timestampField = timestampField;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

}
