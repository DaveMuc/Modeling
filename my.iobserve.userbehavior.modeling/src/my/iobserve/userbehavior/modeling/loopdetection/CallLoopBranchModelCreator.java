package my.iobserve.userbehavior.modeling.loopdetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import my.iobserve.userbehavior.modeling.modelingdata.Branch;
import my.iobserve.userbehavior.modeling.modelingdata.BranchElement;
import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;
import my.iobserve.userbehavior.modeling.modelingdata.CallElement;
import my.iobserve.userbehavior.modeling.modelingdata.ExitElement;
import my.iobserve.userbehavior.modeling.modelingdata.LoopElement;


public class CallLoopBranchModelCreator {
	
	public CallBranchModel detectLoopsInCallBranchModel(CallBranchModel callBranchModel) {
		
		List<Integer> branchGuide = new ArrayList<Integer>();
		List<Integer> processedBranchIds = new ArrayList<Integer>();
		
		// Iterates over all branches within the callBranchModel
		while(processedBranchIds.size()<callBranchModel.getNumberOfBranches()) {
			
			Branch examinedBranch = callBranchModel.getExaminedBranch(branchGuide);
			if(!processedBranchIds.contains(examinedBranch.getBranchId())) {
				
				// Start of loop detection for this branch
				detectLoopsInBranchSequence(examinedBranch);
				
				processedBranchIds.add(examinedBranch.getBranchId());
			}
			
			int startBranchGuideSize = branchGuide.size(); 
			for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
				if(!processedBranchIds.contains(examinedBranch.getChildBranches().get(i).getBranchId())){
					branchGuide.add(i);
					break;
				}
			}
			int endBranchGuideSize = branchGuide.size();
			if(endBranchGuideSize==startBranchGuideSize&&branchGuide.size()>0)
				branchGuide.remove(branchGuide.size()-1);
			
		}
		
		return callBranchModel;				
	}
	
	
	/**
	 * Detects loops within a branch sequence
	 * Extracts the loops that replace the most sequence elements
	 * Iterates until no new loops are found
	 * @param branch
	 */
	private void detectLoopsInBranchSequence(Branch branch) {
		
		List<LoopElement> loopElements = new ArrayList<LoopElement>();
		
		do{
			loopElements.clear();
			
			List<BranchElement> branchSequence = new ArrayList<BranchElement>();
			branchSequence = branch.getBranchSequence();
			
			int branchSequenceSizeWithoutExitElement = branchSequence.size();
			if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
				branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
			if(branchSequence==null||branchSequenceSizeWithoutExitElement<2) {
				return;
			}
				
			for(int indexOfElementInElementList=0;indexOfElementInElementList<branchSequenceSizeWithoutExitElement;indexOfElementInElementList++) {
				
				for(int i=0;i<((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2);i++) {
					
					int elementsToCheck = ((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2) - i;
					
					boolean isALoop = true;
					LoopElement loopElement = new LoopElement();
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
			
			// Filters the loops by overlaps
			filterLoops(loopElements);
			// Inserts the loops into the branch
			insertLoopsIntoBranch(loopElements, branch);
		
		}while(loopElements.size()!=0);
	
	}
	
	/**
	 * Inserts the loopElements into the branch sequence and removes the replaced elements from
	 * the sequence that are contained within the loop element
	 * @param loopElements
	 * @param branch
	 */
	private void insertLoopsIntoBranch(List<LoopElement> loopElements, Branch branch) {
		
		Collections.sort(loopElements, this.SortLoopElementsByStartIndex); 
		
		for(int i=0;i<loopElements.size();i++) {
			LoopElement loopElement = loopElements.get(i);
			loopElement.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+i);
			loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+1);
			branch.getBranchSequence().set(loopElement.getStartIndexInBranchSequence(), loopElement);
		}
		for(LoopElement loopElement : loopElements) {
			for(int i=0;i<loopElement.getNumberOfReplacedElements()-1;i++) {
				int indexToRemove = loopElement.getStartIndexInBranchSequence()+1;
				branch.getBranchSequence().remove(indexToRemove);
			}
			
		}
		
	}

	/**
	 * Checks if there are overlaps between the loops
	 * @param loopElements
	 */
	private void filterLoops(List<LoopElement> loopElements) {
		
		for(int i=0;i<loopElements.size();i++) {
			
			for(int j=i+1;j<loopElements.size();j++) {
				LoopElement loopElement1 = loopElements.get(i);
				LoopElement loopElement2 = loopElements.get(j);
				
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
	private boolean removeTheWeakerLoop(List<LoopElement> loopElements, LoopElement loopElement1,
			LoopElement loopElement2, int indexOfLoop1, int indexOfLoop2) {
		
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
	private void determineLoopCount(LoopElement loopElement, List<BranchElement> branchSequence) {
				
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
	private final Comparator<LoopElement> SortLoopElementsByStartIndex = new Comparator<LoopElement>() {
		
		@Override
		public int compare(final LoopElement e1, final LoopElement e2) {
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
