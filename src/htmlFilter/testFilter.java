package htmlFilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Tong Liu
 */
public class testFilter {
    //public static void main(String args[]) {
        String inputURL = "http://tomcat.bu.edu/ec512/";
        //System.out.println(getHTML("http://tomcat.bu.edu/ec512/"));
        String inputHTML = getHTML(inputURL);
        filter fl = new filter(inputHTML);
        String textFiltered = fl.filterToText();
        //System.out.println(textFiltered);
        fl.parse(textFiltered);
        fl.printHashMap();
    }
    
    /**get html text of specified url
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
