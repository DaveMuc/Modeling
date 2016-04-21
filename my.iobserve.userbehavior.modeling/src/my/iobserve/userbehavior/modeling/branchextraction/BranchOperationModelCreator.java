package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class BranchOperationModelCreator {
	
	public void calculateLikelihoodsOfBranches(BranchOperationModel branchOperationModel) {
		
		List<Integer> branchGuide = new ArrayList<Integer>();
		
		do{
			while(true) {
				Branch examinedBranch = getExaminedBranch(branchGuide, branchOperationModel.getRootBranch());
				int startBranchGuideSize = branchGuide.size();
				if(examinedBranch.getChildBranches().size()==0) {
					break;
				} else {
					for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
						if(examinedBranch.getChildBranches().get(i).getBranchLikelihood()==0){
							branchGuide.add(i);
							break;
						}
					}
				}
				int endBranchGuideSize = branchGuide.size();
				if(endBranchGuideSize==startBranchGuideSize)
					break;
			}
			branchGuide.remove(branchGuide.size()-1);
			setChildBranchLikelihoods(getExaminedBranch(branchGuide, branchOperationModel.getRootBranch()));
		}while(branchGuide.size()>0);
			
	}
	
	private void setChildBranchLikelihoods(Branch examinedBranch) {
//		double countOfParentNode = examinedBranch.getBranchSequence().get(examinedBranch.getBranchSequence().size()-1).getCallElement().getAbsoluteCount();
		double countOfParentNode = examinedBranch.getBranchSequence().get(examinedBranch.getBranchSequence().size()-1).getAbsoluteCount();
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			double countOfChildNode;
//			if(examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).get)
//				countOfChildNode = examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getAbsoluteCount();
//			else 
//				countOfChildNode = examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getExitElement().getAbsoluteCount();
			countOfChildNode = examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getAbsoluteCount();
			double likelihhod = countOfChildNode/countOfParentNode;
			examinedBranch.getChildBranches().get(i).setBranchLikelihood(likelihhod);
		}
	}
		
	public BranchOperationModel createBranchOperationModel(EntryCallSequenceModel entryCallSequenceModel) {
		
		BranchOperationModel branchOperationModel = new BranchOperationModel(entryCallSequenceModel.getWorkloadIntensity(), entryCallSequenceModel.getLikelihoodOfUserGroup());
		List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();
		
		// The initial branch that contains the root node
		// The likelihood of the root branch is always 1 
		Branch rootBranch = new Branch();
		rootBranch.setBranchLikelihood(1);
		
		// Descending sort by call sequence length
		Collections.sort(userSessions, this.SortUserSessionByCallSequenceSize); 
		
		// Initialzes the root sequence with the longest call sequence
		setBranchSequence(rootBranch, userSessions.get(0).getEvents(), 0);
		
		// loops over all userSession without the first user session that initialized the rootBranch
		for(int j=1;j<userSessions.size();j++) {
			
			UserSession userSession = userSessions.get(j);	
			List<Integer> branchGuide = new ArrayList<Integer>();
			int positionInBranch = 0;
			
			for(int i=0;i<=userSession.getEvents().size();i++) {
				
				// Determines which branch is currently examined
				Branch examinedBranch = getExaminedBranch(branchGuide, rootBranch);
				
				if(i<userSession.getEvents().size()){
					
					EntryCallEvent callEvent = userSession.getEvents().get(i);
					
					// Checks if there is a match between the call event and the element in the currently examined branch
					if(checkPositionMatchInBranch(callEvent, examinedBranch, positionInBranch)) {
						incrementCountOfBranchElement(examinedBranch, positionInBranch);
						positionInBranch++;
						continue;
					}	
					
					// Checks if there is a match between the call event and a first element of a child branch 
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						int indexOfMatchingChildBranch = getIndexOfMatchingChildBranch(callEvent, examinedBranch);
						if(indexOfMatchingChildBranch>-1) {
							// Continue with the same call event but switching to the new branch
							branchGuide.add(indexOfMatchingChildBranch);
							i--;
							positionInBranch = 0;
							continue;
						}
					}
					
					// No match could be found --> Split branch into child branches
					
					// If there is already a split at that position add a new branch
					// Else split the branch into two branches
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						addNewBranch(examinedBranch);
					} else {
						splitBranch(examinedBranch, positionInBranch);
					}
					//Add branch sequence to the new branch
					int indexOfNewAddedBranch = examinedBranch.getChildBranches().size()-1;
					examinedBranch = examinedBranch.getChildBranches().get(indexOfNewAddedBranch);
					positionInBranch = 0;
					setBranchSequence(examinedBranch, userSession.getEvents(), i);
					break;
					
				
				} else {
					// End of sequence
					if(checkIfBranchSequenceTerminates(examinedBranch, positionInBranch)) {
						incrementCountOfBranchElement(examinedBranch, positionInBranch);
						break;
					}
					//Checks if there is an exit branch
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						int indexOfMatchingChildBranch = getIndexOfMatchingExitBranch(examinedBranch);
						if(indexOfMatchingChildBranch>-1) {
							// Iterate the exit state adding but switching to the new branch
							branchGuide.add(indexOfMatchingChildBranch);
							i--;
							positionInBranch = 0;
							continue;
						}
					}
					
					// No matching exit element found --> Split branch into child branches
					
					// If there is already a split at that position add a new branch
					// Else split the branch into two branches
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						addNewBranch(examinedBranch);
					} else {
						splitBranch(examinedBranch, positionInBranch);
					}
					//Add exit element to the new exit branch
					int indexOfNewAddedBranch = examinedBranch.getChildBranches().size()-1;
					examinedBranch = examinedBranch.getChildBranches().get(indexOfNewAddedBranch);
					positionInBranch = 0;
					setExitElement(examinedBranch, positionInBranch);
					break;
				}
				
			}
			
		}
		
		branchOperationModel.setRootBranch(rootBranch);
		
		return branchOperationModel;
	}
	


	private void setBranchSequence(Branch examinedBranch, List<EntryCallEvent> events, int i) {
		List<BranchElement> branchSequence = new ArrayList<BranchElement>();
		for(int j=i;j<events.size();j++) {
			EntryCallEvent callEvent = events.get(j);
			CallElement callElement = new CallElement(callEvent.getClassSignature(), callEvent.getOperationSignature());
			callElement.setAbsoluteCount(1);
//			BranchElement branchElement = new BranchElement(callElement);
//			branchSequence.add(branchElement);
			branchSequence.add(callElement);
		}
		ExitElement exitElement = new ExitElement();
		exitElement.setAbsoluteCount(1);
//		BranchElement branchElement = new BranchElement(exitElement);
//		branchSequence.add(branchElement);
		branchSequence.add(exitElement);
		examinedBranch.setBranchSequence(branchSequence);
	}

//	private void initializeRootBranchWithFirstUserSession(Branch rootBranch, UserSession userSession) {
//		List<BranchElement> initialRootBranchSequence = new ArrayList<BranchElement>();
//		for(EntryCallEvent callEvent : userSession.getEvents()) {
//			CallElement callElement = new CallElement(callEvent.getClassSignature(), callEvent.getOperationSignature());
//			callElement.setAbsoluteCount(1);
//			BranchElement branchElement = new BranchElement(callElement);
//			initialRootBranchSequence.add(branchElement);
//		}
//		ExitElement exitElement = new ExitElement();
//		exitElement.setAbsoluteCount(1);
//		BranchElement branchElement = new BranchElement(exitElement);
//		initialRootBranchSequence.add(branchElement);
//		rootBranch.setBranchSequence(initialRootBranchSequence);
//	}


	private void setExitElement(Branch examinedBranch, int positionInBranch) {
		ExitElement exitElement = new ExitElement();
		exitElement.setAbsoluteCount(1);
//		BranchElement branchElement = new BranchElement(exitElement);
		examinedBranch.getBranchSequence().add(exitElement);
	}

	private void addNewBranch(Branch examinedBranch) {
		Branch childBranch = new Branch();
		examinedBranch.addBranch(childBranch);
	}

	private void splitBranch(Branch examinedBranch, int positionInBranch) {
		Branch childBranch1 = new Branch();
		Branch childBranch2 = new Branch();
		List<BranchElement> branchSequence = new ArrayList<BranchElement>(examinedBranch.getBranchSequence().subList(0, positionInBranch));
		List<BranchElement> branchSequence1 = new ArrayList<BranchElement>(examinedBranch.getBranchSequence().subList(positionInBranch, examinedBranch.getBranchSequence().size()));
		List<BranchElement> branchSequence2 = new ArrayList<BranchElement>();
		
		examinedBranch.setBranchSequence(branchSequence);
		childBranch1.setBranchSequence(branchSequence1);
		childBranch2.setBranchSequence(branchSequence2);
		
		for(Branch childBranch : examinedBranch.getChildBranches()) {
			childBranch1.addBranch(childBranch);
		}
		examinedBranch.getChildBranches().clear();
		
		examinedBranch.addBranch(childBranch1);
		examinedBranch.addBranch(childBranch2);
	}
	
	private int getIndexOfMatchingChildBranch(EntryCallEvent callEvent, Branch examinedBranch) {
		if(examinedBranch.getChildBranches().size()==0){
			return -1;
		}
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			if(examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass().equals(CallElement.class)) {
				CallElement callElement = (CallElement) examinedBranch.getChildBranches().get(i).getBranchSequence().get(0);
				if(isCallEventCallElementMatch(callEvent, callElement))
					return i;
			}
		}
		return -1;
	}

	private int getIndexOfMatchingExitBranch(Branch examinedBranch) {
		if(examinedBranch.getChildBranches().size()==0){
			return -1;
		}
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			if(examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass().equals(ExitElement.class)) {
				return i;
			}
		}
		return -1;
	}

	private boolean checkIfBranchSequenceTerminates(Branch examinedBranch, int positionInBranch) {
		if(positionInBranch>=examinedBranch.getBranchSequence().size())
			return false;
		if(examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(ExitElement.class))
			return true;
		return false;
	}

	private void incrementCountOfBranchElement(Branch examinedBranch, int positionInBranch) {
		int absoluteCount = examinedBranch.getBranchSequence().get(positionInBranch).getAbsoluteCount() + 1;
		examinedBranch.getBranchSequence().get(positionInBranch).setAbsoluteCount(absoluteCount);		
	}
	
//	private void incrementCountOfCallElement(Branch examinedBranch, int positionInBranch) {
//		int absoluteCount = examinedBranch.getBranchSequence().get(positionInBranch).getCallElement().getAbsoluteCount() + 1;
//		examinedBranch.getBranchSequence().get(positionInBranch).getCallElement().setAbsoluteCount(absoluteCount);
//	}
	
	private Branch getExaminedBranch(List<Integer> branchGuide, Branch rootBranch) {
		Branch examinedBranch = rootBranch;
		for(int i=0;i<branchGuide.size();i++) {
			examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
		}
		return examinedBranch;
	}

	private boolean isCallEventCallElementMatch(EntryCallEvent callEvent, CallElement callElement) {
		if(callEvent.getOperationSignature().equals(callElement.getOperationSignature())&&callEvent.getClassSignature().equals(callElement.getClassSignature()))
			return true;
		else
			return false;
	}
	
	private boolean checkPositionMatchInBranch(EntryCallEvent callEvent, Branch examinedBranch, int positionInBranch) {
		if(positionInBranch>=examinedBranch.getBranchSequence().size())
			return false;
		if(!examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(CallElement.class))
			return false;
		CallElement callElement = (CallElement) examinedBranch.getBranchSequence().get(positionInBranch);
		if(isCallEventCallElementMatch(callEvent, callElement))
			return true;
		return false;
	}
	
	private boolean isPositionLastElementInBranchSequence(Branch examinedBranch, int positionInBranch) {
		if(examinedBranch.getBranchSequence().size()==positionInBranch)
			return true;
		else
			return false;
	}
	

	
	
	
	
	// Descending Sort user sessions by call sequence length
	// User session with longest call sequence will be first element
	private final Comparator<UserSession> SortUserSessionByCallSequenceSize = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			int sizeO1 = o1.getEvents().size();
			int sizeO2 = o2.getEvents().size();
			if(sizeO1 < sizeO2) {
				return 1;
			} else if(sizeO1 > sizeO2) {
				return -1;
			}
			return 0;
		}
	};

}
