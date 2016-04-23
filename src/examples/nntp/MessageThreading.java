/*    */ package examples.nntp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.net.SocketException;
/*    */ import org.apache.commons.net.PrintCommandListener;
/*    */ import org.apache.commons.net.nntp.Article;
/*    */ import org.apache.commons.net.nntp.NNTPClient;
/*    */ import org.apache.commons.net.nntp.NewsgroupInfo;
/*    */ import org.apache.commons.net.nntp.Threader;
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
/*    */ public class MessageThreading
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws SocketException, IOException
/*    */   {
/* 37 */     if ((args.length != 2) && (args.length != 4)) {
/* 38 */       System.out.println("Usage: MessageThreading <hostname> <groupname> [<user> <password>]");
/* 39 */       return;
/*    */     }
/*    */     
/* 42 */     String hostname = args[0];
/* 43 */     String newsgroup = args[1];
/*    */     
/* 45 */     NNTPClient client = new NNTPClient();
/* 46 */     client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
/* 47 */     client.connect(hostname);
/*    */     
/* 49 */     if (args.length == 4) {
/* 50 */       String user = args[2];
/* 51 */       String password = args[3];
/* 52 */       if (!client.authenticate(user, password)) {
/* 53 */         System.out.println("Authentication failed for user " + user + "!");
/* 54 */         System.exit(1);
/*    */       }
/*    */     }
/*    */     
/* 58 */     String[] fmt = client.listOverviewFmt();
/* 59 */     if (fmt != null) {
/* 60 */       System.out.println("LIST OVERVIEW.FMT:");
/* 61 */       String[] arrayOfString1; int j = (arrayOfString1 = fmt).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/* 62 */         System.out.println(s);
/*    */       }
/*    */     } else {
/* 65 */       System.out.println("Failed to get OVERVIEW.FMT");
/*    */     }
/* 67 */     NewsgroupInfo group = new NewsgroupInfo();
/* 68 */     client.selectNewsgroup(newsgroup, group);
/*    */     
/* 70 */     long lowArticleNumber = group.getFirstArticleLong();
/* 71 */     long highArticleNumber = lowArticleNumber + 5000L;
/*    */     
/* 73 */     System.out.println("Retrieving articles between [" + lowArticleNumber + "] and [" + highArticleNumber + "]");
/* 74 */     Iterable<Article> articles = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
/*    */     
/* 76 */     System.out.println("Building message thread tree...");
/* 77 */     Threader threader = new Threader();
/* 78 */     Article root = (Article)threader.thread(articles);
/*    */     
/* 80 */     Article.printThread(root, 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\nntp\MessageThreading.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */