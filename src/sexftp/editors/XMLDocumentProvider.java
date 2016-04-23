/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.IDocument;
/*    */ import org.eclipse.jface.text.IDocumentPartitioner;
/*    */ import org.eclipse.jface.text.rules.FastPartitioner;
/*    */ import org.eclipse.ui.editors.text.FileDocumentProvider;
/*    */ 
/*    */ public class XMLDocumentProvider extends FileDocumentProvider
/*    */ {
/*    */   protected IDocument createDocument(Object element) throws org.eclipse.core.runtime.CoreException
/*    */   {
/* 12 */     IDocument document = super.createDocument(element);
/* 13 */     if (document != null) {
/* 14 */       IDocumentPartitioner partitioner = 
/* 15 */         new FastPartitioner(
/* 16 */         new XMLPartitionScanner(), 
/* 17 */         new String[] {
/* 18 */         "__xml_tag", 
/* 19 */         "__xml_comment" });
/* 20 */       partitioner.connect(document);
/* 21 */       document.setDocumentPartitioner(partitioner);
/*    */     }
/* 23 */     return document;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLDocumentProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */