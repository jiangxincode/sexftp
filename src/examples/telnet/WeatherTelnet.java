/*    */ package examples.telnet;
/*    */ 
/*    */ import examples.util.IOUtil;
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.net.telnet.TelnetClient;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WeatherTelnet
/*    */ {
/*    */   public static final void main(String[] args)
/*    */   {
/* 46 */     TelnetClient telnet = new TelnetClient();
/*    */     
/*    */     try
/*    */     {
/* 50 */       telnet.connect("rainmaker.wunderground.com", 3000);
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 54 */       e.printStackTrace();
/* 55 */       System.exit(1);
/*    */     }
/*    */     
/* 58 */     IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(), 
/* 59 */       System.in, System.out);
/*    */     
/*    */     try
/*    */     {
/* 63 */       telnet.disconnect();
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 67 */       e.printStackTrace();
/* 68 */       System.exit(1);
/*    */     }
/*    */     
/* 71 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\telnet\WeatherTelnet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */