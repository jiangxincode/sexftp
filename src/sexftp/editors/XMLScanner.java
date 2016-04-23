/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.rules.IRule;
/*    */ 
/*    */ public class XMLScanner extends org.eclipse.jface.text.rules.RuleBasedScanner
/*    */ {
/*    */   public XMLScanner(ColorManager manager)
/*    */   {
/*  9 */     org.eclipse.jface.text.rules.IToken procInstr = 
/* 10 */       new org.eclipse.jface.text.rules.Token(
/* 11 */       new org.eclipse.jface.text.TextAttribute(
/* 12 */       manager.getColor(IXMLColorConstants.PROC_INSTR)));
/*    */     
/* 14 */     IRule[] rules = new IRule[2];
/*    */     
/* 16 */     rules[0] = new org.eclipse.jface.text.rules.SingleLineRule("<?", "?>", procInstr);
/*    */     
/* 18 */     rules[1] = new org.eclipse.jface.text.rules.WhitespaceRule(new XMLWhitespaceDetector());
/*    */     
/* 20 */     setRules(rules);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */