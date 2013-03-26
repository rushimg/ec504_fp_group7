import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.html.HTML;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;

import basicWebCrawler.Crawler;
import basicWebCrawler.simpleDS;

public class TestCrawler {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetURL() {
		Crawler crawl = new Crawler();
		simpleDS ds = new simpleDS();
		
		// to write to html doc
		/*Reader stringReader = new StringReader("<html><head><here>abc<div>def</div></here></head><title>testTitle</title></html>");
		HTMLEditorKit htmlKit = new HTMLEditorKit();
		HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
		try {
			htmlKit.read(stringReader, htmlDoc, 0);
		} catch (Exception e) {
			assertEquals(0,1);
		}*/
		crawl.getTitle("<html><head><here>abc<div>def</div></here></head><title>testTitle</title></html><a href=\"http://www.w3schools.com\">Visit W3Schools</a>",ds);
		//System.out.print(htmlDoc.toString());
		assertEquals(ds.getPageTitle(), "testTitle");
	}

}
