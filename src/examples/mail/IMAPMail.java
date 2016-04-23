/*     */ package examples.mail;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.commons.net.PrintCommandListener;
/*     */ import org.apache.commons.net.imap.IMAPClient;
/*     */ import org.apache.commons.net.imap.IMAPSClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IMAPMail
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  39 */     if (args.length < 3)
/*     */     {
/*  41 */       System.err.println(
/*  42 */         "Usage: IMAPMail <imap server hostname> <username> <password> [TLS]");
/*  43 */       System.exit(1);
/*     */     }
/*     */     
/*  46 */     String server = args[0];
/*  47 */     String username = args[1];
/*  48 */     String password = args[2];
/*     */     
/*  50 */     String proto = args.length > 3 ? args[3] : null;
/*     */     
/*     */     IMAPClient imap;
/*     */     IMAPClient imap;
/*  54 */     if (proto != null) {
/*  55 */       System.out.println("Using secure protocol: " + proto);
/*  56 */       imap = new IMAPSClient(proto, true);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  62 */       imap = new IMAPClient();
/*     */     }
/*  64 */     System.out.println("Connecting to server " + server + " on " + imap.getDefaultPort());
/*     */     
/*     */ 
/*  67 */     imap.setDefaultTimeout(60000);
/*     */     
/*     */ 
/*  70 */     imap.addProtocolCommandListener(new PrintCommandListener(System.out, true));
/*     */     
/*     */     try
/*     */     {
/*  74 */       imap.connect(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  78 */       throw new RuntimeException("Could not connect to server.", e);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  83 */       if (!imap.login(username, password))
/*     */       {
/*  85 */         System.err.println("Could not login to server. Check password.");
/*  86 */         imap.disconnect();
/*  87 */         System.exit(3);
/*     */       }
/*     */       
/*  90 */       imap.setSoTimeout(6000);
/*     */       
/*  92 */       imap.capability();
/*     */       
/*  94 */       imap.select("inbox");
/*     */       
/*  96 */       imap.examine("inbox");
/*     */       
/*  98 */       imap.status("inbox", new String[] { "MESSAGES" });
/*     */       
/* 100 */       imap.logout();
/* 101 */       imap.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 105 */       System.out.println(imap.getReplyString());
/* 106 */       e.printStackTrace();
/* 107 */       System.exit(10);
/* 108 */       return;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\mail\IMAPMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */