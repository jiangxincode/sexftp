/*     */ package sexftp.views.savelisteners;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.runtime.jobs.Job;
/*     */ import org.sexftp.core.ftp.FileMd5;
/*     */ import org.sexftp.core.ftp.FtpPools;
/*     */ import org.sexftp.core.ftp.XFtp;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ import org.sexftp.core.utils.StringUtil;
/*     */ import sexftp.editors.viewlis.IDoSaveListener;
/*     */ import sexftp.views.AbstractSexftpView;
/*     */ import sexftp.views.IFtpStreamMonitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerInnnerEditSaveListener
/*     */   implements IDoSaveListener
/*     */ {
/*     */   private IFile ifile;
/*     */   private String serverDirPath;
/*     */   private String serverFileName;
/*     */   private AbstractSexftpView srcview;
/*     */   private FtpPools ftppool;
/*  34 */   private String md5 = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long MAXTIME = 18000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerInnnerEditSaveListener(IFile ifile, String serverDirPath, String serverFileName, AbstractSexftpView srcview, FtpPools ftppool)
/*     */   {
/*  47 */     this.ifile = ifile;
/*  48 */     this.serverDirPath = serverDirPath;
/*  49 */     this.serverFileName = serverFileName;
/*  50 */     this.srcview = srcview;
/*  51 */     this.ftppool = ftppool;
/*  52 */     this.md5 = FileMd5.getMD5(ifile.getLocation().toFile());
/*     */   }
/*     */   
/*  55 */   public synchronized void dispose() { File file = this.ifile.getLocation().toFile();
/*  56 */     if (!FileMd5.getMD5(file).equals(this.md5))
/*     */     {
/*  58 */       oksave();
/*     */     }
/*  60 */     deleteTmpFolder(file.getParentFile().getParentFile());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   public synchronized void dosave() { oksave(); }
/*     */   
/*     */   private void deleteTmpFolder(File folder) {
/*     */     File[] arrayOfFile1;
/*  71 */     int j = (arrayOfFile1 = folder.listFiles()).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile1[i];
/*     */       
/*  73 */       Long l = new Long(subfile.getName());
/*  74 */       if (System.currentTimeMillis() - l.longValue() > 18000000L) {
/*     */         File[] arrayOfFile2;
/*  76 */         int m = (arrayOfFile2 = subfile.listFiles()).length; for (int k = 0; k < m; k++) { File subsubfile = arrayOfFile2[k];
/*     */           
/*  78 */           subsubfile.delete();
/*     */         }
/*  80 */         subfile.delete();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void oksave()
/*     */   {
/*  87 */     if (this.srcview.showQuestion("Upload Current Edit File [" + this.serverFileName + "] To Server?"))
/*     */     {
/*     */ 
/*  90 */       Job job = new Job("uploading")
/*     */       {
/*     */         protected IStatus run(final IProgressMonitor monitor)
/*     */         {
/*  94 */           monitor.beginTask("uploading..", -1);
/*  95 */           XFtp ftp = ServerInnnerEditSaveListener.this.ftppool.getFtp();
/*  96 */           synchronized (ftp) {
/*  97 */             ServerInnnerEditSaveListener.this.ftppool.getConnectedFtp();
/*  98 */             ftp.cd(ServerInnnerEditSaveListener.this.serverDirPath);
/*     */             
/* 100 */             String uploadFile = ServerInnnerEditSaveListener.this.ifile.getLocation().toFile().getParent() + "/" + ServerInnnerEditSaveListener.this.serverFileName;
/* 101 */             if (!ServerInnnerEditSaveListener.this.ifile.getLocation().toFile().getName().equals(ServerInnnerEditSaveListener.this.serverFileName))
/*     */             {
/* 103 */               FileUtil.copyFile(ServerInnnerEditSaveListener.this.ifile.getLocation().toFile().getAbsolutePath(), uploadFile);
/*     */             }
/*     */             
/* 106 */             ftp.upload(uploadFile, new IFtpStreamMonitor()
/*     */             {
/*     */ 
/*     */               public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize, long totalSize, String info)
/*     */               {
/* 111 */                 monitor.subTask("uploaded:" + StringUtil.getHumanSize(uploadedSize));
/*     */               }
/*     */               
/*     */               public void printSimple(String info)
/*     */               {
/* 116 */                 ServerInnnerEditSaveListener.this.srcview.console(info);
/*     */               }
/*     */             });
/*     */           }
/* 120 */           ServerInnnerEditSaveListener.this.md5 = FileMd5.getMD5(ServerInnnerEditSaveListener.this.ifile.getLocation().toFile());
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */           return Status.OK_STATUS;
/*     */         }
/* 128 */       };
/* 129 */       job.setUser(true);
/* 130 */       job.schedule();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\savelisteners\ServerInnnerEditSaveListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */