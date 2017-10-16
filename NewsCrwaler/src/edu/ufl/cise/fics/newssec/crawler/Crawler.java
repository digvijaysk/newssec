package edu.ufl.cise.fics.newssec.crawler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author digvijay kulkarni
 *
 */
public class Crawler {
	private static final int MAX_PAGES_TO_SEARCH = 30;
	private static Logger logger = LoggerFactory.getLogger(Crawler.class);
	private int visitedPageCount = 0;

	public void search(String url) {
		if (visitedPageCount > MAX_PAGES_TO_SEARCH) {
			return;
		}

		List<String> pagesToVisit = new LinkedList<String>();
		CrawlerHelper helper = new CrawlerHelper();
		pagesToVisit = helper.crawl(url);
		logger.info(url);

		for (String nextPage : pagesToVisit) {
			search(nextPage);
		}
	}

	/**
	 * Commit the list of DBRow to the database.
	 * 
	 * @param rows
	 *            - List<DBRow>
	 * @throws SQLException
	 */
	public static void saveRowsToDatabase(List<DBRow> rows) throws SQLException {
		MySQLConnection con = new MySQLConnection();
		con.getConnection().setAutoCommit(false);

		for (DBRow row : rows) {
			if (CrawlerHelper.isVisitedPage(row.getLink())) {
				continue;
			}
			PreparedStatement stmt = con.getConnection().prepareStatement(row.getInsertQuery());
			stmt.execute();
		}

		con.getConnection().commit();
		con.disconnect();
	}

	public static void main(String[] args) throws SQLException {

		Crawler spider = new Crawler();
		spider.search("http://www.abyznewslinks.com/allco.htm");
		int count = 1;

		logger.info("Failed lines:");
		for (String err : CrawlerHelper.errorRows) {
			logger.info(count + err + "\n");
			count++;
		}

	}
}
