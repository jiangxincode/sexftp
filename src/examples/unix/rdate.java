/*    */ package examples.unix;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.util.Date;
/*    */ import org.apache.commons.net.time.TimeTCPClient;
/*    */ import org.apache.commons.net.time.TimeUDPClient;
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
/*    */ public final class rdate
/*    */ {
/*    */   public static final void timeTCP(String host)
/*    */     throws IOException
/*    */   {
/* 44 */     TimeTCPClient client = new TimeTCPClient();
/*    */     
/*    */ 
/* 47 */     client.setDefaultTimeout(60000);
/* 48 */     client.connect(host);
/* 49 */     System.out.println(client.getDate().toString());
/* 50 */     client.disconnect();
/*    */   }
/*    */   
/*    */   public static final void timeUDP(String host) throws IOException
/*    */   {
/* 55 */     TimeUDPClient client = new TimeUDPClient();
/*    */     
/*    */ 
/* 58 */     client.setDefaultTimeout(60000);
/* 59 */     client.open();
/* 60 */     System.out.println(client.getDate(InetAddress.getByName(host)).toString());
/* 61 */     client.close();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static final void main(String[] args)
/*    */   {
/* 68 */     if (args.length == 1)
/*    */     {
/*    */       try
/*    */       {
/* 72 */         timeTCP(args[0]);
/*    */       }
/*    */       catch (IOException e)
/*    */       {
/* 76 */         e.printStackTrace();
/* 77 */         System.exit(1);
/*    */       }
/*    */     }
/* 80 */     else if ((args.length == 2) && (args[0].equals("-udp")))
/*    */     {
/*    */       try
/*    */       {
/* 84 */         timeUDP(args[1]);
/*    */       }
/*    */       catch (IOException e)
/*    */       {
/* 88 */         e.printStackTrace();
/* 89 */         System.exit(1);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 94 */       System.err.println("Usage: rdate [-udp] <hostname>");
/* 95 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\rdate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */