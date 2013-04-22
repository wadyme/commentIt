/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.untils;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.tongtech.tlq.admin.common.TlqPage;
import com.tongtech.tlq.admin.conf.JmsBroker;
import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.conf.NodeSystemInfo;
import com.tongtech.tlq.admin.conf.QCU;
import com.tongtech.tlq.admin.conf.RcvProcess;
import com.tongtech.tlq.admin.conf.RemoteQue;
import com.tongtech.tlq.admin.conf.SendConn;
import com.tongtech.tlq.admin.conf.SendProcess;
import com.tongtech.tlq.admin.conf.SendQue;
import com.tongtech.tlq.admin.conf.TlqConfException;
import com.tongtech.tlq.admin.conf.jndi.Factory;
import com.tongtech.tlq.admin.conf.jndi.JndiQueue;
import com.tongtech.tlq.admin.remote.api.TLQConnect;
import com.tongtech.tlq.admin.remote.api.TLQOptBaseObj;
import com.tongtech.tlq.admin.remote.api.TLQOptObjFactory;
import com.tongtech.tlq.admin.remote.api.TLQParameterException;
import com.tongtech.tlq.admin.remote.api.TLQRemoteException;
import com.tongtech.tlq.admin.remote.api.jndi.TLQOptFactory;
import com.tongtech.tlq.admin.remote.api.jndi.TLQOptJndiQueue;
import com.tongtech.tlq.admin.remote.api.node.TLQOptNodeSystem;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptLocalQue;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptQCU;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptRcvProcess;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptRemoteQue;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptSendConn;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptSendProcess;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptSendQue;
import com.tongtech.tlq.admin.remote.api.qcu.jms.TLQOptJmsBroker;

/**
 * TongQ示例程序，TongQ提供
 * 
 * @author zhoumb 2011-5-31
 */
public class TongQExample {

	@SuppressWarnings("unchecked")
	public static Set totalInfos = new HashSet();
	// 可根据类输入参数进行初始化
	public static String SendQcuName = "tsendqcu";// 发送QCU名
	public static String SendIp = "127.0.0.1";// 发送连接IP
	public static String SendQueNamePre = "que_send";// 发送队列名前缀
	public static int isSendMsg = 0;// 是否收发消息，0 不发，连接不通；1 发 连接通
	public static int initNum = 0;// 初始化队列和连接数量

	// 以下在程序里写死
	public static int debugFlag = 2;// 0,不开调试日志 1,API和应用控制台输出 2,只应用控制台和文件输出
	// 3,只应用文件输出
	public static int SendPort = 10008;// 发送连接Port
	public static String SendQueLocQue = "lq";// 发送队列对应本地队列
	public final static int recvTimeOut = 100;
	public final static int StopObjWaitTime = 360;// 360次，合计180秒
	public static int OneProcMaxConnNum = 30;// 一个发送进程最大连接数
	public static int gI = 1;
	public static int gCount = 0;
	public static int gMmaxNum = 300;// 容器中放置队列最大数
	public static int gQueNum = -1;// 队列数
	public static String gConnNamePre = "cn_";// 队列数
	public static String gSndProcId = "2";// 发送队列对应本地队列

	public static String gFlag;
	public String serverIp = "";
	public int serverPort = -1;

	static FileOutputStream outPutFileStreamd = null;
	public TLQConnect tlqConnect = null; // 不能是静态变量，一个应用实例，对应一个变量。
	public TLQOptObjFactory tlqFac = null;

	/**
	 * @功能： 设置 tlq服务器ip，port
	 * @param serverIP
	 * @param severPort
	 */
	TongQExample(String serverIP, int severPort) {
		this.serverIp = serverIP;
		this.serverPort = severPort;

	}

	/**
	 * @功能： 设置 tlq服务器ip，port
	 * @param serverIP
	 * @param severPort
	 */
	protected void setServerIpu(String serverIP, int severPort) {
		this.serverIp = serverIP;
		this.serverPort = severPort;

	}

	/**
	 * @功能： 连接TLQ server
	 */
	protected void connect() throws TLQParameterException, TLQRemoteException {
		try {
			if (this.serverIp.length() < 1)
				throw new TLQRemoteException("serverIp is not input!!");
			if (this.serverPort < 0)
				throw new TLQRemoteException("serverPort is not input!!");

			tlqConnect = new TLQConnect(this.serverIp, this.serverPort);
			if (TongQExample.debugFlag == 1) // 设置 日志开关
				tlqConnect.setIsDebug(TLQConnect.DEBUG_YES);
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@setRecvDataTmOut");
			tlqConnect.setRecvDataTmOut(100);
			tlqConnect.connect();
			tlqFac = new TLQOptObjFactory(tlqConnect);

		} catch (TLQParameterException e) {
			e.printStackTrace();
			throw e;
		} catch (TLQRemoteException e2) {
			e2.printStackTrace();
			throw e2;
		}

	}

	/**
	 * @功能：关闭连接TLQ控制台服务器
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

	/**
	 *启动 TongLink/Q
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	protected void startTlq() throws Exception {

		TLQOptNodeSystem optnode = tlqFac.getTLQOptNodeSystem();
		optnode.startNode();
		String state;
		int count = 0;

		while (true) {
			state = optnode.getNodeState();

			if (!TLQOptNodeSystem.OBJ_STATE_RUNNING.equals(state))// Qcu不处于停止状态
			{
				Thread.sleep(500);
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] start tlq Failed.....");
					break;
				}
			} else
				break;
		}

	}

	/**
	 *停止 TongLink/Q
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	protected void stopTlq() throws Exception {
		String state;
		int count = 0;

		TLQOptNodeSystem optnode = tlqFac.getTLQOptNodeSystem();

		if (TLQOptNodeSystem.OBJ_STATE_RUNNING.equals(optnode.getNodeState()))
			optnode.stopNodeByNormal();
		else
			return;

		while (true) {
			state = optnode.getNodeState();

			if (!TLQOptQCU.OBJ_STATE_STOPPED.equals(state))// 节点处于停止状态
			{
				Thread.sleep(500);
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] start tlq Failed.....");
					break;
				}
			} else
				break;
		}

	}

	/**
	 *启动Qcu
	 * 
	 * @param qcuName
	 *            qcu 名字 startQCU @throws Exception
	 */
	@SuppressWarnings("static-access")
	protected void startQcu(String qcuName) throws Exception {

		TLQOptQCU opt = tlqFac.getTLQOptQCU();
		opt.startQCU(qcuName);
		String state = null;
		int count = 0;

		while (true) {
			state = opt.queryQCUState(qcuName);

			if (!TLQOptQCU.OBJ_STATE_RUNNING.equals(state))// Qcu不处于停止状态
			{
				Thread.sleep(500);
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] start QCU " + qcuName + "Failed.....");
					break;
				}
			} else
				break;
		}

	}

	/**
	 * 停止qcu
	 * 
	 * @param qcuName
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	protected void stopQCU(String qcuName) throws Exception {
		String state = null;
		int count = 0;

		TLQOptQCU opt = tlqFac.getTLQOptQCU();
		opt.stopQCUByNormal(qcuName);

		while (true) {
			state = opt.queryQCUState(qcuName);

			if (!TLQOptQCU.OBJ_STATE_STOPPED.equals(state))// Qcu不处于停止状态
			{
				Thread.sleep(500);
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] start QCU " + qcuName + "Failed.....");
					break;
				}
			} else
				break;
		}
	}

	/**
	 * 需要动态启动 动态设置tlq节点名称
	 * 
	 * @param nodeName
	 *            节点名 ，必须输入
	 * @param sendProcMaxNum
	 *            设置节点发送进程数，可选，0 表示设置该项
	 * @throws Exception
	 */
	protected void setSysConf(String nodeName, int sendProcMaxNum) throws Exception {

		String confNodeName = null;
		int temsendprocMaxNum;

		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		if (nodeName == null || nodeName.length() <= 0)
			throw new TLQRemoteException("the input of nodeName is Error!");

		TLQOptNodeSystem nodesSysOpt = tlqFac.getTLQOptNodeSystem();
		NodeSystemInfo nodeinfo = nodesSysOpt.getNodeSystemInfo();

		// 获取节点名
		com.tongtech.tlq.admin.conf.Attribute att = nodeinfo.getNodeName();
		confNodeName = att.getValue();

		// 获取SendProcMaxNum
		com.tongtech.tlq.admin.conf.Attribute att1 = nodeinfo.getSendProcMaxNum();
		temsendprocMaxNum = Integer.parseInt(att1.getValue().toString());

		// System.out.print("SendProcMaxNum= "+temsendprocMaxNum+" confNodeName="+confNodeName);
		if (confNodeName == null)
			throw new TlqConfException("get node name failed!");

		// 如果节点的的名字不是输入名字，需要同步节点名字
		if (!confNodeName.equals(nodeName) || temsendprocMaxNum != sendProcMaxNum) {
			nodeinfo.setNodeName(nodeName);

			if (sendProcMaxNum >= 0)
				nodeinfo.setSendProcMaxNum(sendProcMaxNum);

			nodesSysOpt.setNodeSystemInfo(nodeinfo);
			// 同步nodename需要重启TongLink/Q
			stopTlq();
			startTlq();

		}

	}

	/**
	 *获取Qcu状态
	 * 
	 * @param qcuName
	 *            qcu 名字
	 * @return qcu state
	 * @throws Exception
	 */
	protected String getQCUState(String qcuName) throws Exception {

		TLQOptQCU opt = tlqFac.getTLQOptQCU();
		String state = null;
		state = opt.queryQCUState(qcuName);

		return state;

	}

	@SuppressWarnings("unchecked")
	protected String testgetQCUState(String qcuname) throws Exception {

		TLQOptQCU opt = tlqFac.getTLQOptQCU();
		String state = null;

		Map map = opt.getQCUList();

		String stat = (String) map.get(qcuname);
		System.out.println("stat=" + stat + " qcuname=" + qcuname);

		return state;

	}

	/**
	 *创建新的qcu
	 * 
	 * @param qcuName
	 */
	@SuppressWarnings("static-access")
	public void addQcu(String qcuName) throws TLQRemoteException {

		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptQCU opt = tlqFac.getTLQOptQCU();
			QCU obj = createSendQcu(qcuName);
			opt.addQCU(obj); // 添加Qcu配置
			String state = null;
			int count = 0;
			while (true) {// 等待服务端启动Qcu
				state = opt.queryQCUState(qcuName);

				if (!TLQOptQCU.OBJ_STATE_RUNNING.equals(state))// Qcu不处于工作状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] Create Qcu State" + qcuName + "Failed.....");
						break;
					}
				} else
					break;
			}
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 删除Qcu qcuName：要删除的qcu名字
	 */
	@SuppressWarnings("static-access")
	public void removeQcu(String qcuName) throws TLQRemoteException {

		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptQCU opt = tlqFac.getTLQOptQCU();
			/* Begin,add by gedy 20100928 */

			// 停止qcu
			String state1 = opt.queryQCUState(qcuName);
			if (TLQOptQCU.OBJ_STATE_RUNNING.equals(state1))// Qcu处于工作状态
				opt.stopQCUByNormal(qcuName);
			String state = null;
			int count = 0;
			while (true) {
				state = opt.queryQCUState(qcuName);

				if (!TLQOptQCU.OBJ_STATE_STOPPED.equals(state))// Qcu不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] Remove Qcu " + qcuName + "Failed.....");
						break;
					}
				} else
					break;
			}
			/* End,add by gedy 20100928 */
			opt.deleteQCUByNormal(qcuName); // 删除一个QCU
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @功能： 创建发送qcu
	 *@param：qcuName 要删除的qcu名字
	 */
	public QCU createSendQcu(String qcuName) {
		try {
			QCU qcu = new QCU();
			qcu.setQcuName(qcuName);
			qcu.setQcuStatus(1);
			qcu.setSendQueMaxNum(300);
			qcu.setDySendQueMsgNum(300000);
			qcu.setDySendQueSpaceSize(0);
			qcu.setDupFileRenameFlag(1);
			return qcu;
		} catch (TlqConfException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 *@功能: 动态增加本地队列
	 *@param qcuName
	 *            ：qcu名字
	 *@param localQueName
	 *            ： 本地队列名字
	 */
	public void addLocalQue(String qcuName, String localQueName) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptLocalQue opt = tlqFac.getTLQOptLocalQue(qcuName);
			LocalQue obj = createLocalQue(localQueName); // 创建本地队列对象
			opt.addLocalQue(obj); // 将本地队列添加到tlq服务器上
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *@功能: 动态增加远程队列
	 *@param qcuName
	 *            ：qcu名字
	 *@param remoteQueName
	 *            ： 远程队列名字
	 *@param sndQueName
	 *            远程队列对应的发送队列名字
	 *@param destQueName
	 *            远程队列对应的目的队列名字
	 */
	/**
	 * @param qcuName
	 * @param remoteQueName
	 * @param sndQueName
	 * @param destQueName
	 * @throws TLQRemoteException
	 */
	public void addRemoteQue(String qcuName, String remoteQueName, String sndQueName, String destQueName)
			throws TLQRemoteException, TlqConfException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			RemoteQue remoteQue = new RemoteQue(remoteQueName, sndQueName, destQueName); // 创建
			// 远程队列
			// remoteQue.setDestQueName(destQueName);
			TLQOptRemoteQue opt = tlqFac.getTLQOptRemoteQue(qcuName);

			opt.addRemoteQue(remoteQue); // 将远程队列添加到tlq服务器上
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *@功能: 删除远程队列
	 *@param qcuName
	 *            ：qcu名字
	 *@param remoteQueName
	 *            ： 远程队列名字
	 *@param sndQueName
	 *            远程队列对应的发送队列名字
	 *@param destQueName
	 *            远程队列对应的目的队列名字
	 */
	/**
	 * @param qcuName
	 * @param remoteQueName
	 * @throws TLQRemoteException
	 */
	public void removeRemoteQue(String qcuName, String remoteQueName) throws TLQRemoteException, TlqConfException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptRemoteQue opt = tlqFac.getTLQOptRemoteQue(qcuName);
			opt.deleteRemoteQue(remoteQueName); // 将远程队列从服务器上删除
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *@功能: 动态修改远程队列
	 *@param qcuName
	 *            ：qcu名字
	 *@param remoteQueName
	 *            ： 远程队列名字
	 *@param sndQueName
	 *            远程队列对应的发送队列名字
	 *@param destQueName
	 *            远程队列对应的目的队列名字
	 */
	/**
	 * @param qcuName
	 * @param remoteQueName
	 * @param sndQueName
	 * @param destQueName
	 * @throws TLQRemoteException
	 */
	public void modifyRemoteQue(String qcuName, String remoteQueName, String sndQueName, String destQueName)
			throws TLQRemoteException, TlqConfException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			// remoteQue.setDestQueName(destQueName);
			TLQOptRemoteQue opt = tlqFac.getTLQOptRemoteQue(qcuName);

			RemoteQue rq = opt.getRemoteQue(remoteQueName);

			if (sndQueName.length() > 0)
				rq.setSendQueName(sndQueName);
			if (destQueName.length() > 0)
				rq.setDestQueName(destQueName);
			opt.setRemoteQue(rq);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *@功能: 动态修改本地队列
	 *@param qcuName
	 *            ：qcu名字
	 *@param msgNum
	 *            ：待修改的消息数
	 *@param msgSize
	 *            待修改的消息的大小
	 */
	/**
	 * @param qcuName
	 * @param localQueName
	 * @param sndQueName
	 * @param destQueName
	 * @throws TLQRemoteException
	 */
	public void modifyLocalQue(String qcuName, String localQueName, int msgNum, int msgSize) throws TLQRemoteException,
			TlqConfException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			// remoteQue.setDestQueName(destQueName);
			TLQOptLocalQue opt = tlqFac.getTLQOptLocalQue(qcuName);

			LocalQue lq = opt.getLocalQue(localQueName);

			if (msgNum > 0)
				lq.setMsgNum(msgNum);
			if (msgSize > 0)
				lq.setMsgSize(msgSize);

			opt.setLocalQue(lq);

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @功能：动态修改发送队列
	 * @param queName
	 *            增加的发送队列名
	 * @param destQueName
	 *            目的队列名
	 * @param destQCUName
	 *            目的qcu名字
	 * @param unitcode
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public void modifySendQue(String qcuName, String sendQueName, String destQueName, String destQcuname)
			throws TLQRemoteException, TlqConfException {
		String sendprocessId = null;
		String connNameString = null;

		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			// 查到发送队列对应的发送连接
			TLQOptSendProcess optSndp = tlqFac.getTLQOptSendProcess(qcuName);
			Map mp1 = optSndp.getSendProcessList();
			Iterator it1Iterator = mp1.entrySet().iterator();

			while (it1Iterator.hasNext()) {

				String spId = it1Iterator.next().toString();
				TLQOptSendConn sendconopt = tlqFac.getTLQOptSendConn(qcuName, spId);
				Map connmp = sendconopt.getSendConnList();
				Iterator it2 = connmp.keySet().iterator();

				while (it2.hasNext()) {
					String connname = it2.next().toString();
					SendConn sc = sendconopt.getSendConn(connname);

					// 停止对应的发送连接
					if (sc.getSendQueName().valueToString().equals(sendQueName)
							&& !TLQOptSendConn.OBJ_STATE_STOPPED.equals((connmp.get(connname)))){
						this.SendConnOpt(qcuName, spId, connname, 2);

						sendprocessId = spId;
						connNameString = connname;

						break;
					}

				}

			}

			TLQOptSendQue optSq = tlqFac.getTLQOptSendQue(qcuName);

			SendQue obj = optSq.getSendQue(sendQueName);

			if (destQueName.length() > 0)
				obj.setDefDestQueName(destQueName);
			if (destQcuname.length() > 0)
				obj.setDestQCUName(destQcuname);

			obj.setMsgNum(120);// 注意消息数在动态修改过程 只能该大，不然可能会引起错误
			obj.setMsgSize(1024);

			optSq.setSendQue(obj);// 修改tlq服务器 发送队列的配置

			// 启动对应发送连接
			if (sendprocessId != null && connNameString != null) {
				this.SendConnOpt(qcuName, sendprocessId, connNameString, 1);
			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 创建发送队对象
	 * 
	 * @param queName
	 *            增加的发送队列名
	 * @param destQueName
	 *            目的队列名
	 * @param destQCUName
	 *            目的qcu名字
	 * @return SendQue
	 */
	public SendQue createSendQue(String queName, String destQueName, String destQCUName) {

		try {
			SendQue sq = new SendQue(queName);
			if (isSendMsg == 1)
				sq.setMsgNum(1000);
			else
				sq.setMsgNum(10);
			sq.setMsgSize(20480);
			sq.setDefDestQueName(destQueName);
			sq.setDefPersistence(1);
			sq.setDefPriority(4);
			// 目的QCU名称
			// sq.setDestQCUName(template.getSendQueDestQcuName(unitcode));
			sq.setDestQCUName(destQCUName);
			sq.setQueSpaceSize(0);
			sq.setSendConcurrentNum(1);
			sq.setSendQDataBuff(0);
			return sq;
		} catch (TlqConfException ex) {
			return null;
		}

	}

	/**
	 * 创建的本地队对象
	 * 
	 * @param localQueName
	 *            创建的本地队列名
	 * @return LocalQue
	 */
	public LocalQue createLocalQue(String localQueName) {

		try {
			LocalQue localQue = new LocalQue(localQueName);
			// 缺省消息属性，0为非持久，1为持久
			localQue.setDefPersistence(1);
			// 本地队列数据存储区记录数
			localQue.setLocalQueDataBuff(0);
			// 队列存放最大消息数
			localQue.setMsgNum(1000);
			// 单个消息最大长度，单位byte
			localQue.setMsgSize(1000);
			// 消息组织模式，0为先进先出，1为优先级
			localQue.setMsgArrangeMode(0);
			return localQue;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TlqConfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @功能：动态创建发送队列
	 * @param queName
	 *            增加的发送队列名
	 * @param destQueName
	 *            目的队列名
	 * @param destQCUName
	 *            目的qcu名字
	 * @param unitcode
	 * @throws TLQRemoteException
	 */
	public void addSendQue(String qcuName, String sendQueName, String destQueName, String destQcuname)
			throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(qcuName);
			SendQue obj = createSendQue(sendQueName, destQueName, destQcuname);// 创建发送队列对象
			opt.addSendQue(obj);// 将发送队列添加到tlq服务器
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @功能： 删除本地队列
	 * @param qcuName
	 *            要删除队列的qcuname
	 * @param localQueName
	 *            被删除的本地队列的名字
	 * @throws TLQRemoteException
	 */
	public void removeLocalQue(String ip, int port, String qcuName, String localQueName) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptLocalQue opt = tlqFac.getTLQOptLocalQue(qcuName);
			opt.deleteLocalQueByNormal(localQueName);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @功能: 正常删除发送队列（发送队列还可以被强制删除）
	 * @param qcuName
	 * @param sendQueName
	 *            发送队列名
	 * @throws TLQRemoteException
	 */
	public void removeSendQueByNormal(String qcuName, String sendQueName) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(qcuName); // 获取发送队列
			opt.deleteSendQueByNormal(sendQueName); // 删除发送队列
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @功能： 强制删除发送队列（有消息也能删除）
	 * @param qcuName
	 * @param sendQueName
	 *            要删除发送队列的名字
	 * @throws TLQRemoteException
	 */
	public void removeSendQueByAbort(String qcuName, String sendQueName) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(qcuName); // 获取发送队列对象
			opt.deleteSendQueByNormal(sendQueName); // 强制删除发送队列
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @功能： 删除发送连接（先停止发送连接，再删除）
	 * @param qcuName
	 * @param processId
	 *            发送连接所属发送进程ID
	 * @param sendConnName
	 *            发送连接名字
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void removeSendConn(String qcuName, String processId, String sendConnName) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuName, processId);

			/* Begin,add by gedy */
			Map map1 = opt.getSendConnList();// 获取发送连接列表
			String state1 = (String) map1.get(sendConnName);// 从表中获取发送该连接
			if (TLQOptSendConn.OBJ_STATE_RUNNING.equals(state1))// 发送连接处于工作状态，要停止
				opt.stopSendConnByNormal(sendConnName); // 停止发送连接
			int count = 0;

			// 等待服务上的发送连接停止（停止发送连接一般需要一定时间）
			while (true) {
				Map map = opt.getSendConnList();
				String state = (String) map.get(sendConnName);
				if (!TLQOptSendConn.OBJ_STATE_STOPPED.equals(state))// 发送连接不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] Remove removeSendConn qcuName[" + qcuName + "]sendConnName["
								+ sendConnName + "] Failed.....");
						break;
					}
				} else
					break;
			}
			/* End,add by gedy */
			// 当发送连接停止成功后，删除发送连接
			opt.deleteSendConnByNormal(sendConnName);

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 功能： 动态修改发送连接配置（先停止发送连接，再修改） 注意：
	 * 该方法可能需要根据业务需求，调整输入参数，如ip，port等，本示例主要展示安全修改发送连接的流程。
	 * 
	 * @param qcuName
	 * @param processId
	 *            发送连接所属发送进程ID
	 * @param sendConnName
	 *            发送连接名字
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void modifySendConn(String qcuName, String processId, String sendConnName) throws TLQRemoteException {
		try {
			// 连接监控代理
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuName, processId);
			Map map1 = opt.getSendConnList();
			String state1 = (String) map1.get(sendConnName);
			if (state1 == null)
				throw new TLQRemoteException("conn " + sendConnName + "is not exist !");
			// 停止发送连接
			if (TLQOptSendConn.OBJ_STATE_RUNNING.equals(state1))// 发送连接处于工作状态，要停止
				opt.stopSendConnByNormal(sendConnName);
			int count = 0;

			while (true) {
				Map map = opt.getSendConnList(); // 获取连接列表
				String state = (String) map.get(sendConnName);
				if (!TLQOptSendConn.OBJ_STATE_STOPPED.equals(state))// 发送连接不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error]  modifySendConn qcuName[" + qcuName + "]sendConnName["
								+ sendConnName + "] Failed.....");
						break;
					}
				} else
					break;
			}

			// 修改发送连接
			SendConn sendConn = opt.getSendConn(sendConnName);
			System.out.println("system stat=" + (Integer.parseInt(sendConn.getConnStatus().getValue())));
			if (Integer.parseInt(sendConn.getConnStatus().getValue()) != 1)
				sendConn.setConnStatus(1);// 注意一定是1
			sendConn.setConnPort(10219);
			sendConn.setHostName("127.0.1.1");

			// 设置并启动发送连接
			opt.setSendConn(sendConn);

			// opt.startSendConn(sendConnName);
			// sendConn.setConnStatus(1);//注意一定是0
			while (true) {
				Map map = opt.getSendConnList();
				String state = (String) map.get(sendConnName);
				if (!TLQOptSendConn.OBJ_STATE_RUNNING.equals(state))// 发送连接不处于工作状态
					Thread.sleep(500);
				else
					break;
			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TlqConfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 动态修改接收进程，说明:端口不能动态修改，修改后需要重启节点才能生效
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void modifyRcvProcess(String qcuName, String processId) throws TLQRemoteException, TlqConfException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptRcvProcess opt = tlqFac.getTLQOptRcvProcess(qcuName);

			/* Begin,add by gedy */
			if (!this.isExistRcvProcess(opt, processId))
				return;

			Map mp = opt.getRcvProcessList();
			String state = mp.get(processId).toString();

			if (TLQOptRcvProcess.OBJ_STATE_RUNNING.equals(state))// 接收进程处于工作状态
				opt.stopRcvProcessByNormal(processId);

			int count = 0;
			while (true) {
				mp = null;
				mp = opt.getRcvProcessList();
				state = mp.get(processId).toString();
				// System.out.print("@@rcvstat ="+state);
				if (!TLQOptRcvProcess.OBJ_STATE_STOPPED.equals(state))// 接收进程不处于停止状态
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] modifyRcvProcess qcuName[" + qcuName + "]processId["
								+ processId + "] Failed.....");
						break;
					}
				} else
					break;
			}

			RcvProcess rp = opt.getRcvProcess(processId);

			// 设置接收进程的属性
			rp.setListenPort(14708);
			rp.setLocalAddr("127.0.0.1");

			rp.setRcvProcStatus(1); // 设置为启动状态

			opt.setRcvProcess(rp);

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 获取接收进程状态
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public String getRcvProcessStatus(String qcuName, String processId) throws TLQRemoteException, TlqConfException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptRcvProcess opt = tlqFac.getTLQOptRcvProcess(qcuName);

			/* Begin,add by gedy */
			if (!this.isExistRcvProcess(opt, processId))
				return null;

			Map mp = opt.getRcvProcessList();
			String status = mp.get(processId).toString();
			return status;

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 删除发送进程
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("static-access")
	public void removeSendProcess(String qcuName, String processId) throws TLQRemoteException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendProcess opt = tlqFac.getTLQOptSendProcess(qcuName);
			/* Begin,add by gedy */
			if (!this.isExistSendProcess(opt, processId))
				return;
			String state1 = opt.querySendProcessState(processId);
			if (TLQOptQCU.OBJ_STATE_RUNNING.equals(state1))// 发送进程处于工作状态
				opt.stopSendProcessByNormal(processId);
			int count = 0;
			while (true) {

				String state = opt.querySendProcessState(processId);
				if (!TLQOptSendConn.OBJ_STATE_STOPPED.equals(state))// 发送连接不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] stop removeSendProcess qcuName[" + qcuName + "]processId["
								+ processId + "] Failed.....");
						break;
					}
				} else
					break;
			}
			/* End,add by gedy */
			opt.deleteSendProcessByNormal(processId);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *修改发送进程
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("static-access")
	public void modifySendProcess(String qcuName, String processId) throws TLQRemoteException, TlqConfException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendProcess opt = tlqFac.getTLQOptSendProcess(qcuName);
			/* Begin,add by gedy */
			if (!this.isExistSendProcess(opt, processId))
				return;
			String state1 = opt.querySendProcessState(processId);
			if (TLQOptQCU.OBJ_STATE_RUNNING.equals(state1))// 发送进程处于工作状态
				opt.stopSendProcessByNormal(processId);
			int count = 0;
			while (true) {

				String state = opt.querySendProcessState(processId);
				if (!TLQOptSendProcess.OBJ_STATE_STOPPED.equals(state))// 发送连接不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] modifySendProcess qcuName[" + qcuName + "]processId["
								+ processId + "] Failed.....");
						break;
					}
				} else
					break;
			}
			/* End,add by gedy */
			// 修改发送进程
			SendProcess sp = opt.getSendProcess(processId);

			sp.setSendProcID(1); // 修改发送进程ID
			sp.setSendProcStatus(1); // 修改发送进程是否启动

			opt.deleteSendProcessByNormal(processId);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *修改发送进程
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	public String getSendProcessStatus(String qcuName, String processId) throws TLQRemoteException, TlqConfException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendProcess opt = tlqFac.getTLQOptSendProcess(qcuName);
			/* Begin,add by gedy */
			if (!this.isExistSendProcess(opt, processId))
				return null;
			String status = opt.querySendProcessState(processId);

			return status;

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 删除接收进程
	 * 
	 * @param qcuName
	 * @param processId
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void removeRcvProcess(String qcuName, String processId) throws TLQRemoteException {
		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptRcvProcess opt = tlqFac.getTLQOptRcvProcess(qcuName);

			/* Begin,add by gedy */
			if (!this.isExistRcvProcess(opt, processId))
				return;

			Map mp = opt.getRcvProcessList();
			String state = mp.get(processId).toString();

			if (TLQOptRcvProcess.OBJ_STATE_RUNNING.equals(state))// 接收进程处于工作状
				opt.stopRcvProcessByNormal(processId);

			int count = 0;
			while (true) {
				mp = opt.getRcvProcessList();
				state = mp.get(processId).toString();

				if (!TLQOptRcvProcess.OBJ_STATE_STOPPED.equals(state))// 接收进程不处于停止状态
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] removeRcvProcess qcuName[" + qcuName + "]processId["
								+ processId + "] Failed.....");
						break;
					}
				} else
					break;
			}

			opt.deleteRcvProcessByAbort(processId);

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 增加发送进程
	 * 
	 * @param qcuName
	 * @param processId
	 *            要增加的发送进程的编号
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("static-access")
	public void addSendProcess(String qcuName, int processId) throws TLQRemoteException {
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendProcess opt = tlqFac.getTLQOptSendProcess(qcuName);
			SendProcess obj = createSendProcess(processId); // 创建发送进程对象
			opt.addSendProcess(obj);// 将发送进程添加到tlq节点上去

			String state = null;
			int count = 0;
			while (true) {
				state = opt.querySendProcessState(String.valueOf(processId));

				if (!TLQOptSendProcess.OBJ_STATE_RUNNING.equals(state))// Qcu不处于工作状态
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] Create Qcu State" + qcuName + "Failed.....");
						break;
					}
				} else
					break;
			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建发送进程对象
	 * 
	 * @param processID
	 * @return
	 */
	public SendProcess createSendProcess(int processID) {
		SendProcess process = new SendProcess();
		try {
			process.setSendProcID(processID);
			process.setSendProcStatus(1);
		} catch (TlqConfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return process;
	}

	/**
	 * 添加发送连接
	 * 
	 * @param sendQcu
	 * @param sendProcessId
	 * @param sendConnName
	 * @param sendQueName
	 * @param hostName
	 * @param connectPort
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void addSendConn(String sendQcu, String sendProcessId, String sendConnName, String sendQueName,
			String hostName, int connectPort) throws TLQRemoteException {

		try {

			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(sendQcu, sendProcessId);
			SendConn obj = createSendConn(sendConnName, sendQueName, hostName, connectPort); // 创建发送连接
			opt.addSendConn(obj); // 将发送连接添加到tlq节点中去

			int count = 0;
			// 查看发送连接状态是否是启动状态
			while (true) {
				Map map = opt.getSendConnList();
				String state = (String) map.get(sendConnName);
				if (!TLQOptSendConn.OBJ_STATE_RUNNING.equals(state))// 发送连接不处于工作状态
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] addSendConn qcuName[" + sendQcu + "]sendConnName["
								+ sendConnName + "] Failed.....");
						break;
					}
				} else
					break;
			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建发送连接对象
	 * 
	 * @param connName
	 * @param sendQueName
	 * @param hostName
	 * @param connectPort
	 * @return
	 */
	public SendConn createSendConn(String connName, String sendQueName, String hostName, int connectPort) {
		try {
			SendConn conn = new SendConn(connName, sendQueName);
			// 发送连接状态，0禁用，1正常
			conn.setConnStatus(1);
			conn.setConnPort(connectPort);
			conn.setHostName(hostName);
			// 连接类型,0为常连接，1为按需连接
			conn.setConnType(1);
			// 线路维持时间
			conn.setDiscInterval(20);
			conn.setConnTime(10);
			conn.setBeatInterval(10);
			conn.setReplyTmout(10);
			conn.setSendBlockSize(8);
			conn.setSendBuff(65535);
			return conn;
		} catch (TlqConfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 创建接收进程
	 * 
	 * @param qcuName
	 * @param rcvProcID
	 * @param listenPort
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void addRcvProcess(String qcuName, int rcvProcID, int listenPort) throws TLQRemoteException {

		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptRcvProcess opt = tlqFac.getTLQOptRcvProcess(qcuName);
			RcvProcess obj = new RcvProcess(); // 创建接收进程对象
			obj.setRcvProcID(rcvProcID); // 设置接收进程编号
			// 接收进程状态，0禁用，1正常
			obj.setListenPort(listenPort); // 设置接收进程的监听端口
			obj.setRcvProcStatus(1);
			opt.addRcvProcess(obj); // 将接收进程添加到tlq配置中去

			int count = 0;
			// 等待进程进程启动，确保创建接收进程正常
			while (true) {
				Map map = opt.getRcvProcessList();
				String state = (String) map.get(String.valueOf(rcvProcID));
				if (!TLQOptRcvProcess.OBJ_STATE_RUNNING.equals(state))// 接收不处于工作状态
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] addRcvProcess  qcuName[" + qcuName + "]rcvProcID["
								+ rcvProcID + "] Failed.....");
						break;
					}
				} else
					break;
			}
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TlqConfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 判断qcu是否存在
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistQcu(String qcuName) throws TLQRemoteException {
		if (qcuName == null) {
			return false;
		}
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptQCU opt = tlqFac.getTLQOptQCU();
		Map map = opt.getQCUList();
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			if (qcuName.equals(key)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 判断发送进程是否存在
	 * 
	 * @param opt
	 * @param processId
	 *            发送进程的ID
	 * @return
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistSendProcess(TLQOptSendProcess opt, String processId) throws TLQRemoteException {
		if (processId == null) {
			return false;
		}

		Map map = opt.getSendProcessList();
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			// System.out.println(key);
			if (processId.equals(key)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 判断接收进程是否存在
	 * 
	 * @param opt
	 * @param processId
	 *            发送进程的ID
	 * @return
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistRcvProcess(TLQOptRcvProcess opt, String processId) throws TLQRemoteException {
		if (processId == null) {
			return false;
		}

		Map map = opt.getRcvProcessList();
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			// System.out.println(key);
			if (processId.equals(key)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 监控发送连接是否存在
	 * 
	 * @param opt
	 * @param connName
	 * @return
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistSendConn(String qcuname, String processId, String connName) throws Exception {
		if (qcuname == null || processId == null) {
			return false;
		}
		try {

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuname, processId);

			Map map = opt.getSendConnList();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next().toString();
				// System.out.println(key);
				if (connName.equals(key)) {
					return true;
				}
			}
		} catch (TLQRemoteException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}

		return false;

	}

	/**
	 * 监控发送队列是否存在
	 * 
	 * @param opt
	 * @param sendQueName
	 * @return
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistSendQue(String Qcuname, String sendQueName) throws Exception {
		if (sendQueName == null) {
			return false;
		}
		try {
			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(Qcuname);

			Map map = opt.getSendQueList();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next().toString();
				// System.out.println(key);
				if (sendQueName.equals(key)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

	}

	/**
	 * 监控远程队列是否存在
	 * 
	 * @param Qcuname
	 *            qcu 名字
	 * @param RemoteQueName
	 * @return
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistRemoteQue(String Qcuname, String RemoteQueName) throws Exception {
		if (RemoteQueName == null) {
			return false;
		}
		try {
			TLQOptRemoteQue opt = tlqFac.getTLQOptRemoteQue(Qcuname);

			Map map = opt.getRemoteQueList();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next().toString();
				// System.out.println(key);
				if (RemoteQueName.equals(key)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

	}

	/**
	 * 通过远程接口来做发送连接状态检查
	 * 
	 * @param ip
	 * @param port
	 * @param tlqInfo
	 * @return
	 */
	public boolean testLine(String QcuName, String ProcessId, String ConnName) throws TLQRemoteException {
		int status = -1;

		try {
			/*
			 * if(this.tlqFac==null) throw new
			 * TLQRemoteException("not conn!!!");
			 */

			this.connect();
			TLQOptSendConn tlqOptSendConn = tlqFac.getTLQOptSendConn(QcuName, ProcessId);
			status = tlqOptSendConn.testLine(ConnName, 10);
		} catch (TLQParameterException e) {
			if (!e.getMessage().contains("SendCONN can not connec"))
				e.printStackTrace();
		} catch (TLQRemoteException e) {
			if (!e.getMessage().contains("SendCONN can not connec"))
				e.printStackTrace();
		} finally {

			this.close();
		}

		return status == 0;
	}

	/**
	 * 判断发送队列是否可用： 网络是否连通、是否有剩余空间 可以增加更多的策略：消息数，date空间判断，。
	 * 
	 * @param ip
	 * @param port
	 * @param tlqInfo
	 * @return
	 */
	public boolean testSenQueIsUsable(String QcuName, String sendQueName, String ConnName, String processId) {

		int status = -1;
		int max = 0;
		TLQConnect tlqConnect = null;
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(QcuName);
			max = Integer.parseInt(opt.getSendQueSpareInfo(sendQueName).get("RemainingMessageNum").toString());
			TLQOptSendConn tlqOptSendConn = tlqFac.getTLQOptSendConn(QcuName, processId);
			status = tlqOptSendConn.testLine(ConnName, 10);
		} catch (TLQParameterException e) {
			e.printStackTrace();
		} catch (TLQRemoteException e) {
			e.printStackTrace();
		} finally {
			if (tlqConnect == null) {
				
			}else{
				try {
					tlqConnect.close();
				} catch (TLQRemoteException e) {
					e.printStackTrace();
				}
			}
		}
		if (status == 0 && max > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 测试发送队列是否满了
	 * 
	 * @param ip
	 * @param port
	 * @param tlqInfo
	 * @return
	 */
	public boolean testSendQuneneIsFull(String QcuName, String sendQueName) {

		int max = 0;
		TLQConnect tlqConnect = null;
		try {
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendQue opt = tlqFac.getTLQOptSendQue(QcuName);
			max = Integer.parseInt(opt.getSendQueSpareInfo(sendQueName).get("RemainingMessageNum").toString());// 获取队列剩余空间
		} catch (TLQParameterException e) {
			e.printStackTrace();
		} catch (TLQRemoteException e) {
			e.printStackTrace();
		} finally {
			if (tlqConnect == null) {
				
			} else {
				try {
					tlqConnect.close();
				} catch (TLQRemoteException e) {
					e.printStackTrace();
				}
			}
		}

		return max > 0 ? false : true;
	}

	/**
	 * 查询指定本地队列的当前消息数
	 * 
	 * @param qcuName
	 * @param queueName
	 *            队列名
	 * @param value
	 *            0表示 无条件查询；>0 为有条件查询 查询指定本地队列的当前消息数
	 * @throws TLQParameterException
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	private int getMsgnumOfLoaclQue(String qcuName, String queueName, int value) throws TLQParameterException,
			TLQRemoteException {

		TLQOptLocalQue opt = tlqFac.getTLQOptLocalQue(qcuName);
		if (value > 0) {
			Map map = opt.getLocalQueMsgNum(queueName, opt.MATCH_BIG, 0);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				System.out.println(e.getKey());
				System.out.println("--------------------");
				System.out.println("msgnumber=" + e.getValue());
				System.out.println("====================");
			}
		} else if (value < 0) {
			Map map = opt.getLocalQueMsgNum(queueName, opt.MATCH_NO, 0);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				System.out.println(e.getKey());
				System.out.println("--------------------");
				System.out.println("msgnumber=" + e.getValue());
				System.out.println("====================");
			}
		} else {
			Map map = opt.getLocalQueMsgNum(queueName, opt.MATCH_NO, 0);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				System.out.println(e.getKey());
				System.out.println("--------------------");
				System.out.println("msgnumber=" + e.getValue());
				System.out.println("====================");
			}
		}
		return 0;

	}

	/**
	 * 
	 * @param qcuName
	 * @return Properties 数据项的意义说明 说明：从系统启动开始统计，重启清零 rcvBufSuccessNum
	 *         接收成功的buffer消息数 rcvFileSuccessNum 接收成功的文件消息数 sndBufSuccessNum
	 *         发送成功的buffer消息数 rcvFileFailNum 接收失败的文件消息数 sndFileFailNum
	 *         发送失败的文件消息数 rcvBufFailNum 接收失败的buffer消息数 sndFileSuccessNum
	 *         发送成功的文件消息数 sndBufFailNum 发送成功的buffer消息数
	 * 
	 * @throws TLQParameterException
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unused")
	private Properties getStatQCUHistoryMsgNum(String qcuName) throws TLQParameterException, TLQRemoteException {

		TLQOptQCU opt = this.tlqFac.getTLQOptQCU();

		Properties prop = opt.statQCUHistoryMsgNum(qcuName);
		if (prop == null)
			throw new TLQParameterException("get qcu history msg num");

		return prop;

	}

	/**
	 * 功能：停止或者发送连接 注意： 该方法可能需要根据业务需求，调整输入参数，如ip，port等，本示例主要展示安全修改发送连接的流程。
	 * 
	 * @param qcuName
	 * @param processId
	 *            发送连接所属发送进程ID
	 * @param sendConnName
	 *            发送连接名字 optflag 1 为启动，2停止发送连接
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void SendConnOpt(String qcuName, String processId, String sendConnName, int optflag)
			throws TLQRemoteException {
		try {
			// 连接监控代理
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");
			if (optflag != 1 && optflag != 2)
				throw new TLQRemoteException("the value of optflag  is eror!! ");

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuName, processId);
			Map map1 = opt.getSendConnList();
			String state1 = (String) map1.get(sendConnName);
			if (state1 == null)
				throw new TLQRemoteException("conn " + sendConnName + "is not exist !");
			// 停止发送连接
			if (optflag == 2) {
				if (TLQOptSendConn.OBJ_STATE_RUNNING.equals(state1))// 发送连接处于工作状态，要停止
					opt.stopSendConnByNormal(sendConnName);
			} else {
				if (!TLQOptSendConn.OBJ_STATE_RUNNING.equals(state1))// 发送连接
					// 没有启动
					// ，需要启动
					opt.startSendConn(sendConnName);
			}
			int count = 0;

			while (true) {
				Map map = opt.getSendConnList(); // 获取连接列表
				String state = (String) map.get(sendConnName);

				if (optflag == 2 && !TLQOptSendConn.OBJ_STATE_STOPPED.equals(state) || optflag == 1
						&& !TLQOptSendConn.OBJ_STATE_RUNNING.equals(state))// 发送连接不处于停止状态
				{
					Thread.sleep(500);
					count++;
					if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
					{
						System.out.println("******[Error] SendConnOpt qcuName[" + qcuName + "]sendConnName["
								+ sendConnName + "] Failed.....");
						break;
					}
				} else
					break;
			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 功能 新建连接工厂
	 * 
	 * @param connectionName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 * @throws TlqConfException
	 */
	public void addJndiFactory(String connectionName) throws TLQRemoteException, TLQParameterException,
			TlqConfException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

//		TLQOptJndiSystem optJndiSystem = this.tlqFac.getTLQOptJndiSystem();
		TLQOptFactory optFactory = this.tlqFac.getTLQOptFactory();
		Factory fc = new Factory();

		// 设置 连接工厂属性
		fc.setFacName(connectionName);
		fc.setTmqiAddressList("127.0.0.1"); // 设置连接url
		fc.setTmqiAckTimeout(100000);

		optFactory.addFactory(fc);

	}

	/**
	 * 功能 删除连接工厂
	 * 
	 * @param connectionName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 * @throws TlqConfException
	 */
	public void removeJndiFactory(String connectionName) throws TLQRemoteException, TLQParameterException,
			TlqConfException {

		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

//		TLQOptJndiSystem optJndiSystem = this.tlqFac.getTLQOptJndiSystem();
		TLQOptFactory optFactory = this.tlqFac.getTLQOptFactory();

		// 删除连接工厂
		if (this.isExistJndiFactory(connectionName))
			optFactory.deleteFactory(connectionName);

	}

	/**
	 * 功能 修改连接工厂
	 * 
	 * @param connectionName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	public void modifyJndiFactory(String connectionName) throws TLQRemoteException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

//		TLQOptJndiSystem optJndiSystem = this.tlqFac.getTLQOptJndiSystem();
		TLQOptFactory optFactory = this.tlqFac.getTLQOptFactory();
		Factory fc = optFactory.getFactory(connectionName);

		// 设置 连接工厂属性
		fc.setTmqiAddressList("168.1.1.10"); // 设置连接url
		fc.setTmqiAckTimeout(100000);

		optFactory.setFactory(fc);

	}

	/**
	 * 功能 判断jndi 连接工厂是否存在
	 * 
	 * @param connectionName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistJndiFactory(String connectionName) throws TLQRemoteException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

//		TLQOptJndiSystem optJndiSystem = this.tlqFac.getTLQOptJndiSystem();
		TLQOptFactory optFactory = this.tlqFac.getTLQOptFactory();

		Map faclist = optFactory.getFactoryList();
		Iterator it = faclist.keySet().iterator();
		while (it.hasNext()) {
			String connName = it.next().toString();
			if (connName.equals(connectionName))
				return true;
		}

		return false;

	}

	/**
	 * 功能 增加jndi Queue
	 * 
	 * @throws TLQRemoteException
	 * @throws TlqConfException
	 * @throws TLQParameterException
	 */
	public void addJnidQue(String JndiQueueName) throws TLQRemoteException, TlqConfException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJndiQueue optJndiQueue = this.tlqFac.getTLQOptJndiQueue();

		JndiQueue jndiQue = new JndiQueue();

		jndiQue.setJndiQueueName(JndiQueueName);
		jndiQue.setTlqQueueName("lq"); // 映射的ToingLink
		optJndiQueue.addJndiQueue(jndiQue);
	}

	/**
	 * 功能 修改jndi Queue 配置
	 * 
	 * @param JndiQueueName
	 * @throws TLQRemoteException
	 * @throws TlqConfException
	 * @throws TLQParameterException
	 */
	public void modifyJnidQue(String JndiQueueName) throws TLQRemoteException, TlqConfException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJndiQueue optJndiQueue = this.tlqFac.getTLQOptJndiQueue();

		JndiQueue jndiQue = optJndiQueue.getJndiQueue(JndiQueueName);

		jndiQue.setJndiQueueName(JndiQueueName);
		jndiQue.setTlqQueueName("lq"); // 映射的ToingLink/Q队列名

		optJndiQueue.setJndiQueue(jndiQue);

	}

	/**
	 * 功能 删除jndi Queue
	 * 
	 * @param JndiQueueName
	 * @throws TLQRemoteException
	 * @throws TlqConfException
	 * @throws TLQParameterException
	 */

	public void removeJnidQue(String JndiQueueName) throws TLQRemoteException, TlqConfException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJndiQueue optJndiQueue = this.tlqFac.getTLQOptJndiQueue();

		if (this.isExistJnidQue(JndiQueueName))
			optJndiQueue.deleteJndiQueue(JndiQueueName);

	}

	/**
	 * 功能 判断jndi Queue 是否存在
	 * 
	 * @param JndiQueueName
	 * @throws TLQRemoteException
	 * @throws TlqConfException
	 * @throws TLQParameterException
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistJnidQue(String JndiQueueName) throws TLQRemoteException, TlqConfException,
			TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJndiQueue optJndiQueue = this.tlqFac.getTLQOptJndiQueue();

		Map jndiquelist = optJndiQueue.getJndiQueueList();
		Iterator it = jndiquelist.keySet().iterator();
		while (it.hasNext()) {
			String queName = it.next().toString();
			if (queName.equals(JndiQueueName))
				return true;
		}

		return false;

	}

	/**
	 * 功能： 创建jms代理
	 * 
	 * @param QcuName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	public void addJMSBrokerConf(String QcuName) throws TLQRemoteException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJmsBroker optJMSbroker = this.tlqFac.getTLQOptJmsBroker(QcuName);

		JmsBroker broker = new JmsBroker();
		broker.setListenPort(10024);

		optJMSbroker.addJmsBroker(broker);
		/* optJMSbroker.startJmsBroker(); */

	}

	/**
	 * 功能 修改jms代理
	 * 
	 * @param QcuName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	@SuppressWarnings("static-access")
	public void modifyJMSBrokerConf(String QcuName) throws TLQRemoteException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJmsBroker optJMSbroker = this.tlqFac.getTLQOptJmsBroker(QcuName);

		JmsBroker broker = optJMSbroker.getJmsBroker();
//		String stat = optJMSbroker.queryJmsBrokerState();
		if (optJMSbroker.queryJmsBrokerState().equals(TLQOptJmsBroker.OBJ_STATE_RUNNING))
			optJMSbroker.stopJmsBrokerByNormal();
		int count = 0;
		while (true) {

			if (!optJMSbroker.queryJmsBrokerState().equals(TLQOptJmsBroker.OBJ_STATE_STOPPED))// 接收进程不处于停止状态
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] modifyJMSBrokerConf qcuName[" + QcuName + "]");
					break;
				}
			} else
				break;
		}
		broker.setJmsBrokerStatus(1);
		broker.setListenPort(10024);

		optJMSbroker.setJmsBroker(broker);

	}

	/**
	 * 功能：
	 * 
	 * @param QcuName
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	@SuppressWarnings("static-access")
	public void removeJMSBrokerConf(String QcuName) throws TLQRemoteException, TLQParameterException {
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		TLQOptJmsBroker optJMSbroker = this.tlqFac.getTLQOptJmsBroker(QcuName);

		if (optJMSbroker.queryJmsBrokerState().equals(TLQOptJmsBroker.OBJ_STATE_RUNNING))
			optJMSbroker.stopJmsBrokerByNormal();
		else
			return;

		int count = 0;
		while (true) {

			if (!optJMSbroker.queryJmsBrokerState().equals(TLQOptJmsBroker.OBJ_STATE_STOPPED))// 接收进程不处于停止状态
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
				if (count > this.StopObjWaitTime)// 依QCU的队列连接规模大小，暂定180秒
				{
					System.out.println("******[Error] modifyJMSBrokerConf qcuName[" + QcuName + "]");
					break;
				}
			} else
				break;
		}

		optJMSbroker.deleteJmsBrokerByAbort();

	}

	/**
	 * 功能 判断jms代理是否存在
	 * 
	 * @param QcuName
	 * @return
	 * @throws TLQRemoteException
	 * @throws TLQParameterException
	 */
	public boolean isExisJMSBrokerConf(String QcuName) throws TLQRemoteException, TLQParameterException {
		TLQOptJmsBroker optJMSbroker = null;
		// 连接监控代理
		if (this.tlqFac == null)
			throw new TLQRemoteException("not conn!!!");

		try {
			optJMSbroker = this.tlqFac.getTLQOptJmsBroker(QcuName);
		} catch (Exception e) {
			// TODO: handle exception

		}
		if (optJMSbroker == null)
			return false;
		else
			return true;

	}

	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * @param argv
	 */
	/**
	 * 功能： 为外交部验证testline 是否正常
	 * 
	 * @param
	 * 
	 */
	public void testlingfor300link(int formseq) {
		String conname;
		long temtime = 0;
		@SuppressWarnings("unused")
		int mytest = 10;
		boolean state = false;
//		String Quename;
		while (true) {
			for (int j = formseq * 10; j < formseq * 10 + 10; j++) { // 30个线程，每个连接10个
				// for (int j = 0; j < 300; j++) {
				conname = "conn" + Integer.toString(j);
//				Quename = new String("sq" + Integer.toString(j));
				try {

					temtime = System.currentTimeMillis();

					state = this.testLine("qcu1", "1", conname);

					// System.out.println(" thread seq= "+formseq+"tesline conname = "+conname+"   used time =@@@ "+(System.currentTimeMillis()-temtime));

					if (state == false) {
						// if(conname.equals("conn246")||
						// conname.equals("conn238")||conname.equals("conn237")
						// ||conname.equals("conn245"))
						System.out.println("#########################testline error  !!!!!!!formseq=" + formseq
								+ "tesline conname = " + conname + "   used time =@@@ "
								+ (System.currentTimeMillis() - temtime));
						mytest = 10;
						// System.out.println("#########################testline error  !!!!!!!formseq="+formseq+"tesline conname = "+conname+"   used time =@@@ "+(System.currentTimeMillis()-temtime));
						// Thread.sleep(10000);
					} else {
						/*
						 * for (int i = 0; i < 10; i++) { SendMsg sendmsg =new
						 * SendMsg("qcu1", Quename, "B", "no");
						 * sendmsg.sendMsg();System.out.println(
						 * "@@@@@@@@@@@@@@@@@@@@@@@@testline ok  ok !!!!!!!formseq="
						 * +
						 * formseq+"tesline conname = "+conname+"   used time =@@@ "
						 * +(System.currentTimeMillis()-temtime));
						 * 
						 * }
						 */
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@testline ok  ok !!!!!!!formseq=" + formseq
								+ "tesline conname = " + conname + "   used time =@@@ "
								+ (System.currentTimeMillis() - temtime));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public String getSendConnState(String qcuName, String processId, String sendConnName) throws TLQRemoteException {
		String state = null;
		try {
			// 连接监控代理
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuName, processId);
			Map map1 = opt.getSendConnList();
			state = (String) map1.get(sendConnName);
			if (state == null)
				throw new TLQRemoteException("conn " + sendConnName + "is not exist !");

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return state;

	}

	/**
	 * 功能 ：获取队列深度，如果为发送队列，则能获取相应发送连接状态
	 * 
	 * @param qcuName
	 *            队列管理单元的名称
	 * @param QueName
	 *            查询队列名
	 * @param sendConnName
	 *            发送连接名
	 * @param sendQueflag
	 *            true 表示查血发送队列，false 表示查询 本地队列。
	 * @param processId
	 *            发送连接所属进程ID，[当查询发送队列的时候需要]
	 * @return Map,当输入发送队列的时候，返回 发送队列消息数msgNum和connStat；当为本地队列的时候，返回msgNum
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public Map getQueStat(String qcuName, String QueName, String sendConnName, boolean sendQueflag, String processId)
			throws TLQRemoteException {
		@SuppressWarnings("unused")
		String stat = null;
		TlqPage tlqpage = new TlqPage();
		Map queStat = new HashMap();
		Map tempMap = null;

		String msgnum = "0";
		try {
			// 连接监控代理
			if (this.tlqFac == null)
				throw new TLQRemoteException("not conn!!!");

			if (sendQueflag) {
				TLQOptSendConn opt = tlqFac.getTLQOptSendConn(qcuName, processId);
				TLQOptSendQue optSq = tlqFac.getTLQOptSendQue(qcuName);

				// 获取消息总数
				Map map = optSq.getSingleMessages(QueName, TLQOptBaseObj.MSG_STATUS_READY, tlqpage);
				Properties pro = (Properties) map.get(TLQOptSendQue.GET_STATINFO_PROPERTIES);
				msgnum = pro.get("TotalMsgNum").toString();

				// 获取发送连接的状态
				Properties p = opt.getSendConnSpvInfo(sendConnName);
				System.out.println("LinkStatus=" + p.get("LinkStatus"));

				queStat.put("msgNum", msgnum);
				queStat.put("connStat", p.get("LinkStatus"));
			} else {
				TLQOptLocalQue optLq = tlqFac.getTLQOptLocalQue(qcuName);
				tempMap = optLq.getLocalQueMsgNum(QueName, optLq.MATCH_NO, 0);
				System.out.println("");
				queStat.put("msgNum", tempMap.get(qcuName));

			}

		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queStat;

	}

	public static void main(String[] argv) {

//		boolean flag = false;
//		ArrayList mytest = new ArrayList(30);
//		Thread thread[] = new Thread[30];
//		Map questat = null;
		TongQExample tConfigManager = new TongQExample("127.0.0.1", 10252); // 设置tlq节点的ip和端口
		try {

			tConfigManager.connect();
			/*
			 * questat=tConfigManager.getQueStat("qcu1", "sq10","conn10", true,
			 * "1"); //questat=tConfigManager.getQueStat("qcu1", "lq10",null,
			 * false, null); Set key = questat.keySet(); for (Iterator it =
			 * key.iterator(); it.hasNext();) { String s = (String) it.next();
			 * System.out.println(s+"= "+questat.get(s)); }
			 */
			/*
			 * for (int i = 0; i < 10; i++) {
			 * 
			 * 
			 * long time =System.currentTimeMillis(); tConfigManager.connect();
			 * Properties ps = tConfigManager.getStatQCUHistoryMsgNum("qcu1");
			 * 
			 * System.out.println("rcvBufSuccessNum="
			 * +ps.get("rcvBufSuccessNum"));
			 * System.out.println("sndBufSuccessNum="
			 * +ps.get("sndBufSuccessNum")); System.out.println("测试时间="
			 * +(System.currentTimeMillis()-time)); tConfigManager.close(); }
			 */

			// tConfigManager.setSysConf("sendmsgtest3",30);

			/*
			 * for (int i = 0; i < 20; i++) { thread[i]=new runtestline(i);
			 * thread[i].start(); } for (int i = 0; i < 20; i++) { thread[i]=new
			 * runtestline(i); thread[i].join();
			 * 
			 * }
			 */

			/*
			 * for (int i = 0; i < 30; i++) { thread[i]=new runtestlineLocal(i);
			 * thread[i].start(); } for (int i = 0; i < 30; i++) { thread[i]=new
			 * runtestlineLocal(i); thread[i].join();
			 * 
			 * }
			 */

			// tConfigManager.testlingfor300link(22);
			// System.out.println("link =" +tConfigManager.testLine("qcu1",
			// "1","conn246"));
			// 增加qcu
			// tConfigManager.addQcu("qcu2");
			// 增加qcu
			// tConfigManager.removeQcu("qcu2");
			// 停止qcu
			// tConfigManager.stopQCU("qcu1");
			// 启动qcu
			// tConfigManager.startQCU("qcu1");
			// 动态增加本地队列
			// tConfigManager.addLocalQue("qcu1","lq4");
			// 动态增加发送队列
			// tConfigManager.addSendQue("tsendqcu1", "sq2020", "lq", "lq");
			// 动态增加发送连接
			// tConfigManager.addSendConn("qcu1", "1", "conn2010", "sq1010",
			// "127.0.0.1", 10003);

			// 修理发送连接
			// tConfigManager.modifySendConn("tsendqcu1", "1", "tCFconn");

			// 测试发送连接是否连通
			// tConfigManager.testLine("qcu1", "1", "conn1");

			// 测试qcu是否存在
			/*
			 * flag=tConfigManager.isExistQcu("qcu1"); if(flag)
			 * System.out.println("qcu1 is exist"); else
			 * System.out.println("qcu1 is not exist");
			 */

			// 测试发送连接是否存在
			// tConfigManager.isExistSendConn("qcu1","1","conn1");

			// 测试远程队列是否存在
			/*
			 * flag=tConfigManager.isExistRemoteQue("qcu1", "rq"); if(flag)
			 * System.out.println("rq  is exist"); else
			 * System.out.println("sq  is not exist");
			 */

			// 测试队列是否满
			/*
			 * flag=tConfigManager.testSendQuneneIsFull("qcu1", "sq"); if(flag)
			 * System.out.println("sq  is full"); else
			 * System.out.println("sq  is not full");
			 */

			// 测试发送队列是否可用（有剩余空间而且网络连通，仅用于常连接）
			/*
			 * flag=tConfigManager.testSenQueIsUsable("qcu1", "sq", "conn1",
			 * "1"); if(flag) System.out.println("sq is usable!"); else
			 * System.out.println("sq is not usable!");
			 */

			// 获取本地队列消息数
			tConfigManager.getMsgnumOfLoaclQue("qcu1", "lq", 0);
			// 增加远程队列

			// tConfigManager.addRemoteQue("qcu1", "rq10", "sq", "lq");

			// 获取qcu历史传输消息数
			/*
			 * Properties prop= tConfigManager.getStatQCUHistoryMsgNum("qcu1");
			 * for(Enumeration e=prop.propertyNames(); e.hasMoreElements();){
			 * String key=(String) e.nextElement();
			 * System.out.println(key+"===>"+prop.getProperty(key)); }
			 */

			// 测试远程队列是否存在
			/*
			 * flag=tConfigManager.isExistRemoteQue("qcu1", "rq"); if(flag)
			 * System.out.println("rq  is exist"); else
			 * System.out.println("rq  is not exist");
			 */

			/* tConfigManager.modifyRemoteQue("qcu1", "rq", "sq1010", "lq222"); */

			// 修改发送队列
			// tConfigManager.modifySendQue("tsendqcu1", "sq00",
			// "lq2","sendqcu");

			// 修改本地队列
			// tConfigManager.modifyLocalQue("qcu1", "lq", 140, 1048);
			// 获取 qcu 状态
			// System.out.print("qcu1 is "+tConfigManager.getQCUState("qcu1"));
			// 删除远程队列
			// tConfigManager.removeRemoteQue("qcu1", "rq");
			// 增加接收进程
			// tConfigManager.addRcvProcess("qcu1", 3, 13005);
			// 删除接收进程
			// tConfigManager.removeRcvProcess("qcu1", "1");
			// 修改接收进程
			/* tConfigManager.modifyRcvProcess("qcu1", "1"); */
			// 获取接收进程的状态
			/*
			 * String status=tConfigManager.getRcvProcessStatus("qcu1", "1");
			 * System.out.println("rcv process status is "+status);
			 */
			// 修改发送进程
			// tConfigManager.modifySendProcess("qcu1", "1");
			// 增加发送进程
			// tConfigManager.addSendProcess("qcu1", 1);
			// 删除发送进程
			// tConfigManager.removeSendProcess("qcu1", "1");
			// 获取发送进程的状态
			/*
			 * String status=tConfigManager.getSendProcessStatus("qcu1", "1");
			 * System.out.println("snd process status is "+status);
			 */

			// 判断jndi 连接工厂是否存在
			/*
			 * flag=tConfigManager.isExistJndiFactory("mytestconn"); if(flag)
			 * System.out.println("mytestconn  is exist"); else
			 * System.out.println("mytestconn  is not exist");
			 */

			// 删除连接工厂配置
			/* tConfigManager.removeJndiFactory("mytestconn"); */
			// 增加连接工厂配置
			/* tConfigManager.addJndiFactory("mytestconn"); */

			// 修改连接工厂配置
			// tConfigManager.modifyJndiFactory("mytestconn");

			// 判断jndi队列是否存在
			/*
			 * flag=tConfigManager.isExistJnidQue("mytestjndiQue"); if(flag)
			 * System.out.println("mytestjndiQue  is exist"); else
			 * System.out.println("mytestjndiQue  is not exist");
			 */

			// 增加jndi Queue配置
			// tConfigManager.addJnidQue("mytestjndiQue");
			// 修改jndi Queue配置
			// tConfigManager.modifyJnidQue("mytestjndiQue");
			// 删除jndi Queue配置
			// tConfigManager.removeJnidQue("mytestjndiQue");

			// 删除 jms代理
			// tConfigManager.removeJMSBrokerConf("qcu1");
			// 增加jms 代理
			// tConfigManager.addJMSBrokerConf("qcu1");
			// 修改jms 代理
			// tConfigManager.modifyJMSBrokerConf("qcu1");
			// 判断jms 代理是否存在
			/*
			 * flag=tConfigManager.isExisJMSBrokerConf("qcu1"); if(flag)
			 * System.out.println("jms broer  conf  is exist"); else
			 * System.out.println("jms broer  conf is not exist");
			 */
			// tConfigManager.stopQCU("qcu1");

			/*
			 * while(true) { tConfigManager.testgetQCUState("qcu1"); }
			 */

			// 停止节点
			// tConfigManager.stopTlq();
			// 启动节点
			/* tConfigManager.startTlq(); */
			// tConfigManager.startQCU("qcu1");
			// System.out.print(" qcu1 state is"+
			// tConfigManager.getQCUState("qcu1"));

		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			tConfigManager.close();

		} finally {
			tConfigManager.close();
		}
	}

}

class runtestline extends Thread {
	int fromseq = 0;

	public runtestline(int fromseq) {
		this.fromseq = fromseq;
		// TODO Auto-generated constructor stub
	}

	public void run() {
		TongQExample tConfigManager = new TongQExample("168.1.1.113", 10252); // 设置tlq节点的ip和端口
		try {
			tConfigManager.connect();
			tConfigManager.testlingfor300link(this.fromseq);
			// TODO Auto-generated method stub
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO: handle exception
	}
}

class runtestlineLocal extends Thread {
	int fromseq = 0;

	public runtestlineLocal(int fromseq) {
		this.fromseq = fromseq;
		// TODO Auto-generated constructor stub
	}

	public void run() {
//		 TestLine tConfigManager = new TestLine("qcu1"); // 设置tlq节点的ip和端口
//		 try {
//		
//		 tConfigManager.testlingfor300link(this.fromseq);
//		 // TODO Auto-generated method stub
//		 } catch (Exception e) {
//		 e.printStackTrace();
//		 }
//		 // TODO: handle exception
	}

}
