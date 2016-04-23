/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.rules.IRule;
/*    */ 
/*    */ public class XMLTagScanner extends org.eclipse.jface.text.rules.RuleBasedScanner
/*    */ {
/*    */   public XMLTagScanner(ColorManager manager)
/*    */   {
/*  9 */     org.eclipse.jface.text.rules.IToken string = 
/* 10 */       new org.eclipse.jface.text.rules.Token(
/* 11 */       new org.eclipse.jface.text.TextAttribute(manager.getColor(IXMLColorConstants.STRING)));
/*    */     
/* 13 */     IRule[] rules = new IRule[3];
/*    */     
/*    */ 
/* 16 */     rules[0] = new org.eclipse.jface.text.rules.SingleLineRule("\"", "\"", string, '\\');
/*    */     
/* 18 */     rules[1] = new org.eclipse.jface.text.rules.SingleLineRule("'", "'", string, '\\');
/*    */     
/* 20 */     rules[2] = new org.eclipse.jface.text.rules.WhitespaceRule(new XMLWhitespaceDetector());
/*    */     
/* 22 */     setRules(rules);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLTagScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */