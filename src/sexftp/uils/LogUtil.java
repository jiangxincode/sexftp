/*    */ package sexftp.uils;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import org.apache.commons.net.PrintCommandListener;
/*    */ import org.apache.commons.net.ftp.FTPClient;
/*    */ import org.apache.commons.net.ftp.FTPFile;
/*    */ import org.eclipse.core.runtime.ILog;
/*    */ import org.eclipse.core.runtime.Status;
/*    */ import org.eclipse.ui.console.MessageConsoleStream;
/*    */ import org.sexftp.core.utils.StringUtil;
/*    */ import sexftp.Activator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogUtil
/*    */ {
/*    */   public static void info(String msg)
/*    */   {
/*    */     try
/*    */     {
/* 27 */       Activator.getDefault().getLog().log(new Status(1, "sexftp", "sexftp_info_log:" + msg));
/*    */     }
/*    */     catch (NullPointerException localNullPointerException) {}
/* 30 */     System.out.println(msg);
/*    */   }
/*    */   
/*    */   public static void error(String msg, Throwable e) {
/*    */     try {
/* 35 */       Activator.getDefault().getLog().log(new Status(4, "sexftp", "sexftp_erorr_log:" + msg, e));
/*    */     }
/*    */     catch (NullPointerException localNullPointerException) {}
/* 38 */     System.out.println(msg + "\r\n" + StringUtil.readExceptionDetailInfo(e));
/*    */   }
/*    */   
/*    */   public static MessageConsoleStream initSexftpChangeConsole()
/*    */   {
/* 43 */     return Console.createConsole("SexFtpServerCmd", "repository_rep.gif").getConsoleStream();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 48 */     FTPClient ftpclient = new FTPClient();
/*    */     
/*    */ 
/* 51 */     ftpclient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
/*    */     
/* 53 */     ftpclient.connect("59.151.39.39", 9898);
/* 54 */     boolean isit = ftpclient.login("kkfunapp", "BEIJINGkkfun2007?><|\":}{P+_)");
/* 55 */     System.out.println(isit);
/* 56 */     ftpclient.setFileType(2);
/* 57 */     ftpclient.enterLocalPassiveMode();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 66 */     boolean wk = ftpclient.changeWorkingDirectory("/");
/* 67 */     System.out.println(wk);
/* 68 */     System.out.println(ftpclient.printWorkingDirectory());
/*    */     
/*    */ 
/* 71 */     FTPFile[] listFiles = ftpclient.listFiles();
/* 72 */     FTPFile[] arrayOfFTPFile1; int j = (arrayOfFTPFile1 = listFiles).length; for (int i = 0; i < j; i++) { FTPFile ftpFile = arrayOfFTPFile1[i];
/* 73 */       System.out.println(ftpFile.getName());
/*    */     }
/* 75 */     System.out.println(listFiles.length);
/*    */     
/* 77 */     ftpclient.noop();
/*    */     
/* 79 */     ftpclient.logout();
/*    */     
/* 81 */     if (ftpclient.isConnected())
/*    */     {
/*    */       try
/*    */       {
/* 85 */         ftpclient.disconnect();
/*    */       }
/*    */       catch (IOException localIOException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\LogUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */