/*     */ package examples.ftp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.InetAddress;
/*     */ import org.apache.commons.net.PrintCommandListener;
/*     */ import org.apache.commons.net.ProtocolCommandListener;
/*     */ import org.apache.commons.net.ftp.FTPClient;
/*     */ import org.apache.commons.net.ftp.FTPReply;
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
/*     */ public final class ServerToServerFTP
/*     */ {
/*     */   public static final void main(String[] args)
/*     */   {
/*  48 */     int port1 = 0;int port2 = 0;
/*     */     
/*     */ 
/*     */ 
/*  52 */     if (args.length < 8)
/*     */     {
/*  54 */       System.err.println(
/*  55 */         "Usage: ftp <host1> <user1> <pass1> <file1> <host2> <user2> <pass2> <file2>");
/*     */       
/*  57 */       System.exit(1);
/*     */     }
/*     */     
/*  60 */     String server1 = args[0];
/*  61 */     String[] parts = server1.split(":");
/*  62 */     if (parts.length == 2) {
/*  63 */       server1 = parts[0];
/*  64 */       port1 = Integer.parseInt(parts[1]);
/*     */     }
/*  66 */     String username1 = args[1];
/*  67 */     String password1 = args[2];
/*  68 */     String file1 = args[3];
/*  69 */     String server2 = args[4];
/*  70 */     parts = server2.split(":");
/*  71 */     if (parts.length == 2) {
/*  72 */       server2 = parts[0];
/*  73 */       port2 = Integer.parseInt(parts[1]);
/*     */     }
/*  75 */     String username2 = args[5];
/*  76 */     String password2 = args[6];
/*  77 */     String file2 = args[7];
/*     */     
/*  79 */     ProtocolCommandListener listener = new PrintCommandListener(new PrintWriter(System.out), true);
/*  80 */     FTPClient ftp1 = new FTPClient();
/*  81 */     ftp1.addProtocolCommandListener(listener);
/*  82 */     FTPClient ftp2 = new FTPClient();
/*  83 */     ftp2.addProtocolCommandListener(listener);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  88 */       if (port1 > 0) {
/*  89 */         ftp1.connect(server1, port1);
/*     */       } else {
/*  91 */         ftp1.connect(server1);
/*     */       }
/*  93 */       System.out.println("Connected to " + server1 + ".");
/*     */       
/*  95 */       int reply = ftp1.getReplyCode();
/*     */       
/*  97 */       if (!FTPReply.isPositiveCompletion(reply))
/*     */       {
/*  99 */         ftp1.disconnect();
/* 100 */         System.err.println("FTP server1 refused connection.");
/* 101 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 106 */       if (ftp1.isConnected())
/*     */       {
/*     */         try
/*     */         {
/* 110 */           ftp1.disconnect();
/*     */         }
/*     */         catch (IOException localIOException1) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 117 */       System.err.println("Could not connect to server1.");
/* 118 */       e.printStackTrace();
/* 119 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 125 */       if (port2 > 0) {
/* 126 */         ftp2.connect(server2, port2);
/*     */       } else {
/* 128 */         ftp2.connect(server2);
/*     */       }
/* 130 */       System.out.println("Connected to " + server2 + ".");
/*     */       
/* 132 */       int reply = ftp2.getReplyCode();
/*     */       
/* 134 */       if (!FTPReply.isPositiveCompletion(reply))
/*     */       {
/* 136 */         ftp2.disconnect();
/* 137 */         System.err.println("FTP server2 refused connection.");
/* 138 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 143 */       if (ftp2.isConnected())
/*     */       {
/*     */         try
/*     */         {
/* 147 */           ftp2.disconnect();
/*     */         }
/*     */         catch (IOException localIOException2) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 154 */       System.err.println("Could not connect to server2.");
/* 155 */       e.printStackTrace();
/* 156 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 162 */       if (!ftp1.login(username1, password1))
/*     */       {
/* 164 */         System.err.println("Could not login to " + server1);
/*     */       }
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
/*     */       for (;;)
/*     */       {
/*     */         try
/*     */         {
/* 211 */           if (ftp1.isConnected())
/*     */           {
/* 213 */             ftp1.logout();
/* 214 */             ftp1.disconnect();
/*     */           }
/*     */         }
/*     */         catch (IOException localIOException3) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 224 */           if (!ftp2.isConnected())
/*     */             return;
/* 226 */           ftp2.logout();
/* 227 */           ftp2.disconnect();
/*     */         }
/*     */         catch (IOException localIOException4) {}
/* 168 */         if (!ftp2.login(username2, password2))
/*     */         {
/* 170 */           System.err.println("Could not login to " + server2);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 175 */           ftp2.enterRemotePassiveMode();
/*     */           
/* 177 */           ftp1.enterRemoteActiveMode(InetAddress.getByName(ftp2.getPassiveHost()), 
/* 178 */             ftp2.getPassivePort());
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */           if ((ftp1.remoteRetrieve(file1)) && (ftp2.remoteStoreUnique(file2)))
/*     */           {
/*     */ 
/*     */ 
/* 191 */             ftp1.completePendingCommand();
/* 192 */             ftp2.completePendingCommand(); break;
/*     */           }
/*     */           
/*     */ 
/* 196 */           System.err.println(
/* 197 */             "Couldn't initiate transfer.  Check that filenames are valid.");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 211 */         if (ftp1.isConnected())
/*     */         {
/* 213 */           ftp1.logout();
/* 214 */           ftp1.disconnect();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException9) {}
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 204 */       e.printStackTrace();
/* 205 */       System.exit(1);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 211 */         if (ftp1.isConnected())
/*     */         {
/* 213 */           ftp1.logout();
/* 214 */           ftp1.disconnect();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException7) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 224 */         if (ftp2.isConnected())
/*     */         {
/* 226 */           ftp2.logout();
/* 227 */           ftp2.disconnect();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException8) {}
/*     */     }
/*     */     try
/*     */     {
/* 224 */       if (ftp2.isConnected())
/*     */       {
/* 226 */         ftp2.logout();
/* 227 */         ftp2.disconnect();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException10) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\ftp\ServerToServerFTP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */