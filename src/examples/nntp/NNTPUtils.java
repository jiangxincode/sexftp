package examples.nntp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;

public class NNTPUtils {
	public static List<Article> getArticleInfo(NNTPClient client, long lowArticleNumber, long highArticleNumber)
			throws IOException {
		List<Article> articles = new ArrayList();
		Iterable<Article> arts = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
		for (Article article : arts) {
			articles.add(article);
		}
		return articles;
	}
}
