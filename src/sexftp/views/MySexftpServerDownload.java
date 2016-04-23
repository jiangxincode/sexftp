package sexftp.views;

import org.sexftp.core.ftp.bean.FtpDownloadPro;

public abstract interface MySexftpServerDownload
{
  public abstract void afterDownload(FtpDownloadPro paramFtpDownloadPro)
    throws Exception;
  
  public abstract boolean exceptionNotExits(FtpDownloadPro paramFtpDownloadPro)
    throws Exception;
  
  public abstract String trustFolder(FtpDownloadPro paramFtpDownloadPro)
    throws Exception;
}


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\MySexftpServerDownload.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */