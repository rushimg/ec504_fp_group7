package htmlFilter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1. filter input HTML text to get useful content text.
 * 2. parse useful text to words and count their frequencies.
 * 3. Use Huffman coding to encode the useful text (after 1,2 work well)
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
    
    /**
     * use several complicated regex expression to filter script, style and html tags
     * 
     * @return - text after filtering
     */
    public String filterToText() {
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

        //filter style tags
        Pattern htmlPattern = Pattern.compile(htmlRegex, Pattern.CASE_INSENSITIVE);
        Matcher htmlMatcher = htmlPattern.matcher(outputStr);
        outputStr = htmlMatcher.replaceAll("");    //space problem still needs to be solved, like: E</span>dit >> E dit after parsing
        
        //filter others
        outputStr = outputStr.replaceAll("&nbsp;", "");
        outputStr = outputStr.replaceAll("&lt;", "");
        outputStr = outputStr.replaceAll("&copy;", "");
        
        return outputStr;
    }
    
    /**
     * Test whether the character input is part of a useful word
     * 
     * @param inputChar - the character to be tst
     * @return - whether the character is useful for a word to count, that is, number or letter
     */
    public boolean isUseful(char inputChar) {
        if ((inputChar >= '0') && (inputChar <= '9')) {
            return true;       //is number
        }
        if ((inputChar >= 'a') && (inputChar <= 'z')) {
            return true;       //is capital letter
        }
        if ((inputChar >= 'A') && (inputChar <= 'Z')) {
            return true;       //is lower letter
        }
        
        return false;
    }
    
    /**
     * parse the filtered text with "space" - DEC 32 in ascii
     * 
     * @param filteredText - text to be parsed 
     */
    public void parse(String filteredText) {
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
     * return hashmap freq containing words and frequency
     */
    public HashMap<String, Integer> getHashMap() {      
        return freq;
    }
    
    public void encode(String input) {
        
    }
}