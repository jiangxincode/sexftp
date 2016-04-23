package org.sexftp.core.exceptions;

public class SFConnectionException extends RuntimeException {
	public SFConnectionException() {
	}

	public SFConnectionException(String message) {
		super(message);
	}

	public SFConnectionException(Throwable cause) {
		super(cause);
	}

	public SFConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
