package sexftp.editors;

import org.eclipse.jface.text.rules.IRule;

public class XMLTagScanner extends org.eclipse.jface.text.rules.RuleBasedScanner {
	public XMLTagScanner(ColorManager manager) {
		org.eclipse.jface.text.rules.IToken string = new org.eclipse.jface.text.rules.Token(
				new org.eclipse.jface.text.TextAttribute(manager.getColor(IXMLColorConstants.STRING)));

		IRule[] rules = new IRule[3];

		rules[0] = new org.eclipse.jface.text.rules.SingleLineRule("\"", "\"", string, '\\');

		rules[1] = new org.eclipse.jface.text.rules.SingleLineRule("'", "'", string, '\\');

		rules[2] = new org.eclipse.jface.text.rules.WhitespaceRule(new XMLWhitespaceDetector());

		setRules(rules);
	}
}
