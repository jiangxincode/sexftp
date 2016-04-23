package sexftp.views;

import org.sexftp.core.ftp.bean.FtpDownloadPro;

public abstract interface MySexftpServerDownload {
	public abstract void afterDownload(FtpDownloadPro paramFtpDownloadPro) throws Exception;

	public abstract boolean exceptionNotExits(FtpDownloadPro paramFtpDownloadPro) throws Exception;

	public abstract String trustFolder(FtpDownloadPro paramFtpDownloadPro) throws Exception;
}
