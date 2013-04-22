package cn.ls.integrator.components;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.json.JsonInboundMessageMapper;

import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.handler.SafeMessageProducerSupport;
import cn.ls.integrator.core.utils.ThreadUtils;

import com.tongtech.tlq.base.TlqConnection;
import com.tongtech.tlq.base.TlqException;
import com.tongtech.tlq.base.TlqMessage;
import com.tongtech.tlq.base.TlqMsgOpt;
import com.tongtech.tlq.base.TlqQCU;
/**
 * 
* @ClassName: TongSourceComponent 
* @Description: 东方通接受组件
* @author wanl
* @date 2011-4-22 上午09:36:23 
* @version V1.0
 */
public class TongSourceComponent  extends SafeMessageProducerSupport implements MessageHandler{

	 public String QcuName;
	 public String QName;
	 public int WaitInterval;
	 public int OperateType = -1;
	 
	/**
	 * 是否正在运行
	 */
	private boolean isRuning = false;
	

	public void setQcuName(String qcuName) {
		QcuName = qcuName;
	}

	public void setQName(String qName) {
		QName = qName;
	}

	public void setWaitInterval(int waitInterval) {
		WaitInterval = waitInterval;
	}

	public void setOperateType(int operateType) {
		OperateType = operateType;
	}
	
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		//如果正在运行状态  不处理直接返回空
		if(isRuning){
			return;
		}else{
			isRuning = true;
		}
		Message<?> inMessage = null;		
		TlqQCU tlqQcu =null;
		TlqConnection tlqConnection = null;
		try {
			ThreadUtils.checkThreadInterrupted();
			TlqMessage msgInfo = new TlqMessage();
			TlqMsgOpt msgOpt = new TlqMsgOpt();
			tlqConnection = new TlqConnection();
			msgOpt.QueName = QName;
			msgOpt.WaitInterval = WaitInterval;
			tlqQcu = tlqConnection.openQCU(QcuName);
			//未赋值的情况
			if(OperateType==-1) {
				msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
			}
			//tlq收取消息
			try{
			   tlqQcu.getMessage(msgInfo, msgOpt);
			}catch(TlqException tlqException){
				logger.warn(tlqException.getErrorCause());
				return;
			}
			
			byte[] msgContent = msgInfo.getMsgData();
			String content =  new String(msgContent);
			//消息体只发List data 数据
			Class<?> clazz = ArrayList.class;
	        JsonInboundMessageMapper jsonInboundMessageMapper = new JsonInboundMessageMapper(clazz);
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
			jsonInboundMessageMapper.setObjectMapper(objectMapper);
	        inMessage = jsonInboundMessageMapper.toMessage(content);
	        //return inMessage;
		} catch (InterruptedRuntimeException e) {
			e.printStackTrace();
			logger.error("线程中断", e);
			throw e;		     
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" TongSourceComponent is fail ! " ,e);
			throw new MessagingException(message, "TongSourceComponent is fail !" ,e);
		}
		finally{
			    try {
			    	if(tlqQcu!=null)
	                    tlqQcu.close();
			    	if(tlqConnection!=null)
			    	    tlqConnection.close();
	            } catch (TlqException e) {
	                e.printStackTrace();
	                logger.warn("tlqConnection连接关闭出现异常 " ,e);
	            }
	            isRuning = false;
		}
		//Map<String, Object> headers = new HashMap<String, Object>();
		//Date date = new Date();
		//没有错误 接受消息时间
		//headers.put(IntegratorConstants.RECEIVEDATE, date);
		//inMessage = MessageBuilder.fromMessage(inMessage).copyHeaders(headers).build();
		//System.out.println("inMessage" +  inMessage);
		this.sendMessage(inMessage);
		//return inMessage;
		
	}

}
