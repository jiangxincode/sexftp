/*     */ package examples.telnet;
/*     */ 
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.net.telnet.EchoOptionHandler;
/*     */ import org.apache.commons.net.telnet.InvalidTelnetOptionException;
/*     */ import org.apache.commons.net.telnet.SimpleOptionHandler;
/*     */ import org.apache.commons.net.telnet.SuppressGAOptionHandler;
/*     */ import org.apache.commons.net.telnet.TelnetClient;
/*     */ import org.apache.commons.net.telnet.TelnetNotificationHandler;
/*     */ import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
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
/*     */ public class TelnetClientExample
/*     */   implements Runnable, TelnetNotificationHandler
/*     */ {
/*  52 */   static TelnetClient tc = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*  59 */     FileOutputStream fout = null;
/*     */     
/*  61 */     if (args.length < 1)
/*     */     {
/*  63 */       System.err.println("Usage: TelnetClientExample1 <remote-ip> [<remote-port>]");
/*  64 */       System.exit(1);
/*     */     }
/*     */     
/*  67 */     String remoteip = args[0];
/*     */     
/*     */     int remoteport;
/*     */     int remoteport;
/*  71 */     if (args.length > 1)
/*     */     {
/*  73 */       remoteport = new Integer(args[1]).intValue();
/*     */     }
/*     */     else
/*     */     {
/*  77 */       remoteport = 23;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  82 */       fout = new FileOutputStream("spy.log", true);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  86 */       System.err.println(
/*  87 */         "Exception while opening the spy file: " + 
/*  88 */         e.getMessage());
/*     */     }
/*     */     
/*  91 */     tc = new TelnetClient();
/*     */     
/*  93 */     TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
/*  94 */     EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
/*  95 */     SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
/*     */     
/*     */     try
/*     */     {
/*  99 */       tc.addOptionHandler(ttopt);
/* 100 */       tc.addOptionHandler(echoopt);
/* 101 */       tc.addOptionHandler(gaopt);
/*     */     }
/*     */     catch (InvalidTelnetOptionException e)
/*     */     {
/* 105 */       System.err.println("Error registering option handlers: " + e.getMessage());
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/* 110 */       boolean end_loop = false;
/*     */       try
/*     */       {
/* 113 */         tc.connect(remoteip, remoteport);
/*     */         
/*     */ 
/* 116 */         Thread reader = new Thread(new TelnetClientExample());
/* 117 */         tc.registerNotifHandler(new TelnetClientExample());
/* 118 */         System.out.println("TelnetClientExample");
/* 119 */         System.out.println("Type AYT to send an AYT telnet command");
/* 120 */         System.out.println("Type OPT to print a report of status of options (0-24)");
/* 121 */         System.out.println("Type REGISTER to register a new SimpleOptionHandler");
/* 122 */         System.out.println("Type UNREGISTER to unregister an OptionHandler");
/* 123 */         System.out.println("Type SPY to register the spy (connect to port 3333 to spy)");
/* 124 */         System.out.println("Type UNSPY to stop spying the connection");
/*     */         
/* 126 */         reader.start();
/* 127 */         OutputStream outstr = tc.getOutputStream();
/*     */         
/* 129 */         byte[] buff = new byte['Ѐ'];
/* 130 */         int ret_read = 0;
/*     */         
/*     */         do
/*     */         {
/*     */           try
/*     */           {
/* 136 */             ret_read = System.in.read(buff);
/* 137 */             if (ret_read > 0)
/*     */             {
/* 139 */               if (new String(buff, 0, ret_read).startsWith("AYT"))
/*     */               {
/*     */                 try
/*     */                 {
/* 143 */                   System.out.println("Sending AYT");
/*     */                   
/* 145 */                   System.out.println("AYT response:" + tc.sendAYT(5000L));
/*     */                 }
/*     */                 catch (IOException e)
/*     */                 {
/* 149 */                   System.err.println("Exception waiting AYT response: " + e.getMessage());
/*     */                 }
/*     */               }
/* 152 */               else if (new String(buff, 0, ret_read).startsWith("OPT"))
/*     */               {
/* 154 */                 System.out.println("Status of options:");
/* 155 */                 for (int ii = 0; ii < 25; ii++) {
/* 156 */                   System.out.println("Local Option " + ii + ":" + tc.getLocalOptionState(ii) + " Remote Option " + ii + ":" + tc.getRemoteOptionState(ii));
/*     */                 }
/* 158 */               } else if (new String(buff, 0, ret_read).startsWith("REGISTER"))
/*     */               {
/* 160 */                 StringTokenizer st = new StringTokenizer(new String(buff));
/*     */                 try
/*     */                 {
/* 163 */                   st.nextToken();
/* 164 */                   int opcode = Integer.parseInt(st.nextToken());
/* 165 */                   boolean initlocal = Boolean.parseBoolean(st.nextToken());
/* 166 */                   boolean initremote = Boolean.parseBoolean(st.nextToken());
/* 167 */                   boolean acceptlocal = Boolean.parseBoolean(st.nextToken());
/* 168 */                   boolean acceptremote = Boolean.parseBoolean(st.nextToken());
/* 169 */                   SimpleOptionHandler opthand = new SimpleOptionHandler(opcode, initlocal, initremote, 
/* 170 */                     acceptlocal, acceptremote);
/* 171 */                   tc.addOptionHandler(opthand);
/*     */                 }
/*     */                 catch (Exception e)
/*     */                 {
/* 175 */                   if ((e instanceof InvalidTelnetOptionException))
/*     */                   {
/* 177 */                     System.err.println("Error registering option: " + e.getMessage());
/*     */                   }
/*     */                   else
/*     */                   {
/* 181 */                     System.err.println("Invalid REGISTER command.");
/* 182 */                     System.err.println("Use REGISTER optcode initlocal initremote acceptlocal acceptremote");
/* 183 */                     System.err.println("(optcode is an integer.)");
/* 184 */                     System.err.println("(initlocal, initremote, acceptlocal, acceptremote are boolean)");
/*     */                   }
/*     */                 }
/*     */               }
/* 188 */               else if (new String(buff, 0, ret_read).startsWith("UNREGISTER"))
/*     */               {
/* 190 */                 StringTokenizer st = new StringTokenizer(new String(buff));
/*     */                 try
/*     */                 {
/* 193 */                   st.nextToken();
/* 194 */                   int opcode = new Integer(st.nextToken()).intValue();
/* 195 */                   tc.deleteOptionHandler(opcode);
/*     */                 }
/*     */                 catch (Exception e)
/*     */                 {
/* 199 */                   if ((e instanceof InvalidTelnetOptionException))
/*     */                   {
/* 201 */                     System.err.println("Error unregistering option: " + e.getMessage());
/*     */                   }
/*     */                   else
/*     */                   {
/* 205 */                     System.err.println("Invalid UNREGISTER command.");
/* 206 */                     System.err.println("Use UNREGISTER optcode");
/* 207 */                     System.err.println("(optcode is an integer)");
/*     */                   }
/*     */                 }
/*     */               }
/* 211 */               else if (new String(buff, 0, ret_read).startsWith("SPY"))
/*     */               {
/* 213 */                 tc.registerSpyStream(fout);
/*     */               }
/* 215 */               else if (new String(buff, 0, ret_read).startsWith("UNSPY"))
/*     */               {
/* 217 */                 tc.stopSpyStream();
/*     */               }
/*     */               else
/*     */               {
/*     */                 try
/*     */                 {
/* 223 */                   outstr.write(buff, 0, ret_read);
/* 224 */                   outstr.flush();
/*     */                 }
/*     */                 catch (IOException localIOException1)
/*     */                 {
/* 228 */                   end_loop = true;
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */             if (ret_read <= 0) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           catch (IOException e)
/*     */           {
/* 235 */             System.err.println("Exception while reading keyboard:" + e.getMessage());
/* 236 */             end_loop = true;
/*     */           }
/* 132 */         } while (!
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
/* 239 */           end_loop);
/*     */         
/*     */         try
/*     */         {
/* 243 */           tc.disconnect();
/*     */         }
/*     */         catch (IOException e) {}
/*     */         
/* 247 */         System.err.println("Exception while connecting:" + e.getMessage());
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 252 */         System.err.println("Exception while connecting:" + e.getMessage());
/* 253 */         System.exit(1);
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public void receivedNegotiation(int negotiation_code, int option_code)
/*     */   {
/* 271 */     String command = null;
/* 272 */     if (negotiation_code == 1)
/*     */     {
/* 274 */       command = "DO";
/*     */     }
/* 276 */     else if (negotiation_code == 2)
/*     */     {
/* 278 */       command = "DONT";
/*     */     }
/* 280 */     else if (negotiation_code == 3)
/*     */     {
/* 282 */       command = "WILL";
/*     */     }
/* 284 */     else if (negotiation_code == 4)
/*     */     {
/* 286 */       command = "WONT";
/*     */     }
/* 288 */     System.out.println("Received " + command + " for option code " + option_code);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 298 */     InputStream instr = tc.getInputStream();
/*     */     
/*     */     try
/*     */     {
/* 302 */       byte[] buff = new byte['Ѐ'];
/* 303 */       int ret_read = 0;
/*     */       
/*     */       do
/*     */       {
/* 307 */         ret_read = instr.read(buff);
/* 308 */         if (ret_read > 0)
/*     */         {
/* 310 */           System.out.print(new String(buff, 0, ret_read));
/*     */         }
/*     */         
/* 313 */       } while (ret_read >= 0);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 317 */       System.err.println("Exception while reading socket:" + e.getMessage());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 322 */       tc.disconnect();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 326 */       System.err.println("Exception while closing telnet:" + e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\telnet\TelnetClientExample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */