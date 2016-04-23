package examples.mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

public final class SMTPMail {
	public static final void main(String[] args) {
		List<String> ccList = new ArrayList();

		FileReader fileReader = null;

		if (args.length < 1) {
			System.err.println("Usage: mail smtpserver");
			System.exit(1);
		}

		String server = args[0];

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("From: ");
			System.out.flush();

			String sender = stdin.readLine();

			System.out.print("To: ");
			System.out.flush();

			String recipient = stdin.readLine();

			System.out.print("Subject: ");
			System.out.flush();

			String subject = stdin.readLine();

			SimpleSMTPHeader header = new SimpleSMTPHeader(sender, recipient, subject);

			for (;;) {
				System.out.print("CC <enter one address per line, hit enter to end>: ");
				System.out.flush();

				String cc = stdin.readLine();

				if ((cc == null) || (cc.length() == 0)) {
					break;
				}
				header.addCC(cc.trim());
				ccList.add(cc.trim());
			}
			String cc;
			System.out.print("Filename: ");
			System.out.flush();

			String filename = stdin.readLine();

			try {
				fileReader = new FileReader(filename);
			} catch (FileNotFoundException e) {
				System.err.println("File not found. " + e.getMessage());
			}

			SMTPClient client = new SMTPClient();
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			client.connect(server);

			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				System.err.println("SMTP server refused connection.");
				System.exit(1);
			}

			client.login();

			client.setSender(sender);
			client.addRecipient(recipient);

			for (String recpt : ccList) {
				client.addRecipient(recpt);
			}
			Writer writer = client.sendMessageData();

			if (writer != null) {
				writer.write(header.toString());
				Util.copyReader(fileReader, writer);
				writer.close();
				client.completePendingCommand();
			}

			if (fileReader != null) {
				fileReader.close();
			}

			client.logout();

			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
