/*     */ package examples.unix;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.net.finger.FingerClient;
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
/*     */ public final class finger
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  41 */     boolean longOutput = false;
/*  42 */     int arg = 0;
/*     */     
/*     */ 
/*  45 */     InetAddress address = null;
/*     */     
/*     */ 
/*  48 */     while ((arg < args.length) && (args[arg].startsWith("-")))
/*     */     {
/*  50 */       if (args[arg].equals("-l")) {
/*  51 */         longOutput = true;
/*     */       }
/*     */       else {
/*  54 */         System.err.println("usage: finger [-l] [[[handle][@<server>]] ...]");
/*  55 */         System.exit(1);
/*     */       }
/*  57 */       arg++;
/*     */     }
/*     */     
/*     */ 
/*  61 */     FingerClient finger = new FingerClient();
/*     */     
/*  63 */     finger.setDefaultTimeout(60000);
/*     */     
/*  65 */     if (arg >= args.length)
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/*  71 */         address = InetAddress.getLocalHost();
/*     */       }
/*     */       catch (UnknownHostException e)
/*     */       {
/*  75 */         System.err.println("Error unknown host: " + e.getMessage());
/*  76 */         System.exit(1);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  81 */         finger.connect(address);
/*  82 */         System.out.print(finger.query(longOutput));
/*  83 */         finger.disconnect();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*  87 */         System.err.println("Error I/O exception: " + e.getMessage());
/*  88 */         System.exit(1);
/*     */       }
/*     */       
/*  91 */       return;
/*     */     }
/*     */     
/*     */ 
/*  95 */     while (arg < args.length)
/*     */     {
/*     */ 
/*  98 */       int index = args[arg].lastIndexOf("@");
/*     */       String handle;
/* 100 */       if (index == -1)
/*     */       {
/* 102 */         String handle = args[arg];
/*     */         try
/*     */         {
/* 105 */           address = InetAddress.getLocalHost();
/*     */         }
/*     */         catch (UnknownHostException e)
/*     */         {
/* 109 */           System.err.println("Error unknown host: " + e.getMessage());
/* 110 */           System.exit(1);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 115 */         handle = args[arg].substring(0, index);
/* 116 */         String host = args[arg].substring(index + 1);
/*     */         
/*     */         try
/*     */         {
/* 120 */           address = InetAddress.getByName(host);
/* 121 */           System.out.println("[" + address.getHostName() + "]");
/*     */         }
/*     */         catch (UnknownHostException e)
/*     */         {
/* 125 */           System.err.println("Error unknown host: " + e.getMessage());
/* 126 */           System.exit(1);
/*     */         }
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 132 */         finger.connect(address);
/* 133 */         System.out.print(finger.query(longOutput, handle));
/* 134 */         finger.disconnect();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 138 */         System.err.println("Error I/O exception: " + e.getMessage());
/* 139 */         System.exit(1);
/*     */       }
/*     */       
/* 142 */       arg++;
/* 143 */       if (arg != args.length) {
/* 144 */         System.out.print("\n");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\finger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */