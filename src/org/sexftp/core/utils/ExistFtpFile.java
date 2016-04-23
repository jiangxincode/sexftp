/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.sexftp.core.exceptions.FtpNoSuchFileException;
/*    */ import org.sexftp.core.exceptions.SRuntimeException;
/*    */ import org.sexftp.core.ftp.XFtp;
/*    */ import org.sexftp.core.ftp.bean.FtpFile;
/*    */ 
/*    */ public class ExistFtpFile
/*    */ {
/* 15 */   private Map<String, List<FtpFile>> cFiles = new HashMap();
/*    */   
/* 17 */   private XFtp ftp = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExistFtpFile(XFtp ftp)
/*    */   {
/* 26 */     this.ftp = ftp;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public FtpFile existsFtpFile(String serverPath)
/*    */   {
/* 33 */     String cserverPath = serverPath;
/* 34 */     if (!cserverPath.endsWith("/"))
/*    */     {
/* 36 */       cserverPath = cserverPath.substring(0, cserverPath.lastIndexOf("/") + 1);
/* 37 */       if (!this.cFiles.containsKey(cserverPath)) {
/*    */         try
/*    */         {
/* 40 */           this.ftp.cd(cserverPath);
/* 41 */           this.cFiles.put(cserverPath, this.ftp.listFiles());
/*    */         } catch (FtpNoSuchFileException localFtpNoSuchFileException) {
/* 43 */           this.cFiles.put(cserverPath, new ArrayList());
/*    */         }
/*    */       }
/*    */       
/* 47 */       for (FtpFile ffile : (List)this.cFiles.get(cserverPath))
/*    */       {
/* 49 */         if (ffile.getName().equals(new File(serverPath).getName()))
/*    */         {
/* 51 */           return ffile;
/*    */         }
/*    */       }
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 58 */       return null;
/*    */     }
/* 60 */     throw new SRuntimeException("Cann't Check,[" + serverPath + "] Not File!");
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\ExistFtpFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */