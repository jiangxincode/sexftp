/*    */ package sexftp.editors;
/*    */ 
/*    */ import org.eclipse.jface.text.rules.IToken;
/*    */ 
/*    */ public class XMLPartitionScanner extends org.eclipse.jface.text.rules.RuleBasedPartitionScanner
/*    */ {
/*    */   public static final String XML_COMMENT = "__xml_comment";
/*    */   public static final String XML_TAG = "__xml_tag";
/*    */   
/*    */   public XMLPartitionScanner() {
/* 11 */     IToken xmlComment = new org.eclipse.jface.text.rules.Token("__xml_comment");
/* 12 */     IToken tag = new org.eclipse.jface.text.rules.Token("__xml_tag");
/*    */     
/* 14 */     org.eclipse.jface.text.rules.IPredicateRule[] rules = new org.eclipse.jface.text.rules.IPredicateRule[2];
/*    */     
/* 16 */     rules[0] = new org.eclipse.jface.text.rules.MultiLineRule("<!--", "-->", xmlComment);
/* 17 */     rules[1] = new TagRule(tag);
/*    */     
/* 19 */     setPredicateRules(rules);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLPartitionScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */