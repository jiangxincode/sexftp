/*     */ package examples.unix;
/*     */ 
/*     */ import examples.util.IOUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.commons.net.bsd.RLoginClient;
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
/*     */ public final class rlogin
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  59 */     if (args.length != 4)
/*     */     {
/*  61 */       System.err.println(
/*  62 */         "Usage: rlogin <hostname> <localuser> <remoteuser> <terminal>");
/*  63 */       System.exit(1);
/*  64 */       return;
/*     */     }
/*     */     
/*  67 */     RLoginClient client = new RLoginClient();
/*     */     
/*  69 */     String server = args[0];
/*  70 */     String localuser = args[1];
/*  71 */     String remoteuser = args[2];
/*  72 */     String terminal = args[3];
/*     */     
/*     */     try
/*     */     {
/*  76 */       client.connect(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  80 */       System.err.println("Could not connect to server.");
/*  81 */       e.printStackTrace();
/*  82 */       System.exit(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  87 */       client.rlogin(localuser, remoteuser, terminal);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */       try
/*     */       {
/*  93 */         client.disconnect();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*  97 */       e.printStackTrace();
/*  98 */       System.err.println("rlogin authentication failed.");
/*  99 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/* 103 */     IOUtil.readWrite(client.getInputStream(), client.getOutputStream(), 
/* 104 */       System.in, System.out);
/*     */     
/*     */     try
/*     */     {
/* 108 */       client.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 112 */       e.printStackTrace();
/* 113 */       System.exit(1);
/*     */     }
/*     */     
/* 116 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\rlogin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */