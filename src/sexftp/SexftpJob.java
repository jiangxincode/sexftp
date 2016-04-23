/*     */ package sexftp;
/*     */ 
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.runtime.jobs.Job;
/*     */ import org.sexftp.core.ftp.FtpPools;
/*     */ import sexftp.uils.LogUtil;
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
/*     */ public abstract class SexftpJob
/*     */   extends Job
/*     */   implements Runnable
/*     */ {
/*     */   private SrcViewable secview;
/*     */   private IProgressMonitor monitor;
/*     */   private Thread curThread;
/*  30 */   private boolean fineshed = false;
/*     */   
/*     */   public SexftpJob(String name, SrcViewable secview) {
/*  33 */     super("Sexftp Job - " + name);
/*  34 */     this.secview = secview;
/*     */   }
/*     */   
/*     */   public class SexftpProgressMonitor implements IProgressMonitor {
/*  38 */     IProgressMonitor monitor = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public SexftpProgressMonitor(IProgressMonitor monitor)
/*     */     {
/*  45 */       this.monitor = monitor;
/*     */     }
/*     */     
/*     */     public void beginTask(String arg0, int arg1) {
/*  49 */       this.monitor.beginTask(arg0, arg1);
/*     */     }
/*     */     
/*     */     public void done() {
/*  53 */       this.monitor.done();
/*     */     }
/*     */     
/*     */     public void internalWorked(double arg0) {
/*  57 */       this.monitor.internalWorked(arg0);
/*     */     }
/*     */     
/*     */     public boolean isCanceled() {
/*  61 */       return this.monitor.isCanceled();
/*     */     }
/*     */     
/*     */     public void setCanceled(boolean arg0) {
/*  65 */       this.monitor.setCanceled(arg0);
/*     */     }
/*     */     
/*     */ 
/*  69 */     public void setTaskName(String arg0) { this.monitor.setTaskName(arg0); }
/*     */     
/*  71 */     private long nexoptime = 0L;
/*     */     
/*  73 */     public void subTask(String arg0) { long currentTimeMillis = System.currentTimeMillis();
/*  74 */       if (currentTimeMillis > this.nexoptime)
/*     */       {
/*  76 */         this.monitor.subTask(arg0);
/*  77 */         this.nexoptime = (currentTimeMillis + 500L);
/*     */       }
/*     */     }
/*     */     
/*     */     public void worked(int arg0) {
/*  82 */       this.monitor.worked(arg0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IStatus run(IProgressMonitor monitor)
/*     */   {
/*     */     try {
/*  89 */       if (this.monitor == null) this.monitor = monitor;
/*  90 */       if (this.curThread == null) {
/*  91 */         this.curThread = Thread.currentThread();
/*  92 */         new Thread(this).start();
/*     */       }
/*     */       
/*  95 */       IStatus srun = srun(new SexftpProgressMonitor(monitor));
/*     */       
/*  97 */       return srun;
/*     */     } catch (Throwable e) { IStatus localIStatus1;
/*  99 */       this.secview.handleException(e);
/* 100 */       return Status.CANCEL_STATUS;
/*     */     }
/*     */     finally {
/* 103 */       this.fineshed = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 110 */     for (int i = 0; i < 50000; i++) {
/*     */       try
/*     */       {
/* 113 */         Thread.sleep(1000L);
/* 114 */         if (this.fineshed) {
/*     */           break;
/*     */         }
/*     */         
/* 118 */         if ((this.monitor != null) && (this.monitor.isCanceled()))
/*     */         {
/* 120 */           Thread.sleep(5000L);
/* 121 */           if (this.fineshed) break;
/* 122 */           new FtpPools(null, null).disconnectAll();
/* 123 */           LogUtil.info("Canceled Time Out,Disconnect All.");
/*     */         }
/*     */       } catch (InterruptedException e) {
/* 126 */         LogUtil.error(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract IStatus srun(IProgressMonitor paramIProgressMonitor)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\SexftpJob.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */