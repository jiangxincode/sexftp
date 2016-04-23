/*    */ package sexftp.uils;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.util.Date;
/*    */ import org.eclipse.compare.IModificationDate;
/*    */ import org.eclipse.compare.IStreamContentAccessor;
/*    */ import org.eclipse.compare.ITypedElement;
/*    */ import org.eclipse.core.runtime.CoreException;
/*    */ import org.eclipse.swt.graphics.Image;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompareItem
/*    */   implements IStreamContentAccessor, ITypedElement, IModificationDate
/*    */ {
/*    */   private String contents;
/*    */   private String name;
/*    */   private long time;
/*    */   
/*    */   public CompareItem(String name, String contents, long time)
/*    */   {
/* 25 */     this.name = name;
/* 26 */     this.contents = contents;
/* 27 */     this.time = time;
/*    */   }
/*    */   
/* 30 */   public CompareItem(String name, String contents) { this.name = name;
/* 31 */     this.contents = contents;
/* 32 */     this.time = new Date().getTime();
/*    */   }
/*    */   
/* 35 */   public InputStream getContents() throws CoreException { return new ByteArrayInputStream(this.contents.getBytes()); }
/*    */   
/* 37 */   public Image getImage() { return null; }
/* 38 */   public long getModificationDate() { return this.time; }
/*    */   
/* 40 */   public String getName() { return this.name; }
/*    */   
/*    */ 
/* 43 */   public String getString() { return this.contents; }
/*    */   
/* 45 */   public String getType() { return "txt"; }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\CompareItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */