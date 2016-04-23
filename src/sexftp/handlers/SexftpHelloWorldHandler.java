/*    */ package sexftp.handlers;
/*    */ 
/*    */ import org.eclipse.core.commands.AbstractHandler;
/*    */ import org.eclipse.core.commands.ExecutionEvent;
/*    */ import org.eclipse.core.commands.ExecutionException;
/*    */ import org.eclipse.jface.dialogs.MessageDialog;
/*    */ import org.eclipse.ui.IWorkbenchWindow;
/*    */ import org.eclipse.ui.handlers.HandlerUtil;
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
/*    */ public class SexftpHelloWorldHandler
/*    */   extends AbstractHandler
/*    */ {
/*    */   public Object execute(ExecutionEvent event)
/*    */     throws ExecutionException
/*    */   {
/* 27 */     IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
/* 28 */     MessageDialog.openInformation(
/* 29 */       window.getShell(), 
/* 30 */       "Sexftp", 
/* 31 */       "Hello, Sexftp world");
/* 32 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\handlers\SexftpHelloWorldHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */