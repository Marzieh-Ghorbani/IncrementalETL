package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.models.IModel;

import ir.ui.se.mdserg.Inc_etl.wizards.DataHolder;
import ir.ui.se.mdserg.Inc_etl.wizards.FinalPage;
import ir.ui.se.mdserg.Inc_etl.wizards.MetaModelSelectionPage;
import ir.ui.se.mdserg.Inc_etl.wizards.ModelSelectionPage;

//import Eobject;

//******

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;

public class CL_MergeModel2 extends EpsilonStandalone {

	private static final Class<?> IItemLabelProviderClass = IItemLabelProvider.class;
	private static final Class<?> ITreeItemContentProviderClass = ITreeItemContentProvider.class;
	FinalPage fp;
	DataHolder dt;

	@SuppressWarnings("unchecked")

	public long DeleteElementsModel(String baseTarget, String targetMetamodel) throws URISyntaxException, Exception {

		EtlStandaloneExample etlstandalon = new EtlStandaloneExample();
		ModelSelectionPage modelselect = new ModelSelectionPage();
		MetaModelSelectionPage metamodelselect = new MetaModelSelectionPage(fp, dt);
		CL_MergeModel1 merge1 = new CL_MergeModel1();

		baseTarget = baseTarget.replaceAll("\\\\", "/");
		URI baseTargetURI = URI.createURI("file:/" + baseTarget);
		String Temppath = merge1.temppath;
		Temppath = Temppath.replaceAll("\\\\", "/");
		URI TemppathURI = URI.createURI("file:/" + baseTarget);

		ComposedAdapterFactory composedAdapterFactory;
		ArrayList<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		composedAdapterFactory = new ComposedAdapterFactory(factories);

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource res = rs.createResource(URI.createFileURI(targetMetamodel));
		res.load(null);
		EPackage metapackage = (EPackage) res.getContents().get(0);
		EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
		ResourceSet resourseSet = new ResourceSetImpl();
		resourseSet.getPackageRegistry().put(metapackage.getNsURI(), metapackage);
		resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		Resource resource = resourseSet.getResource(URI.createURI(baseTargetURI.toString()), true);
		Resource temp_resource = resourseSet.getResource(URI.createURI(TemppathURI.toString()), true);

		InMemoryEmfModel Targetv1 = null;
		InMemoryEmfModel temp = null;

		Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
		InMemoryEmfModel tempDB = null;
		tempDB = getInMemoryEmfModel("DB", new File(Temppath), targetMetamodel);
		DynamicEObjectImpl value_parent_temp = null;
		Object ptval = null;

		ArrayList<Object> elm = new ArrayList();
		ArrayList<Object> bae = new ArrayList();
		String Text_base = null;
		String Type_base = null;

		long startTime = System.currentTimeMillis();

		for (Object tempDB_Element : tempDB.allInstances()) {
			IItemLabelProvider itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(tempDB_Element,
					IItemLabelProviderClass);
			Object tempDB_Container = tempDB.getContainerOf(tempDB_Element);
			value_parent_temp = (DynamicEObjectImpl) tempDB_Container;
			if (value_parent_temp != null) {
				ptval = value_parent_temp.eGet(value_parent_temp.eClass().getEStructuralFeature("name"));
			}

			boolean flage = false;

			String Text_tempElement = itemLableprovider.getText(tempDB_Element).split(" ")[1];
			String Type_tempElement = itemLableprovider.getText(tempDB_Element).split(" ")[0];
			for (EObject baseTarget_Element : Targetv1.getAllOfKind(Type_tempElement)) {
				itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(baseTarget_Element,
						IItemLabelProviderClass);

				Text_base = itemLableprovider.getText(baseTarget_Element).split(" ")[1];
				Type_base = itemLableprovider.getText(baseTarget_Element).split(" ")[0];
				bae.add(baseTarget_Element);

				if (Text_tempElement.equals(Text_base)) {
					flage = true;
					elm.add(baseTarget_Element);
				}
			}
		}

		if (elm != null) {
			for (int i = elm.size() - 1; i >= 0; i--) {
				for (int j = bae.size() - 1; j >= 0; j--) {
					if (elm.get(i).equals(bae.get(j))) {
						Targetv1.setStoredOnDisposal(true);
						Targetv1.deleteElement(elm.get(i));
					}
				}
			}
			Targetv1.setStoredOnDisposal(true);
			Targetv1.dispose();
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime2 = endTime - startTime;
		return elapsedTime2;
	}

	@Override
	public String getSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IModel> getModels() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
