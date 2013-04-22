
package cn.ls.integrator.client.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.ls.integrator.client.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UpdateTimer_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateTimer");
    private final static QName _GetConnectionTypeStringListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionTypeStringListResponse");
    private final static QName _GetSendScheduleParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleParameterValuesResponse");
    private final static QName _UpdateTimerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateTimerResponse");
    private final static QName _UpdateRecvScheduleParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvScheduleParameterValues");
    private final static QName _DeleteTimerOnTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTimerOnTask");
    private final static QName _StartAllQueueListener_QNAME = new QName("http://service.server.integrator.ls.cn/", "startAllQueueListener");
    private final static QName _GetSendScheduleListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleListResponse");
    private final static QName _ExistSendErrorLogResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "existSendErrorLogResponse");
    private final static QName _DeleteSendErrorLogResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteSendErrorLogResponse");
    private final static QName _GetSendScheduleConnectionResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleConnectionResponse");
    private final static QName _GetRecvSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvSchedule");
    private final static QName _UpdateSendTaskParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendTaskParameterValuesResponse");
    private final static QName _DeleteSendScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteSendScheduleResponse");
    private final static QName _AddRecvScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "addRecvScheduleResponse");
    private final static QName _GetConnectionTypeResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionTypeResponse");
    private final static QName _GetSendFileNumByScheduleNameResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendFileNumByScheduleNameResponse");
    private final static QName _InitializeFileTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "initializeFileTask");
    private final static QName _GetSendMessageNum_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendMessageNum");
    private final static QName _AddTaskOnTimer_QNAME = new QName("http://service.server.integrator.ls.cn/", "addTaskOnTimer");
    private final static QName _GetSendTaskContextResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskContextResponse");
    private final static QName _GetRecvTaskContextResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskContextResponse");
    private final static QName _GetRecvTaskParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskParameterValuesResponse");
    private final static QName _TestSendScheduleConnect_QNAME = new QName("http://service.server.integrator.ls.cn/", "testSendScheduleConnect");
    private final static QName _DeployRecvTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "deployRecvTask");
    private final static QName _GetSendSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendSchedule");
    private final static QName _GetSendScheduleConnection_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleConnection");
    private final static QName _TestRecvScheduleConnect_QNAME = new QName("http://service.server.integrator.ls.cn/", "testRecvScheduleConnect");
    private final static QName _GetRecvScheduleListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleListResponse");
    private final static QName _GetRecvScheduleParameters_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleParameters");
    private final static QName _GetMessageLogByTaskName_QNAME = new QName("http://service.server.integrator.ls.cn/", "getMessageLogByTaskName");
    private final static QName _ManualStartTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "manualStartTask");
    private final static QName _AddTimerOnTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "addTimerOnTaskResponse");
    private final static QName _StopQueueListenerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopQueueListenerResponse");
    private final static QName _AddTimerOnTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "addTimerOnTask");
    private final static QName _GetConnectionTypeListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionTypeListResponse");
    private final static QName _GetSendTaskParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskParameterValues");
    private final static QName _GetAssociatesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociatesResponse");
    private final static QName _GetAssociateWithRecvTaskParam_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithRecvTaskParam");
    private final static QName _GetSendTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTask");
    private final static QName _GetTimestampResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getTimestampResponse");
    private final static QName _DeleteRecvSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvSchedule");
    private final static QName _UpdateRecvTaskParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvTaskParameterValues");
    private final static QName _GetFileExchangeLogListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeLogListResponse");
    private final static QName _UpdateRecvScheduleParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvScheduleParameterValuesResponse");
    private final static QName _GetErrorLogList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getErrorLogList");
    private final static QName _GetSendScheduleParameters_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleParameters");
    private final static QName _StopSendScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopSendScheduleResponse");
    private final static QName _TestRecvScheduleConnectResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "testRecvScheduleConnectResponse");
    private final static QName _AddRecvTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "addRecvTaskResponse");
    private final static QName _GetSendTaskParameters_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskParameters");
    private final static QName _StopQueueListener_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopQueueListener");
    private final static QName _DeleteRecvTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvTaskResponse");
    private final static QName _GetFileExchangeNumByScheduleNameResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeNumByScheduleNameResponse");
    private final static QName _GetSendTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskResponse");
    private final static QName _GetAssociateWithSendTaskParam_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithSendTaskParam");
    private final static QName _StartSendScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "startSendScheduleResponse");
    private final static QName _GetSendFileNumByScheduleName_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendFileNumByScheduleName");
    private final static QName _GetSendTaskParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskParameterValuesResponse");
    private final static QName _InitializeFileTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "initializeFileTaskResponse");
    private final static QName _TestSendScheduleConnectResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "testSendScheduleConnectResponse");
    private final static QName _GetRecvTaskListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskListResponse");
    private final static QName _GetAssociateWithSendTaskParamResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithSendTaskParamResponse");
    private final static QName _GetSendScheduleState_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleState");
    private final static QName _AddSendScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "addSendScheduleResponse");
    private final static QName _UpdateTimerOnTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateTimerOnTask");
    private final static QName _GetMessageLogByTaskNameResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getMessageLogByTaskNameResponse");
    private final static QName _GetRecvTaskParametersResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskParametersResponse");
    private final static QName _DeleteSendSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteSendSchedule");
    private final static QName _StartSendSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "startSendSchedule");
    private final static QName _GetSendTaskList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskList");
    private final static QName _GetSendScheduleParametersResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleParametersResponse");
    private final static QName _GetSendTaskContext_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskContext");
    private final static QName _GetRecvScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleResponse");
    private final static QName _GetRecvScheduleParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleParameterValuesResponse");
    private final static QName _GetRecvScheduleConnectionResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleConnectionResponse");
    private final static QName _UpdateSendTaskParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendTaskParameterValues");
    private final static QName _GetSendTaskListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskListResponse");
    private final static QName _GetLogList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getLogList");
    private final static QName _UpdateSendScheduleConnection_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendScheduleConnection");
    private final static QName _DeleteRecvScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvScheduleResponse");
    private final static QName _GetRecvTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskResponse");
    private final static QName _TestConnectionResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "testConnectionResponse");
    private final static QName _GetSendScheduleResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleResponse");
    private final static QName _GetSendScheduleStateResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleStateResponse");
    private final static QName _GetSendTaskParametersResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendTaskParametersResponse");
    private final static QName _GetFileExchangeNumByExchageTaskName_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeNumByExchageTaskName");
    private final static QName _DeleteRecvErrorLogResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvErrorLogResponse");
    private final static QName _StopAllQueueListener_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopAllQueueListener");
    private final static QName _AddTaskOnTimerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "addTaskOnTimerResponse");
    private final static QName _GetRecvScheduleParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleParameterValues");
    private final static QName _GetSendFileNumByExchangeTaskName_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendFileNumByExchangeTaskName");
    private final static QName _UpdateSendTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendTask");
    private final static QName _GetAssociates_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociates");
    private final static QName _DeleteTaskOnTimerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTaskOnTimerResponse");
    private final static QName _AddSendSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "addSendSchedule");
    private final static QName _GetFileExchangeNumByExchageTaskNameResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeNumByExchageTaskNameResponse");
    private final static QName _GetSendScheduleParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleParameterValues");
    private final static QName _GetConnectionTypeStringList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionTypeStringList");
    private final static QName _StartQueueListenerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "startQueueListenerResponse");
    private final static QName _DeleteRecvErrorLog_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvErrorLog");
    private final static QName _StartQueueListener_QNAME = new QName("http://service.server.integrator.ls.cn/", "startQueueListener");
    private final static QName _GetFileExchangeNumByScheduleName_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeNumByScheduleName");
    private final static QName _StopAllQueueListenerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopAllQueueListenerResponse");
    private final static QName _GetRecvScheduleList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleList");
    private final static QName _ExistRecvErrorLog_QNAME = new QName("http://service.server.integrator.ls.cn/", "existRecvErrorLog");
    private final static QName _GetConnectionType_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionType");
    private final static QName _GetLogListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getLogListResponse");
    private final static QName _GetRecvTaskParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskParameterValues");
    private final static QName _GetRecvScheduleConnection_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleConnection");
    private final static QName _UpdateRecvScheduleConnection_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvScheduleConnection");
    private final static QName _GetRecvTaskList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskList");
    private final static QName _DeleteTimestamp_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTimestamp");
    private final static QName _GetAssociateWithOutRecvTaskParamResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithOutRecvTaskParamResponse");
    private final static QName _GetErrorLogListResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getErrorLogListResponse");
    private final static QName _ManualStartTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "manualStartTaskResponse");
    private final static QName _UpdateTimerOnTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateTimerOnTaskResponse");
    private final static QName _ExistRecvErrorLogResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "existRecvErrorLogResponse");
    private final static QName _AddRecvSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "addRecvSchedule");
    private final static QName _GetAssociateWithRecvTaskParamResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithRecvTaskParamResponse");
    private final static QName _GetAssociateWithOutSendTaskParam_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithOutSendTaskParam");
    private final static QName _GetSendScheduleList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendScheduleList");
    private final static QName _UpdateRecvTaskParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvTaskParameterValuesResponse");
    private final static QName _GetConnectionTypeList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getConnectionTypeList");
    private final static QName _GetRecvTaskParameters_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskParameters");
    private final static QName _GetRecvTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTask");
    private final static QName _TestConnection_QNAME = new QName("http://service.server.integrator.ls.cn/", "testConnection");
    private final static QName _GetSendFileNumByExchangeTaskNameResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendFileNumByExchangeTaskNameResponse");
    private final static QName _DeployRecvTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deployRecvTaskResponse");
    private final static QName _DeleteTaskOnTimer_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTaskOnTimer");
    private final static QName _DeleteTimerOnTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTimerOnTaskResponse");
    private final static QName _UpdateSendScheduleParameterValuesResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendScheduleParameterValuesResponse");
    private final static QName _DeleteSendErrorLog_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteSendErrorLog");
    private final static QName _GetSendMessageNumResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getSendMessageNumResponse");
    private final static QName _DeleteRecvTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteRecvTask");
    private final static QName _GetAssociateWithOutSendTaskParamResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithOutSendTaskParamResponse");
    private final static QName _UpdateRecvTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvTaskResponse");
    private final static QName _UpdateSendTaskResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendTaskResponse");
    private final static QName _GetAssociateWithOutRecvTaskParam_QNAME = new QName("http://service.server.integrator.ls.cn/", "getAssociateWithOutRecvTaskParam");
    private final static QName _GetFileExchangeLogList_QNAME = new QName("http://service.server.integrator.ls.cn/", "getFileExchangeLogList");
    private final static QName _DeleteTimestampResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "deleteTimestampResponse");
    private final static QName _UpdateRecvTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvTask");
    private final static QName _GetRecvScheduleParametersResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvScheduleParametersResponse");
    private final static QName _GetTimestamp_QNAME = new QName("http://service.server.integrator.ls.cn/", "getTimestamp");
    private final static QName _UpdateRecvScheduleConnectionResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateRecvScheduleConnectionResponse");
    private final static QName _ExistSendErrorLog_QNAME = new QName("http://service.server.integrator.ls.cn/", "existSendErrorLog");
    private final static QName _StopSendSchedule_QNAME = new QName("http://service.server.integrator.ls.cn/", "stopSendSchedule");
    private final static QName _GetRecvTaskContext_QNAME = new QName("http://service.server.integrator.ls.cn/", "getRecvTaskContext");
    private final static QName _UpdateSendScheduleParameterValues_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendScheduleParameterValues");
    private final static QName _StartAllQueueListenerResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "startAllQueueListenerResponse");
    private final static QName _AddRecvTask_QNAME = new QName("http://service.server.integrator.ls.cn/", "addRecvTask");
    private final static QName _UpdateSendScheduleConnectionResponse_QNAME = new QName("http://service.server.integrator.ls.cn/", "updateSendScheduleConnectionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.ls.integrator.client.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSendFileNumByScheduleNameResponse }
     * 
     */
    public GetSendFileNumByScheduleNameResponse createGetSendFileNumByScheduleNameResponse() {
        return new GetSendFileNumByScheduleNameResponse();
    }

    /**
     * Create an instance of {@link GetRecvTaskParametersResponse }
     * 
     */
    public GetRecvTaskParametersResponse createGetRecvTaskParametersResponse() {
        return new GetRecvTaskParametersResponse();
    }

    /**
     * Create an instance of {@link GetConnectionTypeListResponse }
     * 
     */
    public GetConnectionTypeListResponse createGetConnectionTypeListResponse() {
        return new GetConnectionTypeListResponse();
    }

    /**
     * Create an instance of {@link GetSendTask }
     * 
     */
    public GetSendTask createGetSendTask() {
        return new GetSendTask();
    }

    /**
     * Create an instance of {@link GetRecvScheduleParametersResponse }
     * 
     */
    public GetRecvScheduleParametersResponse createGetRecvScheduleParametersResponse() {
        return new GetRecvScheduleParametersResponse();
    }

    /**
     * Create an instance of {@link InitializeFileTask }
     * 
     */
    public InitializeFileTask createInitializeFileTask() {
        return new InitializeFileTask();
    }

    /**
     * Create an instance of {@link UpdateRecvTaskParameterValues }
     * 
     */
    public UpdateRecvTaskParameterValues createUpdateRecvTaskParameterValues() {
        return new UpdateRecvTaskParameterValues();
    }

    /**
     * Create an instance of {@link StartQueueListener }
     * 
     */
    public StartQueueListener createStartQueueListener() {
        return new StartQueueListener();
    }

    /**
     * Create an instance of {@link GetSendTaskParametersResponse }
     * 
     */
    public GetSendTaskParametersResponse createGetSendTaskParametersResponse() {
        return new GetSendTaskParametersResponse();
    }

    /**
     * Create an instance of {@link GetSendFileNumByExchangeTaskName }
     * 
     */
    public GetSendFileNumByExchangeTaskName createGetSendFileNumByExchangeTaskName() {
        return new GetSendFileNumByExchangeTaskName();
    }

    /**
     * Create an instance of {@link GetConnectionTypeStringListResponse }
     * 
     */
    public GetConnectionTypeStringListResponse createGetConnectionTypeStringListResponse() {
        return new GetConnectionTypeStringListResponse();
    }

    /**
     * Create an instance of {@link AddSendSchedule }
     * 
     */
    public AddSendSchedule createAddSendSchedule() {
        return new AddSendSchedule();
    }

    /**
     * Create an instance of {@link GetSendScheduleList }
     * 
     */
    public GetSendScheduleList createGetSendScheduleList() {
        return new GetSendScheduleList();
    }

    /**
     * Create an instance of {@link AddTimerOnTaskResponse }
     * 
     */
    public AddTimerOnTaskResponse createAddTimerOnTaskResponse() {
        return new AddTimerOnTaskResponse();
    }

    /**
     * Create an instance of {@link AddTaskOnTimerResponse }
     * 
     */
    public AddTaskOnTimerResponse createAddTaskOnTimerResponse() {
        return new AddTaskOnTimerResponse();
    }

    /**
     * Create an instance of {@link DeleteTaskOnTimerResponse }
     * 
     */
    public DeleteTaskOnTimerResponse createDeleteTaskOnTimerResponse() {
        return new DeleteTaskOnTimerResponse();
    }

    /**
     * Create an instance of {@link GetConnectionTypeList }
     * 
     */
    public GetConnectionTypeList createGetConnectionTypeList() {
        return new GetConnectionTypeList();
    }

    /**
     * Create an instance of {@link GetRecvTaskListResponse }
     * 
     */
    public GetRecvTaskListResponse createGetRecvTaskListResponse() {
        return new GetRecvTaskListResponse();
    }

    /**
     * Create an instance of {@link DeleteRecvErrorLogResponse }
     * 
     */
    public DeleteRecvErrorLogResponse createDeleteRecvErrorLogResponse() {
        return new DeleteRecvErrorLogResponse();
    }

    /**
     * Create an instance of {@link GetFileExchangeNumByExchageTaskName }
     * 
     */
    public GetFileExchangeNumByExchageTaskName createGetFileExchangeNumByExchageTaskName() {
        return new GetFileExchangeNumByExchageTaskName();
    }

    /**
     * Create an instance of {@link GetRecvScheduleListResponse }
     * 
     */
    public GetRecvScheduleListResponse createGetRecvScheduleListResponse() {
        return new GetRecvScheduleListResponse();
    }

    /**
     * Create an instance of {@link GetRecvSchedule }
     * 
     */
    public GetRecvSchedule createGetRecvSchedule() {
        return new GetRecvSchedule();
    }

    /**
     * Create an instance of {@link AddRecvTask }
     * 
     */
    public AddRecvTask createAddRecvTask() {
        return new AddRecvTask();
    }

    /**
     * Create an instance of {@link ManualStartTask }
     * 
     */
    public ManualStartTask createManualStartTask() {
        return new ManualStartTask();
    }

    /**
     * Create an instance of {@link ExistRecvErrorLog }
     * 
     */
    public ExistRecvErrorLog createExistRecvErrorLog() {
        return new ExistRecvErrorLog();
    }

    /**
     * Create an instance of {@link GetRecvTaskContextResponse }
     * 
     */
    public GetRecvTaskContextResponse createGetRecvTaskContextResponse() {
        return new GetRecvTaskContextResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithRecvTaskParam }
     * 
     */
    public GetAssociateWithRecvTaskParam createGetAssociateWithRecvTaskParam() {
        return new GetAssociateWithRecvTaskParam();
    }

    /**
     * Create an instance of {@link GetSendMessageNumResponse }
     * 
     */
    public GetSendMessageNumResponse createGetSendMessageNumResponse() {
        return new GetSendMessageNumResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithOutRecvTaskParam }
     * 
     */
    public GetAssociateWithOutRecvTaskParam createGetAssociateWithOutRecvTaskParam() {
        return new GetAssociateWithOutRecvTaskParam();
    }

    /**
     * Create an instance of {@link GetTimestampResponse }
     * 
     */
    public GetTimestampResponse createGetTimestampResponse() {
        return new GetTimestampResponse();
    }

    /**
     * Create an instance of {@link GetErrorLogList }
     * 
     */
    public GetErrorLogList createGetErrorLogList() {
        return new GetErrorLogList();
    }

    /**
     * Create an instance of {@link DeleteSendErrorLog }
     * 
     */
    public DeleteSendErrorLog createDeleteSendErrorLog() {
        return new DeleteSendErrorLog();
    }

    /**
     * Create an instance of {@link GetSendScheduleState }
     * 
     */
    public GetSendScheduleState createGetSendScheduleState() {
        return new GetSendScheduleState();
    }

    /**
     * Create an instance of {@link AddRecvScheduleResponse }
     * 
     */
    public AddRecvScheduleResponse createAddRecvScheduleResponse() {
        return new AddRecvScheduleResponse();
    }

    /**
     * Create an instance of {@link GetFileExchangeLogListResponse }
     * 
     */
    public GetFileExchangeLogListResponse createGetFileExchangeLogListResponse() {
        return new GetFileExchangeLogListResponse();
    }

    /**
     * Create an instance of {@link GetRecvScheduleConnectionResponse }
     * 
     */
    public GetRecvScheduleConnectionResponse createGetRecvScheduleConnectionResponse() {
        return new GetRecvScheduleConnectionResponse();
    }

    /**
     * Create an instance of {@link GetErrorLogListResponse }
     * 
     */
    public GetErrorLogListResponse createGetErrorLogListResponse() {
        return new GetErrorLogListResponse();
    }

    /**
     * Create an instance of {@link UpdateRecvScheduleParameterValuesResponse }
     * 
     */
    public UpdateRecvScheduleParameterValuesResponse createUpdateRecvScheduleParameterValuesResponse() {
        return new UpdateRecvScheduleParameterValuesResponse();
    }

    /**
     * Create an instance of {@link ManualStartTaskResponse }
     * 
     */
    public ManualStartTaskResponse createManualStartTaskResponse() {
        return new ManualStartTaskResponse();
    }

    /**
     * Create an instance of {@link StartSendScheduleResponse }
     * 
     */
    public StartSendScheduleResponse createStartSendScheduleResponse() {
        return new StartSendScheduleResponse();
    }

    /**
     * Create an instance of {@link Timer }
     * 
     */
    public Timer createTimer() {
        return new Timer();
    }

    /**
     * Create an instance of {@link UpdateSendScheduleConnection }
     * 
     */
    public UpdateSendScheduleConnection createUpdateSendScheduleConnection() {
        return new UpdateSendScheduleConnection();
    }

    /**
     * Create an instance of {@link StartQueueListenerResponse }
     * 
     */
    public StartQueueListenerResponse createStartQueueListenerResponse() {
        return new StartQueueListenerResponse();
    }

    /**
     * Create an instance of {@link GetConnectionType }
     * 
     */
    public GetConnectionType createGetConnectionType() {
        return new GetConnectionType();
    }

    /**
     * Create an instance of {@link GetSendTaskContext }
     * 
     */
    public GetSendTaskContext createGetSendTaskContext() {
        return new GetSendTaskContext();
    }

    /**
     * Create an instance of {@link TestConnectionResponse }
     * 
     */
    public TestConnectionResponse createTestConnectionResponse() {
        return new TestConnectionResponse();
    }

    /**
     * Create an instance of {@link UpdateTimerResponse }
     * 
     */
    public UpdateTimerResponse createUpdateTimerResponse() {
        return new UpdateTimerResponse();
    }

    /**
     * Create an instance of {@link GetTimestamp }
     * 
     */
    public GetTimestamp createGetTimestamp() {
        return new GetTimestamp();
    }

    /**
     * Create an instance of {@link GetFileExchangeNumByScheduleName }
     * 
     */
    public GetFileExchangeNumByScheduleName createGetFileExchangeNumByScheduleName() {
        return new GetFileExchangeNumByScheduleName();
    }

    /**
     * Create an instance of {@link GetSendTaskParameterValuesResponse }
     * 
     */
    public GetSendTaskParameterValuesResponse createGetSendTaskParameterValuesResponse() {
        return new GetSendTaskParameterValuesResponse();
    }

    /**
     * Create an instance of {@link DeleteRecvSchedule }
     * 
     */
    public DeleteRecvSchedule createDeleteRecvSchedule() {
        return new DeleteRecvSchedule();
    }

    /**
     * Create an instance of {@link StopAllQueueListener }
     * 
     */
    public StopAllQueueListener createStopAllQueueListener() {
        return new StopAllQueueListener();
    }

    /**
     * Create an instance of {@link DeleteTimestamp }
     * 
     */
    public DeleteTimestamp createDeleteTimestamp() {
        return new DeleteTimestamp();
    }

    /**
     * Create an instance of {@link Schedule }
     * 
     */
    public Schedule createSchedule() {
        return new Schedule();
    }

    /**
     * Create an instance of {@link TestRecvScheduleConnectResponse }
     * 
     */
    public TestRecvScheduleConnectResponse createTestRecvScheduleConnectResponse() {
        return new TestRecvScheduleConnectResponse();
    }

    /**
     * Create an instance of {@link ExistSendErrorLogResponse }
     * 
     */
    public ExistSendErrorLogResponse createExistSendErrorLogResponse() {
        return new ExistSendErrorLogResponse();
    }

    /**
     * Create an instance of {@link GetSendScheduleConnectionResponse }
     * 
     */
    public GetSendScheduleConnectionResponse createGetSendScheduleConnectionResponse() {
        return new GetSendScheduleConnectionResponse();
    }

    /**
     * Create an instance of {@link UpdateSendTask }
     * 
     */
    public UpdateSendTask createUpdateSendTask() {
        return new UpdateSendTask();
    }

    /**
     * Create an instance of {@link GetFileExchangeLogList }
     * 
     */
    public GetFileExchangeLogList createGetFileExchangeLogList() {
        return new GetFileExchangeLogList();
    }

    /**
     * Create an instance of {@link GetSendScheduleListResponse }
     * 
     */
    public GetSendScheduleListResponse createGetSendScheduleListResponse() {
        return new GetSendScheduleListResponse();
    }

    /**
     * Create an instance of {@link GetRecvTaskResponse }
     * 
     */
    public GetRecvTaskResponse createGetRecvTaskResponse() {
        return new GetRecvTaskResponse();
    }

    /**
     * Create an instance of {@link DeployRecvTask }
     * 
     */
    public DeployRecvTask createDeployRecvTask() {
        return new DeployRecvTask();
    }

    /**
     * Create an instance of {@link GetSendMessageNum }
     * 
     */
    public GetSendMessageNum createGetSendMessageNum() {
        return new GetSendMessageNum();
    }

    /**
     * Create an instance of {@link Associate }
     * 
     */
    public Associate createAssociate() {
        return new Associate();
    }

    /**
     * Create an instance of {@link ExistRecvErrorLogResponse }
     * 
     */
    public ExistRecvErrorLogResponse createExistRecvErrorLogResponse() {
        return new ExistRecvErrorLogResponse();
    }

    /**
     * Create an instance of {@link GetRecvScheduleList }
     * 
     */
    public GetRecvScheduleList createGetRecvScheduleList() {
        return new GetRecvScheduleList();
    }

    /**
     * Create an instance of {@link GetSendSchedule }
     * 
     */
    public GetSendSchedule createGetSendSchedule() {
        return new GetSendSchedule();
    }

    /**
     * Create an instance of {@link UpdateRecvScheduleConnection }
     * 
     */
    public UpdateRecvScheduleConnection createUpdateRecvScheduleConnection() {
        return new UpdateRecvScheduleConnection();
    }

    /**
     * Create an instance of {@link DeleteRecvErrorLog }
     * 
     */
    public DeleteRecvErrorLog createDeleteRecvErrorLog() {
        return new DeleteRecvErrorLog();
    }

    /**
     * Create an instance of {@link GetSendScheduleParameterValuesResponse }
     * 
     */
    public GetSendScheduleParameterValuesResponse createGetSendScheduleParameterValuesResponse() {
        return new GetSendScheduleParameterValuesResponse();
    }

    /**
     * Create an instance of {@link StartAllQueueListener }
     * 
     */
    public StartAllQueueListener createStartAllQueueListener() {
        return new StartAllQueueListener();
    }

    /**
     * Create an instance of {@link UpdateRecvScheduleConnectionResponse }
     * 
     */
    public UpdateRecvScheduleConnectionResponse createUpdateRecvScheduleConnectionResponse() {
        return new UpdateRecvScheduleConnectionResponse();
    }

    /**
     * Create an instance of {@link GetSendTaskResponse }
     * 
     */
    public GetSendTaskResponse createGetSendTaskResponse() {
        return new GetSendTaskResponse();
    }

    /**
     * Create an instance of {@link UpdateRecvTaskParameterValuesResponse }
     * 
     */
    public UpdateRecvTaskParameterValuesResponse createUpdateRecvTaskParameterValuesResponse() {
        return new UpdateRecvTaskParameterValuesResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithOutRecvTaskParamResponse }
     * 
     */
    public GetAssociateWithOutRecvTaskParamResponse createGetAssociateWithOutRecvTaskParamResponse() {
        return new GetAssociateWithOutRecvTaskParamResponse();
    }

    /**
     * Create an instance of {@link AddTimerOnTask }
     * 
     */
    public AddTimerOnTask createAddTimerOnTask() {
        return new AddTimerOnTask();
    }

    /**
     * Create an instance of {@link AddRecvTaskResponse }
     * 
     */
    public AddRecvTaskResponse createAddRecvTaskResponse() {
        return new AddRecvTaskResponse();
    }

    /**
     * Create an instance of {@link GetLogList }
     * 
     */
    public GetLogList createGetLogList() {
        return new GetLogList();
    }

    /**
     * Create an instance of {@link StopSendSchedule }
     * 
     */
    public StopSendSchedule createStopSendSchedule() {
        return new StopSendSchedule();
    }

    /**
     * Create an instance of {@link GetRecvScheduleParameters }
     * 
     */
    public GetRecvScheduleParameters createGetRecvScheduleParameters() {
        return new GetRecvScheduleParameters();
    }

    /**
     * Create an instance of {@link UpdateSendScheduleConnectionResponse }
     * 
     */
    public UpdateSendScheduleConnectionResponse createUpdateSendScheduleConnectionResponse() {
        return new UpdateSendScheduleConnectionResponse();
    }

    /**
     * Create an instance of {@link UpdateSendTaskParameterValuesResponse }
     * 
     */
    public UpdateSendTaskParameterValuesResponse createUpdateSendTaskParameterValuesResponse() {
        return new UpdateSendTaskParameterValuesResponse();
    }

    /**
     * Create an instance of {@link DeleteSendScheduleResponse }
     * 
     */
    public DeleteSendScheduleResponse createDeleteSendScheduleResponse() {
        return new DeleteSendScheduleResponse();
    }

    /**
     * Create an instance of {@link GetSendFileNumByExchangeTaskNameResponse }
     * 
     */
    public GetSendFileNumByExchangeTaskNameResponse createGetSendFileNumByExchangeTaskNameResponse() {
        return new GetSendFileNumByExchangeTaskNameResponse();
    }

    /**
     * Create an instance of {@link AddTaskOnTimer }
     * 
     */
    public AddTaskOnTimer createAddTaskOnTimer() {
        return new AddTaskOnTimer();
    }

    /**
     * Create an instance of {@link GetRecvScheduleParameterValuesResponse }
     * 
     */
    public GetRecvScheduleParameterValuesResponse createGetRecvScheduleParameterValuesResponse() {
        return new GetRecvScheduleParameterValuesResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithRecvTaskParamResponse }
     * 
     */
    public GetAssociateWithRecvTaskParamResponse createGetAssociateWithRecvTaskParamResponse() {
        return new GetAssociateWithRecvTaskParamResponse();
    }

    /**
     * Create an instance of {@link StopSendScheduleResponse }
     * 
     */
    public StopSendScheduleResponse createStopSendScheduleResponse() {
        return new StopSendScheduleResponse();
    }

    /**
     * Create an instance of {@link AddSendScheduleResponse }
     * 
     */
    public AddSendScheduleResponse createAddSendScheduleResponse() {
        return new AddSendScheduleResponse();
    }

    /**
     * Create an instance of {@link GetAssociates }
     * 
     */
    public GetAssociates createGetAssociates() {
        return new GetAssociates();
    }

    /**
     * Create an instance of {@link GetSendScheduleStateResponse }
     * 
     */
    public GetSendScheduleStateResponse createGetSendScheduleStateResponse() {
        return new GetSendScheduleStateResponse();
    }

    /**
     * Create an instance of {@link AddRecvSchedule }
     * 
     */
    public AddRecvSchedule createAddRecvSchedule() {
        return new AddRecvSchedule();
    }

    /**
     * Create an instance of {@link GetRecvTaskList }
     * 
     */
    public GetRecvTaskList createGetRecvTaskList() {
        return new GetRecvTaskList();
    }

    /**
     * Create an instance of {@link GetRecvScheduleConnection }
     * 
     */
    public GetRecvScheduleConnection createGetRecvScheduleConnection() {
        return new GetRecvScheduleConnection();
    }

    /**
     * Create an instance of {@link GetRecvTaskContext }
     * 
     */
    public GetRecvTaskContext createGetRecvTaskContext() {
        return new GetRecvTaskContext();
    }

    /**
     * Create an instance of {@link UpdateSendScheduleParameterValues }
     * 
     */
    public UpdateSendScheduleParameterValues createUpdateSendScheduleParameterValues() {
        return new UpdateSendScheduleParameterValues();
    }

    /**
     * Create an instance of {@link TestConnection }
     * 
     */
    public TestConnection createTestConnection() {
        return new TestConnection();
    }

    /**
     * Create an instance of {@link GetSendScheduleParameters }
     * 
     */
    public GetSendScheduleParameters createGetSendScheduleParameters() {
        return new GetSendScheduleParameters();
    }

    /**
     * Create an instance of {@link DeployRecvTaskResponse }
     * 
     */
    public DeployRecvTaskResponse createDeployRecvTaskResponse() {
        return new DeployRecvTaskResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithOutSendTaskParamResponse }
     * 
     */
    public GetAssociateWithOutSendTaskParamResponse createGetAssociateWithOutSendTaskParamResponse() {
        return new GetAssociateWithOutSendTaskParamResponse();
    }

    /**
     * Create an instance of {@link GetAssociateWithOutSendTaskParam }
     * 
     */
    public GetAssociateWithOutSendTaskParam createGetAssociateWithOutSendTaskParam() {
        return new GetAssociateWithOutSendTaskParam();
    }

    /**
     * Create an instance of {@link TestSendScheduleConnect }
     * 
     */
    public TestSendScheduleConnect createTestSendScheduleConnect() {
        return new TestSendScheduleConnect();
    }

    /**
     * Create an instance of {@link GetSendScheduleResponse }
     * 
     */
    public GetSendScheduleResponse createGetSendScheduleResponse() {
        return new GetSendScheduleResponse();
    }

    /**
     * Create an instance of {@link ConnectionType }
     * 
     */
    public ConnectionType createConnectionType() {
        return new ConnectionType();
    }

    /**
     * Create an instance of {@link GetSendTaskListResponse }
     * 
     */
    public GetSendTaskListResponse createGetSendTaskListResponse() {
        return new GetSendTaskListResponse();
    }

    /**
     * Create an instance of {@link DeleteSendErrorLogResponse }
     * 
     */
    public DeleteSendErrorLogResponse createDeleteSendErrorLogResponse() {
        return new DeleteSendErrorLogResponse();
    }

    /**
     * Create an instance of {@link GetRecvScheduleResponse }
     * 
     */
    public GetRecvScheduleResponse createGetRecvScheduleResponse() {
        return new GetRecvScheduleResponse();
    }

    /**
     * Create an instance of {@link GetRecvTaskParameterValues }
     * 
     */
    public GetRecvTaskParameterValues createGetRecvTaskParameterValues() {
        return new GetRecvTaskParameterValues();
    }

    /**
     * Create an instance of {@link UpdateSendTaskResponse }
     * 
     */
    public UpdateSendTaskResponse createUpdateSendTaskResponse() {
        return new UpdateSendTaskResponse();
    }

    /**
     * Create an instance of {@link GetSendFileNumByScheduleName }
     * 
     */
    public GetSendFileNumByScheduleName createGetSendFileNumByScheduleName() {
        return new GetSendFileNumByScheduleName();
    }

    /**
     * Create an instance of {@link InitializeFileTaskResponse }
     * 
     */
    public InitializeFileTaskResponse createInitializeFileTaskResponse() {
        return new InitializeFileTaskResponse();
    }

    /**
     * Create an instance of {@link DeleteTimestampResponse }
     * 
     */
    public DeleteTimestampResponse createDeleteTimestampResponse() {
        return new DeleteTimestampResponse();
    }

    /**
     * Create an instance of {@link Task }
     * 
     */
    public Task createTask() {
        return new Task();
    }

    /**
     * Create an instance of {@link GetSendTaskParameterValues }
     * 
     */
    public GetSendTaskParameterValues createGetSendTaskParameterValues() {
        return new GetSendTaskParameterValues();
    }

    /**
     * Create an instance of {@link GetRecvTaskParameterValuesResponse }
     * 
     */
    public GetRecvTaskParameterValuesResponse createGetRecvTaskParameterValuesResponse() {
        return new GetRecvTaskParameterValuesResponse();
    }

    /**
     * Create an instance of {@link GetConnectionTypeResponse }
     * 
     */
    public GetConnectionTypeResponse createGetConnectionTypeResponse() {
        return new GetConnectionTypeResponse();
    }

    /**
     * Create an instance of {@link DeleteTimerOnTask }
     * 
     */
    public DeleteTimerOnTask createDeleteTimerOnTask() {
        return new DeleteTimerOnTask();
    }

    /**
     * Create an instance of {@link GetSendScheduleParameterValues }
     * 
     */
    public GetSendScheduleParameterValues createGetSendScheduleParameterValues() {
        return new GetSendScheduleParameterValues();
    }

    /**
     * Create an instance of {@link GetAssociatesResponse }
     * 
     */
    public GetAssociatesResponse createGetAssociatesResponse() {
        return new GetAssociatesResponse();
    }

    /**
     * Create an instance of {@link DeleteRecvTask }
     * 
     */
    public DeleteRecvTask createDeleteRecvTask() {
        return new DeleteRecvTask();
    }

    /**
     * Create an instance of {@link GetMessageLogByTaskName }
     * 
     */
    public GetMessageLogByTaskName createGetMessageLogByTaskName() {
        return new GetMessageLogByTaskName();
    }

    /**
     * Create an instance of {@link StopAllQueueListenerResponse }
     * 
     */
    public StopAllQueueListenerResponse createStopAllQueueListenerResponse() {
        return new StopAllQueueListenerResponse();
    }

    /**
     * Create an instance of {@link GetSendTaskParameters }
     * 
     */
    public GetSendTaskParameters createGetSendTaskParameters() {
        return new GetSendTaskParameters();
    }

    /**
     * Create an instance of {@link ExistSendErrorLog }
     * 
     */
    public ExistSendErrorLog createExistSendErrorLog() {
        return new ExistSendErrorLog();
    }

    /**
     * Create an instance of {@link DeleteTaskOnTimer }
     * 
     */
    public DeleteTaskOnTimer createDeleteTaskOnTimer() {
        return new DeleteTaskOnTimer();
    }

    /**
     * Create an instance of {@link GetFileExchangeNumByExchageTaskNameResponse }
     * 
     */
    public GetFileExchangeNumByExchageTaskNameResponse createGetFileExchangeNumByExchageTaskNameResponse() {
        return new GetFileExchangeNumByExchageTaskNameResponse();
    }

    /**
     * Create an instance of {@link UpdateRecvTask }
     * 
     */
    public UpdateRecvTask createUpdateRecvTask() {
        return new UpdateRecvTask();
    }

    /**
     * Create an instance of {@link GetAssociateWithSendTaskParamResponse }
     * 
     */
    public GetAssociateWithSendTaskParamResponse createGetAssociateWithSendTaskParamResponse() {
        return new GetAssociateWithSendTaskParamResponse();
    }

    /**
     * Create an instance of {@link DeleteSendSchedule }
     * 
     */
    public DeleteSendSchedule createDeleteSendSchedule() {
        return new DeleteSendSchedule();
    }

    /**
     * Create an instance of {@link StartSendSchedule }
     * 
     */
    public StartSendSchedule createStartSendSchedule() {
        return new StartSendSchedule();
    }

    /**
     * Create an instance of {@link GetFileExchangeNumByScheduleNameResponse }
     * 
     */
    public GetFileExchangeNumByScheduleNameResponse createGetFileExchangeNumByScheduleNameResponse() {
        return new GetFileExchangeNumByScheduleNameResponse();
    }

    /**
     * Create an instance of {@link GetMessageLogByTaskNameResponse }
     * 
     */
    public GetMessageLogByTaskNameResponse createGetMessageLogByTaskNameResponse() {
        return new GetMessageLogByTaskNameResponse();
    }

    /**
     * Create an instance of {@link StopQueueListenerResponse }
     * 
     */
    public StopQueueListenerResponse createStopQueueListenerResponse() {
        return new StopQueueListenerResponse();
    }

    /**
     * Create an instance of {@link UpdateTimerOnTaskResponse }
     * 
     */
    public UpdateTimerOnTaskResponse createUpdateTimerOnTaskResponse() {
        return new UpdateTimerOnTaskResponse();
    }

    /**
     * Create an instance of {@link UpdateSendScheduleParameterValuesResponse }
     * 
     */
    public UpdateSendScheduleParameterValuesResponse createUpdateSendScheduleParameterValuesResponse() {
        return new UpdateSendScheduleParameterValuesResponse();
    }

    /**
     * Create an instance of {@link UpdateRecvScheduleParameterValues }
     * 
     */
    public UpdateRecvScheduleParameterValues createUpdateRecvScheduleParameterValues() {
        return new UpdateRecvScheduleParameterValues();
    }

    /**
     * Create an instance of {@link GetAssociateWithSendTaskParam }
     * 
     */
    public GetAssociateWithSendTaskParam createGetAssociateWithSendTaskParam() {
        return new GetAssociateWithSendTaskParam();
    }

    /**
     * Create an instance of {@link GetLogListResponse }
     * 
     */
    public GetLogListResponse createGetLogListResponse() {
        return new GetLogListResponse();
    }

    /**
     * Create an instance of {@link UpdateRecvTaskResponse }
     * 
     */
    public UpdateRecvTaskResponse createUpdateRecvTaskResponse() {
        return new UpdateRecvTaskResponse();
    }

    /**
     * Create an instance of {@link GetSendTaskList }
     * 
     */
    public GetSendTaskList createGetSendTaskList() {
        return new GetSendTaskList();
    }

    /**
     * Create an instance of {@link GetSendTaskContextResponse }
     * 
     */
    public GetSendTaskContextResponse createGetSendTaskContextResponse() {
        return new GetSendTaskContextResponse();
    }

    /**
     * Create an instance of {@link DeleteTimerOnTaskResponse }
     * 
     */
    public DeleteTimerOnTaskResponse createDeleteTimerOnTaskResponse() {
        return new DeleteTimerOnTaskResponse();
    }

    /**
     * Create an instance of {@link TestRecvScheduleConnect }
     * 
     */
    public TestRecvScheduleConnect createTestRecvScheduleConnect() {
        return new TestRecvScheduleConnect();
    }

    /**
     * Create an instance of {@link TestSendScheduleConnectResponse }
     * 
     */
    public TestSendScheduleConnectResponse createTestSendScheduleConnectResponse() {
        return new TestSendScheduleConnectResponse();
    }

    /**
     * Create an instance of {@link UpdateSendTaskParameterValues }
     * 
     */
    public UpdateSendTaskParameterValues createUpdateSendTaskParameterValues() {
        return new UpdateSendTaskParameterValues();
    }

    /**
     * Create an instance of {@link GetSendScheduleConnection }
     * 
     */
    public GetSendScheduleConnection createGetSendScheduleConnection() {
        return new GetSendScheduleConnection();
    }

    /**
     * Create an instance of {@link UpdateTimerOnTask }
     * 
     */
    public UpdateTimerOnTask createUpdateTimerOnTask() {
        return new UpdateTimerOnTask();
    }

    /**
     * Create an instance of {@link GetRecvTaskParameters }
     * 
     */
    public GetRecvTaskParameters createGetRecvTaskParameters() {
        return new GetRecvTaskParameters();
    }

    /**
     * Create an instance of {@link UpdateTimer }
     * 
     */
    public UpdateTimer createUpdateTimer() {
        return new UpdateTimer();
    }

    /**
     * Create an instance of {@link DeleteRecvScheduleResponse }
     * 
     */
    public DeleteRecvScheduleResponse createDeleteRecvScheduleResponse() {
        return new DeleteRecvScheduleResponse();
    }

    /**
     * Create an instance of {@link GetRecvTask }
     * 
     */
    public GetRecvTask createGetRecvTask() {
        return new GetRecvTask();
    }

    /**
     * Create an instance of {@link Connection }
     * 
     */
    public Connection createConnection() {
        return new Connection();
    }

    /**
     * Create an instance of {@link StopQueueListener }
     * 
     */
    public StopQueueListener createStopQueueListener() {
        return new StopQueueListener();
    }

    /**
     * Create an instance of {@link StartAllQueueListenerResponse }
     * 
     */
    public StartAllQueueListenerResponse createStartAllQueueListenerResponse() {
        return new StartAllQueueListenerResponse();
    }

    /**
     * Create an instance of {@link GetSendScheduleParametersResponse }
     * 
     */
    public GetSendScheduleParametersResponse createGetSendScheduleParametersResponse() {
        return new GetSendScheduleParametersResponse();
    }

    /**
     * Create an instance of {@link DeleteRecvTaskResponse }
     * 
     */
    public DeleteRecvTaskResponse createDeleteRecvTaskResponse() {
        return new DeleteRecvTaskResponse();
    }

    /**
     * Create an instance of {@link GetRecvScheduleParameterValues }
     * 
     */
    public GetRecvScheduleParameterValues createGetRecvScheduleParameterValues() {
        return new GetRecvScheduleParameterValues();
    }

    /**
     * Create an instance of {@link GetConnectionTypeStringList }
     * 
     */
    public GetConnectionTypeStringList createGetConnectionTypeStringList() {
        return new GetConnectionTypeStringList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTimer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateTimer")
    public JAXBElement<UpdateTimer> createUpdateTimer(UpdateTimer value) {
        return new JAXBElement<UpdateTimer>(_UpdateTimer_QNAME, UpdateTimer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionTypeStringListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionTypeStringListResponse")
    public JAXBElement<GetConnectionTypeStringListResponse> createGetConnectionTypeStringListResponse(GetConnectionTypeStringListResponse value) {
        return new JAXBElement<GetConnectionTypeStringListResponse>(_GetConnectionTypeStringListResponse_QNAME, GetConnectionTypeStringListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleParameterValuesResponse")
    public JAXBElement<GetSendScheduleParameterValuesResponse> createGetSendScheduleParameterValuesResponse(GetSendScheduleParameterValuesResponse value) {
        return new JAXBElement<GetSendScheduleParameterValuesResponse>(_GetSendScheduleParameterValuesResponse_QNAME, GetSendScheduleParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTimerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateTimerResponse")
    public JAXBElement<UpdateTimerResponse> createUpdateTimerResponse(UpdateTimerResponse value) {
        return new JAXBElement<UpdateTimerResponse>(_UpdateTimerResponse_QNAME, UpdateTimerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvScheduleParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvScheduleParameterValues")
    public JAXBElement<UpdateRecvScheduleParameterValues> createUpdateRecvScheduleParameterValues(UpdateRecvScheduleParameterValues value) {
        return new JAXBElement<UpdateRecvScheduleParameterValues>(_UpdateRecvScheduleParameterValues_QNAME, UpdateRecvScheduleParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTimerOnTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTimerOnTask")
    public JAXBElement<DeleteTimerOnTask> createDeleteTimerOnTask(DeleteTimerOnTask value) {
        return new JAXBElement<DeleteTimerOnTask>(_DeleteTimerOnTask_QNAME, DeleteTimerOnTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartAllQueueListener }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startAllQueueListener")
    public JAXBElement<StartAllQueueListener> createStartAllQueueListener(StartAllQueueListener value) {
        return new JAXBElement<StartAllQueueListener>(_StartAllQueueListener_QNAME, StartAllQueueListener.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleListResponse")
    public JAXBElement<GetSendScheduleListResponse> createGetSendScheduleListResponse(GetSendScheduleListResponse value) {
        return new JAXBElement<GetSendScheduleListResponse>(_GetSendScheduleListResponse_QNAME, GetSendScheduleListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExistSendErrorLogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "existSendErrorLogResponse")
    public JAXBElement<ExistSendErrorLogResponse> createExistSendErrorLogResponse(ExistSendErrorLogResponse value) {
        return new JAXBElement<ExistSendErrorLogResponse>(_ExistSendErrorLogResponse_QNAME, ExistSendErrorLogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSendErrorLogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteSendErrorLogResponse")
    public JAXBElement<DeleteSendErrorLogResponse> createDeleteSendErrorLogResponse(DeleteSendErrorLogResponse value) {
        return new JAXBElement<DeleteSendErrorLogResponse>(_DeleteSendErrorLogResponse_QNAME, DeleteSendErrorLogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleConnectionResponse")
    public JAXBElement<GetSendScheduleConnectionResponse> createGetSendScheduleConnectionResponse(GetSendScheduleConnectionResponse value) {
        return new JAXBElement<GetSendScheduleConnectionResponse>(_GetSendScheduleConnectionResponse_QNAME, GetSendScheduleConnectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvSchedule")
    public JAXBElement<GetRecvSchedule> createGetRecvSchedule(GetRecvSchedule value) {
        return new JAXBElement<GetRecvSchedule>(_GetRecvSchedule_QNAME, GetRecvSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendTaskParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendTaskParameterValuesResponse")
    public JAXBElement<UpdateSendTaskParameterValuesResponse> createUpdateSendTaskParameterValuesResponse(UpdateSendTaskParameterValuesResponse value) {
        return new JAXBElement<UpdateSendTaskParameterValuesResponse>(_UpdateSendTaskParameterValuesResponse_QNAME, UpdateSendTaskParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSendScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteSendScheduleResponse")
    public JAXBElement<DeleteSendScheduleResponse> createDeleteSendScheduleResponse(DeleteSendScheduleResponse value) {
        return new JAXBElement<DeleteSendScheduleResponse>(_DeleteSendScheduleResponse_QNAME, DeleteSendScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecvScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addRecvScheduleResponse")
    public JAXBElement<AddRecvScheduleResponse> createAddRecvScheduleResponse(AddRecvScheduleResponse value) {
        return new JAXBElement<AddRecvScheduleResponse>(_AddRecvScheduleResponse_QNAME, AddRecvScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionTypeResponse")
    public JAXBElement<GetConnectionTypeResponse> createGetConnectionTypeResponse(GetConnectionTypeResponse value) {
        return new JAXBElement<GetConnectionTypeResponse>(_GetConnectionTypeResponse_QNAME, GetConnectionTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendFileNumByScheduleNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendFileNumByScheduleNameResponse")
    public JAXBElement<GetSendFileNumByScheduleNameResponse> createGetSendFileNumByScheduleNameResponse(GetSendFileNumByScheduleNameResponse value) {
        return new JAXBElement<GetSendFileNumByScheduleNameResponse>(_GetSendFileNumByScheduleNameResponse_QNAME, GetSendFileNumByScheduleNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializeFileTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "initializeFileTask")
    public JAXBElement<InitializeFileTask> createInitializeFileTask(InitializeFileTask value) {
        return new JAXBElement<InitializeFileTask>(_InitializeFileTask_QNAME, InitializeFileTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendMessageNum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendMessageNum")
    public JAXBElement<GetSendMessageNum> createGetSendMessageNum(GetSendMessageNum value) {
        return new JAXBElement<GetSendMessageNum>(_GetSendMessageNum_QNAME, GetSendMessageNum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTaskOnTimer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addTaskOnTimer")
    public JAXBElement<AddTaskOnTimer> createAddTaskOnTimer(AddTaskOnTimer value) {
        return new JAXBElement<AddTaskOnTimer>(_AddTaskOnTimer_QNAME, AddTaskOnTimer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskContextResponse")
    public JAXBElement<GetSendTaskContextResponse> createGetSendTaskContextResponse(GetSendTaskContextResponse value) {
        return new JAXBElement<GetSendTaskContextResponse>(_GetSendTaskContextResponse_QNAME, GetSendTaskContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskContextResponse")
    public JAXBElement<GetRecvTaskContextResponse> createGetRecvTaskContextResponse(GetRecvTaskContextResponse value) {
        return new JAXBElement<GetRecvTaskContextResponse>(_GetRecvTaskContextResponse_QNAME, GetRecvTaskContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskParameterValuesResponse")
    public JAXBElement<GetRecvTaskParameterValuesResponse> createGetRecvTaskParameterValuesResponse(GetRecvTaskParameterValuesResponse value) {
        return new JAXBElement<GetRecvTaskParameterValuesResponse>(_GetRecvTaskParameterValuesResponse_QNAME, GetRecvTaskParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestSendScheduleConnect }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testSendScheduleConnect")
    public JAXBElement<TestSendScheduleConnect> createTestSendScheduleConnect(TestSendScheduleConnect value) {
        return new JAXBElement<TestSendScheduleConnect>(_TestSendScheduleConnect_QNAME, TestSendScheduleConnect.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeployRecvTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deployRecvTask")
    public JAXBElement<DeployRecvTask> createDeployRecvTask(DeployRecvTask value) {
        return new JAXBElement<DeployRecvTask>(_DeployRecvTask_QNAME, DeployRecvTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendSchedule")
    public JAXBElement<GetSendSchedule> createGetSendSchedule(GetSendSchedule value) {
        return new JAXBElement<GetSendSchedule>(_GetSendSchedule_QNAME, GetSendSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleConnection")
    public JAXBElement<GetSendScheduleConnection> createGetSendScheduleConnection(GetSendScheduleConnection value) {
        return new JAXBElement<GetSendScheduleConnection>(_GetSendScheduleConnection_QNAME, GetSendScheduleConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestRecvScheduleConnect }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testRecvScheduleConnect")
    public JAXBElement<TestRecvScheduleConnect> createTestRecvScheduleConnect(TestRecvScheduleConnect value) {
        return new JAXBElement<TestRecvScheduleConnect>(_TestRecvScheduleConnect_QNAME, TestRecvScheduleConnect.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleListResponse")
    public JAXBElement<GetRecvScheduleListResponse> createGetRecvScheduleListResponse(GetRecvScheduleListResponse value) {
        return new JAXBElement<GetRecvScheduleListResponse>(_GetRecvScheduleListResponse_QNAME, GetRecvScheduleListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleParameters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleParameters")
    public JAXBElement<GetRecvScheduleParameters> createGetRecvScheduleParameters(GetRecvScheduleParameters value) {
        return new JAXBElement<GetRecvScheduleParameters>(_GetRecvScheduleParameters_QNAME, GetRecvScheduleParameters.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageLogByTaskName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getMessageLogByTaskName")
    public JAXBElement<GetMessageLogByTaskName> createGetMessageLogByTaskName(GetMessageLogByTaskName value) {
        return new JAXBElement<GetMessageLogByTaskName>(_GetMessageLogByTaskName_QNAME, GetMessageLogByTaskName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManualStartTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "manualStartTask")
    public JAXBElement<ManualStartTask> createManualStartTask(ManualStartTask value) {
        return new JAXBElement<ManualStartTask>(_ManualStartTask_QNAME, ManualStartTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTimerOnTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addTimerOnTaskResponse")
    public JAXBElement<AddTimerOnTaskResponse> createAddTimerOnTaskResponse(AddTimerOnTaskResponse value) {
        return new JAXBElement<AddTimerOnTaskResponse>(_AddTimerOnTaskResponse_QNAME, AddTimerOnTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopQueueListenerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopQueueListenerResponse")
    public JAXBElement<StopQueueListenerResponse> createStopQueueListenerResponse(StopQueueListenerResponse value) {
        return new JAXBElement<StopQueueListenerResponse>(_StopQueueListenerResponse_QNAME, StopQueueListenerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTimerOnTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addTimerOnTask")
    public JAXBElement<AddTimerOnTask> createAddTimerOnTask(AddTimerOnTask value) {
        return new JAXBElement<AddTimerOnTask>(_AddTimerOnTask_QNAME, AddTimerOnTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionTypeListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionTypeListResponse")
    public JAXBElement<GetConnectionTypeListResponse> createGetConnectionTypeListResponse(GetConnectionTypeListResponse value) {
        return new JAXBElement<GetConnectionTypeListResponse>(_GetConnectionTypeListResponse_QNAME, GetConnectionTypeListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskParameterValues")
    public JAXBElement<GetSendTaskParameterValues> createGetSendTaskParameterValues(GetSendTaskParameterValues value) {
        return new JAXBElement<GetSendTaskParameterValues>(_GetSendTaskParameterValues_QNAME, GetSendTaskParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociatesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociatesResponse")
    public JAXBElement<GetAssociatesResponse> createGetAssociatesResponse(GetAssociatesResponse value) {
        return new JAXBElement<GetAssociatesResponse>(_GetAssociatesResponse_QNAME, GetAssociatesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithRecvTaskParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithRecvTaskParam")
    public JAXBElement<GetAssociateWithRecvTaskParam> createGetAssociateWithRecvTaskParam(GetAssociateWithRecvTaskParam value) {
        return new JAXBElement<GetAssociateWithRecvTaskParam>(_GetAssociateWithRecvTaskParam_QNAME, GetAssociateWithRecvTaskParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTask")
    public JAXBElement<GetSendTask> createGetSendTask(GetSendTask value) {
        return new JAXBElement<GetSendTask>(_GetSendTask_QNAME, GetSendTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimestampResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getTimestampResponse")
    public JAXBElement<GetTimestampResponse> createGetTimestampResponse(GetTimestampResponse value) {
        return new JAXBElement<GetTimestampResponse>(_GetTimestampResponse_QNAME, GetTimestampResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvSchedule")
    public JAXBElement<DeleteRecvSchedule> createDeleteRecvSchedule(DeleteRecvSchedule value) {
        return new JAXBElement<DeleteRecvSchedule>(_DeleteRecvSchedule_QNAME, DeleteRecvSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvTaskParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvTaskParameterValues")
    public JAXBElement<UpdateRecvTaskParameterValues> createUpdateRecvTaskParameterValues(UpdateRecvTaskParameterValues value) {
        return new JAXBElement<UpdateRecvTaskParameterValues>(_UpdateRecvTaskParameterValues_QNAME, UpdateRecvTaskParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeLogListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeLogListResponse")
    public JAXBElement<GetFileExchangeLogListResponse> createGetFileExchangeLogListResponse(GetFileExchangeLogListResponse value) {
        return new JAXBElement<GetFileExchangeLogListResponse>(_GetFileExchangeLogListResponse_QNAME, GetFileExchangeLogListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvScheduleParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvScheduleParameterValuesResponse")
    public JAXBElement<UpdateRecvScheduleParameterValuesResponse> createUpdateRecvScheduleParameterValuesResponse(UpdateRecvScheduleParameterValuesResponse value) {
        return new JAXBElement<UpdateRecvScheduleParameterValuesResponse>(_UpdateRecvScheduleParameterValuesResponse_QNAME, UpdateRecvScheduleParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErrorLogList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getErrorLogList")
    public JAXBElement<GetErrorLogList> createGetErrorLogList(GetErrorLogList value) {
        return new JAXBElement<GetErrorLogList>(_GetErrorLogList_QNAME, GetErrorLogList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleParameters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleParameters")
    public JAXBElement<GetSendScheduleParameters> createGetSendScheduleParameters(GetSendScheduleParameters value) {
        return new JAXBElement<GetSendScheduleParameters>(_GetSendScheduleParameters_QNAME, GetSendScheduleParameters.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopSendScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopSendScheduleResponse")
    public JAXBElement<StopSendScheduleResponse> createStopSendScheduleResponse(StopSendScheduleResponse value) {
        return new JAXBElement<StopSendScheduleResponse>(_StopSendScheduleResponse_QNAME, StopSendScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestRecvScheduleConnectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testRecvScheduleConnectResponse")
    public JAXBElement<TestRecvScheduleConnectResponse> createTestRecvScheduleConnectResponse(TestRecvScheduleConnectResponse value) {
        return new JAXBElement<TestRecvScheduleConnectResponse>(_TestRecvScheduleConnectResponse_QNAME, TestRecvScheduleConnectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecvTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addRecvTaskResponse")
    public JAXBElement<AddRecvTaskResponse> createAddRecvTaskResponse(AddRecvTaskResponse value) {
        return new JAXBElement<AddRecvTaskResponse>(_AddRecvTaskResponse_QNAME, AddRecvTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskParameters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskParameters")
    public JAXBElement<GetSendTaskParameters> createGetSendTaskParameters(GetSendTaskParameters value) {
        return new JAXBElement<GetSendTaskParameters>(_GetSendTaskParameters_QNAME, GetSendTaskParameters.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopQueueListener }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopQueueListener")
    public JAXBElement<StopQueueListener> createStopQueueListener(StopQueueListener value) {
        return new JAXBElement<StopQueueListener>(_StopQueueListener_QNAME, StopQueueListener.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvTaskResponse")
    public JAXBElement<DeleteRecvTaskResponse> createDeleteRecvTaskResponse(DeleteRecvTaskResponse value) {
        return new JAXBElement<DeleteRecvTaskResponse>(_DeleteRecvTaskResponse_QNAME, DeleteRecvTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeNumByScheduleNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeNumByScheduleNameResponse")
    public JAXBElement<GetFileExchangeNumByScheduleNameResponse> createGetFileExchangeNumByScheduleNameResponse(GetFileExchangeNumByScheduleNameResponse value) {
        return new JAXBElement<GetFileExchangeNumByScheduleNameResponse>(_GetFileExchangeNumByScheduleNameResponse_QNAME, GetFileExchangeNumByScheduleNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskResponse")
    public JAXBElement<GetSendTaskResponse> createGetSendTaskResponse(GetSendTaskResponse value) {
        return new JAXBElement<GetSendTaskResponse>(_GetSendTaskResponse_QNAME, GetSendTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithSendTaskParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithSendTaskParam")
    public JAXBElement<GetAssociateWithSendTaskParam> createGetAssociateWithSendTaskParam(GetAssociateWithSendTaskParam value) {
        return new JAXBElement<GetAssociateWithSendTaskParam>(_GetAssociateWithSendTaskParam_QNAME, GetAssociateWithSendTaskParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartSendScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startSendScheduleResponse")
    public JAXBElement<StartSendScheduleResponse> createStartSendScheduleResponse(StartSendScheduleResponse value) {
        return new JAXBElement<StartSendScheduleResponse>(_StartSendScheduleResponse_QNAME, StartSendScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendFileNumByScheduleName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendFileNumByScheduleName")
    public JAXBElement<GetSendFileNumByScheduleName> createGetSendFileNumByScheduleName(GetSendFileNumByScheduleName value) {
        return new JAXBElement<GetSendFileNumByScheduleName>(_GetSendFileNumByScheduleName_QNAME, GetSendFileNumByScheduleName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskParameterValuesResponse")
    public JAXBElement<GetSendTaskParameterValuesResponse> createGetSendTaskParameterValuesResponse(GetSendTaskParameterValuesResponse value) {
        return new JAXBElement<GetSendTaskParameterValuesResponse>(_GetSendTaskParameterValuesResponse_QNAME, GetSendTaskParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializeFileTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "initializeFileTaskResponse")
    public JAXBElement<InitializeFileTaskResponse> createInitializeFileTaskResponse(InitializeFileTaskResponse value) {
        return new JAXBElement<InitializeFileTaskResponse>(_InitializeFileTaskResponse_QNAME, InitializeFileTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestSendScheduleConnectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testSendScheduleConnectResponse")
    public JAXBElement<TestSendScheduleConnectResponse> createTestSendScheduleConnectResponse(TestSendScheduleConnectResponse value) {
        return new JAXBElement<TestSendScheduleConnectResponse>(_TestSendScheduleConnectResponse_QNAME, TestSendScheduleConnectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskListResponse")
    public JAXBElement<GetRecvTaskListResponse> createGetRecvTaskListResponse(GetRecvTaskListResponse value) {
        return new JAXBElement<GetRecvTaskListResponse>(_GetRecvTaskListResponse_QNAME, GetRecvTaskListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithSendTaskParamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithSendTaskParamResponse")
    public JAXBElement<GetAssociateWithSendTaskParamResponse> createGetAssociateWithSendTaskParamResponse(GetAssociateWithSendTaskParamResponse value) {
        return new JAXBElement<GetAssociateWithSendTaskParamResponse>(_GetAssociateWithSendTaskParamResponse_QNAME, GetAssociateWithSendTaskParamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleState")
    public JAXBElement<GetSendScheduleState> createGetSendScheduleState(GetSendScheduleState value) {
        return new JAXBElement<GetSendScheduleState>(_GetSendScheduleState_QNAME, GetSendScheduleState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSendScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addSendScheduleResponse")
    public JAXBElement<AddSendScheduleResponse> createAddSendScheduleResponse(AddSendScheduleResponse value) {
        return new JAXBElement<AddSendScheduleResponse>(_AddSendScheduleResponse_QNAME, AddSendScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTimerOnTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateTimerOnTask")
    public JAXBElement<UpdateTimerOnTask> createUpdateTimerOnTask(UpdateTimerOnTask value) {
        return new JAXBElement<UpdateTimerOnTask>(_UpdateTimerOnTask_QNAME, UpdateTimerOnTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageLogByTaskNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getMessageLogByTaskNameResponse")
    public JAXBElement<GetMessageLogByTaskNameResponse> createGetMessageLogByTaskNameResponse(GetMessageLogByTaskNameResponse value) {
        return new JAXBElement<GetMessageLogByTaskNameResponse>(_GetMessageLogByTaskNameResponse_QNAME, GetMessageLogByTaskNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskParametersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskParametersResponse")
    public JAXBElement<GetRecvTaskParametersResponse> createGetRecvTaskParametersResponse(GetRecvTaskParametersResponse value) {
        return new JAXBElement<GetRecvTaskParametersResponse>(_GetRecvTaskParametersResponse_QNAME, GetRecvTaskParametersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSendSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteSendSchedule")
    public JAXBElement<DeleteSendSchedule> createDeleteSendSchedule(DeleteSendSchedule value) {
        return new JAXBElement<DeleteSendSchedule>(_DeleteSendSchedule_QNAME, DeleteSendSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartSendSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startSendSchedule")
    public JAXBElement<StartSendSchedule> createStartSendSchedule(StartSendSchedule value) {
        return new JAXBElement<StartSendSchedule>(_StartSendSchedule_QNAME, StartSendSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskList")
    public JAXBElement<GetSendTaskList> createGetSendTaskList(GetSendTaskList value) {
        return new JAXBElement<GetSendTaskList>(_GetSendTaskList_QNAME, GetSendTaskList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleParametersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleParametersResponse")
    public JAXBElement<GetSendScheduleParametersResponse> createGetSendScheduleParametersResponse(GetSendScheduleParametersResponse value) {
        return new JAXBElement<GetSendScheduleParametersResponse>(_GetSendScheduleParametersResponse_QNAME, GetSendScheduleParametersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskContext")
    public JAXBElement<GetSendTaskContext> createGetSendTaskContext(GetSendTaskContext value) {
        return new JAXBElement<GetSendTaskContext>(_GetSendTaskContext_QNAME, GetSendTaskContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleResponse")
    public JAXBElement<GetRecvScheduleResponse> createGetRecvScheduleResponse(GetRecvScheduleResponse value) {
        return new JAXBElement<GetRecvScheduleResponse>(_GetRecvScheduleResponse_QNAME, GetRecvScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleParameterValuesResponse")
    public JAXBElement<GetRecvScheduleParameterValuesResponse> createGetRecvScheduleParameterValuesResponse(GetRecvScheduleParameterValuesResponse value) {
        return new JAXBElement<GetRecvScheduleParameterValuesResponse>(_GetRecvScheduleParameterValuesResponse_QNAME, GetRecvScheduleParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleConnectionResponse")
    public JAXBElement<GetRecvScheduleConnectionResponse> createGetRecvScheduleConnectionResponse(GetRecvScheduleConnectionResponse value) {
        return new JAXBElement<GetRecvScheduleConnectionResponse>(_GetRecvScheduleConnectionResponse_QNAME, GetRecvScheduleConnectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendTaskParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendTaskParameterValues")
    public JAXBElement<UpdateSendTaskParameterValues> createUpdateSendTaskParameterValues(UpdateSendTaskParameterValues value) {
        return new JAXBElement<UpdateSendTaskParameterValues>(_UpdateSendTaskParameterValues_QNAME, UpdateSendTaskParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskListResponse")
    public JAXBElement<GetSendTaskListResponse> createGetSendTaskListResponse(GetSendTaskListResponse value) {
        return new JAXBElement<GetSendTaskListResponse>(_GetSendTaskListResponse_QNAME, GetSendTaskListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getLogList")
    public JAXBElement<GetLogList> createGetLogList(GetLogList value) {
        return new JAXBElement<GetLogList>(_GetLogList_QNAME, GetLogList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendScheduleConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendScheduleConnection")
    public JAXBElement<UpdateSendScheduleConnection> createUpdateSendScheduleConnection(UpdateSendScheduleConnection value) {
        return new JAXBElement<UpdateSendScheduleConnection>(_UpdateSendScheduleConnection_QNAME, UpdateSendScheduleConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvScheduleResponse")
    public JAXBElement<DeleteRecvScheduleResponse> createDeleteRecvScheduleResponse(DeleteRecvScheduleResponse value) {
        return new JAXBElement<DeleteRecvScheduleResponse>(_DeleteRecvScheduleResponse_QNAME, DeleteRecvScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskResponse")
    public JAXBElement<GetRecvTaskResponse> createGetRecvTaskResponse(GetRecvTaskResponse value) {
        return new JAXBElement<GetRecvTaskResponse>(_GetRecvTaskResponse_QNAME, GetRecvTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testConnectionResponse")
    public JAXBElement<TestConnectionResponse> createTestConnectionResponse(TestConnectionResponse value) {
        return new JAXBElement<TestConnectionResponse>(_TestConnectionResponse_QNAME, TestConnectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleResponse")
    public JAXBElement<GetSendScheduleResponse> createGetSendScheduleResponse(GetSendScheduleResponse value) {
        return new JAXBElement<GetSendScheduleResponse>(_GetSendScheduleResponse_QNAME, GetSendScheduleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleStateResponse")
    public JAXBElement<GetSendScheduleStateResponse> createGetSendScheduleStateResponse(GetSendScheduleStateResponse value) {
        return new JAXBElement<GetSendScheduleStateResponse>(_GetSendScheduleStateResponse_QNAME, GetSendScheduleStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendTaskParametersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendTaskParametersResponse")
    public JAXBElement<GetSendTaskParametersResponse> createGetSendTaskParametersResponse(GetSendTaskParametersResponse value) {
        return new JAXBElement<GetSendTaskParametersResponse>(_GetSendTaskParametersResponse_QNAME, GetSendTaskParametersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeNumByExchageTaskName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeNumByExchageTaskName")
    public JAXBElement<GetFileExchangeNumByExchageTaskName> createGetFileExchangeNumByExchageTaskName(GetFileExchangeNumByExchageTaskName value) {
        return new JAXBElement<GetFileExchangeNumByExchageTaskName>(_GetFileExchangeNumByExchageTaskName_QNAME, GetFileExchangeNumByExchageTaskName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvErrorLogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvErrorLogResponse")
    public JAXBElement<DeleteRecvErrorLogResponse> createDeleteRecvErrorLogResponse(DeleteRecvErrorLogResponse value) {
        return new JAXBElement<DeleteRecvErrorLogResponse>(_DeleteRecvErrorLogResponse_QNAME, DeleteRecvErrorLogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopAllQueueListener }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopAllQueueListener")
    public JAXBElement<StopAllQueueListener> createStopAllQueueListener(StopAllQueueListener value) {
        return new JAXBElement<StopAllQueueListener>(_StopAllQueueListener_QNAME, StopAllQueueListener.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTaskOnTimerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addTaskOnTimerResponse")
    public JAXBElement<AddTaskOnTimerResponse> createAddTaskOnTimerResponse(AddTaskOnTimerResponse value) {
        return new JAXBElement<AddTaskOnTimerResponse>(_AddTaskOnTimerResponse_QNAME, AddTaskOnTimerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleParameterValues")
    public JAXBElement<GetRecvScheduleParameterValues> createGetRecvScheduleParameterValues(GetRecvScheduleParameterValues value) {
        return new JAXBElement<GetRecvScheduleParameterValues>(_GetRecvScheduleParameterValues_QNAME, GetRecvScheduleParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendFileNumByExchangeTaskName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendFileNumByExchangeTaskName")
    public JAXBElement<GetSendFileNumByExchangeTaskName> createGetSendFileNumByExchangeTaskName(GetSendFileNumByExchangeTaskName value) {
        return new JAXBElement<GetSendFileNumByExchangeTaskName>(_GetSendFileNumByExchangeTaskName_QNAME, GetSendFileNumByExchangeTaskName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendTask")
    public JAXBElement<UpdateSendTask> createUpdateSendTask(UpdateSendTask value) {
        return new JAXBElement<UpdateSendTask>(_UpdateSendTask_QNAME, UpdateSendTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociates")
    public JAXBElement<GetAssociates> createGetAssociates(GetAssociates value) {
        return new JAXBElement<GetAssociates>(_GetAssociates_QNAME, GetAssociates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTaskOnTimerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTaskOnTimerResponse")
    public JAXBElement<DeleteTaskOnTimerResponse> createDeleteTaskOnTimerResponse(DeleteTaskOnTimerResponse value) {
        return new JAXBElement<DeleteTaskOnTimerResponse>(_DeleteTaskOnTimerResponse_QNAME, DeleteTaskOnTimerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSendSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addSendSchedule")
    public JAXBElement<AddSendSchedule> createAddSendSchedule(AddSendSchedule value) {
        return new JAXBElement<AddSendSchedule>(_AddSendSchedule_QNAME, AddSendSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeNumByExchageTaskNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeNumByExchageTaskNameResponse")
    public JAXBElement<GetFileExchangeNumByExchageTaskNameResponse> createGetFileExchangeNumByExchageTaskNameResponse(GetFileExchangeNumByExchageTaskNameResponse value) {
        return new JAXBElement<GetFileExchangeNumByExchageTaskNameResponse>(_GetFileExchangeNumByExchageTaskNameResponse_QNAME, GetFileExchangeNumByExchageTaskNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleParameterValues")
    public JAXBElement<GetSendScheduleParameterValues> createGetSendScheduleParameterValues(GetSendScheduleParameterValues value) {
        return new JAXBElement<GetSendScheduleParameterValues>(_GetSendScheduleParameterValues_QNAME, GetSendScheduleParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionTypeStringList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionTypeStringList")
    public JAXBElement<GetConnectionTypeStringList> createGetConnectionTypeStringList(GetConnectionTypeStringList value) {
        return new JAXBElement<GetConnectionTypeStringList>(_GetConnectionTypeStringList_QNAME, GetConnectionTypeStringList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartQueueListenerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startQueueListenerResponse")
    public JAXBElement<StartQueueListenerResponse> createStartQueueListenerResponse(StartQueueListenerResponse value) {
        return new JAXBElement<StartQueueListenerResponse>(_StartQueueListenerResponse_QNAME, StartQueueListenerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvErrorLog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvErrorLog")
    public JAXBElement<DeleteRecvErrorLog> createDeleteRecvErrorLog(DeleteRecvErrorLog value) {
        return new JAXBElement<DeleteRecvErrorLog>(_DeleteRecvErrorLog_QNAME, DeleteRecvErrorLog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartQueueListener }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startQueueListener")
    public JAXBElement<StartQueueListener> createStartQueueListener(StartQueueListener value) {
        return new JAXBElement<StartQueueListener>(_StartQueueListener_QNAME, StartQueueListener.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeNumByScheduleName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeNumByScheduleName")
    public JAXBElement<GetFileExchangeNumByScheduleName> createGetFileExchangeNumByScheduleName(GetFileExchangeNumByScheduleName value) {
        return new JAXBElement<GetFileExchangeNumByScheduleName>(_GetFileExchangeNumByScheduleName_QNAME, GetFileExchangeNumByScheduleName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopAllQueueListenerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopAllQueueListenerResponse")
    public JAXBElement<StopAllQueueListenerResponse> createStopAllQueueListenerResponse(StopAllQueueListenerResponse value) {
        return new JAXBElement<StopAllQueueListenerResponse>(_StopAllQueueListenerResponse_QNAME, StopAllQueueListenerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleList")
    public JAXBElement<GetRecvScheduleList> createGetRecvScheduleList(GetRecvScheduleList value) {
        return new JAXBElement<GetRecvScheduleList>(_GetRecvScheduleList_QNAME, GetRecvScheduleList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExistRecvErrorLog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "existRecvErrorLog")
    public JAXBElement<ExistRecvErrorLog> createExistRecvErrorLog(ExistRecvErrorLog value) {
        return new JAXBElement<ExistRecvErrorLog>(_ExistRecvErrorLog_QNAME, ExistRecvErrorLog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionType")
    public JAXBElement<GetConnectionType> createGetConnectionType(GetConnectionType value) {
        return new JAXBElement<GetConnectionType>(_GetConnectionType_QNAME, GetConnectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getLogListResponse")
    public JAXBElement<GetLogListResponse> createGetLogListResponse(GetLogListResponse value) {
        return new JAXBElement<GetLogListResponse>(_GetLogListResponse_QNAME, GetLogListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskParameterValues")
    public JAXBElement<GetRecvTaskParameterValues> createGetRecvTaskParameterValues(GetRecvTaskParameterValues value) {
        return new JAXBElement<GetRecvTaskParameterValues>(_GetRecvTaskParameterValues_QNAME, GetRecvTaskParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleConnection")
    public JAXBElement<GetRecvScheduleConnection> createGetRecvScheduleConnection(GetRecvScheduleConnection value) {
        return new JAXBElement<GetRecvScheduleConnection>(_GetRecvScheduleConnection_QNAME, GetRecvScheduleConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvScheduleConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvScheduleConnection")
    public JAXBElement<UpdateRecvScheduleConnection> createUpdateRecvScheduleConnection(UpdateRecvScheduleConnection value) {
        return new JAXBElement<UpdateRecvScheduleConnection>(_UpdateRecvScheduleConnection_QNAME, UpdateRecvScheduleConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskList")
    public JAXBElement<GetRecvTaskList> createGetRecvTaskList(GetRecvTaskList value) {
        return new JAXBElement<GetRecvTaskList>(_GetRecvTaskList_QNAME, GetRecvTaskList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTimestamp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTimestamp")
    public JAXBElement<DeleteTimestamp> createDeleteTimestamp(DeleteTimestamp value) {
        return new JAXBElement<DeleteTimestamp>(_DeleteTimestamp_QNAME, DeleteTimestamp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithOutRecvTaskParamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithOutRecvTaskParamResponse")
    public JAXBElement<GetAssociateWithOutRecvTaskParamResponse> createGetAssociateWithOutRecvTaskParamResponse(GetAssociateWithOutRecvTaskParamResponse value) {
        return new JAXBElement<GetAssociateWithOutRecvTaskParamResponse>(_GetAssociateWithOutRecvTaskParamResponse_QNAME, GetAssociateWithOutRecvTaskParamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErrorLogListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getErrorLogListResponse")
    public JAXBElement<GetErrorLogListResponse> createGetErrorLogListResponse(GetErrorLogListResponse value) {
        return new JAXBElement<GetErrorLogListResponse>(_GetErrorLogListResponse_QNAME, GetErrorLogListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManualStartTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "manualStartTaskResponse")
    public JAXBElement<ManualStartTaskResponse> createManualStartTaskResponse(ManualStartTaskResponse value) {
        return new JAXBElement<ManualStartTaskResponse>(_ManualStartTaskResponse_QNAME, ManualStartTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateTimerOnTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateTimerOnTaskResponse")
    public JAXBElement<UpdateTimerOnTaskResponse> createUpdateTimerOnTaskResponse(UpdateTimerOnTaskResponse value) {
        return new JAXBElement<UpdateTimerOnTaskResponse>(_UpdateTimerOnTaskResponse_QNAME, UpdateTimerOnTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExistRecvErrorLogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "existRecvErrorLogResponse")
    public JAXBElement<ExistRecvErrorLogResponse> createExistRecvErrorLogResponse(ExistRecvErrorLogResponse value) {
        return new JAXBElement<ExistRecvErrorLogResponse>(_ExistRecvErrorLogResponse_QNAME, ExistRecvErrorLogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecvSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addRecvSchedule")
    public JAXBElement<AddRecvSchedule> createAddRecvSchedule(AddRecvSchedule value) {
        return new JAXBElement<AddRecvSchedule>(_AddRecvSchedule_QNAME, AddRecvSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithRecvTaskParamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithRecvTaskParamResponse")
    public JAXBElement<GetAssociateWithRecvTaskParamResponse> createGetAssociateWithRecvTaskParamResponse(GetAssociateWithRecvTaskParamResponse value) {
        return new JAXBElement<GetAssociateWithRecvTaskParamResponse>(_GetAssociateWithRecvTaskParamResponse_QNAME, GetAssociateWithRecvTaskParamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithOutSendTaskParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithOutSendTaskParam")
    public JAXBElement<GetAssociateWithOutSendTaskParam> createGetAssociateWithOutSendTaskParam(GetAssociateWithOutSendTaskParam value) {
        return new JAXBElement<GetAssociateWithOutSendTaskParam>(_GetAssociateWithOutSendTaskParam_QNAME, GetAssociateWithOutSendTaskParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendScheduleList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendScheduleList")
    public JAXBElement<GetSendScheduleList> createGetSendScheduleList(GetSendScheduleList value) {
        return new JAXBElement<GetSendScheduleList>(_GetSendScheduleList_QNAME, GetSendScheduleList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvTaskParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvTaskParameterValuesResponse")
    public JAXBElement<UpdateRecvTaskParameterValuesResponse> createUpdateRecvTaskParameterValuesResponse(UpdateRecvTaskParameterValuesResponse value) {
        return new JAXBElement<UpdateRecvTaskParameterValuesResponse>(_UpdateRecvTaskParameterValuesResponse_QNAME, UpdateRecvTaskParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionTypeList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getConnectionTypeList")
    public JAXBElement<GetConnectionTypeList> createGetConnectionTypeList(GetConnectionTypeList value) {
        return new JAXBElement<GetConnectionTypeList>(_GetConnectionTypeList_QNAME, GetConnectionTypeList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskParameters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskParameters")
    public JAXBElement<GetRecvTaskParameters> createGetRecvTaskParameters(GetRecvTaskParameters value) {
        return new JAXBElement<GetRecvTaskParameters>(_GetRecvTaskParameters_QNAME, GetRecvTaskParameters.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTask")
    public JAXBElement<GetRecvTask> createGetRecvTask(GetRecvTask value) {
        return new JAXBElement<GetRecvTask>(_GetRecvTask_QNAME, GetRecvTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "testConnection")
    public JAXBElement<TestConnection> createTestConnection(TestConnection value) {
        return new JAXBElement<TestConnection>(_TestConnection_QNAME, TestConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendFileNumByExchangeTaskNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendFileNumByExchangeTaskNameResponse")
    public JAXBElement<GetSendFileNumByExchangeTaskNameResponse> createGetSendFileNumByExchangeTaskNameResponse(GetSendFileNumByExchangeTaskNameResponse value) {
        return new JAXBElement<GetSendFileNumByExchangeTaskNameResponse>(_GetSendFileNumByExchangeTaskNameResponse_QNAME, GetSendFileNumByExchangeTaskNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeployRecvTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deployRecvTaskResponse")
    public JAXBElement<DeployRecvTaskResponse> createDeployRecvTaskResponse(DeployRecvTaskResponse value) {
        return new JAXBElement<DeployRecvTaskResponse>(_DeployRecvTaskResponse_QNAME, DeployRecvTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTaskOnTimer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTaskOnTimer")
    public JAXBElement<DeleteTaskOnTimer> createDeleteTaskOnTimer(DeleteTaskOnTimer value) {
        return new JAXBElement<DeleteTaskOnTimer>(_DeleteTaskOnTimer_QNAME, DeleteTaskOnTimer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTimerOnTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTimerOnTaskResponse")
    public JAXBElement<DeleteTimerOnTaskResponse> createDeleteTimerOnTaskResponse(DeleteTimerOnTaskResponse value) {
        return new JAXBElement<DeleteTimerOnTaskResponse>(_DeleteTimerOnTaskResponse_QNAME, DeleteTimerOnTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendScheduleParameterValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendScheduleParameterValuesResponse")
    public JAXBElement<UpdateSendScheduleParameterValuesResponse> createUpdateSendScheduleParameterValuesResponse(UpdateSendScheduleParameterValuesResponse value) {
        return new JAXBElement<UpdateSendScheduleParameterValuesResponse>(_UpdateSendScheduleParameterValuesResponse_QNAME, UpdateSendScheduleParameterValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSendErrorLog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteSendErrorLog")
    public JAXBElement<DeleteSendErrorLog> createDeleteSendErrorLog(DeleteSendErrorLog value) {
        return new JAXBElement<DeleteSendErrorLog>(_DeleteSendErrorLog_QNAME, DeleteSendErrorLog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSendMessageNumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getSendMessageNumResponse")
    public JAXBElement<GetSendMessageNumResponse> createGetSendMessageNumResponse(GetSendMessageNumResponse value) {
        return new JAXBElement<GetSendMessageNumResponse>(_GetSendMessageNumResponse_QNAME, GetSendMessageNumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRecvTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteRecvTask")
    public JAXBElement<DeleteRecvTask> createDeleteRecvTask(DeleteRecvTask value) {
        return new JAXBElement<DeleteRecvTask>(_DeleteRecvTask_QNAME, DeleteRecvTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithOutSendTaskParamResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithOutSendTaskParamResponse")
    public JAXBElement<GetAssociateWithOutSendTaskParamResponse> createGetAssociateWithOutSendTaskParamResponse(GetAssociateWithOutSendTaskParamResponse value) {
        return new JAXBElement<GetAssociateWithOutSendTaskParamResponse>(_GetAssociateWithOutSendTaskParamResponse_QNAME, GetAssociateWithOutSendTaskParamResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvTaskResponse")
    public JAXBElement<UpdateRecvTaskResponse> createUpdateRecvTaskResponse(UpdateRecvTaskResponse value) {
        return new JAXBElement<UpdateRecvTaskResponse>(_UpdateRecvTaskResponse_QNAME, UpdateRecvTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendTaskResponse")
    public JAXBElement<UpdateSendTaskResponse> createUpdateSendTaskResponse(UpdateSendTaskResponse value) {
        return new JAXBElement<UpdateSendTaskResponse>(_UpdateSendTaskResponse_QNAME, UpdateSendTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAssociateWithOutRecvTaskParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getAssociateWithOutRecvTaskParam")
    public JAXBElement<GetAssociateWithOutRecvTaskParam> createGetAssociateWithOutRecvTaskParam(GetAssociateWithOutRecvTaskParam value) {
        return new JAXBElement<GetAssociateWithOutRecvTaskParam>(_GetAssociateWithOutRecvTaskParam_QNAME, GetAssociateWithOutRecvTaskParam.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileExchangeLogList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getFileExchangeLogList")
    public JAXBElement<GetFileExchangeLogList> createGetFileExchangeLogList(GetFileExchangeLogList value) {
        return new JAXBElement<GetFileExchangeLogList>(_GetFileExchangeLogList_QNAME, GetFileExchangeLogList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTimestampResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "deleteTimestampResponse")
    public JAXBElement<DeleteTimestampResponse> createDeleteTimestampResponse(DeleteTimestampResponse value) {
        return new JAXBElement<DeleteTimestampResponse>(_DeleteTimestampResponse_QNAME, DeleteTimestampResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvTask")
    public JAXBElement<UpdateRecvTask> createUpdateRecvTask(UpdateRecvTask value) {
        return new JAXBElement<UpdateRecvTask>(_UpdateRecvTask_QNAME, UpdateRecvTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvScheduleParametersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvScheduleParametersResponse")
    public JAXBElement<GetRecvScheduleParametersResponse> createGetRecvScheduleParametersResponse(GetRecvScheduleParametersResponse value) {
        return new JAXBElement<GetRecvScheduleParametersResponse>(_GetRecvScheduleParametersResponse_QNAME, GetRecvScheduleParametersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimestamp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getTimestamp")
    public JAXBElement<GetTimestamp> createGetTimestamp(GetTimestamp value) {
        return new JAXBElement<GetTimestamp>(_GetTimestamp_QNAME, GetTimestamp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRecvScheduleConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateRecvScheduleConnectionResponse")
    public JAXBElement<UpdateRecvScheduleConnectionResponse> createUpdateRecvScheduleConnectionResponse(UpdateRecvScheduleConnectionResponse value) {
        return new JAXBElement<UpdateRecvScheduleConnectionResponse>(_UpdateRecvScheduleConnectionResponse_QNAME, UpdateRecvScheduleConnectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExistSendErrorLog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "existSendErrorLog")
    public JAXBElement<ExistSendErrorLog> createExistSendErrorLog(ExistSendErrorLog value) {
        return new JAXBElement<ExistSendErrorLog>(_ExistSendErrorLog_QNAME, ExistSendErrorLog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopSendSchedule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "stopSendSchedule")
    public JAXBElement<StopSendSchedule> createStopSendSchedule(StopSendSchedule value) {
        return new JAXBElement<StopSendSchedule>(_StopSendSchedule_QNAME, StopSendSchedule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecvTaskContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "getRecvTaskContext")
    public JAXBElement<GetRecvTaskContext> createGetRecvTaskContext(GetRecvTaskContext value) {
        return new JAXBElement<GetRecvTaskContext>(_GetRecvTaskContext_QNAME, GetRecvTaskContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendScheduleParameterValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendScheduleParameterValues")
    public JAXBElement<UpdateSendScheduleParameterValues> createUpdateSendScheduleParameterValues(UpdateSendScheduleParameterValues value) {
        return new JAXBElement<UpdateSendScheduleParameterValues>(_UpdateSendScheduleParameterValues_QNAME, UpdateSendScheduleParameterValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartAllQueueListenerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "startAllQueueListenerResponse")
    public JAXBElement<StartAllQueueListenerResponse> createStartAllQueueListenerResponse(StartAllQueueListenerResponse value) {
        return new JAXBElement<StartAllQueueListenerResponse>(_StartAllQueueListenerResponse_QNAME, StartAllQueueListenerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRecvTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "addRecvTask")
    public JAXBElement<AddRecvTask> createAddRecvTask(AddRecvTask value) {
        return new JAXBElement<AddRecvTask>(_AddRecvTask_QNAME, AddRecvTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSendScheduleConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.integrator.ls.cn/", name = "updateSendScheduleConnectionResponse")
    public JAXBElement<UpdateSendScheduleConnectionResponse> createUpdateSendScheduleConnectionResponse(UpdateSendScheduleConnectionResponse value) {
        return new JAXBElement<UpdateSendScheduleConnectionResponse>(_UpdateSendScheduleConnectionResponse_QNAME, UpdateSendScheduleConnectionResponse.class, null, value);
    }

}
