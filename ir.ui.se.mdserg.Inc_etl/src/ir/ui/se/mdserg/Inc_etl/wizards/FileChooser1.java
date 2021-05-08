package ir.ui.se.mdserg.Inc_etl.wizards;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
 
public class FileChooser1 extends Composite {
	Text mText;
	Button mButton;
	String title = "Title";
	public Button rButton;

 
	public FileChooser1(Composite parent) {
		super(parent, SWT.NULL); 
		createContent();
	}
 
	public void createContent() {

		GridLayout layout = new GridLayout(1,false);
		setLayout(layout);
	    Button mrButton = new Button(this, SWT.RADIO);
	    mrButton.setText("ECL");
	    mrButton.setSelection(true);
	    Button rEMFButton = new Button(this, SWT.RADIO);
	    rEMFButton.setText("EMF Compare");
	}
	
	public String getText() {
		return mText.getText();
	}

	public Text getTextControl() {
		return mText;		
	}
 
	public File getFile() {
		String text = mText.getText();
		if (text.length() == 0) return null;
		return new File(text);
	}
 
	public String getTitle() {
		return title;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
}
