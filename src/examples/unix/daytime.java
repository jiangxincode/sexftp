/*    */ package examples.unix;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import org.apache.commons.net.daytime.DaytimeTCPClient;
/*    */ import org.apache.commons.net.daytime.DaytimeUDPClient;
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
/*    */ public final class daytime
/*    */ {
/*    */   public static final void daytimeTCP(String host)
/*    */     throws IOException
/*    */   {
/* 42 */     DaytimeTCPClient client = new DaytimeTCPClient();
/*    */     
/*    */ 
/* 45 */     client.setDefaultTimeout(60000);
/* 46 */     client.connect(host);
/* 47 */     System.out.println(client.getTime().trim());
/* 48 */     client.disconnect();
/*    */   }
/*    */   
/*    */   public static final void daytimeUDP(String host) throws IOException
/*    */   {
/* 53 */     DaytimeUDPClient client = new DaytimeUDPClient();
/*    */     
/*    */ 
/* 56 */     client.setDefaultTimeout(60000);
/* 57 */     client.open();
/* 58 */     System.out.println(client.getTime(
/* 59 */       InetAddress.getByName(host)).trim());
/* 60 */     client.close();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static final void main(String[] args)
/*    */   {
/* 67 */     if (args.length == 1)
/*    */     {
/*    */       try
/*    */       {
/* 71 */         daytimeTCP(args[0]);
/*    */       }
/*    */       catch (IOException e)
/*    */       {
/* 75 */         e.printStackTrace();
/* 76 */         System.exit(1);
/*    */       }
/*    */     }
/* 79 */     else if ((args.length == 2) && (args[0].equals("-udp")))
/*    */     {
/*    */       try
/*    */       {
/* 83 */         daytimeUDP(args[1]);
/*    */       }
/*    */       catch (IOException e)
/*    */       {
/* 87 */         e.printStackTrace();
/* 88 */         System.exit(1);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 93 */       System.err.println("Usage: daytime [-udp] <hostname>");
/* 94 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\daytime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */