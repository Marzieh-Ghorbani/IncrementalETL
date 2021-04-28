package ir.ui.se.mdserg.Inc_etl.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.trace.Match;

public class ECL_CL_ModelDifferencing {
	
	/**
	 * detecte add object to model */
	
	
	public ArrayList<Object> AddEllement(EclModule eclmodule, File file)  {
		
		/** return all match(true & false )*/
		List<Match> allMatches = eclmodule.getContext().getMatchTrace().getMatches();
		ArrayList<Object> matchelement = new ArrayList<Object>();
		int size = allMatches.size();
		boolean flag1 = false;
		for (int i = 0; i < size; i++) {
			/**
			 * The left & right object of the match */
			Match current = allMatches.get(i);
			/**
			 * get matching = false */
			if (!current.isMatching()) {
				
				/*** The left object of the match , The right object of the match */
				DynamicEObjectImpl Right = (DynamicEObjectImpl) current.getRight();
				/** The object of the matched with Right object */
				Match matchess = eclmodule.getContext().getMatchTrace().getMatch(current.getRight());

				/**
				 * The object of the  not matched with Right object
				 * return different object /** The right object of the match */
				 
				if (matchess == null) {
					flag1 = true;
					boolean flag2 = false;
					/**
					 * Prevent  of iterator */
					for (int j = 0; j < matchelement.size(); j++) {
						if (matchelement.get(j).equals(current.getRight())) {
							flag2 = true;
							break;
						} 
					}

					if (!flag2) {
						matchelement.add(current.getRight());
					}
				}
			}
		}
		if (flag1) {
			return matchelement;
		} 
	else {
		return null;
	}
	  }
	
	//********************************************************
	
	/**
	 * detecte deleted object to model
	 */
	
	public ArrayList<Object> DeleteEllement(EclModule eclmodule,File  modelfile)  {


		List<Match> allMatches = eclmodule.getContext().getMatchTrace().getMatches();
		ArrayList<Object> matchelement = new ArrayList<Object>();

		int size = allMatches.size();
		boolean flag1 = false;

		for (int i = 0; i < size; i++) {

			Match current = allMatches.get(i);
			if (!current.isMatching()) {
				DynamicEObjectImpl Left = (DynamicEObjectImpl) current.getLeft();
				DynamicEObjectImpl Right = (DynamicEObjectImpl) current.getRight();
				Match matchess = eclmodule.getContext().getMatchTrace().getMatch(current.getLeft());
				if (matchess == null) {
					flag1 = true;
					boolean flag2 = false;
					for (int j = 0; j < matchelement.size(); j++) {
						if (matchelement.get(j).equals(current.getLeft())) {
							flag2 = true;
							break;
						} 
					} 
					if (!flag2){
						matchelement.add(current.getLeft());
					}
				}
			}
		}
		if (flag1) {
			return matchelement;
		} 
	else {
		      return null;
		
	}

	}
}

