/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintStream;
/*    */ import org.mozilla.intl.chardet.nsDetector;
/*    */ import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HtmlCharsetDetector
/*    */ {
/* 21 */   public static boolean found = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void main(String[] argv)
/*    */     throws Exception
/*    */   {
/* 30 */     nsDetector det = new nsDetector(3);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 35 */     det.Init(new nsICharsetDetectionObserver() {
/*    */       public void Notify(String charset) {
/* 37 */         HtmlCharsetDetector.found = true;
/* 38 */         System.out.println("CHARSET FIND = " + charset);
/*    */       }
/*    */       
/* 41 */     });
/* 42 */     InputStream imp = new FileInputStream("D:/Documents and Settings/Administrator/Workspaces/MyEclipse 8.5/kkfun_oa/WebContent/css/common.css");
/* 43 */     String str = "你好繁体字";
/* 44 */     for (int i = 0; i < 50; i++)
/* 45 */       str = str + "你好繁体字";
/* 46 */     ByteArrayInputStream bins = new ByteArrayInputStream(str.getBytes("utf-8"));
/* 47 */     imp = bins;
/*    */     
/* 49 */     byte[] buf = new byte['Ѐ'];
/*    */     
/* 51 */     boolean done = false;
/* 52 */     boolean isAscii = true;
/*    */     int len;
/* 54 */     while ((len = imp.read(buf, 0, buf.length)) != -1)
/*    */     {
/*    */       int len;
/* 57 */       if (isAscii) {
/* 58 */         isAscii = det.isAscii(buf, len);
/*    */       }
/*    */       
/* 61 */       if ((!isAscii) && (!done))
/* 62 */         done = det.DoIt(buf, len, false);
/*    */     }
/* 64 */     det.DataEnd();
/*    */     
/* 66 */     if (isAscii) {
/* 67 */       System.out.println("CHARSET = ASCII");
/* 68 */       found = true;
/*    */     }
/*    */     
/* 71 */     if (!found) {
/* 72 */       String[] prob = det.getProbableCharsets();
/* 73 */       for (int i = 0; i < prob.length; i++) {
/* 74 */         System.out.println("Probable Charset = " + prob[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\HtmlCharsetDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */