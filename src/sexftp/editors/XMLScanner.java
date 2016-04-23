package sexftp.editors;

import org.eclipse.jface.text.rules.IRule;

public class XMLScanner extends org.eclipse.jface.text.rules.RuleBasedScanner {
	public XMLScanner(ColorManager manager) {
		org.eclipse.jface.text.rules.IToken procInstr = new org.eclipse.jface.text.rules.Token(
				new org.eclipse.jface.text.TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));

		IRule[] rules = new IRule[2];

		rules[0] = new org.eclipse.jface.text.rules.SingleLineRule("<?", "?>", procInstr);

		rules[1] = new org.eclipse.jface.text.rules.WhitespaceRule(new XMLWhitespaceDetector());

		setRules(rules);
	}
}
