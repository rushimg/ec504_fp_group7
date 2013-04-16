package userInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import htmlFilter.Filter;


public class GUI {
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setModified(true);
		shell.setSize(1300, 700);
		shell.setText("EC504_Group7_P2P");
		shell.setLocation(0, 0);
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		
		final Browser browser = new Browser(shell, SWT.BORDER);
		browser.setBounds(236, 10, 1038, 642);
		//browser.setUrl("http://www.bu.edu");
		Filter filter = new Filter();
		browser.setText(filter.getHTML("http://www.bu.edu/ece"));
		
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
			}
		});
		selectButton.setBounds(134, 3, 80, 31);
		selectButton.setText("Search");
		
		StyledText styledText = new StyledText(composite, SWT.BORDER);
		styledText.setBounds(3, 40, 179, 80);
		styledText.setText("\n\nhttp://www.bu.edu/ece");
		
		StyledText styledText_1 = new StyledText(composite, SWT.BORDER);
		styledText_1.setBounds(3, 126, 179, 80);
		styledText_1.setText("\n\nhttp://www.amazon.com");
		
		StyledText styledText_2 = new StyledText(composite, SWT.BORDER);
		styledText_2.setBounds(3, 212, 179, 80);
		styledText_2.setText("\n\nhttp://github.com");
		
		StyledText styledText_3 = new StyledText(composite, SWT.BORDER);
		styledText_3.setBounds(3, 298, 179, 80);
		styledText_3.setText("\n\nTBD");
		
		StyledText styledText_4 = new StyledText(composite, SWT.BORDER);
		styledText_4.setBounds(3, 384, 179, 80);
		styledText_4.setText("\n\nTBD");
		
		StyledText styledText_5 = new StyledText(composite, SWT.BORDER);
		styledText_5.setBounds(3, 470, 179, 80);
		styledText_5.setText("\n\nTBD");
		
		StyledText styledText_6 = new StyledText(composite, SWT.BORDER);
		styledText_6.setBounds(3, 556, 179, 86);
		styledText_6.setText("\n\nTBD");
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("http://www.bu.edu/ece");
			}
		});
		btnNewButton_1.setBounds(186, 40, 28, 80);
		btnNewButton_1.setText(">>");
		
		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("http://www.amazon.com");
			}
		});
		button.setText(">>");
		button.setBounds(186, 126, 28, 80);
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("http://github.com");
			}
		});
		button_1.setText(">>");
		button_1.setBounds(186, 212, 28, 80);
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setText(">>");
		button_2.setVisible(false);
		button_2.setBounds(186, 298, 28, 80);
		
		Button button_3 = new Button(composite, SWT.NONE);
		button_3.setText(">>");
		button_3.setVisible(false);
		button_3.setBounds(186, 384, 28, 80);
		
		Button button_4 = new Button(composite, SWT.NONE);
		button_4.setText(">>");
		button_4.setVisible(false);
		button_4.setBounds(186, 470, 28, 80);
		
		Button button_5 = new Button(composite, SWT.NONE);
		button_5.setText(">>");
		button_5.setVisible(false);
		button_5.setBounds(186, 556, 28, 86);

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
