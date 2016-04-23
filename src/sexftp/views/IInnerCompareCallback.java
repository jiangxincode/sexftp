package sexftp.views;

import java.util.List;
import org.sexftp.core.ftp.bean.FtpUploadPro;

public abstract interface IInnerCompareCallback
{
  public abstract void afterCompareEnd(List<FtpUploadPro> paramList1, List<FtpUploadPro> paramList2, List<FtpUploadPro> paramList3);
}


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\IInnerCompareCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */