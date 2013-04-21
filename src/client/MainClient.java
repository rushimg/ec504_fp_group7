package client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javax.swing.text.BadLocationException;
import basicWebCrawler.simpleDS;
import basicWebCrawler.Crawler;
import webGraph.Graph;
import htmlFilter.Filter;
import htmlFilter.Filter.Index;
import dataStruct.StoreAndSearch;
import userInterface.GUI;

public class MainClient {
	private static String peerName;
	private static Graph net = new Graph();
	private static Filter filt = new Filter();
	private static StoreAndSearch indexDS = new StoreAndSearch();
	private static GUI gui = new GUI();
	
	public static void main(String [] args) throws IOException, BadLocationException{
		/******Crawler Stuff
		Crawler crawl = new Crawler();
		crawl.setPrintOutput(true);
		crawl.startCrawling();
		/******************/
		Crawler crawler = new Crawler();
		crawler.setPrintOutput(true);
		simpleDS tempDS = new simpleDS();
		while (crawler.getUrlQueue().size() < 200) {		//test for only two nodes in this case, change it to "> 0" for full search
			crawler.startCrawling();
			tempDS = crawler.getCurrentDS();
			net.addNode(tempDS);
			//TODO: addLink() not fully test, ignore temporarily
			//TODO: Boring notice frequently: "addLink Error::Cannot find URL! Link not added."
		}
		
		/*****Crawler <-> Graph Interface ******/
			
		int nextIndex = 0;  //not -1 for initialization
		PriorityQueue<Index> indexPriorityQueue = new PriorityQueue<Index>();
		int graphSize = net.getIndexSize();
		
		//TODO: didn't use peerName here
		//TODO: didn't use getNextNodeToIndex() here, add another getIndexSize() in graph.java to test here
		while(nextIndex <= graphSize){
			System.out.println(nextIndex);
			//nextIndex = net.getNextNodeToIndex(peerName);
			filt.parse(net.Map.get(nextIndex).getText());
			filt.storeInOrder();
			indexPriorityQueue = filt.getPriorityQueue();
			while (!indexPriorityQueue.isEmpty()) {
				Index newIndex = indexPriorityQueue.poll();
				indexDS.Store(newIndex.key, newIndex.frequency, nextIndex);
			}
			nextIndex++;
		}
		
		ArrayList<Integer> testArrayList = indexDS.Search("medical");
		System.out.println(testArrayList);
		testArrayList = indexDS.Search("bu");
		System.out.println(testArrayList);
		
		/**
		 * People who want to run and design GUI conveniently may want to install eclipse WindowsBuilder
		 *  plugin from: http://www.eclipse.org/windowbuilder/
		 */
		gui.open();
		//TODO: Interaction with back-end
		//TODO: parse input text with "AND" and "OR" and their implementation for sending different request and combining result
	}
}
