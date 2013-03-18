package webGraph;
import webGraph.URLnode;
import java.util.ArrayList;

//This is where the magic happens
public class Graph {
	private ArrayList<URLnode> Map;		//web map
	private int currentIndex;	//index of the last added node
	
	/* Graph - constructor
	 * initializes Graph
	 */
	Graph(){
		Map = new ArrayList<URLnode>();
		currentIndex = -1;
		
	}
	
	
	/* addNode
	 * creates a node and adds it to the Map
	 * Then sets the URL and pageName and returns the index to that node
	 */
	public int addNode(String URL, String pageName){
		currentIndex++;
		URLnode tempNode = new URLnode(currentIndex);
		tempNode.setPageURL(URL);
		tempNode.setPageName(pageName);
		Map.add(tempNode);
		return currentIndex;
	}
	
	/* updateNode
	 * updates a node given an index and a parameter name - will probably be used for HTMLindex and indexed only
	 * 
	 * @param index - index of the node
	 * @param parameter - the parameter to be changed. Accepted values: NodeIndex, pageName, pageURL, HTMLindex, linkedTo, indexed
	 * @param value - value to update to
	 */
	public void updateNode(int index, String parameter, String value){
		if(index < 0 || index > currentIndex)
			System.out.println("Graph Error::Index is out of bounds! Not not updated.");
		else{
			if(parameter.equals("NodeIndex") || parameter.equals("HTMLindex") || parameter.equals("linkedTo") || parameter.equals("indexed"))
				System.out.println("Graph Error::Invalid value argument for parameter! Node not updated.");
			else if(parameter.equals("pageName"))
				Map.get(index).setPageName(value);
			else if(parameter.equals("pageURL"))
				Map.get(index).setPageURL(value);
			//NOT DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
