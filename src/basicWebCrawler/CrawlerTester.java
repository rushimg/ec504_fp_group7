package basicWebCrawler;
import stringMatcher.FullFunctionMatching;

public class CrawlerTester {

		public static void main(String args[]){
			FullFunctionMatching test = new FullFunctionMatching();
			test.match("bababcabacab","ababa","");

			
			//System.out.println("Crawler start:");
			//Crawler myTestCrawler = new Crawler("http://www.bu.edu/",null);
			//Crawler myTestCrawler2 = new Crawler("http://algorithmics.bu.edu/twiki/bin/view/EC330_admin/HomeworkThreeDrop","algorithmics.bu.edu");
			System.out.println("Done.");
		}
}
