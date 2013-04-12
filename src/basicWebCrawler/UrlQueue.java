package basicWebCrawler;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class UrlQueue {
	
	// class created by Rushi Ganmukhi 5/23/13
	// need to ensure uniqness in queue of urls.
	private final Set<String> checker = new HashSet<String>();
	private Queue<String> queue = new LinkedList<String>(); 
	
	
	public UrlQueue(){
	}
	
	public void enque(String inputURL){
		if (!checker.contains(inputURL)){
			checker.add(inputURL);
			queue.add(inputURL);
		}
	}
	
	public String deque(){
		return queue.remove();
	}
	
	public int size(){
		return queue.size();
	}
}
