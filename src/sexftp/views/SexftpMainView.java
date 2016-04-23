/*      */ package sexftp.views;
/*      */ 
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.datatransfer.StringSelection;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.io.File;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import org.desy.xbean.XbeanUtil;
/*      */ import org.eclipse.core.resources.IFile;
/*      */ import org.eclipse.core.resources.IProject;
/*      */ import org.eclipse.core.resources.IWorkspace;
/*      */ import org.eclipse.core.resources.IWorkspaceRoot;
/*      */ import org.eclipse.core.runtime.IPath;
/*      */ import org.eclipse.core.runtime.IProgressMonitor;
/*      */ import org.eclipse.core.runtime.IStatus;
/*      */ import org.eclipse.core.runtime.Status;
/*      */ import org.eclipse.core.runtime.content.IContentType;
/*      */ import org.eclipse.core.runtime.jobs.Job;
/*      */ import org.eclipse.jface.action.Action;
/*      */ import org.eclipse.jface.action.ActionContributionItem;
/*      */ import org.eclipse.jface.action.IContributionItem;
/*      */ import org.eclipse.jface.action.IMenuManager;
/*      */ import org.eclipse.jface.action.IToolBarManager;
/*      */ import org.eclipse.jface.action.Separator;
/*      */ import org.eclipse.jface.dialogs.MessageDialogWithToggle;
/*      */ import org.eclipse.jface.viewers.ISelection;
/*      */ import org.eclipse.jface.viewers.IStructuredSelection;
/*      */ import org.eclipse.jface.viewers.TreeExpansionEvent;
/*      */ import org.eclipse.jface.viewers.TreePath;
/*      */ import org.eclipse.jface.viewers.TreeSelection;
/*      */ import org.eclipse.jface.viewers.TreeViewer;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Tree;
/*      */ import org.eclipse.ui.IEditorDescriptor;
/*      */ import org.eclipse.ui.IEditorPart;
/*      */ import org.eclipse.ui.IEditorRegistry;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchPage;
/*      */ import org.eclipse.ui.IWorkbenchWindow;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.eclipse.ui.ide.IDE;
/*      */ import org.eclipse.ui.part.FileEditorInput;
/*      */ import org.sexftp.core.exceptions.AbortException;
/*      */ import org.sexftp.core.exceptions.BizException;
/*      */ import org.sexftp.core.ftp.FtpPools;
/*      */ import org.sexftp.core.ftp.FtpUtil;
/*      */ import org.sexftp.core.ftp.bean.FtpConf;
/*      */ import org.sexftp.core.ftp.bean.FtpDownloadPro;
/*      */ import org.sexftp.core.ftp.bean.FtpFile;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*      */ import org.sexftp.core.utils.FileUtil;
/*      */ import org.sexftp.core.utils.StringUtil;
/*      */ import org.sexftp.core.utils.TreeViewUtil;
/*      */ import org.sexftp.core.utils.TreeViewUtil.ThisYourFind;
/*      */ import sexftp.SexftpJob;
/*      */ import sexftp.SexftpRun;
/*      */ import sexftp.SrcViewable;
/*      */ import sexftp.editors.XMLEditor;
/*      */ import sexftp.editors.viewlis.IDoSaveListener;
/*      */ import sexftp.uils.LangUtil;
/*      */ import sexftp.uils.PluginUtil;
/*      */ 
/*      */ public class SexftpMainView extends AbstractSexftpView
/*      */ {
/*      */   protected void actionLocation_actionPerformed() throws Exception
/*      */   {
/*   78 */     AbstractSexftpView.TreeParent[] selectFtpUploadConfNodes = getSelectFtpUploadConfNodes();
/*   79 */     AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
/*   80 */     AbstractSexftpView.TreeObject[] selectNodes = getSelectNodes(false);
/*   81 */     Object[] selectionObjects = getSelectionObjects();
/*   82 */     String path = null;
/*   83 */     if (selectionObjects.length == 1)
/*      */     {
/*   85 */       path = getDefaultPathToLocation(selectionObjects[0]);
/*      */     }
/*   87 */     final String input = input("Location To", "Location To File Or Folder In Sexftp Viewer\r\nFull Path:Precise Location\r\nFile Name:Only Find In Expanded Tree Node", 
/*      */     
/*   89 */       path != null ? path : "");
/*      */     
/*   91 */     AbstractSexftpView.TreeParent selectObj = null;
/*   92 */     AbstractSexftpView.TreeParent[] arrayOfTreeParent1; int j = (arrayOfTreeParent1 = selectFtpUploadConfNodes).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeParent n = arrayOfTreeParent1[i];
/*      */       
/*   94 */       FtpUploadConf fup = (FtpUploadConf)n.getO();
/*   95 */       if (input.startsWith(fup.getServerPath()))
/*      */       {
/*   97 */         selectObj = n;
/*   98 */         break;
/*      */       }
/*      */     }
/*      */     
/*  102 */     for (int i = 0; i < allFtpUploadConfNodes.length; i++)
/*      */     {
/*  104 */       if (allFtpUploadConfNodes[i] == selectObj)
/*      */       {
/*  106 */         if (input.replace('\\', '/').indexOf("/") >= 0)
/*      */         {
/*  108 */           directTo(input, Integer.valueOf(i));
/*  109 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  117 */     if (input.replace('\\', '/').indexOf("/") >= 0)
/*      */     {
/*  119 */       directTo(input, null);
/*  120 */       return;
/*      */     }
/*      */     
/*  123 */     if (selectNodes.length == 1)
/*      */     {
/*  125 */       AbstractSexftpView.TreeObject to = TreeViewUtil.serchTreeData(selectNodes[0], new org.sexftp.core.utils.SearchCallback()
/*      */       {
/*      */         public TreeViewUtil.ThisYourFind isThisYourFind(AbstractSexftpView.TreeObject o) {
/*  128 */           String path = SexftpMainView.this.getDefaultPathToLocation(o.getO());
/*  129 */           if ((path != null) && (input.equals(new File(path).getName())))
/*      */           {
/*  131 */             return new TreeViewUtil.ThisYourFind(true, false);
/*      */           }
/*  133 */           return new TreeViewUtil.ThisYourFind(false, true);
/*      */         }
/*      */       });
/*  136 */       if (to != null)
/*      */       {
/*  138 */         TreePath tpath = TreeViewUtil.changeTreePath(to);
/*  139 */         TreeSelection t = new TreeSelection(tpath);
/*  140 */         this.viewer.setSelection(t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected String getDefaultPathToLocation(Object selectO)
/*      */   {
/*  148 */     if ((selectO instanceof FtpUploadConf))
/*      */     {
/*  150 */       return ((FtpUploadConf)selectO).getClientPath();
/*      */     }
/*  152 */     return null;
/*      */   }
/*      */   
/*      */   protected void actionApplySexFtpConf_actionPerformed() throws Exception
/*      */   {
/*  157 */     List<String> pathList = new ArrayList();
/*  158 */     String projectName = okPopActionApplySexFtpConf(pathList);
/*  159 */     if (projectName == null)
/*      */     {
/*  161 */       projectName = "SexftpConfig";
/*      */     }
/*      */     
/*  164 */     NewFtpConfDialog n = new NewFtpConfDialog(this.viewer.getTree().getShell(), projectName + new Random().nextInt(100) + ".xml");
/*  165 */     int r = n.open();
/*  166 */     if (r != 0)
/*      */     {
/*  168 */       return;
/*      */     }
/*  170 */     FtpConf ftpConf = n.getFtpconf();
/*  171 */     String confName = ftpConf.getName();
/*  172 */     if (confName == null)
/*      */     {
/*  174 */       throw new BizException("Config File Name Error!");
/*      */     }
/*  176 */     if (new File(workspacePath + confName).exists())
/*      */     {
/*  178 */       throw new BizException("Config File [" + confName + "] Exists!");
/*      */     }
/*      */     
/*  181 */     showMessage("We'll Generate Sexftp Upload Unit XML Config File .\r\nYou May Need Fulfill The XML Config File\r\nAfter Save The XML File,\r\nThe Sexftp Upload Unit Config Will Show In \r\n[Sexftp Local View] and [Sexftp Server View]\r\nAnd Then You Can Use It.\r\nIf You Don't Save,The Sexftp Upload Unit Config File Will Be Deleted.");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  188 */     String projectPath = new File(workspacePath + "/client-temp/").getAbsolutePath();
/*  189 */     if (!projectName.equals("SexftpConfig"))
/*      */     {
/*      */ 
/*  192 */       IProject iproject = org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
/*      */       
/*  194 */       projectPath = iproject.getFile("/a.txt").getLocation().toFile().getParent();
/*      */     }
/*      */     
/*  197 */     String confPath = "/.settings/.sexuftp10/" + confName + ".sfUTF-8";
/*  198 */     IProject wkproject = PluginUtil.getOneOpenedProject();
/*  199 */     IFile file = wkproject.getFile(confPath);
/*  200 */     final File confFile = file.getLocation().toFile();
/*      */     
/*  202 */     if (!confFile.getParentFile().exists())
/*      */     {
/*  204 */       confFile.getParentFile().mkdirs();
/*      */     }
/*      */     
/*  207 */     if (pathList.size() == 0)
/*      */     {
/*  209 */       pathList = Arrays.asList(new String[] { new File(projectPath + "/webroot/").getAbsolutePath() });
/*      */     }
/*      */     
/*  212 */     ftpConf.setName(confName);
/*  213 */     List<FtpUploadConf> ftpUploadConfList = new ArrayList();
/*  214 */     for (String path : pathList) {
/*  215 */       FtpUploadConf ftpUploadConf = new FtpUploadConf();
/*  216 */       ftpUploadConf.setClientPath(path);
/*  217 */       ftpUploadConf.setServerPath(path.replace(projectPath, "").replaceAll("\\\\", "/") + "/");
/*  218 */       ftpUploadConf.setFileMd5("");
/*  219 */       ftpUploadConf.setExcludes(new String[] { "*.svn*", "*/WEB-INF/classes/*.properties" });
/*  220 */       ftpUploadConf.setIncludes(new String[] { "*?.*" });
/*  221 */       ftpUploadConfList.add(ftpUploadConf);
/*      */     }
/*  223 */     ftpConf.setFtpUploadConfList(ftpUploadConfList);
/*  224 */     String bean2xml = XbeanUtil.bean2xml(ftpConf, null);
/*  225 */     bean2xml = StringUtil.replaceAll(bean2xml, "<fileMd5></fileMd5>", "");
/*      */     
/*  227 */     bean2xml = StringUtil.replaceAll(bean2xml, "</serverType>", "</serverType><!-- " + LangUtil.langText(new StringBuilder("Support ").append(FtpPools.FTP_MAP.keySet().toString()).toString()) + " -->");
/*  228 */     bean2xml = StringUtil.replaceAll(bean2xml, "</clientPath>", "</clientPath><!-- " + LangUtil.langText("Client Root Folder Or File Path") + " -->");
/*  229 */     bean2xml = StringUtil.replaceAll(bean2xml, "</serverPath>", "</serverPath><!-- " + LangUtil.langText("Server Root Folder Path") + " -->");
/*  230 */     bean2xml = StringUtil.replaceAll(bean2xml, "</excludes>", "</excludes><!-- " + LangUtil.langText("Exclude Path,? = any character,* = any string") + " -->");
/*  231 */     bean2xml = StringUtil.replaceAll(bean2xml, "</includes>", "</includes><!-- " + LangUtil.langText("Include Path,? = any character,* = any string") + " -->");
/*  232 */     bean2xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!-- you may need use <![CDATA[your string]]> for special char  -->\r\n" + bean2xml;
/*  233 */     FileUtil.writeByte2File(confFile.getAbsolutePath(), StringUtil.getBytes(bean2xml, "utf-8"));
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
/*  246 */     IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
/*  247 */     String id2 = editorRegistry.findEditor("sexftp.editors.XMLEditor").getId();
/*  248 */     file.refreshLocal(1, null);
/*  249 */     IDE.getContentType(file).setDefaultCharset("UTF-8");
/*  250 */     IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(file), id2);
/*  251 */     final XMLEditor xmlEditor = (XMLEditor)openEditor;
/*  252 */     xmlEditor.setDoSaveListener(new IDoSaveListener() {
/*  253 */       private String oldFile = "";
/*      */       
/*      */       public void dosave() {
/*  256 */         try { String xmlconf = FileUtil.getTextFromFile(confFile.getAbsolutePath(), "utf-8");
/*  257 */           FtpConf conf = (FtpConf)XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
/*  258 */           SexftpMainView.this.checkSexFtpConfigFils(conf);
/*  259 */           if ((!this.oldFile.equals(conf.getName())) && (new File(SexftpMainView.workspacePath + conf.getName()).exists()))
/*      */           {
/*  261 */             throw new BizException("[" + conf.getName() + "] Exists!");
/*      */           }
/*  263 */           FileUtil.copyFile(confFile.getAbsolutePath(), SexftpMainView.workspacePath + conf.getName());
/*  264 */           if (this.oldFile.length() == 0)
/*      */           {
/*  266 */             this.oldFile = conf.getName();
/*      */           }
/*      */           String[] arrayOfString;
/*  269 */           int j = (arrayOfString = new String[] { "sexftp.views.MainView", "sexftp.views.ServerView", "sexftp.views.SexftpSyncView" }).length; for (int i = 0; i < j; i++) { String revid = arrayOfString[i];
/*      */             
/*  271 */             SexftpMainView mv = (SexftpMainView)PluginUtil.getActivePage().findView(revid);
/*  272 */             if (mv != null) {
/*  273 */               mv.refreshTreeViewData();
/*  274 */               mv.getViewer().refresh();
/*  275 */               mv.getViewer().expandToLevel(2);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  282 */           Set<String> newPathList = new HashSet();
/*  283 */           for (FtpUploadConf ftpUpConf : conf.getFtpUploadConfList())
/*      */           {
/*      */ 
/*  286 */             String clientPath = new File(ftpUpConf.getClientPath()).getAbsolutePath();
/*  287 */             newPathList.add(clientPath);
/*      */           }
/*  289 */           SexftpMainView.this.inner_formatnew(conf.getName(), false, newPathList, this.oldFile.length() > 0, false);
/*      */         } catch (Exception e) {
/*  291 */           SexftpMainView.this.handleException(e);
/*      */         }
/*      */       }
/*      */       
/*      */       public void dispose() {
/*  296 */         confFile.delete();
/*  297 */         if (xmlEditor.editorCounts() == 1)
/*      */         {
/*  299 */           confFile.getParentFile().delete();
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  304 */     });
/*  305 */     xmlEditor.doSave(null);
/*      */   }
/*      */   
/*      */   protected void actionExpandAll_actionPerformed() throws Exception
/*      */   {
/*  310 */     ISelection selection = this.viewer.getSelection();
/*  311 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  312 */     this.viewer.expandToLevel(obj, 2005);
/*  313 */     super.actionExpandAll_actionPerformed();
/*      */   }
/*      */   
/*      */   protected void actionCollapseAll_actionPerformed() throws Exception
/*      */   {
/*  318 */     ISelection selection = this.viewer.getSelection();
/*  319 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  320 */     this.viewer.collapseToLevel(obj, 1);
/*  321 */     super.actionCollapseAll_actionPerformed();
/*      */   }
/*      */   
/*      */   private void checkSexFtpConfigFils(FtpConf conf)
/*      */   {
/*  326 */     if (conf.getFtpUploadConfList() != null)
/*      */     {
/*  328 */       for (FtpUploadConf ftpUploadConf : conf.getFtpUploadConfList())
/*      */       {
/*  330 */         if (ftpUploadConf.getClientPath() == null)
/*      */         {
/*  332 */           throw new BizException("Client Path Is Empty!");
/*      */         }
/*  334 */         File file = new File(ftpUploadConf.getClientPath());
/*  335 */         if (!file.exists())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  346 */           file.mkdirs();
/*      */         }
/*  348 */         if (ftpUploadConf.getServerPath() == null)
/*      */         {
/*  350 */           throw new BizException("Server Path Is Empty!");
/*      */         }
/*  352 */         if (!ftpUploadConf.getServerPath().endsWith("/"))
/*      */         {
/*  354 */           throw new BizException("[" + ftpUploadConf.getServerPath() + "] Must End With '/'");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  359 */     if ((conf.getName() == null) || (!conf.getName().endsWith(".xml")))
/*      */     {
/*  361 */       throw new BizException("Name [" + conf.getName() + "] Invalid! (ex:Sample.xml)");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionEditSexFtpConf_actionPerformed()
/*      */   {
/*  369 */     ISelection selection = this.viewer.getSelection();
/*  370 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  371 */     AbstractSexftpView.TreeParent p = (AbstractSexftpView.TreeParent)obj;
/*  372 */     final String path = workspacePath + p.getName().split(" \\- ")[0];
/*      */     
/*  374 */     IProject iproject = PluginUtil.getOneOpenedProject();
/*  375 */     if (iproject == null) throw new BizException("We must have one Projects to work!");
/*  376 */     String projectPath = PluginUtil.getProjectRealPath(iproject);
/*      */     
/*  378 */     String confPathInProjectAb = "/.settings/.sexuftp10/.editconf/" + new File(path).getName() + ".sfUTF-8";
/*  379 */     final String confPathInProject = projectPath + confPathInProjectAb;
/*      */     
/*  381 */     File confPathFile = new File(confPathInProject);
/*  382 */     if (!confPathFile.getParentFile().exists()) confPathFile.getParentFile().mkdirs();
/*  383 */     FileUtil.copyFile(path, confPathInProject);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  388 */     IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
/*  389 */     String id2 = editorRegistry.findEditor("sexftp.editors.XMLEditor").getId();
/*      */     try {
/*  391 */       IFile file = iproject.getFile(confPathInProjectAb);
/*  392 */       file.refreshLocal(1, null);
/*  393 */       IDE.getContentType(file).setDefaultCharset("UTF-8");
/*  394 */       IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(file), id2);
/*  395 */       final XMLEditor xmlEditor = (XMLEditor)openEditor;
/*      */       
/*  397 */       xmlEditor.setDoSaveListener(new IDoSaveListener() {
/*      */         public void dosave() {
/*      */           try {
/*  400 */             String xmlconf = FileUtil.getTextFromFile(confPathInProject, "utf-8");
/*  401 */             FtpConf conf = (FtpConf)XbeanUtil.xml2Bean(FtpConf.class, xmlconf);
/*      */             
/*      */ 
/*  404 */             SexftpMainView.this.checkSexFtpConfigFils(conf);
/*      */             
/*      */ 
/*  407 */             File oldFile = new File(path);
/*  408 */             if (oldFile.getName().equals(conf.getName())) {
/*  409 */               FileUtil.copyFile(confPathInProject, path);
/*      */             }
/*      */             else {
/*  412 */               if (new File(oldFile.getParent() + "/" + conf.getName()).exists())
/*      */               {
/*  414 */                 throw new BizException("[" + conf.getName() + "] Exists!");
/*      */               }
/*  416 */               FileUtil.copyFile(confPathInProject, oldFile.getParent() + "/" + conf.getName());
/*  417 */               oldFile.delete();
/*      */             }
/*      */             
/*      */             String[] arrayOfString;
/*  421 */             int j = (arrayOfString = new String[] { "sexftp.views.MainView", "sexftp.views.ServerView", "sexftp.views.SexftpSyncView" }).length; for (int i = 0; i < j; i++) { String revid = arrayOfString[i];
/*      */               
/*  423 */               SexftpMainView mv = (SexftpMainView)PluginUtil.getActivePage().findView(revid);
/*  424 */               if (mv != null) {
/*  425 */                 mv.refreshTreeViewData();
/*  426 */                 mv.getViewer().refresh();
/*  427 */                 mv.getViewer().expandToLevel(2);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  432 */             Set<String> newPathList = new HashSet();
/*      */             
/*  434 */             for (FtpUploadConf ftpUpConf : conf.getFtpUploadConfList())
/*      */             {
/*  436 */               String clientPath = new File(ftpUpConf.getClientPath()).getAbsolutePath();
/*  437 */               newPathList.add(clientPath);
/*      */             }
/*      */             
/*      */ 
/*  441 */             SexftpMainView.this.inner_formatnew(conf.getName(), false, newPathList, true, false);
/*      */ 
/*      */           }
/*      */           catch (Exception e)
/*      */           {
/*  446 */             SexftpMainView.this.handleException(e);
/*      */           }
/*      */         }
/*      */         
/*      */         public void dispose() {
/*  451 */           new File(confPathInProject).delete();
/*  452 */           if (xmlEditor.editorCounts() == 1)
/*      */           {
/*  454 */             new File(confPathInProject).getParentFile().delete();
/*  455 */             new File(confPathInProject).getParentFile().getParentFile().delete();
/*      */           }
/*      */           
/*      */         }
/*      */       });
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  463 */       handleException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void actionDeleteSexFtpConf_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  471 */     ISelection selection = this.viewer.getSelection();
/*  472 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  473 */     AbstractSexftpView.TreeParent p = (AbstractSexftpView.TreeParent)obj;
/*      */     
/*  475 */     if (!showQuestion("Sure To Delete The Sexftp Config File [" + p.getName().split(" \\- ")[0] + "] ?"))
/*      */     {
/*  477 */       return;
/*      */     }
/*      */     
/*  480 */     String path = workspacePath + p.getName().split(" \\- ")[0];
/*  481 */     new File(path).delete();
/*  482 */     actionRefreshSexftp_actionPerformed();
/*      */   }
/*      */   
/*      */   protected void actionExplorer_actionPerformed() throws Exception {
/*  486 */     ISelection selection = this.viewer.getSelection();
/*  487 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  488 */     AbstractSexftpView.TreeObject p = (AbstractSexftpView.TreeObject)obj;
/*  489 */     String path = "";
/*  490 */     if ((p.getO() instanceof IFile))
/*      */     {
/*  492 */       path = ((IFile)p.getO()).getLocation().toFile().getAbsolutePath();
/*      */     }
/*  494 */     else if ((p.getO() instanceof IProject))
/*      */     {
/*  496 */       path = ((IProject)p.getO()).getFile("/a.txt").getLocation().toFile().getParent();
/*      */     }
/*  498 */     else if ((p.getO() instanceof FtpUploadConf))
/*      */     {
/*  500 */       path = ((FtpUploadConf)p.getO()).getClientPath();
/*      */     }
/*  502 */     else if ((p.getO() instanceof FtpUploadPro))
/*      */     {
/*  504 */       path = ((FtpUploadPro)p.getO()).getFtpUploadConf().getClientPath();
/*      */     }
/*  506 */     else if ((p.getO() instanceof FtpDownloadPro))
/*      */     {
/*  508 */       path = ((FtpDownloadPro)p.getO()).getFtpUploadConf().getClientPath();
/*      */     }
/*  510 */     if (path.length() > 0)
/*      */     {
/*  512 */       File pathfile = new File(path);
/*  513 */       if (!pathfile.exists()) return;
/*  514 */       if (pathfile.isFile())
/*      */       {
/*  516 */         path = pathfile.getParent();
/*      */       }
/*      */       else
/*      */       {
/*  520 */         path = pathfile.getAbsolutePath();
/*      */       }
/*      */       
/*  523 */       Runtime.getRuntime().exec("explorer " + path);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void actionCopyQualifiedName_actionPerformed() throws Exception {
/*  528 */     Object[] objes = getSelectionObjects();
/*  529 */     StringBuffer sb = new StringBuffer();
/*  530 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = objes).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/*  531 */       sb.append(o);sb.append("\r\n");
/*      */     }
/*  533 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/*  534 */     Object tText = new StringSelection(sb.toString().trim());
/*  535 */     systemClipboard.setContents((Transferable)tText, null);
/*      */   }
/*      */   
/*      */   protected void actionCopy_actionPerformed() throws Exception {
/*  539 */     ISelection selection = this.viewer.getSelection();
/*  540 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  541 */     AbstractSexftpView.TreeObject p = (AbstractSexftpView.TreeObject)obj;
/*  542 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/*  543 */     Transferable tText = new StringSelection(p.getName());
/*  544 */     systemClipboard.setContents(tText, null);
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
/*      */   protected void actionFormat_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  559 */     if (showQuestion("After Format,New Modify Checking Will Based On this Result!Sure?"))
/*      */     {
/*  561 */       Object[] selectOs = getSelectionObjects();
/*  562 */       FtpConf[] selectFtpconfs = getFtpConfsSelected();
/*  563 */       if (selectFtpconfs.length > 1)
/*      */       {
/*  565 */         throw new AbortException();
/*      */       }
/*      */       
/*  568 */       String confName = selectFtpconfs[0].getName();
/*  569 */       Set<String> newPathList = new HashSet();
/*  570 */       boolean allformat = false;
/*  571 */       Object[] arrayOfObject1; int j = (arrayOfObject1 = selectOs).length; for (int i = 0; i < j; i++) { Object select = arrayOfObject1[i];
/*      */         
/*  573 */         if ((select instanceof FtpConf))
/*      */         {
/*  575 */           for (FtpUploadConf ftpUpConf : ((FtpConf)select).getFtpUploadConfList())
/*      */           {
/*  577 */             String clientPath = new File(ftpUpConf.getClientPath()).getAbsolutePath();
/*  578 */             newPathList.add(clientPath);
/*      */           }
/*  580 */           allformat = true;
/*  581 */           break;
/*      */         }
/*  583 */         if ((select instanceof FtpUploadConf))
/*      */         {
/*  585 */           newPathList.add(((FtpUploadConf)select).getClientPath());
/*      */         }
/*  587 */         else if ((select instanceof FtpUploadPro))
/*      */         {
/*  589 */           newPathList.add(((FtpUploadPro)select).getFtpUploadConf().getClientPath());
/*      */         }
/*  591 */         else if ((select instanceof FtpDownloadPro))
/*      */         {
/*  593 */           newPathList.add(downPro2UpPro((FtpDownloadPro)select).getFtpUploadConf().getClientPath());
/*      */         }
/*      */       }
/*      */       
/*  597 */       newPathList = FileUtil.unionUpFilePath(newPathList);
/*  598 */       if (newPathList.size() == 0)
/*      */       {
/*  600 */         throw new BizException("No Files Format!");
/*      */       }
/*  602 */       inner_formatnew(confName, true, newPathList, false, allformat);
/*      */     }
/*      */   }
/*      */   
/*      */   private void actionFormat_innerPerofrmed(String conffilename) throws Exception
/*      */   {
/*  608 */     actionFormat_innerPerofrmed(conffilename, false, null);
/*      */   }
/*      */   
/*      */   public void actionFormat_innerPerofrmed(String conffilename, final boolean needAnswer, final List<FtpUploadConf> ftpUploadConfList) throws Exception
/*      */   {
/*  613 */     final String path = workspacePath + conffilename;
/*      */     
/*  615 */     Job job = new SexftpJob("Format", this)
/*      */     {
/*      */       protected IStatus srun(IProgressMonitor monitor) throws Exception {
/*  618 */         monitor.beginTask("Formating...", -1);
/*  619 */         if (ftpUploadConfList != null)
/*      */         {
/*  621 */           FtpUtil.formaterSel(SexftpMainView.workspaceWkPath, path, ftpUploadConfList);
/*      */         }
/*      */         else
/*      */         {
/*  625 */           FtpUtil.formater(SexftpMainView.workspaceWkPath, path);
/*      */         }
/*  627 */         Display.getDefault().asyncExec(new Runnable() {
/*      */           public void run() {
/*  629 */             if (this.val$needAnswer)
/*  630 */               SexftpMainView.this.showMessage("Format Success!");
/*      */           }
/*  632 */         });
/*  633 */         return Status.OK_STATUS;
/*      */       }
/*  635 */     };
/*  636 */     job.setUser(true);
/*  637 */     job.schedule(); }
/*      */   
/*  639 */   private Integer ignorNoPrjFiles = null;
/*      */   
/*      */   public void inner_formatnew(String conffilename, final boolean needAnswer, final Set<String> newPathLIst, final boolean skipOld, final boolean allFormat) throws Exception {
/*  642 */     final File lastModifyFile = new File(workspaceWkPath + conffilename + "/lastModMap.d");
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
/*  659 */     Job job = new SexftpJob("Format Local File Upload Point", this) {
/*  660 */       private int formatCounts = 0;
/*      */       
/*      */ 
/*      */       protected IStatus srun(IProgressMonitor monitor)
/*      */         throws Exception
/*      */       {
/*  666 */         Set<String> oldPathSet = new HashSet();
/*  667 */         Map<String, String> lastModMap = null;
/*  668 */         String oldPath; int oretur; if (!lastModifyFile.exists())
/*      */         {
/*  670 */           lastModifyFile.getParentFile().mkdirs();
/*  671 */           lastModMap = new HashMap();
/*      */         }
/*      */         else
/*      */         {
/*  675 */           lastModMap = FtpUtil.readLastModMap(lastModifyFile.getParent());
/*  676 */           if ((skipOld) && (!allFormat))
/*      */           {
/*  678 */             oldPathSet = FileUtil.unionUpFilePath(lastModMap.keySet());
/*  679 */             Set<String> needCheckFormatPath = new HashSet();
/*  680 */             StringBuffer needCheckFormatPathStr = new StringBuffer();
/*  681 */             File newfile; for (String newpath : newPathLIst)
/*      */             {
/*  683 */               newfile = new File(newpath);
/*  684 */               if (newfile.exists()) {
/*  685 */                 newpath = newfile.getAbsolutePath();
/*  686 */                 for (Iterator localIterator2 = oldPathSet.iterator(); localIterator2.hasNext();) { oldPath = (String)localIterator2.next();
/*      */                   
/*  688 */                   oldPath = new File(oldPath).getAbsolutePath();
/*  689 */                   if (!newpath.startsWith(oldPath))
/*      */                   {
/*      */ 
/*  692 */                     IProject[] allProjects = PluginUtil.getAllProjects();
/*  693 */                     boolean isInPrj = false;
/*  694 */                     IProject[] arrayOfIProject1; int j = (arrayOfIProject1 = allProjects).length; for (int i = 0; i < j; i++) { IProject iproject = arrayOfIProject1[i];
/*  695 */                       String prjRealPath = PluginUtil.getProjectRealPath(iproject);
/*      */                       
/*  697 */                       if (newpath.startsWith(prjRealPath))
/*      */                       {
/*  699 */                         isInPrj = true;
/*  700 */                         break;
/*      */                       }
/*      */                     }
/*  703 */                     if (!isInPrj)
/*      */                     {
/*  705 */                       if (!needCheckFormatPath.contains(newpath))
/*      */                       {
/*  707 */                         needCheckFormatPathStr.append(newpath);
/*  708 */                         needCheckFormatPathStr.append("\r\n");
/*      */                       }
/*  710 */                       needCheckFormatPath.add(newpath);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } }
/*  715 */             if (needCheckFormatPath.size() > 0)
/*      */             {
/*  717 */               oretur = 0;
/*  718 */               if (SexftpMainView.this.ignorNoPrjFiles == null)
/*      */               {
/*  720 */                 MessageDialogWithToggle showQuestion = SexftpMainView.this.showQuestionInDTread("We'll Format Local File Upload Point Of New Folders Or Files For Local New Modified Files Check Later On,\r\nBut It's Not The Folders Or Files Of Any Projects As Follows:\r\n[" + 
/*      */                 
/*  722 */                   needCheckFormatPathStr.toString().trim() + "]\r\n" + 
/*  723 */                   "Sure Format Upload Point Of Folders Or Files Above?", "Remember Me In Current Session.");
/*  724 */                 if (showQuestion.getToggleState())
/*      */                 {
/*  726 */                   SexftpMainView.this.ignorNoPrjFiles = Integer.valueOf(showQuestion.getReturnCode());
/*      */                 }
/*  728 */                 oretur = showQuestion.getReturnCode();
/*      */               }
/*      */               else
/*      */               {
/*  732 */                 oretur = SexftpMainView.this.ignorNoPrjFiles.intValue();
/*      */               }
/*  734 */               if (oretur == 1)
/*      */               {
/*  736 */                 return Status.CANCEL_STATUS;
/*      */               }
/*  738 */               if (oretur != 2)
/*      */               {
/*  740 */                 for (String noFormat : needCheckFormatPath)
/*      */                 {
/*  742 */                   newPathLIst.remove(noFormat);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
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
/*  762 */         if (allFormat)
/*      */         {
/*  764 */           lastModMap = new HashMap();
/*      */         }
/*  766 */         List<File> list = new ArrayList();
/*  767 */         for (String newpath : newPathLIst)
/*      */         {
/*  769 */           list.addAll(FileUtil.searchFile(new File(newpath), monitor));
/*      */         }
/*      */         
/*  772 */         monitor.beginTask("Format Local File Upload Point...", list.size());
/*      */         
/*  774 */         for (File file : list)
/*      */         {
/*  776 */           if (monitor.isCanceled()) throw new AbortException("User Canceled.");
/*  777 */           boolean okformat = true;
/*  778 */           for (String oldPath : oldPathSet)
/*      */           {
/*  780 */             if (file.getAbsolutePath().startsWith(oldPath))
/*      */             {
/*  782 */               okformat = false;
/*  783 */               break;
/*      */             }
/*      */           }
/*  786 */           if (okformat)
/*      */           {
/*  788 */             monitor.subTask("Formating " + file.getAbsolutePath());
/*  789 */             SexftpMainView.this.console("Formated Local File Upload Point Of " + file.getAbsolutePath());
/*      */             
/*  791 */             lastModMap.put(file.getAbsolutePath(), org.sexftp.core.ftp.FileMd5.getMD5(file, monitor));
/*  792 */             monitor.subTask("waiting..");
/*  793 */             this.formatCounts += 1;
/*      */           }
/*  795 */           monitor.worked(1);
/*      */         }
/*      */         
/*  798 */         FtpUtil.writeLastModMap(lastModifyFile.getParent(), lastModMap);
/*  799 */         Display.getDefault().asyncExec(new Runnable() {
/*      */           public void run() {
/*  801 */             if (this.val$needAnswer)
/*  802 */               SexftpMainView.this.showMessage("Format Local File Upload Point Success,[" + SexftpMainView.5.this.formatCounts + "] Files Formated!");
/*      */           }
/*  804 */         });
/*  805 */         return Status.OK_STATUS;
/*      */       }
/*  807 */     };
/*  808 */     job.setUser(true);
/*  809 */     job.schedule();
/*      */   }
/*      */   
/*      */   protected void actionPrepareUpload_actionPerformed() throws Exception
/*      */   {
/*  814 */     final Set<FtpConf> ftpconfSet = new HashSet();
/*  815 */     HashMap<FtpUploadConf, FtpConf> uploadMapConf = new HashMap();
/*  816 */     ISelection selection = this.viewer.getSelection();
/*  817 */     Iterator it = ((IStructuredSelection)selection).iterator();
/*  818 */     while (it.hasNext())
/*      */     {
/*  820 */       Object o = it.next();
/*  821 */       if ((o instanceof AbstractSexftpView.TreeParent))
/*      */       {
/*  823 */         p = (AbstractSexftpView.TreeParent)o;
/*  824 */         if ((p.getO() instanceof FtpConf))
/*      */         {
/*  826 */           ftpconfSet.add((FtpConf)p.getO());
/*      */         }
/*  828 */         else if ((p.getO() instanceof FtpUploadConf))
/*      */         {
/*  830 */           et = p.getParent();
/*  831 */           ftpconfSet.add((FtpConf)et.getO());
/*  832 */           uploadMapConf.put((FtpUploadConf)p.getO(), (FtpConf)et.getO());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     FtpConf[] arrayOfFtpConf;
/*      */     
/*  840 */     AbstractSexftpView.TreeParent et = (arrayOfFtpConf = (FtpConf[])ftpconfSet.toArray(new FtpConf[0])).length; for (AbstractSexftpView.TreeParent p = 0; p < et; p++) { FtpConf ftpconf = arrayOfFtpConf[p];
/*      */       
/*  842 */       String lastModify = workspaceWkPath + ftpconf.getName() + "/lastModMap.d";
/*  843 */       if (!new File(lastModify).exists())
/*      */       {
/*  845 */         showMessage("No Formated Files,Need Format First,And You Can Get Modified Files Next Time!");
/*  846 */         actionFormat_innerPerofrmed(ftpconf.getName(), true, null);
/*      */         
/*  848 */         return;
/*      */       }
/*      */     }
/*  851 */     final Iterator itagain = ((IStructuredSelection)selection).iterator();
/*  852 */     Job job = new SexftpJob("PrepareModified", this)
/*      */     {
/*      */       protected IStatus srun(IProgressMonitor monitor) throws Exception
/*      */       {
/*  856 */         monitor.beginTask("Prepare Modified File For Upload,doing ...", -1);
/*      */         
/*      */ 
/*  859 */         final List<AbstractSexftpView.TreeObject> needExpandTree = new ArrayList();
/*  860 */         while (itagain.hasNext())
/*      */         {
/*  862 */           Object o = itagain.next();
/*  863 */           if ((o instanceof AbstractSexftpView.TreeParent))
/*      */           {
/*  865 */             AbstractSexftpView.TreeParent p = (AbstractSexftpView.TreeParent)o;
/*  866 */             if ((p.getO() instanceof FtpConf))
/*      */             {
/*  868 */               String path = SexftpMainView.workspacePath + p.getName().split(" \\- ")[0];
/*  869 */               AbstractSexftpView.TreeObject[] children = p.getChildren();
/*  870 */               AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = children).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject ftpUploadTree = arrayOfTreeObject1[i];
/*  871 */                 ((AbstractSexftpView.TreeParent)ftpUploadTree).removeAll();
/*  872 */                 List<FtpUploadConf> canFtpUploadConfList = FtpUtil.anyaCanUploadFiles(SexftpMainView.workspaceWkPath, path, (FtpConf)p.getO(), (FtpUploadConf)ftpUploadTree.getO());
/*  873 */                 for (FtpUploadConf ftpUploadConf : canFtpUploadConfList) {
/*  874 */                   if (StringUtil.fileStyleEIMatch(ftpUploadConf.getClientPath(), ((FtpUploadConf)ftpUploadTree.getO()).getExcludes(), ((FtpUploadConf)ftpUploadTree.getO()).getIncludes()))
/*      */                   {
/*  876 */                     AbstractSexftpView.TreeObject treeObject = new AbstractSexftpView.TreeObject(SexftpMainView.this, ftpUploadConf.toSimpleString(60), new FtpUploadPro(ftpUploadConf, (FtpConf)p.getO()));
/*  877 */                     ((AbstractSexftpView.TreeParent)ftpUploadTree).addChild(treeObject);
/*      */                   }
/*      */                 }
/*  880 */                 needExpandTree.add(ftpUploadTree);
/*      */               }
/*      */             }
/*  883 */             else if ((p.getO() instanceof FtpUploadConf))
/*      */             {
/*  885 */               p.removeAll();
/*  886 */               AbstractSexftpView.TreeParent et = p.getParent();
/*  887 */               if (!ftpconfSet.contains(p.getO()))
/*      */               {
/*  889 */                 String path = SexftpMainView.workspacePath + et.getName().split(" \\- ")[0];
/*  890 */                 List<FtpUploadConf> canFtpUploadConfList = FtpUtil.anyaCanUploadFiles(SexftpMainView.workspaceWkPath, path, (FtpConf)et.getO(), (FtpUploadConf)p.getO());
/*  891 */                 for (FtpUploadConf ftpUploadConf : canFtpUploadConfList) {
/*  892 */                   if (StringUtil.fileStyleEIMatch(ftpUploadConf.getClientPath(), ((FtpUploadConf)p.getO()).getExcludes(), ((FtpUploadConf)p.getO()).getIncludes()))
/*      */                   {
/*  894 */                     AbstractSexftpView.TreeObject treeObject = new AbstractSexftpView.TreeObject(SexftpMainView.this, ftpUploadConf.toSimpleString(60), new FtpUploadPro(ftpUploadConf, (FtpConf)et.getO()));
/*  895 */                     p.addChild(treeObject);
/*      */                   }
/*      */                 }
/*      */               }
/*  899 */               needExpandTree.add(p);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  906 */           Display.getDefault().asyncExec(new Runnable()
/*      */           {
/*      */             public void run() {
/*  909 */               SexftpMainView.this.viewer.setContentProvider(new AbstractSexftpView.ViewContentProvider(SexftpMainView.this));
/*  910 */               for (AbstractSexftpView.TreeObject need : needExpandTree) {
/*  911 */                 SexftpMainView.this.viewer.expandToLevel(need, 1);
/*      */               }
/*      */             }
/*      */           });
/*      */         }
/*      */         
/*      */ 
/*  918 */         return Status.OK_STATUS;
/*      */       }
/*  920 */     };
/*  921 */     job.setUser(true);
/*  922 */     job.schedule();
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
/*      */   protected void actionRefreshFile_actionPerformed()
/*      */     throws Exception
/*      */   {
/*  996 */     AbstractSexftpView.TreeObject[] selectNodes = getSelectNodes(false);
/*  997 */     if ((selectNodes.length == 1) && ((selectNodes[0] instanceof AbstractSexftpView.TreeParent)))
/*      */     {
/*  999 */       AbstractSexftpView.TreeParent parent = (AbstractSexftpView.TreeParent)selectNodes[0];
/* 1000 */       parent.removeAll();
/* 1001 */       treeExpanded_actionPerformed(new TreeExpansionEvent(this.viewer, parent));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void actionRefreshSexftp_actionPerformed()
/*      */   {
/* 1007 */     refreshTreeViewData();
/* 1008 */     this.viewer.setContentProvider(new AbstractSexftpView.ViewContentProvider(this));
/* 1009 */     this.viewer.expandToLevel(2);
/*      */   }
/*      */   
/*      */   protected void refreshPendingTree(final AbstractSexftpView.TreeParent parent, final SexftpRun run) {
/* 1013 */     final AbstractSexftpView.TreeObject pendchild = new AbstractSexftpView.TreeObject(this, "Pending", "");
/* 1014 */     parent.addChild(pendchild);
/* 1015 */     refreshTreeView(parent);
/*      */     
/* 1017 */     Job job = new SexftpJob("Pending Process", this)
/*      */     {
/*      */       protected IStatus srun(IProgressMonitor monitor) throws Exception {
/*      */         try {
/* 1021 */           monitor.beginTask("Pending Process", -1);
/* 1022 */           run.setMonitor(monitor);
/* 1023 */           run.run();
/*      */         } catch (Throwable e) {
/* 1025 */           SexftpMainView.this.handleException(e);
/*      */         }
/* 1027 */         parent.removeChild(pendchild);
/*      */         
/* 1029 */         if (parent.getChildren().length > 0)
/*      */         {
/* 1031 */           AbstractSexftpView.TreeObject selectTo = null;
/* 1032 */           Object returnObject = run.getReturnObject();
/* 1033 */           if ((returnObject != null) && ((returnObject instanceof AbstractSexftpView.TreeObject)))
/*      */           {
/* 1035 */             selectTo = (AbstractSexftpView.TreeObject)returnObject;
/*      */           }
/*      */           
/* 1038 */           SexftpMainView.this.refreshTreeView(parent, selectTo);
/*      */         }
/*      */         else
/*      */         {
/* 1042 */           SexftpMainView.this.backRefreshTreeView(parent);
/*      */         }
/* 1044 */         return Status.OK_STATUS;
/*      */       }
/*      */       
/* 1047 */     };
/* 1048 */     job.setUser(false);
/* 1049 */     job.schedule();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void backRefreshTreeView(final AbstractSexftpView.TreeParent collapseElem)
/*      */   {
/* 1056 */     Display.getDefault().asyncExec(new Runnable() {
/*      */       public void run() {
/*      */         try {
/* 1059 */           Thread.sleep(100L);
/*      */         } catch (InterruptedException e) {
/* 1061 */           e.printStackTrace();
/*      */         }
/* 1063 */         SexftpMainView.this.viewer.setContentProvider(new AbstractSexftpView.ViewContentProvider(SexftpMainView.this));
/* 1064 */         if (collapseElem != null)
/*      */         {
/* 1066 */           SexftpMainView.this.viewer.collapseToLevel(collapseElem, 1);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void refreshTreeView(final AbstractSexftpView.TreeParent explandElem)
/*      */   {
/* 1074 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */ 
/*      */ 
/*      */       public void run()
/*      */       {
/*      */ 
/*      */ 
/* 1082 */         SexftpMainView.this.viewer.refresh(explandElem);
/* 1083 */         if (explandElem != null)
/*      */         {
/* 1085 */           SexftpMainView.this.viewer.expandToLevel(explandElem, 1);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void refreshTreeViewx(final AbstractSexftpView.TreeParent refreshParent) {
/* 1092 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */ 
/*      */ 
/*      */       public void run()
/*      */       {
/*      */ 
/*      */ 
/* 1100 */         SexftpMainView.this.viewer.refresh(refreshParent);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void refreshTreeView(final AbstractSexftpView.TreeParent refreshElem, final AbstractSexftpView.TreeObject expandElem)
/*      */   {
/* 1107 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */ 
/*      */ 
/*      */       public void run()
/*      */       {
/*      */ 
/*      */ 
/* 1115 */         SexftpMainView.this.viewer.refresh(refreshElem);
/* 1116 */         if (expandElem != null)
/*      */         {
/* 1118 */           TreePath tpath = TreeViewUtil.changeTreePath(expandElem);
/* 1119 */           TreeSelection t = new TreeSelection(tpath);
/* 1120 */           SexftpMainView.this.viewer.setSelection(t);
/*      */         }
/* 1122 */         else if (refreshElem != null)
/*      */         {
/* 1124 */           SexftpMainView.this.viewer.expandToLevel(refreshElem, 1);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refreshTreeViewData()
/*      */   {
/* 1134 */     List<FtpConf> allConf = FtpUtil.getAllConf(workspacePath);
/* 1135 */     AbstractSexftpView.TreeParent root = new AbstractSexftpView.TreeParent(this, "Sexftp Start", null);
/* 1136 */     AbstractSexftpView.TreeParent to1; Object localObject; FtpUploadConf ftpUpConf; for (FtpConf ftpConf : allConf) {
/* 1137 */       String conf = String.format("%s - %s:%s@%s", new Object[] { ftpConf.getName(), ftpConf.getHost(), ftpConf.getPort(), ftpConf.getUsername() });
/* 1138 */       to1 = new AbstractSexftpView.TreeParent(this, conf, ftpConf);
/* 1139 */       if (ftpConf.getFtpUploadConfList() != null) {
/* 1140 */         for (localObject = ftpConf.getFtpUploadConfList().iterator(); ((Iterator)localObject).hasNext();) { ftpUpConf = (FtpUploadConf)((Iterator)localObject).next();
/*      */           
/* 1142 */           if ((this instanceof SexftpServerView))
/*      */           {
/* 1144 */             to1.addChild(new AbstractSexftpView.TreeParent(this, ftpUpConf.getServerPath(), ftpUpConf));
/*      */           }
/*      */           else
/*      */           {
/* 1148 */             to1.addChild(new AbstractSexftpView.TreeParent(this, ftpUpConf.toSimpleString(), ftpUpConf)); }
/*      */         }
/*      */       }
/* 1151 */       root.addChild(to1);
/*      */     }
/*      */     
/* 1154 */     if (isShowProjectView())
/*      */     {
/* 1156 */       IProject[] projects = PluginUtil.getAllOpenedProjects();
/* 1157 */       AbstractSexftpView.TreeParent projectsap = new AbstractSexftpView.TreeParent(this, "Projects View", "Projects Applies");
/* 1158 */       root.addChild(projectsap);
/* 1159 */       ftpUpConf = (localObject = projects).length; for (to1 = 0; to1 < ftpUpConf; to1++) { IProject iProject = localObject[to1];
/* 1160 */         AbstractSexftpView.TreeParent p = new AbstractSexftpView.TreeParent(this, iProject.getName(), iProject);
/* 1161 */         projectsap.addChild(p);
/*      */       }
/*      */     }
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
/* 1174 */     this.invisibleRoot = new AbstractSexftpView.TreeParent(this, "", null);
/* 1175 */     this.invisibleRoot.addChild(root);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean isShowProjectView()
/*      */   {
/* 1182 */     return true;
/*      */   }
/*      */   
/*      */   public FtpUploadPro downPro2UpPro(FtpDownloadPro dpro)
/*      */   {
/* 1187 */     FtpUploadConf ftpUploadConf = dpro.getFtpUploadConf();
/* 1188 */     ftpUploadConf = (FtpUploadConf)StringUtil.deepClone(ftpUploadConf);
/* 1189 */     String clientPath = dpro.getFtpUploadConf().getClientPath() + "/" + new File(dpro.getFtpUploadConf().getServerPath()).getName();
/* 1190 */     ftpUploadConf.setClientPath(new File(clientPath).getAbsolutePath());
/* 1191 */     String serverPath = ftpUploadConf.getServerPath().substring(0, ftpUploadConf.getServerPath().lastIndexOf("/") + 1);
/* 1192 */     ftpUploadConf.setServerPath(serverPath);
/* 1193 */     FtpUploadPro upro = new FtpUploadPro(ftpUploadConf, dpro.getFtpConf());
/* 1194 */     return upro;
/*      */   }
/*      */   
/*      */ 
/*      */   public FtpDownloadPro uPro2DownPro(FtpUploadPro upro)
/*      */   {
/* 1200 */     File file = new File(upro.getFtpUploadConf().getClientPath());
/*      */     
/* 1202 */     String filename = file.getName();
/* 1203 */     FtpFile ftpfile = new FtpFile(filename, false, 0L, null);
/* 1204 */     FtpUploadConf fconf = (FtpUploadConf)StringUtil.deepClone(upro.getFtpUploadConf());
/* 1205 */     if (file.isFile())
/*      */     {
/* 1207 */       fconf.setServerPath(fconf.getServerPath() + filename);
/* 1208 */       fconf.setClientPath(file.getParent());
/*      */     }
/* 1210 */     FtpDownloadPro dpro = new FtpDownloadPro(fconf, upro.getFtpConf(), ftpfile);
/* 1211 */     return dpro;
/*      */   }
/*      */   
/*      */   private void addChildOfProjectDir(AbstractSexftpView.TreeParent p, IProgressMonitor monitor)
/*      */   {
/* 1216 */     IFile ifile = (IFile)p.getO();
/* 1217 */     File[] subfiles = ifile.getLocation().toFile().listFiles();
/* 1218 */     if (subfiles == null) return;
/* 1219 */     File[] arrayOfFile1; int j = (arrayOfFile1 = subfiles).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*      */       
/* 1221 */       if ((!file.isHidden()) && 
/* 1222 */         (file.isDirectory()))
/*      */       {
/* 1224 */         if (monitor.isCanceled())
/*      */         {
/* 1226 */           throw new AbortException();
/*      */         }
/* 1228 */         monitor.subTask("scanning " + file.getAbsolutePath());
/* 1229 */         AbstractSexftpView.TreeParent child = new AbstractSexftpView.TreeParent(this, file.getName(), ifile.getProject().getWorkspace().getRoot().getFile(new org.eclipse.core.runtime.Path(ifile.getFullPath() + "/" + file.getName())));
/* 1230 */         p.addChild(child);
/* 1231 */         addChildOfProjectDir(child, monitor);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void doubleClickAction_actionPerformed()
/*      */     throws Exception
/*      */   {
/* 1240 */     ISelection selection = this.viewer.getSelection();
/* 1241 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/* 1242 */     if ((obj instanceof AbstractSexftpView.TreeParent)) {
/* 1243 */       if ((!this.viewer.getExpandedState(obj)) || (((AbstractSexftpView.TreeParent)obj).getChildren().length == 0))
/*      */       {
/* 1245 */         treeExpanded_actionPerformed(new TreeExpansionEvent(this.viewer, obj));
/* 1246 */         this.viewer.expandToLevel(obj, 1);
/*      */       }
/*      */       else
/*      */       {
/* 1250 */         this.viewer.collapseToLevel(obj, 1);
/*      */       }
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
/*      */ 
/*      */   protected void treeExpanded_actionPerformed(TreeExpansionEvent e)
/*      */     throws Exception
/*      */   {
/* 1278 */     super.treeExpanded_actionPerformed(e);
/* 1279 */     Object elem = e.getElement();
/* 1280 */     if ((elem instanceof AbstractSexftpView.TreeParent))
/*      */     {
/* 1282 */       expandProjects((AbstractSexftpView.TreeParent)elem);
/*      */     }
/*      */   }
/*      */   
/*      */   private void expandProjects(final AbstractSexftpView.TreeParent p) {
/* 1287 */     if ((p.getO() instanceof IProject))
/*      */     {
/* 1289 */       refreshPendingTree(p, new SexftpRun(this)
/*      */       {
/*      */         public void srun()
/*      */           throws Exception
/*      */         {
/* 1294 */           IProject project = (IProject)p.getO();
/* 1295 */           File[] subfiles = project.getFile("/a.txt").getLocation().toFile().getParentFile().listFiles();
/* 1296 */           if (subfiles == null) return;
/* 1297 */           File[] arrayOfFile1; int j = (arrayOfFile1 = subfiles).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/* 1298 */             if (file.isDirectory())
/*      */             {
/* 1300 */               AbstractSexftpView.TreeParent child = new AbstractSexftpView.TreeParent(SexftpMainView.this, file.getName(), project.getFile("/" + file.getName()));
/* 1301 */               p.addChild(child);
/* 1302 */               SexftpMainView.this.addChildOfProjectDir(child, getMonitor());
/*      */             }
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void actionEnableHandle()
/*      */   {
/* 1313 */     this.actionRefreshSexftp.setEnabled(true);
/* 1314 */     this.actionRefreshFile.setEnabled(false);
/* 1315 */     this.actionPrepareUpload.setEnabled(false);
/* 1316 */     this.actionApplySexFtpConf.setEnabled(false);
/* 1317 */     this.actionFormat.setEnabled(false);
/* 1318 */     this.actionEditSexFtpConf.setEnabled(false);
/* 1319 */     this.actionPrepareServUpload.setEnabled(false);
/* 1320 */     this.actionUpload.setEnabled(false);
/* 1321 */     this.actionDeleteSexFtpConf.setEnabled(false);
/* 1322 */     this.actionEdit.setEnabled(false);
/* 1323 */     this.actionLocalEdit.setEnabled(false);
/* 1324 */     this.actionDownload.setEnabled(false);
/* 1325 */     this.actionApplySexFtpConf.setEnabled(true);
/* 1326 */     this.actionCompare.setEnabled(false);
/*      */     
/* 1328 */     if (okPopActionEditSexFtpConf())
/*      */     {
/*      */ 
/* 1331 */       this.actionEditSexFtpConf.setEnabled(true);
/* 1332 */       this.actionDeleteSexFtpConf.setEnabled(true);
/*      */     }
/* 1334 */     FtpConf[] ftpConfsSelected = getFtpConfsSelected();
/* 1335 */     if ((okPopActionPrepareUpload()) && (canEnableUpload()))
/*      */     {
/* 1337 */       if (ftpConfsSelected.length == 1)
/*      */       {
/* 1339 */         this.actionPrepareServUpload.setEnabled(true);
/* 1340 */         this.actionPrepareUpload.setEnabled(true);
/*      */       }
/*      */     }
/* 1343 */     if (ftpConfsSelected.length == 1)
/*      */     {
/* 1345 */       this.actionCompare.setEnabled(true);
/* 1346 */       this.actionFormat.setEnabled(true);
/*      */     }
/* 1348 */     if ((okPopActionUpload()) && (ftpConfsSelected.length == 1))
/*      */     {
/* 1350 */       this.actionUpload.setEnabled(true);
/*      */     }
/*      */     
/* 1353 */     if ((okPopActionDownload()) && (ftpConfsSelected.length == 1))
/*      */     {
/* 1355 */       this.actionDownload.setEnabled(true);
/*      */     }
/*      */     
/* 1358 */     if (getSelectionObjects().length == 1)
/*      */     {
/* 1360 */       Object o = getSelectionObjects()[0];
/* 1361 */       if (((o instanceof FtpUploadPro)) || ((o instanceof FtpDownloadPro)) || ((o instanceof FtpUploadConf)))
/*      */       {
/* 1363 */         this.actionRefreshFile.setEnabled(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public FtpConf[] getFtpConfsSelected() {
/* 1369 */     Set<FtpConf> set = new HashSet();
/* 1370 */     ISelection selection = this.viewer.getSelection();
/* 1371 */     Iterator it = ((IStructuredSelection)selection).iterator();
/* 1372 */     while (it.hasNext())
/*      */     {
/* 1374 */       Object o = it.next();
/* 1375 */       if ((o instanceof AbstractSexftpView.TreeObject))
/*      */       {
/* 1377 */         AbstractSexftpView.TreeObject to = (AbstractSexftpView.TreeObject)o;
/* 1378 */         for (int i = 0; (i < 50) && (to != null); i++)
/*      */         {
/* 1380 */           if ((to.getO() instanceof FtpConf))
/*      */           {
/* 1382 */             set.add((FtpConf)to.getO());
/* 1383 */             break;
/*      */           }
/* 1385 */           to = to.getParent();
/*      */         }
/*      */       }
/*      */     }
/* 1389 */     return (FtpConf[])set.toArray(new FtpConf[0]);
/*      */   }
/*      */   
/*      */   protected boolean canEnableUpload() {
/* 1393 */     return false;
/*      */   }
/*      */   
/*      */   protected void actionDisconnect_actionPerformed() throws Exception
/*      */   {
/* 1398 */     FtpPools pool = new FtpPools(null, this);
/* 1399 */     pool.disconnectAll();
/*      */   }
/*      */   
/*      */   protected void menuAboutToShow_event(IMenuManager manager)
/*      */   {
/* 1404 */     actionEnableHandle();
/* 1405 */     manager.add(this.actionRefreshSexftp);
/* 1406 */     manager.add(this.actionRefreshFile);
/* 1407 */     manager.add(new Separator("additions"));
/* 1408 */     manager.add(this.actionApplySexFtpConf);
/* 1409 */     manager.add(this.actionEditSexFtpConf);
/* 1410 */     manager.add(this.actionDeleteSexFtpConf);
/* 1411 */     manager.add(new Separator("additions"));
/* 1412 */     manager.add(this.actionFormat);
/* 1413 */     manager.add(this.actionPrepareUpload);
/* 1414 */     manager.add(this.actionPrepareServUpload);
/*      */     
/* 1416 */     manager.add(new Separator("additions"));
/* 1417 */     manager.add(this.actionUpload);
/* 1418 */     manager.add(this.actionDownload);
/* 1419 */     manager.add(this.actionEdit);
/* 1420 */     manager.add(this.actionLocalEdit);
/* 1421 */     manager.add(this.actionCompare);
/* 1422 */     manager.add(this.actionDisconnect);
/* 1423 */     manager.add(new Separator("additions"));
/* 1424 */     manager.add(this.actionDirectSLocal);
/* 1425 */     manager.add(this.actionDirectSServer);
/* 1426 */     manager.add(this.actionLocationTo);
/* 1427 */     manager.add(new Separator("additions"));
/* 1428 */     manager.add(this.actionCopy);
/* 1429 */     manager.add(this.actionCopyQualifiedName);
/* 1430 */     manager.add(this.actionCopyCientPath);
/* 1431 */     manager.add(this.actionCopyServerPath);
/* 1432 */     manager.add(this.actionExplorer);
/* 1433 */     manager.add(new Separator("additions"));
/* 1434 */     manager.add(this.actionExpandAll);
/* 1435 */     manager.add(this.actionCollapseAll);
/*      */     
/* 1437 */     List hiddenActions = getHiddenActions();
/* 1438 */     IContributionItem[] arrayOfIContributionItem; int j = (arrayOfIContributionItem = manager.getItems()).length; for (int i = 0; i < j; i++) { IContributionItem menuItem = arrayOfIContributionItem[i];
/*      */       
/* 1440 */       if ((menuItem instanceof ActionContributionItem))
/*      */       {
/* 1442 */         ActionContributionItem a = (ActionContributionItem)menuItem;
/* 1443 */         if (hiddenActions.contains(a.getAction()))
/*      */         {
/* 1445 */           manager.remove(menuItem);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1451 */     super.menuAboutToShow_event(manager);
/*      */   }
/*      */   
/*      */   public void fillLocalToolBar(IToolBarManager manager)
/*      */   {
/* 1456 */     super.fillLocalToolBar(manager);
/* 1457 */     List hiddenActions = getHiddenActions();
/* 1458 */     IContributionItem[] arrayOfIContributionItem; int j = (arrayOfIContributionItem = manager.getItems()).length; for (int i = 0; i < j; i++) { IContributionItem menuItem = arrayOfIContributionItem[i];
/*      */       
/* 1460 */       if ((menuItem instanceof ActionContributionItem))
/*      */       {
/* 1462 */         ActionContributionItem a = (ActionContributionItem)menuItem;
/* 1463 */         if (hiddenActions.contains(a.getAction()))
/*      */         {
/* 1465 */           manager.remove(menuItem);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public List getHiddenActions() {
/* 1472 */     return new ArrayList();
/*      */   }
/*      */   
/*      */   protected boolean okPopActionPrepareUpload() {
/* 1476 */     Object[] os = getSelectionObjects();
/* 1477 */     if (os.length == 0) return false;
/* 1478 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 1479 */       if (!(o instanceof FtpConf))
/*      */       {
/*      */ 
/*      */ 
/* 1483 */         if (!(o instanceof FtpUploadConf))
/*      */         {
/*      */ 
/*      */ 
/* 1487 */           if (!(o instanceof FtpUploadPro))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1493 */             return false; } }
/*      */       }
/*      */     }
/* 1496 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean okPopActionUpload()
/*      */   {
/* 1501 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean okPopActionDownload()
/*      */   {
/* 1506 */     return false;
/*      */   }
/*      */   
/*      */   private boolean okPopActionEditSexFtpConf()
/*      */   {
/* 1511 */     Object[] os = getSelectionObjects();
/* 1512 */     if ((os.length == 1) && ((os[0] instanceof FtpConf)))
/*      */     {
/* 1514 */       return true;
/*      */     }
/* 1516 */     return false;
/*      */   }
/*      */   
/*      */   private String okPopActionApplySexFtpConf(List<String> pathList)
/*      */   {
/* 1521 */     Set<String> projectNameSet = new HashSet();
/* 1522 */     String projectName = null;
/* 1523 */     Object[] objes = getSelectionObjects();
/* 1524 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = objes).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 1525 */       if ((o instanceof IFile))
/*      */       {
/* 1527 */         IFile ifile = (IFile)o;
/* 1528 */         projectName = ifile.getProject().getName();
/* 1529 */         projectNameSet.add(projectName);
/* 1530 */         pathList.add(ifile.getLocation().toFile().getAbsolutePath());
/*      */       }
/* 1532 */       else if ((o instanceof IProject))
/*      */       {
/* 1534 */         IProject iproject = (IProject)o;
/* 1535 */         projectName = iproject.getName();
/* 1536 */         projectNameSet.add(projectName);
/* 1537 */         pathList.add(iproject.getFile("/a.txt").getLocation().toFile().getParent());
/*      */       }
/*      */       else
/*      */       {
/* 1541 */         return null;
/*      */       }
/*      */     }
/*      */     
/* 1545 */     if (projectNameSet.size() == 1) { return projectName;
/*      */     }
/* 1547 */     return null;
/*      */   }
/*      */   
/*      */   protected void actionCopyCientPath_actionPerformed() throws Exception
/*      */   {
/* 1552 */     Object[] objes = getSelectionObjects();
/* 1553 */     StringBuffer sb = new StringBuffer();
/* 1554 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = objes).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 1555 */       String serverPath = null;
/* 1556 */       String clientPath = null;
/* 1557 */       if ((o instanceof FtpUploadConf))
/*      */       {
/* 1559 */         serverPath = ((FtpUploadConf)o).getServerPath();
/* 1560 */         clientPath = ((FtpUploadConf)o).getClientPath();
/*      */       }
/* 1562 */       else if ((o instanceof FtpUploadPro))
/*      */       {
/* 1564 */         serverPath = ((FtpUploadPro)o).getFtpUploadConf().getServerPath();
/* 1565 */         clientPath = ((FtpUploadPro)o).getFtpUploadConf().getClientPath();
/*      */       }
/* 1567 */       else if ((o instanceof FtpDownloadPro))
/*      */       {
/* 1569 */         serverPath = ((FtpDownloadPro)o).getFtpUploadConf().getServerPath();
/* 1570 */         clientPath = ((FtpDownloadPro)o).getFtpUploadConf().getClientPath();
/* 1571 */         if (!serverPath.endsWith("/"))
/*      */         {
/* 1573 */           clientPath = clientPath + "/" + new File(serverPath).getName();
/* 1574 */           clientPath = new File(clientPath).getAbsolutePath();
/*      */         }
/*      */       }
/* 1577 */       File client = new File(clientPath);
/* 1578 */       if ((client.isFile()) && (serverPath.endsWith("/")))
/*      */       {
/* 1580 */         serverPath = serverPath + client.getName();
/*      */       }
/* 1582 */       sb.append(clientPath);
/* 1583 */       sb.append("\r\n");
/*      */     }
/* 1585 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 1586 */     Object tText = new StringSelection(sb.toString().trim());
/* 1587 */     systemClipboard.setContents((Transferable)tText, null);
/*      */   }
/*      */   
/*      */   protected void actionCopyServerPath_actionPerformed() throws Exception
/*      */   {
/* 1592 */     Object[] objes = getSelectionObjects();
/* 1593 */     StringBuffer sb = new StringBuffer();
/* 1594 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = objes).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject1[i];
/* 1595 */       String serverPath = null;
/* 1596 */       String clientPath = null;
/* 1597 */       if ((o instanceof FtpUploadConf))
/*      */       {
/* 1599 */         serverPath = ((FtpUploadConf)o).getServerPath();
/* 1600 */         clientPath = ((FtpUploadConf)o).getClientPath();
/*      */       }
/* 1602 */       else if ((o instanceof FtpUploadPro))
/*      */       {
/* 1604 */         serverPath = ((FtpUploadPro)o).getFtpUploadConf().getServerPath();
/* 1605 */         clientPath = ((FtpUploadPro)o).getFtpUploadConf().getClientPath();
/*      */       }
/* 1607 */       else if ((o instanceof FtpDownloadPro))
/*      */       {
/* 1609 */         serverPath = ((FtpDownloadPro)o).getFtpUploadConf().getServerPath();
/* 1610 */         clientPath = ((FtpDownloadPro)o).getFtpUploadConf().getClientPath();
/* 1611 */         if (!serverPath.endsWith("/"))
/*      */         {
/* 1613 */           clientPath = clientPath + "/" + new File(serverPath).getName();
/* 1614 */           clientPath = new File(clientPath).getAbsolutePath();
/*      */         }
/*      */       }
/* 1617 */       File client = new File(clientPath);
/* 1618 */       if ((client.isFile()) && (serverPath.endsWith("/")))
/*      */       {
/* 1620 */         serverPath = serverPath + client.getName();
/*      */       }
/* 1622 */       sb.append(serverPath);
/* 1623 */       sb.append("\r\n");
/*      */     }
/* 1625 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 1626 */     Object tText = new StringSelection(sb.toString().trim());
/* 1627 */     systemClipboard.setContents((Transferable)tText, null);
/*      */   }
/*      */   
/*      */   protected Object[] getSelectionObjects()
/*      */   {
/* 1632 */     return getSelectionObjects(false);
/*      */   }
/*      */   
/*      */   protected Object[] getSelectionObjects(boolean includChild) {
/* 1636 */     Set r = new HashSet();
/* 1637 */     ISelection selection = this.viewer.getSelection();
/* 1638 */     Iterator it = ((IStructuredSelection)selection).iterator();
/* 1639 */     while (it.hasNext())
/*      */     {
/* 1641 */       Object o = it.next();
/* 1642 */       if ((o instanceof AbstractSexftpView.TreeParent))
/*      */       {
/* 1644 */         AbstractSexftpView.TreeParent p = (AbstractSexftpView.TreeParent)o;
/* 1645 */         r.add(p.getO());
/* 1646 */         if (includChild) {
/* 1647 */           r.addAll(Arrays.asList(getChildObjects(p, null)));
/*      */         }
/* 1649 */       } else if ((o instanceof AbstractSexftpView.TreeObject))
/*      */       {
/* 1651 */         AbstractSexftpView.TreeObject p = (AbstractSexftpView.TreeObject)o;
/* 1652 */         r.add(p.getO());
/*      */       }
/*      */       else
/*      */       {
/* 1656 */         r.add("Unknown");
/*      */       }
/*      */     }
/* 1659 */     return r.toArray();
/*      */   }
/*      */   
/*      */ 
/*      */   protected Object[] getChildObjects(AbstractSexftpView.TreeParent p, IProgressMonitor monitor)
/*      */   {
/* 1665 */     if ((monitor != null) && (monitor.isCanceled()))
/*      */     {
/* 1667 */       throw new AbortException();
/*      */     }
/* 1669 */     List r = new ArrayList();
/* 1670 */     AbstractSexftpView.TreeObject[] children = p.getChildren();
/* 1671 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = children).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject child = arrayOfTreeObject1[i];
/* 1672 */       r.add(child.getO());
/* 1673 */       if ((child instanceof AbstractSexftpView.TreeParent))
/*      */       {
/* 1675 */         if (monitor != null) monitor.subTask("Prepareing data in Node " + child.toString());
/* 1676 */         r.addAll(Arrays.asList(getChildObjects((AbstractSexftpView.TreeParent)child, monitor)));
/*      */       }
/*      */     }
/* 1679 */     return r.toArray();
/*      */   }
/*      */   
/*      */   private AbstractSexftpView.TreeObject[] getChildNode(AbstractSexftpView.TreeParent p)
/*      */   {
/* 1684 */     List<AbstractSexftpView.TreeObject> r = new ArrayList();
/* 1685 */     AbstractSexftpView.TreeObject[] children = p.getChildren();
/* 1686 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = children).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject child = arrayOfTreeObject1[i];
/* 1687 */       r.add(child);
/* 1688 */       if ((child instanceof AbstractSexftpView.TreeParent))
/*      */       {
/* 1690 */         r.addAll(Arrays.asList(getChildNode((AbstractSexftpView.TreeParent)child)));
/*      */       }
/*      */     }
/* 1693 */     return (AbstractSexftpView.TreeObject[])r.toArray(new AbstractSexftpView.TreeObject[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeObject[] getUpNodes(AbstractSexftpView.TreeObject[] nodes) {
/* 1697 */     Set<AbstractSexftpView.TreeObject> set = new LinkedHashSet(Arrays.asList(nodes));
/* 1698 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject; int j = (arrayOfTreeObject = nodes).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject nod = arrayOfTreeObject[i];
/*      */       
/* 1700 */       AbstractSexftpView.TreeObject c = nod;
/* 1701 */       for (int i = 0; (i < 50) && (c != null) && (c != nod); c = c.getParent())
/*      */       {
/* 1703 */         if (set.contains(c))
/*      */         {
/* 1705 */           set.remove(nod);
/* 1706 */           break;
/*      */         }
/* 1701 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1710 */     return (AbstractSexftpView.TreeObject[])set.toArray(new AbstractSexftpView.TreeObject[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeObject[] getSelectNodes(boolean includeChild) {
/* 1714 */     List<AbstractSexftpView.TreeObject> r = new ArrayList();
/* 1715 */     ISelection selection = this.viewer.getSelection();
/* 1716 */     Iterator it = ((IStructuredSelection)selection).iterator();
/* 1717 */     while (it.hasNext())
/*      */     {
/* 1719 */       Object o = it.next();
/* 1720 */       if ((o instanceof AbstractSexftpView.TreeParent))
/*      */       {
/* 1722 */         AbstractSexftpView.TreeParent p = (AbstractSexftpView.TreeParent)o;
/* 1723 */         r.add(p);
/* 1724 */         if (includeChild) {
/* 1725 */           r.addAll(Arrays.asList(getChildNode(p)));
/*      */         }
/*      */       }
/*      */       else {
/* 1729 */         r.add((AbstractSexftpView.TreeObject)o);
/*      */       }
/*      */     }
/* 1732 */     return (AbstractSexftpView.TreeObject[])r.toArray(new AbstractSexftpView.TreeObject[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeParent[] getAllFtpConfNodes() {
/* 1736 */     List<AbstractSexftpView.TreeParent> list = new ArrayList();
/* 1737 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
/*      */       AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/* 1739 */       int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)ftpstart).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];
/*      */         
/* 1741 */         AbstractSexftpView.TreeParent ftpConfNodeIn = (AbstractSexftpView.TreeParent)ftpConfNode;
/* 1742 */         list.add(ftpConfNodeIn);
/*      */       }
/*      */     }
/*      */     
/* 1746 */     return (AbstractSexftpView.TreeParent[])list.toArray(new AbstractSexftpView.TreeParent[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeParent[] getAllFtpUploadConfNodes()
/*      */   {
/* 1751 */     List<AbstractSexftpView.TreeParent> list = new ArrayList();
/* 1752 */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
/*      */       AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/* 1754 */       int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)ftpstart).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];
/*      */         
/* 1756 */         AbstractSexftpView.TreeParent ftpConfNodeIn = (AbstractSexftpView.TreeParent)ftpConfNode;
/* 1757 */         AbstractSexftpView.TreeObject[] arrayOfTreeObject3; int i1 = (arrayOfTreeObject3 = ftpConfNodeIn.getChildren()).length; for (int n = 0; n < i1; n++) { AbstractSexftpView.TreeObject ftpUploadConfNode = arrayOfTreeObject3[n];
/*      */           
/* 1759 */           list.add((AbstractSexftpView.TreeParent)ftpUploadConfNode);
/*      */         }
/*      */       }
/*      */     }
/* 1763 */     return (AbstractSexftpView.TreeParent[])list.toArray(new AbstractSexftpView.TreeParent[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeParent[] getSelectFtpUploadConfNodes()
/*      */   {
/* 1768 */     Set<AbstractSexftpView.TreeParent> nodset = new HashSet();
/* 1769 */     Object[] selectFtpConfNodes = getSelectNodes(true);
/* 1770 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = selectFtpConfNodes).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject1[i];
/* 1771 */       AbstractSexftpView.TreeObject to = (AbstractSexftpView.TreeObject)object;
/* 1772 */       for (int i = 0; (i < 10) && (to != null); i++)
/*      */       {
/* 1774 */         if ((to.getO() instanceof FtpUploadConf))
/*      */         {
/* 1776 */           nodset.add((AbstractSexftpView.TreeParent)to);
/* 1777 */           break;
/*      */         }
/* 1779 */         to = to.getParent();
/*      */       }
/*      */     }
/* 1782 */     return (AbstractSexftpView.TreeParent[])nodset.toArray(new AbstractSexftpView.TreeParent[0]);
/*      */   }
/*      */   
/*      */   public AbstractSexftpView.TreeObject[] getSelectFtpconfNodes()
/*      */   {
/* 1787 */     Set<AbstractSexftpView.TreeObject> nodset = new HashSet();
/* 1788 */     Object[] selectFtpConfNodes = getSelectNodes(false);
/* 1789 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = selectFtpConfNodes).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject1[i];
/* 1790 */       AbstractSexftpView.TreeObject to = (AbstractSexftpView.TreeObject)object;
/* 1791 */       for (int i = 0; (i < 10) && (to != null); i++)
/*      */       {
/* 1793 */         if ((to.getO() instanceof FtpConf))
/*      */         {
/* 1795 */           nodset.add(to);
/* 1796 */           break;
/*      */         }
/* 1798 */         to = to.getParent();
/*      */       }
/*      */     }
/* 1801 */     return (AbstractSexftpView.TreeObject[])nodset.toArray(new AbstractSexftpView.TreeObject[0]);
/*      */   }
/*      */   
/*      */   public void refreshSelectNode(boolean includeChild)
/*      */   {
/* 1806 */     Object[] os = getSelectNodes(includeChild);
/* 1807 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = os).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject1[i];
/* 1808 */       this.viewer.refresh(object, true);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpMainView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */