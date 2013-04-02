package htmlFilter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class JUnitTestFilter {

	@Test
	public void testParsing() throws UnsupportedEncodingException, IOException {
		Filter filter = new Filter();
		String inputHTML = filter.getHTML("http://algorithmics.bu.edu/twiki/pub/EC504/HomeworkTwoCompUpload/pg250.txt");
		String textFiltered = filter.filterToText(inputHTML);
		System.out.println(textFiltered);
		filter.parse(textFiltered);
        filter.printHashMap();       
	}
	
	@Test
	public void testCompression() throws UnsupportedEncodingException, IOException {
		Filter filter = new Filter();
		String inputHTML = filter.getHTML("http://www.bu.edu/ece");
		//System.out.println("string size before compression: " + inputHTML.length());
        String encodeStr = filter.encode(inputHTML);
        //System.out.println("string size after encoding: " + encodeStr.length());
        String decodeStr = filter.decode(encodeStr);
        //System.out.println("string size after decoding: " + decodeStr.length());
        //assertEquals(inputHTML, decodeStr);
        assertEquals(inputHTML.length(), decodeStr.length());
        
        inputHTML = filter.getHTML("http://www.bu.edu");
        encodeStr = filter.encode(inputHTML);
        decodeStr = filter.decode(encodeStr);
        assertEquals(inputHTML.length(), decodeStr.length());
        
        inputHTML = filter.getHTML("http://www.amazon.com");
        encodeStr = filter.encode(inputHTML);
        decodeStr = filter.decode(encodeStr);
        assertEquals(inputHTML.length(), decodeStr.length());
        
        inputHTML = filter.getHTML("http://www.ebay.com");
        encodeStr = filter.encode(inputHTML);
        decodeStr = filter.decode(encodeStr);
        assertEquals(inputHTML.length(), decodeStr.length());
        
        inputHTML = filter.getHTML("http://www.cs.bu.edu");
        encodeStr = filter.encode(inputHTML);
        decodeStr = filter.decode(encodeStr);
        assertEquals(inputHTML.length(), decodeStr.length());
	}
}
