/*   */ package sexftp.editors;
/*   */ 
/*   */ import org.eclipse.jface.text.rules.IWhitespaceDetector;
/*   */ 
/*   */ public class XMLWhitespaceDetector implements IWhitespaceDetector
/*   */ {
/*   */   public boolean isWhitespace(char c) {
/* 8 */     return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
/*   */   }
/*   */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\XMLWhitespaceDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */