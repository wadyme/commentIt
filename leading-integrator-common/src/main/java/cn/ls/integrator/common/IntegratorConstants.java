/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.common;

/**
 * 常量定义
 * 
 * @author zhoumb 2011-4-27
 */
public interface IntegratorConstants {

	public static final String MESSAGE_HEADER_EXCHANGE_TASK_NAME = "exchangeTaskName";
	/**
	 * 消息组id
	 */
	public static final String MESSAGE_HEADER_GROUP_ID = "groupId";

	/**
	 * 单个文件组id
	 */
	public static final String MESSAGE_HEADER_FILE_GROUP_ID = "fileGroupId";

	/**
	 * 单个文件完成字节
	 */
	public static final String MESSAGE_HEADER_FILE_COMPLETE_BYTES = "fileCompleteBytes";

	/**
	 * 单个文件总字节数
	 */
	public static final String MESSAGE_HEADER_FILE_TOTAL_BYTES = "fileTotalBytes";

	/**
	 * 文件总个数
	 */
	public static final String MESSAGE_HEADER_FILE_TOTAL_NUMBER = "fileTotalNumber";

	/**
	 * 文件完成个数
	 */
	public static final String MESSAGE_HEADER_FILE_COMPLETE_NUMBER = "fileCompleteNumber";

	/**
	 * 文件源路径
	 */
	public static final String MESSAGE_HEADER_FILE_SOURCE_PATH = "fileSourcePath";

	/**
	 * 组总消息大小(两种含义：1、数据表的记录总数，2、文件个数)
	 */
	public static final String MESSAGE_HEADER_GROUP_SIZE = "groupSize";
	
	/**
	 * 消息大小
	 */
	public static final String MESSAGE_HEADER_MESSAGE_SIZE = "messageSize";
	/**
	 * 时间戳字段
	 */
	public static final String MESSAGE_HEADER_TIMESTAMP_FIELD = "timestampField";
	/**
	 * 时间戳排序方式
	 */
	public static final String MESSAGE_HEADER_TIMESTAMP_ORDER = "timestampOrder";
	/**
	 * 完成数量 (两种含义：1、数据表完成的记录总数，2、文件完成的总字节数)
	 */
	public static final String MESSAGE_HEADER_COMPLETE_SIZE = "completeSize";
	/**
	 * 任务名称
	 */
	public static final String MESSAGE_HEADER_TASK_NAME = "taskName";

	/**
	 * 任务标题
	 */
	public static final String MESSAGE_HEADER_TASK_TITLE = "taskTitle";

	/**
	 * 任务调度名
	 */
	public static final String MESSAGE_HEADER_SCHEDULE_NAME = "scheduleName";

	/**
	 * 任务调度标题
	 */
	public static final String MESSAGE_HEADER_SCHEDULE_TITLE = "scheduleTitle";

	/**
	 * 发送节点名
	 */
	public static final String MESSAGE_HEADER_SEND_NODE_NAME = "sendNodeName";

	/**
	 * 接收节点名
	 */
	public static final String MESSAGE_HEADER_RECV_NODE_NAME = "recvNodeName";

	/**
	 * 日志类型 （send or recv）
	 */
	public static final String MESSAGE_HEADER_LOG_TYPE = "logType";
	/**
	 * 发送方
	 */
	public static final String MESSAGE_HEADER_PRODUCER = "producer";

	/**
	 * 发送时间
	 */
	public static final String MESSAGE_HEADER_PRODUCE_DATE = "produceDate";

	/**
	 * 接收方
	 */
	public static final String MESSAGE_HEADER_CUSTOMER = "customer";
	/**
	 * 接受时间
	 */
	public static final String MESSAGE_HEADER_RECEIVE_DATE = "receiveDate";

	/**
	 * 数据包大小
	 */
	public static final String MESSAGE_HEADER_PACKAGE_LIMIT_SIZE = "packageLimitSize";
	/**
	 * 百分比
	 */
	public static final String MESSAGE_HEADER_PERCENTAGE = "percentage";

	/**
	 * 业务名称(业务表名)
	 */
	public static final String MESSAGE_HEADER_BUSINESS_NAME = "businessName";

	/**
	 * 消息类型
	 */
	public static final String MESSAGE_HEADER_MESSAGE_TYPE = "messageType";
	/**
	 * 数据库插入符
	 */
	public static final String MESSAGE_HEADER_OPERATION_TYPE = "operationType";

	/**
	 * 限制条件(SQL语句where以后的部分，包括排序、分组等)
	 */
	public static final String MESSAGE_HEADER_CONDITION = "condition";

	/**
	 * 主键字段名称
	 */
	public static final String MESSAGE_HEADER_PK_NAME = "pkName";

	/**
	 * 排序 默认根据主健排序
	 */
	public static final String MESSAGE_HEADER_ORDER = "orderByClause";

	/**
	 * 字段 逗号隔开
	 */
	public static final String MESSAGE_HEADER_FIELDS = "fieldsName";

	/**
	 * 历史交换量
	 */
	public static final String MESSAGE_HEADER_HISTORY_TOTAL = "historyTotal";

	/**
	 * 出现异常是否继续
	 */
	public static final String MESSAGE_HEADER_EXISTON_ERROR = "existon_error";

	/**
	 * 错误的记录
	 */
	public static final String MESSAGE_HEADER_ERROR_RECORD = "record";

	/**
	 * 错误的消息头
	 */
	public static final String MESSAGE_HEADER_ERROR_MESSAGE = "errorMessage";

	public static final String MESSAGE_HEADER_SOURCE_PATH = "sourcePath";

	public static final String MESSAGE_HEADER_PACKAGE_BYTE_SIZE = "packageByteSize";

	public static final String MESSAGE_HEADER_DELETE_SOURCEFILE = "deleteSourceFile";

	public static final String MESSAGE_HEADER_IS_COMPLETE = "isComplete";

	public static final String MESSAGE_HEADER_FILE_NAME = "fileName";

	public static final String MESSAGE_HEADER_LAST_MODIFIED = "lastModified";

	public static final String MESSAGE_HEADER_SIMPLE_FILE_NAME = "simpleFileName";

	public static final String MESSAGE_HEADER_QUEUE_NAME = "queueName";
}
