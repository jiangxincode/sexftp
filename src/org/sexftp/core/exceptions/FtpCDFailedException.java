/*    */ package org.sexftp.core.exceptions;
/*    */ 
/*    */ public class FtpCDFailedException
/*    */   extends RuntimeException
/*    */ {
/*    */   public FtpCDFailedException() {}
/*    */   
/*    */   public FtpCDFailedException(String message)
/*    */   {
/* 10 */     super(message);
/*    */   }
/*    */   
/*    */   public FtpCDFailedException(Throwable cause)
/*    */   {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public FtpCDFailedException(String message, Throwable cause)
/*    */   {
/* 20 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\exceptions\FtpCDFailedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */