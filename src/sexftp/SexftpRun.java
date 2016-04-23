/*    */ package sexftp;
/*    */ 
/*    */ import org.eclipse.core.runtime.IProgressMonitor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SexftpRun
/*    */   implements Runnable
/*    */ {
/*    */   private SrcViewable secview;
/*    */   private Object returnObject;
/*    */   private IProgressMonitor monitor;
/*    */   
/*    */   public SexftpRun(SrcViewable secview)
/*    */   {
/* 21 */     this.secview = secview;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SexftpRun(SrcViewable secview, IProgressMonitor monitor)
/*    */   {
/* 32 */     this.secview = secview;
/* 33 */     this.monitor = monitor;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setSecview(SrcViewable secview)
/*    */   {
/* 39 */     this.secview = secview;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setMonitor(IProgressMonitor monitor)
/*    */   {
/* 45 */     this.monitor = monitor;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokevirtual 32	sexftp/SexftpRun:srun	()V
/*    */     //   4: goto +56 -> 60
/*    */     //   7: astore_1
/*    */     //   8: aload_0
/*    */     //   9: getfield 19	sexftp/SexftpRun:secview	Lsexftp/SrcViewable;
/*    */     //   12: aload_1
/*    */     //   13: invokeinterface 35 2 0
/*    */     //   18: aload_0
/*    */     //   19: invokevirtual 41	sexftp/SexftpRun:sfinally	()V
/*    */     //   22: goto +56 -> 78
/*    */     //   25: astore_3
/*    */     //   26: aload_0
/*    */     //   27: getfield 19	sexftp/SexftpRun:secview	Lsexftp/SrcViewable;
/*    */     //   30: aload_3
/*    */     //   31: invokeinterface 35 2 0
/*    */     //   36: goto +42 -> 78
/*    */     //   39: astore_2
/*    */     //   40: aload_0
/*    */     //   41: invokevirtual 41	sexftp/SexftpRun:sfinally	()V
/*    */     //   44: goto +14 -> 58
/*    */     //   47: astore_3
/*    */     //   48: aload_0
/*    */     //   49: getfield 19	sexftp/SexftpRun:secview	Lsexftp/SrcViewable;
/*    */     //   52: aload_3
/*    */     //   53: invokeinterface 35 2 0
/*    */     //   58: aload_2
/*    */     //   59: athrow
/*    */     //   60: aload_0
/*    */     //   61: invokevirtual 41	sexftp/SexftpRun:sfinally	()V
/*    */     //   64: goto +14 -> 78
/*    */     //   67: astore_3
/*    */     //   68: aload_0
/*    */     //   69: getfield 19	sexftp/SexftpRun:secview	Lsexftp/SrcViewable;
/*    */     //   72: aload_3
/*    */     //   73: invokeinterface 35 2 0
/*    */     //   78: return
/*    */     // Line number table:
/*    */     //   Java source line #52	-> byte code offset #0
/*    */     //   Java source line #53	-> byte code offset #7
/*    */     //   Java source line #54	-> byte code offset #8
/*    */     //   Java source line #58	-> byte code offset #18
/*    */     //   Java source line #59	-> byte code offset #25
/*    */     //   Java source line #60	-> byte code offset #26
/*    */     //   Java source line #56	-> byte code offset #39
/*    */     //   Java source line #58	-> byte code offset #40
/*    */     //   Java source line #59	-> byte code offset #47
/*    */     //   Java source line #60	-> byte code offset #48
/*    */     //   Java source line #62	-> byte code offset #58
/*    */     //   Java source line #58	-> byte code offset #60
/*    */     //   Java source line #59	-> byte code offset #67
/*    */     //   Java source line #60	-> byte code offset #68
/*    */     //   Java source line #63	-> byte code offset #78
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	79	0	this	SexftpRun
/*    */     //   7	6	1	e	Exception
/*    */     //   39	20	2	localObject	Object
/*    */     //   25	6	3	e	Exception
/*    */     //   47	6	3	e	Exception
/*    */     //   67	6	3	e	Exception
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	4	7	java/lang/Exception
/*    */     //   18	22	25	java/lang/Exception
/*    */     //   0	18	39	finally
/*    */     //   40	44	47	java/lang/Exception
/*    */     //   60	64	67	java/lang/Exception
/*    */   }
/*    */   
/*    */   public IProgressMonitor getMonitor()
/*    */   {
/* 67 */     return this.monitor;
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract void srun()
/*    */     throws Exception;
/*    */   
/*    */ 
/*    */   public final void srun(IProgressMonitor monitor)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   protected void sfinally()
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public Object getReturnObject()
/*    */   {
/* 85 */     return this.returnObject;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setReturnObject(Object returnObject)
/*    */   {
/* 91 */     this.returnObject = returnObject;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\SexftpRun.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */