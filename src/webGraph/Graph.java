package webGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.util.Stack;
import basicWebCrawler.simpleDS;

//import stringMatcher.FullFunctionMatching;
//import customJavaFunctionality.Pair;
import customJavaFunctionality.Triple;

//This is where the magic happens
public class Graph {
	private ArrayList<URLnode> Map;		//web map
	private int currentIndex;	//index of the last added node
	//private FullFunctionMatching strmat;
	private HashMap<String,Integer> URLMap;		//maps a URL to an index - used for fast searching
	private HashMap<String,Integer> NameMap;	//maps a Name to an index - used for fast searching
	private ArrayList<Triple> PeerList;		//Links a peer to a sector
	
	private double LOWERBOUND = 0.80;	//minimum percentage per sector
	private double UPPERBOUND = 1.20;	//maximum percentage per sector	
	private ArrayList<Integer> NodesPerPeer;
	private int max_depth;
	
	/* Graph - constructor
	 * initializes Graph
	 */
	public Graph(){
		Map = new ArrayList<URLnode>();
		currentIndex = -1;
		//strmat = new FullFunctionMatching();
		URLMap = new HashMap<String, Integer>();
		NameMap = new HashMap<String,Integer>();
		PeerList = new ArrayList<Triple>();
		NodesPerPeer = new ArrayList<Integer>();
	}
	
	
/***************************INTERFACE OPERATIONS*****************************************/
//region interface

/** getNodeLists
 * called to add Nodes to the graph
 * 
 * @param newNodes - list of <simpleDS> of nodes
 * @note nothing is done with rawHTML at the moment.
 */
public void getNodeLists(ArrayList<simpleDS> newNodes){
	for(int ii = 0; ii < newNodes.size(); ii++){
		int index = addNode(newNodes.get(ii).getPageURL(), newNodes.get(ii).getPageTitle());
		Map.get(index).setText(newNodes.get(ii).getRawText());
		for(int jj = 0; jj < newNodes.get(ii).getLinksList().size(); jj++)
			addLink(index,newNodes.get(ii).getLinksList().get(jj));		
	}	
}


/** getLinksIn -> Interface with Data Structure
 * returns the number of links going into that node - used for page rank
 * 
 * @param index - index to the node
 * @return # of links going into the node
 */
public int getLinksIn(int index){
	if(index > Map.size()){
		System.out.println("Index is out of bounds!");
		return -1;
	}
	else
		return Map.get(index).getLinkedTo();
}
	
/** getNextNodeToIndex
 * 
 * @param peerName - name of the peer asking
 * @return next node. If -1 it could mean the peerName has not been found, or that all nodes have been searched
 */
public int getNextNodeToIndex(String peerName){
	return sectorSearch(peerName);	
}
	
//endregion interface	
	
/***************************SECTOR OPERATIONS********************************************/	
//region sector
	
	/** sectorSeach
	 * Perform BFS until a node is found, or all nodes have been searched
	 * 
	 * @param peerName - name of peer to search index for
	 * @return index to node to search
	 */
	public int sectorSearch(String peerName){
		int peerIndex = -1;
		for(int ii = 0; ii < PeerList.size(); ii++){
			if(PeerList.get(ii).first.equals(peerName)){
				peerIndex = ii;
				break;
			}
		}
		
		if(peerIndex == -1){
			System.out.println("Peer Name not found!");
			return -1;			
		}
		
		int sector = PeerList.get(peerIndex).second;
		Stack<Integer> ToSearch = new Stack<Integer>();
		for(int ii = 0; ii < PeerList.get(peerIndex).third.size(); ii++){
			ToSearch.push(PeerList.get(peerIndex).third.get(ii));
			while(ToSearch.size() > 0){
				int index = ToSearch.pop();
				if(Map.get(index).sector == sector){
					if(Map.get(index).getIndexed() == false){
						//Check among other peers if possible?
						Map.get(index).setIndexed(true);
						return index;						
					}
					else{
						for(int jj = 0; jj < Map.get(index).LinksTo.size(); jj++)
							ToSearch.push(Map.get(index).LinksTo.get(jj));
					}
				}
			}
		}
		return -1;	//no nodes found, all nodes searched
	}
	
	/** setDepth
	 * sets the depth of the nodes - where depth is the sum of the depths of nodes that have not been seen yet + 1
	 * 
	 * @param index - index to node
	 */
	public void setDepth(int index){
		Map.get(index).seen = true;
		for(int ii = 0; ii < Map.get(index).LinksTo.size(); ii++){
			int link = Map.get(index).LinksTo.get(ii);
			if(link != index){	//webpages have been known to be silly
				if(Map.get(link).seen == false){
					Map.get(link).seen = true;
					setDepth(link);
				}
				
			}
		}
		int depth = 1;
		for(int ii = 0; ii < Map.get(index).LinksTo.size();ii++){
			int link = Map.get(index).LinksTo.get(ii);
			if(link != index){
				if(Map.get(link).searched == false){
					depth += Map.get(link).depth;
					Map.get(link).searched = true;
				}					
			}
		}
		Map.get(index).depth = depth;		
	}
	
	
	/** resector
	 *  redistributes sectors among nodes based on number of peers
	 */
	//start at children of the root
	public void resector(){
		for(int ii = 0; ii < Map.size(); ii++){
			Map.get(ii).searched = false;
			Map.get(ii).seen = false;
		}
		if(Map.size() > 0){
			int peers = PeerList.size();
			int LinksPerPeer = Map.get(0).depth/peers;
			int max = (int)Math.ceil(LinksPerPeer*UPPERBOUND);
			int min = (int)Math.ceil(LinksPerPeer*LOWERBOUND);
			NodesPerPeer.clear();
			for(int ii = 0; ii < peers; ii++){
				NodesPerPeer.add(0);
				PeerList.get(ii).second = ii;
			}
			calcMaxDepth(max);
			sectorUp(0,LinksPerPeer,max,min);
			
		}
	}
	

	/** sectorUp
	 * 
	 * @param index - index to current node
	 * @param optimal - optimal depth/peer
	 * @param max - max possible depth/peer
	 * @param min - min possible depth/peer
	 */
	private void sectorUp(int index, int optimal, int max, int min){
		Map.get(index).seen = true;
		if(Map.get(index).depth > max_depth){
			for(int ii = 0; ii < Map.get(index).LinksTo.size(); ii++)
				sectorUp(Map.get(index).LinksTo.get(ii), optimal,max, min);
			AddToBestSector(index, max);		
		}
		else{
			int sector = findBestSector(Map.get(index).depth, max);
			PeerList.get(sector).third.add(index);
			setSector(index,sector);
			calcMaxDepth(max);
			
		}
	}
	
	
	/** setSector
	 * DFS to set the sector
	 * 
	 * @param index - index of node
	 * @param sector - sector of node
	 */
	private void setSector(int index, int sector){
		Map.get(index).seen = true;
		Map.get(index).sector = sector;
		NodesPerPeer.set(sector,NodesPerPeer.get(sector) + 1);
		for(int ii = 0; ii < Map.get(index).LinksTo.size(); ii++){
			int newIndex = Map.get(index).LinksTo.get(ii);
			if(Map.get(newIndex).seen == false)
				setSector(newIndex, sector);
		}	
	}
	
	
	/** findBestSector
	 * gets the best fit for the input
	 * 
	 * @param depth - depth of input
	 * @param max - max size for a peer
	 * @return - sector
	 */
	private int findBestSector(int depth, int max){
		int bestFit = max+10;
		int bestFitIndex = -1;
		for(int ii = 0; ii < NodesPerPeer.size(); ii++){
			int val = max - (NodesPerPeer.get(ii) + depth);
			if(bestFit > val && val >= 0){
				bestFit = val;
				bestFitIndex = ii;
			}
		}
		
		return bestFitIndex;		
	}
	
	
	/** AddToBestSector
	 * sets the sector of the node to be part of the sector that has the fewest members
	 * 
	 * @param index - index to node
	 * @param max - max size of a Peer
	 */
	private void AddToBestSector(int index, int max){
		boolean found = false;
		int min_index = 0;
		int min_value = max;
		for(int ii = 0; ii < NodesPerPeer.size(); ii++){
			int val = NodesPerPeer.get(ii);
			if(min_value > val && val + 1 <= max && val != 0){
				min_value = val;
				min_index = ii;
				found = true;
			}
		}
		if(found == false){
			for(int ii = 0; ii < NodesPerPeer.size();ii++){
				if(NodesPerPeer.get(ii) == 0){
					min_index = ii;
					min_value = 0;
					break;
				}
			}
		}		
		NodesPerPeer.set(min_index, min_value + 1);
		Map.get(index).sector = min_index;
		PeerList.get(min_index).third.add(index);	
		calcMaxDepth(max);
	}
	
	
	/** calcMaxDepth
	 * calculates the maximum depth that can be sustained by any Peer
	 * 
	 * @param - max size of Peer
	 */
	private void calcMaxDepth(int max){
		int maxDepth = 0;
		for(int ii = 0; ii < NodesPerPeer.size(); ii++)
			if(maxDepth < (max - NodesPerPeer.get(ii)))
				maxDepth = (max - NodesPerPeer.get(ii));
		max_depth = maxDepth;		
	}

	
//endregion sector	
	
/***************************NODE OPERATIONS**********************************************/	
//region node
	
	/** addNode
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
	public int addNode(String URL, String pageName, int HTMLindex){
		currentIndex++;
		URLnode tempNode = new URLnode(currentIndex);
		tempNode.setPageURL(URL);
		tempNode.setPageName(pageName);
		tempNode.setHTMLindex(HTMLindex);
		Map.add(tempNode);
		URLMap.put(URL, currentIndex);
		NameMap.put(pageName, currentIndex);
		return currentIndex;				
	}
	
	
	/** findNodeURL
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
	
	
	/** findNodeName
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
	
	
	/** updateNode
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
	public void updateNode(String URL,String parameter,String value){
		if(URLMap.get(URL) != null){
			int index = URLMap.get(URL);
			updateNode(index,parameter,value);			
		}
		else
			System.out.println("Graph Error::Cannot find URL! Node not updated.");		
		
	}
	public void updateNode(String URL,String parameter,int value){
		if(URLMap.get(URL) != null){
			int index = URLMap.get(URL);
			updateNode(index,parameter,value);			
		}
		else
			System.out.println("Graph Error::Cannot find URL! Node not updated.");		
		
	}
	public void updateNode(String URL,String parameter,boolean value){
		if(URLMap.get(URL) != null){
			int index = URLMap.get(URL);
			updateNode(index,parameter,value);			
		}
		else
			System.out.println("Graph Error::Cannot find URL! Node not updated.");		
		
	}
	
	
	/** addLink
	 * adds a connection from one node to another. Also increments the page rank of the other page
	 * 
	 * @param orignalNode - URL with the link
	 * @param linkedToNode - end point of the URL	 * 
	 */
	public void addLink(int originalNode, int linkedToNode){
		if(originalNode < 0 || originalNode > currentIndex || linkedToNode < 0 || linkedToNode > currentIndex)
			System.out.println("addLink Error::originalNode or linkedToNode is out of bounds! Link not added.");		
		else{
			Map.get(originalNode).addLink(linkedToNode);
			Map.get(linkedToNode).incLinkedTo();
		}
	}
	public void addLink(String URL_orig, int linkedToNode){
		if(URLMap.get(URL_orig) != null){
			int index = URLMap.get(URL_orig);
			addLink(index,linkedToNode);			
		}
		else
			System.out.println("addLink Error::Cannot find URL! Link not added.");				
	}
	public void addLink(int originalNode, String URL_linked){
		if(URLMap.get(URL_linked) != null){
			int index = URLMap.get(URL_linked);
			addLink(originalNode,index);			
		}
		else
			System.out.println("addLink Error::Cannot find URL! Link not added.");				
	}
	public void addLink(String URL_orig, String URL_linked){
		if(URLMap.get(URL_orig) != null && URLMap.get(URL_linked) != null){
			int index_orig = URLMap.get(URL_orig);
			int index_linked = URLMap.get(URL_linked);
			addLink(index_orig,index_linked);			
		}
		else
			System.out.println("addLink Error::Cannot find URL! Link not added.");				
	}
	
	
	
//endregion node
	
/***************************GRAPH OPERATIONS*********************************************/
//region graph
	/** printGraph
	 * prints out all the nodes of the graph to the console	 * 
	 */
	public void printGraph(){
		System.out.println("\n");
		for(int ii = 0; ii < Map.size(); ii++){
			System.out.println("Node #" + Map.get(ii).getNodeIndex() + "\t" + Map.get(ii).getPageName());
			System.out.println("URL = " + Map.get(ii).getPageURL());
			System.out.println("HTMLIndex = " + Map.get(ii).getHTMLindex() + "\t" + "linked to = " + Map.get(ii).getLinkedTo() + "\t" + "indexed = " + Map.get(ii).getIndexed());
			System.out.println("Searched = " + Map.get(ii).searched + "\tSeen = " + Map.get(ii).seen);
			System.out.println("Sector = " + Map.get(ii).sector + "\tDepth = " + Map.get(ii).depth);
			System.out.print("Links To = ");
			int size = Map.get(ii).LinksTo.size();
			if(size > 0){
				for(int jj = 0; jj < size-1; jj++)
					System.out.print(Map.get(ii).LinksTo.get(jj) + ", ");
				System.out.println(Map.get(ii).LinksTo.get(size-1));
			}
			System.out.println("\n");			
		}		
		
	}
	
	
	/** addPeer
	 * adds a Peer to the graph - should trigger resorting, but we're testing this stuff still
	 * 
	 * @param PeerName - name of the peer. Should be epic.
	 */
	public void addPeer(String PeerName){
		Triple newPeer = new Triple(PeerName,PeerList.size());
		PeerList.add(newPeer);		
	}
	
	
	
	
	
	
//endregion graph

/***************************DEPRECATED***************************************************/	
//region deprecated
	/** treeSearch - Deprecated
	 * searches the graph based on given parameters
	 * NOTE:: This is an expensive operation. It should be used as infrequently as possible
	 * 
	 * @param parameter - search by pageName or pageURL
	 * @param value - value assigned to parameter
	 * 
	 * @returns index of the node, or -1 if the node is not found
	 */
	@SuppressWarnings("unused")
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
	
	/** DFS_pageName
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
	
	
	
//endregion deprecated
	
}
