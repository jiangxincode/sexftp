/*    */ package org.sexftp.core.ftp.bean;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtpFile
/*    */ {
/*    */   private String name;
/*    */   private boolean isfolder;
/*    */   private long size;
/*    */   private Calendar timeStamp;
/*    */   
/*    */   public FtpFile(String name, boolean isfolder, long size, Calendar timeStamp)
/*    */   {
/* 18 */     this.name = name;
/* 19 */     this.isfolder = isfolder;
/* 20 */     this.size = size;
/* 21 */     this.timeStamp = timeStamp;
/*    */   }
/*    */   
/* 24 */   public String getName() { return this.name; }
/*    */   
/*    */   public boolean isIsfolder() {
/* 27 */     return this.isfolder;
/*    */   }
/*    */   
/* 30 */   public long getSize() { return this.size; }
/*    */   
/*    */   public Calendar getTimeStamp() {
/* 33 */     return this.timeStamp;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\bean\FtpFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */