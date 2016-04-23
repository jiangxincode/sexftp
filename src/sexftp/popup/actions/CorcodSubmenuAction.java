/*     */ package sexftp.popup.actions;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.Path;
/*     */ import org.eclipse.jface.action.IAction;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.StructuredSelection;
/*     */ import org.eclipse.jface.viewers.TreeSelection;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchPart;
/*     */ import org.eclipse.ui.IWorkbenchPartSite;
/*     */ import org.eclipse.ui.part.FileEditorInput;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*     */ import org.sexftp.core.license.LicenseUtils;
/*     */ import sexftp.mainbar.actions.SexftpMainAction;
/*     */ import sexftp.uils.LogUtil;
/*     */ import sexftp.uils.PluginUtil;
/*     */ import sexftp.views.AbstractSexftpEncodView.CorCod;
/*     */ import sexftp.views.AbstractSexftpEncodView.ParentCorCod;
/*     */ import sexftp.views.AbstractSexftpView.TreeObject;
/*     */ import sexftp.views.SexftpEncodView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CorcodSubmenuAction
/*     */   extends SexftpMainAction
/*     */ {
/*     */   private Shell shell;
/*     */   
/*  42 */   public void setActivePart(IAction action, IWorkbenchPart targetPart) { this.shell = targetPart.getSite().getShell(); }
/*     */   
/*  44 */   private String truePath = null;
/*     */   
/*     */ 
/*     */   public void selectionChanged(IAction action, ISelection selection)
/*     */   {
/*  49 */     this.truePath = null;
/*  50 */     if ((selection instanceof StructuredSelection))
/*     */     {
/*  52 */       StructuredSelection s = (StructuredSelection)selection;
/*     */       
/*  54 */       Object e = s.getFirstElement();
/*  55 */       if ((e instanceof FileEditorInput)) {
/*  56 */         FileEditorInput fi = (FileEditorInput)e;
/*  57 */         this.truePath = fi.getFile().getLocation().toFile().getAbsolutePath();
/*  58 */         action.setEnabled(true);
/*  59 */         return;
/*     */       }
/*     */     }
/*  62 */     super.selectionChanged(action, selection);
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
/*  75 */     if (this.path != null)
/*     */     {
/*  77 */       action.setEnabled(true);
/*  78 */       IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(this.path.toString() + "/a.txt"));
/*  79 */       this.truePath = file.getLocation().toFile().getParentFile().getAbsolutePath();
/*     */     }
/*     */     else
/*     */     {
/*  83 */       if ((selection instanceof TreeSelection))
/*     */       {
/*  85 */         this.path = null;
/*  86 */         TreeSelection treeSelect = (TreeSelection)selection;
/*  87 */         Object elem = treeSelect.getFirstElement();
/*  88 */         if ((elem instanceof AbstractSexftpView.TreeObject))
/*     */         {
/*  90 */           Object o = ((AbstractSexftpView.TreeObject)elem).getO();
/*  91 */           if ((o instanceof FtpUploadConf))
/*     */           {
/*  93 */             this.truePath = ((FtpUploadConf)o).getClientPath();
/*     */           }
/*  95 */           else if ((o instanceof FtpUploadPro))
/*     */           {
/*  97 */             this.truePath = ((FtpUploadPro)o).getFtpUploadConf().getClientPath();
/*     */           }
/*     */         }
/* 100 */         else if ((elem instanceof AbstractSexftpEncodView.ParentCorCod))
/*     */         {
/*     */ 
/* 103 */           AbstractSexftpEncodView.CorCod cc = (AbstractSexftpEncodView.CorCod)elem;
/* 104 */           this.truePath = cc.getParentFolder();
/*     */         }
/* 106 */         else if ((elem instanceof AbstractSexftpEncodView.CorCod))
/*     */         {
/*     */ 
/* 109 */           AbstractSexftpEncodView.CorCod cc = (AbstractSexftpEncodView.CorCod)elem;
/* 110 */           this.truePath = cc.getEndexten();
/*     */         }
/*     */       }
/* 113 */       if (this.truePath != null)
/*     */       {
/* 115 */         action.setEnabled(true);
/*     */       }
/*     */       else
/*     */       {
/* 119 */         action.setEnabled(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void run(IAction action)
/*     */   {
/*     */     try {
/* 127 */       LicenseUtils.checkUpdateAndLicense("Submenu Corcod");
/* 128 */       PluginUtil.getActivePage().showView("sexftp.views.SexftpEncodView");
/* 129 */       SexftpEncodView c = (SexftpEncodView)PluginUtil.getActivePage().findView("sexftp.views.SexftpEncodView");
/*     */       
/* 131 */       c.checkAndView(this.truePath);
/*     */     } catch (Throwable e) {
/* 133 */       LogUtil.error(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\popup\actions\CorcodSubmenuAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */