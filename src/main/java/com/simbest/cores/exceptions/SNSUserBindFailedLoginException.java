package com.simbest.cores.exceptions;

/**
 * 非认证管理员访问异常
 * @author lishuyi
 */
public class SNSUserBindFailedLoginException extends AppException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5906654867549866311L;

	public SNSUserBindFailedLoginException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public SNSUserBindFailedLoginException(String errorCode, String errorMessage,
			Throwable e) {
		super(errorCode, errorMessage, e);
	}
		
}
