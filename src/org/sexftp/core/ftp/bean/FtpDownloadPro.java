/*    */ package org.sexftp.core.ftp.bean;
/*    */ 
/*    */ 
/*    */ public class FtpDownloadPro
/*    */ {
/*    */   private FtpUploadConf ftpUploadConf;
/*    */   
/*    */   private FtpConf ftpConf;
/*    */   
/*    */   private FtpFile ftpfile;
/*    */   
/*    */   public FtpDownloadPro(FtpUploadConf ftpUploadConf, FtpConf ftpConf, FtpFile ftpfile)
/*    */   {
/* 14 */     this.ftpUploadConf = ftpUploadConf;
/* 15 */     this.ftpConf = ftpConf;
/* 16 */     this.ftpfile = ftpfile;
/*    */   }
/*    */   
/*    */   public FtpUploadConf getFtpUploadConf() {
/* 20 */     return this.ftpUploadConf;
/*    */   }
/*    */   
/*    */   public void setFtpUploadConf(FtpUploadConf ftpUploadConf) {
/* 24 */     this.ftpUploadConf = ftpUploadConf;
/*    */   }
/*    */   
/*    */   public FtpConf getFtpConf() {
/* 28 */     return this.ftpConf;
/*    */   }
/*    */   
/*    */   public void setFtpConf(FtpConf ftpConf) {
/* 32 */     this.ftpConf = ftpConf;
/*    */   }
/*    */   
/*    */ 
/*    */   public FtpFile getFtpfile()
/*    */   {
/* 38 */     return this.ftpfile;
/*    */   }
/*    */   
/*    */   public void setFtpfile(FtpFile ftpfile)
/*    */   {
/* 43 */     this.ftpfile = ftpfile;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return this.ftpUploadConf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\bean\FtpDownloadPro.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */