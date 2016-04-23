/*     */ package examples.ntp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import org.apache.commons.net.ntp.NTPUDPClient;
/*     */ import org.apache.commons.net.ntp.NtpUtils;
/*     */ import org.apache.commons.net.ntp.NtpV3Packet;
/*     */ import org.apache.commons.net.ntp.TimeInfo;
/*     */ import org.apache.commons.net.ntp.TimeStamp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NTPClient
/*     */ {
/*  47 */   private static final NumberFormat numberFormat = new DecimalFormat("0.00");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void processResponse(TimeInfo info)
/*     */   {
/*  55 */     NtpV3Packet message = info.getMessage();
/*  56 */     int stratum = message.getStratum();
/*     */     String refType;
/*  58 */     String refType; if (stratum <= 0) {
/*  59 */       refType = "(Unspecified or Unavailable)"; } else { String refType;
/*  60 */       if (stratum == 1) {
/*  61 */         refType = "(Primary Reference; e.g., GPS)";
/*     */       } else
/*  63 */         refType = "(Secondary Reference; e.g. via NTP or SNTP)";
/*     */     }
/*  65 */     System.out.println(" Stratum: " + stratum + " " + refType);
/*  66 */     int version = message.getVersion();
/*  67 */     int li = message.getLeapIndicator();
/*  68 */     System.out.println(" leap=" + li + ", version=" + 
/*  69 */       version + ", precision=" + message.getPrecision());
/*     */     
/*  71 */     System.out.println(" mode: " + message.getModeName() + " (" + message.getMode() + ")");
/*  72 */     int poll = message.getPoll();
/*     */     
/*  74 */     System.out.println(" poll: " + (poll <= 0 ? 1 : (int)Math.pow(2.0D, poll)) + 
/*  75 */       " seconds" + " (2 ** " + poll + ")");
/*  76 */     double disp = message.getRootDispersionInMillisDouble();
/*  77 */     System.out.println(" rootdelay=" + numberFormat.format(message.getRootDelayInMillisDouble()) + 
/*  78 */       ", rootdispersion(ms): " + numberFormat.format(disp));
/*     */     
/*  80 */     int refId = message.getReferenceId();
/*  81 */     String refAddr = NtpUtils.getHostAddress(refId);
/*  82 */     String refName = null;
/*  83 */     if (refId != 0) {
/*  84 */       if (refAddr.equals("127.127.1.0")) {
/*  85 */         refName = "LOCAL";
/*  86 */       } else if (stratum >= 2)
/*     */       {
/*     */ 
/*     */ 
/*  90 */         if (!refAddr.startsWith("127.127")) {
/*     */           try {
/*  92 */             InetAddress addr = InetAddress.getByName(refAddr);
/*  93 */             String name = addr.getHostName();
/*  94 */             if ((name == null) || (name.equals(refAddr))) break label423;
/*  95 */             refName = name;
/*     */ 
/*     */           }
/*     */           catch (UnknownHostException localUnknownHostException)
/*     */           {
/* 100 */             refName = NtpUtils.getReferenceClock(message);
/*     */           }
/*     */         }
/* 103 */       } else if ((version >= 3) && ((stratum == 0) || (stratum == 1))) {
/* 104 */         refName = NtpUtils.getReferenceClock(message);
/*     */       }
/*     */     }
/*     */     
/*     */     label423:
/* 109 */     if ((refName != null) && (refName.length() > 1))
/* 110 */       refAddr = refAddr + " (" + refName + ")";
/* 111 */     System.out.println(" Reference Identifier:\t" + refAddr);
/*     */     
/* 113 */     TimeStamp refNtpTime = message.getReferenceTimeStamp();
/* 114 */     System.out.println(" Reference Timestamp:\t" + refNtpTime + "  " + refNtpTime.toDateString());
/*     */     
/*     */ 
/* 117 */     TimeStamp origNtpTime = message.getOriginateTimeStamp();
/* 118 */     System.out.println(" Originate Timestamp:\t" + origNtpTime + "  " + origNtpTime.toDateString());
/*     */     
/* 120 */     long destTime = info.getReturnTime();
/*     */     
/* 122 */     TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
/* 123 */     System.out.println(" Receive Timestamp:\t" + rcvNtpTime + "  " + rcvNtpTime.toDateString());
/*     */     
/*     */ 
/* 126 */     TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
/* 127 */     System.out.println(" Transmit Timestamp:\t" + xmitNtpTime + "  " + xmitNtpTime.toDateString());
/*     */     
/*     */ 
/* 130 */     TimeStamp destNtpTime = TimeStamp.getNtpTime(destTime);
/* 131 */     System.out.println(" Destination Timestamp:\t" + destNtpTime + "  " + destNtpTime.toDateString());
/*     */     
/* 133 */     info.computeDetails();
/* 134 */     Long offsetValue = info.getOffset();
/* 135 */     Long delayValue = info.getDelay();
/* 136 */     String delay = delayValue == null ? "N/A" : delayValue.toString();
/* 137 */     String offset = offsetValue == null ? "N/A" : offsetValue.toString();
/*     */     
/* 139 */     System.out.println(" Roundtrip delay(ms)=" + delay + 
/* 140 */       ", clock offset(ms)=" + offset);
/*     */   }
/*     */   
/*     */   public static final void main(String[] args)
/*     */   {
/* 145 */     if (args.length == 0) {
/* 146 */       System.err.println("Usage: NTPClient <hostname-or-address-list>");
/* 147 */       System.exit(1);
/*     */     }
/*     */     
/* 150 */     NTPUDPClient client = new NTPUDPClient();
/*     */     
/* 152 */     client.setDefaultTimeout(10000);
/*     */     try {
/* 154 */       client.open();
/* 155 */       for (int i = 0; i < args.length; i++)
/*     */       {
/* 157 */         System.out.println();
/*     */         try {
/* 159 */           InetAddress hostAddr = InetAddress.getByName(args[i]);
/* 160 */           System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
/* 161 */           TimeInfo info = client.getTime(hostAddr);
/* 162 */           processResponse(info);
/*     */         } catch (IOException ioe) {
/* 164 */           ioe.printStackTrace();
/*     */         }
/*     */       }
/*     */     } catch (SocketException e) {
/* 168 */       e.printStackTrace();
/*     */     }
/*     */     
/* 171 */     client.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\ntp\NTPClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */