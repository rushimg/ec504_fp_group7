package basicWebCrawler;

import java.util.ArrayList;

public class simpleDS {
	// Simple DS created by Rushi Ganmukhi to hold data from the crawler
	// to be modified later, possible turned into a graph node
	
	private ArrayList<String> Links;
	private String rawHTML;
	private String pageTitle;
	private String pageURL;
	
	// constructor
	public simpleDS(){	 
	}
 
	// return array of Links from HTML page
	public ArrayList<String> getLinksList(){
		return this.Links;
	}
 
	public void setLinks(ArrayList<String> Link){
	   this.Links = Link;
	}
	
	// return rawHTML
	public String getRawHTML(){
		return this.rawHTML; 
	}
	
	public void setRawHTML(String html){
		this.rawHTML = html;
	}
	
	// return page title
	public String getPageTitle(){
		return this.pageTitle;
	}
	
	public void setPageTitle(String title){
		this.pageTitle = title;
	}
	
	// Current page url
	public String getPageURL(){
		return this.pageURL;
	}
	
	public void setPageURL(String url){
		this.pageURL = url;
	}
}
