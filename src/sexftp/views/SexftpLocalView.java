/*      */ package sexftp.views;
/*      */ 
/*      */ import com.thoughtworks.xstream.XStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.desy.common.util.DateTimeUtils;
/*      */ import org.eclipse.core.resources.IFile;
/*      */ import org.eclipse.core.resources.IProject;
/*      */ import org.eclipse.core.runtime.IPath;
/*      */ import org.eclipse.core.runtime.IProgressMonitor;
/*      */ import org.eclipse.core.runtime.IStatus;
/*      */ import org.eclipse.core.runtime.Status;
/*      */ import org.eclipse.core.runtime.content.IContentType;
/*      */ import org.eclipse.core.runtime.jobs.Job;
/*      */ import org.eclipse.jface.action.Action;
/*      */ import org.eclipse.jface.viewers.ISelection;
/*      */ import org.eclipse.jface.viewers.IStructuredSelection;
/*      */ import org.eclipse.jface.viewers.TreeExpansionEvent;
/*      */ import org.eclipse.jface.viewers.TreePath;
/*      */ import org.eclipse.jface.viewers.TreeSelection;
/*      */ import org.eclipse.jface.viewers.TreeViewer;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchPage;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.eclipse.ui.ide.IDE;
/*      */ import org.eclipse.ui.part.FileEditorInput;
/*      */ import org.sexftp.core.exceptions.AbortException;
/*      */ import org.sexftp.core.exceptions.SRuntimeException;
/*      */ import org.sexftp.core.ftp.FileMd5;
/*      */ import org.sexftp.core.ftp.FtpUtil;
/*      */ import org.sexftp.core.ftp.bean.FtpConf;
/*      */ import org.sexftp.core.ftp.bean.FtpDownloadPro;
/*      */ import org.sexftp.core.ftp.bean.FtpFile;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*      */ import org.sexftp.core.utils.FileUtil;
/*      */ import org.sexftp.core.utils.SearchCallback;
/*      */ import org.sexftp.core.utils.StringUtil;
/*      */ import org.sexftp.core.utils.TreeViewUtil;
/*      */ import org.sexftp.core.utils.TreeViewUtil.ThisYourFind;
/*      */ import sexftp.SexftpJob;
/*      */ import sexftp.SexftpRun;
/*      */ import sexftp.SrcViewable;
/*      */ import sexftp.editors.inner.SfTextEditor;
/*      */ import sexftp.editors.viewlis.IDoSaveListener;
/*      */ import sexftp.uils.PluginUtil;
/*      */ import sexftp.uils.PluginUtil.RunAsDisplayThread;
/*      */ 
/*      */ public class SexftpLocalView extends SexftpMainView
/*      */ {
/*      */   protected void actionPrepare()
/*      */   {
/*   64 */     this.actionCompare.setText("Compare With Server(&C)");
/*   65 */     super.actionPrepare();
/*      */   }
/*      */   
/*      */   protected void actionPrepareUpload_actionPerformed()
/*      */     throws Exception
/*      */   {
/*   71 */     final FtpConf[] ftpconfs = getFtpConfsSelected();
/*   72 */     final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
/*      */     
/*   74 */     doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro()
/*      */     {
/*      */       public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
/*   77 */         SexftpLocalView.this.innerPrepareUpload_actionPerformed(selectOs, ftpconfs, selectFtpUploadConfNodes);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */   protected void innerPrepareUpload_actionPerformed(final Object[] selectos, FtpConf[] ftpconfs, final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes)
/*      */     throws Exception
/*      */   {
/*   86 */     if (ftpconfs.length > 1)
/*      */     {
/*   88 */       throw new AbortException(); }
/*      */     FtpConf[] arrayOfFtpConf;
/*   90 */     int j = (arrayOfFtpConf = ftpconfs).length; for (int i = 0; i < j; i++) { FtpConf ftpconf = arrayOfFtpConf[i];
/*      */       
/*   92 */       String lastModify = workspaceWkPath + ftpconf.getName() + "/lastModMap.d";
/*   93 */       if (!new File(lastModify).exists())
/*      */       {
/*   95 */         showMessage("No Formated Files,Need Format First,And You Can Get Modified Files Next Time!");
/*   96 */         actionFormat_innerPerofrmed(ftpconf.getName(), true, null);
/*   97 */         return;
/*      */       }
/*      */     }
/*  100 */     final FtpConf ftpconf = ftpconfs[0];
/*      */     
/*  102 */     Object job = new SexftpJob("Prepare Local New Modified File", this)
/*      */     {
/*      */       protected IStatus srun(IProgressMonitor monitor) throws Exception
/*      */       {
/*  106 */         monitor.subTask("Preparing Files...");
/*      */         
/*  108 */         Object[] os = selectos;
/*      */         
/*  110 */         monitor.beginTask("Prepare Local New Modified File,doing ...", os.length);
/*      */         
/*  112 */         for (int i = 0; i < selectFtpUploadConfNodes.length; i++)
/*      */         {
/*  114 */           AbstractSexftpView.TreeParent tp = selectFtpUploadConfNodes[i];
/*      */           
/*  116 */           AbstractSexftpView.TreeParent newtp = new AbstractSexftpView.TreeParent(SexftpLocalView.this, tp.getName(), StringUtil.deepClone(tp.getO()));
/*  117 */           newtp.setParent(tp.getParent());
/*  118 */           selectFtpUploadConfNodes[i] = newtp;
/*      */         }
/*      */         
/*      */ 
/*  122 */         Map<String, String> lastModMap = FtpUtil.readLastModMap(SexftpLocalView.workspacePath + ".work/" + ftpconf.getName());
/*  123 */         final List<FtpUploadPro> difList = new ArrayList();
/*  124 */         final List<FtpUploadPro> notExistList = new ArrayList();
/*  125 */         Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/*  126 */           if ((o instanceof FtpUploadPro))
/*      */           {
/*  128 */             FtpUploadPro fupro = (FtpUploadPro)o;
/*  129 */             String client = fupro.getFtpUploadConf().getClientPath();
/*  130 */             monitor.subTask("Comparing.. " + client);
/*  131 */             File clientfile = new File(client);
/*  132 */             if ((clientfile.exists()) && (clientfile.isFile()))
/*      */             {
/*  134 */               String md5 = FileMd5.getMD5(clientfile, monitor);
/*  135 */               if (lastModMap.containsKey(client))
/*      */               {
/*  137 */                 if (!((String)lastModMap.get(client)).equals(md5))
/*      */                 {
/*  139 */                   difList.add(fupro);
/*      */                 }
/*      */                 
/*      */               }
/*      */               else {
/*  144 */                 notExistList.add(fupro);
/*      */               }
/*      */             }
/*  147 */             monitor.subTask("waiting...");
/*      */           }
/*  149 */           monitor.worked(1);
/*      */         }
/*      */         
/*  152 */         Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this)
/*      */         {
/*      */           public void srun() throws Exception
/*      */           {
/*  156 */             List<FtpUploadPro> pathList = new ArrayList();
/*  157 */             pathList.addAll(difList);
/*  158 */             pathList.addAll(notExistList);
/*  159 */             boolean syncViewShowed = PluginUtil.isSyncViewShowed(SexftpLocalView.this.activePage);
/*  160 */             if (pathList.size() == 0)
/*      */             {
/*  162 */               SexftpLocalView.this.showMessage("No Different Or Modified Files With The Status Of Last Format Or Upload!");
/*  163 */               if (!syncViewShowed) { return;
/*      */               }
/*      */             }
/*  166 */             SexftpLocalView.this.handleSyncTreeNode(pathList, this.val$selectFtpUploadConfNodes, this.val$ftpconf);
/*      */             
/*  168 */             if (!syncViewShowed)
/*      */             {
/*  170 */               SexftpLocalView.this.showMessage("The Result Will Show In [Sexftp Synchronize View]");
/*      */             }
/*  172 */             SexftpSyncView syncView = PluginUtil.findAndShowSyncView(SexftpLocalView.this.activePage);
/*  173 */             syncView.showDifView(this.val$selectFtpUploadConfNodes, SexftpLocalView.this.anyaCustomizedImgMap(new ArrayList(), difList, notExistList));
/*      */           }
/*      */           
/*      */ 
/*  177 */         });
/*  178 */         return Status.OK_STATUS;
/*      */       }
/*  180 */     };
/*  181 */     ((Job)job).setUser(true);
/*  182 */     ((Job)job).schedule();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionDownload_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  190 */     AbstractSexftpView.TreeObject[] selectTreeObj = getUpNodes(getSelectNodes(false));
/*  191 */     List selectOs = new ArrayList();
/*  192 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = selectTreeObj).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject to = arrayOfTreeObject1[i];
/*      */       
/*  194 */       if ((to.getO() instanceof FtpUploadConf))
/*      */       {
/*  196 */         FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf)to.getO(), 
/*  197 */           (FtpConf)to.getParent().getO(), 
/*  198 */           new FtpFile("/", true, 0L, Calendar.getInstance()));
/*  199 */         selectOs.add(dpro);
/*      */       }
/*  201 */       else if ((to.getO() instanceof FtpConf)) {
/*      */         AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/*  203 */         int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)to).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject suto = arrayOfTreeObject2[k];
/*      */           
/*  205 */           FtpDownloadPro dpro = new FtpDownloadPro((FtpUploadConf)suto.getO(), 
/*  206 */             (FtpConf)suto.getParent().getO(), 
/*  207 */             new FtpFile("/", true, 0L, Calendar.getInstance()));
/*  208 */           selectOs.add(dpro);
/*      */         }
/*      */       }
/*  211 */       else if ((to.getO() instanceof FtpUploadPro))
/*      */       {
/*  213 */         selectOs.add(uPro2DownPro((FtpUploadPro)to.getO()));
/*      */       }
/*      */     }
/*      */     
/*  217 */     PluginUtil.findAndShowServerView(PluginUtil.getActivePage()).innerDownload(selectOs.toArray(), null);
/*      */   }
/*      */   
/*      */   public void innerCompareFiles(final IInnerCompareCallback callback, Object[] os) throws Exception {
/*  221 */     if (os != null)
/*      */     {
/*  223 */       final List<FtpDownloadPro> dproList = new ArrayList();
/*  224 */       boolean hasfolder = false;
/*  225 */       Object[] arrayOfObject; int j = (arrayOfObject = os).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/*      */         
/*  227 */         if ((o instanceof FtpUploadPro))
/*      */         {
/*  229 */           FtpUploadPro upro = (FtpUploadPro)o;
/*  230 */           File file = new File(upro.getFtpUploadConf().getClientPath());
/*      */           
/*  232 */           if (file.isFile())
/*      */           {
/*  234 */             String filename = file.getName();
/*  235 */             FtpFile ftpfile = new FtpFile(filename, false, 0L, null);
/*  236 */             FtpUploadConf fconf = (FtpUploadConf)StringUtil.deepClone(upro.getFtpUploadConf());
/*  237 */             fconf.setServerPath(fconf.getServerPath() + filename);
/*  238 */             fconf.setClientPath(file.getParent());
/*  239 */             FtpDownloadPro dpro = new FtpDownloadPro(fconf, upro.getFtpConf(), ftpfile);
/*  240 */             dproList.add(dpro);
/*      */           }
/*  242 */           if (file.isDirectory())
/*      */           {
/*  244 */             hasfolder = true;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  249 */           hasfolder = true;
/*      */         }
/*      */       }
/*      */       
/*  253 */       if ((dproList.size() == 1) && (!hasfolder))
/*      */       {
/*      */ 
/*      */ 
/*  257 */         FtpDownloadPro dpro = (FtpDownloadPro)dproList.get(0);
/*  258 */         PluginUtil.findServerView(getWorkbenchPage()).innerCompare(new Object[] { dpro });
/*      */       }
/*  260 */       else if ((dproList.size() > 1) || (hasfolder))
/*      */       {
/*  262 */         IFile ifile = PluginUtil.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/comparetemp.v");
/*  263 */         final String tmpeditpath = ifile.getLocation().toFile().getAbsolutePath();
/*  264 */         PluginUtil.findServerView(getWorkbenchPage()).innerDownload(dproList.toArray(), new MySexftpServerDownload() {
/*  265 */           private int downcount = 0;
/*  266 */           private List<FtpUploadPro> equalsList = new ArrayList();
/*  267 */           private List<FtpUploadPro> difList = new ArrayList();
/*  268 */           private List<FtpUploadPro> notExistList = new ArrayList();
/*      */           
/*  270 */           public String trustFolder(FtpDownloadPro dpro) throws Exception { return tmpeditpath; }
/*      */           
/*      */ 
/*      */           public boolean exceptionNotExits(FtpDownloadPro dpro)
/*      */             throws Exception
/*      */           {
/*  276 */             this.notExistList.add(SexftpLocalView.this.downPro2UpPro(dpro));
/*  277 */             this.downcount += 1;
/*  278 */             if (this.downcount >= dproList.size())
/*      */             {
/*  280 */               callback.afterCompareEnd(this.equalsList, this.difList, this.notExistList);
/*      */             }
/*  282 */             return false;
/*      */           }
/*      */           
/*      */           public void afterDownload(FtpDownloadPro dpro) throws Exception
/*      */           {
/*  287 */             String serverMd5 = FileMd5.getMD5(new File(tmpeditpath), null, "");
/*      */             
/*  289 */             File clientfile = new File(dpro.getFtpUploadConf().getClientPath() + "/" + dpro.getFtpfile().getName());
/*      */             
/*  291 */             String clientMd5 = FileMd5.getMD5(clientfile, null, "");
/*  292 */             if (clientMd5.equals(serverMd5))
/*      */             {
/*  294 */               this.equalsList.add(SexftpLocalView.this.downPro2UpPro(dpro));
/*      */             }
/*      */             else
/*      */             {
/*  298 */               this.difList.add(SexftpLocalView.this.downPro2UpPro(dpro));
/*      */             }
/*      */             
/*  301 */             this.downcount += 1;
/*  302 */             if (this.downcount >= dproList.size())
/*      */             {
/*  304 */               callback.afterCompareEnd(this.equalsList, this.difList, this.notExistList);
/*      */             }
/*      */           }
/*  307 */         }, "Prepaer Files Which Different From Server");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void actionCompare_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  315 */     doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro() {
/*      */       public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
/*  317 */         SexftpLocalView.this.innerCompare_actionPerformed(selectOs);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void innerCompare_actionPerformed(Object[] os) throws Exception
/*      */   {
/*  324 */     innerCompareFiles(new IInnerCompareCallback() {
/*      */       public void afterCompareEnd(final List<FtpUploadPro> equalsList, final List<FtpUploadPro> difList, final List<FtpUploadPro> notExistList) {
/*  326 */         Display.getDefault().asyncExec(new Runnable() {
/*      */           public void run() {
/*  328 */             SexftpLocalView.this.customizedImgMap = SexftpLocalView.this.anyaCustomizedImgMap(equalsList, difList, notExistList);
/*  329 */             SexftpLocalView.this.viewer.refresh();
/*      */           }
/*      */           
/*      */         });
/*      */       }
/*  334 */     }, os);
/*  335 */     super.actionCompare_actionPerformed();
/*      */   }
/*      */   
/*      */   private Map<String, String> anyaCustomizedImgMap(List<FtpUploadPro> equalsList, List<FtpUploadPro> difList, List<FtpUploadPro> notExistList)
/*      */   {
/*  340 */     Map<String, String> map = new HashMap();
/*  341 */     File cfile; int i; for (Iterator localIterator = notExistList.iterator(); localIterator.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */         (i < 20) && (cfile != null))
/*      */     {
/*  341 */       FtpUploadPro notExists = (FtpUploadPro)localIterator.next();
/*      */       
/*  343 */       String clianetpah = notExists.getFtpUploadConf().getClientPath();
/*  344 */       map.put(clianetpah, "new_con2.gif");
/*  345 */       map.put(new File(clianetpah).getParent(), "foldermodified_pending.gif");
/*      */       
/*  347 */       cfile = new File(clianetpah).getParentFile();
/*  348 */       i = 0; continue;
/*      */       
/*  350 */       map.put(cfile.getAbsolutePath(), "addtoworkset.gif");i++;cfile = cfile.getParentFile();
/*      */     }
/*      */     
/*      */     File cfile;
/*      */     int i;
/*  353 */     for (localIterator = difList.iterator(); localIterator.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  359 */         (i < 20) && (cfile != null))
/*      */     {
/*  353 */       FtpUploadPro dif = (FtpUploadPro)localIterator.next();
/*      */       
/*  355 */       String clianetpah = dif.getFtpUploadConf().getClientPath();
/*  356 */       map.put(clianetpah, "filemodified_pending.gif");
/*      */       
/*  358 */       cfile = new File(clianetpah).getParentFile();
/*  359 */       i = 0; continue;
/*      */       
/*  361 */       map.put(cfile.getAbsolutePath(), "foldermodified_pending.gif");i++;cfile = cfile.getParentFile();
/*      */     }
/*      */     
/*      */     File cfile;
/*      */     int i;
/*  364 */     for (localIterator = equalsList.iterator(); localIterator.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  370 */         (i < 20) && (cfile != null))
/*      */     {
/*  364 */       FtpUploadPro eq = (FtpUploadPro)localIterator.next();
/*      */       
/*  366 */       String clianetpah = eq.getFtpUploadConf().getClientPath();
/*  367 */       map.put(clianetpah, "interceptor-stack.gif");
/*      */       
/*  369 */       cfile = new File(clianetpah).getParentFile();
/*  370 */       i = 0; continue;
/*      */       
/*  372 */       if (map.containsKey(cfile.getAbsolutePath())) {
/*  373 */         map.put(cfile.getAbsolutePath(), "foldermodified_pending.gif");
/*      */       }
/*  370 */       i++;cfile = cfile.getParentFile();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  376 */     return map;
/*      */   }
/*      */   
/*      */   protected boolean copyTreeNodeIndoAfterSelectAndAddChildUploadPro()
/*      */   {
/*  381 */     return false;
/*      */   }
/*      */   
/*      */   public void doAfterSelectAndAddChildUploadPro(final DoAfterSelectAndAddChildUploadPro run) throws Exception {
/*  385 */     getWorkbenchPage();
/*  386 */     final List selectObjs = new ArrayList();
/*  387 */     final AbstractSexftpView.TreeObject[] selectFtpUploadConfNodes = getUpNodes(getSelectNodes(false));
/*  388 */     Job job = new SexftpJob("Prepare Local File Process", this)
/*      */     {
/*      */       protected IStatus srun(IProgressMonitor monitor) throws Exception {
/*  391 */         monitor.beginTask("Prepare Local File Process", -1);
/*  392 */         AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = selectFtpUploadConfNodes).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject treeObject = arrayOfTreeObject1[i];
/*  393 */           AbstractSexftpView.TreeObject[] oktos = (AbstractSexftpView.TreeObject[])null;
/*  394 */           if ((treeObject instanceof AbstractSexftpView.TreeParent))
/*      */           {
/*  396 */             if ((treeObject.getO() instanceof FtpConf))
/*      */             {
/*  398 */               AbstractSexftpView.TreeParent treeParent = (AbstractSexftpView.TreeParent)treeObject;
/*  399 */               oktos = treeParent.getChildren();
/*      */             }
/*      */             else
/*      */             {
/*  403 */               oktos = new AbstractSexftpView.TreeObject[] { treeObject };
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  408 */             selectObjs.add(treeObject.getO());
/*  409 */             continue; }
/*      */           AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/*  411 */           int m = (arrayOfTreeObject2 = oktos).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject oktree = arrayOfTreeObject2[k];
/*      */             
/*  413 */             if ((oktree instanceof AbstractSexftpView.TreeParent))
/*      */             {
/*  415 */               AbstractSexftpView.TreeParent treeParent = (AbstractSexftpView.TreeParent)oktree;
/*  416 */               AbstractSexftpView.TreeParent newtp = treeParent;
/*  417 */               if (SexftpLocalView.this.copyTreeNodeIndoAfterSelectAndAddChildUploadPro())
/*      */               {
/*  419 */                 newtp = new AbstractSexftpView.TreeParent(SexftpLocalView.this, treeParent.getName(), StringUtil.deepClone(treeParent.getO()));
/*  420 */                 newtp.setParent(new AbstractSexftpView.TreeParent(SexftpLocalView.this, "", treeParent.getParent().getO()));
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  427 */               SexftpLocalView.this.addChildUploadPro(newtp, monitor, true);
/*  428 */               selectObjs.add(newtp.getO());
/*  429 */               selectObjs.addAll(Arrays.asList(SexftpLocalView.this.getChildObjects(newtp, monitor)));
/*  430 */               SexftpLocalView.this.refreshTreeView(newtp);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  436 */         run.doafter(selectObjs.toArray(), monitor);
/*  437 */         return Status.OK_STATUS;
/*      */       }
/*  439 */     };
/*  440 */     job.setUser(true);
/*  441 */     job.schedule();
/*      */   }
/*      */   
/*      */   protected void actionPrepareServUpload_actionPerformed() throws Exception
/*      */   {
/*  446 */     final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
/*  447 */     final FtpConf ftpconf = getFtpConfsSelected()[0];
/*  448 */     final IWorkbenchPage activePage = getWorkbenchPage();
/*  449 */     doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro()
/*      */     {
/*      */       public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
/*  452 */         SexftpLocalView.this.innerPrepareServUpload_actionPerformed(selectOs, selectFtpUploadConfNodes, ftpconf, activePage);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public void innerPrepareServUpload_actionPerformed(Object[] selectOs, final AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes, final FtpConf ftpconf, final IWorkbenchPage activePage)
/*      */     throws Exception
/*      */   {
/*  460 */     if (!showQuestion("This Operation Will Compare Data With Server,It's May Take a Long Time,Sure?\r\nWe Suggest You To Use <View Or Upload Local New Modified Files>,It's Only Compare With Last File Upload Point At Local."))
/*      */     {
/*      */ 
/*  463 */       return;
/*      */     }
/*      */     
/*      */ 
/*  467 */     Object[] os = selectOs;
/*      */     
/*      */ 
/*  470 */     innerCompareFiles(new IInnerCompareCallback()
/*      */     {
/*      */       public void afterCompareEnd(final List<FtpUploadPro> equalsList, final List<FtpUploadPro> difList, final List<FtpUploadPro> notExistList)
/*      */       {
/*  474 */         for (int i = 0; i < selectFtpUploadConfNodes.length; i++)
/*      */         {
/*  476 */           AbstractSexftpView.TreeParent tp = selectFtpUploadConfNodes[i];
/*      */           
/*  478 */           AbstractSexftpView.TreeParent newtp = new AbstractSexftpView.TreeParent(SexftpLocalView.this, tp.getName(), StringUtil.deepClone(tp.getO()));
/*  479 */           newtp.setParent(tp.getParent());
/*  480 */           selectFtpUploadConfNodes[i] = newtp;
/*      */         }
/*  482 */         Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this)
/*      */         {
/*      */           public void srun()
/*      */             throws Exception
/*      */           {
/*  487 */             List<FtpUploadPro> pathList = new ArrayList();
/*  488 */             pathList.addAll(difList);
/*  489 */             pathList.addAll(notExistList);
/*  490 */             if (pathList.size() == 0)
/*      */             {
/*  492 */               SexftpLocalView.this.showMessage("No Different Or Modified Files With Server!");
/*  493 */               return;
/*      */             }
/*      */             
/*      */ 
/*  497 */             SexftpLocalView.this.handleSyncTreeNode(pathList, this.val$selectFtpUploadConfNodes, this.val$ftpconf);
/*  498 */             if (!PluginUtil.isSyncViewShowed(this.val$activePage))
/*      */             {
/*  500 */               SexftpLocalView.this.showMessage("The Result Will Show In [Sexftp Synchronize View]");
/*      */             }
/*  502 */             SexftpSyncView syncView = PluginUtil.findAndShowSyncView(this.val$activePage);
/*  503 */             syncView.showDifView(this.val$selectFtpUploadConfNodes, SexftpLocalView.this.anyaCustomizedImgMap(equalsList, difList, notExistList));
/*      */           }
/*      */           
/*      */         });
/*      */       }
/*  508 */     }, os);
/*  509 */     super.actionPrepareServUpload_actionPerformed();
/*      */   }
/*      */   
/*      */ 
/*      */   private void handleSyncTreeNode(List<FtpUploadPro> pathList, AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes, FtpConf ftpconf)
/*      */   {
/*  515 */     Map<String, AbstractSexftpView.TreeParent> folderMap = new HashMap();
/*  516 */     for (FtpUploadPro upro : pathList) {
/*  517 */       String path = new File(upro.getFtpUploadConf().getClientPath()).getAbsolutePath();
/*  518 */       File folder = new File(path).getParentFile();
/*  519 */       if (!folderMap.containsKey(folder.getAbsolutePath())) {
/*      */         AbstractSexftpView.TreeParent[] arrayOfTreeParent;
/*  521 */         int j = (arrayOfTreeParent = selectFtpUploadConfNodes).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeParent tp = arrayOfTreeParent[i];
/*      */           
/*  523 */           FtpUploadConf ftpUp = (FtpUploadConf)tp.getO();
/*  524 */           String ftpUpClientPath = new File(ftpUp.getClientPath()).getAbsolutePath();
/*  525 */           if (path.startsWith(ftpUpClientPath))
/*      */           {
/*      */ 
/*  528 */             FtpUploadConf foldUp = (FtpUploadConf)StringUtil.deepClone(ftpUp);
/*  529 */             foldUp.setClientPath(folder.getAbsolutePath());
/*      */             
/*  531 */             String xd = folder.getAbsolutePath().substring(ftpUpClientPath.length()).replace("\\", "/");
/*  532 */             if (xd.length() > 0)
/*      */             {
/*  534 */               if (!xd.endsWith("/")) xd = xd + "/";
/*  535 */               if (xd.startsWith("/")) xd = xd.substring(1);
/*  536 */               foldUp.setServerPath(ftpUp.getServerPath() + xd);
/*      */               
/*  538 */               FtpUploadPro ftpUpro = new FtpUploadPro(foldUp, ftpconf);
/*  539 */               AbstractSexftpView.TreeParent foldertp = new AbstractSexftpView.TreeParent(this, xd.substring(0, xd.length() - 1), ftpUpro);
/*  540 */               tp.addChild(foldertp);
/*  541 */               folderMap.put(folder.getAbsolutePath(), foldertp); break;
/*      */             }
/*      */             
/*      */ 
/*  545 */             folderMap.put(folder.getAbsolutePath(), tp);
/*      */             
/*  547 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  551 */       AbstractSexftpView.TreeParent foldertp = (AbstractSexftpView.TreeParent)folderMap.get(folder.getAbsolutePath());
/*  552 */       if (foldertp != null)
/*      */       {
/*  554 */         FtpUploadConf ftpUploadConf = null;
/*  555 */         if ((foldertp.getO() instanceof FtpUploadConf))
/*      */         {
/*  557 */           ftpUploadConf = (FtpUploadConf)foldertp.getO();
/*      */         }
/*      */         else
/*      */         {
/*  561 */           FtpUploadPro fupro = (FtpUploadPro)foldertp.getO();
/*  562 */           ftpUploadConf = fupro.getFtpUploadConf();
/*      */         }
/*  564 */         FtpUploadConf fuconf = (FtpUploadConf)StringUtil.deepClone(ftpUploadConf);
/*  565 */         fuconf.setClientPath(path);
/*  566 */         FtpUploadPro ffupro = new FtpUploadPro(fuconf, ftpconf);
/*  567 */         AbstractSexftpView.TreeObject fto = new AbstractSexftpView.TreeObject(this, new File(path).getName(), ffupro);
/*  568 */         foldertp.addChild(fto);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void treeExpanded_actionPerformed(TreeExpansionEvent e)
/*      */     throws Exception
/*      */   {
/*  576 */     Object elem = e.getElement();
/*  577 */     if ((elem instanceof AbstractSexftpView.TreeParent))
/*      */     {
/*  579 */       AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent)elem;
/*  580 */       if ((((parent.getO() instanceof FtpUploadConf)) || ((parent.getO() instanceof FtpUploadPro))) && (parent.getChildren().length == 0))
/*      */       {
/*  582 */         addChildUploadPro(parent, null, false);
/*  583 */         refreshTreeView(parent);
/*      */       }
/*      */     }
/*      */     
/*  587 */     super.treeExpanded_actionPerformed(e);
/*      */   }
/*      */   
/*      */   protected void treeCollapsed_actionPerformed(TreeExpansionEvent e)
/*      */     throws Exception
/*      */   {
/*  593 */     super.treeCollapsed_actionPerformed(e);
/*  594 */     Object elem = e.getElement();
/*  595 */     if ((elem instanceof AbstractSexftpView.TreeParent))
/*      */     {
/*  597 */       AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent)elem;
/*  598 */       if ((parent.getO() instanceof FtpUploadConf))
/*      */       {
/*  600 */         parent.removeAll();
/*  601 */         this.viewer.refresh();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void actionLocalEdit_actionPerformed() throws Exception
/*      */   {
/*  608 */     Object[] selectO = getSelectionObjects();
/*  609 */     if ((selectO.length == 1) && ((selectO[0] instanceof FtpUploadPro)))
/*      */     {
/*  611 */       FtpUploadPro dpro = (FtpUploadPro)selectO[0];
/*  612 */       innerEditLocalFile(dpro);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doubleClickAction_actionPerformed() throws Exception
/*      */   {
/*  618 */     super.doubleClickAction_actionPerformed();
/*  619 */     actionLocalEdit_actionPerformed();
/*      */   }
/*      */   
/*      */   protected boolean okPopActionDownload()
/*      */   {
/*  624 */     return true;
/*      */   }
/*      */   
/*      */   protected void actionEdit_actionPerformed() throws Exception
/*      */   {
/*  629 */     super.actionEdit_actionPerformed();
/*  630 */     Object[] selectO = getSelectionObjects();
/*  631 */     if ((selectO.length == 1) && ((selectO[0] instanceof FtpUploadPro)))
/*      */     {
/*  633 */       FtpUploadPro upro = (FtpUploadPro)selectO[0];
/*  634 */       FtpFile ftfile = new FtpFile(new File(upro.getFtpUploadConf().getClientPath()).getName(), false, 0L, Calendar.getInstance());
/*  635 */       XStream xstream = new XStream();
/*  636 */       FtpUploadConf up = (FtpUploadConf)xstream.fromXML(xstream.toXML(upro.getFtpUploadConf()));
/*  637 */       up.setServerPath(up.getServerPath() + ftfile.getName());
/*  638 */       FtpDownloadPro dpro = new FtpDownloadPro(up, upro.getFtpConf(), ftfile);
/*  639 */       SexftpServerView mv = PluginUtil.findServerView(getWorkbenchPage());
/*  640 */       mv.innerEditServerFile(dpro);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void innerEditLocalFile(final FtpUploadPro dpro)
/*      */     throws Exception
/*      */   {
/*  648 */     String clientPath = dpro.getFtpUploadConf().getClientPath();
/*  649 */     File f = new File(clientPath);
/*  650 */     if (!f.isFile()) return;
/*  651 */     IProject[] arrayOfIProject; int j = (arrayOfIProject = PluginUtil.getAllOpenedProjects()).length; for (int i = 0; i < j; i++) { IProject p = arrayOfIProject[i];
/*      */       
/*  653 */       String projectPath = p.getFile("/a.txt").getLocation().toFile().getParent();
/*  654 */       if (f.getAbsolutePath().startsWith(projectPath))
/*      */       {
/*  656 */         String ipath = f.getAbsolutePath().substring(projectPath.length());
/*  657 */         IFile ifile = p.getFile(ipath);
/*      */         
/*  659 */         ifile.refreshLocal(1, null);
/*  660 */         if (ifile.exists()) {
/*  661 */           IDE.openEditor(getWorkbenchPage(), ifile);
/*  662 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  666 */     IFile ifile = PluginUtil.createSexftpIFileFromPath("/.seredittemp/" + System.currentTimeMillis() + "/" + f.getName());
/*  667 */     File profile = ifile.getLocation().toFile();
/*  668 */     if (!profile.getParentFile().exists())
/*      */     {
/*  670 */       profile.getParentFile().mkdirs();
/*      */     }
/*  672 */     IFile inewfile = ifile;
/*  673 */     FileUtil.copyFile(f.getAbsolutePath(), profile.getAbsolutePath());
/*  674 */     ifile.refreshLocal(1, null);
/*  675 */     Charset c = org.sexftp.core.utils.Cpdetector.richencode(new FileInputStream(profile));
/*  676 */     if ((c != null) && (c.toString().indexOf("ASCII") < 0))
/*      */     {
/*  678 */       if ((IDE.getContentType(inewfile) == null) || 
/*  679 */         (IDE.getContentType(inewfile).getDefaultCharset() == null) || 
/*  680 */         (!IDE.getContentType(inewfile).getDefaultCharset().equalsIgnoreCase(c.toString())))
/*      */       {
/*  682 */         inewfile = PluginUtil.rename(ifile, ifile.getName() + ".sf" + c);
/*  683 */         if (IDE.getContentType(inewfile) != null)
/*      */         {
/*  685 */           IDE.getContentType(inewfile).setDefaultCharset(c.toString());
/*      */         }
/*      */         else
/*      */         {
/*  689 */           showMessage("You May Need Set The Text file encoding (" + c + ")");
/*      */         }
/*      */       }
/*      */     }
/*  693 */     final IFile editfile = inewfile;
/*  694 */     org.eclipse.ui.IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(inewfile), "sexftp.editors.inner.SfTextEditor");
/*  695 */     SfTextEditor stextEditor = (SfTextEditor)openEditor;
/*  696 */     stextEditor.setDoSaveListener(new IDoSaveListener()
/*      */     {
/*      */       public void dosave() {
/*  699 */         FileUtil.copyFile(editfile.getLocation().toFile().getAbsolutePath(), dpro.getFtpUploadConf().getClientPath());
/*      */       }
/*      */       
/*      */       public void dispose() {
/*  703 */         File file = editfile.getLocation().toFile();
/*  704 */         File[] arrayOfFile; int j = (arrayOfFile = file.getParentFile().listFiles()).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile[i];
/*      */           
/*  706 */           subfile.delete();
/*      */         }
/*  708 */         file.getParentFile().delete();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void actionEnableHandle()
/*      */   {
/*  715 */     super.actionEnableHandle();
/*  716 */     Object[] os = getSelectionObjects();
/*  717 */     if ((os.length == 1) && ((os[0] instanceof FtpUploadPro)) && (new File(((FtpUploadPro)os[0]).getFtpUploadConf().getClientPath()).isFile()))
/*      */     {
/*  719 */       this.actionLocalEdit.setEnabled(true);
/*  720 */       this.actionEdit.setEnabled(true);
/*      */     }
/*  722 */     this.actionDirectSLocal.setEnabled(false);
/*  723 */     this.actionDirectSServer.setEnabled(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void actionDirectSServer_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  745 */     ISelection selection = this.viewer.getSelection();
/*  746 */     AbstractSexftpView.TreeObject obj = (AbstractSexftpView.TreeObject)((IStructuredSelection)selection).getFirstElement();
/*  747 */     AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
/*  748 */     SexftpServerView mv = PluginUtil.findAndShowServerView(PluginUtil.getActivePage());
/*  749 */     if (selectFtpUploadConfNodes.length >= 1)
/*      */     {
/*  751 */       AbstractSexftpView.TreeParent selectObj = selectFtpUploadConfNodes[0];
/*  752 */       if ((obj.getO() instanceof FtpUploadPro))
/*      */       {
/*  754 */         FtpUploadPro dpro = (FtpUploadPro)obj.getO();
/*  755 */         String clilentPath = dpro.getFtpUploadConf().getClientPath();
/*  756 */         String serverPath = dpro.getFtpUploadConf().getServerPath();
/*  757 */         if (new File(clilentPath).isFile())
/*      */         {
/*  759 */           serverPath = serverPath + new File(clilentPath).getName();
/*      */         }
/*      */         
/*  762 */         AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
/*  763 */         for (int i = 0; i < allFtpUploadConfNodes.length; i++)
/*      */         {
/*  765 */           if (allFtpUploadConfNodes[i] == selectObj)
/*      */           {
/*  767 */             mv.directTo(serverPath, Integer.valueOf(i));
/*  768 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean canEnableUpload()
/*      */   {
/*  777 */     boolean can = false;
/*  778 */     Object[] arrayOfObject; int j = (arrayOfObject = getSelectionObjects()).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/*      */       
/*  780 */       if (((o instanceof FtpConf)) || ((o instanceof FtpUploadPro)) || ((o instanceof FtpUploadConf)))
/*      */       {
/*  782 */         can = true;
/*  783 */         break;
/*      */       }
/*      */     }
/*      */     
/*  787 */     return can;
/*      */   }
/*      */   
/*      */   private void addChildUploadPro(AbstractSexftpView.TreeParent p, IProgressMonitor monitor, boolean includeChild) {
/*  791 */     addChildUploadPro(p, monitor, includeChild, null);
/*      */   }
/*      */   
/*      */   private void addChildUploadPro(AbstractSexftpView.TreeParent p, IProgressMonitor monitor, boolean includeChild, String onlyPath) {
/*  795 */     if ((monitor != null) && (monitor.isCanceled()))
/*      */     {
/*  797 */       throw new AbortException();
/*      */     }
/*  799 */     FtpUploadConf ftpUploadConf = null;
/*  800 */     FtpConf ftpConf = null;
/*  801 */     if ((p.getO() instanceof FtpUploadPro))
/*      */     {
/*  803 */       FtpUploadPro ftpPro = (FtpUploadPro)p.getO();
/*  804 */       ftpUploadConf = ftpPro.getFtpUploadConf();
/*  805 */       ftpConf = ftpPro.getFtpConf();
/*      */     }
/*  807 */     else if ((p.getO() instanceof FtpUploadConf))
/*      */     {
/*  809 */       ftpUploadConf = (FtpUploadConf)p.getO();
/*  810 */       ftpConf = (FtpConf)p.getParent().getO();
/*      */     }
/*      */     else
/*      */     {
/*  814 */       return;
/*      */     }
/*  816 */     File file = new File(ftpUploadConf.getClientPath());
/*  817 */     if ((file.isHidden()) && (file.getParentFile() != null)) return;
/*  818 */     File[] subfiles = file.listFiles();
/*  819 */     if ((file.isDirectory()) && (subfiles != null))
/*      */     {
/*  821 */       if (monitor != null) monitor.subTask("scanning " + file.getAbsolutePath());
/*  822 */       File[] arrayOfFile1; int j = (arrayOfFile1 = subfiles).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile1[i];
/*  823 */         if (!subfile.isHidden()) {
/*  824 */           if (subfile.isDirectory())
/*      */           {
/*  826 */             FtpUploadConf canFtpUploadConf = new FtpUploadConf();
/*  827 */             canFtpUploadConf.setClientPath(subfile.getAbsolutePath());
/*  828 */             canFtpUploadConf.setServerPath(ftpUploadConf.getServerPath() + subfile.getName() + "/");
/*  829 */             canFtpUploadConf.setExcludes(ftpUploadConf.getExcludes());
/*  830 */             canFtpUploadConf.setIncludes(ftpUploadConf.getIncludes());
/*      */             
/*  832 */             if (StringUtil.fileStyleEIMatch(subfile.getAbsolutePath(), ftpUploadConf.getExcludes(), ftpUploadConf.getIncludes()))
/*      */             {
/*      */ 
/*  835 */               AbstractSexftpView.TreeObject exists = existFtpuploadPro(p, canFtpUploadConf.getClientPath());
/*  836 */               AbstractSexftpView.TreeParent newP = new AbstractSexftpView.TreeParent(this, getFileInfo(subfile), new FtpUploadPro(canFtpUploadConf, ftpConf));
/*  837 */               if (exists != null)
/*      */               {
/*  839 */                 newP = (AbstractSexftpView.TreeParent)exists;
/*      */               }
/*      */               else
/*      */               {
/*  843 */                 p.addChild(newP);
/*      */               }
/*  845 */               if (includeChild)
/*      */               {
/*  847 */                 if ((onlyPath == null) || (onlyPath.startsWith(subfile.getAbsolutePath())))
/*      */                 {
/*  849 */                   addChildUploadPro(newP, monitor, includeChild, onlyPath);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  856 */             FtpUploadConf canFtpUploadConf = new FtpUploadConf();
/*  857 */             canFtpUploadConf.setClientPath(subfile.getAbsolutePath());
/*  858 */             canFtpUploadConf.setServerPath(ftpUploadConf.getServerPath());
/*  859 */             canFtpUploadConf.setExcludes(ftpUploadConf.getExcludes());
/*  860 */             canFtpUploadConf.setIncludes(ftpUploadConf.getIncludes());
/*      */             
/*  862 */             if (StringUtil.fileStyleEIMatch(subfile.getAbsolutePath(), ftpUploadConf.getExcludes(), ftpUploadConf.getIncludes()))
/*      */             {
/*  864 */               if (existFtpuploadPro(p, canFtpUploadConf.getClientPath()) == null)
/*      */               {
/*  866 */                 AbstractSexftpView.TreeObject newP = new AbstractSexftpView.TreeObject(this, getFileInfo(subfile), new FtpUploadPro(canFtpUploadConf, ftpConf));
/*  867 */                 p.addChild(newP);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String getFileInfo(File file) {
/*  877 */     if (!file.isFile())
/*      */     {
/*  879 */       return file.getName();
/*      */     }
/*      */     try {
/*  882 */       long size = file.length();
/*  883 */       return String.format("%s ( %s %s )", new Object[] { file.getName(), StringUtil.getHumanSize(size), DateTimeUtils.format(new Date(file.lastModified())) });
/*      */     }
/*      */     catch (Exception e) {
/*  886 */       throw new SRuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private AbstractSexftpView.TreeObject existFtpuploadPro(AbstractSexftpView.TreeParent p, String clientPath) {
/*      */     AbstractSexftpView.TreeObject[] arrayOfTreeObject;
/*  892 */     int j = (arrayOfTreeObject = p.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject to = arrayOfTreeObject[i];
/*      */       
/*  894 */       if ((to.getO() instanceof FtpUploadPro))
/*      */       {
/*  896 */         FtpUploadPro fupro = (FtpUploadPro)to.getO();
/*  897 */         if (fupro.getFtpUploadConf().getClientPath().equals(clientPath))
/*      */         {
/*  899 */           return to;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  904 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean okPopActionUpload()
/*      */   {
/*  909 */     Object[] os = getSelectionObjects();
/*  910 */     if (os.length == 0) return false;
/*  911 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/*  912 */       if (!(o instanceof FtpConf))
/*      */       {
/*      */ 
/*      */ 
/*  916 */         if (!(o instanceof FtpUploadConf))
/*      */         {
/*      */ 
/*      */ 
/*  920 */           if (!(o instanceof FtpUploadPro))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  926 */             return false; } }
/*      */       }
/*      */     }
/*  929 */     return true;
/*      */   }
/*      */   
/*      */   protected void actionUpload_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  935 */     doAfterSelectAndAddChildUploadPro(new DoAfterSelectAndAddChildUploadPro()
/*      */     {
/*      */       public void doafter(Object[] selectOs, IProgressMonitor monitor) throws Exception {
/*  938 */         SexftpLocalView.this.innerUpload_actionPerformed(selectOs);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */   public void innerUpload_actionPerformed(Object[] selectOs)
/*      */   {
/*  946 */     FtpConf ftpConf = null;
/*  947 */     final List<FtpUploadConf> okFtpUploadConfList = new ArrayList();
/*  948 */     Object[] arrayOfObject; int j = (arrayOfObject = selectOs).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/*  949 */       if ((o instanceof FtpUploadPro))
/*      */       {
/*  951 */         FtpUploadPro ftpUploadPro = (FtpUploadPro)o;
/*  952 */         if ((ftpConf != null) && (!ftpConf.equals(ftpUploadPro.getFtpConf())))
/*      */         {
/*  954 */           showMessage("Ftp Config Only Support One Upload At The Same Time,But you Chose More as :[" + ftpConf.getName() + "," + ftpUploadPro.getFtpConf().getName() + "]");
/*  955 */           return;
/*      */         }
/*  957 */         ftpConf = ftpUploadPro.getFtpConf();
/*  958 */         if (new File(ftpUploadPro.getFtpUploadConf().getClientPath()).isFile())
/*  959 */           okFtpUploadConfList.add(ftpUploadPro.getFtpUploadConf());
/*      */       }
/*      */     }
/*  962 */     if (okFtpUploadConfList.size() == 0)
/*      */     {
/*  964 */       showMessage("Nothing For Upload,You Can Try Thes Options Before as :\r\nPrepare Modified Files  For Upload\r\nPrepare All Files For Upload");
/*  965 */       return;
/*      */     }
/*  967 */     final FtpConf ftpConfOk = ftpConf;
/*  968 */     int r = ((Integer)PluginUtil.runAsDisplayThread(new PluginUtil.RunAsDisplayThread()
/*      */     {
/*      */       public Object run() throws Exception {
/*  971 */         UploadConfirmDialog ul = new UploadConfirmDialog(SexftpLocalView.this.getShell(), 
/*  972 */           "Upload To:[" + ftpConfOk.toString() + "]", 
/*  973 */           "Confirm To Upload These [" + okFtpUploadConfList.size() + "] Files:", 
/*  974 */           okFtpUploadConfList, ftpConfOk);
/*  975 */         int r = ul.open();
/*  976 */         return Integer.valueOf(r);
/*      */       }
/*      */     })).intValue();
/*      */     
/*      */ 
/*      */ 
/*  982 */     if (r == 0)
/*      */     {
/*  984 */       Object job = new SexftpJob("Uploading", this)
/*      */       {
/*      */         protected IStatus srun(final IProgressMonitor monitor)
/*      */         {
/*      */           try {
/*  989 */             monitor.beginTask("Uploading", okFtpUploadConfList.size());
/*  990 */             SexftpLocalView.this.openConsole();
/*  991 */             FtpUtil.executeUpload(ftpConfOk, okFtpUploadConfList, new IFtpStreamMonitor() {
/*      */               private FtpUploadConf curftpUploadConf;
/*  993 */               long calSize = 0L;
/*  994 */               long timsta = 0L;
/*  995 */               long speed = 0L;
/*  996 */               boolean smallLeftCompleted = true;
/*  997 */               boolean okCancel = false;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */               public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize, long totalSize, String info)
/*      */               {
/* 1004 */                 if (ftpUploadConf != null)
/*      */                 {
/* 1006 */                   this.curftpUploadConf = ftpUploadConf;
/* 1007 */                   this.calSize = 0L;
/* 1008 */                   this.timsta = System.currentTimeMillis();
/* 1009 */                   return;
/*      */                 }
/* 1011 */                 long secds = System.currentTimeMillis() - this.timsta;
/* 1012 */                 if (secds > 1000L)
/*      */                 {
/* 1014 */                   this.speed = (((float)(uploadedSize - this.calSize) / ((float)secds / 1000.0F)));
/* 1015 */                   this.calSize = uploadedSize;
/* 1016 */                   this.timsta = System.currentTimeMillis();
/*      */                 }
/*      */                 
/* 1019 */                 monitor.subTask(String.format("(%s in %s) %s \r\n uploading %s%s", new Object[] {
/* 1020 */                   StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize), 
/* 1021 */                   (float)this.speed > 1.0E-4F ? StringUtil.getHumanSize(this.speed) + "/s" : "", 
/* 1022 */                   this.curftpUploadConf.getServerPath(), new File(this.curftpUploadConf.getClientPath()).getName() }));
/*      */                 
/*      */ 
/* 1025 */                 this.okCancel = monitor.isCanceled();
/*      */                 
/* 1027 */                 if (totalSize == uploadedSize)
/*      */                 {
/* 1029 */                   monitor.worked(1);
/*      */ 
/*      */                 }
/* 1032 */                 else if ((this.okCancel) && (totalSize - uploadedSize < 102400L))
/*      */                 {
/* 1034 */                   if (this.smallLeftCompleted)
/*      */                   {
/* 1036 */                     SexftpLocalView.this.console("Canncled But Go Ahead Little Left Files!");
/* 1037 */                     this.okCancel = false;
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1046 */                 if (this.okCancel)
/*      */                 {
/* 1048 */                   SexftpLocalView.this.console("Operation Canceled!");
/* 1049 */                   SexftpLocalView.this.console(String.format("Last Uploaded %s Of %s .", new Object[] { StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize) }));
/* 1050 */                   if (totalSize > uploadedSize)
/*      */                   {
/* 1052 */                     SexftpLocalView.this.console(String.format("Warning:Incomplete Upload %s%s %s of %s!", new Object[] {
/* 1053 */                       this.curftpUploadConf.getServerPath(), 
/* 1054 */                       new File(this.curftpUploadConf.getClientPath()).getName(), 
/* 1055 */                       StringUtil.getHumanSize(uploadedSize), StringUtil.getHumanSize(totalSize) }));
/*      */                   }
/* 1057 */                   throw new AbortException();
/*      */                 }
/*      */               }
/*      */               
/*      */               public void printSimple(String info) {
/* 1062 */                 SexftpLocalView.this.console(info);
/*      */               }
/* 1064 */             }, SexftpLocalView.this);
/* 1065 */             if (!monitor.isCanceled())
/*      */             {
/* 1067 */               monitor.subTask("Upload Success!Now ReFormatting...");
/* 1068 */               SexftpLocalView.this.console("Upload Success!Now ReFormatting...");
/* 1069 */               String path = SexftpLocalView.workspacePath + ftpConfOk.getName();
/* 1070 */               FtpUtil.formaterSel(SexftpLocalView.workspaceWkPath, path, okFtpUploadConfList);
/* 1071 */               SexftpLocalView.this.console("Upload Task Finished!");
/*      */             }
/*      */           } catch (Exception e) {
/* 1074 */             SexftpLocalView.this.handleException(e);
/*      */           }
/* 1076 */           return Status.OK_STATUS;
/*      */         }
/*      */         
/* 1079 */       };
/* 1080 */       ((Job)job).setUser(true);
/* 1081 */       ((Job)job).schedule();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void directTo(final String expandClientPath, Integer ftpUploadTreeNodesIndex)
/*      */   {
/* 1106 */     AbstractSexftpView.TreeParent r = null;
/* 1107 */     if (ftpUploadTreeNodesIndex != null)
/*      */     {
/* 1109 */       r = getAllFtpUploadConfNodes()[ftpUploadTreeNodesIndex.intValue()];
/*      */     }
/*      */     else
/*      */     {
/* 1113 */       r = getRoot();
/*      */     }
/* 1115 */     TreeViewUtil.serchTreeData(r, new SearchCallback() {
/*      */       public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject o) {
/* 1117 */         if ((o.getO() instanceof FtpUploadConf))
/*      */         {
/* 1119 */           String client = new File(((FtpUploadConf)o.getO()).getClientPath()).getAbsolutePath();
/* 1120 */           if (expandClientPath.startsWith(client))
/*      */           {
/* 1122 */             ((AbstractSexftpView.TreeParent)o).getChildren();
/*      */             
/*      */ 
/*      */ 
/* 1126 */             SexftpLocalView.this.addChildUploadPro((AbstractSexftpView.TreeParent)o, null, true, new File(expandClientPath).getAbsolutePath());
/*      */             
/* 1128 */             SexftpLocalView.this.directExpand(expandClientPath, o);
/*      */             
/* 1130 */             throw new AbortException();
/*      */           }
/*      */         }
/* 1133 */         return new TreeViewUtil.ThisYourFind(false, true);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void directExpand(final String explandClientPath, final AbstractSexftpView.TreeObject to)
/*      */   {
/* 1140 */     final AbstractSexftpView.TreeObject serchto = TreeViewUtil.serchTreeData(to, new SearchCallback()
/*      */     {
/*      */       public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject newo) {
/* 1143 */         if ((newo.getO() instanceof FtpUploadPro))
/*      */         {
/* 1145 */           String sclient = ((FtpUploadPro)newo.getO()).getFtpUploadConf().getClientPath();
/* 1146 */           String dclient = explandClientPath;
/* 1147 */           sclient = new File(sclient).getAbsolutePath();
/* 1148 */           dclient = new File(dclient).getAbsolutePath();
/* 1149 */           if (sclient.equals(dclient))
/*      */           {
/* 1151 */             return new TreeViewUtil.ThisYourFind(true, false);
/*      */           }
/* 1153 */           if (!dclient.startsWith(sclient))
/*      */           {
/* 1155 */             return new TreeViewUtil.ThisYourFind(false, false);
/*      */           }
/*      */           
/*      */ 
/* 1159 */           return new TreeViewUtil.ThisYourFind(false, true);
/*      */         }
/*      */         
/* 1162 */         return new TreeViewUtil.ThisYourFind(false, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1191 */     });new Thread(new SexftpRun(this)
/*      */     {
/*      */       public void srun()
/*      */         throws Exception
/*      */       {
/* 1170 */         Thread.sleep(100L);
/* 1171 */         Display.getDefault().asyncExec(new SexftpRun(SexftpLocalView.this)
/*      */         {
/*      */           public void srun() throws Exception {
/* 1174 */             if (this.val$serchto != null)
/*      */             {
/* 1176 */               SexftpLocalView.this.viewer.refresh();
/* 1177 */               TreePath tpath = TreeViewUtil.changeTreePath(this.val$serchto);
/* 1178 */               TreeSelection t = new TreeSelection(tpath);
/* 1179 */               SexftpLocalView.this.viewer.setSelection(t);
/* 1180 */               SexftpLocalView.this.console("Direct To Ok:" + this.val$serchto);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1185 */               TreePath tpath = TreeViewUtil.changeTreePath(this.val$to);
/* 1186 */               TreeSelection t = new TreeSelection(tpath);
/* 1187 */               SexftpLocalView.this.viewer.setSelection(t);
/*      */             }
/*      */           }
/*      */         });
/*      */       }
/*      */     })
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1192 */       .start();
/*      */   }
/*      */   
/*      */   protected String getDefaultPathToLocation(Object selectO)
/*      */   {
/* 1197 */     if ((selectO instanceof FtpUploadPro))
/*      */     {
/* 1199 */       return ((FtpUploadPro)selectO).getFtpUploadConf().getClientPath();
/*      */     }
/* 1201 */     return super.getDefaultPathToLocation(selectO);
/*      */   }
/*      */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpLocalView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */