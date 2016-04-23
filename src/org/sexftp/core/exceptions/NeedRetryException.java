/*    */ package org.sexftp.core.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NeedRetryException
/*    */   extends RuntimeException
/*    */ {
/*    */   public NeedRetryException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NeedRetryException(String message, Throwable cause)
/*    */   {
/* 18 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NeedRetryException(String message)
/*    */   {
/* 26 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NeedRetryException(Throwable cause)
/*    */   {
/* 34 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\exceptions\NeedRetryException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */