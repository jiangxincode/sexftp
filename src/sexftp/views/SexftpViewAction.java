/*    */ package sexftp.views;
/*    */ 
/*    */ import org.eclipse.jface.action.Action;
/*    */ import org.eclipse.swt.widgets.Event;
/*    */ import org.sexftp.core.license.LicenseUtils;
/*    */ import sexftp.uils.LangUtil;
/*    */ import sexftp.uils.LogUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SexftpViewAction
/*    */   extends Action
/*    */ {
/*    */   public void setText(String text)
/*    */   {
/* 20 */     super.setText(LangUtil.langText(text));
/* 21 */     super.setToolTipText(LangUtil.langText(text));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setToolTipText(String toolTipText) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void runWithEvent(Event event)
/*    */   {
/* 32 */     LicenseUtils.checkUpdateAndLicense(getText());
/*    */     try {
/* 34 */       super.runWithEvent(event);
/*    */     } catch (Throwable e) {
/* 36 */       LogUtil.error(e.getMessage(), e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpViewAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */