/*     */ package sexftp.uils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetIP
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  78 */     boolean bHasNoArgs = false;
/*  79 */     if (args.length <= 0) { bHasNoArgs = true;
/*     */     }
/*  81 */     StringBuffer sbFileContent = new StringBuffer();
/*  82 */     boolean bGetSuccess = true;
/*     */     try
/*     */     {
/*  85 */       InetAddress host = InetAddress.getLocalHost();
/*     */       
/*  87 */       String hostName = host.getHostName();
/*  88 */       String hostAddr = host.getHostAddress();
/*  89 */       host.getCanonicalHostName();
/*     */       
/*  91 */       Date da = new Date();
/*  92 */       String osname = System.getProperty("os.name");
/*  93 */       String osversion = System.getProperty("os.version");
/*  94 */       String username = System.getProperty("user.name");
/*  95 */       String userhome = System.getProperty("user.home");
/*  96 */       String userdir = System.getProperty("user.dir");
/*     */       
/*  98 */       if (bHasNoArgs) {
/*  99 */         System.out.println("hostName is:" + hostName);
/* 100 */         System.out.println("hostAddr is:" + hostAddr);
/*     */         
/* 102 */         System.out.println("Current Date is:" + da.toString());
/* 103 */         System.out.println("osname is:" + osname);
/* 104 */         System.out.println("osversion is:" + osversion);
/* 105 */         System.out.println("username is:" + username);
/* 106 */         System.out.println("userhome is:" + userhome);
/* 107 */         System.out.println("userdir is:" + userdir);
/*     */       }
/*     */       else {
/* 110 */         sbFileContent.append("hostName is:" + hostName + "\n");
/* 111 */         sbFileContent.append("hostAddr is:" + hostAddr + "\n");
/*     */         
/* 113 */         sbFileContent.append("Current Date is:" + da.toString() + "\n");
/* 114 */         sbFileContent.append("osname is:" + osname + "\n");
/* 115 */         sbFileContent.append("osversion is:" + osversion + "\n");
/* 116 */         sbFileContent.append("username is:" + username + "\n");
/* 117 */         sbFileContent.append("userhome is:" + userhome + "\n");
/* 118 */         sbFileContent.append("userdir is:" + userdir + "\n");
/*     */       }
/*     */       
/* 121 */       StringBuffer url = new StringBuffer();
/* 122 */       if ((bHasNoArgs) || (args[0].equals(null)) || (args[0].equals(""))) {
/* 123 */         url.append("http://www.cz88.net/ip/viewip778.aspx");
/*     */       }
/*     */       else
/* 126 */         url.append(args[0]);
/* 127 */       StringBuffer strForeignIP = new StringBuffer("strForeignIPUnkown");
/* 128 */       StringBuffer strLocation = new StringBuffer("strLocationUnkown");
/*     */       
/*     */ 
/* 131 */       if (getWebIp(url.toString(), strForeignIP, strLocation)) {
/* 132 */         if (bHasNoArgs) {
/* 133 */           System.out.println("Foreign IP is:" + strForeignIP);
/* 134 */           System.out.println("Location is:" + strLocation);
/*     */         }
/*     */         else {
/* 137 */           sbFileContent.append("Foreign IP is:" + strForeignIP + "\n");
/* 138 */           sbFileContent.append("Location is:" + strLocation + "\n");
/*     */         }
/*     */         
/*     */       }
/* 142 */       else if (bHasNoArgs) {
/* 143 */         System.out.println("Failed to connect:" + url);
/*     */       }
/*     */       else {
/* 146 */         bGetSuccess = false;
/* 147 */         sbFileContent.append("Failed to connect:" + url + "\n");
/*     */       }
/*     */       
/*     */     }
/*     */     catch (UnknownHostException e)
/*     */     {
/* 153 */       if (bHasNoArgs) {
/* 154 */         e.printStackTrace();
/*     */       }
/*     */       else {
/* 157 */         bGetSuccess = false;
/* 158 */         sbFileContent.append(e.getStackTrace() + "\n");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 163 */     if (bGetSuccess) {
/* 164 */       sbFileContent.insert(0, "sucess\n");
/*     */     } else {
/* 166 */       sbFileContent.insert(0, "fail\n");
/*     */     }
/* 168 */     if (!bHasNoArgs) { write2file(sbFileContent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean getWebIp(String strUrl, StringBuffer strForeignIP, StringBuffer strLocation)
/*     */   {
/*     */     try
/*     */     {
/* 177 */       URL url = new URL(strUrl);
/*     */       
/* 179 */       BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
/*     */       
/* 181 */       String s = "";
/* 182 */       StringBuffer sb = new StringBuffer("");
/* 183 */       while ((s = br.readLine()) != null) {
/* 184 */         sb.append(s + "\r\n");
/*     */       }
/* 186 */       br.close();
/*     */       
/* 188 */       String webContent = "";
/* 189 */       webContent = sb.toString();
/*     */       
/* 191 */       if ((webContent.equals(null)) || (webContent.equals(""))) { return false;
/*     */       }
/*     */       
/*     */ 
/* 195 */       String flagofForeignIPString = "IPMessage";
/* 196 */       int startIP = webContent.indexOf(flagofForeignIPString) + flagofForeignIPString.length() + 2;
/* 197 */       int endIP = webContent.indexOf("</span>", startIP);
/* 198 */       strForeignIP.delete(0, webContent.length());
/* 199 */       strForeignIP.append(webContent.substring(startIP, endIP));
/*     */       
/* 201 */       String flagofLocationString = "AddrMessage";
/* 202 */       int startLoc = webContent.indexOf(flagofLocationString) + flagofLocationString.length() + 2;
/* 203 */       int endLoc = webContent.indexOf("</span>", startLoc);
/* 204 */       strLocation.delete(0, webContent.length());
/* 205 */       strLocation.append(webContent.substring(startLoc, endLoc));
/*     */       
/* 207 */       return true;
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 211 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void write2file(StringBuffer content)
/*     */   {
/* 218 */     if (content.length() <= 0) return;
/*     */     try
/*     */     {
/* 221 */       FileOutputStream fos = new FileOutputStream("GETIP.sys");
/* 222 */       OutputStreamWriter osr = new OutputStreamWriter(fos);
/* 223 */       BufferedWriter bw = new BufferedWriter(osr);
/*     */       try
/*     */       {
/* 226 */         int index = 0;
/* 227 */         while (index >= 0) {
/* 228 */           int preIndex = index;
/* 229 */           index = content.indexOf("\n", preIndex + 2);
/*     */           
/* 231 */           if (index > 0) {
/* 232 */             String str = new String(content.substring(preIndex, index));
/* 233 */             bw.write(str);
/* 234 */             bw.newLine();
/*     */           }
/*     */           else {
/* 237 */             String str = new String(content.substring(preIndex, content.length() - 1));
/* 238 */             bw.write(str);
/* 239 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 249 */         bw.close();
/*     */       }
/*     */       catch (IOException localIOException2) {}
/*     */       return;
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\GetIP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */