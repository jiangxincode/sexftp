/*     */ package org.sexftp.core.utils;
/*     */ 
/*     */ import info.monitorenter.cpdetector.io.ASCIIDetector;
/*     */ import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
/*     */ import info.monitorenter.cpdetector.io.JChardetFacade;
/*     */ import info.monitorenter.cpdetector.io.UnicodeDetector;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.sexftp.core.exceptions.SRuntimeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cpdetector
/*     */ {
/*  35 */   private static Set<Character> ZH_SBC_CASE = new HashSet(Arrays.asList(new Character[] {
/*  36 */     Character.valueOf('　'), 
/*  37 */     Character.valueOf(65281), 
/*  38 */     Character.valueOf(65282), 
/*  39 */     Character.valueOf(65283), 
/*  40 */     Character.valueOf(65284), 
/*  41 */     Character.valueOf(65285), 
/*  42 */     Character.valueOf(65286), 
/*  43 */     Character.valueOf(65287), 
/*  44 */     Character.valueOf(65288), 
/*  45 */     Character.valueOf(65289), 
/*  46 */     Character.valueOf(65290), 
/*  47 */     Character.valueOf(65291), 
/*  48 */     Character.valueOf(65292), 
/*  49 */     Character.valueOf(65293), 
/*  50 */     Character.valueOf(65294), 
/*  51 */     Character.valueOf(65295), 
/*  52 */     Character.valueOf(65296), 
/*  53 */     Character.valueOf(65297), 
/*  54 */     Character.valueOf(65298), 
/*  55 */     Character.valueOf(65299), 
/*  56 */     Character.valueOf(65300), 
/*  57 */     Character.valueOf(65301), 
/*  58 */     Character.valueOf(65302), 
/*  59 */     Character.valueOf(65303), 
/*  60 */     Character.valueOf(65304), 
/*  61 */     Character.valueOf(65305), 
/*  62 */     Character.valueOf(65306), 
/*  63 */     Character.valueOf(65307), 
/*  64 */     Character.valueOf(65308), 
/*  65 */     Character.valueOf(65309), 
/*  66 */     Character.valueOf(65310), 
/*  67 */     Character.valueOf(65311), 
/*  68 */     Character.valueOf(65312), 
/*  69 */     Character.valueOf(65313), 
/*  70 */     Character.valueOf(65314), 
/*  71 */     Character.valueOf(65315), 
/*  72 */     Character.valueOf(65316), 
/*  73 */     Character.valueOf(65317), 
/*  74 */     Character.valueOf(65318), 
/*  75 */     Character.valueOf(65319), 
/*  76 */     Character.valueOf(65320), 
/*  77 */     Character.valueOf(65321), 
/*  78 */     Character.valueOf(65322), 
/*  79 */     Character.valueOf(65323), 
/*  80 */     Character.valueOf(65324), 
/*  81 */     Character.valueOf(65325), 
/*  82 */     Character.valueOf(65326), 
/*  83 */     Character.valueOf(65327), 
/*  84 */     Character.valueOf(65328), 
/*  85 */     Character.valueOf(65329), 
/*  86 */     Character.valueOf(65330), 
/*  87 */     Character.valueOf(65331), 
/*  88 */     Character.valueOf(65332), 
/*  89 */     Character.valueOf(65333), 
/*  90 */     Character.valueOf(65334), 
/*  91 */     Character.valueOf(65335), 
/*  92 */     Character.valueOf(65336), 
/*  93 */     Character.valueOf(65337), 
/*  94 */     Character.valueOf(65338), 
/*  95 */     Character.valueOf(65339), 
/*  96 */     Character.valueOf(65340), 
/*  97 */     Character.valueOf(65341), 
/*  98 */     Character.valueOf(65342), 
/*  99 */     Character.valueOf(65343), 
/* 100 */     Character.valueOf(65344), 
/* 101 */     Character.valueOf(65345), 
/* 102 */     Character.valueOf(65346), 
/* 103 */     Character.valueOf(65347), 
/* 104 */     Character.valueOf(65348), 
/* 105 */     Character.valueOf(65349), 
/* 106 */     Character.valueOf(65350), 
/* 107 */     Character.valueOf(65351), 
/* 108 */     Character.valueOf(65352), 
/* 109 */     Character.valueOf(65353), 
/* 110 */     Character.valueOf(65354), 
/* 111 */     Character.valueOf(65355), 
/* 112 */     Character.valueOf(65356), 
/* 113 */     Character.valueOf(65357), 
/* 114 */     Character.valueOf(65358), 
/* 115 */     Character.valueOf(65359), 
/* 116 */     Character.valueOf(65360), 
/* 117 */     Character.valueOf(65361), 
/* 118 */     Character.valueOf(65362), 
/* 119 */     Character.valueOf(65363), 
/* 120 */     Character.valueOf(65364), 
/* 121 */     Character.valueOf(65365), 
/* 122 */     Character.valueOf(65366), 
/* 123 */     Character.valueOf(65367), 
/* 124 */     Character.valueOf(65368), 
/* 125 */     Character.valueOf(65369), 
/* 126 */     Character.valueOf(65370), 
/* 127 */     Character.valueOf(65371), 
/* 128 */     Character.valueOf(65372), 
/* 129 */     Character.valueOf(65373), 
/* 130 */     Character.valueOf(65374) }));
/*     */   public static final int sta = 161;
/*     */   
/*     */   public static void main(String[] args) throws Exception
/*     */   {
/* 135 */     byte[] data = FileUtil.readBytesFromInStream(new FileInputStream("E:/coynn/workc/MyEclipse 8.5/productActivation/src/com/kkfun/productactive/monitor/ProductActivationMonitor.java"));
/* 136 */     new String(data, "GBK");
/* 137 */     new String(data, "x-EUC-TW");
/* 138 */     data = delASCIIdata(data);
/* 139 */     System.out.println(encode(new ByteArrayInputStream(data, 0, 2)));
/* 140 */     System.out.println(new String(data));
/*     */   }
/*     */   
/*     */   public static byte[] delASCIIdata(byte[] data)
/*     */   {
/* 145 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 146 */     for (int i = 0; i < data.length; i++) {
/* 147 */       byte b = data[i];
/* 148 */       char c = (char)b;
/* 149 */       if ((c >= '') || (c <= '\b'))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */         bos.write(b);
/* 156 */         i++;
/*     */         
/* 158 */         if (i < data.length) bos.write(data[i]);
/*     */       }
/*     */     }
/* 161 */     return bos.toByteArray(); }
/*     */   
/* 163 */   public static final String[] SUPORT_CHARSET = { "GBK", "UTF-8", "GB2312", "GB18030" };
/*     */   private static final int PS = 134217728;
/*     */   
/*     */   public static String isOnlyASC(byte[] data) throws UnsupportedEncodingException {
/*     */     String[] arrayOfString;
/* 168 */     int j = (arrayOfString = SUPORT_CHARSET).length; for (int i = 0; i < j; i++) { String suportchar = arrayOfString[i];
/*     */       
/* 170 */       boolean isallasc = true;
/* 171 */       String encode = "";
/* 172 */       char[] arrayOfChar; int m = (arrayOfChar = new String(data, suportchar).toCharArray()).length; for (int k = 0; k < m; k++) { char c = arrayOfChar[k];
/*     */         
/* 174 */         if ((c > '') || (c < '\b'))
/*     */         {
/* 176 */           if (ZH_SBC_CASE.contains(Character.valueOf(c)))
/*     */           {
/* 178 */             encode = suportchar;
/*     */           }
/*     */           else
/*     */           {
/* 182 */             isallasc = false;
/* 183 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 187 */       if (isallasc)
/*     */       {
/* 189 */         if (encode.length() > 0)
/*     */         {
/* 191 */           return "US-ASCII_" + encode;
/*     */         }
/*     */         
/* 194 */         return "US-ASCII";
/*     */       }
/*     */     }
/* 197 */     return "";
/*     */   }
/*     */   
/*     */   public static String onlyNoneASCII(String str) {
/* 201 */     StringBuffer sb = new StringBuffer();
/* 202 */     char[] arrayOfChar; int j = (arrayOfChar = str.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/*     */       
/* 204 */       if ((c > '') || (c < '\b'))
/*     */       {
/* 206 */         sb.append(c);
/*     */       }
/*     */     }
/* 209 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Charset richencode(InputStream in) throws UnsupportedEncodingException {
/* 213 */     byte[] data = FileUtil.readBytesFromInStream(in);
/* 214 */     String isallasc = isOnlyASC(data);
/* 215 */     String encode = null;
/* 216 */     if (isallasc.length() > 0)
/*     */     {
/* 218 */       if (isallasc.startsWith("US-ASCII_"))
/*     */       {
/* 220 */         encode = isallasc.replace("US-ASCII_", "");
/*     */       }
/*     */       else
/*     */       {
/* 224 */         encode = isallasc;
/*     */       }
/*     */     }
/* 227 */     if (encode == null)
/*     */     {
/* 229 */       Charset c = encode(new ByteArrayInputStream(data));
/* 230 */       encode = c != null ? c.toString() : null;
/*     */     }
/*     */     
/* 233 */     if (encode != null)
/*     */     {
/* 235 */       if (("x-EUC-TW".equalsIgnoreCase(encode)) || ("windows-1252".equalsIgnoreCase(encode)) || ("EUC-KR".equalsIgnoreCase(encode)))
/*     */       {
/* 237 */         byte[] newdata = delASCIIdata(data);
/* 238 */         Charset c = encode(new ByteArrayInputStream(newdata));
/* 239 */         String newcode = c != null ? c.toString() : null;
/* 240 */         if (newcode != null)
/*     */         {
/* 242 */           if (newcode.startsWith("GB"))
/*     */           {
/* 244 */             encode = newcode;
/*     */           }
/*     */           else
/*     */           {
/* 248 */             encode = "GB18030";
/*     */           }
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 254 */         return Charset.forName(encode);
/*     */       } catch (Exception e) {
/* 256 */         throw new SRuntimeException(e);
/*     */       }
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */   
/*     */   public static Charset encode(InputStream in) {
/* 263 */     return encode(in, 134217728);
/*     */   }
/*     */   
/*     */ 
/* 267 */   public static Charset xencode(InputStream in) { return null; }
/*     */   
/*     */   public static Charset encode(InputStream in, int length) {
/* 270 */     CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */     detector.add(ASCIIDetector.getInstance());
/* 283 */     detector.add(JChardetFacade.getInstance());
/*     */     
/* 285 */     detector.add(UnicodeDetector.getInstance());
/* 286 */     Charset charset = null;
/* 287 */     BufferedInputStream bis = new BufferedInputStream(in);
/*     */     try
/*     */     {
/* 290 */       charset = detector.detectCodepage(bis, length);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {}catch (IOException localIOException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 297 */     return charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\Cpdetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */