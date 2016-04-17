package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class BranchOperationModelCreator {
	
	public Branch createBranchOperationModel(EntryCallSequenceModel entryCallSequenceModel) {
		
		int numberOfUserSessions = entryCallSequenceModel.getUserSessions().size();
		
		// The initial branch that contains the root node
		// The likelihood of the root branch is always 1 
		Branch branchOperationModel = new Branch();
		branchOperationModel.setBranchLikelihood(1);
		
		int indexOfCallEvent = 0;
		boolean addedNewCall = false;
		List<String> listOfOperationSignatures = new ArrayList<String>();
		List<List<UserSession>> listOfListOfUserSessions = new ArrayList<List<UserSession>>();
		
		do {
		
			addedNewCall = false;
			listOfOperationSignatures.clear();
			listOfListOfUserSessions.clear();
			
			for(final UserSession userSession : entryCallSequenceModel.getUserSessions()) {
				
				if(indexOfCallEvent>=userSession.getEvents().size()) 
					continue;
				
				addedNewCall = true;
				String operationSignature = userSession.getEvents().get(indexOfCallEvent).getOperationSignature();

				if(!listOfOperationSignatures.contains(operationSignature)) {
					listOfOperationSignatures.add(operationSignature);
					List<UserSession> listOfUserSessions = new ArrayList<UserSession>();
					listOfUserSessions.add(userSession);
					listOfListOfUserSessions.add(listOfUserSessions);
				} else {
					int index = listOfOperationSignatures.indexOf(operationSignature);
					List<UserSession> listOfUserSessions = listOfListOfUserSessions.get(index);
					listOfUserSessions.add(userSession);
					listOfListOfUserSessions.add(index, listOfUserSessions);
				}
			}
			
			indexOfCallEvent++;
		
		} while(addedNewCall);
		
		return branchOperationModel;
	}

}
