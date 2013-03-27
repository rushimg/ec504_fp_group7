package basicWebCrawler;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class Crawler {
	
	// Class Created by Rushi Ganmukhi to Crawl the BU website
	
    private UrlQueue urlQueue = new UrlQueue();
    private final String rootURL = "http://www.bu.edu";
    
	//constructor 
	public Crawler(){
		//TODO: serious cleanup, what vars passed around?
		//TODO: Actually perform this recursively
		urlQueue.enque(rootURL);
	}
	
	public simpleDS recursiveCrawl() throws IOException, BadLocationException{
		return this.crawl(urlQueue.deque());
	}
	
	public simpleDS crawl(String url) throws IOException, BadLocationException{
		simpleDS ds = new simpleDS();
		BufferedReader br = htmlGrabData(url);
		if (br != null){
			this.setSimpleDS(br,ds,url);
		}
		return ds;
	}
	
	public BufferedReader htmlGrabData(String strUrl){
		BufferedReader br = null;
	    try{
		URL url = new URL(strUrl);
		java.net.URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
	    }catch(IOException e){
	    	//die silently on bad url
	    }
		return br;
	}
	
	// function to set the attributes of simpleDS
	public void setSimpleDS(BufferedReader br,simpleDS ds,String url) throws IOException, BadLocationException{
	    HTMLEditorKit htmlKit = new HTMLEditorKit();
	    HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
	    HTMLEditorKit.Parser parser = new ParserDelegator();
	    HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
	    parser.parse(br, callback, true);
	    
		ds.setLinks(this.getLinks(htmlDoc,url));
		ds.setPageTitle(this.getTitle(htmlDoc));
		ds.setRawHTML(this.getBody(htmlDoc));
		ds.setRawText(this.getTextBody(htmlDoc));
		ds.setPageURL(url);
	}

	public String getBody(HTMLDocument htmlDoc) throws BadLocationException, IOException{
		StringWriter writer = new StringWriter();
		HTMLEditorKit kit = new HTMLEditorKit();
		kit.write(writer, htmlDoc, 0, htmlDoc.getLength());
		String s = writer.toString();
		return s;
	}
	
	public String getTextBody(HTMLDocument htmlDoc) throws BadLocationException, IOException{
		StringWriter writer = new StringWriter();
		HTMLEditorKit kit = new HTMLEditorKit();
		kit.write(writer, htmlDoc, 0, htmlDoc.getLength());
		return htmlDoc.getText(0, htmlDoc.getLength());
	}
	
	public String getTitle(HTMLDocument htmlDoc){
	     return  (String) htmlDoc.getProperty(HTMLDocument.TitleProperty); 
    }

	//Code for this method written with the aide of http://www.java2s.com/Code/Java/Swing-JFC/HTMLDocumentDocumentIteratorExample.htm
	public ArrayList<String> getLinks(HTMLDocument htmlDoc, String currentUrl){
		    ArrayList<String> links = new ArrayList<String>();
		    
		    for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.A); iterator
		        .isValid(); iterator.next()) {
		      AttributeSet attributes = iterator.getAttributes();
		      String srcString = (String) attributes
		          .getAttribute(HTML.Attribute.HREF);
              if (srcString != null){ // check for null, it happens
            	  srcString = this.checkLink(srcString, currentUrl);
		          links.add(srcString);
		          urlQueue.enque(srcString); // add links to the pages that need to be crawled.
              }
		    }
		    return links;
	}
	
	//TODO: More checks?
	public String checkLink(String link, String base){
		if (!(link.contains(base))){
			return base + link;
		}
		return link;
	}
	
}	
