/*     */ package sexftp.editors;
/*     */ 
/*     */ import org.eclipse.core.runtime.Assert;
/*     */ import org.eclipse.jface.text.BadLocationException;
/*     */ import org.eclipse.jface.text.DocumentEvent;
/*     */ import org.eclipse.jface.text.IDocument;
/*     */ import org.eclipse.jface.text.IRegion;
/*     */ import org.eclipse.jface.text.ITypedRegion;
/*     */ import org.eclipse.jface.text.Region;
/*     */ import org.eclipse.jface.text.TextAttribute;
/*     */ import org.eclipse.jface.text.TextPresentation;
/*     */ import org.eclipse.jface.text.presentation.IPresentationDamager;
/*     */ import org.eclipse.jface.text.presentation.IPresentationRepairer;
/*     */ import org.eclipse.swt.custom.StyleRange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NonRuleBasedDamagerRepairer
/*     */   implements IPresentationDamager, IPresentationRepairer
/*     */ {
/*     */   protected IDocument fDocument;
/*     */   protected TextAttribute fDefaultTextAttribute;
/*     */   
/*     */   public NonRuleBasedDamagerRepairer(TextAttribute defaultTextAttribute)
/*     */   {
/*  28 */     Assert.isNotNull(defaultTextAttribute);
/*     */     
/*  30 */     this.fDefaultTextAttribute = defaultTextAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDocument(IDocument document)
/*     */   {
/*  37 */     this.fDocument = document;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int endOfLineOf(int offset)
/*     */     throws BadLocationException
/*     */   {
/*  50 */     IRegion info = this.fDocument.getLineInformationOfOffset(offset);
/*  51 */     if (offset <= info.getOffset() + info.getLength()) {
/*  52 */       return info.getOffset() + info.getLength();
/*     */     }
/*  54 */     int line = this.fDocument.getLineOfOffset(offset);
/*     */     try {
/*  56 */       info = this.fDocument.getLineInformation(line + 1);
/*  57 */       return info.getOffset() + info.getLength();
/*     */     } catch (BadLocationException localBadLocationException) {}
/*  59 */     return this.fDocument.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event, boolean documentPartitioningChanged)
/*     */   {
/*  70 */     if (!documentPartitioningChanged) {
/*     */       try
/*     */       {
/*  73 */         IRegion info = 
/*  74 */           this.fDocument.getLineInformationOfOffset(event.getOffset());
/*  75 */         int start = Math.max(partition.getOffset(), info.getOffset());
/*     */         
/*  77 */         int end = 
/*  78 */           event.getOffset() + (
/*  79 */           event.getText() == null ? 
/*  80 */           event.getLength() : 
/*  81 */           event.getText().length());
/*     */         
/*  83 */         if ((info.getOffset() <= end) && 
/*  84 */           (end <= info.getOffset() + info.getLength()))
/*     */         {
/*  86 */           end = info.getOffset() + info.getLength();
/*     */         } else {
/*  88 */           end = endOfLineOf(end);
/*     */         }
/*  90 */         end = 
/*  91 */           Math.min(
/*  92 */           partition.getOffset() + partition.getLength(), 
/*  93 */           end);
/*  94 */         return new Region(start, end - start);
/*     */       }
/*     */       catch (BadLocationException localBadLocationException) {}
/*     */     }
/*     */     
/*     */ 
/* 100 */     return partition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createPresentation(TextPresentation presentation, ITypedRegion region)
/*     */   {
/* 109 */     addRange(
/* 110 */       presentation, 
/* 111 */       region.getOffset(), 
/* 112 */       region.getLength(), 
/* 113 */       this.fDefaultTextAttribute);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addRange(TextPresentation presentation, int offset, int length, TextAttribute attr)
/*     */   {
/* 129 */     if (attr != null) {
/* 130 */       presentation.addStyleRange(
/* 131 */         new StyleRange(
/* 132 */         offset, 
/* 133 */         length, 
/* 134 */         attr.getForeground(), 
/* 135 */         attr.getBackground(), 
/* 136 */         attr.getStyle()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\NonRuleBasedDamagerRepairer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */