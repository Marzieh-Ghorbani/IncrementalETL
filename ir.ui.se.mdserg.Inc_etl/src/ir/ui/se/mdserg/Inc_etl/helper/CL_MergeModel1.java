package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.models.IModel;

import ir.ui.se.mdserg.Inc_etl.wizards.DataHolder;
import ir.ui.se.mdserg.Inc_etl.wizards.FinalPage;
import ir.ui.se.mdserg.Inc_etl.wizards.MetaModelSelectionPage;
import ir.ui.se.mdserg.Inc_etl.wizards.ModelSelectionPage;

//******

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;

public class CL_MergeModel1 extends EpsilonStandalone {

	private static final Class<?> IItemLabelProviderClass = IItemLabelProvider.class;
	private static final Class<?> ITreeItemContentProviderClass = ITreeItemContentProvider.class;

	String temppath = "C:\\Users\\M\\Desktop\\Model\\temp.xmi";
	FinalPage fp;
	DataHolder dt;

	@SuppressWarnings("unchecked")

	public long CreateTempraryModel(String baseTarget, String targetMetamodel) throws URISyntaxException, Exception {

		EtlStandaloneExample etlstandalon = new EtlStandaloneExample();
		ModelSelectionPage modelselect = new ModelSelectionPage();
		MetaModelSelectionPage metamodelselect = new MetaModelSelectionPage(fp, dt);

		String addTargetpath = etlstandalon.addtargetpath;
		addTargetpath = addTargetpath.replaceAll("\\\\", "/");
		URI addTargetpathURI = URI.createURI("file:/" + addTargetpath);

		baseTarget = modelselect.TargetModelFC.path;
		baseTarget = baseTarget.replaceAll("\\\\", "/");
		URI baseTargetURI = URI.createURI("file:/" + baseTarget);
		String etlpath = metamodelselect.EtlScriptFC.path;
		temppath = temppath.replaceAll("\\\\", "/");
		java.net.URI temppathURI = java.net.URI.create("file:/" + temppath);

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
		Resource resource = resourseSet.getResource(baseTargetURI, true);
		Resource temp_resource = resourseSet.createResource(URI.createURI(temppathURI.toString()));

		InMemoryEmfModel Targetv1 = null;
		Targetv1 = getInMemoryEmfModel("DB", new File(baseTarget), targetMetamodel);
		InMemoryEmfModel tempDB = null;
		tempDB = getInMemoryEmfModel("DB", new File(addTargetpath), targetMetamodel);

		String Type_tempDB_Container = null;
		DynamicEObjectImpl value_tempDB_Container = null;
		Object val = null;

		EObject telement = null;
		int index;
		HashMap existElement_tpDB = new HashMap();
		HashMap NotExistElement_tpDB = new HashMap();
		HashMap NotExistElement1_tpDB = new HashMap();
		Set<String> hash_null_Set = new HashSet<String>();
		Set<Object> hash_null_type = new HashSet<Object>();
		Set<String> hash_Set = new HashSet<String>();
		Set<Object> hash_type = new HashSet<Object>();
		EObject copyElement = null;
		ArrayList<Object> value_tpDB = new ArrayList<Object>();
		Boolean flag1 = false;

		long startTime = System.currentTimeMillis();

		tempDB = getInMemoryEmfModel("DB", new File(addTargetpath), targetMetamodel);

		for (Object tempDB_Element : tempDB.allInstances()) {

			Object tempDB_Container = tempDB.getContainerOf(tempDB_Element);
			value_tempDB_Container = (DynamicEObjectImpl) tempDB_Container;
			IItemLabelProvider itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(tempDB_Element,
					IItemLabelProviderClass);
			boolean flage = false;

			index = itemLableprovider.getText(tempDB_Element).lastIndexOf(" ");
			String Type_Element = itemLableprovider.getText(tempDB_Element).substring(0, index);
			String Text_Element = itemLableprovider.getText(tempDB_Element).substring(index + 1);

			for (Object baseTarget_Element : Targetv1.allInstances()) {

				itemLableprovider = (IItemLabelProvider) composedAdapterFactory.adapt(baseTarget_Element,
						IItemLabelProviderClass);

				index = itemLableprovider.getText(baseTarget_Element).indexOf(" ");
				String Type_ElementbaseTarget = itemLableprovider.getText(baseTarget_Element).substring(0, index + 1);
				String Text_ElementbaseTarget = itemLableprovider.getText(baseTarget_Element).substring(index + 1);

				if (Text_Element.equals(Text_ElementbaseTarget)) {
					flage = true;
					existElement_tpDB.put(Text_Element, Type_Element);

					break;
				}
			}

			if (!flage & value_tempDB_Container == null) {
				telement = (EObject) tempDB_Element;
				hash_null_type.add(Type_Element);
				NotExistElement1_tpDB.put(telement, (value_tempDB_Container));
			}

			if (!flage & value_tempDB_Container != null) {

				Type_tempDB_Container = value_tempDB_Container.eClass().getName();
				val = value_tempDB_Container.eGet(value_tempDB_Container.eClass().getEStructuralFeature("name"));
				telement = (EObject) tempDB_Element;
				hash_type.add(Type_tempDB_Container);
				NotExistElement_tpDB.put(telement, (value_tempDB_Container.eClass().getName() + " " + val));

			}
		}

		if (NotExistElement1_tpDB != null) {

			Iterator it;
			Map.Entry pair = null;
			Map.Entry pair1 = null;
			it = NotExistElement1_tpDB.entrySet().iterator();
			List<String> listOfKeys = null;

			while (it.hasNext()) {

				pair = (Entry) it.next();
				Object key = pair.getKey();
				resource.getContents().add((EObject) key);

				@SuppressWarnings("rawtypes")
				Map options2 = new HashMap();
				options2.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
				try {
					resource.save(options2);
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

		if (NotExistElement_tpDB != null) {
			Iterator it;
			Map.Entry pair = null;
			Map.Entry pair1 = null;
			it = NotExistElement_tpDB.entrySet().iterator();
			List<Object> listOfKeys = null;
			while (it.hasNext()) {
				pair = (Entry) it.next();
				Object v = pair.getValue();
				hash_Set.add((String) v);
			}
			for (String s : hash_Set) {
				for (Object type : hash_type) {
					Boolean flag = false;
					for (Object baseElement : Targetv1.getAllOfKind((String) type)) {
						EObject bsEle = (EObject) baseElement;
						IItemLabelProvider itemLableprovider1 = (IItemLabelProvider) composedAdapterFactory
								.adapt(baseElement, IItemLabelProviderClass);
						if (s.equals(itemLableprovider1.getText(baseElement))) {
							listOfKeys = MapUtilities.getAllKeysForValue(NotExistElement_tpDB, s);
							for (int i = 0; i < listOfKeys.size(); i++) {
								((List<EObject>) ((EObject) baseElement)
										.eGet(((EObject) baseElement).eClass().getEStructuralFeature("columns"), true))
												.add((EObject) listOfKeys.get(i));
								temp_resource.getContents().add((EObject) baseElement);
							}
							flag = true;
						}
					}
				}
			}
		}
		@SuppressWarnings("rawtypes")
		Map options2 = new HashMap();
		options2.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		try {
			temp_resource.save(options2);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime1 = endTime - startTime;
		return elapsedTime1;
	}

	public static class MapUtilities {

		static <K, V> List<K> getAllKeysForValue(Map<K, V> mapOfWords, V value) {
			List<K> listOfKeys = null;

			if (mapOfWords.containsValue(value)) {
				listOfKeys = new ArrayList<>();
				for (Map.Entry<K, V> entry : mapOfWords.entrySet()) {
					if (entry.getValue().equals(value)) {
						listOfKeys.add(entry.getKey());
					}
				}
			}
			return listOfKeys;
		}
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
