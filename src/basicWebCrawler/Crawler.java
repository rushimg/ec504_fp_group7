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

	//constructor 
	public Crawler(){
		//TODO: serious cleanup, what vars passed around?
		//TODO: Actually perform this recursively
	}
	
	public simpleDS crawl(String url) throws IOException, BadLocationException{
		BufferedReader br = htmlGrabData(url);
		simpleDS ds = new simpleDS();
		this.setSimpleDS(br,ds,url);
		return ds;
	}
	
	// function to set the attributes of simpleDS
	public void setSimpleDS(BufferedReader br,simpleDS ds,String url) throws IOException, BadLocationException{
	    HTMLEditorKit htmlKit = new HTMLEditorKit();
	    HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
	    HTMLEditorKit.Parser parser = new ParserDelegator();
	    HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
	    parser.parse(br, callback, true);
	    
		ds.setLinks(this.getLinks(htmlDoc));
		ds.setPageTitle(this.getTitle(htmlDoc));
		ds.setRawHTML(this.getBody(htmlDoc));
		ds.setPageURL(url);
	}
	
	// TODO: remove html tabs and newlines in string
	public String getBody(HTMLDocument htmlDoc) throws BadLocationException, IOException{
		StringWriter writer = new StringWriter();
		HTMLEditorKit kit = new HTMLEditorKit();
		kit.write(writer, htmlDoc, 0, htmlDoc.getLength());
		String s = writer.toString();
		return s;
	}
	
	public String getTitle(HTMLDocument htmlDoc){
	     return  (String) htmlDoc.getProperty(HTMLDocument.TitleProperty); 
    }
	
	//TODO: Need to check links and append root url if not complete, also check css, etc. links
	//Code for this method written with the aide of http://www.java2s.com/Code/Java/Swing-JFC/HTMLDocumentDocumentIteratorExample.htm
	public ArrayList<String> getLinks(HTMLDocument htmlDoc){
		    ArrayList<String> links = new ArrayList<String>();
		    
		    for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.A); iterator
		        .isValid(); iterator.next()) {
		      AttributeSet attributes = iterator.getAttributes();
		      String srcString = (String) attributes
		          .getAttribute(HTML.Attribute.HREF);
		      links.add(srcString);
		    }
		    return links;
	}
	
	public BufferedReader htmlGrabData(String strUrl) throws IOException{
		URL url = new URL(strUrl);
	    java.net.URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		return br;
	}

}	

	/*
	private String baseURL = "http://www.bu.edu";

	protected ArrayList<String> links;
	protected HashMap<String,Integer> linkLookup;
	protected final String storageFile = "storage.txt";
	protected String rootURL;
	protected String currentPage;
	protected int indexToLinks;
	
	Crawler(String URLin, String setRoot){
		links = new ArrayList<String>();
		linkLookup = new HashMap<String,Integer>();
		//String rootURL;
		if(setRoot == null){
			rootURL = "";
			int www = 0;
			boolean hold = false;
			String holding = "";
			for(int URLIndex = URLin.length() - 1; URLIndex >= 0; URLIndex--){
			//	char aa = URLin.charAt(URLIndex);
				if(URLin.charAt(URLIndex) == '/' && URLIndex != URLin.length()-1){
					//we are done
					break;
				}
				else if(URLin.charAt(URLIndex) == '.'){
					if(hold == false)
						hold = true;
					else{
						rootURL += '.';
						rootURL += holding;
						holding = "";
						www = 0;
					}
				}
				else if(hold == true && (URLin.charAt(URLIndex) != 'w' && URLin.charAt(URLIndex) != 'W')){
					hold = false;
					rootURL += '.';
					if(www != 0){
						www = 0;
						rootURL += holding;
						holding = "";
					}
					rootURL += URLin.charAt(URLIndex);
				}
				else if(hold == true && (URLin.charAt(URLIndex) == 'w' || URLin.charAt(URLIndex) == 'W')){
					www++;
					if(www == 3)
						break;
					else{
						holding += URLin.charAt(URLIndex);
					}
				}
				else 
					rootURL += URLin.charAt(URLIndex);
				
				
			}
		}
		else
			rootURL = setRoot;
		String temp = "";
		for(int ii = rootURL.length()-1; ii >=0;ii--)
			temp += rootURL.charAt(ii);
		
		rootURL = temp;
		
		//rootURL = URLin;
		links.add(URLin);
		indexToLinks = 0;
		long minuteCounter = 0;
		long runningTime = 0;
		int count = 1;
		try{
			while(indexToLinks < links.size()){
				currentPage = links.get(indexToLinks);
				try{
				grabHTML(links.get(indexToLinks));
				findLinks();
				}catch (FileNotFoundException e){
					System.out.println("\t Illegal URL: " + currentPage);
				}
				indexToLinks = links.size() + 1;
				if(System.currentTimeMillis() - minuteCounter > 10000){
					System.out.println("Number of links found = " + links.size() + "    Links processed = " + indexToLinks +"\t"+ (runningTime*10) + "s \t count = " + count);
					minuteCounter = System.currentTimeMillis();
					runningTime++;
					count++;
					//if(count>3)
					//	break;
				}
				
			}
			printLinks();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}
	

	
	public boolean checkLink(String newLink){
		if(newLink.length() < 7)
			return false;
		String httpCheck = newLink.substring(0, 7);
		String httpTest = "http://";
		if(!httpCheck.equals(httpTest))
			return false;
		String extensionCheck = newLink.substring(newLink.length() - 4);
		if(extensionCheck.equals(".css"))
			return false;
		int index = 7;
		String rootCapture = "";
		while(index < newLink.length() && newLink.charAt(index) != '/')
			rootCapture += newLink.charAt(index++);
		if(rootCapture.length() < rootURL.length())
			return false;
		rootCapture = rootCapture.substring(rootCapture.length() - rootURL.length());
		if(rootCapture.equals(rootURL))
			return false;
		
		
		if(linkLookup.get(newLink) == null)
			return true;
		else{
			int hits = linkLookup.get(newLink);
			linkLookup.remove(newLink);
			linkLookup.put(newLink, hits+1);
			return false;
		}
		
	}	//end CheckLink
	
	
}*/	//end class 
