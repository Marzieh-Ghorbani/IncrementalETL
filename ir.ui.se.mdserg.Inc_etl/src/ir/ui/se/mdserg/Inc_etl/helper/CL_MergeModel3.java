package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.models.IModel;

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

public  class CL_MergeModel3 extends EpsilonStandalone {

	private static final Class<?> IItemLabelProviderClass = IItemLabelProvider.class;
	private static final Class<?> ITreeItemContentProviderClass = ITreeItemContentProvider.class;

	@SuppressWarnings("unchecked")

	public long UpdateInitialTarget(String baseTarget, String targetMetamodel) throws IOException{
		
		CL_MergeModel1 merge1 = new CL_MergeModel1();
		baseTarget = baseTarget.replaceAll("\\\\", "/");
		URI baseTargetURI = URI.createURI("file:/"+baseTarget);
		String Temppath= merge1.temppath;
		Temppath = Temppath.replaceAll("\\\\", "/");
		URI TemppathURI = URI.createURI("file:/"+Temppath);
		
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
		ResourceSet resourseSet = new ResourceSetImpl();
		resourseSet.getPackageRegistry().put(metapackage.getNsURI(), metapackage);
		resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		Resource resource = resourseSet.getResource(URI.createURI(baseTargetURI.toString()), true);
		Resource temp_resource = resourseSet.getResource(URI.createURI(TemppathURI.toString()), true);

		InMemoryEmfModel Targetv1 = null;
		InMemoryEmfModel temp = null;
		InMemoryEmfModel tempDB = null;
		String Text_base = null;
		String Type_base = null;
		Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
		temp = getInMemoryEmfModel("DB", new File(Temppath), targetMetamodel);
		
		long startTime = System.currentTimeMillis();
		
		for (Object temp_Element : temp.allInstances()) {
			IItemLabelProvider itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(temp_Element,
					IItemLabelProviderClass);
			boolean flage = false;
			String Text_tempElement = itemLableprovider.getText(temp_Element).split(" ")[1];
			String Type_tempElement = itemLableprovider.getText(temp_Element).split(" ")[0];
			try {
				for (EObject baseTarget_Element : Targetv1.getAllOfKind(Type_tempElement)) {
					itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(baseTarget_Element,
							IItemLabelProviderClass);
					Text_base = itemLableprovider.getText(baseTarget_Element).split(" ")[1];
					Type_base = itemLableprovider.getText(baseTarget_Element).split(" ")[0];
					if (Text_tempElement.equals(Text_base)) {
						flage = true;
						break;
					} 
				}
			} catch (EolModelElementTypeNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			if (!flage) {
				EObject copyElement = EcoreUtil.copy((EObject) temp_Element);
				resource.getContents().add(copyElement);
				/// save model
				@SuppressWarnings("rawtypes")
				Map options = new HashMap();
				options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
				try {
					resource.save(options);
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} 
			Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
		} 
       long endTime = System.currentTimeMillis();
	   long elapsedTime3= endTime-startTime;
	   return elapsedTime3;
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
