package cn.ls.integrator.server.qscanner.tongq;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.support.MessageBuilder;

import cn.ls.integrator.common.IntegratorConstants;
import cn.ls.integrator.components.manager.ExchangeStateManager;
import cn.ls.integrator.components.manager.impl.ExchangeStateManagerImpl;
import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.model.TaskType;
import cn.ls.integrator.core.qscanner.QueueScanner;
import cn.ls.integrator.core.task.TaskExecuter;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.tlq.base.TlqConnection;
import com.tongtech.tlq.base.TlqException;
import com.tongtech.tlq.base.TlqMessage;
import com.tongtech.tlq.base.TlqMsgOpt;
import com.tongtech.tlq.base.TlqQCU;

public class TongQueueScanner implements QueueScanner {

	private static final Logger logger = Logger.getLogger(TongQueueScanner.class);
	
	TlqConnection tlqConnection = null;
	TlqQCU tlqQcu = null;	
	
	private static final int WaitInterval = 1;
	
	private TongQueue tongQueue;
	
	public TongQueue getTongQueue() {
		return tongQueue;
	}
	
	public void setTongQueue(TongQueue tongQueue) {
		this.tongQueue = tongQueue;
	}
	
	public TongQueueScanner(TongQueue tongQueue){
		this.tongQueue = tongQueue;
	}
	/**
	 * 1：判断TongQueue 的实例对象tongQueue是否为空，是抛出IntegratorException
	 * 2:不停的尝试连接QCU，直到连接成功
	 * 3：从队列中获取消息，执行TaskExecuter.execute()
	 */
	@Override
	public void scan() {
		TlqMsgOpt msgOpt = getTlqMsgOpt();
		if(tongQueue == null){
			throw new IntegratorException("tongQueue can not be null");
		}
		
		ThreadUtils.checkThreadInterrupted();
		while(true){
			try {
				connect();
			} catch (TlqException e) {
				if(1002 == e.getTlqErrno()){
					logger.error("建立与qcu的连接失败,将在5秒后重新建立连接", e);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						throw new InterruptedRuntimeException();
					}
					continue;
				}else {
					logger.error("建立与qcu的连接失败", e);
					return ;
				}
			}
			break;
		}
		String queueName = tongQueue.getQueueName();
		String[] arr = queueName.split("_");
		if(arr.length == 4){
			queueName = arr[2];
		}
		try {
			while (true) {
				ThreadUtils.checkThreadInterrupted();
				TlqMessage msgInfo = null;
				try {
					msgInfo = getMessage(msgOpt);
					ThreadUtils.checkThreadInterrupted();
				} catch (InterruptedRuntimeException e2) {
					throw e2;
				} catch (Exception e1) {
					logger.info(e1.getMessage());
					continue;
				}
				
				Message<?> inMessage = null;
				Object scheduleObj = null;
				Object taskObj = null;
				try {
					byte[] msgContent = msgInfo.getMsgData();
//					String content = new String(msgContent);
//					JsonInboundMessageMapper jsonInboundMessageMapper = new JsonInboundMessageMapper(ArrayList.class);
					Object obj = null;
					try {
						ByteArrayInputStream bi = new ByteArrayInputStream(msgContent);
						ObjectInputStream oi = new ObjectInputStream(bi);
						obj = oi.readObject();
						bi.close();
						oi.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					inMessage = (Message<?>)obj;
					MessageHeaders headMessage = inMessage.getHeaders();
					ExchangeStateManager exchangeStateManager = ExchangeStateManagerImpl.getInstance();
					exchangeStateManager.beforeRecive(headMessage);
					scheduleObj = headMessage.get(IntegratorConstants.MESSAGE_HEADER_SCHEDULE_NAME);
					taskObj = headMessage.get(IntegratorConstants.MESSAGE_HEADER_TASK_NAME);
					Map<String, Object> newHeader = new HashMap<String, Object>();
					newHeader.put(IntegratorConstants.MESSAGE_HEADER_QUEUE_NAME, queueName);
					inMessage = MessageBuilder.fromMessage(inMessage)
							.copyHeaders(headMessage).copyHeaders(newHeader).build();
				} catch (Exception e) {
					logger.error("无法解析消息内容", e);
					askMessage(tlqQcu, msgOpt, msgInfo);
					ThreadUtils.checkThreadInterrupted();
					continue;
				}
				//执行接收任务
				try {
					if (scheduleObj != null && taskObj != null) {
						String scheduleName = scheduleObj.toString();
						String taskName = taskObj.toString();
						if (!StringUtility.isBlank(scheduleName) && !StringUtility.isBlank(taskName)) {
							ThreadUtils.checkThreadInterrupted();
							TaskExecuter.execute(scheduleName, taskName, inMessage, TaskType.recv);
						}
					}
				} catch (InterruptedRuntimeException e) {
					//删除0数据，待测
//				} catch (RuntimeException e) {
//					logger.error("********TongQueueScanner:执行任务时发生InterruptedRuntimeException异常", e);
//					MessageHeaders headMessage = inMessage.getHeaders();
//					ExchangeStateManager exchangeStateManager = ExchangeStateManagerImpl.getInstance();
//					exchangeStateManager.deleteZeroData(headMessage);//删除0数据
					throw e;
				} catch (Exception e) {
					logger.error("执行任务时发生异常", e);
					//删除0数据，待测
					//TODO 执行接收任务失败，删除产生的0条数据。
//					MessageHeaders headMessage = inMessage.getHeaders();
//					ExchangeStateManager exchangeStateManager = ExchangeStateManagerImpl.getInstance();
//					exchangeStateManager.deleteZeroData(headMessage);//删除0数据
				}finally {
					askMessage(tlqQcu, msgOpt, msgInfo);
					ThreadUtils.checkThreadInterrupted();
				}
			}
		}
		finally {
			if(tlqQcu != null){
				try {
					tlqQcu.close();
				} catch (TlqException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(tlqConnection != null){
				try {
					tlqConnection.close();
				} catch (TlqException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		

	}
	
	/**
	 * 给TLQ回复消息
	 * @param tlqQcu
	 * @param msgOpt
	 * @param msgInfo
	 */
	private void askMessage(TlqQCU qcu, TlqMsgOpt msgOpt, TlqMessage msg){
		logger.info("开始给队列回复消息");
		try {
			qcu.ackMessage(msg, msgOpt, TlqMsgOpt.TLQACK_COMMIT);
		} catch (Exception e) {
			logger.error("给队列回复消息时发生异常", e);
		}
		logger.info("给队列回复消息成功");
	}
	
	/**
	 * 从队列中获取消息
	 * @param tlqQcu
	 * @param msgOpt
	 * @return
	 * @throws Exception
	 */
	private TlqMessage getMessage(TlqMsgOpt msgOpt) throws Exception{
		TlqMessage msgInfo = new TlqMessage();
		// 收取一条消息
		logger.info("开始从队列" + tongQueue.getQueueName() + "中获取消息");
		try {
			tlqQcu.getMessage(msgInfo, msgOpt);
		} catch (TlqException e) {
			if(2603 == e.getTlqErrno()){
				logger.info("队列中没有消息");
			}else {
				logger.error("从队列" + tongQueue.getQueueName() + "中获取消息发生异常", e);
			}
			throw new IntegratorException("取消息异常" + e);
		}
		logger.info("成功从队列" + tongQueue.getQueueName() + "中获取消息");
		return msgInfo;
	}
	
	private TlqMsgOpt getTlqMsgOpt(){
		TlqMsgOpt msgOpt  = new TlqMsgOpt();
		msgOpt.QueName = tongQueue.getQueueName();
		msgOpt.WaitInterval = WaitInterval;
		msgOpt.AckMode = TlqMsgOpt.TLQACK_USER;
		msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
		return msgOpt;
	}

	/**
	 * 连接TLQ server
	 * @throws TlqException 
	 */
	private void connect() throws TlqException{
		tlqConnection = new TlqConnection();
		tlqQcu = tlqConnection.openQCU(tongQueue.getQcuName());
	}
}
