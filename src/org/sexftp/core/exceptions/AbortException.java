package org.sexftp.core.exceptions;

public class AbortException extends RuntimeException {
	public AbortException() {
	}

	public AbortException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbortException(String message) {
		super(message);
	}

	public AbortException(Throwable cause) {
		super(cause);
	}
}
