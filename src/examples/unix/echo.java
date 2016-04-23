/*     */ package examples.unix;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import org.apache.commons.net.echo.EchoTCPClient;
/*     */ import org.apache.commons.net.echo.EchoUDPClient;
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
/*     */ public final class echo
/*     */ {
/*     */   public static final void echoTCP(String host)
/*     */     throws IOException
/*     */   {
/*  48 */     EchoTCPClient client = new EchoTCPClient();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */     client.setDefaultTimeout(60000);
/*  55 */     client.connect(host);
/*  56 */     System.out.println("Connected to " + host + ".");
/*  57 */     BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
/*  58 */     PrintWriter echoOutput = 
/*  59 */       new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
/*  60 */     BufferedReader echoInput = 
/*  61 */       new BufferedReader(new InputStreamReader(client.getInputStream()));
/*     */     String line;
/*  63 */     while ((line = input.readLine()) != null) {
/*     */       String line;
/*  65 */       echoOutput.println(line);
/*  66 */       System.out.println(echoInput.readLine());
/*     */     }
/*     */     
/*  69 */     client.disconnect();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void echoUDP(String host)
/*     */     throws IOException
/*     */   {
/*  81 */     BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
/*  82 */     InetAddress address = InetAddress.getByName(host);
/*  83 */     EchoUDPClient client = new EchoUDPClient();
/*     */     
/*  85 */     client.open();
/*     */     
/*  87 */     client.setSoTimeout(5000);
/*  88 */     System.out.println("Ready to echo to " + host + ".");
/*     */     
/*     */     String line;
/*     */     
/*  92 */     while ((line = input.readLine()) != null) {
/*     */       String line;
/*  94 */       byte[] data = line.getBytes();
/*  95 */       client.send(data, address);
/*  96 */       int count = 0;
/*     */       do
/*     */       {
/*     */         try
/*     */         {
/* 101 */           length = client.receive(data);
/*     */         }
/*     */         catch (SocketException localSocketException)
/*     */         {
/*     */           int length;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 110 */           System.err.println(
/* 111 */             "SocketException: Timed out and dropped packet");
/* 112 */           break;
/*     */ 
/*     */         }
/*     */         catch (InterruptedIOException localInterruptedIOException)
/*     */         {
/* 117 */           System.err.println(
/* 118 */             "InterruptedIOException: Timed out and dropped packet");
/* 119 */           break; }
/*     */         int length;
/* 121 */         System.out.print(new String(data, 0, length));
/* 122 */         count += length;
/*     */       }
/* 124 */       while (count < data.length);
/*     */       
/* 126 */       System.out.println();
/*     */     }
/*     */     
/* 129 */     client.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final void main(String[] args)
/*     */   {
/* 136 */     if (args.length == 1)
/*     */     {
/*     */       try
/*     */       {
/* 140 */         echoTCP(args[0]);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 144 */         e.printStackTrace();
/* 145 */         System.exit(1);
/*     */       }
/*     */     }
/* 148 */     else if ((args.length == 2) && (args[0].equals("-udp")))
/*     */     {
/*     */       try
/*     */       {
/* 152 */         echoUDP(args[1]);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 156 */         e.printStackTrace();
/* 157 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 162 */       System.err.println("Usage: echo [-udp] <hostname>");
/* 163 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\unix\echo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */