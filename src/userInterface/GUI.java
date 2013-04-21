package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import webGraph.Graph;
import webGraph.URLnode;
import dataStruct.StoreAndSearch;
import basicWebCrawler.Crawler;
import basicWebCrawler.simpleDS;
import htmlFilter.Filter;
import htmlFilter.Filter.Index;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;


public class GUI implements SelectionListener{
	private Text text;
	private Label resultCountLabel;
	private StyledText[] styledTextArray = new StyledText[7];
	private Button[] buttonsArray = new Button[7];
	private static String peerName;
	private static Graph net = new Graph();
	private static Filter filt = new Filter();
	private static StoreAndSearch indexDS = new StoreAndSearch();
	private static GUI gui = new GUI();
	private static ArrayList<Integer> resultList = new ArrayList<Integer>();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Crawler crawler = new Crawler();
			crawler.setPrintOutput(true);
			simpleDS tempDS = new simpleDS();
			int crawlerCount = 0;
			while (crawlerCount < 20) {		//test for only two nodes in this case, change it to "> 0" for full search
				crawler.startCrawling();
				tempDS = crawler.getCurrentDS();
				net.addNode(tempDS);
				crawlerCount++;
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
				//System.out.println(nextIndex);
				//nextIndex = net.getNextNodeToIndex(peerName);
				filt.parse(net.Map.get(nextIndex).getText());
				filt.storeInOrder();
				indexPriorityQueue = filt.getPriorityQueue();
				while (!indexPriorityQueue.isEmpty()) {
					Index newIndex = indexPriorityQueue.poll();
					indexDS.Store(newIndex.key, newIndex.frequency, nextIndex);
					if (newIndex.key.equals("time"))
						System.out.println("index debug: " + newIndex.key + " " + newIndex.frequency + " " + nextIndex) ;
				}
				nextIndex++;
			}			
			
			/**
			 * People who want to run and design GUI conveniently may want to install eclipse WindowsBuilder
			 *  plugin from: http://www.eclipse.org/windowbuilder/
			 */
			gui.open();
			//TODO: Interaction with back-end
			//TODO: parse input text with "AND" and "OR" and their implementation for sending different request and combining result
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == buttonsArray[2])
			System.out.println("right");
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		final Shell shell = new Shell(display, SWT.MIN);
		shell.setModified(true);
		shell.setSize(1300, 700);
		shell.setText("EC504_Group7_P2P");
		shell.setLocation(0, 0);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		
		final Browser browser = new Browser(shell, SWT.BORDER);
		browser.setBounds(236, 10, 1038, 642);
		browser.setUrl("http://www.bu.edu");
		Filter filter = new Filter();
		//browser.setText(filter.getHTML("http://www.bu.edu/ece"));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 216, 642);
		composite.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(3, 3, 129, 31);
		text.setFont(new Font(display, "Arial", 14, SWT.NORMAL));
		
		Button selectButton = new Button(composite, SWT.NONE);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String inputText = text.getText();
				if (inputText.length() == 0) {
					MessageDialog.openInformation(shell, "warning", "Please input something to search");
					return;
				}
				if (resultList != null)
					resultList.clear();
				resultList = indexDS.Search(inputText);
				System.out.println(resultList);
				if (resultList == null) {
					resultCountLabel.setText("0 results found");
					for (int i = 0; i < 7; i++) {
						styledTextArray[i].setText("");
					}
					return;
				}
				resultCountLabel.setText(resultList.size() + " results found");
				for (StyledText st: styledTextArray) {
					st.setText("");
				}
				for (int i = 0; i < 7; i++) {
					if (i < resultList.size()) {
						URLnode tempNode = net.Map.get(resultList.get(i));
						styledTextArray[i].setText(tempNode.getPageName() + "\n" + tempNode.getPageURL() + "\n");
					}
				}
			}
		});
		selectButton.setBounds(134, 3, 80, 31);
		selectButton.setText("Search");

		StyledText styledText_0 = new StyledText(composite, SWT.BORDER);
		styledText_0.setBounds(3, 70, 179, 78);
		styledTextArray[0] = styledText_0;
		
		StyledText styledText_1 = new StyledText(composite, SWT.BORDER);
		styledText_1.setBounds(3, 152, 179, 78);
		styledTextArray[1] = styledText_1;
		
		StyledText styledText_2 = new StyledText(composite, SWT.BORDER);
		styledText_2.setBounds(3, 234, 179, 78);
		styledTextArray[2] = styledText_2;
		
		StyledText styledText_3 = new StyledText(composite, SWT.BORDER);
		styledText_3.setBounds(3, 316, 179, 78);
		styledTextArray[3] = styledText_3;
		
		StyledText styledText_4 = new StyledText(composite, SWT.BORDER);
		styledText_4.setBounds(3, 398, 179, 78);
		styledTextArray[4] = styledText_4;
		
		StyledText styledText_5 = new StyledText(composite, SWT.BORDER);
		styledText_5.setBounds(3, 480, 179, 78);
		styledTextArray[5] = styledText_5;
		
		StyledText styledText_6 = new StyledText(composite, SWT.BORDER);
		styledText_6.setBounds(3, 562, 179, 78);
		styledTextArray[6] = styledText_6;
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				URLnode tempNode = net.Map.get(resultList.get(0));
				browser.setUrl(tempNode.getPageURL());
			}
		});
		btnNewButton_1.setBounds(186, 70, 28, 78);
		btnNewButton_1.setText(">>");
		buttonsArray[0] = btnNewButton_1;
		
		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("http://www.amazon.com");
			}
		});
		button.setText(">>");
		button.setBounds(186, 152, 28, 78);
		buttonsArray[1] = button;
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
		});
		button_1.setText(">>");
		button_1.setBounds(186, 234, 28, 78);
		buttonsArray[2] = button_1;
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setText(">>");
		button_2.setVisible(true);
		button_2.setBounds(186, 316, 28, 78);
		buttonsArray[3] = button_2;
		
		Button button_3 = new Button(composite, SWT.NONE);
		button_3.setText(">>");
		button_3.setVisible(true);
		button_3.setBounds(186, 398, 28, 78);
		buttonsArray[4] = button_3;
		
		Button button_4 = new Button(composite, SWT.NONE);
		button_4.setText(">>");
		button_4.setVisible(true);
		button_4.setBounds(186, 480, 28, 78);
		buttonsArray[5] = button_4;
		
		Button button_5 = new Button(composite, SWT.NONE);
		button_5.setText(">>");
		button_5.setVisible(true);
		button_5.setBounds(186, 562, 28, 78);
		buttonsArray[6] = button_5;
		
	    resultCountLabel = new Label(composite, SWT.NONE);
		resultCountLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		resultCountLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		resultCountLabel.setBounds(3, 40, 211, 24);
		resultCountLabel.setText("0 results found");
		resultCountLabel.setFont(new Font(display, "Arial", 14, SWT.NORMAL));

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
