package htmlFilter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**1. filter input html text to get useful content text.
 * 2. parse useful text to words and count their frequencies.
 * 3. Use huffman coding to encode the useful text (after 1,2 work well)
 * 
 * @author Tong Liu
 *
 */

public class filter {
    private String inputHTML;
    private HashMap<String, Integer> freq = new HashMap<String, Integer>();
    
    /**
     * constructor - initialization
     */
    filter(String htmlText) {
        inputHTML = htmlText;
    }
    
    /**use a simple regex expression to test first
     * 
     * @return - text after filtering
     */
    public String filterToText() {
        return inputHTML.replaceAll("<[^>]*>", "");
    }
    
    /**parse the filtered text with "space" - DEC 32 in ascii
     * 
     * @param filteredText - text to be parsed 
     */
    public void parse(String filteredText) {
        String subStr;      //word parsed
        int strLen = filteredText.length();     //length of filteredText
        for (int i = 0;i < strLen;i++) {
            if (filteredText.charAt(i) != ' ') {
                int subStrEnd = i + 1;          //end index of the word
                while (filteredText.charAt(subStrEnd) != ' ') {     //looking for the end of the subString
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
    
    /**Store the word parsed into hash map and update frequency
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
    
    /**Print all the words and their frequencies from hash map
     * 
     */
    public void printHashMap() {
        Iterator<Map.Entry<String, Integer>> freqIter = freq.entrySet().iterator();
        while (freqIter.hasNext()) {
            Map.Entry<String, Integer> tempEntry = freqIter.next();
            String tempWord = tempEntry.getKey();
            int tempFreq = tempEntry.getValue();
            System.out.println(tempWord + ": " + tempFreq);
        }
    }
    
    public void encode(String input) {
        
    }
}