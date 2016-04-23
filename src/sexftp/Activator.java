/*    */ package sexftp;
/*    */ 
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
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
/*    */ public class Activator
/*    */   extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "sexftp";
/*    */   private static Activator plugin;
/*    */   
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 29 */     super.start(context);
/* 30 */     plugin = this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 38 */     plugin = null;
/* 39 */     super.stop(context);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Activator getDefault()
/*    */   {
/* 48 */     return plugin;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ImageDescriptor getImageDescriptor(String path)
/*    */   {
/* 59 */     return imageDescriptorFromPlugin("sexftp", path);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\Activator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */