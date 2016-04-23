package examples.nntp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public class ExtendedNNTPOps {
	NNTPClient client;

	public ExtendedNNTPOps() {
		this.client = new NNTPClient();
		this.client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
	}

	private void demo(String host, String user, String password) {
		try {
			this.client.connect(host);

			if ((user != null) && (password != null)) {
				boolean success = this.client.authenticate(user, password);
				if (success) {
					System.out.println("Authentication succeeded");
				} else {
					System.out.println("Authentication failed, error =" + this.client.getReplyString());
				}
			}

			NewsgroupInfo testGroup = new NewsgroupInfo();
			this.client.selectNewsgroup("alt.test", testGroup);
			long lowArticleNumber = testGroup.getFirstArticleLong();
			long highArticleNumber = lowArticleNumber + 100L;
			Iterable<Article> articles = this.client.iterateArticleInfo(lowArticleNumber, highArticleNumber);

			for (Article article : articles) {
				if (article.isDummy()) {
					System.out.println("Could not parse: " + article.getSubject());
				} else {
					System.out.println(article.getSubject());
				}
			}

			NewsgroupInfo[] fanGroups = this.client.listNewsgroups("alt.fan.*");
			for (int i = 0; i < fanGroups.length; i++) {
				System.out.println(fanGroups[i].getNewsgroup());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int argc = args.length;
		if (argc < 1) {
			System.err.println("usage: ExtendedNNTPOps nntpserver [username password]");
			System.exit(1);
		}

		ExtendedNNTPOps ops = new ExtendedNNTPOps();
		ops.demo(args[0], argc >= 3 ? args[1] : null, argc >= 3 ? args[2] : null);
	}
}
