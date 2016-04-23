/*     */ package sexftp.editors;
/*     */ 
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IAction;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.jface.action.MenuManager;
/*     */ import org.eclipse.jface.action.Separator;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.ISharedImages;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.actions.ActionFactory;
/*     */ import org.eclipse.ui.ide.IDEActionFactory;
/*     */ import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
/*     */ import org.eclipse.ui.texteditor.ITextEditor;
/*     */ import org.eclipse.ui.texteditor.ITextEditorActionConstants;
/*     */ 
/*     */ public class SexftpMultiPageEditorContributor
/*     */   extends MultiPageEditorActionBarContributor
/*     */ {
/*     */   private IEditorPart activeEditorPart;
/*     */   private Action sampleAction;
/*     */   
/*     */   public SexftpMultiPageEditorContributor()
/*     */   {
/*  29 */     createActions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected IAction getAction(ITextEditor editor, String actionID)
/*     */   {
/*  36 */     return editor == null ? null : editor.getAction(actionID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setActivePage(IEditorPart part)
/*     */   {
/*  43 */     if (this.activeEditorPart == part) {
/*  44 */       return;
/*     */     }
/*  46 */     this.activeEditorPart = part;
/*     */     
/*  48 */     IActionBars actionBars = getActionBars();
/*  49 */     if (actionBars != null)
/*     */     {
/*  51 */       ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor)part : null;
/*     */       
/*  53 */       actionBars.setGlobalActionHandler(
/*  54 */         ActionFactory.DELETE.getId(), 
/*  55 */         getAction(editor, ITextEditorActionConstants.DELETE));
/*  56 */       actionBars.setGlobalActionHandler(
/*  57 */         ActionFactory.UNDO.getId(), 
/*  58 */         getAction(editor, ITextEditorActionConstants.UNDO));
/*  59 */       actionBars.setGlobalActionHandler(
/*  60 */         ActionFactory.REDO.getId(), 
/*  61 */         getAction(editor, ITextEditorActionConstants.REDO));
/*  62 */       actionBars.setGlobalActionHandler(
/*  63 */         ActionFactory.CUT.getId(), 
/*  64 */         getAction(editor, ITextEditorActionConstants.CUT));
/*  65 */       actionBars.setGlobalActionHandler(
/*  66 */         ActionFactory.COPY.getId(), 
/*  67 */         getAction(editor, ITextEditorActionConstants.COPY));
/*  68 */       actionBars.setGlobalActionHandler(
/*  69 */         ActionFactory.PASTE.getId(), 
/*  70 */         getAction(editor, ITextEditorActionConstants.PASTE));
/*  71 */       actionBars.setGlobalActionHandler(
/*  72 */         ActionFactory.SELECT_ALL.getId(), 
/*  73 */         getAction(editor, ITextEditorActionConstants.SELECT_ALL));
/*  74 */       actionBars.setGlobalActionHandler(
/*  75 */         ActionFactory.FIND.getId(), 
/*  76 */         getAction(editor, ITextEditorActionConstants.FIND));
/*  77 */       actionBars.setGlobalActionHandler(
/*  78 */         IDEActionFactory.BOOKMARK.getId(), 
/*  79 */         getAction(editor, IDEActionFactory.BOOKMARK.getId()));
/*  80 */       actionBars.updateActionBars();
/*     */     }
/*     */   }
/*     */   
/*  84 */   private void createActions() { this.sampleAction = new Action() {
/*     */       public void run() {
/*  86 */         MessageDialog.openInformation(null, "Sexftp", "Sample Action Executed");
/*     */       }
/*  88 */     };
/*  89 */     this.sampleAction.setText("Sample Action");
/*  90 */     this.sampleAction.setToolTipText("Sample Action tool tip");
/*  91 */     this.sampleAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  92 */       .getImageDescriptor("IMG_OBJS_TASK_TSK"));
/*     */   }
/*     */   
/*  95 */   public void contributeToMenu(IMenuManager manager) { IMenuManager menu = new MenuManager("Editor &Menu");
/*  96 */     manager.prependToGroup("additions", menu);
/*  97 */     menu.add(this.sampleAction);
/*     */   }
/*     */   
/* 100 */   public void contributeToToolBar(IToolBarManager manager) { manager.add(new Separator());
/* 101 */     manager.add(this.sampleAction);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\SexftpMultiPageEditorContributor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */