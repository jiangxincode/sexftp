/*     */ package examples.nntp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import org.apache.commons.net.PrintCommandListener;
/*     */ import org.apache.commons.net.io.Util;
/*     */ import org.apache.commons.net.nntp.NNTPClient;
/*     */ import org.apache.commons.net.nntp.NNTPReply;
/*     */ import org.apache.commons.net.nntp.SimpleNNTPHeader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PostMessage
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  50 */     FileReader fileReader = null;
/*     */     
/*     */ 
/*     */ 
/*  54 */     if (args.length < 1)
/*     */     {
/*  56 */       System.err.println("Usage: post newsserver");
/*  57 */       System.exit(1);
/*     */     }
/*     */     
/*  60 */     String server = args[0];
/*     */     
/*  62 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*     */     try
/*     */     {
/*  66 */       System.out.print("From: ");
/*  67 */       System.out.flush();
/*     */       
/*  69 */       String from = stdin.readLine();
/*     */       
/*  71 */       System.out.print("Subject: ");
/*  72 */       System.out.flush();
/*     */       
/*  74 */       String subject = stdin.readLine();
/*     */       
/*  76 */       SimpleNNTPHeader header = new SimpleNNTPHeader(from, subject);
/*     */       
/*  78 */       System.out.print("Newsgroup: ");
/*  79 */       System.out.flush();
/*     */       
/*  81 */       String newsgroup = stdin.readLine();
/*  82 */       header.addNewsgroup(newsgroup);
/*     */       
/*     */       for (;;)
/*     */       {
/*  86 */         System.out.print("Additional Newsgroup <Hit enter to end>: ");
/*  87 */         System.out.flush();
/*     */         
/*     */ 
/*  90 */         newsgroup = stdin.readLine().trim();
/*     */         
/*  92 */         if (newsgroup.length() == 0) {
/*     */           break;
/*     */         }
/*  95 */         header.addNewsgroup(newsgroup);
/*     */       }
/*     */       
/*  98 */       System.out.print("Organization: ");
/*  99 */       System.out.flush();
/*     */       
/* 101 */       String organization = stdin.readLine();
/*     */       
/* 103 */       System.out.print("References: ");
/* 104 */       System.out.flush();
/*     */       
/* 106 */       String references = stdin.readLine();
/*     */       
/* 108 */       if ((organization != null) && (organization.length() > 0)) {
/* 109 */         header.addHeaderField("Organization", organization);
/*     */       }
/* 111 */       if ((references != null) && (references.length() > 0)) {
/* 112 */         header.addHeaderField("References", references);
/*     */       }
/* 114 */       header.addHeaderField("X-Newsreader", "NetComponents");
/*     */       
/* 116 */       System.out.print("Filename: ");
/* 117 */       System.out.flush();
/*     */       
/* 119 */       String filename = stdin.readLine();
/*     */       
/*     */       try
/*     */       {
/* 123 */         fileReader = new FileReader(filename);
/*     */       }
/*     */       catch (FileNotFoundException e)
/*     */       {
/* 127 */         System.err.println("File not found. " + e.getMessage());
/* 128 */         System.exit(1);
/*     */       }
/*     */       
/* 131 */       NNTPClient client = new NNTPClient();
/* 132 */       client.addProtocolCommandListener(new PrintCommandListener(
/* 133 */         new PrintWriter(System.out), true));
/*     */       
/* 135 */       client.connect(server);
/*     */       
/* 137 */       if (!NNTPReply.isPositiveCompletion(client.getReplyCode()))
/*     */       {
/* 139 */         client.disconnect();
/* 140 */         System.err.println("NNTP server refused connection.");
/* 141 */         System.exit(1);
/*     */       }
/*     */       
/* 144 */       if (client.isAllowedToPost())
/*     */       {
/* 146 */         Writer writer = client.postArticle();
/*     */         
/* 148 */         if (writer != null)
/*     */         {
/* 150 */           writer.write(header.toString());
/* 151 */           Util.copyReader(fileReader, writer);
/* 152 */           writer.close();
/* 153 */           client.completePendingCommand();
/*     */         }
/*     */       }
/*     */       
/* 157 */       if (fileReader != null) {
/* 158 */         fileReader.close();
/*     */       }
/*     */       
/* 161 */       client.logout();
/*     */       
/* 163 */       client.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 167 */       e.printStackTrace();
/* 168 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\nntp\PostMessage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */