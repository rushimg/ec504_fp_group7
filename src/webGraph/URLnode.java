package webGraph;
import java.util.ArrayList;

/* URLnode
 * Defines the information stored in the node of the graph
 * 
 */
public class URLnode {
	private int NodeIndex;	//self-reference of node to its position in the full graph list
	private String pageName;	//name of the page stored in the node - identified by information contained between <title> ... </title> tags
	private String pageURL; 	//URL of the page stored in the node
	private int HTMLindex;	//index to list of HTML text. If the link is clicked it will send the index to the main interface which will grab and decode the HTML code and load it into the browser
	private int linkedTo;	//number of links to the page
	private boolean indexed;	//has this node been indexed by a peer
	
	public boolean searched;	//used to determine if node has been searched
	public boolean seen;
	public int sector;			//sector that the node belongs to.
	public ArrayList<Integer> LinksTo;	//List of indices pointed to by this node
	public int depth;
	
	/* URLnode - constructor
	 * Constructs a URLnode. Minimum info needed is a NodeIndex.
	 */
	URLnode(int newNodeIndex){
		if(newNodeIndex > -1)
			NodeIndex = newNodeIndex;
		else{
			System.out.println("URLnode Error::Node Index is below 0! Setting index to -1.");
			NodeIndex = -1;
		}
		LinksTo = new ArrayList<Integer>();		
		indexed = false;
		searched = false;
		seen = false;
		sector = 0;
		depth = 0;
	}
	
	/* setNodeIndex
	 * Setter for NodeIndex
	 */
	public void setNodeIndex(int newNodeIndex){
		if(newNodeIndex > -1)
			NodeIndex = newNodeIndex;
		else{
			System.out.println("URLnode Error::Node Index is below 0! Setting index to -1.");
			NodeIndex = -1;
		}
	}
	
	/* getNodeIndex
	 * getter for Node Index 
	 */
	public int getNodeIndex(){
		return NodeIndex;
	}
	
	/* setPageName
	 * set the page name
	 */
	public void setPageName(String newPageName){
		pageName = newPageName;
	}
	
	/* getPageName
	 * get the page name 
	 */
	public String getPageName(){
		return pageName;
	}
	
	/* setPageURL
	 * set the page name
	 */
	public void setPageURL(String newPageURL){
		pageURL = newPageURL;
	}
	
	/* getPageURL
	 * get the page name 
	 */
	public String getPageURL(){
		return pageURL;
	}	
	
	/* setHTMLindex
	 * set the HTML index assuming it is greater than -1
	 */
	public void setHTMLindex(int newHTMLindex){
		if(newHTMLindex > -1)
			HTMLindex = newHTMLindex;
		else{
			System.out.println("URLnode Error::HTML Index is below 0! Setting index to -1.");
			HTMLindex = -1;
		}
	}
	
	/* getHTMLindex
	 *  get HTMLindex
	 */
	public int getHTMLindex(){
		return HTMLindex;
	}
	
	/* setLinkedTo
	 * set linkedTo assuming it is greater than -1
	 */
	public void setLinkedTo(int newLinkedTo){
		if(newLinkedTo > -1)
			linkedTo = newLinkedTo;
		else{
			System.out.println("URLnode Error::linkedTo parameter is below 0! Setting index to -1.");
			linkedTo = -1;
		}
	}
	
	/* getLinkedTo
	 *  get LinkedTo
	 */
	public int getLinkedTo(){
		return linkedTo;
	}	
	
	/*incLinkedTo
	 * increments linked to by 1
	 */
	public void incLinkedTo(){
		linkedTo++;
	}
	
	/* setIndexed
	 * set the indexed bit to show the node has been indexed by a peer
	 */
	public void setIndexed(boolean newIndexed){
		indexed = newIndexed;
	}
	
	/* getIndexed
	 * get the indexed bit
	 */
	public boolean getIndexed(){
		return indexed;
	}

	/* addLink
	 * add an indexed to another node
	 */
	public void addLink(int newNode){
		if(newNode > -1)
			LinksTo.add(newNode);
		else
			System.out.println("URLnode Error::newNode index is below 0! Not adding a node.");
	}






}
