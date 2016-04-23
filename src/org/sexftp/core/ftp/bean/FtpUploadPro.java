/*    */ package org.sexftp.core.ftp.bean;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtpUploadPro
/*    */ {
/*    */   private FtpUploadConf ftpUploadConf;
/*    */   
/*    */   private FtpConf ftpConf;
/*    */   
/*    */ 
/*    */   public FtpUploadPro(FtpUploadConf ftpUploadConf, FtpConf ftpConf)
/*    */   {
/* 14 */     this.ftpUploadConf = ftpUploadConf;
/* 15 */     this.ftpConf = ftpConf;
/*    */   }
/*    */   
/*    */   public FtpUploadConf getFtpUploadConf() {
/* 19 */     return this.ftpUploadConf;
/*    */   }
/*    */   
/*    */   public void setFtpUploadConf(FtpUploadConf ftpUploadConf) {
/* 23 */     this.ftpUploadConf = ftpUploadConf;
/*    */   }
/*    */   
/*    */   public FtpConf getFtpConf() {
/* 27 */     return this.ftpConf;
/*    */   }
/*    */   
/*    */   public void setFtpConf(FtpConf ftpConf) {
/* 31 */     this.ftpConf = ftpConf;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 36 */     return this.ftpUploadConf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\bean\FtpUploadPro.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */