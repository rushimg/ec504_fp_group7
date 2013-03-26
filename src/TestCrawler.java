import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.text.*;
import javax.swing.text.html.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import basicWebCrawler.Crawler;
import basicWebCrawler.simpleDS;

public class TestCrawler {
	// functional and unit test for testCrawler.
	
    public HTMLDocument testDoc;
    public Crawler testCrawl;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String test = "<!DOCTYPE HTML PUBLIC \"-//w3c//DTD html 3.2//EN\"><html><head><title>Test Title</title>"
  	          + "</head><a href=\"http://www.google.com\">123</a><body>the test body</body></html>";
  	   HTMLEditorKit kit = new HTMLEditorKit(); 
  	   HTMLDocument doc = new HTMLDocument();

  	   kit.read(new StringReader(test), doc, 0);
  	   this.testDoc = doc;
  	   
  	   Crawler crawl = new Crawler();
  	   this.testCrawl = crawl;

	}

	@After
	public void tearDown() throws Exception {
		this.testDoc = null;
		this.testCrawl = null;
	}
	
    @Test
    public void testGetTitle() throws IOException, BadLocationException{
    	   assertEquals("Test Title", this.testCrawl.getTitle(this.testDoc));
    }
    
    @Test
    public void testGetBody() throws IOException, BadLocationException{
    	   assertTrue(this.testCrawl.getBody(this.testDoc).contains("<title>Test Title</title>"));
    	   assertTrue(this.testCrawl.getBody(this.testDoc).contains("the test body"));
    }
    
    @Test
    public void testGetLinks() throws IOException, BadLocationException{
    	   assertEquals("http://www.google.com", (this.testCrawl.getLinks(this.testDoc)).get(0));
    }
   
	@Test 
	// Functional Test for simple one page crawl
	public void testFunctionalCrawl() throws IOException, BadLocationException{
		simpleDS ds = this.testCrawl.crawl("http://people.bu.edu/rushimg/");
		//System.out.println(ds.getLinksList().toString());
		assertEquals("Your Name Home Page",ds.getPageTitle());
		assertEquals("http://people.bu.edu/rushimg/",ds.getPageURL());
		//System.out.println(ds.getRawHTML());
	}

}
