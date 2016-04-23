package sexftp.views;

import java.util.List;
import org.sexftp.core.ftp.bean.FtpUploadPro;

public abstract interface IInnerCompareCallback {
	public abstract void afterCompareEnd(List<FtpUploadPro> paramList1, List<FtpUploadPro> paramList2,
			List<FtpUploadPro> paramList3);
}
