/*    */ package sexftp;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import org.eclipse.core.runtime.IPath;
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ import org.eclipse.ui.IMemento;
/*    */ import org.eclipse.ui.IPathEditorInput;
/*    */ import org.eclipse.ui.IPersistableElement;
/*    */ import org.sexftp.core.utils.FileUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathEditorInput
/*    */   implements IPathEditorInput, IPersistableElement
/*    */ {
/*    */   private IPath path;
/*    */   
/*    */   public PathEditorInput(IPath path)
/*    */   {
/* 24 */     this.path = path;
/*    */   }
/*    */   
/* 27 */   public IPath getPath() { return this.path; }
/*    */   
/*    */   public boolean exists() {
/* 30 */     return this.path.toFile().exists();
/*    */   }
/*    */   
/*    */   public ImageDescriptor getImageDescriptor() {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.path.toString();
/*    */   }
/*    */   
/* 41 */   public IPersistableElement getPersistable() { return this; }
/*    */   
/*    */   public String getToolTipText() {
/* 44 */     return this.path.toString();
/*    */   }
/*    */   
/*    */   public Object getAdapter(Class adapter) {
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 52 */     if (((obj instanceof PathEditorInput)) && (this.path != null)) {
/* 53 */       return this.path.equals(((PathEditorInput)obj).getPath());
/*    */     }
/* 55 */     return super.equals(obj);
/*    */   }
/*    */   
/* 58 */   public String getFactoryId() { return null; }
/*    */   
/*    */   public void saveState(IMemento arg0) {}
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 65 */     String str = "你乱勃l";
/* 66 */     byte[] b = str.getBytes("utf-8");
/* 67 */     FileUtil.writeByte2File("d:/out.ff", b);
/* 68 */     String ns = new String(b, "gbk");
/* 69 */     System.out.println(ns);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\PathEditorInput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */