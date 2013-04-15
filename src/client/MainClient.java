package client;
import java.util.PriorityQueue;

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
	private static StoreAndSearch DS = new StoreAndSearch();
	private static GUI gui = new GUI();
	
	public static void main(String [] args){
		/******Crawler Stuff
		Crawler crawl = new Crawler();
		crawl.setPrintOutput(true);
		crawl.startCrawling();
		/******************/
		
		
		/*****Crawler <-> Graph Interface ******/
			
			
		int nextIndex = 1;
		PriorityQueue<Index> indexPriorityQueue = new PriorityQueue<Index>();
		
		while(nextIndex != -1){
			nextIndex = net.getNextNodeToIndex(peerName);
			filt.parse(net.Map.get(nextIndex).getText());
			filt.storeInOrder();
			indexPriorityQueue = filt.getPriorityQueue();
			while (!indexPriorityQueue.isEmpty()) {
				Index newIndex = indexPriorityQueue.poll();
				//DS.Store(stuff(ii).string, stuff(ii).Freq, nextIndex);
			}			
		}
		
		gui.open();
		//gui needs method for send request
	
	}
}
