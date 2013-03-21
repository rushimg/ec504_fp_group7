package basicWebCrawler;

public class simpleDS {
	// Simple DS created by Rushi Ganmukhi to hold data from the crawler
	// to be modified later, possible turned into a graph node
	
	private String[] Links;
	private String rawHTML;
	private String pageTitle;
	
	// constructor
	public simpleDS(){	 
	}
 
	// return array of Links from HTML page
	String[] getLinksList(){
		return this.Links;
	}
 
	void setLinks(String[] Link){
	   this.Links = Link;
	}
	
	// return rawHTML
	String getRawHTML(){
		return this.rawHTML; 
	}
	
	void setRawHTML(String html){
		this.rawHTML = html;
	}
	
	// return page title
	String getPageTitle(){
		return this.pageTitle;
	}
	
	void setPageTitle(String title){
		this.pageTitle = title;
	}
}
