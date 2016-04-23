/*    */ package sexftp.preferences;
/*    */ 
/*    */ import org.eclipse.jface.preference.BooleanFieldEditor;
/*    */ import org.eclipse.jface.preference.FieldEditorPreferencePage;
/*    */ import org.eclipse.jface.preference.IntegerFieldEditor;
/*    */ import org.eclipse.jface.preference.RadioGroupFieldEditor;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchPreferencePage;
/*    */ import sexftp.Activator;
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
/*    */ public class SexftpPreferencePage
/*    */   extends FieldEditorPreferencePage
/*    */   implements IWorkbenchPreferencePage
/*    */ {
/*    */   public SexftpPreferencePage()
/*    */   {
/* 27 */     super(1);
/* 28 */     setPreferenceStore(Activator.getDefault().getPreferenceStore());
/* 29 */     setDescription("Sexftp Preference,Some Change Should Restart Eclipse Go into Effect.");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void createFieldEditors()
/*    */   {
/* 41 */     addField(
/* 42 */       new BooleanFieldEditor(
/* 43 */       "booleanPreference", 
/* 44 */       "&Prompt Before Overwrites File", 
/* 45 */       getFieldEditorParent()));
/*    */     
/* 47 */     addField(new RadioGroupFieldEditor(
/* 48 */       "choicePreference", 
/* 49 */       "Sexftp Language", 
/* 50 */       1, 
/* 51 */       new String[][] { { "&English", "enus" }, {
/* 52 */       "&C简体中文", "zhcn" } }, 
/* 53 */       getFieldEditorParent()));
/* 54 */     addField(
/* 55 */       new IntegerFieldEditor("inttimeout", "Server Timeout Milliseconds:", getFieldEditorParent()));
/*    */   }
/*    */   
/*    */   public void init(IWorkbench workbench) {}
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\preferences\SexftpPreferencePage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */