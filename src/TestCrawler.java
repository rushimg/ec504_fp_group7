import static org.junit.Assert.*;

import java.io.FileWriter;

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
	public void testGetLink() {
		Crawler crawl = new Crawler();
		//HTML html = new HTML();
        HTMLDocument html = new HTMLDocument();
        html.
         
		crawl.getLinks(html);
	}

}
