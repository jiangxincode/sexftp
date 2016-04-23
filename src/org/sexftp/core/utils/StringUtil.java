/*     */ package org.sexftp.core.utils;
/*     */ 
/*     */ import com.thoughtworks.xstream.XStream;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*     */   public static String getHumanSize(long size)
/*     */   {
/*  20 */     float s = 0.0F;
/*  21 */     String u = "";
/*  22 */     if (size > 1048576L)
/*     */     {
/*  24 */       s = (float)size / 1024.0F / 1024.0F;
/*  25 */       u = "MB";
/*     */     }
/*  27 */     else if (size > 1024L)
/*     */     {
/*  29 */       s = (float)size / 1024.0F;
/*  30 */       u = "KB";
/*     */     }
/*     */     else
/*     */     {
/*  34 */       s = (float)size;
/*  35 */       u = "Bytes";
/*     */     }
/*  37 */     return String.format("%.2f %s", new Object[] { Float.valueOf(s), u });
/*     */   }
/*     */   
/*     */   public static String iso88591(String str, String encode) {
/*     */     try {
/*  42 */       return new String(str.getBytes(encode), "iso-8859-1");
/*     */     } catch (UnsupportedEncodingException e) {
/*  44 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String bakFromiso88591(String str, String encode) {
/*     */     try {
/*  50 */       return new String(str.getBytes("iso-8859-1"), encode);
/*     */     } catch (UnsupportedEncodingException e) {
/*  52 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] getBytes(String str, String encode) {
/*     */     try {
/*  58 */       return str.getBytes(encode);
/*     */     } catch (UnsupportedEncodingException e) {
/*  60 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String readExceptionDetailInfo(Throwable e) {
/*  65 */     StringWriter sw = new StringWriter();
/*  66 */     PrintWriter pw = new PrintWriter(sw);
/*  67 */     e.printStackTrace(pw);
/*  68 */     return sw.toString();
/*     */   }
/*     */   
/*  71 */   public static final char[] REGKEYS = { '\\', '^', '{', '}', '[', ']', '-', '+', '$', 
/*  72 */     '*', '?', '.' };
/*     */   
/*     */ 
/*     */   public static final String FILE_STYLE_MF = "d79d6a9cd10a4c5cb523b00fea9dda7f";
/*     */   
/*     */ 
/*     */   public static String replaceAll(String str, String findstr, String replacestr)
/*     */   {
/*  80 */     String[] sps = split(str, findstr);
/*  81 */     StringBuffer s = new StringBuffer();
/*  82 */     int i = 0;
/*  83 */     String[] arrayOfString1; int j = (arrayOfString1 = sps).length; for (int i = 0; i < j; i++) { String sp = arrayOfString1[i];
/*     */       
/*  85 */       if (i > 0)
/*     */       {
/*  87 */         s.append(replacestr);
/*     */       }
/*  89 */       s.append(sp);
/*  90 */       i++;
/*     */     }
/*  92 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static String[] split(String str, String findstr)
/*     */   {
/*  97 */     int indexOf = str.indexOf(findstr);
/*  98 */     int lastIndex = 0;
/*  99 */     List<String> lst = new ArrayList();
/* 100 */     for (int i = 0; (i < 5000) && (indexOf >= 0); i++)
/*     */     {
/* 102 */       String sp = str.substring(lastIndex, indexOf);
/* 103 */       lst.add(sp);
/* 104 */       lastIndex = indexOf + findstr.length();
/* 105 */       if (indexOf + findstr.length() >= str.length())
/*     */         break;
/* 107 */       indexOf = str.indexOf(findstr, indexOf + findstr.length());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */     lst.add(str.substring(lastIndex, str.length()));
/* 115 */     return (String[])lst.toArray(new String[0]);
/*     */   }
/*     */   
/*     */   public static boolean fileStyleMatch(String str, String[] matchstrs)
/*     */   {
/* 120 */     if (matchstrs != null) { String[] arrayOfString;
/* 121 */       int j = (arrayOfString = matchstrs).length; for (int i = 0; i < j; i++) { String matchstr = arrayOfString[i];
/* 122 */         if ((matchstr != null) && (matchstr.trim().length() > 0) && 
/* 123 */           (fileStyleMatch(str, matchstr)))
/*     */         {
/* 125 */           return true; }
/*     */       }
/*     */     }
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean fileStyleEIMatch(String str, String[] excludes, String[] includes)
/*     */   {
/* 133 */     boolean isExlude = (excludes == null) || (!fileStyleMatch(str, excludes));
/* 134 */     boolean isInclude = false;
/* 135 */     if (includes != null)
/*     */     {
/* 137 */       if ((new File(str).isDirectory()) || (str.endsWith("/")))
/*     */       {
/* 139 */         isInclude = true;
/*     */       }
/*     */       else
/*     */       {
/* 143 */         isInclude = fileStyleMatch(str, includes);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 148 */       isInclude = true;
/*     */     }
/* 150 */     return (isInclude) && (isExlude);
/*     */   }
/*     */   
/*     */   public static boolean fileStyleMatch(String str, String matchstr)
/*     */   {
/* 155 */     str = str.replace('\\', '/').toLowerCase();
/* 156 */     matchstr = matchstr.replace('\\', '/').toLowerCase();
/*     */     try {
/* 158 */       String s = matchstr;
/*     */       
/* 160 */       for (int i = 0; i < REGKEYS.length; i++)
/*     */       {
/* 162 */         char regKey = REGKEYS[i];
/* 163 */         s = replaceAll(s, regKey, String.format("%s%2d", new Object[] { "d79d6a9cd10a4c5cb523b00fea9dda7f", Integer.valueOf(i) }));
/*     */       }
/* 165 */       for (int i = 0; i < REGKEYS.length; i++)
/*     */       {
/* 167 */         String newRK = String.format("%s%2d", new Object[] { "d79d6a9cd10a4c5cb523b00fea9dda7f", Integer.valueOf(i) });
/* 168 */         char regKey = REGKEYS[i];
/*     */         
/* 170 */         switch (regKey) {
/*     */         case '\\': 
/* 172 */           s = replaceAll(s, newRK, "\\\\");
/* 173 */           break;
/*     */         case '.': 
/* 175 */           s = replaceAll(s, newRK, "\\.");
/* 176 */           break;
/*     */         case '*': 
/* 178 */           s = replaceAll(s, newRK, ".*");
/* 179 */           break;
/*     */         case '?': 
/* 181 */           s = replaceAll(s, newRK, ".?");
/* 182 */           break;
/*     */         default: 
/* 184 */           s = replaceAll(s, newRK, "\\" + regKey);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 189 */       s = "^" + s + "$";
/*     */       
/*     */ 
/* 192 */       Pattern p = Pattern.compile(s);
/*     */       
/* 194 */       Matcher matcher = p.matcher(str);
/*     */       
/* 196 */       return matcher.matches();
/*     */     } catch (Exception e) {
/* 198 */       throw new RuntimeException(String.format("%s - %s", new Object[] { str, matchstr }), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String simpString(String str, int maxlen)
/*     */   {
/* 204 */     if (str.length() > maxlen)
/*     */     {
/* 206 */       int lstind = str.length() - maxlen * 4 / 5;
/* 207 */       int fstind = maxlen * 1 / 5;
/* 208 */       return str.substring(0, fstind) + "..." + str.substring(lstind);
/*     */     }
/* 210 */     return str;
/*     */   }
/*     */   
/* 213 */   public static void main(String[] args) throws Exception { java.util.Set<Map.Entry<Object, Object>> entrySet = System.getProperties().entrySet();
/* 214 */     for (Map.Entry<Object, Object> entry : entrySet) {
/* 215 */       System.out.println(entry.getKey() + ":" + entry.getValue());
/*     */     }
/* 217 */     System.out.println(InetAddress.getLocalHost().toString());
/*     */   }
/*     */   
/*     */ 
/* 221 */   static XStream xStream = new XStream();
/*     */   
/*     */   public static Object deepClone(Object o) {
/* 224 */     return xStream.fromXML(xStream.toXML(o));
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\StringUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */