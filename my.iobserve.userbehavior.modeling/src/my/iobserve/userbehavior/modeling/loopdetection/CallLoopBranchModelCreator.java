package my.iobserve.userbehavior.modeling.loopdetection;

import java.util.ArrayList;
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
		
		while(processedBranchIds.size()<callBranchModel.getNumberOfBranches()) {
			
			Branch examinedBranch = getExaminedBranch(branchGuide, callBranchModel.getRootBranch());
			if(!processedBranchIds.contains(examinedBranch.getBranchId())) {
				// DO SOMETHING
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
	
	private void detectLoopsInBranchSequence(Branch branch) {
		
		
		List<BranchElement> branchSequence = branch.getBranchSequence();
		
		if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
			branchSequence.remove(branchSequence.size()-1);
		
		if(branchSequence==null||branchSequence.size()<2) {
			return;
		}
		
		List<LoopElement> loopElements = new ArrayList<LoopElement>();
		
//		int indexOfElementInElementList = 0;
		
		for(int indexOfElementInElementList=0;indexOfElementInElementList<branchSequence.size();indexOfElementInElementList++) {
			
			for(int i=0;i<((branchSequence.size()-indexOfElementInElementList)/2);i++) {
				
				int elementsToCheck = ((branchSequence.size()-indexOfElementInElementList)/2) - i;
				String headString = "";
				String tailString = "";
				
				boolean isALoop = true;
				LoopElement loopElement = new LoopElement();
				for(int k=0;k<elementsToCheck;k++) {
//					headString = headString+branchSequence.get(indexOfElementInElementList+k).getOperationSignature()+"-";
//					tailString = tailString+branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getOperationSignature()+"-";
//					if(!headString.equals(tailString)) {
//						break;
//					}
					if(!branchSequence.get(indexOfElementInElementList+k).getOperationSignature().equals(branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getOperationSignature())) {
						isALoop = false;
						break;
					} 
					if(branchSequence.get(indexOfElementInElementList+k).getClass().equals(CallElement.class)){
						loopElement.addCallElementToLoopSequence((CallElement) branchSequence.get(indexOfElementInElementList+k));
					}
				}
				
				if(isALoop) {
					String loopString = "";
					for(CallElement callElement : loopElement.getLoopSequence()) {
						loopString = loopString + callElement.getOperationSignature();
					}
					System.out.println(loopString);
					loopElement.setStartIndexInBranchSequence(indexOfElementInElementList);
					loopElement.setEndIndexInBranchSequence(indexOfElementInElementList+elementsToCheck+elementsToCheck-1);
					loopElement.setNumberOfReplacedElements(loopElement.getEndIndexInBranchSequence()-loopElement.getStartIndexInBranchSequence()+1);
					loopElements.add(loopElement);
					/**
					 * TODO Determine loop Count
					 */
				}
				
//				if(headString.equals(tailString)) {
//					System.out.println(headString);
//					// check element after looped elements
//					// indexOfElementInElementList = indexOfElementInElementList+elementsToCheck+elementsToCheck-1;
//					break;
//				}

			}
			
//			indexOfElementInElementList++;
		}
		
		/**
		 * TODO Insert Loops into branch sequence
		 */
	
		int k = 0;
		k = k + 1;
	}
	
	private Branch getExaminedBranch(List<Integer> branchGuide, Branch rootBranch) {
		Branch examinedBranch = rootBranch;
		for(int i=0;i<branchGuide.size();i++) {
			examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
		}
		return examinedBranch;
	}

}
