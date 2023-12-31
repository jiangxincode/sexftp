package org.sexftp.core.exceptions;

public class FtpCDFailedException extends RuntimeException {
	public FtpCDFailedException() {
	}

	public FtpCDFailedException(String message) {
		super(message);
	}

	public FtpCDFailedException(Throwable cause) {
		super(cause);
	}

	public FtpCDFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
