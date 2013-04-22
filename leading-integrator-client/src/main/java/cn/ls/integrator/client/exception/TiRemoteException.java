package cn.ls.integrator.client.exception;

public class TiRemoteException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 具体的异常类
	 */
	private Throwable cause = null;

	public TiRemoteException() {
		super();
	}
	
	public TiRemoteException(String message){
		super(message);
	}
	public TiRemoteException(String message,Throwable ex) {
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
	
	public Throwable getCause() {
		return cause;
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
