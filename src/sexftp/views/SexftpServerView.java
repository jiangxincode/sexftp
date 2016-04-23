/*     */ package sexftp.views;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.desy.common.util.DateTimeUtils;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.runtime.content.IContentType;
/*     */ import org.eclipse.core.runtime.jobs.Job;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.dialogs.MessageDialogWithToggle;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.TreeExpansionEvent;
/*     */ import org.eclipse.jface.viewers.TreeViewer;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.ide.IDE;
/*     */ import org.eclipse.ui.part.FileEditorInput;
/*     */ import org.sexftp.core.exceptions.AbortException;
/*     */ import org.sexftp.core.exceptions.BizException;
/*     */ import org.sexftp.core.ftp.FtpPools;
/*     */ import org.sexftp.core.ftp.XFtp;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ import org.sexftp.core.ftp.bean.FtpDownloadPro;
/*     */ import org.sexftp.core.ftp.bean.FtpFile;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.utils.Cpdetector;
/*     */ import org.sexftp.core.utils.ExistFtpFile;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ import org.sexftp.core.utils.SearchCallback;
/*     */ import org.sexftp.core.utils.StringUtil;
/*     */ import org.sexftp.core.utils.TreeViewUtil;
/*     */ import org.sexftp.core.utils.TreeViewUtil.ThisYourFind;
/*     */ import sexftp.SexftpJob;
/*     */ import sexftp.SexftpRun;
/*     */ import sexftp.SrcViewable;
/*     */ import sexftp.editors.inner.SfTextEditor;
/*     */ import sexftp.uils.PluginUtil;
/*     */ import sexftp.views.savelisteners.ServerInnnerEditSaveListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SexftpServerView
/*     */   extends SexftpMainView
/*     */ {
/*     */   protected void actionPrepare()
/*     */   {
/*  64 */     this.actionCompare.setText("Compare With Local(&C)");
/*  65 */     super.actionPrepare();
/*     */   }
/*     */   
/*     */   protected String getDefaultPathToLocation(Object selectO) {
/*  69 */     if ((selectO instanceof FtpDownloadPro))
/*     */     {
/*  71 */       return ((FtpDownloadPro)selectO).getFtpUploadConf().getServerPath();
/*     */     }
/*  73 */     return super.getDefaultPathToLocation(selectO);
/*     */   }
/*     */   
/*     */   protected void actionCompare_actionPerformed() throws Exception {
/*  77 */     Object[] os = getSelectionObjects();
/*  78 */     innerCompare(os);
/*  79 */     super.actionCompare_actionPerformed();
/*     */   }
/*     */   
/*     */   public void innerCompare(Object[] os) throws Exception
/*     */   {
/*  84 */     final IWorkbenchPage page = PluginUtil.getActivePage();
/*  85 */     if ((os != null) && (os.length == 1) && ((os[0] instanceof FtpDownloadPro)))
/*     */     {
/*  87 */       FtpDownloadPro dpro = (FtpDownloadPro)os[0];
/*     */       
/*  89 */       IFile ifile = PluginUtil.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/" + dpro.getFtpfile().getName());
/*  90 */       final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();
/*  91 */       innerDownload(os, new MySexftpServerDownload()
/*     */       {
/*     */         public String trustFolder(FtpDownloadPro dpro) {
/*  94 */           return tmpeditpath;
/*     */         }
/*     */         
/*     */ 
/*  98 */         public boolean exceptionNotExits(FtpDownloadPro dpro) throws Exception { return true; }
/*     */         
/*     */         public void afterDownload(FtpDownloadPro dpro) throws Exception {
/* 101 */           String client = dpro.getFtpUploadConf().getClientPath();
/* 102 */           File clentFile = new File(client);
/* 103 */           if (!clentFile.exists()) throw new BizException("[" + clentFile.getAbsolutePath() + "] Not Exists!");
/* 104 */           if (clentFile.isDirectory())
/*     */           {
/* 106 */             client = client + "/" + dpro.getFtpfile().getName();
/*     */           }
/*     */           
/* 109 */           File f = new File(tmpeditpath);
/* 110 */           if (f.length() > 10000000L)
/*     */           {
/* 112 */             throw new BizException(String.format("[%s] 文件共 %s ，超过了 %s，不能继续操作。", new Object[] { f.getName(), StringUtil.getHumanSize(f.length()), StringUtil.getHumanSize(10000000L) }));
/*     */           }
/* 114 */           f = new File(client);
/* 115 */           if (f.length() > 10000000L)
/*     */           {
/* 117 */             throw new BizException(String.format("[%s] 文件共 %s ，超过了 %s，不能继续操作。", new Object[] { f.getName(), StringUtil.getHumanSize(f.length()), StringUtil.getHumanSize(10000000L) }));
/*     */           }
/* 119 */           Charset cs = Cpdetector.richencode(new FileInputStream(tmpeditpath));
/* 120 */           Charset cc = Cpdetector.richencode(new FileInputStream(client));
/*     */           
/* 122 */           String stext = FileUtil.getTextFromFile(tmpeditpath, cs != null ? cs.toString() : "gbk");
/* 123 */           String ctext = FileUtil.getTextFromFile(client, cc != null ? cc.toString() : "gbk");
/* 124 */           FileUtil.deleteFolder(new File(tmpeditpath).getParentFile());
/* 125 */           PluginUtil.openCompareEditor(page, ctext, stext, String.format("%s - %s", new Object[] { new File(client).getAbsolutePath(), dpro.getFtpUploadConf().getServerPath() }));
/*     */         }
/*     */         
/* 128 */       }, "Compare Process");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void treeExpanded_actionPerformed(TreeExpansionEvent e) throws Exception {
/* 133 */     Object elem = e.getElement();
/* 134 */     if ((elem instanceof AbstractSexftpView.TreeParent))
/*     */     {
/* 136 */       AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent)elem;
/* 137 */       expandTreeData(parent, null);
/*     */     }
/* 139 */     super.treeExpanded_actionPerformed(e);
/*     */   }
/*     */   
/*     */   private void expandTreeData(final AbstractSexftpView.TreeParent parent, final String onlyPath)
/*     */   {
/* 144 */     boolean isftpuploadconf = parent.getO() instanceof FtpUploadConf;
/* 145 */     boolean isftpdownpro = parent.getO() instanceof FtpDownloadPro;
/* 146 */     final FtpConf ftpconf = 
/* 147 */       isftpdownpro ? ((FtpDownloadPro)parent.getO()).getFtpConf() : isftpuploadconf ? (FtpConf)parent.getParent().getO() : null;
/* 148 */     final FtpUploadConf ftpUploadConf = 
/* 149 */       isftpdownpro ? ((FtpDownloadPro)parent.getO()).getFtpUploadConf() : isftpuploadconf ? (FtpUploadConf)parent.getO() : null;
/* 150 */     if ((parent.getChildren().length == 0) && (ftpUploadConf != null))
/*     */     {
/* 152 */       refreshPendingTree(parent, new SexftpRun(this) {
/*     */         public void srun() {
/* 154 */           FtpPools ftppool = new FtpPools(ftpconf, SexftpServerView.this);
/* 155 */           XFtp ftp = ftppool.getFtp();
/* 156 */           synchronized (ftp) {
/* 157 */             ftp = ftppool.getConnectedFtp();
/*     */             
/* 159 */             FtpPools ftppool2 = new FtpPools(ftpconf, SexftpServerView.this);
/* 160 */             ftppool2.getFtp();
/* 161 */             AbstractSexftpView.TreeObject onlyTo = SexftpServerView.this.expandServerFile(parent, ftpconf, ftpUploadConf, ftp, onlyPath);
/* 162 */             setReturnObject(onlyTo);
/* 163 */             if ((onlyPath != null) && (onlyTo == null))
/*     */             {
/* 165 */               SexftpServerView.this.showMessage("Cann't Find [" + onlyPath + "] In [Sexftp Server View]!");
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private AbstractSexftpView.TreeObject expandServerFile(AbstractSexftpView.TreeParent parent, FtpConf ftpconf, FtpUploadConf ftpUploadConf, XFtp ftp, String onlyPath)
/*     */   {
/* 175 */     AbstractSexftpView.TreeObject onlyTreeObject = null;
/* 176 */     List<FtpDownloadPro> listDownloadPro = null;
/* 177 */     listDownloadPro = listDownloadPro(ftpUploadConf, ftpconf, ftp);
/* 178 */     for (FtpDownloadPro ftpDownloadPro : listDownloadPro) {
/* 179 */       String thisServer = ftpDownloadPro.getFtpUploadConf().getServerPath();
/*     */       
/*     */ 
/*     */ 
/* 183 */       String serverPath = thisServer;
/* 184 */       String teeNodeName = String.format("%s ( %s %s )", new Object[] { ftpDownloadPro.getFtpfile().getName(), StringUtil.getHumanSize(ftpDownloadPro.getFtpfile().getSize()), DateTimeUtils.format(ftpDownloadPro.getFtpfile().getTimeStamp().getTime()) });
/* 185 */       if (serverPath.endsWith("/"))
/*     */       {
/* 187 */         AbstractSexftpView.TreeObject exists = existFtpDownloadPro(parent, thisServer);
/* 188 */         AbstractSexftpView.TreeParent child = new AbstractSexftpView.TreeParent(this, ftpDownloadPro.getFtpfile().getName(), ftpDownloadPro);
/* 189 */         if (exists != null)
/*     */         {
/* 191 */           child = (AbstractSexftpView.TreeParent)exists;
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 200 */           parent.addChild(child);
/*     */         }
/*     */         
/* 203 */         if (thisServer.equals(onlyPath))
/*     */         {
/* 205 */           onlyTreeObject = child;
/*     */ 
/*     */         }
/* 208 */         else if ((onlyPath != null) && (onlyPath.startsWith(thisServer)))
/*     */         {
/* 210 */           onlyTreeObject = onlyTreeObject == null ? expandServerFile(child, ftpconf, ftpDownloadPro.getFtpUploadConf(), ftp, onlyPath) : onlyTreeObject;
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 216 */         AbstractSexftpView.TreeObject to = new AbstractSexftpView.TreeObject(this, teeNodeName, ftpDownloadPro);
/* 217 */         if (existFtpDownloadPro(parent, thisServer) == null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */           parent.addChild(to);
/*     */         }
/* 226 */         if (thisServer.equals(onlyPath))
/*     */         {
/* 228 */           onlyTreeObject = to;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 235 */     return onlyTreeObject;
/*     */   }
/*     */   
/*     */   private AbstractSexftpView.TreeObject existFtpDownloadPro(AbstractSexftpView.TreeParent p, String serverPath) {
/*     */     AbstractSexftpView.TreeObject[] arrayOfTreeObject;
/* 240 */     int j = (arrayOfTreeObject = p.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject to = arrayOfTreeObject[i];
/*     */       
/* 242 */       if ((to.getO() instanceof FtpDownloadPro))
/*     */       {
/* 244 */         FtpDownloadPro fupro = (FtpDownloadPro)to.getO();
/* 245 */         if (fupro.getFtpUploadConf().getServerPath().equals(serverPath))
/*     */         {
/* 247 */           return to;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 252 */     return null;
/*     */   }
/*     */   
/*     */   public void directTo(final String expandServerPath, Integer ftpUploadTreeNodesIndex) {
/* 256 */     final AbstractSexftpView.TreeParent r = ftpUploadTreeNodesIndex != null ? getAllFtpUploadConfNodes()[ftpUploadTreeNodesIndex.intValue()] : getRoot();
/* 257 */     if (r.getChildren().length == 0)
/*     */     {
/* 259 */       expandTreeData(r, expandServerPath);
/* 260 */       return;
/*     */     }
/*     */     
/* 263 */     TreeViewUtil.serchTreeData(r, new SearchCallback() {
/*     */       public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject o) {
/* 265 */         if ((o.getO() instanceof FtpDownloadPro))
/*     */         {
/* 267 */           String serverPath = ((FtpDownloadPro)o.getO()).getFtpUploadConf().getServerPath();
/* 268 */           if (expandServerPath.equals(serverPath))
/*     */           {
/* 270 */             SexftpServerView.this.refreshTreeView(r, o);
/* 271 */             throw new AbortException();
/*     */           }
/* 273 */           if ((expandServerPath.startsWith(serverPath)) && ((o instanceof AbstractSexftpView.TreeParent)))
/*     */           {
/* 275 */             AbstractSexftpView.TreeObject[] children = ((AbstractSexftpView.TreeParent)o).getChildren();
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 280 */             if (children.length == 0)
/*     */             {
/* 282 */               SexftpServerView.this.expandTreeData((AbstractSexftpView.TreeParent)o, expandServerPath);
/* 283 */               throw new AbortException();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 288 */         return new TreeViewUtil.ThisYourFind(false, true);
/*     */       }
/*     */       
/* 291 */     });
/* 292 */     showMessage("Cann't Find [" + expandServerPath + "] In [Sexftp Server View]!");
/*     */   }
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
/*     */   protected void actionDirectSLocal_actionPerformed()
/*     */     throws Exception
/*     */   {
/* 311 */     ISelection selection = this.viewer.getSelection();
/* 312 */     AbstractSexftpView.TreeObject obj = (AbstractSexftpView.TreeObject)((IStructuredSelection)selection).getFirstElement();
/* 313 */     AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
/* 314 */     SexftpLocalView mv = PluginUtil.findAndShowLocalView(PluginUtil.getActivePage());
/* 315 */     if (selectFtpUploadConfNodes.length >= 1)
/*     */     {
/* 317 */       AbstractSexftpView.TreeParent selectObj = selectFtpUploadConfNodes[0];
/* 318 */       if ((obj.getO() instanceof FtpDownloadPro))
/*     */       {
/*     */ 
/* 321 */         FtpDownloadPro dpro = (FtpDownloadPro)obj.getO();
/* 322 */         String clilentPath = dpro.getFtpUploadConf().getClientPath();
/* 323 */         if (!dpro.getFtpUploadConf().getServerPath().endsWith("/"))
/*     */         {
/* 325 */           clilentPath = clilentPath + "/" + new File(dpro.getFtpUploadConf().getServerPath()).getName();
/* 326 */           clilentPath = new File(clilentPath).getAbsolutePath();
/*     */         }
/* 328 */         AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
/* 329 */         for (int i = 0; i < allFtpUploadConfNodes.length; i++)
/*     */         {
/* 331 */           if (allFtpUploadConfNodes[i] == selectObj)
/*     */           {
/* 333 */             mv.directTo(clilentPath, Integer.valueOf(i));
/* 334 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 341 */     super.actionDirectSLocal_actionPerformed();
/*     */   }
/*     */   
/*     */   protected boolean isShowProjectView()
/*     */   {
/* 346 */     return false;
/*     */   }
/*     */   
/*     */   protected void doubleClickAction_actionPerformed() throws Exception {
/* 350 */     super.doubleClickAction_actionPerformed();
/* 351 */     actionEdit_actionPerformed();
/*     */   }
/*     */   
/*     */ 
/*     */   public List getHiddenActions()
/*     */   {
/* 357 */     return Arrays.asList(new Object[] {
/* 358 */       this.actionDirectSServer, 
/* 359 */       this.actionUpload, 
/* 360 */       this.actionPrepareServUpload, 
/* 361 */       this.actionPrepareUpload, this.actionLocalEdit });
/*     */   }
/*     */   
/*     */   protected void menuAboutToShow_event(IMenuManager manager) {
/* 365 */     super.menuAboutToShow_event(manager);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void actionEnableHandle()
/*     */   {
/* 371 */     super.actionEnableHandle();
/* 372 */     Object[] os = getSelectionObjects();
/* 373 */     if ((os.length == 1) && ((os[0] instanceof FtpDownloadPro)) && (!((FtpDownloadPro)os[0]).getFtpUploadConf().getServerPath().endsWith("/")))
/*     */     {
/* 375 */       this.actionEdit.setEnabled(true);
/*     */     }
/*     */     else
/*     */     {
/* 379 */       this.actionCompare.setEnabled(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 390 */     this.actionDirectSLocal.setEnabled(true);
/* 391 */     this.actionDirectSServer.setEnabled(false);
/*     */   }
/*     */   
/*     */   protected void actionEdit_actionPerformed()
/*     */     throws Exception
/*     */   {
/* 397 */     Object[] selectO = getSelectionObjects();
/* 398 */     if ((selectO.length == 1) && ((selectO[0] instanceof FtpDownloadPro)))
/*     */     {
/* 400 */       FtpDownloadPro dpro = (FtpDownloadPro)selectO[0];
/* 401 */       if (!dpro.getFtpUploadConf().getServerPath().endsWith("/"))
/*     */       {
/* 403 */         innerEditServerFile(dpro);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 410 */     super.actionEdit_actionPerformed();
/*     */   }
/*     */   
/*     */   public void innerEditServerFile(FtpDownloadPro dpro) throws Exception
/*     */   {
/* 415 */     final IFile ifile = PluginUtil.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/" + dpro.getFtpfile().getName());
/* 416 */     final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();
/*     */     
/* 418 */     innerDownload(new Object[] { dpro }, new MySexftpServerDownload()
/*     */     {
/*     */       public String trustFolder(FtpDownloadPro dpro) {
/* 421 */         return tmpeditpath;
/*     */       }
/*     */       
/*     */       public boolean exceptionNotExits(FtpDownloadPro dpro) throws Exception
/*     */       {
/* 426 */         return true;
/*     */       }
/*     */       
/*     */       public void afterDownload(final FtpDownloadPro dpro) {
/* 430 */         Display.getDefault().asyncExec(new SexftpRun(SexftpServerView.this)
/*     */         {
/*     */           public void srun() throws Exception {
/* 433 */             this.val$ifile.refreshLocal(1, null);
/* 434 */             IFile inewfile = this.val$ifile;
/*     */             
/* 436 */             Charset c = Cpdetector.richencode(new FileInputStream(this.val$ifile.getLocation().toFile()));
/* 437 */             if ((c != null) && (c.toString().indexOf("ASCII") < 0))
/*     */             {
/* 439 */               IContentType contentType = IDE.getContentType(inewfile);
/* 440 */               if ((contentType == null) || (contentType.getBaseType() == null) || (!"text".equalsIgnoreCase(contentType.getBaseType().getName())))
/*     */               {
/*     */ 
/*     */ 
/* 444 */                 if (c.toString().startsWith("UTF-16"))
/*     */                 {
/* 446 */                   c = Charset.forName("gbk"); }
/*     */               }
/* 448 */               inewfile = PluginUtil.rename(this.val$ifile, this.val$ifile.getName() + ".sf" + c);
/* 449 */               if (contentType != null)
/*     */               {
/* 451 */                 contentType.setDefaultCharset(c.toString());
/*     */               }
/*     */               else
/*     */               {
/* 455 */                 SexftpServerView.this.showMessage("You May Need Set The Text file encoding (" + c + ")");
/*     */               }
/*     */             }
/* 458 */             FtpConf ftpconf = dpro.getFtpConf();
/* 459 */             FtpPools ftppool = new FtpPools(ftpconf, SexftpServerView.this);
/* 460 */             String serverDirPath = new File(dpro.getFtpUploadConf().getServerPath()).getParent().replace('\\', '/');
/* 461 */             String serverFileName = dpro.getFtpfile().getName();
/* 462 */             IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(inewfile), "sexftp.editors.inner.SfTextEditor");
/* 463 */             SfTextEditor stextEditor = (SfTextEditor)openEditor;
/* 464 */             stextEditor.setDoSaveListener(new ServerInnnerEditSaveListener(inewfile, serverDirPath, serverFileName, SexftpServerView.this, ftppool));
/*     */           }
/*     */         });
/*     */       }
/*     */     });
/*     */   }
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
/*     */   protected void actionDisconnect_actionPerformed()
/*     */     throws Exception
/*     */   {
/* 512 */     super.actionDisconnect_actionPerformed();
/*     */   }
/*     */   
/*     */   private List<FtpDownloadPro> listDownloadPro(FtpUploadConf ftpUploadConf, FtpConf ftpconf, XFtp ftp) {
/* 516 */     List<FtpDownloadPro> downloadProList = new ArrayList();
/* 517 */     ftp.cd(ftpUploadConf.getServerPath());
/* 518 */     List<FtpFile> listFiles = ftp.listFiles();
/* 519 */     for (FtpFile ftpfile : listFiles) {
/* 520 */       String filename = ftpfile.getName();
/* 521 */       if ((!filename.equals(".")) && (!filename.equals(".."))) {
/* 522 */         FtpUploadConf expandFtpUploadInf = new FtpUploadConf();
/* 523 */         String serverPath = ftpUploadConf.getServerPath();
/* 524 */         String clientPath = ftpUploadConf.getClientPath();
/*     */         
/* 526 */         serverPath = serverPath + filename;
/* 527 */         if (ftpfile.isIsfolder())
/*     */         {
/* 529 */           serverPath = serverPath + "/";
/* 530 */           clientPath = clientPath + "/" + filename;
/*     */         }
/* 532 */         expandFtpUploadInf.setServerPath(serverPath);
/* 533 */         expandFtpUploadInf.setClientPath(clientPath);
/* 534 */         expandFtpUploadInf.setIncludes(ftpUploadConf.getIncludes());
/* 535 */         expandFtpUploadInf.setExcludes(ftpUploadConf.getExcludes());
/* 536 */         FtpDownloadPro dpro = new FtpDownloadPro(expandFtpUploadInf, ftpconf, ftpfile);
/*     */         
/* 538 */         downloadProList.add(dpro);
/*     */       } }
/* 540 */     return downloadProList;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean okPopActionDownload()
/*     */   {
/* 546 */     Object[] os = getSelectionObjects();
/* 547 */     if (os.length == 0) return false;
/* 548 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 549 */       if (!(o instanceof FtpConf))
/*     */       {
/*     */ 
/*     */ 
/* 553 */         if (!(o instanceof FtpUploadConf))
/*     */         {
/*     */ 
/*     */ 
/* 557 */           if (!(o instanceof FtpDownloadPro))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 563 */             return false; } }
/*     */       }
/*     */     }
/* 566 */     return true;
/*     */   }
/*     */   
/*     */   protected void actionDownload_actionPerformed() throws Exception
/*     */   {
/* 571 */     AbstractSexftpView.TreeObject[] selectTreeObj = getUpNodes(getSelectNodes(false));
/* 572 */     List selectOs = new ArrayList();
/* 573 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = selectTreeObj).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject to = arrayOfTreeObject1[i];
/*     */       
/* 575 */       if ((to.getO() instanceof FtpUploadConf))
/*     */       {
/* 577 */         FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf)to.getO(), 
/* 578 */           (FtpConf)to.getParent().getO(), 
/* 579 */           new FtpFile("/", true, 0L, Calendar.getInstance()));
/* 580 */         selectOs.add(dpro);
/*     */       }
/* 582 */       else if ((to.getO() instanceof FtpConf)) {
/*     */         AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/* 584 */         int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)to).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject suto = arrayOfTreeObject2[k];
/*     */           
/* 586 */           FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf)suto.getO(), 
/* 587 */             (FtpConf)suto.getParent().getO(), 
/* 588 */             new FtpFile("/", true, 0L, Calendar.getInstance()));
/* 589 */           selectOs.add(dpro);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 594 */         selectOs.add(to.getO());
/*     */       }
/*     */     }
/* 597 */     innerDownload(selectOs.toArray(), null);
/*     */   }
/*     */   
/* 600 */   public void innerDownload(Object[] os, MySexftpServerDownload mydownload) throws Exception { innerDownload(os, mydownload, null); }
/*     */   
/*     */   public void innerDownload(final Object[] os, final MySexftpServerDownload mydownload, String title) throws Exception
/*     */   {
/* 604 */     Job job = new SexftpJob(title != null ? title : "Download Process", this) {
/*     */       private List<FtpDownloadPro> list;
/* 606 */       private boolean finisedPrepare = false;
/* 607 */       private int workedcont = 0;
/* 608 */       private double workedpercents = 800.0D;
/* 609 */       private boolean corrTime = false;
/* 610 */       private Integer overwritecode = null;
/* 611 */       private boolean okdowncurrent = true;
/*     */       
/*     */       protected IStatus srun(final IProgressMonitor monitor) throws Exception {
/* 614 */         FtpPools ftppool = null;
/* 615 */         XFtp ftp = null;
/* 616 */         FtpDownloadPro pdpro = null;
/* 617 */         Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; FtpConf ftpconf; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 618 */           if ((o instanceof FtpDownloadPro))
/*     */           {
/* 620 */             pdpro = (FtpDownloadPro)o;
/* 621 */             ftpconf = ((FtpDownloadPro)o).getFtpConf();
/* 622 */             ftppool = new FtpPools(ftpconf, SexftpServerView.this);
/* 623 */             ftp = ftppool.getFtp();
/* 624 */             break;
/*     */           }
/*     */         }
/*     */         
/* 628 */         monitor.beginTask("Getting Data From Server...", 1000);
/* 629 */         if (ftp != null)
/*     */         {
/* 631 */           synchronized (ftp) {
/* 632 */             ftppool.getConnectedFtp();
/* 633 */             this.list = new Vector();
/* 634 */             ExistFtpFile existf = new ExistFtpFile(ftp);
/* 635 */             Object[] arrayOfObject2; ftpconf = (arrayOfObject2 = os).length; for (FtpConf localFtpConf1 = 0; localFtpConf1 < ftpconf; localFtpConf1++) { Object o = arrayOfObject2[localFtpConf1];
/* 636 */               if ((o instanceof FtpDownloadPro))
/*     */               {
/* 638 */                 pdpro = (FtpDownloadPro)o;
/* 639 */                 String cserverPath = pdpro.getFtpUploadConf().getServerPath();
/* 640 */                 if (!cserverPath.endsWith("/"))
/*     */                 {
/* 642 */                   monitor.subTask("Checking " + cserverPath);
/* 643 */                   if (existf.existsFtpFile(cserverPath) == null)
/*     */                   {
/*     */ 
/* 646 */                     if ((mydownload == null) || (mydownload.exceptionNotExits(pdpro)))
/*     */                     {
/* 648 */                       throw new BizException("[" + pdpro.getFtpUploadConf().getServerPath() + "] Not Exists On Server!");
/*     */                     }
/*     */                   }
/*     */                   else
/* 652 */                     pdpro.setFtpfile(existf.existsFtpFile(cserverPath));
/*     */                 } else {
/* 654 */                   SexftpServerView.this.innerDownladRetriList(pdpro, monitor, this.list, ftp);
/*     */                 }
/*     */               }
/*     */             }
/* 658 */             this.finisedPrepare = true;
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
/*     */ 
/*     */ 
/*     */ 
/* 704 */             String dir = "";
/* 705 */             for (int i = 0; i < 10; i++)
/*     */             {
/* 707 */               if ((!this.finisedPrepare) && (this.list.size() == 0))
/*     */               {
/*     */ 
/* 710 */                 for (int t = 0; t < 30; t++)
/*     */                 {
/* 712 */                   Thread.sleep(1000L);
/* 713 */                   if (this.list.size() > 0)
/*     */                     break;
/*     */                 } }
/* 716 */               if (this.list.size() == 0)
/*     */               {
/* 718 */                 if (this.finisedPrepare) {
/*     */                   break;
/*     */                 }
/* 721 */                 throw new BizException("Download Time Out!");
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 726 */               if ((this.finisedPrepare) && (!this.corrTime))
/*     */               {
/* 728 */                 this.list.size();
/*     */                 
/* 730 */                 if (1000 > this.list.size())
/*     */                 {
/* 732 */                   monitor.worked(1000 - this.list.size());
/*     */                 }
/* 734 */                 else if (1000 < this.list.size())
/*     */                 {
/* 736 */                   this.workedpercents = ((1000 - this.workedcont) * 100 / (this.list.size() - this.workedcont));
/* 737 */                   this.workedpercents = (this.workedpercents * 70.0D / 100.0D);
/*     */                 }
/* 739 */                 this.corrTime = true;
/*     */               }
/*     */               
/* 742 */               FtpDownloadPro dpro = (FtpDownloadPro)this.list.get(0);
/*     */               
/*     */ 
/* 745 */               final FtpDownloadPro fdpro = dpro;
/* 746 */               String serverPath = dpro.getFtpUploadConf().getServerPath();
/* 747 */               String wkdir = serverPath.substring(0, serverPath.lastIndexOf("/") + 1);
/* 748 */               String fielname = serverPath.substring(serverPath.lastIndexOf("/") + 1);
/* 749 */               if (!dir.equals(wkdir))
/*     */               {
/* 751 */                 ftp.cd(wkdir);
/* 752 */                 dir = wkdir;
/* 753 */                 SexftpServerView.this.console("working in " + wkdir);
/*     */               }
/* 755 */               IFtpStreamMonitor fmon = new IFtpStreamMonitor() {
/*     */                 private FtpUploadConf curftpUploadConf;
/* 757 */                 long calSize = 0L;
/* 758 */                 long timsta = 0L;
/* 759 */                 long speed = 0L;
/* 760 */                 boolean smallLeftCompleted = true;
/* 761 */                 boolean okCancel = false;
/*     */                 
/*     */                 public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize, long totalSize, String info)
/*     */                 {
/* 765 */                   totalSize = (int)fdpro.getFtpfile().getSize();
/*     */                   
/*     */ 
/*     */ 
/* 769 */                   if (ftpUploadConf != null)
/*     */                   {
/* 771 */                     this.curftpUploadConf = ftpUploadConf;
/* 772 */                     this.calSize = 0L;
/* 773 */                     this.timsta = System.currentTimeMillis();
/* 774 */                     return;
/*     */                   }
/* 776 */                   long secds = System.currentTimeMillis() - this.timsta;
/* 777 */                   if (secds > 1000L)
/*     */                   {
/* 779 */                     this.speed = (((float)(uploadedSize - this.calSize) / ((float)secds / 1000.0F)));
/* 780 */                     this.calSize = uploadedSize;
/* 781 */                     this.timsta = System.currentTimeMillis();
/*     */                   }
/*     */                   
/* 784 */                   monitor.subTask(String.format("(%s in %s) %s \r\n getting %s", new Object[] {
/* 785 */                     StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize), 
/* 786 */                     (float)this.speed > 1.0E-4F ? StringUtil.getHumanSize(this.speed) + "/s" : "", 
/* 787 */                     this.curftpUploadConf.getServerPath() }));
/*     */                   
/*     */ 
/* 790 */                   this.okCancel = monitor.isCanceled();
/*     */                   
/* 792 */                   if (totalSize == uploadedSize)
/*     */                   {
/* 794 */                     int r = new Random().nextInt(100);
/*     */                     
/*     */ 
/* 797 */                     if (SexftpServerView.5.this.workedpercents > r)
/*     */                     {
/* 799 */                       monitor.worked(1);
/* 800 */                       SexftpServerView.5.this.workedcont += 1;
/*     */                     }
/*     */                     
/*     */                   }
/* 804 */                   else if ((this.okCancel) && (totalSize - uploadedSize < 102400L))
/*     */                   {
/* 806 */                     if (this.smallLeftCompleted)
/*     */                     {
/* 808 */                       SexftpServerView.this.console("Canncled But Go Ahead Little Left Files! " + fdpro.getFtpfile().getName() + " " + StringUtil.getHumanSize(uploadedSize));
/* 809 */                       this.okCancel = false;
/*     */                     }
/*     */                   }
/*     */                   
/* 813 */                   if (this.okCancel)
/*     */                   {
/* 815 */                     SexftpServerView.this.console("Operation Canceled!");
/* 816 */                     SexftpServerView.this.console(String.format("Last Downloaded %s Of %s .", new Object[] { StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize) }));
/* 817 */                     if (totalSize > uploadedSize)
/*     */                     {
/* 819 */                       SexftpServerView.this.console(String.format("Warning:Incomplete Download %s %s of %s!", new Object[] {
/* 820 */                         this.curftpUploadConf.getServerPath(), 
/* 821 */                         StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize) }));
/*     */                     }
/* 823 */                     SexftpServerView.5.this.list = null;
/* 824 */                     throw new AbortException();
/*     */                   }
/*     */                 }
/*     */                 
/*     */                 public void printSimple(String info)
/*     */                 {
/* 830 */                   SexftpServerView.this.console(info);
/*     */                 }
/* 832 */               };
/* 833 */               fmon.printStreamString(dpro.getFtpUploadConf(), 0L, 0L, null);
/* 834 */               String trustPath = mydownload != null ? mydownload.trustFolder(fdpro) : null;
/* 835 */               final String cpath = dpro.getFtpUploadConf().getClientPath() + "/" + fielname;
/* 836 */               this.okdowncurrent = true;
/* 837 */               if ((PluginUtil.overwriteTips().booleanValue()) && (trustPath == null) && (new File(cpath).exists()))
/*     */               {
/* 839 */                 Display.getDefault().syncExec(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 842 */                     int oretur = 0;
/* 843 */                     if (SexftpServerView.5.this.overwritecode == null)
/*     */                     {
/* 845 */                       MessageDialogWithToggle q = SexftpServerView.this.showQuestion("File [" + cpath + "] Exists,Overwirte?", "Remember Me In This Operation!");
/* 846 */                       if (q.getToggleState())
/*     */                       {
/* 848 */                         SexftpServerView.5.this.overwritecode = Integer.valueOf(q.getReturnCode());
/*     */                       }
/* 850 */                       oretur = q.getReturnCode();
/*     */                     }
/*     */                     else
/*     */                     {
/* 854 */                       oretur = SexftpServerView.5.this.overwritecode.intValue();
/*     */                     }
/* 856 */                     if (oretur != 2)
/*     */                     {
/*     */ 
/*     */ 
/* 860 */                       if (oretur == 1)
/*     */                       {
/* 862 */                         SexftpServerView.5.this.list = null;
/* 863 */                         SexftpServerView.5.this.okdowncurrent = false;
/*     */                       }
/* 865 */                       else if (oretur == 3)
/*     */                       {
/* 867 */                         SexftpServerView.5.this.okdowncurrent = false;
/*     */                       }
/*     */                       else {
/* 870 */                         SexftpServerView.5.this.okdowncurrent = false;
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 });
/*     */               }
/* 876 */               if (this.list == null) throw new AbortException();
/* 877 */               if ((this.okdowncurrent) && (!monitor.isCanceled()))
/*     */               {
/* 879 */                 ftp.download(fielname, cpath, fmon);
/* 880 */                 if (mydownload != null)
/*     */                 {
/* 882 */                   mydownload.afterDownload(dpro);
/*     */                 }
/*     */               }
/*     */               
/* 886 */               this.list.remove(0);
/* 887 */               if ((this.finisedPrepare) && (this.list.size() == 0)) break;
/* 888 */               if ((this.list.size() > 0) && (i >= 9)) { i = 0;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 894 */         return Status.OK_STATUS;
/*     */       }
/*     */       
/* 897 */     };
/* 898 */     job.setUser(true);
/* 899 */     job.schedule();
/*     */   }
/*     */   
/*     */   private List<FtpDownloadPro> innerDownladRetriList(FtpDownloadPro dpro, IProgressMonitor monitor, List<FtpDownloadPro> list, XFtp ftp)
/*     */   {
/* 904 */     if ((monitor.isCanceled()) || (list == null))
/*     */     {
/* 906 */       throw new AbortException();
/*     */     }
/* 908 */     String serverPath = dpro.getFtpUploadConf().getServerPath();
/* 909 */     if (serverPath.endsWith("/"))
/*     */     {
/* 911 */       monitor.subTask("Prepareing " + serverPath);
/* 912 */       List<FtpDownloadPro> listDownloadPro = listDownloadPro(dpro.getFtpUploadConf(), dpro.getFtpConf(), ftp);
/* 913 */       for (FtpDownloadPro ftpDownloadPro : listDownloadPro) {
/* 914 */         innerDownladRetriList(ftpDownloadPro, monitor, list, ftp);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 919 */     else if (list != null) { list.add(dpro);
/*     */     }
/* 921 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpServerView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */