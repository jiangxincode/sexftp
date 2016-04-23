/*    */ package org.sexftp.core.exceptions;
/*    */ 
/*    */ public class FtpConnectClosedException
/*    */   extends RuntimeException
/*    */ {
/*    */   public FtpConnectClosedException() {}
/*    */   
/*    */   public FtpConnectClosedException(String message)
/*    */   {
/* 10 */     super(message);
/*    */   }
/*    */   
/*    */   public FtpConnectClosedException(Throwable cause)
/*    */   {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public FtpConnectClosedException(String message, Throwable cause)
/*    */   {
/* 20 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\exceptions\FtpConnectClosedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */