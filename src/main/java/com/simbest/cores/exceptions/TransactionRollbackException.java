package com.simbest.cores.exceptions;

/**
 * 失误回退异常
 * @author lishuyi
 */
public class TransactionRollbackException extends AppException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4432025698389231168L;

	public TransactionRollbackException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public TransactionRollbackException(String errorCode, String errorMessage,
			Throwable e) {
		super(errorCode, errorMessage, e);
	}
	
}
