package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolModule; 
//import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.IEtlModule;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.strategy.DefaultTransformationStrategy;

import ir.ui.se.mdserg.Inc_etl.wizards.FileChooser;

//import Eobject;

//******

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;

//********************************************************************************************

public class EtlStandaloneExample extends EpsilonStandalone {

	private static final Class<?> IItemLabelProviderClass = IItemLabelProvider.class;
	private static final Class<?> ITreeItemContentProviderClass = ITreeItemContentProvider.class;
	protected static InMemoryEmfModel baseModel = null;
	protected static InMemoryEmfModel newVersion = null;
	protected static InMemoryEmfModel DB = null;
	protected static InMemoryEmfModel OO2DB = null;
	protected static InMemoryEmfModel delDB = null;
	public static InMemoryEmfModel Targetv1;
	
	//String Path = FileChooser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	

	String addtargetpath = FileChooser.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/Model/AddTarget.xmi";
	String deletetargetpath =FileChooser.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/Model/DeleteTarget.xmi";
	String oo2dbpath = FileChooser.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/Model/OO2DB.xmi";
	String TMpath = FileChooser.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/MetaModel/TM.ecore";
    
    
	static EtlModule module = new EtlModule();
	static String Text1 = null;

	public long CreateAddTarget(String Model1,String Model2,String baseTarget,String sourceMetamodel, String targetMetamodel,String etlpath) throws URISyntaxException, Exception {
		

		addtargetpath = addtargetpath.replaceAll("\\\\", "/");
		java.net.URI addtargetpathURI = java.net.URI.create("file:/"+addtargetpath) ;

		deletetargetpath = deletetargetpath.replaceAll("\\\\", "/");
		java.net.URI deletetargetpathURI = java.net.URI.create("file:/"+deletetargetpath) ; 
		
	      oo2dbpath = oo2dbpath.replaceAll("\\\\", "/");
		  java.net.URI oo2dbURI = java.net.URI.create("file:/"+oo2dbpath) ;
		  
		  TMpath = TMpath.replaceAll("\\\\", "/");
		  java.net.URI TMURI = java.net.URI.create("file:/"+TMpath) ;
		  
		
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource res = rs.createResource(URI.createFileURI(targetMetamodel));
		res.load(null);
		EPackage metapackage = (EPackage) res.getContents().get(0);
		EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
		ResourceSet resourseSet = new ResourceSetImpl();
		resourseSet.getPackageRegistry().put(metapackage.getNsURI(), metapackage);
		resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	
		
		Resource resource = resourseSet.createResource(URI.createURI(addtargetpathURI.toString()));
		Resource resource1 = resourseSet.createResource(URI.createURI(deletetargetpathURI.toString()));
		
		etlpath = etlpath.replaceAll("\\\\", "/");
		java.net.URI etlURI = java.net.URI.create("file:/"+etlpath) ; 
		
		module.getContext().setModule(module);
		module.parse(etlURI);

		EclModule eclmodule = new EclModule();
		baseModel = getInMemoryEmfModel("Left", new File(Model1), sourceMetamodel);
		newVersion = getInMemoryEmfModel("Right", new File(Model2), sourceMetamodel);
		DB = getInMemoryEmfModel("DB", new File(addtargetpath), targetMetamodel);
		delDB = getInMemoryEmfModel("DB", new File(deletetargetpath), targetMetamodel);
		OO2DB = getInMemoryEmfModel("OO2DB", new File(oo2dbpath), TMpath);
		Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
		Targetv1.setStoredOnDisposal(true);

		// *******
		ComposedAdapterFactory composedAdapterFactory;
		ArrayList<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		composedAdapterFactory = new ComposedAdapterFactory(factories);
		
		eclmodule.parse(EpsilonStandalone.class.getResource("Ecloo2.ecl").toURI());
		eclmodule.getContext().getModelRepository().addModel(baseModel);
		eclmodule.getContext().getModelRepository().addModel(newVersion);

		long ECLstartTime = System.currentTimeMillis();
		eclmodule.execute();

		// ********************************************************************************************************************

		module.getContext().getModelRepository().addModel(DB);
		module.getContext().getModelRepository().addModel(OO2DB);
		module.getContext().setTransformationStrategy(new DefaultTransformationStrategy());
		module.getContext().getModelRepository().addModel(delDB);
		module.getContext().setTransformationStrategy(new DefaultTransformationStrategy());
		newVersion.setName("OO");
		module.getContext().getModelRepository().addModel(newVersion);

		// *******************************************************************************************************************
		
		ECL_CL_ModelDifferencing md = new ECL_CL_ModelDifferencing();
		ArrayList<Object> AddedElement = md.AddEllement(eclmodule, new File("Ecloo2.ecl"));

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - ECLstartTime;
		long startTime12 = System.currentTimeMillis();
		
		if (AddedElement != null) {
			for (int k = 0; k < AddedElement.size(); k++) {
				Collection<Object> addtargets = CollectionUtil.createDefaultList();
				DynamicEObjectImpl addedElement = (DynamicEObjectImpl) AddedElement.get(k);
				String ElementType = (((DynamicEObjectImpl) AddedElement.get(k)).eClass().getName());
				
				int count = module.getTransformationRules().size();
				
				for (int j = 0; j < count; j++) {
					
					TransformationRule tr = module.getTransformationRules().get(j);
					
					int indexOf_sign = module.getTransformationRules().get(j).getSourceParameter().getTypeName().indexOf("!")+1;
					
					String RuleType = (module.getTransformationRules().get(j).getSourceParameter().getTypeName()
							.substring(indexOf_sign));
					
					List<Parameter> TargetType = module.getTransformationRules().get(j).getTargetParameters();
					
					if (ElementType.equals(RuleType)) {
					
						for (int i = 0; i < TargetType.size(); i++) {
							
							int indexOf_TargetType = TargetType.get(i).getTypeName().indexOf("!")+1;
							
							EObject eObject1 = DB.createInstance(TargetType.get(i).getTypeName().substring(indexOf_TargetType));
							addtargets.add(eObject1);
							DB.setResource(resource);
							DB.getResource().getContents().add(eObject1);
							module.getContext().getTransformationTrace().add(addedElement, addtargets, tr);
						}


						if (tr.getName().equals(module.getTransformationRules().get(j).getName())) {
							Collection addtargetElement = tr.transform(addedElement, addtargets, module.getContext());
							
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
					} 
				} 
			} 
		} 

		long endTime12 = System.currentTimeMillis();
		long elapsedTime32 = endTime12 - startTime12;
	
		// ********************************************MethodeDELETE************************************************
		
		long startTime24 = System.currentTimeMillis();

		ArrayList<Object> DeletedElement = md.DeleteEllement(eclmodule, new File("Ecloo2.ecl"));
		ArrayList<Object> value = new ArrayList();
		ArrayList<String> ElementType = new ArrayList();
		baseModel.setName("OO");
		module.getContext().getModelRepository().addModel(baseModel);
		// end

		long endTime24 = System.currentTimeMillis();
		long elapsedTime24 = endTime24 - startTime24;
		
		long startTime1 = System.currentTimeMillis();
		
		if (DeletedElement != null) {
			
			for (int k = 0; k < DeletedElement.size(); k++) {

				Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
				Targetv1.setStoredOnDisposal(true);

				Collection<Object> deltargets = CollectionUtil.createDefaultList();

				DynamicEObjectImpl deletedElement = (DynamicEObjectImpl) DeletedElement.get(k);
				String deletedElements = (((DynamicEObjectImpl) DeletedElement.get(k)).eClass().getName()); // type

				int count = module.getTransformationRules().size();
				for (int j = 0; j < count; j++) {

					TransformationRule tr = module.getTransformationRules().get(j);
					int indexOf_Sign = module.getTransformationRules().get(j).getSourceParameter().getTypeName().indexOf("!")+1;
					
					String RuleType = (module.getTransformationRules().get(j).getSourceParameter().getTypeName()
							.substring(indexOf_Sign));

					List<Parameter> TargetType = module.getTransformationRules().get(j).getTargetParameters();
					if (deletedElements.equals(RuleType)) {

						for (int i = 0; i < TargetType.size(); i++) {
			
							int indexOf_TargetType = TargetType.get(i).getTypeName().indexOf("!")+1;
							EObject eObject = delDB.createInstance(TargetType.get(i).getTypeName().substring(indexOf_TargetType));
						
							deltargets.add(eObject);
							delDB.setResource(resource1);
							delDB.getResource().getContents().add(eObject);

							module.getContext().getTransformationTrace().add(deletedElement, deltargets, tr);
							ElementType.add(TargetType.get(i).getTypeName().substring(3));
						}

						if (tr.getName().equals(module.getTransformationRules().get(j).getName())) {

							long startTime2 = System.currentTimeMillis();
							Collection deletetargetElement = tr.transform(deletedElement, deltargets,
									module.getContext());

							long endTime2 = System.currentTimeMillis();
							long elapsedTime22 = endTime2 - startTime2;

							for (Object obj : deletetargetElement) {
								DynamicEObjectImpl eobj = (DynamicEObjectImpl) obj;
								Text1 = (String) eobj.eGet(eobj.eClass().getEStructuralFeature("name"));
								value.add(Text1);
							}
						
							for (int w = value.size() - 1; w >= 0; w--) {
								IItemLabelProvider itemLableprovider;
								
								for (int t = ElementType.size() - 1; t >= 0; t--) {
									
									for (EObject modelElement : Targetv1.getAllOfKind(ElementType.get(t))) {
										itemLableprovider = (IItemLabelProvider) composedAdapterFactory
												.adapt(modelElement, IItemLabelProviderClass);
										
										int index = itemLableprovider.getText(modelElement).lastIndexOf(" ");
										String Text2 = itemLableprovider.getText(modelElement).substring(index + 1);
										if (value.get(w).equals(Text2)) {
											Targetv1.deleteElement(modelElement);
											break;
										}
									}
								}
							}
							Targetv1.dispose();
						} 
					} 
				} 
			} 
		}

		long endTime1 = System.currentTimeMillis();
		long elapsedTime3 = endTime1 - startTime1;
		long add_deleteTimes = elapsedTime32 + elapsedTime3;
		
		@SuppressWarnings("rawtypes")
		Map options1 = new HashMap();
		options1.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		try {
			resource1.save(options1);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return add_deleteTimes;

	}

	@Override
	public IEolModule createModule() {
		return new EtlModule();
	}


	
	@Override
	public void postProcess() {

	}

	public List<ModuleElement> getModuleElements() {
		return Collections.emptyList();
	}

	public IEtlModule getModule() {
		return null;

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
