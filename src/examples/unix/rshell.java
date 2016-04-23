/*     */ package examples.unix;
/*     */ 
/*     */ import examples.util.IOUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.commons.net.bsd.RCommandClient;
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
/*     */ public final class rshell
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  52 */     if (args.length != 4)
/*     */     {
/*  54 */       System.err.println(
/*  55 */         "Usage: rshell <hostname> <localuser> <remoteuser> <command>");
/*  56 */       System.exit(1);
/*  57 */       return;
/*     */     }
/*     */     
/*  60 */     RCommandClient client = new RCommandClient();
/*     */     
/*  62 */     String server = args[0];
/*  63 */     String localuser = args[1];
/*  64 */     String remoteuser = args[2];
/*  65 */     String command = args[3];
/*     */     
/*     */     try
/*     */     {
/*  69 */       client.connect(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  73 */       System.err.println("Could not connect to server.");
/*  74 */       e.printStackTrace();
/*  75 */       System.exit(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  80 */       client.rcommand(localuser, remoteuser, command);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */       try
/*     */       {
/*  86 */         client.disconnect();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*  90 */       e.printStackTrace();
/*  91 */       System.err.println("Could not execute command.");
/*  92 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/*  96 */     IOUtil.readWrite(client.getInputStream(), client.getOutputStream(), 
/*  97 */       System.in, System.out);
/*     */     
/*     */     try
/*     */     {
/* 101 */       client.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 105 */       e.printStackTrace();
/* 106 */       System.exit(1);
/*     */     }
/*     */     
/* 109 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\rshell.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */