package my.iobserve.userbehavior.modeling.clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;
import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class AbstractClustering {
	
	List<EntryCallSequenceModel> clusteredEntryCallSequenceModels = new ArrayList<EntryCallSequenceModel>();
	
	protected Instances createInstances(List<UserSession> sessions) {
		
		Attribute attribute1 = new Attribute("amountOfEvents");
		FastVector fvWekaAttributes = new FastVector(1);
		fvWekaAttributes.addElement(attribute1);
		Instances isTrainingSet = new Instances("Relation", fvWekaAttributes, sessions.size());
		
		for(final UserSession userSession:sessions) {
			
			Instance instance = new Instance(1);
			instance.setValue((Attribute)fvWekaAttributes.elementAt(0), userSession.getEvents().size());
			isTrainingSet.add(instance);
			
		}
		
		return isTrainingSet;
	}
	
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
	
	protected List<String> createListOfDistinctCalledMethods(List<UserSession> sessions) {
		
		List<String> listOfCalledMethods = new ArrayList<String>();
		
		for(final UserSession userSession:sessions) {
			
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			
			while(iteratorEvents.hasNext()) {
				
				final EntryCallEvent event = iteratorEvents.next();
				
				if(!listOfCalledMethods.contains(event.getOperationSignature())) {
					listOfCalledMethods.add(event.getOperationSignature());
				}
				
			}
			
		}
		
		return listOfCalledMethods;
		
	}
	
	protected List<UserSessionAsAbsoluteTransitionMatrix> createAbsoluteTransitionUserSessions(List<UserSession> sessions, List<String> distinctOperationSignatures) {
		
		List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel = new ArrayList<UserSessionAsAbsoluteTransitionMatrix>(); 
		
		for(final UserSession userSession:sessions) {
			
			UserSessionAsAbsoluteTransitionMatrix transitionMatrix = new UserSessionAsAbsoluteTransitionMatrix(userSession.getSessionId(), distinctOperationSignatures.size());
			List<EntryCallEvent> callSequence = userSession.getEvents();
			
			for(int i=0;i<callSequence.size()-1;i++) {
				
				String currentCall = callSequence.get(i).getOperationSignature();
				String subsequentCall = callSequence.get(i+1).getOperationSignature();
				
				int indexOfCurrentCall = distinctOperationSignatures.indexOf(currentCall);
				int indexOfSubsequentCall = distinctOperationSignatures.indexOf(subsequentCall);
				
				transitionMatrix.getAbsoluteTransitionMatrix() [indexOfCurrentCall] [indexOfSubsequentCall] = transitionMatrix.getAbsoluteTransitionMatrix() [indexOfCurrentCall] [indexOfSubsequentCall] + 1;
				
			}
			
			
			absoluteTransitionModel.add(transitionMatrix);
		}
		
		return absoluteTransitionModel;
	}
	
	protected List<EntryCallSequenceModel> createForEveryClusterASequenceModel(int numberOfClusters, int[] assignments, EntryCallSequenceModel sequenceModel) {
		
		List<EntryCallSequenceModel> entryCallSequenceModels = new ArrayList<EntryCallSequenceModel>(numberOfClusters);
		
		for(int k=0;k<numberOfClusters;k++) {
			
			List<UserSession> sessions = new ArrayList<UserSession>();
			String instancesBelongingToCluster = "Cluster "+k+": ";
			int instanceNumber = 0;
			
			for(int clusterNum : assignments) {
				
				if(clusterNum==k) {
					sessions.add(sequenceModel.getUserSessions().get(instanceNumber));
					instancesBelongingToCluster = instancesBelongingToCluster + "Instance"+instanceNumber+" ";
				}
				instanceNumber++;
			}
			
			entryCallSequenceModels.add(new EntryCallSequenceModel(sessions));
			System.out.println(instancesBelongingToCluster);
		}
		
		return entryCallSequenceModels;
		
	}

}
