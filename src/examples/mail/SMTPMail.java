/*     */ package examples.mail;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.net.PrintCommandListener;
/*     */ import org.apache.commons.net.io.Util;
/*     */ import org.apache.commons.net.smtp.SMTPClient;
/*     */ import org.apache.commons.net.smtp.SMTPReply;
/*     */ import org.apache.commons.net.smtp.SimpleSMTPHeader;
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
/*     */ public final class SMTPMail
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  49 */     List<String> ccList = new ArrayList();
/*     */     
/*  51 */     FileReader fileReader = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     if (args.length < 1)
/*     */     {
/*  58 */       System.err.println("Usage: mail smtpserver");
/*  59 */       System.exit(1);
/*     */     }
/*     */     
/*  62 */     String server = args[0];
/*     */     
/*  64 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*     */     try
/*     */     {
/*  68 */       System.out.print("From: ");
/*  69 */       System.out.flush();
/*     */       
/*  71 */       String sender = stdin.readLine();
/*     */       
/*  73 */       System.out.print("To: ");
/*  74 */       System.out.flush();
/*     */       
/*  76 */       String recipient = stdin.readLine();
/*     */       
/*  78 */       System.out.print("Subject: ");
/*  79 */       System.out.flush();
/*     */       
/*  81 */       String subject = stdin.readLine();
/*     */       
/*  83 */       SimpleSMTPHeader header = new SimpleSMTPHeader(sender, recipient, subject);
/*     */       
/*     */ 
/*     */       for (;;)
/*     */       {
/*  88 */         System.out.print("CC <enter one address per line, hit enter to end>: ");
/*  89 */         System.out.flush();
/*     */         
/*  91 */         String cc = stdin.readLine();
/*     */         
/*  93 */         if ((cc == null) || (cc.length() == 0)) {
/*     */           break;
/*     */         }
/*  96 */         header.addCC(cc.trim());
/*  97 */         ccList.add(cc.trim());
/*     */       }
/*     */       String cc;
/* 100 */       System.out.print("Filename: ");
/* 101 */       System.out.flush();
/*     */       
/* 103 */       String filename = stdin.readLine();
/*     */       
/*     */       try
/*     */       {
/* 107 */         fileReader = new FileReader(filename);
/*     */       }
/*     */       catch (FileNotFoundException e)
/*     */       {
/* 111 */         System.err.println("File not found. " + e.getMessage());
/*     */       }
/*     */       
/* 114 */       SMTPClient client = new SMTPClient();
/* 115 */       client.addProtocolCommandListener(new PrintCommandListener(
/* 116 */         new PrintWriter(System.out), true));
/*     */       
/* 118 */       client.connect(server);
/*     */       
/* 120 */       if (!SMTPReply.isPositiveCompletion(client.getReplyCode()))
/*     */       {
/* 122 */         client.disconnect();
/* 123 */         System.err.println("SMTP server refused connection.");
/* 124 */         System.exit(1);
/*     */       }
/*     */       
/* 127 */       client.login();
/*     */       
/* 129 */       client.setSender(sender);
/* 130 */       client.addRecipient(recipient);
/*     */       
/*     */ 
/*     */ 
/* 134 */       for (String recpt : ccList) {
/* 135 */         client.addRecipient(recpt);
/*     */       }
/* 137 */       Writer writer = client.sendMessageData();
/*     */       
/* 139 */       if (writer != null)
/*     */       {
/* 141 */         writer.write(header.toString());
/* 142 */         Util.copyReader(fileReader, writer);
/* 143 */         writer.close();
/* 144 */         client.completePendingCommand();
/*     */       }
/*     */       
/* 147 */       if (fileReader != null) {
/* 148 */         fileReader.close();
/*     */       }
/*     */       
/* 151 */       client.logout();
/*     */       
/* 153 */       client.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 157 */       e.printStackTrace();
/* 158 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\mail\SMTPMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */