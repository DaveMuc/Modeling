package my.iobserve.userbehavior.modeling.loopdetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import my.iobserve.userbehavior.modeling.branchextraction.CallBranchModelCreator;
import my.iobserve.userbehavior.modeling.iobservedata.EntryCallEvent;
import my.iobserve.userbehavior.modeling.iobservedata.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.iobservedata.UserSession;
import my.iobserve.userbehavior.modeling.modelingdata.Branch;
import my.iobserve.userbehavior.modeling.modelingdata.BranchElement;
import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;
import my.iobserve.userbehavior.modeling.modelingdata.CallElement;
import my.iobserve.userbehavior.modeling.modelingdata.ExitElement;
import my.iobserve.userbehavior.modeling.modelingdata.LoopBranchElement;
import my.iobserve.userbehavior.modeling.modelingdata.LoopSequenceElement;


public class CallLoopBranchModelCreator {
	
	public void detectLoopsInCallBranchModel(CallBranchModel callBranchModel) {
		// Looped branches
		setBranchTreeLevels(callBranchModel.getRootBranch(), callBranchModel.getRootBranch().getTreeLevel());
		detectLoopedBranches(callBranchModel.getRootBranch());
		
		// Loops within sequences
		detectLoopsWithinBranch(callBranchModel.getRootBranch());
		
		// Set Branch ids
		setBranchIds(callBranchModel.getRootBranch(),1);
	}
	
