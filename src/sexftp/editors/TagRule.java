package sexftp.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;

public class TagRule extends org.eclipse.jface.text.rules.MultiLineRule {
	public TagRule(org.eclipse.jface.text.rules.IToken token) {
		super("<", ">", token);
	}

	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c == 63) {
				scanner.unread();
				return false;
			}
			if (c == 33) {
				scanner.unread();

				return false;
			}
		} else if (sequence[0] == '>') {
			scanner.unread();
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
}
