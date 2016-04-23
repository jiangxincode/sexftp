package org.sexftp.core.exceptions;

public class FtpConnectClosedException extends RuntimeException {
	public FtpConnectClosedException() {
	}

	public FtpConnectClosedException(String message) {
		super(message);
	}

	public FtpConnectClosedException(Throwable cause) {
		super(cause);
	}

	public FtpConnectClosedException(String message, Throwable cause) {
		super(message, cause);
	}
}
