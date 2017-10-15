package edu.ufl.cise.fics.newssec.crawler;

import java.io.IOException;
import java.util.ArrayList;
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
	List<DBRow> bulkData = null;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	public static Set<String> pagesVisited = new HashSet<String>();

	private Document htmlDocument;
	private List<DBRow> dbRecords;

	public CrawlerHelper() {
		bulkData = new ArrayList<DBRow>();
	}

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
	 * This performs all the work. It makes an HTTP request, checks the
	 * response, and then gathers up all the links on the page. Perform a
	 * searchForWord after the successful crawl
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
			if (connection.response().statusCode() == 200) {
				System.out.println("\n **Visiting** Received web page at " + url);
			}

			if (!isLeaf("Media Type")) {
				Elements linksOnPage = htmlDocument.select("a[href]");

				System.out.println("Found (" + linksOnPage.size() + ") links");
				for (Element link : linksOnPage) {
					System.out.println(link.attr("href") + "----------------------------------------------");
					String pageUrl = link.absUrl("href");
					if (pageUrl.contains("abyznewslinks.com") && !isVisitedPage(pageUrl)) {
						links.add(pageUrl);
					}
					return links;
				}
			} else {
				parseLeafNode();
				return links; // no links
			}
		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			return links;
		}
		return links;
	}

	private void parseLeafNode() {
		Elements trElements = this.htmlDocument.getElementsByTag("tr");
		removeTop4Rows(trElements);
		parseAllRows(trElements);
	}

	private void parseAllRows(Elements trElements) {
		for (Element element : trElements) {
			if (element.children().size() == 6) {
				parseHTMLBlock(element);
			}
		}
	}

	private void parseHTMLBlock(Element element) {
		int index = 1;
		int listSize = bulkData.size();

		for (Element row : element.children()) {
			parseHTMLLine(row, index, listSize);
			index++;
		}
	}

	private void parseHTMLLine(Element element, int lineNo, int size) {

		int count = 0;
		String row = element.html();
		row = removeFontTag(row);
		String[] colValue = getColumnValues(row);

		for (String s : colValue) {
			if (lineNo == 1) {
				DBRow dbrow = new DBRow(1, s);
				bulkData.add(dbrow);
			} else {
				if (lineNo == 2) {
					bulkData.get(size + count).setLink(parseHrefToLink(s));
				} else if (lineNo == 3) {
					bulkData.get(size + count).setMediaType(s);
				} else if (lineNo == 4) {
					bulkData.get(size + count).setMediaFocus(s);
				} else if (lineNo == 5) {
					bulkData.get(size + count).setLanguage(s);
				} else if (lineNo == 6) {
					bulkData.get(size + count).setSource(s);
				}
			}
			count++;
		}
	}

	private String parseHrefToLink(String s) {
		return s.substring(s.indexOf('"'), s.lastIndexOf('"'));
	}

	private String[] getColumnValues(String s) {
		//numberOfRows = s.lastIndexOf("<br>") - s.indexOf("<br>") + 1;
		return s.split("<br>");
	}

	private String removeFontTag(String row) {
		String s = row.substring(row.indexOf(">") + 1);
		if (s.contains("<")) {
			return s.substring(0, s.lastIndexOf("<"));
		} else
			return s;
	}

	private void removeTop4Rows(Elements trElements) {
		trElements.remove(0);
		trElements.remove(0);
		trElements.remove(0);
		trElements.remove(0);
	}

	/**
	 * Performs a search on the body of on the HTML document that is retrieved.
	 * This method should only be called after a successful crawl.
	 *
	 * @param searchWord
	 *            - The word or string to look for
	 * @return whether or not the word was found
	 */
	public boolean searchForWord(String searchWord) {
		// Defensive coding. This method should only be used after a successful
		// crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}
		System.out.println("Searching for the word " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	public static boolean isVisitedPage(String url) {
		if (CrawlerHelper.pagesVisited.contains(url)) {
			return true;
		}
		CrawlerHelper.pagesVisited.add(url);
		return false;
	}

	public boolean isLeaf(String searchWord) {
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}
		System.out.println("Searching for the word " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}
}