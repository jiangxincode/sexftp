/*     */ package examples.unix;
/*     */ 
/*     */ import examples.util.IOUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.commons.net.bsd.RExecClient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class rexec
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  48 */     if (args.length != 4)
/*     */     {
/*  50 */       System.err.println(
/*  51 */         "Usage: rexec <hostname> <username> <password> <command>");
/*  52 */       System.exit(1);
/*  53 */       return;
/*     */     }
/*     */     
/*  56 */     RExecClient client = new RExecClient();
/*     */     
/*  58 */     String server = args[0];
/*  59 */     String username = args[1];
/*  60 */     String password = args[2];
/*  61 */     String command = args[3];
/*     */     
/*     */     try
/*     */     {
/*  65 */       client.connect(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  69 */       System.err.println("Could not connect to server.");
/*  70 */       e.printStackTrace();
/*  71 */       System.exit(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  76 */       client.rexec(username, password, command);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */       try
/*     */       {
/*  82 */         client.disconnect();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*  86 */       e.printStackTrace();
/*  87 */       System.err.println("Could not execute command.");
/*  88 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/*  92 */     IOUtil.readWrite(client.getInputStream(), client.getOutputStream(), 
/*  93 */       System.in, System.out);
/*     */     
/*     */     try
/*     */     {
/*  97 */       client.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 101 */       e.printStackTrace();
/* 102 */       System.exit(1);
/*     */     }
/*     */     
/* 105 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\rexec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */