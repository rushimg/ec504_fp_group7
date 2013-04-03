package htmlFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tong Liu
 */
public class TestFilter {
    public static void main(String args[]) throws UnsupportedEncodingException, IOException {
        //String inputURL = "http://algorithmics.bu.edu/twiki/bin/view/EC504";
        //String inputURL = "http://www.bu.edu/ece";
        String inputURL = "https://github.com";
        //System.out.println(getHTML(inputURL));
        String inputHTML = getHTML(inputURL);
        Filter fl = new Filter();
        String textFiltered = fl.filterToText(inputHTML);
        //System.out.println(textFiltered);
        //fl.parse(textFiltered);
        //fl.printHashMap();
        System.out.println("string size before compression: " + inputHTML.length());
        System.out.println(inputHTML);
        String encodeStr = fl.encode(inputHTML);
        System.out.println(encodeStr);
        System.out.println("string size after encoding: " + encodeStr.length());
        String decodeStr = fl.decode(encodeStr);
        System.out.println(decodeStr);
        System.out.println("string size after decoding: " + decodeStr.length());
    }
    
    /**get HTML text of specified url
     * 
     * @param urlStr - url to get HTML text
     * @return - HTML text
     */
    public static String getHTML(String urlStr) {
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
