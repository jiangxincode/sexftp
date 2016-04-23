/*    */ package org.sexftp.core.ftp.bean;
/*    */ 
/*    */ import org.sexftp.core.Tosimpleable;
/*    */ import org.sexftp.core.utils.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtpUploadConf
/*    */   implements Tosimpleable
/*    */ {
/*    */   private String clientPath;
/*    */   private String serverPath;
/*    */   private String fileMd5;
/*    */   private String[] excludes;
/*    */   private String[] includes;
/*    */   
/*    */   public String getClientPath()
/*    */   {
/* 20 */     return this.clientPath;
/*    */   }
/*    */   
/* 23 */   public void setClientPath(String clientPath) { this.clientPath = clientPath; }
/*    */   
/*    */   public String getServerPath() {
/* 26 */     return this.serverPath;
/*    */   }
/*    */   
/* 29 */   public void setServerPath(String serverPath) { this.serverPath = serverPath; }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getFileMd5()
/*    */   {
/* 35 */     return this.fileMd5;
/*    */   }
/*    */   
/* 38 */   public void setFileMd5(String fileMd5) { this.fileMd5 = fileMd5; }
/*    */   
/*    */   public String[] getExcludes() {
/* 41 */     return this.excludes;
/*    */   }
/*    */   
/* 44 */   public void setExcludes(String[] excludes) { this.excludes = excludes; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return String.format("%s <-> %s", new Object[] {
/* 49 */       this.clientPath, this.serverPath });
/*    */   }
/*    */   
/*    */   public String[] getIncludes() {
/* 53 */     return this.includes;
/*    */   }
/*    */   
/* 56 */   public void setIncludes(String[] includes) { this.includes = includes; }
/*    */   
/*    */   public String toSimpleString()
/*    */   {
/* 60 */     return toSimpleString(60);
/*    */   }
/*    */   
/*    */   public String toSimpleString(int maxlen) {
/* 64 */     return String.format("%s <-> %s", new Object[] {
/* 65 */       StringUtil.simpString(this.clientPath, maxlen), StringUtil.simpString(this.serverPath, maxlen) });
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\bean\FtpUploadConf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */