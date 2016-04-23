/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.ITextDoubleClickStrategy;
/*    */ import org.eclipse.jface.text.TextAttribute;
/*    */ import org.eclipse.jface.text.presentation.PresentationReconciler;
/*    */ import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
/*    */ import org.eclipse.jface.text.rules.Token;
/*    */ import org.eclipse.jface.text.source.ISourceViewer;
/*    */ import org.eclipse.jface.text.source.SourceViewerConfiguration;
/*    */ 
/*    */ 
/*    */ public class XMLConfiguration
/*    */   extends SourceViewerConfiguration
/*    */ {
/*    */   private XMLDoubleClickStrategy doubleClickStrategy;
/*    */   private XMLTagScanner tagScanner;
/*    */   private XMLScanner scanner;
/*    */   private ColorManager colorManager;
/*    */   
/* 20 */   public XMLConfiguration(ColorManager colorManager) { this.colorManager = colorManager; }
/*    */   
/*    */   public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
/* 23 */     return new String[] {
/* 24 */       "__dftl_partition_content_type", 
/* 25 */       "__xml_comment", 
/* 26 */       "__xml_tag" };
/*    */   }
/*    */   
/*    */   public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType)
/*    */   {
/* 31 */     if (this.doubleClickStrategy == null)
/* 32 */       this.doubleClickStrategy = new XMLDoubleClickStrategy();
/* 33 */     return this.doubleClickStrategy;
/*    */   }
/*    */   
/*    */   protected XMLScanner getXMLScanner() {
/* 37 */     if (this.scanner == null) {
/* 38 */       this.scanner = new XMLScanner(this.colorManager);
/* 39 */       this.scanner.setDefaultReturnToken(
/* 40 */         new Token(
/* 41 */         new TextAttribute(
/* 42 */         this.colorManager.getColor(IXMLColorConstants.DEFAULT))));
/*    */     }
/* 44 */     return this.scanner;
/*    */   }
/*    */   
/* 47 */   protected XMLTagScanner getXMLTagScanner() { if (this.tagScanner == null) {
/* 48 */       this.tagScanner = new XMLTagScanner(this.colorManager);
/* 49 */       this.tagScanner.setDefaultReturnToken(
/* 50 */         new Token(
/* 51 */         new TextAttribute(
/* 52 */         this.colorManager.getColor(IXMLColorConstants.TAG))));
/*    */     }
/* 54 */     return this.tagScanner;
/*    */   }
/*    */   
/*    */   public org.eclipse.jface.text.presentation.IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
/* 58 */     PresentationReconciler reconciler = new PresentationReconciler();
/*    */     
/* 60 */     DefaultDamagerRepairer dr = 
/* 61 */       new DefaultDamagerRepairer(getXMLTagScanner());
/* 62 */     reconciler.setDamager(dr, "__xml_tag");
/* 63 */     reconciler.setRepairer(dr, "__xml_tag");
/*    */     
/* 65 */     dr = new DefaultDamagerRepairer(getXMLScanner());
/* 66 */     reconciler.setDamager(dr, "__dftl_partition_content_type");
/* 67 */     reconciler.setRepairer(dr, "__dftl_partition_content_type");
/*    */     
/* 69 */     NonRuleBasedDamagerRepairer ndr = 
/* 70 */       new NonRuleBasedDamagerRepairer(
/* 71 */       new TextAttribute(
/* 72 */       this.colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
/* 73 */     reconciler.setDamager(ndr, "__xml_comment");
/* 74 */     reconciler.setRepairer(ndr, "__xml_comment");
/*    */     
/* 76 */     return reconciler;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */