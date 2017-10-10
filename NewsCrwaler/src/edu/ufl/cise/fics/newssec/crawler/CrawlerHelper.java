package edu.ufl.cise.fics.newssec.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerHelper {
	// We'll use a fake USER_AGENT so the web server thinks the robot is a normal
	// web browser.
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	public static Set<String> pagesVisited = new HashSet<String>();
	
	private Document htmlDocument;

	static {
		pagesVisited.add("http://www.abyznewslinks.com/");
		pagesVisited.add("http://www.abyznewslinks.com/seara.htm");
		pagesVisited.add("http://www.abyznewslinks.com/priva.htm");
		pagesVisited.add("http://www.abyznewslinks.com/admod.htm");
		pagesVisited.add("http://www.abyznewslinks.com/resou.htm");
		pagesVisited.add("http://www.abyznewslinks.com/about.htm");
		pagesVisited.add("http://www.abyznewslinks.com/contc.htm");
	}
	/**
	 * This performs all the work. It makes an HTTP request, checks the response,
	 * and then gathers up all the links on the page. Perform a searchForWord after
	 * the successful crawl
	 * 
	 * @param url
	 *            - The URL to visit
	 * @return whether or not the crawl was successful
	 */
	public List<String> crawl(String url) {
		List<String> links = new LinkedList<String>();
		try {

			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
															// indicating that everything is great.
			{
				System.out.println("\n**Visiting** Received web page at " + url);
			}
			/*
			 * if(!connection.response().contentType().contains("text/html")) {
			 * System.out.println("**Failure** Retrieved something other than HTML"); return
			 * false; }
			 */
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage) {
				
				String pageUrl =link.absUrl("href");
				if(pageUrl.contains("abyznewslinks.com") && !isVisitedPage(pageUrl)) {
					System.out.println(link.attr("href")+"----------------------------------------------");
					links.add(pageUrl);
				}
				
			}
			return links;
		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			return links;
		}
	}

	/**
	 * Performs a search on the body of on the HTML document that is retrieved. This
	 * method should only be called after a successful crawl.
	 * 
	 * @param searchWord
	 *            - The word or string to look for
	 * @return whether or not the word was found
	 */
	public boolean searchForWord(String searchWord) {
		// Defensive coding. This method should only be used after a successful crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}
		System.out.println("Searching for the word " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}
	
	public static  boolean isVisitedPage(String url) {
		if(CrawlerHelper.pagesVisited.contains(url)) {
			return true;
		}
		CrawlerHelper.pagesVisited.add(url);
		return false;
	}

	/*public List<String> getLinks() {
		return this.links;
	}*/

}
