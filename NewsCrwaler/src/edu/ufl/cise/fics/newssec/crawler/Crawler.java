package edu.ufl.cise.fics.newssec.crawler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author digvijay kulkarni
 *
 */
public class Crawler {
	private static final int MAX_PAGES_TO_SEARCH = 30;

	private int visitedPageCount = 0;

	public void search(String url) {
		if (visitedPageCount > MAX_PAGES_TO_SEARCH) {
			return;
		}

		List<String> pagesToVisit = new LinkedList<String>();
		CrawlerHelper helper = new CrawlerHelper();
		pagesToVisit = helper.crawl(url);
		System.out.println(url);

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
			PreparedStatement stmt = con.getConnection().prepareStatement(row.getInsertQuery());
			stmt.execute();
		}

		con.getConnection().commit();
		con.disconnect();
	}

	public static void main(String[] args) throws SQLException {
		// Crawler spider = new Crawler();
		// spider.search("http://www.abyznewslinks.com/allco.htm");
		MySQLConnection con = new MySQLConnection();
		PreparedStatement stmt = con.getConnection().prepareStatement(
				"INSERT INTO  news_websites (name, continent, country, coverage, url, media_type, media_focus, language, source, twitter_followers, facebook_likes, quantcast_rank,"
						+ "google_trend_index) values( 'AajTak', 'Asia', 'India', 'asdas', 'http://aajtak3.com', 'bc', 'drama', 'hindi', 'tv', 12, 13, 23, 80);");

		stmt.execute();
		con.disconnect();

		// public static void main(String[] args) {
		// Crawler spider = new Crawler();
		// spider.search("http://www.abyznewslinks.com/nicar.htm");

	}
}