	private void setBranchIds(Branch branch, int branchId) {
		branch.setBranchId(branchId);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			int newBranchId = branchId +i+1;
			setBranchIds(branch.getChildBranches().get(i),newBranchId);
		}
	}
	
	private void detectLoopsWithinBranch(Branch branch) {		
		detectLoopsWithinBranchSequence(branch.getBranchSequence());
		for(int i=0;i<branch.getChildBranches().size();i++) {
			detectLoopsWithinBranch(branch.getChildBranches().get(i));
		}
	}
	
	private void detectLoopedBranches(Branch branch) {
		detectEqualBranchSegments(branch);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			detectLoopedBranches(branch.getChildBranches().get(i));
		}
	}
	
	private void setBranchTreeLevels(Branch branch, int treeLevel) {
		branch.setTreeLevel(treeLevel);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			setBranchTreeLevels(branch.getChildBranches().get(i), treeLevel+1);
		}
	}
	
	
	private void detectEqualBranchSegments(Branch branch) {
		
		List<Branch> branchesToCheck = new ArrayList<Branch>();
		boolean isPrioriSequenceEqual = true;
		
		if(branch.getChildBranches().size()==0)
			return;
		// Collect child branches that are subsequent checked for iterations
		for(int i=0;i<branch.getChildBranches().size();i++) {
			if(branch.getChildBranches().get(i).getChildBranches().size()==0||branch.getChildBranches().get(i).getChildBranches().size()!=branch.getChildBranches().size())
				return;
			branchesToCheck.add(branch.getChildBranches().get(i));
			if(isPrioriSequenceEqual)
				isPrioriSequenceEqual = isPrioriSequenceEqual(branch.getBranchSequence(), branch.getChildBranches().get(i).getBranchSequence());
		}
		
		boolean areLoopsDetected = detectBranchLoopsWithPostSequences(branch, branchesToCheck);
		if(isPrioriSequenceEqual&&!areLoopsDetected)
			detectBranchLoopsWithPreSequence(branch, branchesToCheck);
	
	}
	
	private void detectBranchLoopsWithPreSequence(Branch branch, List<Branch> branchesToCheck) {
		// TODO Auto-generated method stub
		
	}

	private boolean detectBranchLoopsWithPostSequences(Branch branch, List<Branch> branchesToCheck) {
		
		List<Branch> branchesAfterTheIteratedBranches = new ArrayList<Branch>();
		//	BranchElement subsequentElementToCheck = null;
		int treeLevel = branch.getTreeLevel();
		int countOfLoopedBranches = 1;
		boolean treeLevelIsEqual = true;
		boolean sequenceIsPrefix = true;
		
		while(treeLevelIsEqual) {
		
			treeLevel++;
			List<Branch> branchesOfTreeLevel = new ArrayList<Branch>();
			branchesOfTreeLevel = getBranchesOfTreeLevel(branch, treeLevel, branchesOfTreeLevel);
			if(branchesOfTreeLevel==null||branchesOfTreeLevel.size()==0) {
				treeLevelIsEqual = false;
				sequenceIsPrefix = false;
			}
			
			for(int j=0;j<branchesOfTreeLevel.size();j++) {
				if(branchesOfTreeLevel.get(j).getChildBranches().size()!=branchesToCheck.size()) {
					treeLevelIsEqual = false;
					sequenceIsPrefix = false;
					break;
				}
				boolean matchingSequenceFound = false;
				for(int s=0;s<branchesOfTreeLevel.get(j).getChildBranches().size();s++) {
					matchingSequenceFound = false;
					for(int k=0;k<branchesToCheck.size();k++) {
						if(doBranchesMatch(branchesOfTreeLevel.get(j).getChildBranches().get(s), branchesToCheck.get(k))) {
							if(branchesOfTreeLevel.get(j).getChildBranches().get(s).getBranchSequence().size()!=branchesToCheck.get(k).getBranchSequence().size()) {
								treeLevelIsEqual = false;
//								if(subsequentElementToCheck==null) {
//									subsequentElementToCheck = branchesOfTreeLevel.get(j).getChildBranches().get(s).getBranchSequence().get(branchesToCheck.get(k).getBranchSequence().size());
//								} else if(!areBranchElementsEqual(branchesOfTreeLevel.get(j).getChildBranches().get(s).getBranchSequence().get(branchesToCheck.get(k).getBranchSequence().size()), subsequentElementToCheck))
//									return;
							}
						matchingSequenceFound = true;
						break;
						}
					}
					if(!matchingSequenceFound) {
						sequenceIsPrefix = false;
						treeLevelIsEqual = false;
						break;
					} 
				}
				if(!matchingSequenceFound) {
					sequenceIsPrefix = false;
					treeLevelIsEqual = false;
					break;
				}
			}
			
			if(sequenceIsPrefix) 
				countOfLoopedBranches++;
			
			if(sequenceIsPrefix&&!treeLevelIsEqual&&countOfLoopedBranches>1) {
				branchesAfterTheIteratedBranches = getBranchesOfTreeLevel(branch, treeLevel+1, branchesAfterTheIteratedBranches);
				reconstructBranch(branch, countOfLoopedBranches, branchesAfterTheIteratedBranches, branchesToCheck);
				return true;
			} else if(!sequenceIsPrefix&&countOfLoopedBranches>1) {
				List<Branch> branchesOfFatherTreeLevel = new ArrayList<Branch>();
				branchesOfFatherTreeLevel = getBranchesOfTreeLevel(branch, treeLevel-1, branchesOfFatherTreeLevel);
				if(doChildBranchesMatch(branchesOfFatherTreeLevel)) {
					branchesAfterTheIteratedBranches = getBranchesOfTreeLevel(branch, treeLevel+1, branchesAfterTheIteratedBranches);
					reconstructBranch(branch, countOfLoopedBranches, branchesAfterTheIteratedBranches, branchesToCheck);
					return true;
				}
			}
				
		}
		return false;
	}
	
	private void reconstructBranch(Branch branch, int countOfLoopedBranches, List<Branch> branchesAfterTheIteratedBranches, List<Branch> branchesToCheck) {
		LoopBranchElement loopBranch = new LoopBranchElement();
		loopBranch.setLoopCount(countOfLoopedBranches);
		LoopSequenceElement loopSequence = new LoopSequenceElement();
		
		int indexCount = 0;
		for(int k=branch.getChildBranches().get(0).getBranchSequence().size()-1;k==1;k--){
			BranchElement branchElement1 = branch.getChildBranches().get(0).getBranchSequence().get(k);
			boolean elementsAreEqual = true;
			for(int i=1;i<branch.getChildBranches().size();i++) {
				int consideredIndex = branch.getChildBranches().get(i).getBranchSequence().size()-1-indexCount;
				BranchElement branchElement2 = branch.getChildBranches().get(i).getBranchSequence().get(consideredIndex);
				if(!areBranchElementsEqual(branchElement1, branchElement2)){
					elementsAreEqual = false;
					break;
				}
			}
			if(elementsAreEqual) {
				for(int i=0;i<branch.getChildBranches().size();i++) {
					branch.getChildBranches().get(i).getBranchSequence().remove(branch.getChildBranches().get(i).getBranchSequence().size()-1);
				}
				loopSequence.addCallElementToLoopSequence(0, branchElement1);
			} else {
				break;
			}
			indexCount++;
		}
		
		for(int i=0;i<branch.getChildBranches().size();i++) {
			loopBranch.addBranchToLoopBranch(branch.getChildBranches().get(i));
		}
		if(indexCount>0){
			loopSequence.addCallElementToLoopSequence(0,loopBranch);
			branch.getBranchSequence().add(loopSequence);
		} else {
			branch.getBranchSequence().add(loopBranch);
		}
		
		branch.getChildBranches().clear();
		for(int i=0;i<branchesAfterTheIteratedBranches.size();i++) {
			for(int j=0;j<branchesToCheck.size();j++) {
				if(areBranchElementsEqual(branchesToCheck.get(j).getBranchSequence().get(0),branchesAfterTheIteratedBranches.get(i).getBranchSequence().get(0))) {
					for(int k=0;k<branchesToCheck.get(j).getBranchSequence().size();k++) {
						branchesAfterTheIteratedBranches.get(i).getBranchSequence().remove(0);
					}
					break;
				}
			}
		}
		
		EntryCallSequenceModel entryCallSequenceModel = getEntryCallSequenceModelFromBranches(branchesAfterTheIteratedBranches);
		CallBranchModelCreator callBranchModelCreator = new CallBranchModelCreator();
		CallBranchModel callBranchModel = callBranchModelCreator.createCallBranchModel(entryCallSequenceModel);
		callBranchModelCreator.calculateLikelihoodsOfBranches(callBranchModel);
		Branch branchToAdd = callBranchModel.getRootBranch();
		
		for(int i=0;i<branchToAdd.getBranchSequence().size();i++) {
			branch.getBranchSequence().add(branchToAdd.getBranchSequence().get(i));
		}
		for(int i=0;i<branchToAdd.getChildBranches().size();i++) {
			branch.getChildBranches().add(branchToAdd.getChildBranches().get(i));
		}
	}
	
	private EntryCallSequenceModel getEntryCallSequenceModelFromBranches(List<Branch> branchesAfterTheIteratedBranches) {
		List<UserSession> userSessions = new ArrayList<UserSession>();
		for(Branch branch : branchesAfterTheIteratedBranches) {
			List<UserSession> userSessionsOfBranch = new ArrayList<UserSession>();
			List<EntryCallEvent> events = new ArrayList<EntryCallEvent>();
			userSessionsOfBranch = getUserSessionsFromBranch(branch, events, userSessionsOfBranch);
			userSessions.addAll(userSessionsOfBranch);
		}
		
		return new EntryCallSequenceModel(userSessions);
	}
	
	private List<UserSession> getUserSessionsFromBranch(Branch branch,List<EntryCallEvent> events, List<UserSession> userSessions) {

		for(int i=0;i<branch.getBranchSequence().size();i++) {
			if(branch.getBranchSequence().get(i).getClass().equals(CallElement.class)) {
				EntryCallEvent entryCallEvent = new EntryCallEvent(0,0,0,branch.getBranchSequence().get(i).getOperationSignature(),branch.getBranchSequence().get(i).getClassSignature(),"","");
				events.add(entryCallEvent); 
			}
		}
		for(int i=0;i<branch.getChildBranches().size();i++) {
			userSessions = getUserSessionsFromBranch(branch.getChildBranches().get(i),events,userSessions);
		}
		if(branch.getChildBranches().size()==0) {
			UserSession userSession = new UserSession();
			for(EntryCallEvent event : events) {
				userSession.add(event);
			}
			userSessions.add(userSession);
		}
		
		return userSessions;
	}

	private boolean doChildBranchesMatch(List<Branch> branches) {
		List<Branch> branchesToMatch = new ArrayList<Branch>();
		for(int i=0;i<branches.get(0).getChildBranches().size();i++) {
			branchesToMatch.add(branches.get(0).getChildBranches().get(i));
		}
		for(int i=0;i<branches.size();i++) {
			if(branches.get(i).getChildBranches().size()!=branchesToMatch.size())
				return false;
			for(int k=0;k<branchesToMatch.size();k++) {
				boolean matchingElementFound = false;
				for(int j=0;j<branches.get(i).getChildBranches().size();j++) {
					if(areBranchElementsEqual(branchesToMatch.get(k).getBranchSequence().get(0),branches.get(i).getChildBranches().get(j).getBranchSequence().get(0))
							&&branchesToMatch.get(k).getBranchLikelihood()==branches.get(i).getChildBranches().get(j).getBranchLikelihood()) {
						matchingElementFound = true;
						break;
					}
				}
				if(!matchingElementFound) {
					return false;
				}
			}	
		}

		return true;
	}

	private List<Branch> getBranchesOfTreeLevel(Branch branch, int treeLevel, List<Branch> childBranches) {
		
		if(branch.getTreeLevel()==treeLevel) 
			childBranches.add(branch);	
		for(int i=0;i<branch.getChildBranches().size();i++) {
			if(branch.getChildBranches().get(i).getTreeLevel()>treeLevel)
				break;
			childBranches = getBranchesOfTreeLevel(branch.getChildBranches().get(i), treeLevel, childBranches);
		}
		
		return childBranches;
	}
	
	private boolean isPrioriSequenceEqual(List<BranchElement> branchSequence, List<BranchElement> childbranchSequence) {
		
		if(branchSequence.size()<(childbranchSequence.size()-1)||childbranchSequence.size()<=1)
			return false;
		
		int branchSequenceIndex = 1;
		for(int i=childbranchSequence.size()-1;i==1;i--) {
			if(!areBranchElementsEqual(childbranchSequence.get(i),branchSequence.get(branchSequence.size()-branchSequenceIndex)))
				return false;
			branchSequenceIndex++;
		}
		
		return true;
	}
	
	
	private boolean areBranchElementsEqual(BranchElement branchElement1, BranchElement branchElement2) {
		if(!branchElement1.getClass().equals(branchElement2.getClass()))
			return false;
		if(branchElement1.getClass().equals(CallElement.class)&&branchElement2.getClass().equals(CallElement.class)) {
			if((!branchElement1.getClassSignature().equals(branchElement2.getClassSignature()))||
					(!branchElement1.getOperationSignature().equals(branchElement2.getOperationSignature())))
				return false;
		}
		return true;
	}
		
	
	private boolean doBranchesMatch(Branch branch1, Branch branch2) {
		if(branch1.getBranchSequence().size()<branch2.getBranchSequence().size())
			return false;
		if(branch1.getBranchLikelihood()!=branch2.getBranchLikelihood())
			return false;
		for(int i=0;i<branch2.getBranchSequence().size();i++) {
			BranchElement branchElementOfSequence1 = branch1.getBranchSequence().get(i);
			BranchElement branchElementOfSequence2 = branch2.getBranchSequence().get(i);
			if(!branchElementOfSequence1.getClass().equals(branchElementOfSequence2.getClass()))
				return false;
			if(branchElementOfSequence1.getClass().equals(CallElement.class)&&branchElementOfSequence2.getClass().equals(CallElement.class)) {
				if (!(branchElementOfSequence1.getClassSignature().equals(branchElementOfSequence2.getClassSignature())) ||
				!(branchElementOfSequence1.getOperationSignature().equals(branchElementOfSequence2.getOperationSignature())))
					return false;
			}			
		}
		return true;
	}
	
	
	/**
	 * Detects loops within a branch sequence
	 * 1. 	Finds all iterated segments within the given sequence 
	 * 2.	For each obtained segment the loop count is determined
	 * 3. 	The loops are examined if they overlap 
	 * 		If there is an overlap between two loops, the loop that replaces less sequence elements is filtered out
	 * 4.	The loop elements are inserted into the sequence
	 * 
	 * Iterates until no new loops are found
	 * @param branch
	 */
	private void detectLoopsWithinBranchSequence(List<BranchElement> branchSequence) {
		
		if(!(branchSequence.size()>0))
			return;
		
		List<LoopSequenceElement> loopElements = new ArrayList<LoopSequenceElement>();
		
		do{
			loopElements.clear();
			
//			List<BranchElement> branchSequence = new ArrayList<BranchElement>();
//			branchSequence = branch.getBranchSequence();
			
			int branchSequenceSizeWithoutExitElement = branchSequence.size();
			if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
				branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
			if(branchSequence==null||branchSequenceSizeWithoutExitElement<2) {
				return;
			}
				
			for(int indexOfElementInElementList=0;indexOfElementInElementList<branchSequenceSizeWithoutExitElement;indexOfElementInElementList++) {
				
				// If the branch element is a loop sequence element, it is checked for loops within the loop
				if(branchSequence.get(indexOfElementInElementList).getClass().equals(LoopSequenceElement.class)) {
					LoopSequenceElement loopi = (LoopSequenceElement) branchSequence.get(indexOfElementInElementList);
					detectLoopsWithinBranchSequence(loopi.getLoopSequence());
					continue;
				}
				// If the branch element is a loop branch element, it is checked for loops within the sequences of the branches
				if(branchSequence.get(indexOfElementInElementList).getClass().equals(LoopBranchElement.class)) {
					LoopBranchElement loopi = (LoopBranchElement) branchSequence.get(indexOfElementInElementList);
					for(int i=0;i<loopi.getLoopBranches().size();i++) {
						detectLoopsWithinBranchSequence(loopi.getLoopBranches().get(i).getBranchSequence());
					}
					continue;
				}
				
				for(int i=0;i<((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2);i++) {
					
					int elementsToCheck = ((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2) - i;
					
					boolean isALoop = true;
					LoopSequenceElement loopElement = new LoopSequenceElement();
					for(int k=0;k<elementsToCheck;k++) {
						if(!branchSequence.get(indexOfElementInElementList+k).getClass().equals(CallElement.class)||!branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getClass().equals(CallElement.class)) {
							isALoop = false;
							break;
						}
						if(!branchSequence.get(indexOfElementInElementList+k).getOperationSignature().equals(branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getOperationSignature())) {
							isALoop = false;
							break;
						} 
						if(branchSequence.get(indexOfElementInElementList+k).getClass().equals(CallElement.class)){
							loopElement.addCallElementToLoopSequence((CallElement) branchSequence.get(indexOfElementInElementList+k));
						}
					}
					
					if(isALoop) {
						
						loopElement.setStartIndexInBranchSequence(indexOfElementInElementList);
						loopElement.setEndIndexInBranchSequence(indexOfElementInElementList+elementsToCheck+elementsToCheck-1);

						// Determines the number of loops for each loop
						determineLoopCount(loopElement, branchSequence);
						
						loopElements.add(loopElement);
						
					}
	
				}
				
			}
			
			if(loopElements.size()>0) {
				// Filters the loops by overlaps
				filterLoops(loopElements);
				// Inserts the loops into the branch
				insertLoopsIntoBranch(loopElements, branchSequence);
			}
			
//			for(LoopElement loopElement : loopElements) {
//				detectLoopsInBranchSequence(loopElement.getLoopSequence());
//			}
		
		}while(loopElements.size()!=0);
	
	}
	
	/**
	 * Inserts the loopElements into the branch sequence and removes the replaced elements from
	 * the sequence that are contained within the loop element
	 * @param loopElements
	 * @param branch
	 */
	private void insertLoopsIntoBranch(List<LoopSequenceElement> loopElements, List<BranchElement> branchSequence) {
		
		Collections.sort(loopElements, this.SortLoopElementsByStartIndex); 
		
		for(int i=0;i<loopElements.size();i++) {
			LoopSequenceElement loopElement = loopElements.get(i);
			loopElement.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+i);
			loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+1);
			branchSequence.add(loopElement.getStartIndexInBranchSequence(), loopElement);
		}
		
		int countOfRemovedElementsFromLoopsBefore = 0;
		for(LoopSequenceElement loopElement : loopElements) {
			for(int i=0;i<loopElement.getNumberOfReplacedElements();i++) {
				int indexToRemove = loopElement.getStartIndexInBranchSequence()+1-countOfRemovedElementsFromLoopsBefore;
				branchSequence.remove(indexToRemove);
			}
			countOfRemovedElementsFromLoopsBefore += loopElement.getNumberOfReplacedElements();
		}
		
	}

	/**
	 * Checks if there are overlaps between the loops
	 * @param loopElements
	 */
	private void filterLoops(List<LoopSequenceElement> loopElements) {
		
		for(int i=0;i<loopElements.size();i++) {
			
			for(int j=i+1;j<loopElements.size();j++) {
				LoopSequenceElement loopElement1 = loopElements.get(i);
				LoopSequenceElement loopElement2 = loopElements.get(j);
				
				// Checks if there is an overlap between two loops
				if((loopElement1.getStartIndexInBranchSequence()>=loopElement2.getStartIndexInBranchSequence()&&loopElement1.getStartIndexInBranchSequence()<=loopElement2.getEndIndexInBranchSequence())
						||(loopElement1.getEndIndexInBranchSequence()>=loopElement2.getStartIndexInBranchSequence()&&loopElement1.getEndIndexInBranchSequence()<=loopElement2.getEndIndexInBranchSequence())
						||(loopElement2.getStartIndexInBranchSequence()>=loopElement1.getStartIndexInBranchSequence()&&loopElement2.getStartIndexInBranchSequence()<=loopElement1.getEndIndexInBranchSequence())
						||(loopElement2.getEndIndexInBranchSequence()>=loopElement1.getStartIndexInBranchSequence()&&loopElement2.getEndIndexInBranchSequence()<=loopElement1.getEndIndexInBranchSequence())){
					
					// Returns the removed loop 
					boolean loop1Weaker = removeTheWeakerLoop(loopElements, loopElement1, loopElement2, i, j);
					
					// Iterate loop considering the removing
					if(loop1Weaker) {
						i--;
						break;	
					} else {
						j--;
					}
				}
			}
		}
	}

	/**
	 * Removes the loop that replaces less sequence elements
	 * @param loopElements
	 */
	private boolean removeTheWeakerLoop(List<LoopSequenceElement> loopElements, LoopSequenceElement loopElement1,
			LoopSequenceElement loopElement2, int indexOfLoop1, int indexOfLoop2) {
		
		if(loopElement1.getNumberOfReplacedElements()>loopElement2.getNumberOfReplacedElements()) {
			loopElements.remove(indexOfLoop2);
			return false;
		} else if(loopElement2.getNumberOfReplacedElements()>loopElement1.getNumberOfReplacedElements()) {
			loopElements.remove(indexOfLoop1);
			return true;
		} else if(loopElement1.getLoopSequence().size()<=loopElement2.getLoopSequence().size()) {
			loopElements.remove(indexOfLoop2);
			return false;
		} else {
			loopElements.remove(indexOfLoop1);
			return true;
		}
		
	}

	/**
	 * Determines the loop count by checking how often the loop is iterated in a row
	 * @param loopElement
	 * @param branchSequence
	 */
	private void determineLoopCount(LoopSequenceElement loopElement, List<BranchElement> branchSequence) {
				
		int loopCount = 2;
		for(int i=loopElement.getEndIndexInBranchSequence();i+loopElement.getLoopSequence().size()<branchSequence.size();i=i+loopElement.getLoopSequence().size()) {
			boolean isAdditionalLoop = true;
			for(int j=0;j<loopElement.getLoopSequence().size();j++){
				if(!branchSequence.get(loopElement.getStartIndexInBranchSequence()+j).getOperationSignature().equals(branchSequence.get(i+1+j).getOperationSignature())) {
					isAdditionalLoop = false;
					break;
				}
			}
			if(isAdditionalLoop)
				loopCount++;
			else
				break;
		}
		loopElement.setLoopCount(loopCount);
		loopElement.setNumberOfReplacedElements(loopElement.getLoopCount()*loopElement.getLoopSequence().size());
		loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+loopElement.getNumberOfReplacedElements()-1);
	}
	

	/**
	 * Comporator to sort the loopElements according to their position in the sequence
	 */
	private final Comparator<LoopSequenceElement> SortLoopElementsByStartIndex = new Comparator<LoopSequenceElement>() {
		
		@Override
		public int compare(final LoopSequenceElement e1, final LoopSequenceElement e2) {
			int index1 = e1.getStartIndexInBranchSequence();
			int index2 = e2.getStartIndexInBranchSequence();
			if(index1 > index2) {
				return 1;
			} else if(index1 < index2) {
				return -1;
			}
			return 0;
		}
	};

}
