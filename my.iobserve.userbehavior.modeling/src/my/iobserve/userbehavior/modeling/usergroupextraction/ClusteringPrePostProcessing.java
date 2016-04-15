package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;
import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;

public class ClusteringPrePostProcessing {
	
	// Chapter 4.3.3.1
	public List<String> getListOfDistinctOperationSignatures(List<UserSession> sessions) {
		
		List<String> listOfDistinctOperationSignatures = new ArrayList<String>();
		
		for(final UserSession userSession:sessions) {
			
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			
			while(iteratorEvents.hasNext()) {
				
				final EntryCallEvent event = iteratorEvents.next();
				
				if(!listOfDistinctOperationSignatures.contains(event.getOperationSignature())) {
					listOfDistinctOperationSignatures.add(event.getOperationSignature());
				}
				
			}
			
		}
		
		return listOfDistinctOperationSignatures;
	}
	
	// Chapter 4.3.3.2
	public List<UserSessionAsAbsoluteTransitionMatrix> getAbsoluteTransitionModel(List<UserSession> userSessions, List<String> listOfDistinctOperationSignatures) {
		
		List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel = new ArrayList<UserSessionAsAbsoluteTransitionMatrix>(); 
		
		for(final UserSession userSession:userSessions) {
			
			UserSessionAsAbsoluteTransitionMatrix transitionMatrix = new UserSessionAsAbsoluteTransitionMatrix(userSession.getSessionId(), listOfDistinctOperationSignatures.size());
			List<EntryCallEvent> callSequence = userSession.getEvents();
			
			for(int i=0;i<callSequence.size()-1;i++) {
				
				String currentCall = callSequence.get(i).getOperationSignature();
				String subsequentCall = callSequence.get(i+1).getOperationSignature();
				
				int indexOfCurrentCall = listOfDistinctOperationSignatures.indexOf(currentCall);
				int indexOfSubsequentCall = listOfDistinctOperationSignatures.indexOf(subsequentCall);
				
				transitionMatrix.getAbsoluteTransitionMatrix() [indexOfCurrentCall] [indexOfSubsequentCall] = transitionMatrix.getAbsoluteTransitionMatrix() [indexOfCurrentCall] [indexOfSubsequentCall] + 1;
				
			}
			
			
			absoluteTransitionModel.add(transitionMatrix);
		}
		
		return absoluteTransitionModel;
	}
	
	// Chapter 4.3.3.4
	public List<EntryCallSequenceModel> getForEachUserGroupAEntryCallSequenceModel(ClusteringResults clusteringResults, EntryCallSequenceModel callSequenceModel) {
		
		int numberOfClusters = clusteringResults.getNumberOfClusters();
		int[] assignments = clusteringResults.getAssignments();
		List<EntryCallSequenceModel> entryCallSequenceModels = new ArrayList<EntryCallSequenceModel>(numberOfClusters);
		double countOfAbsoluteUserSessions = callSequenceModel.getUserSessions().size();
		
		for(int k=0;k<numberOfClusters;k++) {
			
			List<UserSession> sessions = new ArrayList<UserSession>();
			String instancesBelongingToCluster = "Cluster "+k+": ";
			int instanceNumber = 0;
			double countOfAssigendUserSessions = 0;
			
			for(int clusterNum : assignments) {
				
				if(clusterNum==k) {
					sessions.add(callSequenceModel.getUserSessions().get(instanceNumber));
					countOfAssigendUserSessions++;
					instancesBelongingToCluster = instancesBelongingToCluster + "Instance"+instanceNumber+" ";
				}
				instanceNumber++;
			}
			
			double relativeFrequencyOfUserGroup = countOfAssigendUserSessions/countOfAbsoluteUserSessions;
			entryCallSequenceModels.add(new EntryCallSequenceModel(sessions, relativeFrequencyOfUserGroup));
			System.out.println(instancesBelongingToCluster);
		}
		
		return entryCallSequenceModels;
		
	}

}
