package ir.ui.se.mdserg.Inc_etl.wizards;

import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jdt.internal.core.SetVariablesOperation;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class FinalPage extends WizardPage {

	private static Composite container;
	private TableViewer tableViewer;
	Color Black;
	static Table table;
	private TableItem item1 ;
	
	DataHolder dt;
	
	

	public FinalPage(DataHolder dt) {
		// TODO Auto-generated constructor stub
		super("FinalPage");
		setTitle("ResultPage");
		setMessage("Transformation is running......");	
		this.dt = dt;
	}
	
	@Inject
	private MDirtyable dirty;

	@Override
	public void createControl(Composite parent) {
		
		
		setPageComplete(false);
		container = new Composite(parent, SWT.NULL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(data);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		Table table = new Table(container, SWT.BORDER);
		tableViewer = new TableViewer(table);
		data.heightHint = 50;
		table.setLayoutData(data);
		TableColumn tc1 = new TableColumn(table, SWT.CENTER);
		TableColumn tc2 = new TableColumn(table, SWT.CENTER);
		tc1.setText("Address of updated target model");
		tc2.setText("ExecutionTime(s)");
		tc1.setWidth(200);
		tc2.setWidth(200);
		table.setHeaderVisible(true);
		setControl(container);
		
		item1 = new TableItem(table, SWT.NONE);
	}
	
	public void updateTable() {
	
		String Update_baseTarget = dt.path; 
		String RunTime = dt.Time;
	
		item1.setText(new String[] {Update_baseTarget,RunTime}); 
	}
	
	
	private TableViewerColumn createTableViewerColumn(String string, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
