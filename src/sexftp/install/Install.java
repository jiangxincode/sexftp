/*     */ package sexftp.install;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.sexftp.core.utils.ByteUtils;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Install
/*     */ {
/*  31 */   private static String lang = "en";
/*  32 */   private static String curDir = ".";
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*  37 */     URL resource = Install.class.getClassLoader().getResource("sexftp/install/Install.class");
/*     */     
/*  39 */     String url = URLDecoder.decode(resource.getFile(), "utf-8");
/*  40 */     if (url.indexOf(".jar") > 0)
/*     */     {
/*  42 */       url = url.replace("file:/", "");
/*  43 */       url = url.substring(0, url.indexOf(".jar") + 4);
/*  44 */       if (new File(url).exists())
/*     */       {
/*  46 */         curDir = new File(url).getParent();
/*  47 */         System.out.println("Install Dir Changed:" + curDir);
/*     */       }
/*     */     }
/*     */     
/*  51 */     System.out.println("[sexftp]  Chose Language:");
/*  52 */     String lan = readStringFromSystemin("2", new String[] { "1 - English", "2 - 简体中文" });
/*  53 */     if (lan.equals("2")) { lang = "zh";
/*     */     }
/*  55 */     note();
/*     */     
/*  57 */     println("[sexftp]  Are You Sure To Go Ahead Install?", "[sexftp]  准备好了安装Sexftp吗？");
/*  58 */     String okInstallBegin = readStringFromSystemin("1", new String[] { "1 - Ok", "2 - Cancel" });
/*  59 */     if (okInstallBegin.equals("2"))
/*     */     {
/*  61 */       System.exit(-1);
/*     */     }
/*     */     
/*  64 */     File[] findFile = findFile();
/*  65 */     String[] inItem = new String[findFile.length + 1];
/*  66 */     println("[sexftp]  Chose Eclispe Folder", "[sexftp]  选择eclipse安装目录:");
/*  67 */     for (int i = 0; i < findFile.length; i++) {
/*  68 */       File file = findFile[i];
/*     */       
/*  70 */       inItem[i] = (i + 1 + " - " + file.getParentFile().getParentFile().getParentFile());
/*     */     }
/*  72 */     inItem[findFile.length] = (findFile.length + 1 + " - Not above?以上都不是?");
/*  73 */     String input = readStringFromSystemin(null, inItem);
/*     */     
/*  75 */     if (Integer.parseInt(input) == findFile.length + 1)
/*     */     {
/*  77 */       note();
/*     */       
/*  79 */       println("[sexftp]  Make Sure And Run Me Again!", "[sexftp]  请确认好后再运行本安装程序！");
/*  80 */       System.exit(-1);
/*     */     }
/*     */     
/*  83 */     File okfile = findFile[(Integer.parseInt(input) - 1)];
/*     */     
/*     */ 
/*  86 */     System.out.println("[sexftp]  将安装到:" + okfile);
/*     */     for (;;)
/*     */     {
/*  89 */       byte[] configdatas = FileUtil.readBytesFromInStream(new FileInputStream(okfile));
/*  90 */       String configHex = ByteUtils.getHexString(configdatas);
/*  91 */       String sexftpstr = ByteUtils.getHexString("sexftp,2012.".getBytes("utf-8"));
/*  92 */       if (configHex.indexOf(sexftpstr) < 0) {
/*     */         break;
/*     */       }
/*  95 */       String sexftpendstr = ByteUtils.getHexString(",4,false".getBytes("utf-8"));
/*     */       
/*  97 */       String enterHex = ByteUtils.getHexString("\n".getBytes("utf-8"));
/*  98 */       String newLineHex = ByteUtils.getHexString("\r".getBytes("utf-8"));
/*     */       
/* 100 */       String[] sfhsps = configHex.split(sexftpstr)[1].split(sexftpendstr);
/* 101 */       String sexftpItemHex = sexftpstr + sfhsps[0] + sexftpendstr;
/*     */       
/* 103 */       configHex = configHex.replace(enterHex + newLineHex + sexftpItemHex, "").trim();
/* 104 */       configHex = configHex.replace(newLineHex + enterHex + sexftpItemHex, "").trim();
/* 105 */       configHex = configHex.replace(enterHex + sexftpItemHex, "").trim();
/* 106 */       configHex = configHex.replace(sexftpItemHex, "").trim();
/*     */       
/* 108 */       println("[sexftp]  Sexftp Intalled,Will Unistall!", "[sexftp]  发现已经装了Sexftp，将先卸载她！");
/* 109 */       println(new String(ByteUtils.getByteArray(sexftpItemHex), "utf-8"));
/*     */       
/* 111 */       String isokUnistall = readStringFromSystemin("1", new String[] { "1 - Ok", "2 - Cancel" });
/* 112 */       if (isokUnistall.equals("2"))
/*     */       {
/* 114 */         System.exit(-1);
/*     */       }
/*     */       
/* 117 */       ByteUtils.writeByte2Stream(ByteUtils.getByteArray(configHex), new FileOutputStream(okfile));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     println("[Sexftp]  Installing...", "[Sexftp]  安装中...");
/* 125 */     install(okfile, new File(new File(curDir).getAbsolutePath()));
/* 126 */     println("[Sexftp]  Installed Success! Restart Eclipse/Myeclipse to Start!", "[Sexftp]  安装成功，重启 Eclipse/Myeclise即可生效。");
/* 127 */     println("[Sexftp]  If Not Success By Thes Install Program,Contact Us!", "[Sexftp]  如果本安装程序没能成功，请联系我们解决。");
/* 128 */     input = readStringFromSystemin("1", new String[] { "1 - Exit." });
/*     */   }
/*     */   
/*     */   public static void note()
/*     */   {
/* 133 */     println("[Sexftp]  Make Sure You Done One of these as follows:\r\n   1、move install files to Eclipse/Myeclipse install directory.\r\n   2、Run The Eclipse/Myeclise.\r\nIf you Do One Of Them,I Can find Eclipse/Myeclipse to Install Sexftp.", 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 138 */       "[Sexftp]  确定你做了如下两件事之一：\r\n   1、将本安装程序解压在 Eclipse/Myeclipse安装目录再运行。\r\n   2、运行 Eclipse/Myeclipse。\r\n以上两种方法只要选择一种即可， 本安装程序就能找到 Eclipse/Myeclipse以便安装 Sexftp 插件.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void install(File okfile, File meFolder)
/*     */   {
/* 146 */     File sexftpJar = null;
/* 147 */     File[] listFiles = meFolder.listFiles();
/* 148 */     if (listFiles != null)
/*     */     {
/* 150 */       Arrays.sort(listFiles);
/* 151 */       for (int i = listFiles.length - 1; i >= 0; i--)
/*     */       {
/* 153 */         if (listFiles[i].getName().endsWith(".jar"))
/*     */         {
/* 155 */           sexftpJar = listFiles[i];
/* 156 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 160 */     if (sexftpJar == null)
/*     */     {
/* 162 */       println("[Sexftp]  Plugin Jar Losted!", "[Sexftp]  插件Jar丢失！");
/* 163 */       println("In The Folder:", "在这个目录下：");
/* 164 */       System.out.println(meFolder);
/* 165 */       System.exit(-1);
/*     */     }
/*     */     
/*     */ 
/* 169 */     File newPluginFolder = new File(okfile.getParentFile().getParentFile().getParentFile().getAbsolutePath() + "/sexftp/");
/* 170 */     if (!newPluginFolder.exists())
/*     */     {
/* 172 */       newPluginFolder.mkdirs();
/*     */     }
/* 174 */     FileUtil.copyFile(sexftpJar.getAbsolutePath(), newPluginFolder.getAbsolutePath() + "/" + sexftpJar.getName());
/* 175 */     String mefolderpath = newPluginFolder.getAbsolutePath().replace('\\', '/');
/* 176 */     if (!mefolderpath.endsWith("/")) mefolderpath = mefolderpath + "/";
/* 177 */     mefolderpath = mefolderpath + sexftpJar.getName();
/*     */     
/*     */ 
/* 180 */     String addconfigStr = sexftpJar.getName().replace('_', ',').replace(".jar", "") + ",file:/" + mefolderpath + ",4,false";
/*     */     try {
/* 182 */       BufferedWriter bw = new BufferedWriter(new FileWriter(okfile, true));
/* 183 */       bw.write("\r\n" + addconfigStr);
/* 184 */       bw.close();
/*     */     } catch (IOException e) {
/* 186 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static File[] findFile()
/*     */     throws Exception
/*     */   {
/* 194 */     File f = new File(curDir);
/* 195 */     println("[sexftp]  Find eclipse From:", "[sexftp]  从这里开始寻找eclipse:");
/* 196 */     println(f.getAbsolutePath());
/* 197 */     println("[sexftp]  It may taken 10 Seconds.", "[sexftp]  可能需要10秒钟的时间.");
/*     */     
/* 199 */     f = findEclpseFile(new File(f.getAbsolutePath()));
/* 200 */     List<File> files = new ArrayList();
/* 201 */     if (f != null) {
/* 202 */       files.add(f);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 208 */       Process process = Runtime.getRuntime().exec("wmic process get ExecutablePath");
/* 209 */       String in = new String(FileUtil.readBytesFromInStream(process.getInputStream()));
/* 210 */       String[] its = in.split("\n");
/*     */       String[] arrayOfString1;
/* 212 */       int j = (arrayOfString1 = its).length; for (int i = 0; i < j; i++) { String it = arrayOfString1[i];
/* 213 */         if (it.trim().endsWith("eclipse.exe"))
/*     */         {
/* 215 */           startTime = System.currentTimeMillis();
/* 216 */           File okf = findEclipseFile(new File(it.trim()).getParentFile());
/* 217 */           if (okf != null) {
/* 218 */             files.add(okf);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 224 */       if (files.size() == 0) {
/* 225 */         println("[sexftp]  Try Find Windows edclipse Process Failed,You Can Do[WINDOWS KEY->RUN] Input WMIC Then Enter!After That,Rerun Me!", 
/* 226 */           "[sexftp]  搜索eclipse进程失败，你可以先作这个操作:windows键->运行 输入WMIC回车,然后重新运行本安装程序。 ");
/*     */       }
/*     */     }
/* 229 */     return (File[])files.toArray(new File[0]);
/*     */   }
/*     */   
/*     */   public static File findEclpseFile(File from) {
/* 233 */     startTime = System.currentTimeMillis();
/* 234 */     File st = from;
/* 235 */     for (int i = 0; (i < 20) && (st != null); i++)
/*     */     {
/* 237 */       File findOk = findEclipseFile(st);
/* 238 */       findedPathSet.add(st.getAbsolutePath());
/* 239 */       if (findOk != null) {
/* 240 */         return findOk;
/*     */       }
/* 242 */       st = st.getParentFile();
/*     */     }
/* 244 */     return null; }
/*     */   
/* 246 */   private static Set<String> findedPathSet = new HashSet();
/* 247 */   private static long startTime = 0L;
/*     */   
/*     */   public static File findEclipseFile(File f) {
/* 250 */     long findTime = System.currentTimeMillis() - startTime;
/* 251 */     if (findTime > 10000L) {
/* 252 */       return null;
/*     */     }
/* 254 */     if (findedPathSet.contains(f.getAbsolutePath()))
/*     */     {
/* 256 */       return null;
/*     */     }
/* 258 */     File maybefile = null;
/* 259 */     if (f.isDirectory())
/*     */     {
/* 261 */       String maybePath = f.getAbsolutePath() + "/configuration/org.eclipse.equinox.simpleconfigurator/bundles.info";
/* 262 */       maybefile = new File(maybePath);
/* 263 */       if (maybefile.exists()) {
/* 264 */         return maybefile;
/*     */       }
/* 266 */       maybefile = null;
/* 267 */       if (f.listFiles() != null) { File[] arrayOfFile;
/* 268 */         int j = (arrayOfFile = f.listFiles()).length; for (int i = 0; i < j; i++) { File subf = arrayOfFile[i];
/*     */           
/* 270 */           maybefile = findEclipseFile(subf);
/* 271 */           if (maybefile != null) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 277 */     return maybefile;
/*     */   }
/*     */   
/*     */   public static String readStringFromSystemin(String defaultVal, String... item) throws Exception
/*     */   {
/* 282 */     Set<String> set = new LinkedHashSet();
/* 283 */     String[] arrayOfString1; int j = (arrayOfString1 = item).length; for (int i = 0; i < j; i++) { String it = arrayOfString1[i];
/* 284 */       String[] os = it.split("-");
/* 285 */       if (os.length < 2) throw new RuntimeException(it + " use error '-' flag!");
/* 286 */       set.add(os[0].trim());
/* 287 */       System.out.println("        " + it);
/*     */     }
/*     */     String r;
/*     */     do {
/* 291 */       System.out.print("[Input] Please Chose " + Arrays.toString(set.toArray()) + " " + (defaultVal == null ? "" : defaultVal) + ":");
/* 292 */       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/* 293 */       r = br.readLine();
/* 294 */       if ((r.trim().length() == 0) && (defaultVal != null))
/*     */       {
/* 296 */         return defaultVal;
/*     */       }
/* 298 */     } while (!set.contains(r));
/*     */     
/* 300 */     return r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void print(String en, String zh)
/*     */   {
/* 307 */     if (lang.equals("zh"))
/*     */     {
/* 309 */       System.out.print(zh);
/*     */     }
/*     */     else
/*     */     {
/* 313 */       System.out.print(en);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void println(String en, String zh) {
/* 318 */     if (lang.equals("zh"))
/*     */     {
/* 320 */       System.out.println(zh);
/*     */     }
/*     */     else
/*     */     {
/* 324 */       System.out.println(en);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void println(String str) {
/* 329 */     System.out.println(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\install\Install.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */