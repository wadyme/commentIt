package cn.ls.integrator.core.exception;

public class IntegratorException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 具体的异常类
	 */

	public IntegratorException() {
		super();
	}
	
	public IntegratorException(String message){
		super(message);
	}
	public IntegratorException(String message,Throwable ex) {
		super(message,ex);
	}
	
	/**
	 * 输出异常的StackTrace
	 */
	public void printStackTrace() {
		super.printStackTrace();
		Throwable cause = getCause();
		if (cause != null) {
			cause.printStackTrace();
		}
	}
	
	public String getMessage() {
		String message = super.getMessage();
		Throwable cause = getCause();
		if (cause != null) {
			return message + "; nested exception is " + cause;
		}
		return message;
	}
}
