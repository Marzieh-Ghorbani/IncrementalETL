package ir.ui.se.mdserg.Inc_etl.wizards;

import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ir.ui.se.mdserg.Inc_etl.helper.EtlStandaloneExample;
import ir.ui.se.mdserg.Inc_etl.wizards.DataHolder;
import ir.ui.se.mdserg.Inc_etl.wizards.FinalPage;
//import ir.ui.se.mdserg.e3mp.wizards.FileChooser;
//import wizards.DataTransferObject;
import ir.ui.se.mdserg.Inc_etl.helper.CL_MergeModel1;
import ir.ui.se.mdserg.Inc_etl.helper.CL_MergeModel2;
import ir.ui.se.mdserg.Inc_etl.helper.CL_MergeModel3;

public class MetaModelSelectionPage extends WizardPage {

	 public String path;
	private static Composite container;
	private static Group SMMSection;
	private static Group TMMSection;
	private static Group EtlSection;
	public static FileChooser EtlScriptFC;
	private static Group grpModelProperties;
	public static FileChooser SourceMetaModelFC;
	public static FileChooser TargetMetaModelFC;
	private TableViewer tableViewer;
	public static FileChooser1 radio;
	public IProject selectedProject;
	
	static String shell;
	String Source_metamodel;
	String Target_metamodel;
	String etlpath;
	String basetarget;
	long TotalTime;
	String endTimes;
	DataHolder dt;
	FinalPage finalPage;


	ModelSelectionPage modelselect = new ModelSelectionPage();
	EtlStandaloneExample etlstandalon = new EtlStandaloneExample();
	CL_MergeModel1 merge1 = new CL_MergeModel1();
	CL_MergeModel2 merge2 = new CL_MergeModel2();
	CL_MergeModel3 merge3 = new CL_MergeModel3();

	public MetaModelSelectionPage(FinalPage fp, DataHolder dt) {
		// TODO Auto-generated constructor stub
		super("MetaModelSelectionPage");
		setTitle("Requirement Selection");
		this.dt = dt;
		this.finalPage = fp;
		setMessage("Please select meta-models and required files");

	}

	@Override
	public void createControl(Composite parent) {

		// TODO Auto-generated method stub
		container = new Composite(parent, SWT.NULL);
		// setPageComplete(false); // button finish disenable mishavsd
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(data);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		SMMSection = new Group(container, SWT.NULL);
		SMMSection.setText("Select Source Meta-Model");
		SMMSection.setLayout(new GridLayout());
		SMMSection.setLayoutData(data);
		SourceMetaModelFC = new FileChooser(SMMSection);
		SourceMetaModelFC.Extensions = "*.ecore";
		SourceMetaModelFC.ExtensionsName = "Ecore";
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		SourceMetaModelFC.setLayoutData(gridData);

		TMMSection = new Group(container, SWT.NULL);
		TMMSection.setText("Select Target Meta-Model");
		TMMSection.setLayout(new GridLayout());
		TMMSection.setLayoutData(data);
		TargetMetaModelFC = new FileChooser(TMMSection);
		TargetMetaModelFC.Extensions = "*.ecore";
		TargetMetaModelFC.ExtensionsName = "Ecore";
		GridData TargetgridData = new GridData(GridData.FILL_HORIZONTAL);
		TargetMetaModelFC.setLayoutData(TargetgridData);

		EtlSection = new Group(container, SWT.NULL);
		EtlSection.setText("Select ETL File");
		EtlSection.setLayout(new GridLayout());
		EtlSection.setLayoutData(data);
		EtlScriptFC = new FileChooser(EtlSection);
		EtlScriptFC.Extensions = "*.etl";
		EtlScriptFC.ExtensionsName = "EtL Script";
		GridData EtlgridData = new GridData(GridData.FILL_HORIZONTAL);
		EtlScriptFC.setLayoutData(EtlgridData);
		setControl(container);

		// creat radio button
		grpModelProperties = new Group(container, SWT.NULL);
		GridData rddata = new GridData(GridData.FILL_BOTH);
		grpModelProperties.setText("Select Comparison Strategy");
		grpModelProperties.setLayout(new GridLayout());
		grpModelProperties.setLayoutData(rddata);
		radio = new FileChooser1(grpModelProperties);
		GridData radioData = new GridData(GridData.FILL_BOTH);
		radio.setLayoutData(radioData);
	}
	
	
	public void onLoad(IProject selectedPrj)
	{
		this.selectedProject = selectedPrj ;
		this.path = selectedPrj.getLocation().toString() ; 
 
		SourceMetaModelFC.directory = this.path ;
		TargetMetaModelFC.directory = this.path ; 
		EtlScriptFC.directory = this.path ; 
		//Version2ModelFC.directory = this.path ; 
	}


	@Override
	public IWizardPage getNextPage() {
		boolean isNextPressed = "nextPressed"
				.equalsIgnoreCase(Thread.currentThread().getStackTrace()[2].getMethodName());
		if (isNextPressed) {
			boolean validatedNextPress = false;
			try {

				validatedNextPress = this.nextPressed();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!validatedNextPress) {
				return this;
			}
		}

		return super.getNextPage();
	}

	protected boolean nextPressed() throws URISyntaxException, Exception {

		if ((SourceMetaModelFC.path == null) || TargetMetaModelFC.path == null || EtlScriptFC == null) {
			this.setErrorMessage("You must select all the required files ...");
			this.getErrorMessage();
			return false;
		}

		else {

			String InitialModel = ModelSelectionPage.InitialModelFC.path;
			String updatModel = ModelSelectionPage.UpdateModelFC.path;
			basetarget = modelselect.TargetModelFC.path;
			Source_metamodel = SourceMetaModelFC.path;
			Target_metamodel = TargetMetaModelFC.path;
			etlpath = EtlScriptFC.path;

			long Add_DeletTime = etlstandalon.CreateAddTarget(InitialModel, updatModel, basetarget, Source_metamodel,
					Target_metamodel, etlpath);
			long elapsedTime1 = merge1.CreateTempraryModel(basetarget, Target_metamodel);
			long elapsedTime2 = merge2.DeleteElementsModel(basetarget, Target_metamodel);
			long elapsedTime3 = merge3.UpdateInitialTarget(basetarget, Target_metamodel);
			long TotalTime = Add_DeletTime + elapsedTime1 + elapsedTime2 + elapsedTime3;
			String endTimes = String.valueOf(TotalTime);
			dt.path = basetarget;
			dt.Time = endTimes; 
		}
		
		finalPage.updateTable();
		return true;
	}

}
