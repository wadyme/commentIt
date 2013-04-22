package cn.ls.integrator.components.metadata;

import javax.sql.DataSource;


import cn.ls.integrator.components.mongo.MongoDataStore;

public class JdbcSourceMetadata {
	/**
	 * 消息类型
	 */
	private String messageType ;
	/**
	 * mongods
	 */
	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();
	
	/**
	 * 数据源
	 */
	private DataSource dataSource;
	/**
	 * 表操作类型
	 */
	private String operationType;
	
	/**
	 * 业务表名
	 */
	private String taskName;
	/**
	 * 字段集名
	 */
	private String fieldsName;
	/**
	 * 排序
	 */
	private String orderByClause;
	private String taskSQL;
	/**
	 * 限制条件
	 */
	private String whereClause;
	/**
	 * 包限制
	 */
	private int packageLimitSize;
	/**
	 * 时间戳字段
	 */
	private String timestampField;
	
	private String timestampOrder;
	/**
	 * 主键字段
	 */
	private String pkField;
	
	public String getTaskSQL() {
		return taskSQL;
	}
	public void setTaskSQL(String taskSQL) {
		this.taskSQL = taskSQL;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public MongoDataStore getMongoDataStore() {
		return mongoDataStore;
	}
	public void setMongoDataStore(MongoDataStore mongoDataStore) {
		this.mongoDataStore = mongoDataStore;
	}

	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getFieldsName() {
		return fieldsName;
	}
	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}
	public String getOrderByClause() {
		return orderByClause;
	}
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
	public String getWhereClause() {
		return whereClause;
	}
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
	public String getTimestampField() {
		return timestampField;
	}
	public void setTimestampField(String timestampField) {
		this.timestampField = timestampField;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public int getPackageLimitSize() {
		return packageLimitSize;
	}
	public void setPackageLimitSize(int packageLimitSize) {
		this.packageLimitSize = packageLimitSize;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getTimestampOrder() {
		return timestampOrder;
	}
	public void setTimestampOrder(String timestampOrder) {
		this.timestampOrder = timestampOrder;
	}


}
