package cn.ls.integrator.components.page;

/**
 * 一些通用的异常，指示发生错误。
 * 
 * @author wanl
 * 
 * @since 1.0
 */
public class PaginationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7102346822207026944L;

	public PaginationException() {
		super();
	}

	public PaginationException(String msg) {
		super(msg);
	}

	public PaginationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PaginationException(Throwable cause) {
		super(cause);
	}
}
