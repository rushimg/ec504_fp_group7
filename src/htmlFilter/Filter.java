package htmlFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 1. filter input HTML text to get useful content text.
 * 2. parse useful text to words and count their frequencies.
 * 3. Use GZIP to encode the useful text (after 1,2 work well)
 * 
 * @author Tong Liu
 *
 */

public class Filter {
	public class Index {
		String key;
		int frequency;
		int nodeIndex;
	}
  
    private HashMap<String, Integer> freq = new HashMap<String, Integer>();
    
    private Comparator<Index> cmp = new Comparator<Index>() {
        @Override
        public int compare(Index t, Index t1) {
            int tFreq = t.frequency;
            int t1Freq = t1.frequency;
            if (tFreq > t1Freq) 
                return -1;
            else if (tFreq < t1Freq)
                return 1;
            return 0;
        }
    };
    
    private PriorityQueue<Index> freqPQ= new PriorityQueue<>(10, cmp); 
    /**
     * constructor - initialization
     */
    public Filter() {

    }
    
    /**
     * use several complicated regex expression to filter script, style and html tags
     * 
     * Notice: regex expressioon of script and style come from http://keml.iteye.com/blog/1617223
     * 
     * @param inputHTML - input html text to be filtered
     * @return - text after filtering
     */
    public String filterToText(String inputHTML) {
        String outputStr = inputHTML;

        String scriptRegex = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";    //define regex expression of script
        String styleRegex = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";       //define regex expression of style
        String htmlRegex = "<[^>]+>";                                                              //define regex expression of html

        //filter script tags
        Pattern scriptPattern = Pattern.compile(scriptRegex, Pattern.CASE_INSENSITIVE);
        Matcher scriptMatcher = scriptPattern.matcher(outputStr);
        outputStr = scriptMatcher.replaceAll("");

        //filter style tags
        Pattern stylePattern = Pattern.compile(styleRegex, Pattern.CASE_INSENSITIVE);
        Matcher styleMatcher = stylePattern.matcher(outputStr);
        outputStr = styleMatcher.replaceAll("");   

        //filter html tags
        Pattern htmlPattern = Pattern.compile(htmlRegex, Pattern.CASE_INSENSITIVE);
        Matcher htmlMatcher = htmlPattern.matcher(outputStr);
        outputStr = htmlMatcher.replaceAll("");    //space problem still needs to be solved, like: E</span>dit >> E dit after parsing
        
        //filter others
        outputStr = outputStr.replaceAll("&nbsp;", "");
        outputStr = outputStr.replaceAll("&lt;", "");
        outputStr = outputStr.replaceAll("&copy;", "");
        outputStr = outputStr.replaceAll("&amp;", "");
        
        outputStr = outputStr.replaceAll("\t","");
        outputStr = outputStr.replaceAll("\n\r","");
        
        return outputStr;
    }
    
    /**
     * Test whether the character input is part of a useful word
     * 
     * @param inputChar - the character to be test
     * @return - whether the character is useful for a word to count, that is, number or letter
     */
    public boolean isUseful(char inputChar) {
        if ((inputChar >= '0') && (inputChar <= '9')) {
            return true;       //is number
        }
        if ((inputChar >= 'a') && (inputChar <= 'z')) {
            return true;       //is lower letter
        }
        if ((inputChar >= 'A') && (inputChar <= 'Z')) {
            return true;       //is capital letter
        }
        
        return false;
    }
    
    /**
     * parse the filtered text to words and their frequencies - still need some optimization
     * 
     * @param filteredText - text to be parsed 
     */
    public void parse(String filteredText) {
    	filteredText = filteredText.toLowerCase();
        String subStr;      //word parsed
        int strLen = filteredText.length();     //length of filteredText
        for (int i = 0;i < strLen;i++) {
            if (isUseful(filteredText.charAt(i))) { //judge for useful information
                int subStrEnd = i + 1;          //end index of the word
                while (isUseful(filteredText.charAt(subStrEnd))) {     //looking for the end of the subString
                    if (subStrEnd == strLen - 1) {      //to avoid indexOutOfBoundException
                        subStr = filteredText.substring(i, subStrEnd);  //edge case - at the end of the string
                        putIntoHashmap(subStr);
                        return;
                    }
                    subStrEnd++;
                }
                subStr = filteredText.substring(i, subStrEnd);
                putIntoHashmap(subStr);
                i = subStrEnd - 1;      //skip the word parsed
                //System.out.println(subStr);
            }
        }
    }
    
    /**
     * Store the word parsed into hash map and update frequency
     * 
     * @param word - the word parsed to put into hash map
     */
    public void putIntoHashmap(String word) {
        if (freq.get(word) == null) {
            freq.put(word, 1);
        }
        else {
            freq.put(word, freq.get(word) + 1);
        }
    }
    
    /**
     * Print all the words and their frequencies from hash map
     */
    public void printHashMap() {
        Iterator<Map.Entry<String, Integer>> freqIter = freq.entrySet().iterator();
        while (freqIter.hasNext()) {
            Map.Entry<String, Integer> tempEntry = freqIter.next();
            String tempWord = tempEntry.getKey();
            int tempFreq = tempEntry.getValue();
            System.out.println(tempWord + " : " + tempFreq);
        }
    }
    
    /**
     * store words and frequencies and index info into priorityqueue
     */
    public void storeInOrder() {
    	Iterator<Map.Entry<String, Integer>> freqIter = freq.entrySet().iterator();
        while (freqIter.hasNext()) {
        	Map.Entry<String, Integer> tempEntry = freqIter.next();
        	Index tempIndex = new Index();
        	tempIndex.key = tempEntry.getKey();
        	tempIndex.frequency = tempEntry.getValue();
        	freqPQ.add(tempIndex);
        }
    }
    
    /**
     * print info in order of frequencies
     */
    public void printInOrder() {
    	while(freqPQ.size() > 0) {
    		Index tempIndex = freqPQ.poll();
    		System.out.println(tempIndex.key + " " + tempIndex.frequency + " " + tempIndex.nodeIndex);
    	}
    }
    
    /**
     * return hashmap freq containing words and frequency
     */
    public HashMap<String, Integer> getHashMap() {      
        return freq;
    }
    
    /**
     * return priorityQueue in order
     */
    public PriorityQueue<Index> getPQ() {
    	return freqPQ;
    }
    
    /**
     * encode input string using GZIP (efficiency of huffman is too low)
     * "ISO-8859-1"
     * 
     * @param input - string to be encoded
     * @return - string after encoding
     */
    public String encode(String input) throws UnsupportedEncodingException, IOException {
        if (input == null) {
            System.out.println("Input Format Error");
            return null;
        }
        
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(baos);
        gos.write(input.getBytes());
        gos.close();
        
        return baos.toString("ISO-8859-1");
    }
       
    /**
     * decode input string using GZIP()
     * "GBK"
     * 
     * @param input - string to be decoded
     * @return - string after decoding
     */
    public String decode(String input) throws UnsupportedEncodingException, IOException {
        if (input == null) {
            System.out.println("Input Format Error");
            return null;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes("ISO-8859-1"));
        GZIPInputStream gis = new GZIPInputStream(bais);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gis.read(buffer)) >= 0) {
            baos.write(buffer, 0, n);
        }
         
        return baos.toString("GBK");
    }
    
    /**get HTML text of specified url
     * 
     * @param urlStr - url to get HTML text
     * @return - HTML text
     */
    public String getHTML(String urlStr) {
        String tempStr = null;
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            InputStreamReader is = new InputStreamReader(url.openStream(), "gb2312");
            BufferedReader br = new BufferedReader(is);
            
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr + "\r\n");
            }
            
            br.close();
        } catch (Exception ex) {
            System.out.println("url error");
        }
              
        return sb.toString();
    }
}