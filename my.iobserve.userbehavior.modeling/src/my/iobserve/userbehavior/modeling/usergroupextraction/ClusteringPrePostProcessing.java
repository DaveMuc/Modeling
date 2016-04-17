package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;
import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import my.iobserve.userbehavior.modeling.data.WorkloadIntensity;

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
	
	// Chapter 4.3.3.5
	public void setTheWorkloadIntensityForTheEntryCallSequenceModels(List<EntryCallSequenceModel> entryCallSequenceModels) {
		
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			
			WorkloadIntensity workloadIntensity = new WorkloadIntensity();
			
			calculateInterarrivalTime(entryCallSequenceModel.getUserSessions(), workloadIntensity);
			calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions(), workloadIntensity);
			
			entryCallSequenceModel.setWorkloadIntensity(workloadIntensity);
		}
		
	}
	
	private void calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions, WorkloadIntensity workloadIntensity) {
		int maxNumberOfConcurrentUsers = 0;
		int averageNumberOfConcurrentUsers = 0;
	
		if(sessions.size() > 0) {
			
			Collections.sort(sessions, this.SortUserSessionByEntryTime);
			List<Long> listOfExitTimes = new ArrayList<Long>();
			int countOfConcurrentUsers = 0;
			
			for (int i = 0; i < sessions.size() - 1; i++) {
				
				final long startTime =  sessions.get(i).getEntryTime();
				final long exitTime = sessions.get(i).getExitTime();
				int numberOfConcurrentUserSessionsDuringThisSession = 1;
				
				for(int j=0;j<listOfExitTimes.size();j++) {
					if(startTime<listOfExitTimes.get(j)) {
						numberOfConcurrentUserSessionsDuringThisSession++;
					}
				}
				
				if(numberOfConcurrentUserSessionsDuringThisSession > maxNumberOfConcurrentUsers)
					maxNumberOfConcurrentUsers = numberOfConcurrentUserSessionsDuringThisSession;
				
				countOfConcurrentUsers += numberOfConcurrentUserSessionsDuringThisSession;
				listOfExitTimes.add(exitTime);
			}
			

			averageNumberOfConcurrentUsers = countOfConcurrentUsers/sessions.size();
		}
		
		workloadIntensity.setMaxNumberOfConcurrentUsers(maxNumberOfConcurrentUsers);
		workloadIntensity.setAvgNumberOfConcurrentUsers(averageNumberOfConcurrentUsers);
	}
	
	/**
	 * David Peter
	 * Used from package org.iobserve.analysis.filter.TEntryEventSequence;
	 * Changed to double values
	 */
	/**
	 * Calculate the inter arrival time of the given user sessions
	 * @param sessions sessions
	 * @return >= 0.
	 */
	private void calculateInterarrivalTime(final List<UserSession> sessions, WorkloadIntensity workloadIntensity) {
		long interArrivalTime = 0;
		if(sessions.size() > 0) {
			//sort user sessions
			Collections.sort(sessions, this.SortUserSessionByExitTime);
			
			long sum = 0;
			for (int i = 0; i < sessions.size() - 1; i++) {
				final long exitTimeU1 = sessions.get(i).getExitTime();
				final long exitTimeU2 = sessions.get(i + 1).getExitTime();
				sum += exitTimeU2 - exitTimeU1;
			}
			
			final long numberSessions = sessions.size() > 1?sessions.size()-1:1;
			interArrivalTime = sum / numberSessions;
		}
		
		workloadIntensity.setInterarrivalTimeOfUserSessions(interArrivalTime);
	}
	
	
	/**
	 * David Peter
	 * Used from package org.iobserve.analysis.filter.TEntryEventSequence;
	 */
	/**
	/**
	 * Sorts {@link UserSession} by the exit time
	 */
	private final Comparator<UserSession> SortUserSessionByExitTime = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			long exitO1 = o1.getExitTime();
			long exitO2 = o2.getExitTime();
			if(exitO1 > exitO2) {
				return 1;
			} else if(exitO1 < exitO2) {
				return -1;
			}
			return 0;
		}
	};
	
	private final Comparator<UserSession> SortUserSessionByEntryTime = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			long entryO1 = o1.getEntryTime();
			long entryO2 = o2.getEntryTime();
			if(entryO1 > entryO2) {
				return 1;
			} else if(entryO1 < entryO2) {
				return -1;
			}
			return 0;
		}
	};

}
