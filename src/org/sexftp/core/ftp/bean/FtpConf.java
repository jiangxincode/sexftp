/*    */ package org.sexftp.core.ftp.bean;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtpConf
/*    */ {
/* 13 */   String host = "localhost";
/* 14 */   Integer port = Integer.valueOf(21);
/* 15 */   String username = "root";
/*    */   String password;
/* 17 */   String serverType = "ftp";
/*    */   String name;
/*    */   private List<FtpUploadConf> ftpUploadConfList;
/*    */   
/* 21 */   public String getHost() { return this.host; }
/*    */   
/*    */   public void setHost(String host) {
/* 24 */     this.host = host;
/*    */   }
/*    */   
/* 27 */   public Integer getPort() { return this.port; }
/*    */   
/*    */   public void setPort(Integer port) {
/* 30 */     this.port = port;
/*    */   }
/*    */   
/* 33 */   public String getUsername() { return this.username; }
/*    */   
/*    */   public void setUsername(String username) {
/* 36 */     this.username = username;
/*    */   }
/*    */   
/* 39 */   public String getPassword() { return this.password; }
/*    */   
/*    */   public void setPassword(String password) {
/* 42 */     this.password = password;
/*    */   }
/*    */   
/* 45 */   public String getServerType() { return this.serverType; }
/*    */   
/*    */   public void setServerType(String serverType) {
/* 48 */     this.serverType = serverType;
/*    */   }
/*    */   
/*    */ 
/*    */   public List<FtpUploadConf> getFtpUploadConfList()
/*    */   {
/* 54 */     return this.ftpUploadConfList;
/*    */   }
/*    */   
/* 57 */   public void setFtpUploadConfList(List<FtpUploadConf> ftpUploadConfList) { this.ftpUploadConfList = ftpUploadConfList; }
/*    */   
/*    */   public String getName() {
/* 60 */     return this.name;
/*    */   }
/*    */   
/* 63 */   public void setName(String name) { this.name = name; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 67 */     return String.format("%s[%s - %s:%s@%s]", new Object[] { this.name, this.serverType, this.host, 
/* 68 */       this.port, this.username });
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\bean\FtpConf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */