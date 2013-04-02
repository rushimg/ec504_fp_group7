package tester;
import webGraph.Graph;

public class TestItAll {
	
public static void main(String [] args){
	Graph TestGraph = new Graph();
	
	int ind1 = TestGraph.addNode("www.boop.edu", "home");
	int ind2 = TestGraph.addNode("www.boop.edu/soup", "soup");
	int ind3 = TestGraph.addNode("www.boop.edu/droop", "droop");
	int ind4 = TestGraph.addNode("www.boop.edu/soup/bloop", "bloop");
	int ind5 = TestGraph.addNode("www.boop.edu/loop", "loop");
	
	TestGraph.addLink(ind1, ind2);
	TestGraph.addLink(ind1, ind3);
	TestGraph.addLink(ind1, ind5);
	TestGraph.addLink(ind2, ind4);
	TestGraph.addLink(ind3, ind4);
	TestGraph.addLink(ind4, ind1);
	
	TestGraph.setDepth(ind1);
	TestGraph.printGraph();
	System.out.println("Wowawewow");
}
	
	
	
	

}
