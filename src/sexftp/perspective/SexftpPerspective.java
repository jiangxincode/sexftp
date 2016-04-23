/*    */ package sexftp.perspective;
/*    */ 
/*    */ import org.eclipse.ui.IPageLayout;
/*    */ import org.eclipse.ui.IPerspectiveFactory;
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
/*    */ 
/*    */ 
/*    */ public class SexftpPerspective
/*    */   implements IPerspectiveFactory
/*    */ {
/*    */   public void createInitialLayout(IPageLayout pageLayout)
/*    */   {
/* 24 */     pageLayout.addView("sexftp.views.MainView", 1, 0.2F, "org.eclipse.ui.editorss");
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\perspective\SexftpPerspective.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */