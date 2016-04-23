/*    */ package org.sexftp.core.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AbortException
/*    */   extends RuntimeException
/*    */ {
/*    */   public AbortException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AbortException(String message, Throwable cause)
/*    */   {
/* 18 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AbortException(String message)
/*    */   {
/* 26 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AbortException(Throwable cause)
/*    */   {
/* 34 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\exceptions\AbortException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */