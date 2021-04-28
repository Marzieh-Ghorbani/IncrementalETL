package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;

///////////////////////////////////////
import java.io.IOException;
//import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.Model;
import org.eclipse.emf.common.util.URI;

/////////Load Model///////////////////


//******************************************

public abstract class EpsilonStandalone {

	protected IEolExecutableModule module;
	protected Object result;
	public abstract String getSource() throws Exception;
	public abstract List<IModel> getModels() throws Exception;
	public void postProcess() {
	};

	public void preProcess() {
	};

	public void execute() throws Exception {
		module = createModule();
		module.parse(getFile(getSource()));
		// module.getChildren();

		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			System.exit(-1);
		}
		for (IModel model : getModels()) {
			module.getContext().getModelRepository().addModel(model);
		}
		preProcess();
		result = execute(module);
		postProcess();
		module.getContext().getModelRepository().dispose();
	}
	protected Object execute(IEolExecutableModule module) throws EolRuntimeException {
		return module.execute();
	}
	protected EmfModel createEmfModel(String name, String model, String metamodel, boolean readOnLoad,
			 boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(Model.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, getFile(metamodel).toURI().toString());
		properties.put(EmfModel.PROPERTY_MODEL_URI, getFile(model).toURI().toString());
		properties.put(Model.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(Model.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, "");

		return emfModel;
	}

	protected EmfModel createEmfModelByURI(String name, String model, String metamodel, boolean readOnLoad,
			boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(Model.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, getFile(model).toURI().toString());

		properties.put(Model.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(Model.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, "");
		return emfModel;
	}

	protected File getFile(String fileName) throws URISyntaxException {

		String binUri = EpsilonStandalone.class.getResource(fileName).toString();
		String uri = null;
		if (binUri.toString().indexOf("bin") > -1) {
			uri = new String(binUri.toString().replaceAll("bin", "src"));
		} else {
			uri = binUri;
		}

		return new File(uri);
	}

	public IEolExecutableModule createModule() {
		// TODO Auto-generated method stub
		return null;
	}

	// ****************************************Load Model***************************

	public static  InMemoryEmfModel getInMemoryEmfModel(String name, File modelfile, String metamodelPath)
			throws IOException {
		ResourceSet rs = new ResourceSetImpl();
		rs.getPackageRegistry().put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		Resource r = null;
		EPackage metamodel = null;
		java.net.URI javaURI = new File(metamodelPath).toURI();
		r = rs.createResource(URI.createURI(javaURI.toString()));
		r.load(Collections.emptyMap());
		metamodel = (EPackage) r.getContents().get(0);
		rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(metamodel.getNsURI(), metamodel);
		r = rs.createResource(URI.createFileURI(modelfile.toString()));
		r.load(Collections.emptyMap());
		InMemoryEmfModel model = new InMemoryEmfModel(name, r, metamodel);
		model.setMetamodelFile((URI.createFileURI(metamodelPath)).toString());
		model.setMetamodelFileBased(true);
		return model;
	}
}
