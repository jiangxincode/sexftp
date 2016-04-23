/*    */ package org.sexftp.core.bean;
/*    */ 
/*    */ public class FileZillaServer {
/*    */   private String Host;
/*    */   private String Port;
/*    */   private String Protocol;
/*    */   private String User;
/*    */   private String Pass;
/*    */   
/* 10 */   public String getHost() { return this.Host; }
/*    */   
/*    */   public void setHost(String host) {
/* 13 */     this.Host = host;
/*    */   }
/*    */   
/* 16 */   public String getPort() { return this.Port; }
/*    */   
/*    */   public void setPort(String port) {
/* 19 */     this.Port = port;
/*    */   }
/*    */   
/* 22 */   public String getProtocol() { return this.Protocol; }
/*    */   
/*    */   public void setProtocol(String protocol) {
/* 25 */     this.Protocol = protocol;
/*    */   }
/*    */   
/* 28 */   public String getUser() { return this.User; }
/*    */   
/*    */   public void setUser(String user) {
/* 31 */     this.User = user;
/*    */   }
/*    */   
/* 34 */   public String getPass() { return this.Pass; }
/*    */   
/*    */   public void setPass(String pass) {
/* 37 */     this.Pass = pass;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 42 */     int result = 1;
/* 43 */     result = 31 * result + (this.Host == null ? 0 : this.Host.hashCode());
/* 44 */     result = 31 * result + (this.Pass == null ? 0 : this.Pass.hashCode());
/* 45 */     result = 31 * result + (this.Port == null ? 0 : this.Port.hashCode());
/* 46 */     result = 31 * result + (
/* 47 */       this.Protocol == null ? 0 : this.Protocol.hashCode());
/* 48 */     result = 31 * result + (this.User == null ? 0 : this.User.hashCode());
/* 49 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 53 */     if (this == obj)
/* 54 */       return true;
/* 55 */     if (obj == null)
/* 56 */       return false;
/* 57 */     if (getClass() != obj.getClass())
/* 58 */       return false;
/* 59 */     FileZillaServer other = (FileZillaServer)obj;
/* 60 */     if (this.Host == null) {
/* 61 */       if (other.Host != null)
/* 62 */         return false;
/* 63 */     } else if (!this.Host.equals(other.Host))
/* 64 */       return false;
/* 65 */     if (this.Pass == null) {
/* 66 */       if (other.Pass != null)
/* 67 */         return false;
/* 68 */     } else if (!this.Pass.equals(other.Pass))
/* 69 */       return false;
/* 70 */     if (this.Port == null) {
/* 71 */       if (other.Port != null)
/* 72 */         return false;
/* 73 */     } else if (!this.Port.equals(other.Port))
/* 74 */       return false;
/* 75 */     if (this.Protocol == null) {
/* 76 */       if (other.Protocol != null)
/* 77 */         return false;
/* 78 */     } else if (!this.Protocol.equals(other.Protocol))
/* 79 */       return false;
/* 80 */     if (this.User == null) {
/* 81 */       if (other.User != null)
/* 82 */         return false;
/* 83 */     } else if (!this.User.equals(other.User))
/* 84 */       return false;
/* 85 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\bean\FileZillaServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */