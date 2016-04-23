/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.rules.ICharacterScanner;
/*    */ 
/*    */ public class TagRule extends org.eclipse.jface.text.rules.MultiLineRule
/*    */ {
/*    */   public TagRule(org.eclipse.jface.text.rules.IToken token) {
/*  8 */     super("<", ">", token);
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed)
/*    */   {
/* 14 */     int c = scanner.read();
/* 15 */     if (sequence[0] == '<') {
/* 16 */       if (c == 63)
/*    */       {
/* 18 */         scanner.unread();
/* 19 */         return false;
/*    */       }
/* 21 */       if (c == 33) {
/* 22 */         scanner.unread();
/*    */         
/* 24 */         return false;
/*    */       }
/* 26 */     } else if (sequence[0] == '>') {
/* 27 */       scanner.unread();
/*    */     }
/* 29 */     return super.sequenceDetected(scanner, sequence, eofAllowed);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\TagRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */