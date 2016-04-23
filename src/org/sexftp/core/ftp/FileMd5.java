/*    */ package org.sexftp.core.ftp;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.security.MessageDigest;
/*    */ import org.eclipse.core.runtime.IProgressMonitor;
/*    */ import org.sexftp.core.utils.StringUtil;
/*    */ 
/*    */ public class FileMd5
/*    */ {
/*    */   public static String getMD5(File file)
/*    */   {
/* 15 */     return getInerMD5(file, null, null);
/*    */   }
/*    */   
/* 18 */   public static String getMD5(File file, IProgressMonitor monitor) { return getInerMD5(file, monitor, null); }
/*    */   
/*    */ 
/* 21 */   public static String getMD5(File file, IProgressMonitor monitor, String semd5) { return getInerMD5(file, monitor, semd5); }
/*    */   
/*    */   private static String getInerMD5(File file, IProgressMonitor monitor, String semd5) {
/* 24 */     FileInputStream fis = null;
/*    */     try {
/* 26 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 27 */       MessageDigest mdxor = MessageDigest.getInstance("SHA1");
/* 28 */       byte[] fb = semd5 != null ? semd5.getBytes("gbk") : file.getName().getBytes("gbk");
/* 29 */       fis = new FileInputStream(file);
/* 30 */       String filename = file.getAbsolutePath();
/* 31 */       long dilled = 0L;
/* 32 */       long total = file.length();
/*    */       
/* 34 */       byte[] buffer = new byte[' '];
/* 35 */       int length = -1;
/* 36 */       while ((length = fis.read(buffer)) != -1) {
/* 37 */         md.update(buffer, 0, length);
/* 38 */         for (int i = 0; i < fb.length; i++) {
/* 39 */           int tmp97_95 = i; byte[] tmp97_93 = buffer;tmp97_93[tmp97_95] = ((byte)(tmp97_93[tmp97_95] ^ fb[i]));
/*    */         }
/* 41 */         mdxor.update(buffer, 0, length);
/*    */         
/* 43 */         if (monitor != null)
/*    */         {
/* 45 */           if (monitor.isCanceled())
/* 46 */             throw new org.sexftp.core.exceptions.AbortException("User Canceled.");
/* 47 */           if (total > 20000000L)
/*    */           {
/* 49 */             dilled += length;
/*    */             
/* 51 */             monitor.subTask(String.format("(%s in %s) \r\nAnalyzing Content Of %s", new Object[] { StringUtil.getHumanSize(dilled), StringUtil.getHumanSize(total), filename }));
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 56 */       String filebody = bytesToString(md.digest());
/* 57 */       String fileNameAndBody = bytesToString(mdxor.digest());
/* 58 */       return fileNameAndBody + filebody;
/*    */     }
/*    */     catch (RuntimeException ex) {
/* 61 */       throw ex;
/*    */     }
/*    */     catch (Exception ex) {
/* 64 */       throw new RuntimeException(ex);
/*    */     }
/*    */     finally {
/*    */       try {
/* 68 */         fis.close();
/*    */       }
/*    */       catch (IOException localIOException2) {}
/*    */     }
/*    */   }
/*    */   
/*    */   public static String bytesToString(byte[] data)
/*    */   {
/* 76 */     char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
/* 77 */       'a', 'b', 'c', 'd', 'e', 'f' };
/* 78 */     char[] temp = new char[data.length * 2];
/* 79 */     for (int i = 0; i < data.length; i++) {
/* 80 */       byte b = data[i];
/* 81 */       temp[(i * 2)] = hexDigits[(b >>> 4 & 0xF)];
/* 82 */       temp[(i * 2 + 1)] = hexDigits[(b & 0xF)];
/*    */     }
/* 84 */     return new String(temp);
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 89 */     File f = new File("d:/out.t");
/* 90 */     System.out.println(getMD5(f));
/* 91 */     String s1 = "93ee04140d60241002da80756a8eda07";
/* 92 */     String s2 = "93ee04140d60241002da80756a8eda07";
/* 93 */     System.out.println(s1.equals(s2));
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\FileMd5.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */