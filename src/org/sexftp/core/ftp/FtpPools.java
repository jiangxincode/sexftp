/*     */ package org.sexftp.core.ftp;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.sexftp.core.exceptions.BizException;
/*     */ import org.sexftp.core.exceptions.SRuntimeException;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtpPools
/*     */   implements Consoleable
/*     */ {
/*  21 */   private static Map<String, XFtp> CASH = new Hashtable();
/*  22 */   public static Map<String, Class<? extends XFtp>> FTP_MAP = new LinkedHashMap();
/*     */   
/*  24 */   static { FTP_MAP.put("ftp", MyFtp.class);
/*  25 */     FTP_MAP.put("sftp", MySFTP.class);
/*  26 */     FTP_MAP.put("ftps", MyFtps.class);
/*  27 */     FTP_MAP.put("ftpes", MyFtpes.class);
/*  28 */     FTP_MAP.put("file", MyFile.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private FtpConf conf;
/*     */   
/*     */ 
/*     */   private Consoleable console;
/*     */   
/*     */ 
/*     */   public FtpPools(FtpConf conf, Consoleable console)
/*     */   {
/*  42 */     this.conf = conf;
/*  43 */     this.console = (console != null ? console : this);
/*     */   }
/*     */   
/*     */   public XFtp getNewFtp() {
/*  47 */     XFtp xftp = null;
/*  48 */     Class<? extends XFtp> xftpcls = (Class)FTP_MAP.get(this.conf.getServerType());
/*  49 */     if (xftpcls == null)
/*     */     {
/*  51 */       throw new BizException("Server Type:" + this.conf.getServerType() + " in " + this.conf.getName() + " Not Supported!");
/*     */     }
/*     */     try {
/*  54 */       xftp = new MyftpProxy((XFtp)xftpcls.newInstance(), this.console);
/*     */     } catch (Exception e) {
/*  56 */       throw new SRuntimeException(e);
/*     */     }
/*  58 */     xftp.prepareConnect(this.conf.getHost(), this.conf.getPort().intValue(), this.conf.getUsername(), this.conf.getPassword(), null);
/*  59 */     return xftp;
/*     */   }
/*     */   
/*     */   public XFtp getFtp() {
/*  63 */     synchronized (FtpPools.class) {
/*  64 */       XFtp xftp = null;
/*  65 */       String key = String.format("%s:%d@%s@%s", new Object[] { this.conf.getHost(), this.conf.getPort(), this.conf.getUsername(), this.conf.getServerType() });
/*  66 */       if (CASH.containsKey(key)) {
/*  67 */         xftp = (XFtp)CASH.get(key);
/*     */       }
/*     */       else {
/*     */         try
/*     */         {
/*  72 */           Class<? extends XFtp> xftpcls = (Class)FTP_MAP.get(this.conf.getServerType());
/*  73 */           if (xftpcls == null)
/*     */           {
/*  75 */             throw new BizException("Server Type [" + this.conf.getServerType() + "] in [" + this.conf.getName() + "] Not Supported!\r\n" + 
/*  76 */               "We Supported " + FTP_MAP.keySet().toString());
/*     */           }
/*  78 */           xftp = new MyftpProxy((XFtp)xftpcls.newInstance(), this.console);
/*  79 */           xftp.prepareConnect(this.conf.getHost(), this.conf.getPort().intValue(), this.conf.getUsername(), this.conf.getPassword(), null);
/*  80 */           CASH.put(key, xftp);
/*     */         }
/*     */         catch (RuntimeException e)
/*     */         {
/*  84 */           throw e;
/*     */         }
/*     */         catch (Exception e) {
/*  87 */           throw new SRuntimeException(e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  92 */       return xftp;
/*     */     }
/*     */   }
/*     */   
/*     */   public void disconnectAll() {
/*  97 */     Set<Map.Entry<String, XFtp>> entrySet = CASH.entrySet();
/*  98 */     for (Map.Entry<String, XFtp> entry : entrySet) {
/*     */       try {
/* 100 */         ((XFtp)entry.getValue()).disconnect();
/* 101 */         if (this.console != null) this.console.console("disconceted " + entry.getValue());
/*     */       } catch (Exception e) {
/* 103 */         if (this.console != null) this.console.console(e.getMessage());
/*     */       }
/*     */     }
/* 106 */     CASH.clear();
/*     */   }
/*     */   
/*     */   public XFtp getConnectedFtp() {
/* 110 */     XFtp xftp = getFtp();
/* 111 */     if (!xftp.isConnect())
/*     */     {
/* 113 */       this.console.console("Connecting... " + this.conf.toString());
/* 114 */       xftp.connect();
/* 115 */       this.console.console("Connected.");
/*     */     }
/* 117 */     return xftp;
/*     */   }
/*     */   
/*     */   public void console(String str) {}
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\FtpPools.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */