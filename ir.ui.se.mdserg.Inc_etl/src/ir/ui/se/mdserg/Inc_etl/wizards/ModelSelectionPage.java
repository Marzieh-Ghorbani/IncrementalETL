package ir.ui.se.mdserg.Inc_etl.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import ir.ui.se.mdserg.Inc_etl.wizards.FileChooser;

public class ModelSelectionPage extends WizardPage {

	 public String path;
	static Composite container;
	static Group InMSection;
	static Group UMSection;
	static Group TMSection;
	public static FileChooser InitialModelFC;
	public static FileChooser UpdateModelFC;
	public static FileChooser TargetModelFC;
	public IProject selectedProject;
	
	static String shell;

	public ModelSelectionPage() {
		// TODO Auto-generated constructor stub
		super("ModelSelectionPage");
		setTitle("Model Selection");
		setDescription("Please select input models");

	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		container = new Composite(parent, SWT.NULL);
		setPageComplete(true); // button finish disenable mishavsd
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(data);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		InMSection = new Group(container, SWT.NULL);
		InMSection.setText("Select Initial Version of Source Model");
		InMSection.setLayout(new GridLayout());
		InMSection.setLayoutData(data);
		InitialModelFC = new FileChooser(InMSection);
		InitialModelFC.Extensions = "*.xmi";
		InitialModelFC.ExtensionsName = "Model";
		GridData IMgridData = new GridData(GridData.FILL_HORIZONTAL);
		InitialModelFC.setLayoutData(IMgridData);

		UMSection = new Group(container, SWT.NULL);
		UMSection.setText("Select Updated Version of Source Model");
		UMSection.setLayout(new GridLayout());
		UMSection.setLayoutData(data);
		UpdateModelFC = new FileChooser(UMSection);
		UpdateModelFC.Extensions = "*.xmi";
		UpdateModelFC.ExtensionsName = "Model";
		GridData VMgridData = new GridData(GridData.FILL_HORIZONTAL);
		UpdateModelFC.setLayoutData(VMgridData);

		TMSection = new Group(container, SWT.NULL);
		TMSection.setText("Select Initial Version of Target Model");
		TMSection.setLayout(new GridLayout()); // show TextBox
		TMSection.setLayoutData(data);
		TargetModelFC = new FileChooser(TMSection);
		TargetModelFC.Extensions = "*.xmi";
		TargetModelFC.ExtensionsName = "Model";
		GridData TgridData = new GridData(GridData.FILL_HORIZONTAL);
		TargetModelFC.setLayoutData(TgridData);
		setControl(container);

	}

	public void setSelectedProject(IProject project) {
		this.selectedProject = project;
	}
	
	
	public void onLoad(IProject selectedPrj)
	{
		this.selectedProject = selectedPrj ;
		this.path = selectedPrj.getLocation().toString() ; 

		InitialModelFC.directory = this.path ; 
		UpdateModelFC.directory = this.path ; 
		TargetModelFC.directory = this.path ; 
		//EclScriptFC.directory = this.path ; 
	}
	
	
	
	
	

	/** @override */

	@Override
	public IWizardPage getNextPage() {
		boolean isNextPressed = "nextPressed"
				.equalsIgnoreCase(Thread.currentThread().getStackTrace()[2].getMethodName());
		if (isNextPressed) {
			boolean validatedNextPress = this.nextPressed();
			if (!validatedNextPress) {
				return this;
			}
		}
		return super.getNextPage();
	}

	/**
	 * @see WizardDialog#nextPressed()
	 * @see WizardPage#getNextPage()
	 */
	protected boolean nextPressed() {

		if ((ModelSelectionPage.InitialModelFC.path == null) || ModelSelectionPage.UpdateModelFC.path == null
				|| ModelSelectionPage.TargetModelFC == null) {
			this.setErrorMessage("You must select all the required files ...");
			this.getErrorMessage();
			return false;
		} else

			return true;
	}

}
