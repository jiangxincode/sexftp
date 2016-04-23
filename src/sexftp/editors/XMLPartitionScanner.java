package sexftp.editors;

import org.eclipse.jface.text.rules.IToken;

public class XMLPartitionScanner extends org.eclipse.jface.text.rules.RuleBasedPartitionScanner {
	public static final String XML_COMMENT = "__xml_comment";
	public static final String XML_TAG = "__xml_tag";

	public XMLPartitionScanner() {
		IToken xmlComment = new org.eclipse.jface.text.rules.Token("__xml_comment");
		IToken tag = new org.eclipse.jface.text.rules.Token("__xml_tag");

		org.eclipse.jface.text.rules.IPredicateRule[] rules = new org.eclipse.jface.text.rules.IPredicateRule[2];

		rules[0] = new org.eclipse.jface.text.rules.MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}
}
