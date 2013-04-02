package basicWebCrawler;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FunctionalCrawlerTest {

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
	public void test() throws IOException, BadLocationException {
		Crawler crawl = new Crawler();
		crawl.setPrintOutput(true);
		crawl.startCrawling();
	}

}
