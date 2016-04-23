/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class UrlUtil
/*    */ {
/*    */   /* Error */
/*    */   public static byte[] requestUrlData(String url, byte[] requestdata)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aconst_null
/*    */     //   1: astore_2
/*    */     //   2: aconst_null
/*    */     //   3: astore_3
/*    */     //   4: new 16	java/net/URL
/*    */     //   7: dup
/*    */     //   8: aload_0
/*    */     //   9: invokespecial 18	java/net/URL:<init>	(Ljava/lang/String;)V
/*    */     //   12: invokevirtual 21	java/net/URL:openConnection	()Ljava/net/URLConnection;
/*    */     //   15: checkcast 25	java/net/HttpURLConnection
/*    */     //   18: astore 5
/*    */     //   20: aload 5
/*    */     //   22: ldc 27
/*    */     //   24: invokevirtual 28	java/net/HttpURLConnection:setReadTimeout	(I)V
/*    */     //   27: aload 5
/*    */     //   29: iconst_1
/*    */     //   30: invokevirtual 32	java/net/HttpURLConnection:setDoOutput	(Z)V
/*    */     //   33: aload 5
/*    */     //   35: iconst_1
/*    */     //   36: invokevirtual 36	java/net/HttpURLConnection:setDoInput	(Z)V
/*    */     //   39: aload 5
/*    */     //   41: iconst_0
/*    */     //   42: invokevirtual 39	java/net/HttpURLConnection:setUseCaches	(Z)V
/*    */     //   45: aload 5
/*    */     //   47: ldc 42
/*    */     //   49: invokevirtual 44	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
/*    */     //   52: aload 5
/*    */     //   54: ldc 47
/*    */     //   56: ldc 49
/*    */     //   58: invokevirtual 51	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
/*    */     //   61: aload_1
/*    */     //   62: arraylength
/*    */     //   63: ifle +31 -> 94
/*    */     //   66: aload_0
/*    */     //   67: ldc 55
/*    */     //   69: invokevirtual 57	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*    */     //   72: ifne +22 -> 94
/*    */     //   75: aload 5
/*    */     //   77: invokevirtual 63	java/net/HttpURLConnection:getOutputStream	()Ljava/io/OutputStream;
/*    */     //   80: astore_2
/*    */     //   81: aload_2
/*    */     //   82: aload_1
/*    */     //   83: invokevirtual 67	java/io/OutputStream:write	([B)V
/*    */     //   86: aload_2
/*    */     //   87: invokevirtual 73	java/io/OutputStream:flush	()V
/*    */     //   90: aload_2
/*    */     //   91: invokevirtual 76	java/io/OutputStream:close	()V
/*    */     //   94: aload 5
/*    */     //   96: invokevirtual 79	java/net/HttpURLConnection:connect	()V
/*    */     //   99: aload 5
/*    */     //   101: invokevirtual 82	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
/*    */     //   104: astore_3
/*    */     //   105: new 86	java/io/ByteArrayOutputStream
/*    */     //   108: dup
/*    */     //   109: invokespecial 88	java/io/ByteArrayOutputStream:<init>	()V
/*    */     //   112: astore 6
/*    */     //   114: sipush 2048
/*    */     //   117: newarray <illegal type>
/*    */     //   119: astore 7
/*    */     //   121: iconst_0
/*    */     //   122: istore 8
/*    */     //   124: goto +13 -> 137
/*    */     //   127: aload 6
/*    */     //   129: aload 7
/*    */     //   131: iconst_0
/*    */     //   132: iload 8
/*    */     //   134: invokevirtual 89	java/io/ByteArrayOutputStream:write	([BII)V
/*    */     //   137: aload_3
/*    */     //   138: aload 7
/*    */     //   140: invokevirtual 92	java/io/InputStream:read	([B)I
/*    */     //   143: dup
/*    */     //   144: istore 8
/*    */     //   146: iconst_m1
/*    */     //   147: if_icmpne -20 -> 127
/*    */     //   150: aload 6
/*    */     //   152: invokevirtual 98	java/io/ByteArrayOutputStream:toByteArray	()[B
/*    */     //   155: astore 4
/*    */     //   157: aload_3
/*    */     //   158: invokevirtual 102	java/io/InputStream:close	()V
/*    */     //   161: goto +52 -> 213
/*    */     //   164: astore 5
/*    */     //   166: new 103	org/sexftp/core/exceptions/SFConnectionException
/*    */     //   169: dup
/*    */     //   170: aload 5
/*    */     //   172: invokespecial 105	org/sexftp/core/exceptions/SFConnectionException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   175: athrow
/*    */     //   176: astore 5
/*    */     //   178: new 108	java/lang/RuntimeException
/*    */     //   181: dup
/*    */     //   182: aload 5
/*    */     //   184: invokespecial 110	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   187: athrow
/*    */     //   188: astore 9
/*    */     //   190: aload_2
/*    */     //   191: ifnull +7 -> 198
/*    */     //   194: aload_2
/*    */     //   195: invokevirtual 76	java/io/OutputStream:close	()V
/*    */     //   198: aload_3
/*    */     //   199: ifnull +11 -> 210
/*    */     //   202: aload_3
/*    */     //   203: invokevirtual 102	java/io/InputStream:close	()V
/*    */     //   206: goto +4 -> 210
/*    */     //   209: pop
/*    */     //   210: aload 9
/*    */     //   212: athrow
/*    */     //   213: aload_2
/*    */     //   214: ifnull +7 -> 221
/*    */     //   217: aload_2
/*    */     //   218: invokevirtual 76	java/io/OutputStream:close	()V
/*    */     //   221: aload_3
/*    */     //   222: ifnull +11 -> 233
/*    */     //   225: aload_3
/*    */     //   226: invokevirtual 102	java/io/InputStream:close	()V
/*    */     //   229: goto +4 -> 233
/*    */     //   232: pop
/*    */     //   233: aload 4
/*    */     //   235: areturn
/*    */     // Line number table:
/*    */     //   Java source line #22	-> byte code offset #0
/*    */     //   Java source line #23	-> byte code offset #2
/*    */     //   Java source line #27	-> byte code offset #4
/*    */     //   Java source line #29	-> byte code offset #20
/*    */     //   Java source line #30	-> byte code offset #27
/*    */     //   Java source line #31	-> byte code offset #33
/*    */     //   Java source line #32	-> byte code offset #39
/*    */     //   Java source line #33	-> byte code offset #45
/*    */     //   Java source line #34	-> byte code offset #52
/*    */     //   Java source line #35	-> byte code offset #61
/*    */     //   Java source line #37	-> byte code offset #75
/*    */     //   Java source line #38	-> byte code offset #81
/*    */     //   Java source line #39	-> byte code offset #86
/*    */     //   Java source line #40	-> byte code offset #90
/*    */     //   Java source line #43	-> byte code offset #94
/*    */     //   Java source line #44	-> byte code offset #99
/*    */     //   Java source line #46	-> byte code offset #105
/*    */     //   Java source line #47	-> byte code offset #114
/*    */     //   Java source line #48	-> byte code offset #121
/*    */     //   Java source line #49	-> byte code offset #124
/*    */     //   Java source line #51	-> byte code offset #127
/*    */     //   Java source line #49	-> byte code offset #137
/*    */     //   Java source line #55	-> byte code offset #150
/*    */     //   Java source line #57	-> byte code offset #157
/*    */     //   Java source line #59	-> byte code offset #164
/*    */     //   Java source line #60	-> byte code offset #166
/*    */     //   Java source line #62	-> byte code offset #176
/*    */     //   Java source line #64	-> byte code offset #178
/*    */     //   Java source line #67	-> byte code offset #188
/*    */     //   Java source line #70	-> byte code offset #190
/*    */     //   Java source line #71	-> byte code offset #198
/*    */     //   Java source line #73	-> byte code offset #209
/*    */     //   Java source line #77	-> byte code offset #210
/*    */     //   Java source line #70	-> byte code offset #213
/*    */     //   Java source line #71	-> byte code offset #221
/*    */     //   Java source line #73	-> byte code offset #232
/*    */     //   Java source line #79	-> byte code offset #233
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	236	0	url	String
/*    */     //   0	236	1	requestdata	byte[]
/*    */     //   1	217	2	outputStream	java.io.OutputStream
/*    */     //   3	223	3	in	java.io.InputStream
/*    */     //   155	3	4	respDatas	byte[]
/*    */     //   213	1	4	respDatas	byte[]
/*    */     //   233	1	4	respDatas	byte[]
/*    */     //   18	82	5	urlConnection	java.net.HttpURLConnection
/*    */     //   164	7	5	e	java.net.ConnectException
/*    */     //   176	7	5	e	Exception
/*    */     //   112	39	6	baos	java.io.ByteArrayOutputStream
/*    */     //   119	20	7	buf	byte[]
/*    */     //   122	23	8	len	int
/*    */     //   188	23	9	localObject	Object
/*    */     //   209	1	14	localIOException1	java.io.IOException
/*    */     //   232	1	15	localIOException2	java.io.IOException
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   4	161	164	java/net/ConnectException
/*    */     //   4	161	176	java/lang/Exception
/*    */     //   4	188	188	finally
/*    */     //   190	206	209	java/io/IOException
/*    */     //   213	229	232	java/io/IOException
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 83 */     byte[] get = requestUrlData("http://deveasy.googlecode.com/svn/trunk/desy_dev/sexftp/genupdate.htm", new byte[1]);
/* 84 */     System.out.println(new String(get));
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\UrlUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */