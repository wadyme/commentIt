package cn.ls.integrator.server.qscanner.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

import cn.ls.integrator.core.exception.IntegratorException;
import cn.ls.integrator.core.exception.InterruptedRuntimeException;
import cn.ls.integrator.core.qscanner.Queue;
import cn.ls.integrator.core.qscanner.QueueScanner;
import cn.ls.integrator.core.qscanner.QueueState;
import cn.ls.integrator.core.qscanner.QueueVenderor;
import cn.ls.integrator.core.utils.SystemConfigUtility;

//TODO scanner 线程的维护 异常终止的停止
public class QueueScannerUtils {

	private static Logger logger = Logger.getLogger(QueueScannerUtils.class);
	
	private static boolean isStarted;
	
	private static ExecutorService executorService ;
	
	private static Map<Queue, Future<?>> futures ;
	
	private static QueueStateCheakThread queueStateThread;
	
	private static ExecutorService getExecutorService(){
		if(executorService == null){
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
	
	private static synchronized Map<Queue, Future<?>>  getFutures(){
		if(futures == null){
			futures = new HashMap<Queue, Future<?>>();
		}
		return futures;
	}
	/**
	 *创建一个QueueStateCheakThread。
	 */
	private static synchronized QueueStateCheakThread getQueueStateThread(){
		if(queueStateThread == null || !queueStateThread.isAlive()){
			queueStateThread = new QueueStateCheakThread();
		}
		return queueStateThread;
	}
	/**
	 * 扫描所有队列并启动。
	 * 获得QueueVenderor<Queue>对象queueVenderor，通过queueVenderor.getQueueState()
	 * 判断消息中间件是否启动，如果启动则让QueueStateThread执行队列的启动。如果没有启动则抛出IntegratorException。
	 */
	public static void startScan(){
//		System.out.println("开始扫描");
		if(isStarted){
//			System.out.println("扫描已经启动，不能再启动");
			return;
		}
		isStarted = true;
		QueueVenderor<Queue> queueVenderor = null;
		try {
			queueVenderor = getVenderor();
		} catch (IntegratorException e) {
			isStarted = false;
			throw e;
		}
		QueueState state = queueVenderor.getQueueState();
		if(QueueState.STARTING.equals(state) ||QueueState.WORKING.equals(state) || QueueState.STOPPED.equals(state) ){
			if(!getQueueStateThread().isAlive()){
				getQueueStateThread().start();
			}
		}
	}
	/**
	 * 启动所有的Queue,再根据Queue创建QueueScanner实例，由该实例再创建QueueScannerThread的实例，执行具体任务线程
	 */
	@SuppressWarnings("unchecked")
	private static void startScanner(){
		QueueVenderor<Queue> queueVenderor = getVenderor();
		long startSystemTime = System.currentTimeMillis();
		while(true){
			
			if(QueueState.WORKING.equals(queueVenderor.getQueueState())){
				break;
			}
			try {
				logger.info("消息中间件正在启动，2秒之后重新获得中间件状态");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new InterruptedRuntimeException();
			}
			if(System.currentTimeMillis() - startSystemTime >= 60 * 1000){
				isStarted = false;
				throw new IntegratorException("等待消息中间件启动超时");
			}
		}
		try {
//			System.out.println("开始获得所有队列信息");
			List<Queue> queueList = queueVenderor.getRecieveQueueList();
//			System.out.println("成功获得所有队列信息");
			if(queueList != null && queueList.size() != 0){
				for(Queue queue : queueList){
					if(getFutures().get(queue) == null){
						QueueScanner scanner = queueVenderor.createQueueScanner(queue);
//						System.out.println("此处开始启动 " + queue);
						Future future = getExecutorService().submit(new QueueScannerThread(scanner));
						getFutures().put(queue, future);
					}
				}
			}
		} catch (Exception e) {
			logger.error("启动队列扫描过程中发生错误", e);
			isStarted = false;
		}
	}
	/**
	 * 停止所有Queue的任务
	 */
	public static void stopScan(){
//		System.out.println("结束扫描");
		if(!isStarted){
			return;
		}
		if(!getExecutorService().isShutdown()){
			getExecutorService().shutdownNow();
			executorService = null;
			futures = null;
		}
		isStarted = false;
	}
	/**
	 * 停止对队列的监控，并从缓存中清除该队列
	 * @param queue Queue类型
	 */
	public static void stopScan(Queue queue){
//		System.out.println("开始停止:" + queue);
		Future<?> future = getFutures().get(queue);
		if(future == null){
			return;
		}
		future.cancel(true);
		getFutures().remove(queue);
	}
	/**
	 * 1：通过缓存的 Map<Queue, Future<?>> futures判断传入Queue的实例是否存在，如果存在则返回
	 * 2：如果存在则根据Queue的实例创建QueueScanner实例scanner
	 * 3：根据scanner创建Future对象 future
	 * 4：将future放入缓存Map<Queue, Future<?>> futures
	 * @param queue Queue的实例对象
	 */
	@SuppressWarnings("unchecked")
	public static void startScan(Queue queue) {
//		System.err.println("开始扫描:" + queue);
		if(getFutures().get(queue) != null){
//			System.out.println(queue + "扫描已经启动，不能再次启动");
			return;
		}
		QueueScanner scanner = getVenderor().createQueueScanner(queue);
		Future future = getExecutorService().submit(new QueueScannerThread(scanner));
		getFutures().put(queue, future);
	}
	/**
	 * 通过系统配置获得QueueVenderor的实现类名称反射生成QueueVenderor对象
	 * @return QueueVenderor<Queue>
	 */
	@SuppressWarnings("unchecked")
	private static QueueVenderor<Queue> getVenderor(){
		QueueVenderor<Queue> queueVenderor = null;
		try{
			String venderor = SystemConfigUtility.getSystemConfigProperty(SystemConfigUtility.QSCANNER_VENDEROR);
			Class<QueueVenderor<Queue>> clazz = ClassUtils.getClass(venderor);
			queueVenderor = clazz.newInstance();
		} catch (Exception e) {
			logger.error("请检查li.properities中配置属性qscanner.venderor", e);
			throw new IntegratorException("启动失败", e);
		} 
		return queueVenderor;
	}
	/**
	 *执行所有QueueScanner对象的scan()。
	 */
	static class QueueStateCheakThread extends Thread{
		@Override
		public void run() {
			startScanner();
		}
	}
	/**
	 *执行一个QueueScanner对象的scan()。
	 */
	static class QueueScannerThread implements Runnable{
		
		QueueScanner scanner;
		
		QueueScannerThread(QueueScanner scanner){
			this.scanner = scanner;
		}
		@Override
		public void run() {
			scanner.scan();
		}
	}
}
