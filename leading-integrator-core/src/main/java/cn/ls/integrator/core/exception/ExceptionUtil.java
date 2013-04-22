package cn.ls.integrator.core.exception;

import org.junit.Test;

public class ExceptionUtil {

	public static String getMessageStack(Throwable t){
		if(t.getCause() == null){
			return t.getMessage();
		} else {
			return t.getMessage() + "  Cause by -> " + getMessageStack(t.getCause());
		}
	}
	
	@Test
	public void test(){
		try {
			test3();
		} catch (Exception e) {
			System.out.println();
		}
	}
	
	private void test2(){
		throw new IntegratorException("message1");
	}
	
	private void test3(){
		try{
			test2();
		} catch (Exception e) {
			throw new IntegratorException("message2", e);
		}
	}
}
