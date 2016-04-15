package my.iobserve.userbehavior.modeling.clustering;


import java.util.List;

import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class AbstractClustering {
	
	// Creates the absolute transition attributes for the clustering
	protected Instances createInstances(List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel, List<String> listOfCalledMethods) {
		
		int numberOfMatrixElements = listOfCalledMethods.size()*listOfCalledMethods.size();
		FastVector fvWekaAttributes = new FastVector(numberOfMatrixElements);
		
		for(int i=0;i<numberOfMatrixElements;i++) {
			String attributeName = "Attribute"+i;
			Attribute attribute = new Attribute(attributeName);
			fvWekaAttributes.addElement(attribute);
		}
		
		Instances clusterSet = new Instances("TransitionCounts", fvWekaAttributes, absoluteTransitionModel.size());
		
		for(final UserSessionAsAbsoluteTransitionMatrix userSession:absoluteTransitionModel) {
			
			int indexOfAttribute = 0;
			Instance instance = new Instance(numberOfMatrixElements);
			
			for(int row=0;row<listOfCalledMethods.size();row++) {
				for(int column=0;column<listOfCalledMethods.size();column++) {
					
					instance.setValue((Attribute)fvWekaAttributes.elementAt(indexOfAttribute), userSession.getAbsoluteTransitionMatrix()[row][column]);
					indexOfAttribute++;
					
				}
			}
			
			clusterSet.add(instance);
		}
		
		return clusterSet;
	}
	

	// Creates the user session length attributes for the clustering
//	protected Instances createInstances(List<UserSession> sessions) {
//		
//		Attribute attribute1 = new Attribute("amountOfEvents");
//		FastVector fvWekaAttributes = new FastVector(1);
//		fvWekaAttributes.addElement(attribute1);
//		Instances isTrainingSet = new Instances("Relation", fvWekaAttributes, sessions.size());
//		
//		for(final UserSession userSession:sessions) {
//			
//			Instance instance = new Instance(1);
//			instance.setValue((Attribute)fvWekaAttributes.elementAt(0), userSession.getEvents().size());
//			isTrainingSet.add(instance);
//			
//		}
//		
//		return isTrainingSet;
//	}
}
