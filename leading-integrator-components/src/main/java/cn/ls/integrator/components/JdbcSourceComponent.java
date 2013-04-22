package cn.ls.integrator.components;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import loquat.reflectutils.ReflectUtils;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.db.dialect.Dialect;
import cn.ls.integrator.components.db.dialect.DialectFactory;
import cn.ls.integrator.components.metadata.DataSourceMetadata;
import cn.ls.integrator.components.metadata.JdbcSourceMetadata;
import cn.ls.integrator.components.mongo.MongoDataStore;
import cn.ls.integrator.components.page.Package;
import cn.ls.integrator.components.utils.DataSourcePools;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.log.message.LastUpdateTime;
import cn.ls.integrator.core.model.MessageType;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;
import cn.ls.integrator.core.version.UniquelyIdentifies;

import com.google.code.morphia.query.Query;

public class JdbcSourceComponent extends SafeMessageProducerSupport implements
		MessageHandler {

	protected final Logger log = Logger.getLogger(JdbcSourceComponent.class);
	/**
	 * 默认的时间戳 日期 1000-1-1 0:00:00:-30609820800000
	 */
	private static final long DEFAULT_TIMESTAMP = -3060982080000L;

	private String databaseType;
	/**
	 * 表操作类型
	 */
	public String operationType;

	private JdbcSourceMetadata metadata;

	/**
	 * 是否正在运行
	 */
	private boolean isRuning = false;

	/**
	 * 消息类型
	 */
	private String messageType = "";

	/**
	 * jdbc操作类
	 */
	private SimpleJdbcOperations jdbcOperations;
	/**
	 * mongods
	 */
	private MongoDataStore mongoDataStore = MongoDataStore.getInstance();

	/**
	 * 数据源配置
	 */
	private DataSourceMetadata dataSource;
	/**
	 * 业务表名
	 */
	private String taskSQL;
	/**
	 * 字段集名
	 */
	private String taskName;
	/**
	 * 排序
	 */
	private String orderByClause;

	/**
	 * 时间戳字段排序（默认升序 asc）
	 */
	private String timestampOrder;
	/**
	 * 包限制
	 */
	private int packageLimitSize;
	/**
	 * 限制条件
	 */
	private String whereClause;
	/**
	 * 时间戳字段
	 */
	private String timestampField;
	/**
	 * 主键字段
	 */
	private String pkField;

	private String fieldsName;

	private DataSource jdbcDataSource;

	private Dialect dialect;
	// 数据源的名称，如果该项为空
	private String dataSourceName;

	private static final String DEAULT_SEND_SOURCE = "LOCAL";
	private boolean filterWithoutTimestamp;

	public boolean isFilterWithoutTimestamp() {
		return filterWithoutTimestamp;
	}

	public void setFilterWithoutTimestamp(boolean filterWithoutTimestamp) {
		this.filterWithoutTimestamp = filterWithoutTimestamp;
	}

	public String getFieldsName() {
		return fieldsName;
	}

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}

	public String getTaskSQL() {
		return taskSQL;
	}

	public void setTaskSQL(String taskSQL) {
		this.taskSQL = taskSQL;
	}

	@Override
	protected void onInit() {
		super.onInit();
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		MessageHeaders headers = message.getHeaders();
		String taskName = (String) headers
				.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
		String scheduleName = (String) headers
				.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
		initDataSource(scheduleName, taskName);
		// 如果程序正在运行
		if (isRuning)
			return;
		isRuning = true;
		try {
			ThreadUtils.checkThreadInterrupted();
			putMessage(message);
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			log.error("线程中断", e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("scheduleName: " + scheduleName + ", taskName: "
					+ taskName + ": jdbc源组件错误异常", e);
		} finally {
			isRuning = false;
		}
	}

	private void initDataSource(String scheduleName, String taskName) {
		if (dataSource == null) {
			if (StringUtility.isBlank(this.dataSourceName)) {
				this.dataSourceName = DEAULT_SEND_SOURCE;
			}
			dataSource = DataSourcePools.initDataSourceMetadata(scheduleName,this.dataSourceName, TaskType.send);
		}
		jdbcDataSource = DataSourcePools.getDataSource(dataSource);
		// jdbc操作类
		jdbcOperations = new SimpleJdbcTemplate(jdbcDataSource);
		if (StringUtility.isBlank(this.databaseType)) {
			this.databaseType = dataSource.getDbType();
		}
		dialect = DialectFactory.buildDialect(databaseType);

	}

	/**
	 * 
	 * @Title: getLastUpdateTime
	 * @Description: 获取最后更新时间戳
	 * @param taskName
	 * @return
	 * @return long 返回类型
	 * @throws
	 */
	private LastUpdateTime getLastUpdateTime(String taskName) {
		Query<LastUpdateTime> query = mongoDataStore.createQuery(
				LastUpdateTime.class).filter("taskName", taskName).filter(
				"integratorId", UniquelyIdentifies.getId());
		List<LastUpdateTime> list = query.asList();
		LastUpdateTime lastUpdateTime = new LastUpdateTime();
		lastUpdateTime.setIntegratorId(UniquelyIdentifies.getId());
		if (list != null && list.size() > 0) {
			lastUpdateTime = list.get(0);
			return lastUpdateTime;
		} else {
			lastUpdateTime.setTaskName(taskName);
			lastUpdateTime.setLastUpdateTime(DEFAULT_TIMESTAMP);
			lastUpdateTime.setSameNumber(0);
			mongoDataStore.updateFirst(query, lastUpdateTime, true);
		}
		return lastUpdateTime;

	}

	@SuppressWarnings("unchecked")
	protected List<?> doPoll(String selectQuery, Object timestamp) {
		List<?> payload = null;
		final RowMapper<?> rowMapper = new ColumnMapRowMapper();
		ResultSetExtractor<List<Object>> resultSetExtractor;
		final int pkgSize = this.metadata.getPackageLimitSize();
		if (this.metadata.getPackageLimitSize() > 0) {
			resultSetExtractor = new ResultSetExtractor<List<Object>>() {
				public List<Object> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					List<Object> results = new ArrayList<Object>(3);
					int rowNum = 0;
					while (rs.next() && rowNum < pkgSize) {
						results.add(rowMapper.mapRow(rs, rowNum++));
					}
					return results;
				}
			};
		} else {
			ResultSetExtractor<List<Object>> temp = new RowMapperResultSetExtractor<Object>(
					(RowMapper<Object>) rowMapper);
			resultSetExtractor = temp;
		}

		// payload = this.jdbcOperations.getJdbcOperations().query(selectQuery,
		// resultSetExtractor);
		if (timestamp == null) {
			payload = this.jdbcOperations.getJdbcOperations().query(
					selectQuery, resultSetExtractor);
		} else {
			// TODO 多时间戳
			payload = this.jdbcOperations.getJdbcOperations()
					.query(selectQuery, new Object[] { timestamp },
							resultSetExtractor);
		}
		if (payload.size() < 1) {
			payload = null;
		}
		return payload;
	}

	public Message<Object> receive(String selectQuery, Object timestamp) {
		Object payload = doPoll(selectQuery, timestamp);
		if (payload == null) {
			return null;
		}
		return MessageBuilder.withPayload(payload).build();
	}

	private void putMessage(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		String task = headers
				.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME)
				+ "."
				+ headers.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
		// 获取打包的限制条件（c 消息优先）
		getDefaultMetadata(headers);
		String whereClause = this.metadata.getWhereClause();
		if (StringUtility.isBlank(this.getTimestampField())) {
			commandWithoutTimestamp(headers, task);
			return;
		}
		if (StringUtility.isBlank(whereClause)) {
			commandWithoutWhereClause(headers, task);
		} else {
			commandWithWhereClause(headers, task);
		}
	}

	private void commandWithoutTimestamp(MessageHeaders headers, String task) {
		if (StringUtility.isBlank(this.metadata.getPkField())) {
			throw new IntegratorException("任务" + task + "对应的主键不存在，请配置");
		}
		String selectQuery = dialect
				.buildSelectSql(this.metadata.getTaskSQL(), this.metadata
						.getFieldsName(), this.metadata.getWhereClause(),
						this.metadata.getOrderByClause(), this.metadata
								.getTimestampField(), this.metadata
								.getTimestampOrder());
		selectQuery = selectQuery.replace(Dialect.REPLACE_ORDER_BY_CLAUSE,
				this.metadata.getPkField());
		String countSql = dialect.buildCountSql(selectQuery);
		Long total = jdbcOperations.queryForLong(countSql);

		// 总包数
		Long totalPages = Package.getPackageSize(total, this.metadata
				.getPackageLimitSize());
		long startIndex = 0;
		long lastIndex = 0;
		// 组消息编号
		String groupId = UUID.randomUUID().toString();

		for (int currentPage = 1; currentPage <= totalPages; currentPage++) {

			startIndex = (currentPage - 1)
					* this.metadata.getPackageLimitSize() + 1;
			lastIndex = Package.getLastIndex(total, this.metadata
					.getPackageLimitSize(), currentPage, totalPages);
			// }
			long percentage = ((lastIndex - startIndex + 1) + (currentPage - 1)
					* this.metadata.getPackageLimitSize())
					* 100 / total;
			String paginationSQL = dialect.paginationSQL(startIndex, lastIndex,
					selectQuery);
			// 数据集
			Message<?> adapterMessage = receive(paginationSQL, null);
			if (filterWithoutTimestamp) {
				adapterMessage = filterMessageWithoutTimestamp(adapterMessage);
			}
			if (adapterMessage != null) {
				List<?> payload = (List<?>) adapterMessage.getPayload();
				Map<String, Object> adapterheaders = new HashMap<String, Object>();
				// 组消息头
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE,
						MessageType.dataMessage);
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID,
						groupId);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, total);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE,
						payload.size());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME,
						taskName);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_FIELD,
						timestampField);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_ORDER,
						this.metadata.getTimestampOrder());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE,
						this.metadata.getPackageLimitSize());
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_PK_NAME,
						pkField);
				// 百分比
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PERCENTAGE,
						percentage);

				// adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_OPERATION_TYPE,
				// operationType);
				// 组装消息信息给
				adapterMessage = MessageBuilder.fromMessage(adapterMessage)
						.copyHeaders(headers).copyHeaders(adapterheaders)
						.build();
				// 按照message循环打包消息发送给下一个组件
				ThreadUtils.checkThreadInterrupted();
				sendMessage(adapterMessage);
			}
		}
	}

	private Message<?> filterMessageWithoutTimestamp(Message<?> adapterMessage) {
		// TODO Auto-generated method stub
		return adapterMessage;
	}

	private void commandWithWhereClause(MessageHeaders headers, String task) {

		// query sql
		String selectQuery = dialect
				.buildSelectSql(this.metadata.getTaskSQL(), this.metadata
						.getFieldsName(), this.metadata.getWhereClause(),
						this.metadata.getOrderByClause(), this.metadata
								.getTimestampField(), this.metadata
								.getTimestampOrder());
		String countSql = dialect.buildCountSql(selectQuery);
		Long total = jdbcOperations.queryForLong(countSql);

		// 总包数
		Long totalPages = Package.getPackageSize(total, this.metadata
				.getPackageLimitSize());
		long startIndex = 0;
		long lastIndex = 0;
		// 组消息编号
		String groupId = UUID.randomUUID().toString();

		for (int currentPage = 1; currentPage <= totalPages; currentPage++) {

			startIndex = (currentPage - 1)
					* this.metadata.getPackageLimitSize() + 1;
			lastIndex = Package.getLastIndex(total, this.metadata
					.getPackageLimitSize(), currentPage, totalPages);
			// }
			long percentage = ((lastIndex - startIndex + 1) + (currentPage - 1)
					* this.metadata.getPackageLimitSize())
					* 100 / total;
			String paginationSQL = dialect.paginationSQL(startIndex, lastIndex,
					selectQuery);
			// 数据集
			Message<?> adapterMessage = receive(paginationSQL, null);
			if (adapterMessage != null) {
				List<?> payload = (List<?>) adapterMessage.getPayload();
				Map<String, Object> adapterheaders = new HashMap<String, Object>();
				// 组消息头
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID,
						groupId);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, total);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE,
						payload.size());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME,
						taskName);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_FIELD,
						timestampField);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_ORDER,
						this.metadata.getTimestampOrder());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE,
						this.metadata.getPackageLimitSize());
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_PK_NAME,
						pkField);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE,
						MessageType.dataMessage);
				// 百分比
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PERCENTAGE,
						percentage);

				// adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_OPERATION_TYPE,
				// operationType);
				// 组装消息信息给
				adapterMessage = MessageBuilder.fromMessage(adapterMessage)
						.copyHeaders(headers).copyHeaders(adapterheaders)
						.build();
				// 按照message循环打包消息发送给下一个组件
				ThreadUtils.checkThreadInterrupted();
				sendMessage(adapterMessage);
			}
		}

	}

	private void commandWithoutWhereClause(MessageHeaders headers, String task) {
		Class<?> timeStampClazz = getLastUpdateTimeType(this.metadata
				.getTaskSQL(), this.metadata.getTimestampField().split(",")[0]);

		// 获取最大的时间戳
		LastUpdateTime lastUpdateTime = getLastUpdateTime(task);
		Long maxTimestamp = lastUpdateTime.getLastUpdateTime();
		Object timestamp = getTimestampObject(timeStampClazz, maxTimestamp);
		// query sql
		String selectQuery = dialect
				.buildSelectSql(this.metadata.getTaskSQL(), this.metadata
						.getFieldsName(), this.metadata.getWhereClause(),
						this.metadata.getOrderByClause(), this.metadata
								.getTimestampField(), this.metadata
								.getTimestampOrder());
		String countSql = dialect.buildCountSql(selectQuery);
		Long total = 0L;
		total = jdbcOperations.queryForLong(countSql, timestamp);
		total -= lastUpdateTime.getSameNumber();
		// String countSql = "select count(*) from ADMINISTRATOR.TEST_TABLE";
		// Long total = jdbcOperations.queryForLong(countSql);

		// 总包数
		Long totalPages = Package.getPackageSize(total, this.metadata
				.getPackageLimitSize());
		long startIndex = 0;
		long lastIndex = 0;
		// 组消息编号
		String groupId = UUID.randomUUID().toString();

		for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
			// lastUpdateTime = getLastUpdateTime(task);
			// maxTimestamp = lastUpdateTime.getLastUpdateTime();
			// timestamp = getTimestampObject(timeStampClazz, maxTimestamp);

			startIndex = (currentPage - 1)
					* this.metadata.getPackageLimitSize() + 1;
			lastIndex = Package.getLastIndex(total, this.metadata
					.getPackageLimitSize(), currentPage, totalPages);
			// startIndex = 1 + lastUpdateTime.getSameNumber();
			// if(currentPage == totalPages){
			// lastIndex = total-((currentPage - 1) *
			// this.metadata.getPackageLimitSize());
			// }else {
			// lastIndex = this.metadata.getPackageLimitSize();
			// }
			long percentage = ((lastIndex - startIndex + 1) + (currentPage - 1)
					* this.metadata.getPackageLimitSize())
					* 100 / total;
			startIndex += lastUpdateTime.getSameNumber();
			lastIndex += lastUpdateTime.getSameNumber();
			String paginationSQL = dialect.paginationSQL(startIndex, lastIndex,
					selectQuery);
			// 数据集
			Message<?> adapterMessage = receive(paginationSQL, timestamp);
			if (adapterMessage != null) {
				List<?> payload = (List<?>) adapterMessage.getPayload();
				Map<String, Object> adapterheaders = new HashMap<String, Object>();
				// 组消息头
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_GROUP_ID,
						groupId);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_GROUP_SIZE, total);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_SIZE,
						payload.size());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME,
						taskName);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_FIELD,
						timestampField);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_ORDER,
						this.metadata.getTimestampOrder());
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE,
						this.metadata.getPackageLimitSize());
				adapterheaders.put(IntegratorConstants.MESSAGE_HEADER_PK_NAME,
						pkField);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE,
						MessageType.dataMessage);
				// 百分比
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_PERCENTAGE,
						percentage);
				adapterheaders.put(
						IntegratorConstants.MESSAGE_HEADER_OPERATION_TYPE,
						operationType);
				// 组装消息信息给
				adapterMessage = MessageBuilder.fromMessage(adapterMessage)
						.copyHeaders(headers).copyHeaders(adapterheaders)
						.build();
				// 按照message循环打包消息发送给下一个组件
				ThreadUtils.checkThreadInterrupted();
				sendMessage(adapterMessage);
				updateTimeMillis(payload, task);
			}
		}

	}

	private Class<?> getLastUpdateTimeType(String taskSQL, String timeStampField) {
		String sql = "select " + timeStampField +" from " + taskSQL + " t  where 1=2";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(this.jdbcDataSource);
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		SqlRowSetMetaData sqlRowSetMtdt = sqlRowSet.getMetaData();
		try {
			return Class.forName(sqlRowSetMtdt.getColumnClassName(1));
		} catch (InvalidResultSetAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void updateTimeMillis(List<?> payload, String taskName) {
		if (payload == null || payload.size() == 0) {
			return;
		}
		int newSameNumber = 0;
		long maxTime = 0L;
		for (int i = 0; i < payload.size(); i++) {
			int index = 0;
			if (StringUtility.equalsIgnoreCase("asc", this.metadata
					.getTimestampOrder())) {
				index = payload.size() - 1 - i;
			} else {
				index = i;
			}
			Map map = (HashMap) payload.get(index);
			Object timestampObj = getLastTimeStamp(map);
			if (maxTime == 0L) {
				maxTime = getTimestampTime(timestampObj);
				if (maxTime == 0L) {
					// TODO 查询结果中没有timestameField定义的字段
				}
				newSameNumber++;
			} else {
				if (maxTime == getTimestampTime(timestampObj)) {
					newSameNumber++;
				} else {
					break;
				}
			}
		}
		LastUpdateTime oldLastUpdateTime = getLastUpdateTime(taskName);
		if (maxTime == (oldLastUpdateTime.getLastUpdateTime())) {
			newSameNumber += oldLastUpdateTime.getSameNumber();
		}
		// 每次更新
		long updateTimeMillis = maxTime;
		Query<LastUpdateTime> query = mongoDataStore.createQuery(
				LastUpdateTime.class).filter("taskName", taskName).filter(
				"integratorId", UniquelyIdentifies.getId());
		List<LastUpdateTime> list = query.asList();
		LastUpdateTime lastUpdateTime = null;
		if (list != null && list.size() > 0) {
			lastUpdateTime = list.get(0);
		} else {
			lastUpdateTime = new LastUpdateTime();
		}
		lastUpdateTime.setIntegratorId(UniquelyIdentifies.getId());
		lastUpdateTime.setTaskName(taskName);
		lastUpdateTime.setLastUpdateTime(updateTimeMillis);
		lastUpdateTime.setSameNumber(newSameNumber);
		mongoDataStore.updateFirst(query, lastUpdateTime, true);

		// newLastUpdateTime = mongoDataStore.find(LastUpdateTime.class,
		// IntegratorConstants.MESSAGE_HEADER_TASK_NAME, taskName).get();
		// if (newLastUpdateTime == null) {
		// newLastUpdateTime = new LastUpdateTime();
		// }
		// newLastUpdateTime.setTaskName(taskName);
		// newLastUpdateTime.setLastUpdateTime(updateTimeMillis);
		// newLastUpdateTime.setSameNumber(newSameNumber);
		// mongoDataStore.save(newLastUpdateTime);
	}

	@SuppressWarnings("unchecked")
	private Object getLastTimeStamp(Map map) {
		Object timestampObj = null;
		if (timestampField.contains(",")) {
			String[] timeArr = timestampField.split(",");
			Object timeStampObj1 = map.get(timeArr[0]);
			Object timeStampObj2 = map.get(timeArr[1]);
			if (timeStampObj1 instanceof Date) {
				if (timeStampObj2 == null) {
					timestampObj = timeStampObj1;
				} else if (((Date) timeStampObj1)
						.compareTo((Date) timeStampObj2) > 0) {
					timestampObj = timeStampObj1;
				} else {
					timestampObj = timeStampObj2;
				}
			} else {
				if (timeStampObj1 == null) {
					timestampObj = timeStampObj2;
				} else if (timeStampObj2 == null) {
					timestampObj = timeStampObj1;
				} else if (Long.parseLong(String.valueOf(timeStampObj1)) > Long
						.parseLong(String.valueOf(timeStampObj2))) {
					timestampObj = timeStampObj1;
				} else {
					timestampObj = timeStampObj2;
				}
			}
		} else {
			timestampObj = map.get(timestampField);
		}
		return timestampObj;
	}

	private void getDefaultMetadata(Map<String, Object> headers) {
		Map<String, Object> properties = new HashMap<String, Object>();
		// 消息类型
		// setProperty(properties, headers, "messageType",
		// IntegratorConstants.MESSAGE_HEADER_MESSAGE_TYPE, this.messageType);
		// MongoDataStore
		setProperty(properties, headers, "mongoDataStore", "mongoDataStore",
				this.mongoDataStore);
		// dataSource
		setProperty(properties, headers, "dataSource", "dataSource",
				this.dataSource);
		// taskName
		setProperty(properties, headers, "taskName",
				IntegratorConstants.MESSAGE_HEADER_BUSINESS_NAME, this.taskName);

		// fieldsName
		setProperty(properties, headers, "fieldsName",
				IntegratorConstants.MESSAGE_HEADER_FIELDS, this.fieldsName);
		// orderByClause
		setProperty(properties, headers, "orderByClause",
				IntegratorConstants.MESSAGE_HEADER_ORDER, this.orderByClause);

		setProperty(properties, headers, "operationType",
				IntegratorConstants.MESSAGE_HEADER_OPERATION_TYPE,
				this.operationType);

		// whereClause
		setProperty(properties, headers, "whereClause",
				IntegratorConstants.MESSAGE_HEADER_CONDITION, this.whereClause);
		// packageLimitSize
		// setPropertie(properties, headers, "packageLimitSize",
		// IntegratorConstants.PACKAGESIZE, this.packageLimitSize);
		if (!StringUtility.isBlank("" + this.packageLimitSize)) {
			properties.put(
					IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE,
					this.packageLimitSize);

		} else {
			setProperty(properties, headers, "packageLimitSize",
					IntegratorConstants.MESSAGE_HEADER_PACKAGE_LIMIT_SIZE,
					99999999);
		}

		// timestampOrder 默认asc
		properties.put(IntegratorConstants.MESSAGE_HEADER_TIMESTAMP_ORDER,
				StringUtility.equals("desc", timestampOrder) ? "desc" : "asc");

		// timestampField
		setProperty(properties, headers, "timestampField", "timestampField",
				this.timestampField);
		// pkField
		setProperty(properties, headers, "pkField",
				IntegratorConstants.MESSAGE_HEADER_PK_NAME, this.pkField);
		properties.put("taskSQL", taskSQL);
		this.metadata = new JdbcSourceMetadata();
		ReflectUtils.getInstance().populate(this.metadata, properties);

	}

	private long getTimestampTime(Object timestampObj) {
		if (timestampObj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) timestampObj;
			return timestamp.getTime();
		} else if (timestampObj instanceof Date) {
			Date date = (Date) timestampObj;
			return date.getTime();
		} else if (timestampObj instanceof java.sql.Date) {
			java.sql.Date date = (java.sql.Date) timestampObj;
			return date.getTime();
		} else {
			String timestampStr = String.valueOf(timestampObj);
			timestampStr = timestampStr.replace("L", "");
			// TODO 没有判断空值 强行转换出错
			if (StringUtility.isNotBlank(timestampStr)
					&& StringUtility.isNumeric(timestampStr)) {
				return Long.parseLong(timestampStr);
			} else {
				// 默认返回
				return 0L;
			}
		}
	}

	private Object getTimestampObject(Class<?> timestampClazz, long time) {
		Object timestamp = null;
//		if (timestampClazz == Timestamp.class) {
//			timestamp = new Timestamp(time);
//		} else if (timestampClazz == Date.class) {
//			timestamp = new Date(time);
//		} else if (timestampClazz == java.sql.Date.class) {
//			timestamp = new java.sql.Date(time);
//		} else {
//			timestamp = time;
//		}
		if(timestampClazz == Date.class || 
				timestampClazz.getSuperclass() == Date.class){
			timestamp = new Date(time);
		}else{
			timestamp = time;
		}
		return timestamp;
	}

	private void setProperty(Map<String, Object> target,
			Map<String, Object> soruce, String targetKey, String soruceKey,
			Object defaultValue) {
		if (soruce.get(soruceKey) == null) {
			if (defaultValue != null) {
				target.put(targetKey, defaultValue);
			}
		} else {
			target.put(targetKey, soruce.get(soruceKey));
		}
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public SimpleJdbcOperations getJdbcOperations() {
		return jdbcOperations;
	}

	public void setJdbcOperations(SimpleJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
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

	public int getPackageLimitSize() {
		return packageLimitSize;
	}

	public void setPackageLimitSize(int packageLimitSize) {
		this.packageLimitSize = packageLimitSize;
	}

	public String getTimestampField() {
		return timestampField;
	}

	public void setTimestampField(String timestampField) {
		this.timestampField = timestampField;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setDataSource(DataSourceMetadata dataSource) {
		this.dataSource = dataSource;
	}

	public String getTimestampOrder() {
		return timestampOrder;
	}

	public void setTimestampOrder(String timestampOrder) {
		this.timestampOrder = timestampOrder;
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
