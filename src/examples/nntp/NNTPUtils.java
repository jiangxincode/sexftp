/*    */ package examples.nntp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.net.nntp.Article;
/*    */ import org.apache.commons.net.nntp.NNTPClient;
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
/*    */ public class NNTPUtils
/*    */ {
/*    */   public static List<Article> getArticleInfo(NNTPClient client, long lowArticleNumber, long highArticleNumber)
/*    */     throws IOException
/*    */   {
/* 44 */     List<Article> articles = new ArrayList();
/* 45 */     Iterable<Article> arts = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
/* 46 */     for (Article article : arts) {
/* 47 */       articles.add(article);
/*    */     }
/* 49 */     return articles;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\nntp\NNTPUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */