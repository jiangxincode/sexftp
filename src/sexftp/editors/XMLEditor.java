/*     */ package sexftp.editors;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.editors.text.TextEditor;
/*     */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*     */ import sexftp.editors.viewlis.IDoSaveListener;
/*     */ import sexftp.uils.LogUtil;
/*     */ import sexftp.views.SexftpViewAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLEditor
/*     */   extends TextEditor
/*     */ {
/*  26 */   private static Set<XMLEditor> editSet = new HashSet(50);
/*     */   
/*     */   private IDoSaveListener doSaveListener;
/*     */   
/*     */   private ColorManager colorManager;
/*     */   
/*     */   protected Action actionEditSexFtpConf;
/*     */   
/*     */   protected void editorContextMenuAboutToShow(IMenuManager menu)
/*     */   {
/*  36 */     menu.add(this.actionEditSexFtpConf);
/*  37 */     menu.add(new Separator("additions"));
/*  38 */     super.editorContextMenuAboutToShow(menu);
/*     */   }
/*     */   
/*     */   public void createPartControl(Composite parent) {
/*  42 */     super.createPartControl(parent);
/*  43 */     parent.getChildren();
/*     */     
/*     */ 
/*  46 */     this.actionEditSexFtpConf = new SexftpViewAction() {
/*     */       public void run() {
/*  48 */         if (XMLEditor.this.doSaveListener != null) XMLEditor.this.doSaveListener.dosave();
/*     */       }
/*  50 */     };
/*  51 */     this.actionEditSexFtpConf.setText("Save Sexftp Upload Unit Config And Load In Sexftp Viewer(&S)");
/*  52 */     this.actionEditSexFtpConf.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/boxmodel_props.gif"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public XMLEditor()
/*     */   {
/*  59 */     this.colorManager = new ColorManager();
/*  60 */     setSourceViewerConfiguration(new XMLConfiguration(this.colorManager));
/*  61 */     setDocumentProvider(new XMLDocumentProvider());
/*     */   }
/*     */   
/*  64 */   public void dispose() { this.colorManager.dispose();
/*  65 */     if (this.doSaveListener != null)
/*     */     {
/*  67 */       this.doSaveListener.dispose();
/*     */     }
/*  69 */     super.dispose();
/*  70 */     editSet.remove(this);
/*     */   }
/*     */   
/*     */   protected void initializeEditor()
/*     */   {
/*  75 */     super.initializeEditor();
/*  76 */     new Thread()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  80 */           sleep(2000L);
/*  81 */           Display.getDefault().asyncExec(new Runnable()
/*     */           {
/*     */             public void run() {
/*  84 */               if (XMLEditor.this.doSaveListener == null)
/*     */               {
/*  86 */                 XMLEditor.this.setTitle("Dead Config File");
/*  87 */                 XMLEditor.this.setTitleToolTip("It's Dead Config File,Please Close Me And Chose [Edit Sexftp Config] Option Again!");
/*  88 */                 PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(XMLEditor.this, true);
/*     */               }
/*     */               
/*     */             }
/*     */           });
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*  96 */     }.start();
/*  97 */     editSet.add(this);
/*     */   }
/*     */   
/*     */   public void doSave(IProgressMonitor progressMonitor)
/*     */   {
/* 102 */     super.doSave(progressMonitor);
/* 103 */     if (this.doSaveListener != null) this.doSaveListener.dosave();
/* 104 */     LogUtil.info("Save XmlEditor,Now There is " + editorCounts() + " Editors");
/*     */   }
/*     */   
/* 107 */   public IDoSaveListener getDoSaveListener() { return this.doSaveListener; }
/*     */   
/*     */   public void setDoSaveListener(IDoSaveListener doSaveListener) {
/* 110 */     this.doSaveListener = doSaveListener;
/*     */   }
/*     */   
/*     */   public int editorCounts()
/*     */   {
/* 115 */     return editSet.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLEditor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */