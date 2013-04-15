package client;
import java.io.IOException;
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
	private static StoreAndSearch DS = new StoreAndSearch();
	private static GUI gui = new GUI();
	
	public static void main(String [] args) throws IOException, BadLocationException{
		/******Crawler Stuff
		Crawler crawl = new Crawler();
		crawl.setPrintOutput(true);
		crawl.startCrawling();
		/******************/
		Crawler crawler = new Crawler();
		crawler.setPrintOutput(true);
		simpleDS tempDs = new simpleDS();
		while (crawler.getUrlQueue().size() > 0) {
			crawler.startCrawling();
			tempDs = crawler.getCurrentDS();
			//TODO: tempDs(simpleDS) ---> URLnode ---> Graph
			// System.out.println(crawler.getCurrentDS().getRawText());
		}
		
		/*****Crawler <-> Graph Interface ******/
			
		int nextIndex = -1;
		PriorityQueue<Index> indexPriorityQueue = new PriorityQueue<Index>();
		StoreAndSearch indexDS = new StoreAndSearch();
		
		while(nextIndex != -1){
			nextIndex = net.getNextNodeToIndex(peerName);
			filt.parse(net.Map.get(nextIndex).getText());
			filt.storeInOrder();
			indexPriorityQueue = filt.getPriorityQueue();
			while (!indexPriorityQueue.isEmpty()) {
				Index newIndex = indexPriorityQueue.poll();
				indexDS.Store(newIndex.key, newIndex.frequency, nextIndex);
			}			
		}
		
		gui.open();
		//TODO:gui needs method for send request
	}
}
