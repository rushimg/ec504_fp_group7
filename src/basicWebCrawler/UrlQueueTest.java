package basicWebCrawler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UrlQueueTest {

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
	public void testQueue() {
		UrlQueue fifo = new UrlQueue();
		fifo.enque("URL1");
		fifo.enque("URL2");
		fifo.enque("URL3");
		fifo.enque("URL3");
		fifo.enque("URL4");
		fifo.enque("URL3");
		fifo.enque("URL5");
        assertEquals("URL1",fifo.deque());
        assertEquals("URL2",fifo.deque());
        assertEquals("URL3",fifo.deque());
        assertEquals("URL4",fifo.deque());
        assertEquals("URL5",fifo.deque()); 
	}

}
