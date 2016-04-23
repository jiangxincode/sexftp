/*    */ package examples.nntp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import org.apache.commons.net.PrintCommandListener;
/*    */ import org.apache.commons.net.nntp.Article;
/*    */ import org.apache.commons.net.nntp.NNTPClient;
/*    */ import org.apache.commons.net.nntp.NewsgroupInfo;
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
/*    */ public class ExtendedNNTPOps
/*    */ {
/*    */   NNTPClient client;
/*    */   
/*    */   public ExtendedNNTPOps()
/*    */   {
/* 40 */     this.client = new NNTPClient();
/* 41 */     this.client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
/*    */   }
/*    */   
/*    */   private void demo(String host, String user, String password)
/*    */   {
/*    */     try {
/* 47 */       this.client.connect(host);
/*    */       
/*    */ 
/* 50 */       if ((user != null) && (password != null)) {
/* 51 */         boolean success = this.client.authenticate(user, password);
/* 52 */         if (success) {
/* 53 */           System.out.println("Authentication succeeded");
/*    */         } else {
/* 55 */           System.out.println("Authentication failed, error =" + this.client.getReplyString());
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 60 */       NewsgroupInfo testGroup = new NewsgroupInfo();
/* 61 */       this.client.selectNewsgroup("alt.test", testGroup);
/* 62 */       long lowArticleNumber = testGroup.getFirstArticleLong();
/* 63 */       long highArticleNumber = lowArticleNumber + 100L;
/* 64 */       Iterable<Article> articles = this.client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
/*    */       
/* 66 */       for (Article article : articles) {
/* 67 */         if (article.isDummy()) {
/* 68 */           System.out.println("Could not parse: " + article.getSubject());
/*    */         } else {
/* 70 */           System.out.println(article.getSubject());
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 75 */       NewsgroupInfo[] fanGroups = this.client.listNewsgroups("alt.fan.*");
/* 76 */       for (int i = 0; i < fanGroups.length; i++) {
/* 77 */         System.out.println(fanGroups[i].getNewsgroup());
/*    */       }
/*    */     }
/*    */     catch (IOException e) {
/* 81 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 88 */     int argc = args.length;
/* 89 */     if (argc < 1) {
/* 90 */       System.err.println("usage: ExtendedNNTPOps nntpserver [username password]");
/* 91 */       System.exit(1);
/*    */     }
/*    */     
/* 94 */     ExtendedNNTPOps ops = new ExtendedNNTPOps();
/* 95 */     ops.demo(args[0], argc >= 3 ? args[1] : null, argc >= 3 ? args[2] : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\nntp\ExtendedNNTPOps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */