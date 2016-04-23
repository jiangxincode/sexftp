/*    */ package org.sexftp.core.ftp;
/*    */ 
/*    */ import org.apache.commons.net.ftp.FTPClient;
/*    */ import org.apache.commons.net.ftp.FTPSClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MyFtps
/*    */   extends MyFtp
/*    */ {
/*    */   protected FTPClient myInstance()
/*    */   {
/* 38 */     return new FTPSClient(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\MyFtps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */