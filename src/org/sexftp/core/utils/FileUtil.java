/*     */ package org.sexftp.core.utils;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.sexftp.core.exceptions.AbortException;
/*     */ import org.sexftp.core.exceptions.BizException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*     */   public static String getTextFromFile(String filePath, String encode)
/*     */   {
/*  33 */     File f = new File(filePath);
/*  34 */     if (f.length() > 10000000L)
/*     */     {
/*  36 */       throw new BizException(String.format("[%s] File Total Size [%s] , More Than [%s], Can't Go Ahead!", new Object[] { filePath, StringUtil.getHumanSize(f.length()), StringUtil.getHumanSize(10000000L) }));
/*     */     }
/*  38 */     InputStream ins = null;
/*     */     try
/*     */     {
/*  41 */       ins = new FileInputStream(filePath);
/*  42 */       byte[] data = new byte[ins.available()];
/*  43 */       ins.read(data);
/*  44 */       return new String(data, encode);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  48 */       throw new RuntimeException(e);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/*  53 */         ins.close();
/*     */       }
/*     */       catch (Exception localException2) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void writeByte2File(String fielname, byte[] data)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: new 16	java/io/File
/*     */     //   5: dup
/*     */     //   6: aload_0
/*     */     //   7: invokespecial 18	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   10: invokevirtual 83	java/io/File:getParentFile	()Ljava/io/File;
/*     */     //   13: invokevirtual 87	java/io/File:mkdirs	()Z
/*     */     //   16: pop
/*     */     //   17: new 91	java/io/FileOutputStream
/*     */     //   20: dup
/*     */     //   21: aload_0
/*     */     //   22: invokespecial 93	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
/*     */     //   25: astore_2
/*     */     //   26: aload_2
/*     */     //   27: aload_1
/*     */     //   28: invokevirtual 94	java/io/OutputStream:write	([B)V
/*     */     //   31: aload_2
/*     */     //   32: invokevirtual 100	java/io/OutputStream:flush	()V
/*     */     //   35: goto +26 -> 61
/*     */     //   38: astore_3
/*     */     //   39: new 63	java/lang/RuntimeException
/*     */     //   42: dup
/*     */     //   43: aload_3
/*     */     //   44: invokespecial 65	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   47: athrow
/*     */     //   48: astore 4
/*     */     //   50: aload_2
/*     */     //   51: invokevirtual 103	java/io/OutputStream:close	()V
/*     */     //   54: goto +4 -> 58
/*     */     //   57: pop
/*     */     //   58: aload 4
/*     */     //   60: athrow
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual 103	java/io/OutputStream:close	()V
/*     */     //   65: goto +4 -> 69
/*     */     //   68: pop
/*     */     //   69: return
/*     */     // Line number table:
/*     */     //   Java source line #68	-> byte code offset #0
/*     */     //   Java source line #71	-> byte code offset #2
/*     */     //   Java source line #72	-> byte code offset #17
/*     */     //   Java source line #73	-> byte code offset #26
/*     */     //   Java source line #74	-> byte code offset #31
/*     */     //   Java source line #78	-> byte code offset #38
/*     */     //   Java source line #80	-> byte code offset #39
/*     */     //   Java source line #82	-> byte code offset #48
/*     */     //   Java source line #85	-> byte code offset #50
/*     */     //   Java source line #87	-> byte code offset #57
/*     */     //   Java source line #91	-> byte code offset #58
/*     */     //   Java source line #85	-> byte code offset #61
/*     */     //   Java source line #87	-> byte code offset #68
/*     */     //   Java source line #93	-> byte code offset #69
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	70	0	fielname	String
/*     */     //   0	70	1	data	byte[]
/*     */     //   1	61	2	os	java.io.OutputStream
/*     */     //   38	6	3	e	Exception
/*     */     //   48	11	4	localObject	Object
/*     */     //   57	1	5	localException1	Exception
/*     */     //   68	1	6	localException2	Exception
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   2	35	38	java/lang/Exception
/*     */     //   2	48	48	finally
/*     */     //   50	54	57	java/lang/Exception
/*     */     //   61	65	68	java/lang/Exception
/*     */   }
/*     */   
/*     */   public static void deleteFolder(File folder)
/*     */   {
/*  96 */     if (folder.isFile()) { folder.delete();
/*     */     } else {
/*     */       File[] arrayOfFile;
/*  99 */       int j = (arrayOfFile = folder.listFiles()).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile[i];
/*     */         
/* 101 */         deleteFolder(subfile);
/*     */       }
/* 103 */       folder.delete();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void copyFile(String srcPath, String desPath)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: iconst_0
/*     */     //   5: istore 4
/*     */     //   7: new 44	java/io/FileInputStream
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: invokespecial 46	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
/*     */     //   15: astore_2
/*     */     //   16: new 91	java/io/FileOutputStream
/*     */     //   19: dup
/*     */     //   20: aload_1
/*     */     //   21: invokespecial 93	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
/*     */     //   24: astore_3
/*     */     //   25: sipush 8192
/*     */     //   28: newarray <illegal type>
/*     */     //   30: astore 5
/*     */     //   32: iconst_0
/*     */     //   33: istore 4
/*     */     //   35: goto +64 -> 99
/*     */     //   38: aload_3
/*     */     //   39: aload 5
/*     */     //   41: iconst_0
/*     */     //   42: iload 6
/*     */     //   44: invokevirtual 125	java/io/OutputStream:write	([BII)V
/*     */     //   47: goto +45 -> 92
/*     */     //   50: astore 7
/*     */     //   52: new 63	java/lang/RuntimeException
/*     */     //   55: dup
/*     */     //   56: new 128	java/lang/StringBuilder
/*     */     //   59: dup
/*     */     //   60: ldc -126
/*     */     //   62: invokespecial 132	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   65: iload 4
/*     */     //   67: invokevirtual 133	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   70: ldc -119
/*     */     //   72: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   75: aload 7
/*     */     //   77: invokevirtual 142	java/lang/Exception:getMessage	()Ljava/lang/String;
/*     */     //   80: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   83: invokevirtual 146	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   86: aload 7
/*     */     //   88: invokespecial 149	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   91: athrow
/*     */     //   92: iload 4
/*     */     //   94: iload 6
/*     */     //   96: iadd
/*     */     //   97: istore 4
/*     */     //   99: aload_2
/*     */     //   100: aload 5
/*     */     //   102: invokevirtual 53	java/io/InputStream:read	([B)I
/*     */     //   105: dup
/*     */     //   106: istore 6
/*     */     //   108: iconst_m1
/*     */     //   109: if_icmpne -71 -> 38
/*     */     //   112: goto +49 -> 161
/*     */     //   115: astore 5
/*     */     //   117: aload 5
/*     */     //   119: athrow
/*     */     //   120: astore 5
/*     */     //   122: new 152	org/sexftp/core/exceptions/SRuntimeException
/*     */     //   125: dup
/*     */     //   126: aload 5
/*     */     //   128: invokespecial 154	org/sexftp/core/exceptions/SRuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   131: athrow
/*     */     //   132: astore 8
/*     */     //   134: aload_2
/*     */     //   135: ifnull +11 -> 146
/*     */     //   138: aload_2
/*     */     //   139: invokevirtual 60	java/io/InputStream:close	()V
/*     */     //   142: goto +4 -> 146
/*     */     //   145: pop
/*     */     //   146: aload_3
/*     */     //   147: ifnull +11 -> 158
/*     */     //   150: aload_3
/*     */     //   151: invokevirtual 103	java/io/OutputStream:close	()V
/*     */     //   154: goto +4 -> 158
/*     */     //   157: pop
/*     */     //   158: aload 8
/*     */     //   160: athrow
/*     */     //   161: aload_2
/*     */     //   162: ifnull +11 -> 173
/*     */     //   165: aload_2
/*     */     //   166: invokevirtual 60	java/io/InputStream:close	()V
/*     */     //   169: goto +4 -> 173
/*     */     //   172: pop
/*     */     //   173: aload_3
/*     */     //   174: ifnull +11 -> 185
/*     */     //   177: aload_3
/*     */     //   178: invokevirtual 103	java/io/OutputStream:close	()V
/*     */     //   181: goto +4 -> 185
/*     */     //   184: pop
/*     */     //   185: return
/*     */     // Line number table:
/*     */     //   Java source line #109	-> byte code offset #0
/*     */     //   Java source line #110	-> byte code offset #2
/*     */     //   Java source line #111	-> byte code offset #4
/*     */     //   Java source line #113	-> byte code offset #7
/*     */     //   Java source line #114	-> byte code offset #16
/*     */     //   Java source line #115	-> byte code offset #25
/*     */     //   Java source line #118	-> byte code offset #32
/*     */     //   Java source line #119	-> byte code offset #35
/*     */     //   Java source line #121	-> byte code offset #38
/*     */     //   Java source line #122	-> byte code offset #50
/*     */     //   Java source line #123	-> byte code offset #52
/*     */     //   Java source line #125	-> byte code offset #92
/*     */     //   Java source line #119	-> byte code offset #99
/*     */     //   Java source line #130	-> byte code offset #115
/*     */     //   Java source line #131	-> byte code offset #117
/*     */     //   Java source line #133	-> byte code offset #120
/*     */     //   Java source line #135	-> byte code offset #122
/*     */     //   Java source line #138	-> byte code offset #132
/*     */     //   Java source line #140	-> byte code offset #134
/*     */     //   Java source line #141	-> byte code offset #138
/*     */     //   Java source line #143	-> byte code offset #145
/*     */     //   Java source line #146	-> byte code offset #146
/*     */     //   Java source line #147	-> byte code offset #150
/*     */     //   Java source line #148	-> byte code offset #157
/*     */     //   Java source line #152	-> byte code offset #158
/*     */     //   Java source line #140	-> byte code offset #161
/*     */     //   Java source line #141	-> byte code offset #165
/*     */     //   Java source line #143	-> byte code offset #172
/*     */     //   Java source line #146	-> byte code offset #173
/*     */     //   Java source line #147	-> byte code offset #177
/*     */     //   Java source line #148	-> byte code offset #184
/*     */     //   Java source line #153	-> byte code offset #185
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	186	0	srcPath	String
/*     */     //   0	186	1	desPath	String
/*     */     //   1	165	2	is	InputStream
/*     */     //   3	175	3	os	java.io.OutputStream
/*     */     //   5	93	4	t	int
/*     */     //   30	71	5	bytes	byte[]
/*     */     //   115	3	5	e	AbortException
/*     */     //   120	7	5	e	Exception
/*     */     //   38	57	6	c	int
/*     */     //   106	3	6	c	int
/*     */     //   50	37	7	e	Exception
/*     */     //   132	27	8	localObject	Object
/*     */     //   145	1	12	localException1	Exception
/*     */     //   157	1	13	localException2	Exception
/*     */     //   172	1	14	localException3	Exception
/*     */     //   184	1	15	localException4	Exception
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   38	47	50	java/lang/Exception
/*     */     //   7	112	115	org/sexftp/core/exceptions/AbortException
/*     */     //   7	112	120	java/lang/Exception
/*     */     //   7	132	132	finally
/*     */     //   134	142	145	java/lang/Exception
/*     */     //   146	154	157	java/lang/Exception
/*     */     //   161	169	172	java/lang/Exception
/*     */     //   173	181	184	java/lang/Exception
/*     */   }
/*     */   
/*     */   public static byte[] readBytesFromInStream(InputStream in)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
/* 160 */       byte[] buf = new byte['ࠀ'];
/* 161 */       int len = 0;
/*     */       try
/*     */       {
/* 164 */         while ((len = in.read(buf)) != -1)
/*     */         {
/* 166 */           baos.write(buf, 0, len);
/*     */         }
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 171 */         throw new RuntimeException(e);
/*     */       }
/* 173 */       return baos.toByteArray();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 177 */       throw new RuntimeException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 181 */       if (in != null)
/*     */       {
/*     */         try
/*     */         {
/* 185 */           in.close();
/*     */         }
/*     */         catch (Exception e) {
/* 188 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static List<File> searchFile(File f, IProgressMonitor monitor)
/*     */   {
/* 196 */     List<File> list = new ArrayList();
/* 197 */     if (f.isFile())
/*     */     {
/* 199 */       list.add(f);
/*     */     }
/*     */     else
/*     */     {
/* 203 */       File[] subfiles = f.listFiles();
/* 204 */       if (subfiles != null)
/*     */       {
/* 206 */         if (monitor != null)
/*     */         {
/* 208 */           if (monitor.isCanceled())
/*     */           {
/* 210 */             throw new AbortException();
/*     */           }
/*     */           
/* 213 */           monitor.subTask("scanning in " + f.getAbsolutePath());
/*     */         }
/*     */         File[] arrayOfFile1;
/* 216 */         int j = (arrayOfFile1 = subfiles).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile1[i];
/*     */           
/* 218 */           list.addAll(searchFile(subfile, monitor));
/*     */         }
/*     */       }
/*     */     }
/* 222 */     return list;
/*     */   }
/*     */   
/*     */   public static Set<String> unionUpFilePath(Set<String> pathList)
/*     */   {
/* 227 */     Set<String> newPathList = new HashSet();
/* 228 */     int j; int i; for (Iterator localIterator = pathList.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 233 */         i < j)
/*     */     {
/* 228 */       String path = (String)localIterator.next();
/*     */       
/* 230 */       File file = new File(path);
/* 231 */       String addPath = file.isDirectory() ? file.getAbsolutePath() : file.getParent();
/* 232 */       newPathList.add(addPath);
/* 233 */       String[] arrayOfString; j = (arrayOfString = (String[])newPathList.toArray(new String[0])).length;i = 0; continue;String oldPath = arrayOfString[i];
/*     */       
/* 235 */       if ((!oldPath.equals(addPath)) && (oldPath.startsWith(addPath)))
/*     */       {
/* 237 */         newPathList.remove(oldPath);
/*     */       }
/* 233 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */     return newPathList;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\FileUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */