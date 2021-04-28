package ir.ui.se.mdserg.Inc_etl.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ir.ui.se.mdserg.Inc_etl.wizards.DataHolder;
//import ir.ui.se.mdserg.Inc_etl.wizards.ProjectSelectionWizardPage;

public class Wizardpage extends Wizard {

	public IProject selectedProject ; 
	
	private static Shell shell;
	DataHolder dt = new DataHolder();
	//ProjectSelectionWizardPage projectSelectionPage ; 
	ModelSelectionPage modelselectionPage = new ModelSelectionPage();
	FinalPage finalpage = new FinalPage(dt);
	MetaModelSelectionPage metamodelSelectionPage = new MetaModelSelectionPage(finalpage , dt);

	@Override
	public void addPages() {
		
		//projectSelectionPage = new ProjectSelectionWizardPage("Project Selection"); 
		//addPage(projectSelectionPage) ; 
		addPage(modelselectionPage);
		addPage(metamodelSelectionPage);
		addPage(finalpage);
	}
	
	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() == modelselectionPage
				|| getContainer().getCurrentPage() == metamodelSelectionPage)
			return false;
		else
			return true;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
