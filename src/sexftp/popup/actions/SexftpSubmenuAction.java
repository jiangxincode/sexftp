/*    */ package sexftp.popup.actions;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.eclipse.jface.action.IAction;
/*    */ import org.eclipse.jface.viewers.ISelection;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ import org.eclipse.ui.IWorkbenchPart;
/*    */ import org.eclipse.ui.IWorkbenchPartSite;
/*    */ import sexftp.mainbar.actions.SexftpMainAction;
/*    */ import sexftp.uils.LangUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SexftpSubmenuAction
/*    */   extends SexftpMainAction
/*    */ {
/*    */   private Shell shell;
/*    */   
/*    */   public void setActivePart(IAction action, IWorkbenchPart targetPart)
/*    */   {
/* 31 */     this.shell = targetPart.getSite().getShell();
/*    */   }
/*    */   
/* 34 */   static Map<String, String> idMapLable = new HashMap();
/*    */   
/*    */   public void selectionChanged(IAction action, ISelection selection) {
/* 37 */     String id = action.getId();
/*    */     
/* 39 */     String lable = (String)idMapLable.get(id);
/* 40 */     if (lable == null)
/*    */     {
/* 42 */       synchronized (idMapLable) {
/* 43 */         lable = action.getText();
/* 44 */         if ((lable != null) && (lable.length() > 0))
/* 45 */           idMapLable.put(id, lable);
/*    */       }
/*    */     }
/* 48 */     if ((lable != null) && (lable.length() > 0))
/*    */     {
/* 50 */       String viewText = LangUtil.langText(lable);
/* 51 */       action.setText(viewText);
/*    */     }
/* 53 */     super.selectionChanged(action, selection);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\popup\actions\SexftpSubmenuAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */