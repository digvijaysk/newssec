package edu.ufl.cise.fics.newssec.crawler;

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

	public static void main(String[] args) {
		Crawler spider = new Crawler();
		spider.search("http://www.abyznewslinks.com/nicar.htm");
	}
}
