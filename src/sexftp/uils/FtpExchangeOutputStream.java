/*    */ package sexftp.uils;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import org.eclipse.ui.console.MessageConsoleStream;
/*    */ import org.sexftp.core.utils.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtpExchangeOutputStream
/*    */   extends PrintWriter
/*    */ {
/*    */   private String encode;
/* 20 */   private StringBuffer listenStr = null;
/* 21 */   private String preStr = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FtpExchangeOutputStream(MessageConsoleStream out, String encode)
/*    */   {
/* 28 */     super(out);
/* 29 */     this.encode = encode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void println(String str)
/*    */   {
/* 36 */     String isobk = StringUtil.bakFromiso88591(str, this.encode);
/* 37 */     if (!isobk.equals(str))
/*    */     {
/* 39 */       str = str + " [" + isobk + "]";
/*    */     }
/* 41 */     super.println(str);
/*    */   }
/*    */   
/*    */ 
/*    */   public void print(String str)
/*    */   {
/* 47 */     String isobk = StringUtil.bakFromiso88591(str, this.encode);
/* 48 */     StringBuffer sb = new StringBuffer();
/* 49 */     StringBuffer gb = new StringBuffer();
/* 50 */     char[] arrayOfChar; int j = (arrayOfChar = isobk.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/*    */       
/* 52 */       if ((c >= 0) && (c <= '­'))
/*    */       {
/* 54 */         if (gb.length() > 0)
/*    */         {
/* 56 */           String iso88591 = StringUtil.iso88591(gb.toString(), this.encode);
/* 57 */           sb.append(iso88591 + "[" + gb.toString() + "]");
/* 58 */           gb = new StringBuffer();
/*    */         }
/* 60 */         sb.append(c);
/*    */ 
/*    */       }
/*    */       else
/*    */       {
/* 65 */         gb.append(c);
/*    */       }
/*    */     }
/* 68 */     if (this.listenStr != null)
/*    */     {
/* 70 */       this.listenStr.append(sb.toString());
/* 71 */       this.listenStr.append("\r\n");
/*    */     }
/*    */     
/* 74 */     String p = String.format("%s %s %s", new Object[] { new SimpleDateFormat("yyyy-M-d HH:mm:ss").format(new Date()), this.preStr, sb.toString() });
/* 75 */     super.print(p);
/*    */   }
/*    */   
/*    */ 
/*    */   public StringBuffer getListenStr()
/*    */   {
/* 81 */     return this.listenStr;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setListenStr(StringBuffer listenStr)
/*    */   {
/* 87 */     this.listenStr = listenStr;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getPreStr()
/*    */   {
/* 93 */     return this.preStr;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPreStr(String preStr)
/*    */   {
/* 99 */     this.preStr = preStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\FtpExchangeOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */