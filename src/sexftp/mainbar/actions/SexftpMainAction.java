/*     */ package sexftp.mainbar.actions;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.jface.action.IAction;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.TreeSelection;
/*     */ import org.eclipse.ui.IObjectActionDelegate;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchPart;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.IWorkbenchWindowActionDelegate;
/*     */ import org.eclipse.ui.PartInitException;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.sexftp.core.exceptions.AbortException;
/*     */ import org.sexftp.core.license.LicenseUtils;
/*     */ import sexftp.uils.LogUtil;
/*     */ import sexftp.views.SexftpMainView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SexftpMainAction
/*     */   implements IWorkbenchWindowActionDelegate, IObjectActionDelegate
/*     */ {
/*     */   private IWorkbenchWindow window;
/*  36 */   protected IPath path = null;
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
/*     */   public void run(IAction action)
/*     */   {
/*     */     try
/*     */     {
/*  59 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("sexftp.views.MainView");
/*     */     }
/*     */     catch (PartInitException localPartInitException) {}
/*  62 */     if (this.path != null)
/*     */     {
/*  64 */       IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(this.path);
/*     */       
/*  66 */       SexftpMainView mainView = (SexftpMainView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("sexftp.views.MainView");
/*     */       try
/*     */       {
/*  69 */         mainView.directTo(file.getLocation().toFile().getAbsolutePath(), null);
/*     */       } catch (AbortException e) {
/*  71 */         LogUtil.info("abort:" + e.getMessage());
/*     */       }
/*     */     }
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
/*     */   public void selectionChanged(IAction action, ISelection selection)
/*     */   {
/*  86 */     if ((selection instanceof TreeSelection))
/*     */     {
/*  88 */       this.path = null;
/*  89 */       TreeSelection treeSelect = (TreeSelection)selection;
/*     */       try {
/*  91 */         this.path = ((IPath)treeSelect.getFirstElement().getClass().getMethod("getPath", new Class[0]).invoke(treeSelect.getFirstElement(), new Object[0]));
/*     */       }
/*     */       catch (Exception localException1) {}
/*     */       try
/*     */       {
/*  96 */         this.path = ((IPath)treeSelect.getFirstElement().getClass().getMethod("getFullPath", new Class[0]).invoke(treeSelect.getFirstElement(), new Object[0]));
/*     */       }
/*     */       catch (Exception localException2) {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */     LicenseUtils.checkUpdateAndLicense(String.format("Submain[%s][%s]", new Object[] { action != null ? action.getText() : "", this.path }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(IWorkbenchWindow window)
/*     */   {
/* 135 */     this.window = window;
/*     */   }
/*     */   
/*     */   public void setActivePart(IAction arg0, IWorkbenchPart arg1) {}
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\mainbar\actions\SexftpMainAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */