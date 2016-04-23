/*     */ package org.sexftp.core.ftp;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import org.sexftp.core.exceptions.NeedRetryException;
/*     */ import org.sexftp.core.ftp.bean.FtpFile;
/*     */ import sexftp.views.AbstractSexftpView;
/*     */ import sexftp.views.IFtpStreamMonitor;
/*     */ 
/*     */ public class MyftpProxy
/*     */   implements XFtp
/*     */ {
/*     */   private static final int RETRY_TIMES = 8;
/*     */   private XFtp xftp;
/*     */   private Consoleable console;
/*  17 */   private String curDir = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCanTryException(Exception ex)
/*     */   {
/*  29 */     if ("User Disconnected".equals(this.connectstr))
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     Throwable ce = ex;
/*  35 */     for (int i = 0; i < 5; i++)
/*     */     {
/*  37 */       if ((ce instanceof SocketTimeoutException))
/*     */       {
/*  39 */         return true;
/*     */       }
/*  41 */       if ((ce instanceof NeedRetryException))
/*     */       {
/*  43 */         return true;
/*     */       }
/*  45 */       if ((ce.getMessage() != null) && (ce.getMessage().toLowerCase().indexOf("timeout") >= 0))
/*     */       {
/*  47 */         return true;
/*     */       }
/*  49 */       ce = ce.getCause();
/*  50 */       if (ce == null) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MyftpProxy(XFtp xftp, Consoleable console)
/*     */   {
/*  70 */     this.xftp = xftp;
/*  71 */     this.console = console;
/*     */   }
/*     */   
/*  74 */   public void cd(String directory) { RuntimeException er = null;
/*  75 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/*  78 */         this.xftp.cd(directory);
/*  79 */         this.curDir = directory;
/*  80 */         return;
/*     */       } catch (RuntimeException e) {
/*  82 */         if (isCanTryException(e))
/*     */         {
/*  84 */           er = e;
/*     */         }
/*     */         else
/*  87 */           throw e;
/*     */       }
/*  89 */       if (er != null)
/*     */       {
/*  91 */         this.console.console("cd " + directory + " failed!" + er.getMessage() + " try " + i + " again...");
/*     */       }
/*     */       
/*  94 */       if (reconnect(i + 1 < 8, null))
/*     */       {
/*  96 */         i = 0;
/*     */       }
/*     */     }
/*  99 */     if (er != null) {
/* 100 */       throw er;
/*     */     }
/*     */   }
/*     */   
/*     */   public void cdOrMakeIfNotExists(String directory)
/*     */   {
/* 106 */     RuntimeException er = null;
/* 107 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/* 110 */         this.xftp.cdOrMakeIfNotExists(directory);
/* 111 */         this.curDir = directory;
/* 112 */         return;
/*     */       } catch (RuntimeException e) {
/* 114 */         if (isCanTryException(e))
/*     */         {
/* 116 */           er = e;
/*     */         }
/*     */         else
/* 119 */           throw e;
/*     */       }
/* 121 */       if (er != null)
/*     */       {
/* 123 */         this.console.console("cd " + directory + " failed!" + er.getMessage() + " try again...");
/*     */       }
/*     */       
/* 126 */       if (reconnect(i + 1 < 8, null))
/*     */       {
/* 128 */         i = 0;
/*     */       }
/*     */     }
/* 131 */     if (er != null)
/* 132 */       throw er;
/*     */   }
/*     */   
/*     */   public void connect() {
/* 136 */     this.connectstr = "User connecte";
/* 137 */     RuntimeException er = null;
/* 138 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/* 141 */         this.xftp.connect();
/* 142 */         finallyed();
/*     */         
/* 144 */         return;
/*     */       } catch (RuntimeException e) {
/* 146 */         if (isCanTryException(e))
/*     */         {
/* 148 */           er = e;
/*     */         }
/*     */         else
/* 151 */           throw e;
/*     */       }
/* 153 */       if (er != null)
/*     */       {
/* 155 */         this.console.console("connect failed!" + er.getMessage() + " try " + i + " again...");
/*     */       }
/*     */       
/* 158 */       if (i + 1 >= 8)
/*     */       {
/* 160 */         if ((this.console instanceof AbstractSexftpView))
/*     */         {
/* 162 */           AbstractSexftpView view = (AbstractSexftpView)this.console;
/* 163 */           if (view.showQuestion("Server Operation Timeout,Retry [8] Times,Retry Again?"))
/*     */           {
/* 165 */             i = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */       finallyed();
/*     */     }
/* 177 */     if (er != null) {
/* 178 */       throw er;
/*     */     }
/*     */   }
/*     */   
/* 182 */   public void delete(String deleteFile) { this.xftp.delete(deleteFile); }
/*     */   
/* 184 */   private String connectstr = "";
/*     */   private String host;
/*     */   
/* 187 */   public void disconnect() { this.xftp.disconnect();
/* 188 */     finallyed();
/* 189 */     this.connectstr = "User Disconnected";
/*     */   }
/*     */   
/*     */   public void download(String downloadFile, String saveFile, IFtpStreamMonitor monitor)
/*     */   {
/* 194 */     String curDir = this.curDir;
/* 195 */     RuntimeException er = null;
/* 196 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/* 199 */         File sf = new File(saveFile);
/* 200 */         if (!sf.getParentFile().exists()) {
/* 201 */           sf.getParentFile().mkdirs();
/*     */         }
/* 203 */         this.xftp.download(downloadFile, saveFile, monitor);
/* 204 */         return;
/*     */       } catch (RuntimeException e) {
/* 206 */         if (isCanTryException(e))
/*     */         {
/* 208 */           er = e;
/*     */         }
/*     */         else
/* 211 */           throw e;
/*     */       }
/* 213 */       if (er != null)
/*     */       {
/* 215 */         this.console.console("download " + downloadFile + " failed!" + er.getMessage() + " try " + i + " again...");
/*     */       }
/*     */       
/* 218 */       if (reconnect(i + 1 < 8, curDir))
/*     */       {
/* 220 */         i = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 225 */     if (er != null)
/* 226 */       throw er;
/*     */   }
/*     */   
/*     */   public boolean isConnect() {
/* 230 */     boolean isConnect = this.xftp.isConnect();
/* 231 */     if (!isConnect) finallyed();
/* 232 */     return isConnect;
/*     */   }
/*     */   
/*     */   public List<FtpFile> listFiles() {
/* 236 */     String curDir = this.curDir;
/* 237 */     RuntimeException er = null;
/* 238 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/* 241 */         return this.xftp.listFiles();
/*     */       } catch (RuntimeException e) {
/* 243 */         if (isCanTryException(e))
/*     */         {
/* 245 */           er = e;
/*     */         }
/*     */         else
/* 248 */           throw e;
/*     */       }
/* 250 */       if (er != null)
/*     */       {
/* 252 */         this.console.console("listFiles Of " + curDir + " failed!" + er.getMessage() + " try again " + i + "...");
/*     */       }
/*     */       
/* 255 */       if (reconnect(i + 1 < 8, curDir))
/*     */       {
/* 257 */         i = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 262 */     if (er != null)
/*     */     {
/* 264 */       throw er;
/*     */     }
/* 266 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private int port;
/*     */   
/*     */   private String username;
/*     */   private String password;
/* 274 */   private String encode = "gbk";
/*     */   
/* 276 */   public void prepareConnect(String host, int port, String username, String password, String encode) { this.host = host;
/* 277 */     this.port = port;
/* 278 */     this.password = password;
/* 279 */     this.username = username;
/* 280 */     this.encode = (encode != null ? encode : "gbk");
/* 281 */     this.xftp.prepareConnect(host, port, username, password, encode);
/*     */   }
/*     */   
/*     */   public void upload(String uploadFile, IFtpStreamMonitor monitor)
/*     */   {
/* 286 */     String curDir = this.curDir;
/* 287 */     RuntimeException er = null;
/* 288 */     for (int i = 0; i < 8; i++)
/*     */     {
/*     */       try {
/* 291 */         this.xftp.upload(uploadFile, monitor);
/* 292 */         return;
/*     */       } catch (RuntimeException e) {
/* 294 */         if (isCanTryException(e))
/*     */         {
/* 296 */           er = e;
/*     */         }
/*     */         else
/* 299 */           throw e;
/*     */       }
/* 301 */       if (er != null)
/*     */       {
/* 303 */         this.console.console("upload " + uploadFile + " failed!" + er.getMessage() + " try " + i + " again...");
/*     */       }
/*     */       
/* 306 */       if (reconnect(i + 1 < 8, curDir))
/*     */       {
/* 308 */         i = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 313 */     if (er != null)
/* 314 */       throw er;
/*     */   }
/*     */   
/*     */   private void finallyed() {
/* 318 */     this.curDir = null;
/*     */   }
/*     */   
/*     */   private boolean reconnect(boolean isok, String curdir)
/*     */   {
/* 323 */     boolean retryAgain = false;
/* 324 */     this.xftp.disconnect();
/* 325 */     if (!isok)
/*     */     {
/* 327 */       if ((this.console instanceof AbstractSexftpView))
/*     */       {
/* 329 */         AbstractSexftpView view = (AbstractSexftpView)this.console;
/* 330 */         if (view.showQuestion("Server Operation Timeout,Retry [8] Times,Retry Again?"))
/*     */         {
/* 332 */           isok = true;
/* 333 */           retryAgain = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 342 */     if (isok)
/*     */     {
/* 344 */       connect();
/* 345 */       if (curdir != null)
/*     */       {
/* 347 */         cd(curdir);
/*     */       }
/*     */     }
/* 350 */     return retryAgain;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 354 */     return 
/* 355 */       String.format(
/* 356 */       "MyftpProxy [curDir=%s, host=%s, port=%s, username=%s, xftp=%s]", new Object[] {
/* 357 */       this.curDir, this.host, Integer.valueOf(this.port), this.username, this.xftp });
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\MyftpProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */