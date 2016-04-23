package examples.nntp;

public final class ListNewsgroups
{
  /* Error */
  public static final void main(String[] args)
  {
    // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: iconst_1
    //   3: if_icmpge +12 -> 15
    //   6: getstatic 16	java/lang/System:err	Ljava/io/PrintStream;
    //   9: ldc 22
    //   11: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   14: return
    //   15: new 30	org/apache/commons/net/nntp/NNTPClient
    //   18: dup
    //   19: invokespecial 32	org/apache/commons/net/nntp/NNTPClient:<init>	()V
    //   22: astore_1
    //   23: aload_0
    //   24: arraylength
    //   25: iconst_2
    //   26: if_icmplt +9 -> 35
    //   29: aload_0
    //   30: iconst_1
    //   31: aaload
    //   32: goto +5 -> 37
    //   35: ldc 33
    //   37: astore_2
    //   38: aload_1
    //   39: aload_0
    //   40: iconst_0
    //   41: aaload
    //   42: invokevirtual 35	org/apache/commons/net/nntp/NNTPClient:connect	(Ljava/lang/String;)V
    //   45: iconst_0
    //   46: istore_3
    //   47: aload_1
    //   48: aload_2
    //   49: invokevirtual 38	org/apache/commons/net/nntp/NNTPClient:iterateNewsgroupListing	(Ljava/lang/String;)Ljava/lang/Iterable;
    //   52: invokeinterface 42 1 0
    //   57: astore 5
    //   59: goto +26 -> 85
    //   62: aload 5
    //   64: invokeinterface 48 1 0
    //   69: checkcast 54	java/lang/String
    //   72: astore 4
    //   74: iinc 3 1
    //   77: getstatic 56	java/lang/System:out	Ljava/io/PrintStream;
    //   80: aload 4
    //   82: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   85: aload 5
    //   87: invokeinterface 59 1 0
    //   92: ifne -30 -> 62
    //   95: goto +10 -> 105
    //   98: astore 4
    //   100: aload 4
    //   102: invokevirtual 63	java/io/IOException:printStackTrace	()V
    //   105: getstatic 56	java/lang/System:out	Ljava/io/PrintStream;
    //   108: iload_3
    //   109: invokevirtual 68	java/io/PrintStream:println	(I)V
    //   112: iconst_0
    //   113: istore_3
    //   114: aload_1
    //   115: aload_2
    //   116: invokevirtual 71	org/apache/commons/net/nntp/NNTPClient:iterateNewsgroups	(Ljava/lang/String;)Ljava/lang/Iterable;
    //   119: invokeinterface 42 1 0
    //   124: astore 5
    //   126: goto +29 -> 155
    //   129: aload 5
    //   131: invokeinterface 48 1 0
    //   136: checkcast 74	org/apache/commons/net/nntp/NewsgroupInfo
    //   139: astore 4
    //   141: iinc 3 1
    //   144: getstatic 56	java/lang/System:out	Ljava/io/PrintStream;
    //   147: aload 4
    //   149: invokevirtual 76	org/apache/commons/net/nntp/NewsgroupInfo:getNewsgroup	()Ljava/lang/String;
    //   152: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   155: aload 5
    //   157: invokeinterface 59 1 0
    //   162: ifne -33 -> 129
    //   165: getstatic 56	java/lang/System:out	Ljava/io/PrintStream;
    //   168: iload_3
    //   169: invokevirtual 68	java/io/PrintStream:println	(I)V
    //   172: goto +82 -> 254
    //   175: astore_3
    //   176: aload_3
    //   177: invokevirtual 63	java/io/IOException:printStackTrace	()V
    //   180: aload_1
    //   181: invokevirtual 80	org/apache/commons/net/nntp/NNTPClient:isConnected	()Z
    //   184: ifeq +103 -> 287
    //   187: aload_1
    //   188: invokevirtual 83	org/apache/commons/net/nntp/NNTPClient:disconnect	()V
    //   191: goto +96 -> 287
    //   194: astore 7
    //   196: getstatic 16	java/lang/System:err	Ljava/io/PrintStream;
    //   199: ldc 86
    //   201: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   204: aload 7
    //   206: invokevirtual 63	java/io/IOException:printStackTrace	()V
    //   209: iconst_1
    //   210: invokestatic 88	java/lang/System:exit	(I)V
    //   213: goto +74 -> 287
    //   216: astore 6
    //   218: aload_1
    //   219: invokevirtual 80	org/apache/commons/net/nntp/NNTPClient:isConnected	()Z
    //   222: ifeq +29 -> 251
    //   225: aload_1
    //   226: invokevirtual 83	org/apache/commons/net/nntp/NNTPClient:disconnect	()V
    //   229: goto +22 -> 251
    //   232: astore 7
    //   234: getstatic 16	java/lang/System:err	Ljava/io/PrintStream;
    //   237: ldc 86
    //   239: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   242: aload 7
    //   244: invokevirtual 63	java/io/IOException:printStackTrace	()V
    //   247: iconst_1
    //   248: invokestatic 88	java/lang/System:exit	(I)V
    //   251: aload 6
    //   253: athrow
    //   254: aload_1
    //   255: invokevirtual 80	org/apache/commons/net/nntp/NNTPClient:isConnected	()Z
    //   258: ifeq +29 -> 287
    //   261: aload_1
    //   262: invokevirtual 83	org/apache/commons/net/nntp/NNTPClient:disconnect	()V
    //   265: goto +22 -> 287
    //   268: astore 7
    //   270: getstatic 16	java/lang/System:err	Ljava/io/PrintStream;
    //   273: ldc 86
    //   275: invokevirtual 24	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   278: aload 7
    //   280: invokevirtual 63	java/io/IOException:printStackTrace	()V
    //   283: iconst_1
    //   284: invokestatic 88	java/lang/System:exit	(I)V
    //   287: return
    // Line number table:
    //   Java source line #38	-> byte code offset #0
    //   Java source line #40	-> byte code offset #6
    //   Java source line #41	-> byte code offset #14
    //   Java source line #44	-> byte code offset #15
    //   Java source line #45	-> byte code offset #23
    //   Java source line #49	-> byte code offset #38
    //   Java source line #51	-> byte code offset #45
    //   Java source line #53	-> byte code offset #47
    //   Java source line #54	-> byte code offset #74
    //   Java source line #55	-> byte code offset #77
    //   Java source line #53	-> byte code offset #85
    //   Java source line #57	-> byte code offset #98
    //   Java source line #58	-> byte code offset #100
    //   Java source line #60	-> byte code offset #105
    //   Java source line #62	-> byte code offset #112
    //   Java source line #63	-> byte code offset #114
    //   Java source line #64	-> byte code offset #141
    //   Java source line #65	-> byte code offset #144
    //   Java source line #63	-> byte code offset #155
    //   Java source line #67	-> byte code offset #165
    //   Java source line #69	-> byte code offset #175
    //   Java source line #71	-> byte code offset #176
    //   Java source line #77	-> byte code offset #180
    //   Java source line #78	-> byte code offset #187
    //   Java source line #80	-> byte code offset #194
    //   Java source line #82	-> byte code offset #196
    //   Java source line #83	-> byte code offset #204
    //   Java source line #84	-> byte code offset #209
    //   Java source line #74	-> byte code offset #216
    //   Java source line #77	-> byte code offset #218
    //   Java source line #78	-> byte code offset #225
    //   Java source line #80	-> byte code offset #232
    //   Java source line #82	-> byte code offset #234
    //   Java source line #83	-> byte code offset #242
    //   Java source line #84	-> byte code offset #247
    //   Java source line #86	-> byte code offset #251
    //   Java source line #77	-> byte code offset #254
    //   Java source line #78	-> byte code offset #261
    //   Java source line #80	-> byte code offset #268
    //   Java source line #82	-> byte code offset #270
    //   Java source line #83	-> byte code offset #278
    //   Java source line #84	-> byte code offset #283
    //   Java source line #88	-> byte code offset #287
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	args	String[]
    //   22	240	1	client	org.apache.commons.net.nntp.NNTPClient
    //   37	79	2	pattern	String
    //   46	123	3	j	int
    //   175	2	3	e	java.io.IOException
    //   72	9	4	s	String
    //   98	3	4	e1	java.io.IOException
    //   139	9	4	n	org.apache.commons.net.nntp.NewsgroupInfo
    //   57	99	5	localIterator	java.util.Iterator
    //   216	36	6	localObject	Object
    //   194	11	7	e	java.io.IOException
    //   232	11	7	e	java.io.IOException
    //   268	11	7	e	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   47	95	98	java/io/IOException
    //   38	172	175	java/io/IOException
    //   180	191	194	java/io/IOException
    //   38	180	216	finally
    //   218	229	232	java/io/IOException
    //   254	265	268	java/io/IOException
  }
}


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\nntp\ListNewsgroups.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */