package com.mfatihercik.moneytransfer.core;

public class ValidationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	public ValidationException(int code) {
		super();
		this.setCode(code);
	}
	public ValidationException(int code,String message) {
		super(message);
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
