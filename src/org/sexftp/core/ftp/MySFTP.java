/*     */ package org.sexftp.core.ftp;
/*     */ 
/*     */ import com.jcraft.jsch.Channel;
/*     */ import com.jcraft.jsch.ChannelSftp;
/*     */ import com.jcraft.jsch.ChannelSftp.LsEntry;
/*     */ import com.jcraft.jsch.JSch;
/*     */ import com.jcraft.jsch.Session;
/*     */ import com.jcraft.jsch.SftpATTRS;
/*     */ import com.jcraft.jsch.SftpException;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.sexftp.core.exceptions.FtpNoSuchFileException;
/*     */ import org.sexftp.core.exceptions.SRuntimeException;
/*     */ import org.sexftp.core.ftp.bean.FtpFile;
/*     */ import sexftp.uils.PluginUtil;
/*     */ import sexftp.views.IFtpStreamMonitor;
/*     */ 
/*     */ public class MySFTP
/*     */   implements XFtp
/*     */ {
/*     */   private ChannelSftp channelSftp;
/*     */   private String host;
/*     */   private int port;
/*     */   private String username;
/*     */   private String password;
/*  37 */   private String encode = "gbk";
/*     */   
/*     */ 
/*     */   public void connect()
/*     */   {
/*  42 */     ChannelSftp sftp = null;
/*     */     try {
/*  44 */       int serverTimeout = PluginUtil.getServerTimeout();
/*  45 */       JSch jsch = new JSch();
/*     */       
/*  47 */       Session sshSession = jsch.getSession(this.username, this.host, this.port);
/*  48 */       sshSession.setPassword(this.password);
/*  49 */       Properties sshConfig = new Properties();
/*  50 */       sshConfig.put("StrictHostKeyChecking", "no");
/*  51 */       sshSession.setTimeout(serverTimeout);
/*  52 */       sshSession.setConfig(sshConfig);
/*  53 */       sshSession.connect();
/*  54 */       Channel channel = sshSession.openChannel("sftp");
/*  55 */       channel.connect();
/*  56 */       sftp = (ChannelSftp)channel;
/*     */     } catch (Exception e) {
/*  58 */       throw new SRuntimeException(String.format("%s \r\n using %s : %s", new Object[] { e.getMessage(), this.username, this.password }), e);
/*     */     }
/*  60 */     this.channelSftp = sftp;
/*     */   }
/*     */   
/*     */   public void prepareConnect(String host, int port, String username, String password, String encode)
/*     */   {
/*  65 */     this.host = host;
/*  66 */     this.port = port;
/*  67 */     this.password = password;
/*  68 */     this.username = username;
/*  69 */     this.encode = (encode != null ? encode : "gbk");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */   {
/*  77 */     this.channelSftp.disconnect();
/*  78 */     this.channelSftp.exit();
/*     */   }
/*     */   
/*     */   public void cd(String directory)
/*     */   {
/*  83 */     ChannelSftp sftp = this.channelSftp;
/*     */     try {
/*  85 */       sftp.cd(directory);
/*     */     } catch (SftpException e) {
/*  87 */       if ((e.getMessage() != null) && (e.getMessage().toLowerCase().startsWith("No such file".toLowerCase())))
/*     */       {
/*  89 */         throw new FtpNoSuchFileException(e.toString());
/*     */       }
/*  91 */       if (e.id == 2)
/*     */       {
/*  93 */         throw new FtpNoSuchFileException(e.toString());
/*     */       }
/*     */       
/*     */ 
/*  97 */       throw new RuntimeException(directory + e.getMessage(), e);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 101 */       throw new RuntimeException(directory + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cdOrMakeIfNotExists(String directory)
/*     */   {
/* 107 */     ChannelSftp sftp = this.channelSftp;
/*     */     try {
/* 109 */       sftp.cd(directory);
/*     */     } catch (Exception localException1) {
/*     */       try {
/* 112 */         mkdirs(directory);
/*     */       } catch (Exception e2) {
/* 114 */         throw new RuntimeException(directory, e2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void mkdirs(String directory) throws Exception
/*     */   {
/* 121 */     ChannelSftp sftp = this.channelSftp;
/* 122 */     String[] dirs = directory.split("/");
/* 123 */     if (directory.startsWith("/"))
/*     */     {
/* 125 */       sftp.cd("/"); }
/*     */     String[] arrayOfString1;
/* 127 */     int j = (arrayOfString1 = dirs).length; for (int i = 0; i < j; i++) { String dir = arrayOfString1[i];
/* 128 */       if (dir.length() > 0)
/*     */       {
/*     */         try
/*     */         {
/* 132 */           sftp.cd(dir);
/*     */         } catch (Exception localException1) {
/*     */           try {
/* 135 */             sftp.mkdir(dir);
/* 136 */             sftp.cd(dir);
/*     */           } catch (Exception e1) {
/* 138 */             throw new SRuntimeException("Make directory " + directory + " Failed! (" + dir + ")", e1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void upload(String uploadFile, final IFtpStreamMonitor monitor)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 108	org/sexftp/core/ftp/MySFTP:channelSftp	Lcom/jcraft/jsch/ChannelSftp;
/*     */     //   4: astore_3
/*     */     //   5: new 201	java/io/File
/*     */     //   8: dup
/*     */     //   9: aload_1
/*     */     //   10: invokespecial 203	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   13: astore 4
/*     */     //   15: aconst_null
/*     */     //   16: astore 5
/*     */     //   18: new 204	java/io/FileInputStream
/*     */     //   21: dup
/*     */     //   22: aload 4
/*     */     //   24: invokespecial 206	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   27: astore 5
/*     */     //   29: aload 4
/*     */     //   31: invokevirtual 209	java/io/File:length	()J
/*     */     //   34: lstore 6
/*     */     //   36: aload 5
/*     */     //   38: invokevirtual 212	java/io/FileInputStream:close	()V
/*     */     //   41: new 215	org/sexftp/core/ftp/MySFTP$1
/*     */     //   44: dup
/*     */     //   45: aload_0
/*     */     //   46: aload 4
/*     */     //   48: aload_2
/*     */     //   49: lload 6
/*     */     //   51: invokespecial 217	org/sexftp/core/ftp/MySFTP$1:<init>	(Lorg/sexftp/core/ftp/MySFTP;Ljava/io/File;Lsexftp/views/IFtpStreamMonitor;J)V
/*     */     //   54: astore 8
/*     */     //   56: aload 8
/*     */     //   58: astore 5
/*     */     //   60: aload_3
/*     */     //   61: aload 8
/*     */     //   63: aload 4
/*     */     //   65: invokevirtual 220	java/io/File:getName	()Ljava/lang/String;
/*     */     //   68: invokevirtual 223	com/jcraft/jsch/ChannelSftp:put	(Ljava/io/InputStream;Ljava/lang/String;)V
/*     */     //   71: goto +54 -> 125
/*     */     //   74: astore 6
/*     */     //   76: aload 6
/*     */     //   78: athrow
/*     */     //   79: astore 6
/*     */     //   81: aload 6
/*     */     //   83: invokevirtual 226	java/lang/Exception:getCause	()Ljava/lang/Throwable;
/*     */     //   86: instanceof 230
/*     */     //   89: ifeq +12 -> 101
/*     */     //   92: aload 6
/*     */     //   94: invokevirtual 226	java/lang/Exception:getCause	()Ljava/lang/Throwable;
/*     */     //   97: checkcast 230	org/sexftp/core/exceptions/AbortException
/*     */     //   100: athrow
/*     */     //   101: new 154	java/lang/RuntimeException
/*     */     //   104: dup
/*     */     //   105: aload 6
/*     */     //   107: invokespecial 232	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   110: athrow
/*     */     //   111: astore 9
/*     */     //   113: aload 5
/*     */     //   115: invokevirtual 212	java/io/FileInputStream:close	()V
/*     */     //   118: goto +4 -> 122
/*     */     //   121: pop
/*     */     //   122: aload 9
/*     */     //   124: athrow
/*     */     //   125: aload 5
/*     */     //   127: invokevirtual 212	java/io/FileInputStream:close	()V
/*     */     //   130: goto +4 -> 134
/*     */     //   133: pop
/*     */     //   134: aload_2
/*     */     //   135: new 156	java/lang/StringBuilder
/*     */     //   138: dup
/*     */     //   139: ldc -21
/*     */     //   141: invokespecial 162	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   144: new 201	java/io/File
/*     */     //   147: dup
/*     */     //   148: aload_1
/*     */     //   149: invokespecial 203	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   152: invokevirtual 220	java/io/File:getName	()Ljava/lang/String;
/*     */     //   155: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   158: invokevirtual 167	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   161: invokeinterface 237 2 0
/*     */     //   166: return
/*     */     // Line number table:
/*     */     //   Java source line #150	-> byte code offset #0
/*     */     //   Java source line #151	-> byte code offset #5
/*     */     //   Java source line #152	-> byte code offset #15
/*     */     //   Java source line #154	-> byte code offset #18
/*     */     //   Java source line #155	-> byte code offset #29
/*     */     //   Java source line #156	-> byte code offset #36
/*     */     //   Java source line #157	-> byte code offset #41
/*     */     //   Java source line #182	-> byte code offset #56
/*     */     //   Java source line #183	-> byte code offset #60
/*     */     //   Java source line #185	-> byte code offset #74
/*     */     //   Java source line #186	-> byte code offset #76
/*     */     //   Java source line #188	-> byte code offset #79
/*     */     //   Java source line #189	-> byte code offset #81
/*     */     //   Java source line #191	-> byte code offset #92
/*     */     //   Java source line #193	-> byte code offset #101
/*     */     //   Java source line #195	-> byte code offset #111
/*     */     //   Java source line #197	-> byte code offset #113
/*     */     //   Java source line #198	-> byte code offset #121
/*     */     //   Java source line #200	-> byte code offset #122
/*     */     //   Java source line #197	-> byte code offset #125
/*     */     //   Java source line #198	-> byte code offset #133
/*     */     //   Java source line #201	-> byte code offset #134
/*     */     //   Java source line #203	-> byte code offset #166
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	167	0	this	MySFTP
/*     */     //   0	167	1	uploadFile	String
/*     */     //   0	167	2	monitor	IFtpStreamMonitor
/*     */     //   4	57	3	sftp	ChannelSftp
/*     */     //   13	51	4	file	File
/*     */     //   16	110	5	useIns	FileInputStream
/*     */     //   34	16	6	avili	long
/*     */     //   74	3	6	e	org.sexftp.core.exceptions.AbortException
/*     */     //   79	27	6	e	Exception
/*     */     //   54	8	8	ins	FileInputStream
/*     */     //   111	12	9	localObject	Object
/*     */     //   121	1	11	localException1	Exception
/*     */     //   133	1	12	localException2	Exception
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   18	71	74	org/sexftp/core/exceptions/AbortException
/*     */     //   18	71	79	java/lang/Exception
/*     */     //   18	111	111	finally
/*     */     //   113	118	121	java/lang/Exception
/*     */     //   125	130	133	java/lang/Exception
/*     */   }
/*     */   
/*     */   public void download(String downloadFile, String saveFile, final IFtpStreamMonitor monitor)
/*     */   {
/* 211 */     ChannelSftp sftp = this.channelSftp;
/*     */     try {
/* 213 */       File file = new File(saveFile);
/* 214 */       String exists = file.exists() ? " (overwrite exists:" + file.getName() + ")" : "";
/* 215 */       FileOutputStream fos = new FileOutputStream(file) {
/* 216 */         int t = 0;
/*     */         
/*     */         public void write(byte[] b, int off, int len) throws IOException
/*     */         {
/* 220 */           if (len >= 0)
/*     */           {
/* 222 */             this.t += len;
/* 223 */             monitor.printStreamString(null, this.t, 0L, "");
/*     */           }
/* 225 */           super.write(b, off, len);
/*     */         }
/* 227 */       };
/* 228 */       sftp.get(downloadFile, fos);
/* 229 */       monitor.printSimple("      download success :" + new File(downloadFile).getName() + exists);
/*     */     } catch (Exception e) {
/* 231 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete(String deleteFile)
/*     */   {
/* 240 */     ChannelSftp sftp = this.channelSftp;
/*     */     try {
/* 242 */       sftp.rm(deleteFile);
/*     */     } catch (Exception e) {
/* 244 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FtpFile> listFiles()
/*     */   {
/* 255 */     List<FtpFile> listfiles = new ArrayList();
/* 256 */     ChannelSftp sftp = this.channelSftp;
/*     */     try
/*     */     {
/* 259 */       Vector ls = sftp.ls(sftp.pwd());
/* 260 */       for (Object f : ls)
/*     */       {
/* 262 */         ChannelSftp.LsEntry lsentry = (ChannelSftp.LsEntry)f;
/* 263 */         if ((!lsentry.getFilename().equals(".")) && (!lsentry.getFilename().equals(".."))) {
/* 264 */           StringTokenizer s = new StringTokenizer(lsentry.getLongname(), "\t ");
/* 265 */           boolean isFolder = false;
/* 266 */           while (s.hasMoreElements())
/*     */           {
/* 268 */             String i = (String)s.nextElement();
/*     */             try {
/* 270 */               Integer valueOf = Integer.valueOf(i);
/* 271 */               isFolder = valueOf.intValue() != 1;
/*     */             }
/*     */             catch (NumberFormatException localNumberFormatException) {}
/*     */           }
/*     */           
/*     */ 
/* 277 */           Calendar instance = Calendar.getInstance();
/*     */           
/* 279 */           instance.setTime(new Date(lsentry.getAttrs().getMTime() * 1000L));
/* 280 */           FtpFile file = new FtpFile(lsentry.getFilename(), isFolder, lsentry.getAttrs().getSize(), instance);
/* 281 */           listfiles.add(file);
/*     */         } }
/* 283 */       return listfiles;
/*     */     } catch (SftpException e) {
/* 285 */       throw new RuntimeException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 314 */     String[] arrayOfString = args;int j = args.length; for (int i = 0; i < j; i++) { String arg = arrayOfString[i];
/* 315 */       System.out.println(arg);
/*     */     }
/*     */     
/* 318 */     XFtp sf = new MySFTP();
/* 319 */     String host = args[1];
/* 320 */     String port = args[2];
/* 321 */     String username = args[3];
/* 322 */     String password = args[4];
/* 323 */     String directory = args[5];
/* 324 */     args[0];
/* 325 */     sf.prepareConnect(host, new Integer(port).intValue(), username, password, null);
/* 326 */     sf.cdOrMakeIfNotExists(directory);
/*     */     try
/*     */     {
/* 329 */       sf.disconnect();
/* 330 */       System.exit(0);
/*     */     } catch (Exception e) {
/* 332 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/* 336 */   public boolean isConnect() { return (this.channelSftp.isConnected()) && (!this.channelSftp.isClosed()) && (!this.channelSftp.isEOF()); }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\MySFTP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */