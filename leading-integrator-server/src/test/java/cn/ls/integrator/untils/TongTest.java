/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.untils;

import java.util.ArrayList;

import org.springframework.integration.json.JsonInboundMessageMapper;

import com.tongtech.tlq.admin.remote.api.TLQConnect;
import com.tongtech.tlq.admin.remote.api.TLQOptObjFactory;
import com.tongtech.tlq.admin.remote.api.TLQParameterException;
import com.tongtech.tlq.admin.remote.api.TLQRemoteException;
import com.tongtech.tlq.base.TlqConnection;
import com.tongtech.tlq.base.TlqMessage;
import com.tongtech.tlq.base.TlqMsgOpt;
import com.tongtech.tlq.base.TlqQCU;

/**
 * 
 * 
 * @author zhoumb 2011-6-2
 */
public class TongTest {

	TLQOptObjFactory tlqFac;

	TLQConnect tlqConnect;

	public void getMegTest() {
		try {
			String qcuName = "";
			String queueName = "";
			int WaitInterval = 100;
			String ip = "";
			int port = 0;
			tlqConnect = new TLQConnect(ip, port);
			TlqConnection tlqConnection = new TlqConnection();
			TlqQCU tlqQcu = tlqConnection.openQCU(qcuName);
			TlqMessage msgInfo = new TlqMessage();
			TlqMsgOpt msgOpt = new TlqMsgOpt();
			msgOpt.QueName = queueName;
			msgOpt.WaitInterval = WaitInterval;
			msgOpt.AckMode = TlqMsgOpt.TLQACK_USER;
			msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
			tlqQcu.getMessage(msgInfo, msgOpt);
			// 收取一条消息
			byte[] msgContent = msgInfo.getMsgData();
			String content = new String(msgContent);
			Class<?> clazz = ArrayList.class;
			JsonInboundMessageMapper jsonInboundMessageMapper = new JsonInboundMessageMapper(clazz);
			int acktype = TlqMsgOpt.TLQACK_COMMIT;
			tlqQcu.ackMessage(msgInfo, msgOpt, acktype);
			System.out.println(jsonInboundMessageMapper.toMessage(content));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 连接TLQ server
	 */
	protected void connect() throws TLQParameterException, TLQRemoteException {
		tlqConnect.setRecvDataTmOut(100);
		tlqConnect.connect();
		tlqFac = new TLQOptObjFactory(tlqConnect);
	}

	/**
	 * 关闭连接TLQ控制台服务器
	 */
	protected void close() {
		try {
			if (null != tlqConnect) {
				tlqConnect.close();
				tlqConnect = null;
			}
		} catch (TLQRemoteException e) {
			e.printStackTrace();
		}
		this.tlqFac = null;
	}

}
