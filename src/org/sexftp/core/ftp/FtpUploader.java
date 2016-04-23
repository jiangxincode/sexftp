/*     */ package org.sexftp.core.ftp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.desy.xbean.XbeanUtil;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtpUploader
/*     */ {
/*  38 */   public static Map<String, XFtp> FTP_MAP = new HashMap();
/*     */   
/*  40 */   static { FTP_MAP.put("ftp", new BACKMyFtp());
/*  41 */     FTP_MAP.put("sftp", new MySFTP());
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception
/*     */   {
/*  46 */     System.out.println(Arrays.toString(args));
/*  47 */     String workBaseDir = args[0];
/*     */     
/*  49 */     String coffilepath = null;
/*  50 */     if ((args.length == 2) && (args[1].trim().length() > 0))
/*     */     {
/*  52 */       coffilepath = args[1];
/*  53 */       System.out.println("chosed:" + new File(coffilepath).getName());
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  58 */       File FileCurDir = new File(".");
/*  59 */       File[] confFiles = FileCurDir.listFiles(new FilenameFilter() {
/*     */         public boolean accept(File dir, String name) {
/*  61 */           if (name.endsWith(".xml"))
/*  62 */             return true;
/*  63 */           return false;
/*     */         }
/*  65 */       });
/*  66 */       Arrays.sort(confFiles);
/*  67 */       String[] confNames = new String[confFiles.length];
/*  68 */       for (int i = 0; i < confFiles.length; i++) {
/*  69 */         confNames[i] = (i + 1 + " - " + confFiles[i].getName());
/*     */       }
/*  71 */       System.out.println("chose conf files indexs:");
/*  72 */       String index = readStringFromSystemin(null, confNames);
/*  73 */       System.out.println("chosed:" + confFiles[(Integer.parseInt(index) - 1)].getName());
/*  74 */       coffilepath = confFiles[(Integer.parseInt(index) - 1)].getAbsolutePath();
/*     */     }
/*  76 */     String wkdir = workBaseDir + "/" + new File(coffilepath).getName();
/*     */     
/*  78 */     File wkdirFile = new File(wkdir);
/*  79 */     if (!wkdirFile.exists()) { wkdirFile.mkdirs();
/*     */     }
/*  81 */     String xmlconf = FileUtil.getTextFromFile(coffilepath, "utf-8");
/*  82 */     FtpConf conf = (FtpConf)XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
/*     */     
/*     */ 
/*  85 */     List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
/*  86 */     for (FtpUploadConf ftpUploadConf : conf.getFtpUploadConfList())
/*     */     {
/*  88 */       expandFtpUploadConfList.addAll(expandFtpUploadConf(ftpUploadConf));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     File lastMoMap = new File(wkdir + "/lastModMap.d");
/*     */     
/*     */ 
/*  96 */     if (!lastMoMap.exists())
/*     */     {
/*  98 */       System.out.println("Not Format,Formating...");
/*  99 */       formater(expandFtpUploadConfList, wkdir);
/* 100 */       System.exit(0);
/*     */     }
/*     */     
/*     */ 
/* 104 */     ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastMoMap));
/* 105 */     Map<String, String> lastModMap = (Map)ois.readObject();
/*     */     
/*     */ 
/* 108 */     String chosedUploadCfPath = wkdir + "/chosedUploadCf.d";
/* 109 */     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chosedUploadCfPath)));
/* 110 */     System.out.println("Uplad Queues:");
/* 111 */     boolean hasData = false;
/* 112 */     for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
/* 113 */       String path = expandFtpUploadConf.getClientPath();
/* 114 */       if ((!lastModMap.containsKey(path)) || (!((String)lastModMap.get(path)).equals(expandFtpUploadConf.getFileMd5())))
/*     */       {
/* 116 */         System.out.println(expandFtpUploadConf.toSimpleString());
/* 117 */         bw.write(expandFtpUploadConf + "\r\n");
/* 118 */         hasData = true;
/*     */       }
/*     */     }
/* 121 */     bw.close();
/* 122 */     if (!hasData)
/*     */     {
/* 124 */       System.out.println("No files modifed after last format!");
/* 125 */       System.exit(0);
/*     */     }
/*     */     
/*     */ 
/* 129 */     System.out.println("");
/* 130 */     String option = readStringFromSystemin("3", new String[] {
/* 131 */       "1 - Format", 
/* 132 */       "2 - View Or Modify Upload Queues In Notpad", 
/* 133 */       "3 - Upload" });
/*     */     
/*     */ 
/* 136 */     if (option.equalsIgnoreCase("1"))
/*     */     {
/* 138 */       formater(expandFtpUploadConfList, wkdir);
/* 139 */       System.exit(0);
/*     */     }
/* 141 */     else if ((option.length() > 0) && (option.equalsIgnoreCase("2")))
/*     */     {
/*     */ 
/* 144 */       Runtime.getRuntime().exec("notepad " + chosedUploadCfPath);
/* 145 */       System.out.println("You Can View or Modify The Upload Queues In Notpad,After that,Chose Options as :");
/* 146 */       option = readStringFromSystemin("1", new String[] {
/* 147 */         "1 - Ok,Upload", 
/* 148 */         "2 - Cancel" });
/* 149 */       if (option.equalsIgnoreCase("2")) { System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 154 */     Object OkUploadConfList = new ArrayList();
/* 155 */     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(chosedUploadCfPath)));
/*     */     for (;;)
/*     */     {
/* 158 */       String readLine = br.readLine();
/* 159 */       if (readLine == null) break;
/* 160 */       if (readLine.trim().length() > 0)
/*     */       {
/* 162 */         FtpUploadConf expandFtpUploadConf = new FtpUploadConf();
/* 163 */         expandFtpUploadConf.setClientPath(readLine.split("<->")[0].trim());
/* 164 */         expandFtpUploadConf.setServerPath(readLine.split("<->")[1].trim());
/* 165 */         ((List)OkUploadConfList).add(expandFtpUploadConf);
/*     */       }
/*     */     }
/* 168 */     expandFtpUploadConfList = (List<FtpUploadConf>)OkUploadConfList;
/*     */     
/*     */ 
/* 171 */     XFtp ftp = (XFtp)FTP_MAP.get(conf.getServerType());
/* 172 */     ftp.prepareConnect(conf.getHost(), conf.getPort().intValue(), conf.getUsername(), conf.getPassword(), null);
/*     */     
/* 174 */     for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
/* 175 */       ftp.cdOrMakeIfNotExists(expandFtpUploadConf.getServerPath());
/* 176 */       System.out.println("working... " + expandFtpUploadConf.getServerPath());
/*     */     }
/*     */     
/*     */ 
/* 180 */     System.out.println("Finished!");
/* 181 */     ftp.disconnect();
/*     */     
/*     */ 
/* 184 */     conf = (FtpConf)XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
/* 185 */     expandFtpUploadConfList = new ArrayList();
/* 186 */     for (FtpUploadConf ftpUploadConf : conf.getFtpUploadConfList())
/*     */     {
/* 188 */       expandFtpUploadConfList.addAll(expandFtpUploadConf(ftpUploadConf));
/*     */     }
/* 190 */     formater(expandFtpUploadConfList, wkdir);
/*     */     
/*     */ 
/* 193 */     System.exit(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<FtpUploadConf> expandFtpUploadConf(FtpUploadConf ftpUploadConf)
/*     */   {
/* 200 */     String clientPath = ftpUploadConf.getClientPath();
/* 201 */     File file = new File(clientPath);
/* 202 */     List<FtpUploadConf> expandFtpUploadConfList = new ArrayList();
/* 203 */     expandFtpUploadConf(file, ftpUploadConf, expandFtpUploadConfList);
/* 204 */     return expandFtpUploadConfList;
/*     */   }
/*     */   
/*     */   public static void formater(List<FtpUploadConf> expandFtpUploadConfList, String wkdir) throws Exception {
/* 208 */     Map<String, String> lastModMap = new HashMap();
/* 209 */     for (FtpUploadConf expandFtpUploadConf : expandFtpUploadConfList) {
/* 210 */       lastModMap.put(expandFtpUploadConf.getClientPath(), expandFtpUploadConf.getFileMd5());
/*     */     }
/* 212 */     ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(wkdir + "/lastModMap.d"));
/* 213 */     os.writeObject(lastModMap);
/* 214 */     os.close();
/* 215 */     System.out.println("Format Success!");
/*     */   }
/*     */   
/*     */   private static void expandFtpUploadConf(File file, FtpUploadConf ftpUploadConf, List<FtpUploadConf> expandFtpUploadConfList) {
/* 219 */     if (file.isHidden()) return;
/*     */     int j;
/* 221 */     if (file.isDirectory())
/*     */     {
/* 223 */       File[] subFiles = file.listFiles();
/* 224 */       File[] arrayOfFile1; j = (arrayOfFile1 = subFiles).length; for (int i = 0; i < j; i++) { File subFile = arrayOfFile1[i];
/* 225 */         expandFtpUploadConf(subFile, ftpUploadConf, expandFtpUploadConfList);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 230 */       String clientFileFolderPath = file.getParentFile().getAbsolutePath();
/*     */       
/* 232 */       String[] excludes = ftpUploadConf.getExcludes();
/* 233 */       if (excludes != null) {
/*     */         String[] arrayOfString1;
/* 235 */         int k = (arrayOfString1 = excludes).length; for (j = 0; j < k; j++) { String exclude = arrayOfString1[j];
/* 236 */           if (file.getAbsolutePath().startsWith(exclude.replace('/', '\\').replaceAll("\\\\\\\\", "\\\\"))) { return;
/*     */           }
/*     */         }
/*     */       }
/* 240 */       String srClientPath = ftpUploadConf.getClientPath();
/* 241 */       String srServerPath = ftpUploadConf.getServerPath();
/* 242 */       if (!srServerPath.endsWith("/")) throw new RuntimeException(srServerPath + " not end with /");
/* 243 */       String abPath = "";
/* 244 */       if (clientFileFolderPath.length() >= srClientPath.length())
/*     */       {
/* 246 */         abPath = clientFileFolderPath.substring(srClientPath.length()).replace('\\', '/');
/*     */       }
/* 248 */       FtpUploadConf expandFtpUploadConf = new FtpUploadConf();
/* 249 */       expandFtpUploadConf.setClientPath(file.getAbsolutePath());
/* 250 */       expandFtpUploadConf.setServerPath(srServerPath + abPath);
/* 251 */       expandFtpUploadConf.setFileMd5(FileMd5.getMD5(file));
/* 252 */       expandFtpUploadConfList.add(expandFtpUploadConf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] readBytesFromInStream(InputStream in)
/*     */   {
/*     */     try
/*     */     {
/* 260 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
/* 261 */       byte[] buf = new byte['ࠀ'];
/* 262 */       int len = 0;
/*     */       try
/*     */       {
/* 265 */         while ((len = in.read(buf)) != -1)
/*     */         {
/* 267 */           baos.write(buf, 0, len);
/*     */         }
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 272 */         throw new RuntimeException(e);
/*     */       }
/* 274 */       return baos.toByteArray();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 278 */       throw new RuntimeException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 282 */       if (in != null)
/*     */       {
/*     */         try
/*     */         {
/* 286 */           in.close();
/*     */         }
/*     */         catch (Exception e) {
/* 289 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String readStringFromSystemin(String defaultVal, String... item)
/*     */     throws Exception
/*     */   {
/* 303 */     Set<String> set = new LinkedHashSet();
/* 304 */     String[] arrayOfString1; int j = (arrayOfString1 = item).length; for (int i = 0; i < j; i++) { String it = arrayOfString1[i];
/* 305 */       String[] os = it.split("-");
/* 306 */       if (os.length != 2) throw new RuntimeException(it + " use error '-' flag!");
/* 307 */       set.add(os[0].trim());
/* 308 */       System.out.println("        " + it);
/*     */     }
/*     */     String r;
/*     */     do {
/* 312 */       System.out.print("Please Chose " + Arrays.toString(set.toArray()) + " " + (defaultVal == null ? "" : defaultVal) + ":");
/* 313 */       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/* 314 */       r = br.readLine();
/* 315 */       if ((r.trim().length() == 0) && (defaultVal != null))
/*     */       {
/* 317 */         return defaultVal;
/*     */       }
/* 319 */     } while (!set.contains(r));
/*     */     
/* 321 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\ftp\FtpUploader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */