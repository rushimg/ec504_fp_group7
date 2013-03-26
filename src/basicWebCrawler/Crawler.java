package basicWebCrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

import sun.misc.Queue;
import sun.net.www.URLConnection;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;
import com.sun.org.apache.xml.internal.utils.DOMBuilder;
import com.sun.tools.hat.internal.parser.Reader;
import com.sun.tools.javac.util.List;
import com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser;


public class Crawler extends HTMLEditorKit.ParserCallback{
	
	// Class Created by Rushi Ganmukhi to Crawl the BU website

	//constructor 
	public Crawler(){
	}
	
	
	//TODO: what do I want this class to input/output?
	public simpleDS crawl(String url) throws IOException, BadLocationException{
		BufferedReader br = htmlGrabData(url);
		simpleDS ds = new simpleDS();
		this.setSimpleDS(br,ds);
		return ds;
	}
	
	// TODO: passing bufferedReader seems stupid, better way?
	// function to set the attributes of simpleDS
	public void setSimpleDS(BufferedReader br,simpleDS ds) throws IOException, BadLocationException{
	    HTMLEditorKit htmlKit = new HTMLEditorKit();
	    HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
	    HTMLEditorKit.Parser parser = new ParserDelegator();
	    HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
	    parser.parse(br, callback, true);
		ds.setLinks(this.getLinks(htmlDoc));
		ds.setPageTitle(this.getTitle(htmlDoc));
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
// String str = "<html><head></head><body><div><a href=\"/explore/research/\">research</a></div></body></html>";
// InputStream is = new ByteArrayInputStream(str.getBytes());
	//BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	/*String line;
 while ((line = br.readLine()) != null) {
		System.out.println(line);
	}*/
 //Reader stringReader = new StringReader(string);
 //HTMLEditorKit htmlKit = new HTMLEditorKit();
 //HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
 //htmlKit.read(stringReader, htmlDoc, 0);


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void grabHTML(String URLin)throws IOException,ClassNotFoundException,FileNotFoundException {
		URL uu = new URL(URLin);
		InputStream plainStream = uu.openStream();
		OutputStream out = new FileOutputStream(storageFile);
		
		byte[] buf = new byte[1024];
		int len;
		
		while((len = plainStream.read(buf))> 0){
			out.write(buf,0,len);
		}
		plainStream.close();
		out.close();
	}
	
	public void findLinks()throws IOException{
		BufferedReader in = new BufferedReader( new InputStreamReader(new FileInputStream(storageFile)));
		
		ArrayList<String> htmlText = new ArrayList<String>();
		String str;
		
		while((str = in.readLine()) != null){
			htmlText.add(str);
		}
		
		in.close();
		String newLink = "";
		for(int line = 0; line < htmlText.size(); line++){
			String temp = htmlText.get(line);
			boolean inHREF = false;
			if(temp != ""){
				for(int index = 0; index < temp.length();index++){
					if(inHREF != true){
						if(temp.charAt(index) == 'h' && index < temp.length() - 5){
							if(temp.charAt(index+1) == 'r' && temp.charAt(index + 2) == 'e' && temp.charAt(index+3) == 'f'){
								index += 4;
								while(index < temp.length() && temp.charAt(index) == ' ')
									index++;
								if(temp.charAt(index) == '='){
									index++;
									while(index < temp.length() && temp.charAt(index) == ' ')
										index++;
									if(temp.charAt(index) == '\"' || temp.charAt(index) == '\''){
										index++;
										inHREF = true;
										newLink = "";
									}
								}								
							}
						}
					}
					else{		//inHREF = true
						if(temp.charAt(index) == '\'' || temp.charAt(index) == '\"'){
							inHREF = false;
							if(newLink.length() < 4 || (newLink.charAt(0) != 'h' || newLink.charAt(1) != 't' ||newLink.charAt(2) != 't' ||newLink.charAt(3) != 'p')){
								if(rootURL.charAt(rootURL.length() - 1) == '/')
									newLink = currentPage + newLink;
								else
									newLink = currentPage + '/' + newLink;
							}
							//System.out.println(newLink);
							boolean add = checkLink(newLink);
							if(add == true){
								linkLookup.put(newLink, 1);
								links.add(newLink);
								//System.out.println(newLink);
							}
						}
						else
							newLink += temp.charAt(index);
					}
						
						
						
				}	//end line analysis
			}	
		}	
	}	//end findLinks
	
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
	
	public void printLinks(){
		FileWriter fileWriter = null;
		try{
			File linksFile = new File("links.txt");
			fileWriter = new FileWriter(linksFile);
			for(int ii = 0; ii < links.size();ii++){
				String temp = links.get(ii);
				String content = "Entry " + ii + " Hits = ";
				content += linkLookup.get(temp);
				content += " URL = " + temp +"\n";
				fileWriter.write(content);
			}
			fileWriter.close();
			
		} catch(IOException ex){
			System.out.println("File Output Failed!");
		}
		
		
	}
	
}*/	//end class 
