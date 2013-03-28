package webGraph;
import stringMatcher.FullFunctionMatching;
import customJavaFunctionality.Pair;
import java.util.HashMap;
import webGraph.URLnode;
import java.util.ArrayList;

//This is where the magic happens
public class Graph {
	private ArrayList<URLnode> Map;		//web map
	private int currentIndex;	//index of the last added node
	private FullFunctionMatching strmat;
	private HashMap<String,Integer> URLMap;		//maps a URL to an index - used for fast searching
	private HashMap<String,Integer> NameMap;	//maps a Name to an index - used for fast searching
	/* Graph - constructor
	 * initializes Graph
	 */
	Graph(){
		Map = new ArrayList<URLnode>();
		currentIndex = -1;
		strmat = new FullFunctionMatching();
		URLMap = new HashMap<String, Integer>();
		NameMap = new HashMap<String,Integer>();
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
		URLMap.put(URL, currentIndex);
		NameMap.put(pageName, currentIndex);
		return currentIndex;
	}
	
	/* findNodeURL
	 * attempts to find a node based on a URL
	 * 
	 * @param URL - the URL of the node
	 * @returns - returns the index to the node if it exists. If it doesn't returns -1
	 */
	public int findNodeURL(String URL){
		if(URLMap.get(URL) == null)
			return -1;
		else
			return URLMap.get(URL);		
	}
	
	/* findNodeName
	 * attempts to find a node based on a page name
	 * 
	 * @param name - the name of the page
	 * @returns - returns the index to the node if it exists. If it doesn't returns -1
	 */
	public int findNodeName(String name){
		if(NameMap.get(name) == null)
			return -1;
		else
			return NameMap.get(name);		
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
			else
				System.out.println("Graph Error::Invalid parameter! Node not updated.");
		}
	}
	public void updateNode(int index, String parameter, int value){
		if(index < 0 || index > currentIndex)
			System.out.println("Graph Error::Index is out of bounds! Not not updated.");
		else{
			if(parameter.equals("pageURL") || parameter.equals("pageName") || parameter.equals("indexed"))
				System.out.println("Graph Error::Invalid value argument for parameter! Node not updated.");
			else if(parameter.equals("linkedTo"))
				Map.get(index).setLinkedTo(value);
			else if(parameter.equals("HTMLindex"))
				Map.get(index).setHTMLindex(value);
			else if(parameter.equals("NodeIndex"))
				Map.get(index).setNodeIndex(value);
			else
				System.out.println("Graph Error::Invalid parameter! Node not updated.");
		}
	}
	public void updateNode(int index, String parameter, boolean value){
		if(index < 0 || index > currentIndex)
			System.out.println("Graph Error::Index is out of bounds! Not not updated.");
		else{
			if(parameter.equals("NodeIndex") || parameter.equals("HTMLindex") || parameter.equals("linkedTo") || parameter.equals("pageName") || parameter.equals("pageURL"))
				System.out.println("Graph Error::Invalid value argument for parameter! Node not updated.");
			else if(parameter.equals("indexed"))
				Map.get(index).setIndexed(value);
			else
				System.out.println("Graph Error::Invalid parameter! Node not updated.");
		}
	}
	
	/* addLink
	 * adds a connection from one node to another. Also increments the page rank of the other page
	 * 
	 * @param orignalNode - URL with the link
	 * @param linkedToNode - end point of the URL	 * 
	 */
	public void addLink(int originalNode, int linkedToNode){
		if(originalNode < 0 || originalNode > currentIndex || linkedToNode < 0 || linkedToNode > currentIndex)
			System.out.println("Graph Error::originalNode or linkedToNode is out of bounds! Link not added.");		
		else{
			Map.get(originalNode).addLink(linkedToNode);
			Map.get(linkedToNode).incLinkedTo();
		}
	}
	
	
	
	/* treeSearch
	 * searches the graph based on given parameters
	 * NOTE:: This is an expensive operation. It should be used as infrequently as possible
	 * 
	 * @param parameter - search by pageName or pageURL
	 * @param value - value assigned to parameter
	 * 
	 * @returns index of the node, or -1 if the node is not found
	 */
	private int treeSearch(String parameter, String value){
		if(parameter.equals("pageName")){	//worst possible search
			int index = DFS_pageName(0,value);	//root is assumed to be at 0
			return index;			
		}
		else if(parameter.equals("pageURL")){	//bit better, but more complicated
			//Screw it - I'll finish this if we actually need it.
			return -1;
		}
		else{
			System.out.println("treeSearch Error::Invalid parameter! Returning -1.");
			return -1;
		}
	}
	
	/* DFS_pageName
	 * Performs a depth-first search of the tree, searching by pageName
	 * Will probably take a bajillion years
	 * 
	 * @param node - index to current node being searched
	 * @param pageName - pageName to be matched
	 */
	private int DFS_pageName(int node, String pageName){
		Map.get(node).searched = true;
		if(Map.get(node).getPageName().equals(pageName) == true)
			return node;
		else{
			for(int ii = 0; ii < Map.get(node).LinksTo.size();ii++){
				int nextNode = Map.get(Map.get(node).LinksTo.get(ii)).getNodeIndex();
				int nodeVal = -1;
				if(Map.get(nextNode).searched == false){
					nodeVal = DFS_pageName(nextNode,pageName);
					if(nodeVal != -1)
						return nodeVal;
				}
			}
			return -1;
		}		
	}
	
	
	
	
	
}
