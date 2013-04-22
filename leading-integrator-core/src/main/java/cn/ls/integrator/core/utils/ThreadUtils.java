package cn.ls.integrator.core.utils;

import cn.ls.integrator.core.exception.InterruptedRuntimeException;
/**
 *线程的工具类
 */
public final class ThreadUtils {
	/**
	 * 检查当前线程是否被打断，如果是则则抛出InterruptedRuntimeException。
	 */
	public static void checkThreadInterrupted() {
		if (Thread.interrupted()) {
			throw new InterruptedRuntimeException();
		}
	}

}
