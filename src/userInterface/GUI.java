/* Tong Liu - GUI */

package userInterface;

import java.awt.Window;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.text.BadLocationException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import webGraph.Graph;
import webGraph.URLnode;
import dataStruct.IndexStruct;
import dataStruct.StoreAndSearch;
import dataStruct.StoreIntoFile;
import basicWebCrawler.Crawler;
import basicWebCrawler.simpleDS;
import htmlFilter.Filter;
import htmlFilter.Filter.Index;
import org.eclipse.swt.widgets.Label;


public class GUI {
	//region vars
	private Text text;
	private Label resultCountLabel;
	private StyledText[] styledTextArray = new StyledText[6];
	private Button[] buttonsArray = new Button[6];
	private static int resultCount = 0;
	private static int maxPageNumber = 1;
	private static int pageNumber = 1;
	private static String peerName;
	private Graph net = new Graph();
	private static Filter filt = new Filter();
	private StoreAndSearch indexDS = new StoreAndSearch();
	private static GUI gui = new GUI();
	private static ArrayList<Integer> resultList = new ArrayList<Integer>();
	private static Display display;
	private static Shell shell;
	private static Label pageLabel;
	private static Button crawlButton;
	private static Button loadButton;
	private static Button searchButton;
	public ArrayList<simpleDS> GraphQueue = new ArrayList<simpleDS>();
	public ArrayList<Integer> FilterIndexQueue = new ArrayList<Integer>();
	private Graph graphNetGraph = new Graph();
	private String addressToConnect = "";
	private int portToConnect = 0;
	private boolean firstPeer = false;
	PriorityQueue<Index> indexPriorityQueue = new PriorityQueue<Index>();
	int graphSize = graphNetGraph.getIndexSize();
	StoreIntoFile tempStoreIntoFile = new StoreIntoFile();
	//endregion
	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * People who want to run and design GUI conveniently may want to
		 * install eclipse WindowsBuilder plugin from:
		 * http://www.eclipse.org/windowbuilder/
		 */
		gui.open();
		// TODO: parse input text with "AND" and "OR" and their implementation
		// for sending different request and combining result
		//crawlWebsites(50);
	}

	/**
	 * crawl websites in bu.edu domain
	 * 
	 * @param count
	 *            - maximum number of websites to crawl
	 */
	public void addNodeToGraph(simpleDS ds){
		graphNetGraph.addNode(ds);
	}
	
	public void addToFiltQueue(int ind){
		FilterIndexQueue.add(ind);
	}
	
	public int quickSearchTheGraph(){
		return graphNetGraph.quickSearch(peerName);
	}
	
	public simpleDS getOffQueue(){
		if(GraphQueue.size()!=0){
			simpleDS temp = GraphQueue.get(0);
			GraphQueue.remove(0);
			return temp;
		}
		return null;
	}
	
	public int getOffFiltQueue(){
		if(FilterIndexQueue.size() != 0){
			int ind = FilterIndexQueue.get(0);
			FilterIndexQueue.remove(0);
			return ind;
		}
		return -1;
	}
	
	public void DoFilterStuff(int index){
		filt.parse(graphNetGraph.Map.get(index).getText());
		filt.storeInOrder();
		indexPriorityQueue = filt.getPriorityQueue();
		while (!indexPriorityQueue.isEmpty()) {
			Index newIndex = indexPriorityQueue.poll();
			IndexStruct tempIndexStruct = new IndexStruct(newIndex.frequency, index);
			if (tempStoreIntoFile.myHashMap.get(newIndex.key) == null) {
				ArrayList<IndexStruct> tempArrayList = new ArrayList<IndexStruct>();
				tempArrayList.add(tempIndexStruct);
				tempStoreIntoFile.myHashMap.put(newIndex.key, tempArrayList);
			} else {
				ArrayList<IndexStruct> tempArrayList = tempStoreIntoFile.myHashMap.get(newIndex.key);
				tempArrayList.add(tempIndexStruct);
				tempStoreIntoFile.myHashMap.put(newIndex.key, tempArrayList);
			}
		}
		
		
		
	}
	
	public void crawlWebsites(int count) throws IOException, BadLocationException {
		Crawler crawler = new Crawler();
		crawler.setPrintOutput(true);
		
		//simpleDS tempDS = new simpleDS();
		peerName = "Hyperion";
		graphNetGraph.addPeer(peerName);
		
		

		
		
	//	Thread GraphThread = new Thread(new MagicThreading(graphNetGraph,peerName,GraphQueue,FilterIndexQueue,this));
	//	Thread FilterThread = new Thread(new FilterThreading(filt,graphNetGraph,peerName,FilterIndexQueue,indexPriorityQueue, tempStoreIntoFile,this));
	//	GraphThread.start();
	//	FilterThread.start();
		int crawlerCount = 0;
		while ((crawlerCount < count) || (crawler.getUrlQueue().size() == 0)) { 
			crawler.startCrawling();
			simpleDS tempDS = crawler.getCurrentDS();
			if (tempDS == null)
				break;
			DoFilterStuff(graphNetGraph.addNode(tempDS));
			crawlerCount++;
		}
		

		
		
		System.out.println("number of websites to grab in queue: "
				+ crawler.getUrlQueue().size());




		
	/*	
		while (nextIndex <= graphSize) {
			filt.parse(graphNetGraph.Map.get(nextIndex).getText());
			filt.storeInOrder();
			indexPriorityQueue = filt.getPriorityQueue();
			while (!indexPriorityQueue.isEmpty()) {
				Index newIndex = indexPriorityQueue.poll();
				IndexStruct tempIndexStruct = new IndexStruct(
						newIndex.frequency, nextIndex);
				if (tempStoreIntoFile.myHashMap.get(newIndex.key) == null) {
					ArrayList<IndexStruct> tempArrayList = new ArrayList<IndexStruct>();
					tempArrayList.add(tempIndexStruct);
					tempStoreIntoFile.myHashMap
							.put(newIndex.key, tempArrayList);
				} else {
					ArrayList<IndexStruct> tempArrayList = tempStoreIntoFile.myHashMap
							.get(newIndex.key);
					tempArrayList.add(tempIndexStruct);
					tempStoreIntoFile.myHashMap
							.put(newIndex.key, tempArrayList);
				}
			}
			nextIndex++;
		}
		*/
		
		ObjectOutputStream outStream = new ObjectOutputStream(
				new FileOutputStream("DS.txt"));
		outStream.writeObject(tempStoreIntoFile);
		outStream.close();
		ObjectOutputStream outStream1 = new ObjectOutputStream(
				new FileOutputStream("Graph.txt"));
		outStream1.writeObject(graphNetGraph);
		outStream1.close();
	}

	/**
	 * load graph and index data structure into memory
	 */
	public void loadGraphAndIndex() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(
				"DS.txt"));
		StoreIntoFile readFile = (StoreIntoFile) inStream.readObject();
		ObjectInputStream inStream1 = new ObjectInputStream(
				new FileInputStream("Graph.txt"));
		net = (Graph) inStream1.readObject();
		inStream.close();
		inStream1.close();

		Iterator<Map.Entry<String, ArrayList<IndexStruct>>> freqIter = readFile.myHashMap
				.entrySet().iterator();
		indexDS.clear();
		while (freqIter.hasNext()) {
			Map.Entry<String, ArrayList<IndexStruct>> tempEntry = freqIter
					.next();
			String tempWord = tempEntry.getKey();
			ArrayList<IndexStruct> tempArrayList = tempEntry.getValue();
			for (IndexStruct tempIndexStruct : tempArrayList) {
				indexDS.Store(tempWord, tempIndexStruct.frequency,
						tempIndexStruct.nodeIndex);
				// System.out.println(tempWord + " : " +
				// tempIndexStruct.frequency + " " + tempIndexStruct.nodeIndex);
			}
		}
	}

	/**
	 * update result according to current page number
	 */
	public void updatePage() {
		int beginIndex = (pageNumber - 1) * 6;
		int endIndex = beginIndex + 5;
		for (int i = beginIndex; i <= endIndex; i++) {
			if (i < resultList.size()) {
				URLnode tempNode = net.Map.get(resultList.get(i));
				StyleRange titleStyleRange = new StyleRange();
				titleStyleRange.start = 0;
				if (tempNode.getPageName() == null)
					continue;
				titleStyleRange.length = tempNode.getPageName().length();
				titleStyleRange.font = new Font(display, "Arial", 9, SWT.BOLD);
				titleStyleRange.underline = true;
				int styleTextIndex = i - beginIndex; // index of component
														// styleText;
				styledTextArray[styleTextIndex].setText(tempNode.getPageName()
						+ "\n" + tempNode.getPageURL() + "\n");
				StyleRange urlStyleRange = new StyleRange();
				urlStyleRange.start = tempNode.getPageName().length();
				urlStyleRange.length = tempNode.getPageURL().length();
				urlStyleRange.font = new Font(display, "Arial", 8, SWT.ITALIC);
				StyleRange[] allStyleRanges = new StyleRange[2];
				allStyleRanges[0] = titleStyleRange;
				allStyleRanges[1] = urlStyleRange;
				styledTextArray[styleTextIndex].setStyleRanges(allStyleRanges);
			} else {
				int styleTextIndex = i - beginIndex; // index of component
														// styleText;
				styledTextArray[styleTextIndex].setText("");
			}
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.MIN);
		shell.setModified(true);
		shell.setSize(1300, 720);
		shell.setText("EC504_Group7_P2P");
		shell.setLocation(0, 0);
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		shell.setImage(new Image(display, "icon.ico"));

		InputDialog initialDialog = new InputDialog(shell, "Join Network", "If you are the first peer, click \"cancel\", " +
				"otherwise Input the IP address and port to connect: ", "127.0.0.1:8080", null);
		if (initialDialog.open() == org.eclipse.jface.window.Window.CANCEL) {
			firstPeer = true;
		}
		else if (initialDialog.open() == org.eclipse.jface.window.Window.OK) {
			String initialString = "";
			initialString = initialDialog.getValue();
			int parseIndex = initialString.indexOf(':');
			if (parseIndex == -1) {
				MessageDialog.openInformation(shell, "wrong format", "please input in this format : \"address:port\"" +
						" like \"127.0.0.1:8080\"");
				return;
			}
			String tempAddress = initialString.substring(0, parseIndex);
			int tempPort = 0;
			try {
				tempPort = Integer.valueOf(initialString.substring(parseIndex + 1));
			} catch (NumberFormatException e) {
				MessageDialog.openInformation(shell, "wrong format", "please input in this format : \"address:port\"" +
						" like \"127.0.0.1:8080\"");
				return;
			}
			addressToConnect = tempAddress;
			portToConnect = tempPort;
			System.out.println("address to connect: " + addressToConnect + "; port to connect: " + portToConnect);
		}
		
		final Browser browser = new Browser(shell, SWT.BORDER);
		browser.setBounds(262, 10, 1025, 672);
		browser.setUrl("http://www.bu.edu");
		// Filter filter = new Filter();
		// browser.setText(filter.getHTML("http://www.bu.edu/ece"));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 243, 672);
		composite.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));

		text = new Text(composite, SWT.BORDER);
		text.setBounds(3, 3, 236, 31);
		text.setFont(new Font(display, "Arial", 14, SWT.NORMAL));

		searchButton = new Button(composite, SWT.NONE);
		searchButton.setEnabled(false);
		searchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String inputText = text.getText();
				if (inputText.length() == 0) {
					MessageDialog.openInformation(shell, "warning",
							"Please input something to search");
					return;
				}
				if (resultList != null)
					resultList.clear();
				resultList = indexDS.Search(inputText);
				System.out.println(resultList);
				if (resultList == null) {
					resultCount = 0;
					resultCountLabel.setText(" " + resultCount + " found");
					for (int i = 0; i < 6; i++) {
						styledTextArray[i].setText("");
					}
					return;
				}
				pageNumber = 1;
				pageLabel.setText(String.valueOf(pageNumber));
				resultCount = resultList.size();
				if (resultCount % 6 == 0) {
					maxPageNumber = resultCount / 6;
				} else {
					maxPageNumber = resultCount / 6 + 1;
				}
				resultCountLabel.setText(" " + resultCount + " found");
				for (StyledText st : styledTextArray) {
					st.setText("");
				}
				updatePage();
			}
		});
		searchButton.setBounds(6, 42, 75, 31);
		searchButton.setText("Search");

		StyledText styledText_0 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_0.setBounds(3, 109, 206, 84);
		styledTextArray[0] = styledText_0;

		StyledText styledText_1 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_1.setBounds(3, 197, 206, 84);
		styledTextArray[1] = styledText_1;

		StyledText styledText_2 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_2.setBounds(3, 285, 206, 84);
		styledTextArray[2] = styledText_2;

		StyledText styledText_3 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_3.setBounds(3, 373, 206, 84);
		styledTextArray[3] = styledText_3;

		StyledText styledText_4 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_4.setBounds(3, 461, 206, 84);
		styledTextArray[4] = styledText_4;

		StyledText styledText_5 = new StyledText(composite, SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		styledText_5.setBounds(3, 549, 206, 84);
		styledTextArray[5] = styledText_5;

		Button button_0 = new Button(composite, SWT.NONE);
		button_0.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_0.setBounds(215, 109, 24, 84);
		button_0.setText(">>");
		buttonsArray[0] = button_0;

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6 + 1;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_1.setText(">>");
		button_1.setBounds(215, 197, 24, 84);
		buttonsArray[1] = button_1;

		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6 + 2;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_2.setText(">>");
		button_2.setBounds(215, 285, 24, 84);
		buttonsArray[2] = button_2;

		Button button_3 = new Button(composite, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6 + 3;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_3.setText(">>");
		button_3.setVisible(true);
		button_3.setBounds(215, 373, 24, 84);
		buttonsArray[3] = button_3;

		Button button_4 = new Button(composite, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6 + 4;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_4.setText(">>");
		button_4.setVisible(true);
		button_4.setBounds(215, 461, 24, 84);
		buttonsArray[4] = button_4;

		Button button_5 = new Button(composite, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int currentIndex = (pageNumber - 1) * 6 + 5;
				if (currentIndex < resultCount) {
					URLnode tempNode = net.Map.get(resultList.get(currentIndex));
					browser.setUrl(tempNode.getPageURL());
				}
			}
		});
		button_5.setText(">>");
		button_5.setVisible(true);
		button_5.setBounds(215, 549, 24, 84);
		buttonsArray[5] = button_5;

		resultCountLabel = new Label(composite, SWT.RIGHT);
		resultCountLabel.setAlignment(SWT.LEFT);
		resultCountLabel.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		resultCountLabel.setBackground(display
				.getSystemColor(SWT.COLOR_DARK_BLUE));
		resultCountLabel.setBounds(3, 78, 117, 25);
		resultCountLabel.setText(" " + resultCount + " found");
		resultCountLabel.setFont(new Font(display, "Arial", 14, SWT.NORMAL));

		pageLabel = new Label(composite, SWT.CENTER);
		pageLabel.setBounds(96, 650, 45, 20);
		pageLabel.setFont(new Font(display, "Arial", 12, SWT.BOLD));
		pageLabel.setText(String.valueOf(pageNumber));

		Button leftPageButton = new Button(composite, SWT.CENTER);
		leftPageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pageNumber > 1) {
					pageNumber--;
					pageLabel.setText(String.valueOf(pageNumber));
					updatePage();
				}
			}
		});
		leftPageButton.setBounds(45, 647, 45, 25);
		leftPageButton.setText("<");
		leftPageButton.setFont(new Font(display, "Arial Unicode", 12,
				SWT.NORMAL));

		Button rightPageButton = new Button(composite, SWT.CENTER);
		rightPageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pageNumber < maxPageNumber) {
					pageNumber++;
					pageLabel.setText(String.valueOf(pageNumber));
					updatePage();
				}
			}
		});
		rightPageButton.setText(">");
		rightPageButton.setBounds(147, 647, 45, 25);
		rightPageButton.setFont(new Font(display, "Arial Unicode", 12,
				SWT.NORMAL));

		crawlButton = new Button(composite, SWT.NONE);
		crawlButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int countLimit = 0;
				if (MessageDialog
						.openConfirm(shell, "Crawling Confirm",
								"Crawling may take a long time, do you still want to continue crawling?")) {
					InputDialog inputDialog = new InputDialog(
							shell,
							"Crawling size request",
							"please input the maximum number of websites to crawl: ",
							"20", null);
					inputDialog.open();
					try {
						countLimit = Integer.valueOf(inputDialog.getValue());
					} catch (NumberFormatException nfe) {
						MessageDialog
								.openInformation(shell, "warning",
										"Your input has to be a positive integer, this operation failed.");
						return;
					}
					if (countLimit <= 0) {
						MessageDialog
								.openInformation(shell, "warning",
										"Your input has to be a positive integer, this operation failed.");
						return;
					}
				} else {
					return;
				}
				try {
					crawlWebsites(countLimit);
					MessageDialog
							.openInformation(shell, "Congratulations",
									"Crawling completed! You can 'Load' now to update original data.");
					loadButton.setEnabled(true);
				} catch (IOException | BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
		crawlButton.setText("Crawl");
		crawlButton.setBounds(84, 42, 75, 31);

		loadButton = new Button(composite, SWT.NONE);
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					loadGraphAndIndex();
					searchButton.setEnabled(true);
					loadButton.setEnabled(false);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		loadButton.setText("Load");
		loadButton.setBounds(162, 42, 75, 31);

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}