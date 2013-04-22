package cn.ls.integrator.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestExecutorService {

	@Test
	public void testExecutorService(){
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new ExecutorThread(1));
//		exec.submit(new ExecutorThread(2));
//		exec.submit(new ExecutorThread(3));
//		exec.shutdown();
//		exec.shutdownNow();
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.submit(new ExecutorThread(1));
		exec.submit(new ExecutorThread(2));
		exec.submit(new ExecutorThread(3));
		exec.submit(new ExecutorThread(4));
		exec.submit(new ExecutorThread(5));
		exec.shutdownNow();
	}
	
	static class ExecutorThread extends Thread{
		int times;
		public void setTimes(int times){
			this.times = times;
		}
		public ExecutorThread(int times) {
			this.times = times;
		}
		@Override
		public void run() {
			int i = 0;
			while(true){
				if (i%10 == 0){
					System.out.println(i/1000000 + "########" + times);
				}
				i++;
				try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
