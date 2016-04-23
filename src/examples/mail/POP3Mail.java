/*     */ package examples.mail;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.net.PrintCommandListener;
/*     */ import org.apache.commons.net.pop3.POP3Client;
/*     */ import org.apache.commons.net.pop3.POP3MessageInfo;
/*     */ import org.apache.commons.net.pop3.POP3SClient;
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
/*     */ public final class POP3Mail
/*     */ {
/*     */   public static final void printMessageInfo(BufferedReader reader, int id)
/*     */     throws IOException
/*     */   {
/*  43 */     String from = "";
/*  44 */     String subject = "";
/*     */     String line;
/*  46 */     while ((line = reader.readLine()) != null) {
/*     */       String line;
/*  48 */       String lower = line.toLowerCase(Locale.ENGLISH);
/*  49 */       if (lower.startsWith("from: ")) {
/*  50 */         from = line.substring(6).trim();
/*  51 */       } else if (lower.startsWith("subject: ")) {
/*  52 */         subject = line.substring(9).trim();
/*     */       }
/*     */     }
/*     */     
/*  56 */     System.out.println(Integer.toString(id) + " From: " + from + "  Subject: " + subject);
/*     */   }
/*     */   
/*     */   public static final void main(String[] args)
/*     */   {
/*  61 */     if (args.length < 3)
/*     */     {
/*  63 */       System.err.println(
/*  64 */         "Usage: POP3Mail <pop3 server hostname> <username> <password> [TLS [true=implicit]]");
/*  65 */       System.exit(1);
/*     */     }
/*     */     
/*  68 */     String server = args[0];
/*  69 */     String username = args[1];
/*  70 */     String password = args[2];
/*     */     
/*  72 */     String proto = args.length > 3 ? args[3] : null;
/*  73 */     boolean implicit = args.length > 4 ? Boolean.parseBoolean(args[4]) : false;
/*     */     
/*     */     POP3Client pop3;
/*     */     POP3Client pop3;
/*  77 */     if (proto != null) {
/*  78 */       System.out.println("Using secure protocol: " + proto);
/*  79 */       pop3 = new POP3SClient(proto, implicit);
/*     */     } else {
/*  81 */       pop3 = new POP3Client();
/*     */     }
/*  83 */     System.out.println("Connecting to server " + server + " on " + pop3.getDefaultPort());
/*     */     
/*     */ 
/*  86 */     pop3.setDefaultTimeout(60000);
/*     */     
/*     */ 
/*  89 */     pop3.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
/*     */     
/*     */     try
/*     */     {
/*  93 */       pop3.connect(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  97 */       System.err.println("Could not connect to server.");
/*  98 */       e.printStackTrace();
/*  99 */       System.exit(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 104 */       if (!pop3.login(username, password))
/*     */       {
/* 106 */         System.err.println("Could not login to server.  Check password.");
/* 107 */         pop3.disconnect();
/* 108 */         System.exit(1);
/*     */       }
/*     */       
/* 111 */       POP3MessageInfo[] messages = pop3.listMessages();
/*     */       
/* 113 */       if (messages == null)
/*     */       {
/* 115 */         System.err.println("Could not retrieve message list.");
/* 116 */         pop3.disconnect();
/* 117 */         return;
/*     */       }
/* 119 */       if (messages.length == 0)
/*     */       {
/* 121 */         System.out.println("No messages");
/* 122 */         pop3.logout();
/* 123 */         pop3.disconnect(); return;
/*     */       }
/*     */       
/*     */       POP3MessageInfo[] arrayOfPOP3MessageInfo1;
/* 127 */       int j = (arrayOfPOP3MessageInfo1 = messages).length; for (int i = 0; i < j; i++) { POP3MessageInfo msginfo = arrayOfPOP3MessageInfo1[i];
/* 128 */         BufferedReader reader = (BufferedReader)pop3.retrieveMessageTop(msginfo.number, 0);
/*     */         
/* 130 */         if (reader == null) {
/* 131 */           System.err.println("Could not retrieve message header.");
/* 132 */           pop3.disconnect();
/* 133 */           System.exit(1);
/*     */         }
/* 135 */         printMessageInfo(reader, msginfo.number);
/*     */       }
/*     */       
/* 138 */       pop3.logout();
/* 139 */       pop3.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 143 */       e.printStackTrace();
/* 144 */       return;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\mail\POP3Mail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */