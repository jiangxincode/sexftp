/*     */ package sexftp.editors;
/*     */ 
/*     */ import org.eclipse.jface.text.IDocument;
/*     */ 
/*     */ public class XMLDoubleClickStrategy implements org.eclipse.jface.text.ITextDoubleClickStrategy {
/*     */   protected org.eclipse.jface.text.ITextViewer fText;
/*     */   
/*     */   public void doubleClicked(org.eclipse.jface.text.ITextViewer part) {
/*   9 */     int pos = part.getSelectedRange().x;
/*     */     
/*  11 */     if (pos < 0) {
/*  12 */       return;
/*     */     }
/*  14 */     this.fText = part;
/*     */     
/*  16 */     if (!selectComment(pos))
/*  17 */       selectWord(pos);
/*     */   }
/*     */   
/*     */   protected boolean selectComment(int caretPos) {
/*  21 */     IDocument doc = this.fText.getDocument();
/*     */     
/*     */     try
/*     */     {
/*  25 */       int pos = caretPos;
/*  26 */       char c = ' ';
/*     */       
/*  28 */       while (pos >= 0) {
/*  29 */         c = doc.getChar(pos);
/*  30 */         if (c == '\\') {
/*  31 */           pos -= 2;
/*     */         }
/*     */         else {
/*  34 */           if ((c == '\r') || (c == '"'))
/*     */             break;
/*  36 */           pos--;
/*     */         }
/*     */       }
/*  39 */       if (c != '"') {
/*  40 */         return false;
/*     */       }
/*  42 */       int startPos = pos;
/*     */       
/*  44 */       pos = caretPos;
/*  45 */       int length = doc.getLength();
/*  46 */       c = ' ';
/*     */       
/*  48 */       while (pos < length) {
/*  49 */         c = doc.getChar(pos);
/*  50 */         if ((c == '\r') || (c == '"'))
/*     */           break;
/*  52 */         pos++;
/*     */       }
/*  54 */       if (c != '"') {
/*  55 */         return false;
/*     */       }
/*  57 */       int endPos = pos;
/*     */       
/*  59 */       int offset = startPos + 1;
/*  60 */       int len = endPos - offset;
/*  61 */       this.fText.setSelectedRange(offset, len);
/*  62 */       return true;
/*     */     }
/*     */     catch (org.eclipse.jface.text.BadLocationException localBadLocationException) {}
/*     */     
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean selectWord(int caretPos) {
/*  70 */     IDocument doc = this.fText.getDocument();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  75 */       int pos = caretPos;
/*     */       
/*     */ 
/*  78 */       while (pos >= 0) {
/*  79 */         char c = doc.getChar(pos);
/*  80 */         if (!Character.isJavaIdentifierPart(c))
/*     */           break;
/*  82 */         pos--;
/*     */       }
/*     */       
/*  85 */       int startPos = pos;
/*     */       
/*  87 */       pos = caretPos;
/*  88 */       int length = doc.getLength();
/*     */       
/*  90 */       while (pos < length) {
/*  91 */         char c = doc.getChar(pos);
/*  92 */         if (!Character.isJavaIdentifierPart(c))
/*     */           break;
/*  94 */         pos++;
/*     */       }
/*     */       
/*  97 */       int endPos = pos;
/*  98 */       selectRange(startPos, endPos);
/*  99 */       return true;
/*     */     }
/*     */     catch (org.eclipse.jface.text.BadLocationException localBadLocationException) {}
/*     */     
/*     */ 
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   private void selectRange(int startPos, int stopPos) {
/* 108 */     int offset = startPos + 1;
/* 109 */     int length = stopPos - offset;
/* 110 */     this.fText.setSelectedRange(offset, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLDoubleClickStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */