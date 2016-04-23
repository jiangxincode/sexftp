/*    */ package sexftp.editors.inner;
/*    */ 
/*    */ import org.eclipse.ui.editors.text.TextEditor;
/*    */ import sexftp.editors.viewlis.IDoSaveListener;
/*    */ 
/*    */ public class SfTextEditor extends TextEditor
/*    */ {
/*    */   private IDoSaveListener doSaveListener;
/*    */   
/*    */   protected void initializeEditor()
/*    */   {
/* 12 */     super.initializeEditor();
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 16 */     if (this.doSaveListener != null)
/*    */     {
/* 18 */       this.doSaveListener.dispose();
/*    */     }
/* 20 */     super.dispose();
/*    */   }
/*    */   
/*    */   public void doSave(org.eclipse.core.runtime.IProgressMonitor progressMonitor) {
/* 24 */     super.doSave(progressMonitor);
/* 25 */     if (this.doSaveListener != null) this.doSaveListener.dosave();
/*    */   }
/*    */   
/*    */   public IDoSaveListener getDoSaveListener() {
/* 29 */     return this.doSaveListener;
/*    */   }
/*    */   
/*    */   public void setDoSaveListener(IDoSaveListener doSaveListener) {
/* 33 */     this.doSaveListener = doSaveListener;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\inner\SfTextEditor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */