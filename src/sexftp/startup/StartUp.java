/*   */ package sexftp.startup;
/*   */ 
/*   */ import org.eclipse.ui.IStartup;
/*   */ 
/*   */ public class StartUp implements IStartup
/*   */ {
/*   */   public void earlyStartup()
/*   */   {
/* 9 */     org.sexftp.core.license.LicenseUtils.checkUpdateAndLicense("StartUp");
/*   */   }
/*   */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\startup\StartUp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */