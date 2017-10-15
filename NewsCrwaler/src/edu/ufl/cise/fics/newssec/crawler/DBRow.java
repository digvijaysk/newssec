package edu.ufl.cise.fics.newssec.crawler;

/**
 * Class representing row in the database for a News house.
 * 
 *
 */
public class DBRow {
	private String name;
	private String continent;
	private String country;
	private String coverage;
	private String link;
	private String mediaType;
	private String mediaFocus;
	private String language;
	private String source;
	private int twitter_followers;
	private int facebook_likes;
	private int quantcast_rank;
	private int google_trend_index;

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaFocus() {
		return mediaFocus;
	}

	public void setMediaFocus(String mediaFocus) {
		this.mediaFocus = mediaFocus;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public DBRow(int index, String val) {
		if (index == 1) {
			this.coverage = val;
		} else if (index == 2) {
			this.link = val;
		} else if (index == 3) {
			this.mediaType = val;
		} else if (index == 4) {
			this.mediaFocus = val;
		} else if (index == 5) {
			this.language = val;
		} else if (index == 6) {
			this.source = val;
		}
	}

	public DBRow(String coverage, String link, String mediaType, String mediaFocus, String language, String source) {
		this.coverage = coverage;
		this.link = link;
		this.mediaType = mediaType;
		this.mediaFocus = mediaFocus;
		this.language = language;
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getTwitter_followers() {
		return twitter_followers;
	}

	public void setTwitter_followers(int twitter_followers) {
		this.twitter_followers = twitter_followers;
	}

	public int getFacebook_likes() {
		return facebook_likes;
	}

	public void setFacebook_likes(int facebook_likes) {
		this.facebook_likes = facebook_likes;
	}

	public int getQuantcast_rank() {
		return quantcast_rank;
	}

	public void setQuantcast_rank(int quantcast_rank) {
		this.quantcast_rank = quantcast_rank;
	}

	public int getGoogle_trend_index() {
		return google_trend_index;
	}

	public void setGoogle_trend_index(int google_trend_index) {
		this.google_trend_index = google_trend_index;
	}

	/**
	 * Creates and returns an insert query for News_websites database.
	 * 
	 * @return String- Insert query for news_websites database
	 */
	public String getInsertQuery() {

		return new StringBuilder().append(
				"INSERT INTO  news_websites (name, continent, country, coverage, url, media_type, media_focus, language, source, ")
				.append("twitter_followers, facebook_likes, quantcast_rank, google_trend_index) values( '")
				.append(getName()).append("','").append(getContinent()).append("','").append(getCountry()).append("','")
				.append(getCoverage()).append("','").append(getLink()).append("','").append(getMediaType())
				.append("','").append(getMediaFocus()).append("','").append(getLanguage()).append("','")
				.append(getSource()).append("',").append("null").append(",").append("null").append(",").append("null")
				.append(",").append("null").append(");").toString();
	}
}