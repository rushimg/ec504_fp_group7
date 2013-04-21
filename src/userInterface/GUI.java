/* Tong Liu - GUI */

package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javax.swing.JButton;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyleRange;
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

public class GUI{
	private Text text;
	private Label resultCountLabel;
	private StyledText[] styledTextArray = new StyledText[6];
	private Button[] buttonsArray = new Button[6];
	private static int resultCount = 0;
	private static int pageNumber = 1;
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
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display, SWT.MIN);
		shell.setModified(true);
		shell.setSize(1300, 700);
		shell.setText("EC504_Group7_P2P");
		shell.setLocation(0, 0);
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		shell.setImage(new Image(display, "icon.ico"));
		
		final Browser browser = new Browser(shell, SWT.BORDER);
		browser.setBounds(259, 10, 1015, 642);
		browser.setUrl("http://www.bu.edu");
		//Filter filter = new Filter();
		//browser.setText(filter.getHTML("http://www.bu.edu/ece"));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 243, 642);
		composite.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(3, 3, 236, 31);
		text.setFont(new Font(display, "Arial", 14, SWT.NORMAL));
		
		Button searchButton = new Button(composite, SWT.NONE);
		searchButton.addSelectionListener(new SelectionAdapter() {
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
					resultCount = 0;
					resultCountLabel.setText(" " + resultCount + " found");
					for (int i = 0; i < 6; i++) {
						styledTextArray[i].setText("");
					}
					return;
				}
				resultCount = resultList.size();
				resultCountLabel.setText(" " + resultCount + " found");
				for (StyledText st: styledTextArray) {
					st.setText("");
				}
				for (int i = 0; i < 6; i++) {
					if (i < resultList.size()) {
						URLnode tempNode = net.Map.get(resultList.get(i));
						StyleRange titleStyleRange = new StyleRange();
						titleStyleRange.start = 0;
						titleStyleRange.length = tempNode.getPageName().length();
						titleStyleRange.font = new Font(display, "Arial", 9, SWT.BOLD);
						titleStyleRange.underline = true;
						styledTextArray[i].setText(tempNode.getPageName() + "\n" + tempNode.getPageURL() + "\n");
						StyleRange urlStyleRange = new StyleRange();
						urlStyleRange.start = tempNode.getPageName().length();
						urlStyleRange.length = tempNode.getPageURL().length();
						urlStyleRange.fontStyle = SWT.ITALIC;
						StyleRange[] allStyleRanges = new StyleRange[2];
						allStyleRanges[0] = titleStyleRange;
						allStyleRanges[1] = urlStyleRange;
						styledTextArray[i].setStyleRanges(allStyleRanges);
					}
				}
			}
		});
		searchButton.setBounds(5, 42, 91, 31);
		searchButton.setText("Search");

		StyledText styledText_0 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_0.setBounds(3, 80, 206, 84);	
		styledTextArray[0] = styledText_0;
		
		StyledText styledText_1 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_1.setBounds(3, 168, 206, 84);
		styledTextArray[1] = styledText_1;
		
		StyledText styledText_2 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_2.setBounds(3, 256, 206, 84);
		styledTextArray[2] = styledText_2;
		
		StyledText styledText_3 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_3.setBounds(3, 344, 206, 84);
		styledTextArray[3] = styledText_3;
		
		StyledText styledText_4 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_4.setBounds(3, 432, 206, 84);
		styledTextArray[4] = styledText_4;
		
		StyledText styledText_5 = new StyledText(composite, SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		styledText_5.setBounds(3, 520, 206, 84);
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
		button_0.setBounds(215, 80, 24, 84);	
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
		button_1.setBounds(215, 168, 24, 84);
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
		button_2.setBounds(215, 256, 24, 84);
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
		button_3.setBounds(215, 344, 24, 84);
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
		button_4.setBounds(215, 432, 24, 84);
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
		button_5.setBounds(215, 520, 24, 84);
		buttonsArray[5] = button_5;
		
	    resultCountLabel = new Label(composite, SWT.RIGHT);
		resultCountLabel.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		resultCountLabel.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		resultCountLabel.setBounds(120, 44, 117, 25);
		resultCountLabel.setText(" " + resultCount + " found");
		resultCountLabel.setFont(new Font(display, "Arial", 14, SWT.NORMAL));
		
		Label pageLabel = new Label(composite, SWT.CENTER);
		pageLabel.setBounds(102, 620, 45, 20);
		pageLabel.setFont(new Font(display, "Arial", 12, SWT.NORMAL));
		pageLabel.setText(String.valueOf(pageNumber));
		
		Button leftPageButton = new Button(composite, SWT.CENTER);
		leftPageButton.setBounds(51, 617, 45, 25);
		leftPageButton.setText("<");
		leftPageButton.setFont(new Font(display, "Arial Unicode", 12, SWT.NORMAL));
		
		Button rightPageButton = new Button(composite, SWT.CENTER);
		rightPageButton.setText(">");
		rightPageButton.setBounds(153, 617, 45, 25);
		rightPageButton.setFont(new Font(display, "Arial Unicode", 12, SWT.NORMAL));

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
