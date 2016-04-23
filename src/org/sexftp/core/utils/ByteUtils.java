/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class ByteUtils
/*    */ {
/*    */   public static String getHexString(byte[] b) throws Exception
/*    */   {
/* 10 */     StringBuffer result = new StringBuffer();
/* 11 */     for (int i = 0; i < b.length; i++) {
/* 12 */       result.append(Integer.toString((b[i] & 0xFF) + 256, 16).substring(1));
/*    */     }
/* 14 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static byte[] getByteArray(String str) {
/* 18 */     if (str.length() % 2 == 1) {
/* 19 */       str = str + 'F';
/*    */     }
/*    */     
/* 22 */     byte[] ret = new byte[str.length() / 2];
/*    */     
/* 24 */     for (int i = 0; i < ret.length; i++) {
/* 25 */       String bs = str.substring(2 * i, 2 * i + 2);
/* 26 */       ret[i] = ((byte)Integer.parseInt(bs, 16));
/*    */     }
/*    */     
/* 29 */     return ret;
/*    */   }
/*    */   
/* 32 */   public static void main(String[] args) { byte[] d = 
/* 33 */       getByteArray("ad0e");
/* 34 */     System.out.println(Arrays.toString(d));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static byte[] encryption(byte[] data)
/*    */   {
/* 44 */     byte[] cods = { 54, 8, -10, 19, -30, 13, 71, -96 };
/* 45 */     int codsi = 0;
/*    */     
/* 47 */     for (int i = 0; i < data.length; i++)
/*    */     {
/* 49 */       int tmp56_55 = i;data[tmp56_55] = ((byte)(data[tmp56_55] ^ cods[codsi]));
/* 50 */       codsi++;
/* 51 */       if (codsi >= cods.length) codsi = 0;
/*    */     }
/* 53 */     return data;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public static void writeByte2Stream(byte[] data, java.io.OutputStream os)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: aload_0
/*    */     //   2: invokevirtual 107	java/io/OutputStream:write	([B)V
/*    */     //   5: aload_1
/*    */     //   6: invokevirtual 113	java/io/OutputStream:flush	()V
/*    */     //   9: goto +110 -> 119
/*    */     //   12: astore_2
/*    */     //   13: aload_2
/*    */     //   14: ifnull +15 -> 29
/*    */     //   17: aload_2
/*    */     //   18: invokevirtual 116	java/io/IOException:toString	()Ljava/lang/String;
/*    */     //   21: ldc 119
/*    */     //   23: invokevirtual 121	java/lang/String:indexOf	(Ljava/lang/String;)I
/*    */     //   26: ifge +12 -> 38
/*    */     //   29: new 125	java/lang/RuntimeException
/*    */     //   32: dup
/*    */     //   33: aload_2
/*    */     //   34: invokespecial 127	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   37: athrow
/*    */     //   38: aload_1
/*    */     //   39: ifnull +119 -> 158
/*    */     //   42: aload_1
/*    */     //   43: invokevirtual 130	java/io/OutputStream:close	()V
/*    */     //   46: goto +112 -> 158
/*    */     //   49: astore 4
/*    */     //   51: aload 4
/*    */     //   53: ifnull +14 -> 67
/*    */     //   56: aload 4
/*    */     //   58: invokevirtual 116	java/io/IOException:toString	()Ljava/lang/String;
/*    */     //   61: ldc 119
/*    */     //   63: invokevirtual 121	java/lang/String:indexOf	(Ljava/lang/String;)I
/*    */     //   66: pop
/*    */     //   67: new 125	java/lang/RuntimeException
/*    */     //   70: dup
/*    */     //   71: aload 4
/*    */     //   73: invokespecial 127	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   76: athrow
/*    */     //   77: astore_3
/*    */     //   78: aload_1
/*    */     //   79: ifnull +38 -> 117
/*    */     //   82: aload_1
/*    */     //   83: invokevirtual 130	java/io/OutputStream:close	()V
/*    */     //   86: goto +31 -> 117
/*    */     //   89: astore 4
/*    */     //   91: aload 4
/*    */     //   93: ifnull +14 -> 107
/*    */     //   96: aload 4
/*    */     //   98: invokevirtual 116	java/io/IOException:toString	()Ljava/lang/String;
/*    */     //   101: ldc 119
/*    */     //   103: invokevirtual 121	java/lang/String:indexOf	(Ljava/lang/String;)I
/*    */     //   106: pop
/*    */     //   107: new 125	java/lang/RuntimeException
/*    */     //   110: dup
/*    */     //   111: aload 4
/*    */     //   113: invokespecial 127	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   116: athrow
/*    */     //   117: aload_3
/*    */     //   118: athrow
/*    */     //   119: aload_1
/*    */     //   120: ifnull +38 -> 158
/*    */     //   123: aload_1
/*    */     //   124: invokevirtual 130	java/io/OutputStream:close	()V
/*    */     //   127: goto +31 -> 158
/*    */     //   130: astore 4
/*    */     //   132: aload 4
/*    */     //   134: ifnull +14 -> 148
/*    */     //   137: aload 4
/*    */     //   139: invokevirtual 116	java/io/IOException:toString	()Ljava/lang/String;
/*    */     //   142: ldc 119
/*    */     //   144: invokevirtual 121	java/lang/String:indexOf	(Ljava/lang/String;)I
/*    */     //   147: pop
/*    */     //   148: new 125	java/lang/RuntimeException
/*    */     //   151: dup
/*    */     //   152: aload 4
/*    */     //   154: invokespecial 127	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   157: athrow
/*    */     //   158: return
/*    */     // Line number table:
/*    */     //   Java source line #59	-> byte code offset #0
/*    */     //   Java source line #60	-> byte code offset #5
/*    */     //   Java source line #62	-> byte code offset #12
/*    */     //   Java source line #64	-> byte code offset #13
/*    */     //   Java source line #70	-> byte code offset #29
/*    */     //   Java source line #75	-> byte code offset #38
/*    */     //   Java source line #79	-> byte code offset #42
/*    */     //   Java source line #81	-> byte code offset #49
/*    */     //   Java source line #83	-> byte code offset #51
/*    */     //   Java source line #87	-> byte code offset #67
/*    */     //   Java source line #74	-> byte code offset #77
/*    */     //   Java source line #75	-> byte code offset #78
/*    */     //   Java source line #79	-> byte code offset #82
/*    */     //   Java source line #81	-> byte code offset #89
/*    */     //   Java source line #83	-> byte code offset #91
/*    */     //   Java source line #87	-> byte code offset #107
/*    */     //   Java source line #90	-> byte code offset #117
/*    */     //   Java source line #75	-> byte code offset #119
/*    */     //   Java source line #79	-> byte code offset #123
/*    */     //   Java source line #81	-> byte code offset #130
/*    */     //   Java source line #83	-> byte code offset #132
/*    */     //   Java source line #87	-> byte code offset #148
/*    */     //   Java source line #91	-> byte code offset #158
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	159	0	data	byte[]
/*    */     //   0	159	1	os	java.io.OutputStream
/*    */     //   12	22	2	e	java.io.IOException
/*    */     //   77	41	3	localObject	Object
/*    */     //   49	23	4	e	java.io.IOException
/*    */     //   89	23	4	e	java.io.IOException
/*    */     //   130	23	4	e	java.io.IOException
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	9	12	java/io/IOException
/*    */     //   42	46	49	java/io/IOException
/*    */     //   0	38	77	finally
/*    */     //   82	86	89	java/io/IOException
/*    */     //   123	127	130	java/io/IOException
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\ByteUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */