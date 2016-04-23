package examples.mail;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;

public final class IMAPMail {
	public static final void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage: IMAPMail <imap server hostname> <username> <password> [TLS]");
			System.exit(1);
		}

		String server = args[0];
		String username = args[1];
		String password = args[2];

		String proto = args.length > 3 ? args[3] : null;

		IMAPClient imap;
		if (proto != null) {
			System.out.println("Using secure protocol: " + proto);
			imap = new IMAPSClient(proto, true);

		} else {

			imap = new IMAPClient();
		}
		System.out.println("Connecting to server " + server + " on " + imap.getDefaultPort());

		imap.setDefaultTimeout(60000);

		imap.addProtocolCommandListener(new PrintCommandListener(System.out, true));

		try {
			imap.connect(server);
		} catch (IOException e) {
			throw new RuntimeException("Could not connect to server.", e);
		}

		try {
			if (!imap.login(username, password)) {
				System.err.println("Could not login to server. Check password.");
				imap.disconnect();
				System.exit(3);
			}

			imap.setSoTimeout(6000);

			imap.capability();

			imap.select("inbox");

			imap.examine("inbox");

			imap.status("inbox", new String[] { "MESSAGES" });

			imap.logout();
			imap.disconnect();
		} catch (IOException e) {
			System.out.println(imap.getReplyString());
			e.printStackTrace();
			System.exit(10);
			return;
		}
	}
}
