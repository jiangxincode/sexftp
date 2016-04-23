/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class UrlGet
/*    */ {
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 11 */     System.out.println(doGet("http://localhost:8080/kkfun_data_statistics/dailyReleaseStat/getDailyReleaseStats.action?table=V350_RESULT&monitorTotalReport=true&selfHostView=true", "utf-8"));
/*    */   }
/*    */   
/*    */   public static String doGet(String urlPath, String encode) throws Exception {
/* 15 */     URL url = new URL(urlPath);
/* 16 */     HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
/*    */     
/* 18 */     urlConnection.setDoOutput(true);
/* 19 */     urlConnection.setDoInput(true);
/* 20 */     urlConnection.setUseCaches(false);
/* 21 */     urlConnection.setRequestMethod("GET");
/* 22 */     urlConnection.getOutputStream().flush();
/* 23 */     urlConnection.getOutputStream().close();
/*    */     
/* 25 */     urlConnection.connect();
/* 26 */     java.io.InputStream in = urlConnection.getInputStream();
/*    */     
/*    */ 
/*    */ 
/* 30 */     BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(
/* 31 */       in, encode));
/*    */     
/*    */ 
/* 34 */     StringBuffer data = new StringBuffer();
/* 35 */     String lines; while ((lines = reader.readLine()) != null) { String lines;
/* 36 */       data.append(lines);
/*    */     }
/* 38 */     reader.close();
/*    */     
/*    */ 
/* 41 */     urlConnection.disconnect();
/* 42 */     return data.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\UrlGet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */