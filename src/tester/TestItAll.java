package tester;
import webGraph.Graph;

public class TestItAll {
	
public static void main(String [] args){
	Graph TestGraph = new Graph();
	
	int indA = TestGraph.addNode("A", "A");
	int indB = TestGraph.addNode("B", "B");
	int indC = TestGraph.addNode("C", "C");
	int indD = TestGraph.addNode("D", "D");
	int indE = TestGraph.addNode("E", "E");
	int indF = TestGraph.addNode("F", "F");
	int indH = TestGraph.addNode("H", "H");
	int indK = TestGraph.addNode("K", "K");
	int indS = TestGraph.addNode("S", "S");
	int indM = TestGraph.addNode("M", "M");
	
	TestGraph.addLink(indA, indB);
	TestGraph.addLink(indA, indC);
	TestGraph.addLink(indA, indS);
	TestGraph.addLink(indB, indD);
	TestGraph.addLink(indB, indH);
	TestGraph.addLink(indC, indH);
	TestGraph.addLink(indS, indM);
	TestGraph.addLink(indD, indE);
	TestGraph.addLink(indD, indF);
	TestGraph.addLink(indH, indK);
	
	TestGraph.setDepth(indA);
	TestGraph.printGraph();
	System.out.println("\n\n\n");
	TestGraph.addPeer("Prometheus");
	TestGraph.addPeer("Zeus");
	TestGraph.addPeer("Poseidon");
	TestGraph.addPeer("Meep");
	TestGraph.resector();
	TestGraph.printGraph();
	System.out.println("Wowawewow");
}
	
	
	
	

}
