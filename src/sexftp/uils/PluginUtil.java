/*     */ package sexftp.uils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.compare.CompareConfiguration;
/*     */ import org.eclipse.compare.CompareEditorInput;
/*     */ import org.eclipse.compare.structuremergeviewer.DiffNode;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.jface.preference.IPreferenceStore;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PartInitException;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.sexftp.core.exceptions.SRuntimeException;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ import sexftp.Activator;
/*     */ import sexftp.editors.inner.SexftpCompareEditor;
/*     */ import sexftp.views.SexftpLocalView;
/*     */ import sexftp.views.SexftpServerView;
/*     */ import sexftp.views.SexftpSyncView;
/*     */ 
/*     */ public class PluginUtil
/*     */ {
/*     */   public static IWorkbenchPage getActivePage()
/*     */   {
/*  37 */     IWorkbench workbench = PlatformUI.getWorkbench();
/*  38 */     if (workbench == null) return null;
/*  39 */     IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
/*  40 */     if (activeWorkbenchWindow == null) return null;
/*  41 */     return activeWorkbenchWindow.getActivePage();
/*     */   }
/*     */   
/*     */   public static IProject[] getAllOpenedProjects() {
/*  45 */     List<IProject> proejctList = new ArrayList();
/*  46 */     IProject[] iprojects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
/*  47 */     IProject[] arrayOfIProject1; int j = (arrayOfIProject1 = iprojects).length; for (int i = 0; i < j; i++) { IProject iProject = arrayOfIProject1[i];
/*  48 */       if ((iProject.isOpen()) && (!iProject.getName().startsWith(".sexftp")))
/*     */       {
/*  50 */         proejctList.add(iProject);
/*     */       }
/*     */     }
/*  53 */     return (IProject[])proejctList.toArray(new IProject[0]);
/*     */   }
/*     */   
/*     */   public static IProject[] getAllProjects()
/*     */   {
/*  58 */     List<IProject> proejctList = new ArrayList();
/*  59 */     IProject[] iprojects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
/*  60 */     IProject[] arrayOfIProject1; int j = (arrayOfIProject1 = iprojects).length; for (int i = 0; i < j; i++) { IProject iProject = arrayOfIProject1[i];
/*  61 */       if (!iProject.getName().startsWith(".sexftp"))
/*     */       {
/*  63 */         proejctList.add(iProject);
/*     */       }
/*     */     }
/*  66 */     return (IProject[])proejctList.toArray(new IProject[0]);
/*     */   }
/*     */   
/*     */   public static String getProjectRealPath(IProject project)
/*     */   {
/*  71 */     return project.getFile("/a.txt").getLocation().toFile().getParent();
/*     */   }
/*     */   
/*     */   public static IProject getOneOpenedProject()
/*     */   {
/*     */     try {
/*  77 */       IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
/*  78 */       IProject project = root.getProject(".sexftpwkproject");
/*  79 */       if (!project.exists()) {
/*  80 */         project.create(null);
/*     */       }
/*  82 */       if (!project.isOpen())
/*  83 */         project.open(null);
/*  84 */       return project;
/*     */     }
/*     */     catch (Exception e) {
/*  87 */       throw new SRuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IFile createSexftpIFileFromPath(String filePath)
/*     */   {
/*  98 */     IFile file = null;
/*     */     try {
/* 100 */       IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
/* 101 */       IProject project = root.getProject(".sexftpwkproject");
/* 102 */       if (!project.exists()) {
/* 103 */         project.create(null);
/*     */       }
/* 105 */       if (!project.isOpen())
/* 106 */         project.open(null);
/* 107 */       file = project.getFile(filePath);
/* 108 */       File f = file.getLocation().toFile();
/* 109 */       if (!f.getParentFile().exists())
/*     */       {
/* 111 */         f.getParentFile();
/*     */       }
/* 113 */       file.refreshLocal(1, null);
/*     */     }
/*     */     catch (Exception e) {
/* 116 */       throw new SRuntimeException(e);
/*     */     }
/* 118 */     return file;
/*     */   }
/*     */   
/*     */   public static IFile rename(IFile ifile, String newfilename)
/*     */   {
/* 123 */     File file = ifile.getLocation().toFile();
/* 124 */     File newfile = new File(file.getParent() + "/" + newfilename);
/*     */     
/* 126 */     FileUtil.copyFile(file.getAbsolutePath(), newfile.getAbsolutePath());
/* 127 */     String oldfilepath = ifile.getFullPath();
/* 128 */     oldfilepath = oldfilepath.substring(0, oldfilepath.lastIndexOf("/"));
/* 129 */     oldfilepath = oldfilepath.substring(oldfilepath.indexOf("/", 1));
/* 130 */     IFile newifile = ifile.getProject().getFile(oldfilepath + "/" + newfilename);
/* 131 */     newifile.getLocation().toFile();
/*     */     try {
/* 133 */       ifile.getProject().getFile(oldfilepath + "/").refreshLocal(1, null);
/* 134 */       newifile.refreshLocal(1, null);
/*     */     } catch (CoreException e) {
/* 136 */       throw new SRuntimeException(e);
/*     */     }
/* 138 */     return newifile;
/*     */   }
/*     */   
/*     */   public static void openCompareEditor(IWorkbenchPage actPage, final String leftContents, final String rightContents, final String descContent)
/*     */   {
/* 143 */     CompareEditorInput input = new CompareEditorInput(new CompareConfiguration())
/*     */     {
/*     */ 
/*     */       protected Object prepareInput(IProgressMonitor arg0)
/*     */         throws java.lang.reflect.InvocationTargetException, InterruptedException
/*     */       {
/* 149 */         CompareItem left = 
/* 150 */           new CompareItem("Left Title", leftContents);
/* 151 */         CompareItem right = 
/* 152 */           new CompareItem("Right Title", rightContents);
/* 153 */         DiffNode diffNode = new DiffNode(left, right);
/* 154 */         diffNode.getName();
/* 155 */         return diffNode;
/*     */       }
/* 157 */     };
/* 158 */     Display.getDefault().asyncExec(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 162 */           SexftpCompareEditor c = (SexftpCompareEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(PluginUtil.this, "sexftp.editors.inner.SexftpCompareEditor");
/* 163 */           c.setContentDescription(descContent);
/*     */         } catch (PartInitException e) {
/* 165 */           throw new SRuntimeException(e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static SexftpLocalView findLocalView(IWorkbenchPage actPage)
/*     */     throws PartInitException
/*     */   {
/* 174 */     (SexftpLocalView)runAsDisplayThread(new RunAsDisplayThread()
/*     */     {
/*     */       public Object run() throws Exception {
/* 177 */         SexftpLocalView find = (SexftpLocalView)PluginUtil.this.findView("sexftp.views.MainView");
/* 178 */         if (find == null)
/*     */         {
/* 180 */           PluginUtil.this.showView("sexftp.views.MainView");
/*     */           
/* 182 */           find = (SexftpLocalView)PluginUtil.this.findView("sexftp.views.MainView");
/*     */         }
/*     */         
/* 185 */         return find;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static SexftpLocalView findAndShowLocalView(IWorkbenchPage actPage) throws PartInitException
/*     */   {
/* 192 */     (SexftpLocalView)runAsDisplayThread(new RunAsDisplayThread()
/*     */     {
/*     */       public Object run() throws Exception {
/* 195 */         PluginUtil.this.showView("sexftp.views.MainView");
/*     */         
/* 197 */         return (SexftpLocalView)PluginUtil.this.findView("sexftp.views.MainView");
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static SexftpSyncView findAndShowSyncView(IWorkbenchPage actPage) throws PartInitException
/*     */   {
/* 204 */     actPage.showView("sexftp.views.SexftpSyncView");
/*     */     
/* 206 */     return (SexftpSyncView)actPage.findView("sexftp.views.SexftpSyncView");
/*     */   }
/*     */   
/*     */   public static boolean isSyncViewShowed(IWorkbenchPage actPage) throws PartInitException
/*     */   {
/* 211 */     ((Boolean)runAsDisplayThread(new RunAsDisplayThread()
/*     */     {
/*     */       public Object run() throws Exception {
/* 214 */         if (PluginUtil.this.findView("sexftp.views.SexftpSyncView") != null) return Boolean.valueOf(true); return Boolean.valueOf(false);
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object runAsDisplayThread(RunAsDisplayThread yourun)
/*     */   {
/* 227 */     Runnable runnable = new Runnable() {
/* 228 */       private Object q = null;
/* 229 */       private Throwable e = null;
/*     */       
/*     */       public void run() {
/*     */         try {
/* 233 */           this.q = PluginUtil.this.run();
/*     */         } catch (Throwable e) {
/* 235 */           this.e = e;
/*     */         }
/*     */       }
/*     */       
/*     */       public Object getObj()
/*     */       {
/* 241 */         return this.q;
/*     */       }
/*     */       
/* 244 */       public Throwable getE() { return this.e;
/*     */       }
/*     */ 
/* 247 */     };
/* 248 */     Display.getDefault().syncExec(runnable);
/*     */     try {
/* 250 */       Throwable e = (Throwable)runnable.getClass().getMethod("getE", new Class[0]).invoke(runnable, new Object[0]);
/* 251 */       if (e != null)
/*     */       {
/* 253 */         if ((e instanceof RuntimeException))
/*     */         {
/* 255 */           throw ((RuntimeException)e);
/*     */         }
/* 257 */         throw new SRuntimeException(e);
/*     */       }
/* 259 */       return runnable.getClass().getMethod("getObj", new Class[0]).invoke(runnable, new Object[0]);
/*     */     }
/*     */     catch (RuntimeException e)
/*     */     {
/* 263 */       throw e;
/*     */     }
/*     */     catch (Exception e) {
/* 266 */       throw new SRuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static SexftpServerView findServerView(IWorkbenchPage actPage) throws PartInitException
/*     */   {
/* 272 */     (SexftpServerView)runAsDisplayThread(new RunAsDisplayThread()
/*     */     {
/*     */       public Object run() throws Exception {
/* 275 */         SexftpServerView find = (SexftpServerView)PluginUtil.this.findView("sexftp.views.ServerView");
/* 276 */         if (find == null)
/*     */         {
/* 278 */           PluginUtil.this.showView("sexftp.views.ServerView");
/* 279 */           find = (SexftpServerView)PluginUtil.this.findView("sexftp.views.ServerView");
/*     */         }
/* 281 */         return find;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public static SexftpServerView findAndShowServerView(IWorkbenchPage actPage)
/*     */     throws PartInitException
/*     */   {
/* 290 */     (SexftpServerView)runAsDisplayThread(new RunAsDisplayThread()
/*     */     {
/*     */       public Object run() throws Exception {
/* 293 */         PluginUtil.this.showView("sexftp.views.ServerView");
/*     */         
/* 295 */         return (SexftpServerView)PluginUtil.this.findView("sexftp.views.ServerView");
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static String getLanguage()
/*     */   {
/* 302 */     return getPreferenceStore("choicePreference");
/*     */   }
/*     */   
/*     */   public static Boolean overwriteTips() {
/* 306 */     String over = getPreferenceStore("booleanPreference");
/* 307 */     return Boolean.valueOf(over != null ? Boolean.valueOf(over).booleanValue() : true);
/*     */   }
/*     */   
/*     */   public static int getServerTimeout() {
/* 311 */     String over = getPreferenceStore("inttimeout");
/* 312 */     return over != null ? Integer.parseInt(over) : 10000;
/*     */   }
/*     */   
/*     */   public static String getPreferenceStore(String key) {
/* 316 */     if (Activator.getDefault() == null) return null;
/* 317 */     IPreferenceStore store = Activator.getDefault().getPreferenceStore();
/* 318 */     if (store == null) return null;
/* 319 */     return store.getString(key);
/*     */   }
/*     */   
/*     */   public static abstract interface RunAsDisplayThread
/*     */   {
/*     */     public abstract Object run()
/*     */       throws Exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\PluginUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */