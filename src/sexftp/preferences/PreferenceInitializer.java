/*    */ package sexftp.preferences;
/*    */ 
/*    */ import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
/*    */ import org.eclipse.jface.preference.IPreferenceStore;
/*    */ import sexftp.Activator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreferenceInitializer
/*    */   extends AbstractPreferenceInitializer
/*    */ {
/*    */   public void initializeDefaultPreferences()
/*    */   {
/* 19 */     IPreferenceStore store = Activator.getDefault().getPreferenceStore();
/* 20 */     store.setDefault("booleanPreference", true);
/* 21 */     store.setDefault("choicePreference", "zhcn");
/* 22 */     store.setDefault("inttimeout", 
/* 23 */       10000);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\preferences\PreferenceInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */