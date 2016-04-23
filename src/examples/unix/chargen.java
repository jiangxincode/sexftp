/*     */ package examples.unix;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import org.apache.commons.net.chargen.CharGenTCPClient;
/*     */ import org.apache.commons.net.chargen.CharGenUDPClient;
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
/*     */ public final class chargen
/*     */ {
/*     */   public static final void chargenTCP(String host)
/*     */     throws IOException
/*     */   {
/*  48 */     int lines = 100;
/*     */     
/*  50 */     CharGenTCPClient client = new CharGenTCPClient();
/*     */     
/*     */ 
/*     */ 
/*  54 */     client.setDefaultTimeout(60000);
/*  55 */     client.connect(host);
/*  56 */     BufferedReader chargenInput = 
/*  57 */       new BufferedReader(new InputStreamReader(client.getInputStream()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  62 */     while (lines-- > 0) {
/*     */       String line;
/*  64 */       if ((line = chargenInput.readLine()) == null)
/*     */         break;
/*  66 */       System.out.println(line);
/*     */     }
/*     */     
/*  69 */     client.disconnect();
/*     */   }
/*     */   
/*     */   public static final void chargenUDP(String host) throws IOException
/*     */   {
/*  74 */     int packets = 50;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  79 */     InetAddress address = InetAddress.getByName(host);
/*  80 */     CharGenUDPClient client = new CharGenUDPClient();
/*     */     
/*  82 */     client.open();
/*     */     
/*     */ 
/*  85 */     client.setSoTimeout(5000);
/*     */     
/*  87 */     while (packets-- > 0)
/*     */     {
/*  89 */       client.send(address);
/*     */       
/*     */       try
/*     */       {
/*  93 */         data = client.receive();
/*     */       }
/*     */       catch (SocketException localSocketException)
/*     */       {
/*     */         byte[] data;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 102 */         System.err.println("SocketException: Timed out and dropped packet");
/* 103 */         continue;
/*     */ 
/*     */       }
/*     */       catch (InterruptedIOException localInterruptedIOException)
/*     */       {
/* 108 */         System.err.println(
/* 109 */           "InterruptedIOException: Timed out and dropped packet");
/* 110 */         continue; }
/*     */       byte[] data;
/* 112 */       System.out.write(data);
/* 113 */       System.out.flush();
/*     */     }
/*     */     
/* 116 */     client.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final void main(String[] args)
/*     */   {
/* 123 */     if (args.length == 1)
/*     */     {
/*     */       try
/*     */       {
/* 127 */         chargenTCP(args[0]);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 131 */         e.printStackTrace();
/* 132 */         System.exit(1);
/*     */       }
/*     */     }
/* 135 */     else if ((args.length == 2) && (args[0].equals("-udp")))
/*     */     {
/*     */       try
/*     */       {
/* 139 */         chargenUDP(args[1]);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 143 */         e.printStackTrace();
/* 144 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 149 */       System.err.println("Usage: chargen [-udp] <hostname>");
/* 150 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\chargen.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */