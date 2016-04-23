/*    */ package sexftp.describer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import org.eclipse.core.runtime.QualifiedName;
/*    */ import org.eclipse.core.runtime.content.IContentDescription;
/*    */ import org.eclipse.core.runtime.content.ITextContentDescriber;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SexftpInnerContentDescriber
/*    */   implements ITextContentDescriber
/*    */ {
/*    */   public int describe(InputStream ins, IContentDescription arg)
/*    */     throws IOException
/*    */   {
/* 23 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public int describe(Reader arg0, IContentDescription arg1)
/*    */     throws IOException
/*    */   {
/* 30 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public QualifiedName[] getSupportedOptions()
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\describer\SexftpInnerContentDescriber.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */