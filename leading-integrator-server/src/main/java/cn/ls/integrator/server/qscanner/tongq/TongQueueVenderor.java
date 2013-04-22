package cn.ls.integrator.server.qscanner.tongq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.qscanner.QueueScanner;
import cn.ls.integrator.core.qscanner.QueueState;
import cn.ls.integrator.core.qscanner.QueueVenderor;
import cn.ls.integrator.core.utils.StringUtility;
import cn.ls.integrator.core.utils.SystemConfigUtility;

import com.tongtech.tlq.admin.remote.api.TLQConnect;
import com.tongtech.tlq.admin.remote.api.TLQOptObjFactory;
import com.tongtech.tlq.admin.remote.api.TLQParameterException;
import com.tongtech.tlq.admin.remote.api.TLQRemoteException;
import com.tongtech.tlq.admin.remote.api.node.TLQOptNodeSystem;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptLocalQue;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptQCU;

public class TongQueueVenderor implements QueueVenderor<TongQueue> {

	private Logger logger = Logger.getLogger(TongQueueVenderor.class);
	/**tlq监听端口**/
	public static final String QSCANNER_TLQ_PORT = "qscanner.tlq.port";
	
	/**tlq监听IP**/
	public static final String QSCANNER_TLQ_IP = "qscanner.tlq.ip";
	
	private static String ip = null;
	
	private static int port = 0;
	
	TLQOptObjFactory tlqFac;

	TLQConnect tlqConnect;

	@Override
	public QueueScanner createQueueScanner(TongQueue queue) {
		QueueScanner scanner = new TongQueueScanner(queue);
		return scanner;
	}
	
	@Override
	public QueueState getQueueState() {
		try {
			long timeTemp = System.currentTimeMillis();
			while(true){
				try {
					connect();
					break;
				} catch (TLQParameterException e) {
					throw new IntegratorException("TLQ 参数错误", e);
				} catch (TLQRemoteException e) {
					logger.error("TLQ 远程连接异常, 等待1秒重新连接");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						throw new InterruptedRuntimeException(e1);
					}
				}
				if((System.currentTimeMillis() - timeTemp) >= 30*1000){
					throw new IntegratorException("无法连接远程TLQ");
				}
			}
			TLQOptNodeSystem opt = tlqFac.getTLQOptNodeSystem();
			if(TLQOptNodeSystem.OBJ_STATE_RUNNING.equals(opt.getNodeState())){
				return QueueState.WORKING;
			}
			if(TLQOptNodeSystem.OBJ_STATE_STARTING.equals(opt.getNodeState())){
				return QueueState.STARTING;
			}
			if(TLQOptNodeSystem.OBJ_STATE_STOPPED.equals(opt.getNodeState())){
				return QueueState.STOPPED;
			}
			if(TLQOptNodeSystem.OBJ_STATE_STOPPING.equals(opt.getNodeState())){
				return QueueState.STOPPING;
			}
		}catch (TLQRemoteException e) {
			logger.error(e.getMessage(), e);
			throw new IntegratorException(e.getMessage(), e);
		}finally {
			if(tlqConnect != null){
				try {
					tlqConnect.close();
				} catch (TLQRemoteException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		return null;
	}
	/**
	 * 获得接收队列的List<TongQueue>。
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TongQueue> getRecieveQueueList() {
		
		List<TongQueue> queueList = new ArrayList<TongQueue>();
		try {
			connect();
			Set<String> qcuSet = getQcuSet();
			for (String qcuName : qcuSet) {
				TLQOptLocalQue opt = tlqFac.getTLQOptLocalQue(qcuName);
				// 队列数量
				Map<String, Object> queueMap = opt.getLocalQueList();
				if (queueMap.size() > 0) {
					Set<String> queueSet = queueMap.keySet();
					for (String queueName : queueSet) {
						TongQueue tongQueue = new TongQueue();
						tongQueue.setQcuName(qcuName);
						tongQueue.setQueueName(queueName);
						queueList.add(tongQueue);
					}
				}
			}
		} catch (TLQParameterException e) {
			throw new IntegratorException("TLQ 参数错误", e);
		} catch (TLQRemoteException e) {
			throw new IntegratorException("TlQ 连接异常", e);
		} finally {
			if(tlqConnect != null){
				try {
					tlqConnect.close();
				} catch (TLQRemoteException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return queueList;
	}
	
	/**
	 * 获得QCU名称的Set<String>。
	 * @throws TLQRemoteException
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getQcuSet() throws TLQRemoteException {
		
		TLQOptQCU tlqOptQcu = tlqFac.getTLQOptQCU();
		Map<String, Object> qcuMap = null;
		
		TLQOptNodeSystem opt = tlqFac.getTLQOptNodeSystem();
		int count = 0;
		String state = null;
		while (true) {
			state = opt.getNodeState();
			if (!TLQOptNodeSystem.OBJ_STATE_RUNNING.equals(state)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new InterruptedRuntimeException();
				}
				count++;
				if (count > 3) {
					throw new IntegratorException("TongLingQ 正在启动，等待超时，无法获得QCU列表");
				}
			} else {
				break;
			}
		}
		qcuMap = tlqOptQcu.getQCUList();
		Set<String> set = qcuMap.keySet();
		return set;
	}
	
	private static void init(){
		if(StringUtility.isBlank(ip)){
			ip = SystemConfigUtility.getSystemConfigProperty(QSCANNER_TLQ_IP);
			if(StringUtility.isBlank(ip)){
				throw new IntegratorException("您配置文件中没有配置属性：" + QSCANNER_TLQ_IP);
			}
		}
		if(port == 0){
			String portStr = SystemConfigUtility.getSystemConfigProperty(QSCANNER_TLQ_PORT);
			if (StringUtility.isNotBlank(portStr)) {
				port = Integer.parseInt(portStr);
			} else {
				throw new IntegratorException("您配置文件中没有配置属性：" + QSCANNER_TLQ_PORT);
			}
		}
	}

	/**
	 * 连接TLQ server
	 */
	protected void connect() throws TLQParameterException,
			TLQRemoteException {
		init();
		tlqConnect = new TLQConnect(ip, port);
		tlqConnect.connect();
		//tlqConnect.setIsDebug(TLQConnect.DEBUG_YES);
		tlqFac = new TLQOptObjFactory(tlqConnect);
		
	}
}
