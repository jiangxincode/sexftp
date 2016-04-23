/*    */ package examples.unix;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.commons.net.whois.WhoisClient;
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
/*    */ public final class fwhois
/*    */ {
/*    */   public static final void main(String[] args)
/*    */   {
/* 38 */     InetAddress address = null;
/*    */     
/*    */ 
/* 41 */     if (args.length != 1)
/*    */     {
/* 43 */       System.err.println("usage: fwhois handle[@<server>]");
/* 44 */       System.exit(1);
/*    */     }
/*    */     
/* 47 */     int index = args[0].lastIndexOf("@");
/*    */     
/* 49 */     WhoisClient whois = new WhoisClient();
/*    */     
/* 51 */     whois.setDefaultTimeout(60000);
/*    */     String host;
/* 53 */     String handle; String host; if (index == -1)
/*    */     {
/* 55 */       String handle = args[0];
/* 56 */       host = "whois.internic.net";
/*    */     }
/*    */     else
/*    */     {
/* 60 */       handle = args[0].substring(0, index);
/* 61 */       host = args[0].substring(index + 1);
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 66 */       address = InetAddress.getByName(host);
/* 67 */       System.out.println("[" + address.getHostName() + "]");
/*    */     }
/*    */     catch (UnknownHostException e)
/*    */     {
/* 71 */       System.err.println("Error unknown host: " + e.getMessage());
/* 72 */       System.exit(1);
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 77 */       whois.connect(address);
/* 78 */       System.out.print(whois.query(handle));
/* 79 */       whois.disconnect();
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 83 */       System.err.println("Error I/O exception: " + e.getMessage());
/* 84 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\fwhois.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */