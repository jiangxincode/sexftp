package sexftp.views;

import org.sexftp.core.ftp.bean.FtpUploadConf;

public abstract interface IFtpStreamMonitor
{
  public abstract void printSimple(String paramString);
  
  public abstract void printStreamString(FtpUploadConf paramFtpUploadConf, long paramLong1, long paramLong2, String paramString);
}


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\IFtpStreamMonitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */