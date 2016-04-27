package my.iobserve.userbehavior.modeling.modelingdata;

import java.util.ArrayList;
import java.util.List;

public class LoopElement implements BranchElement {
	
	private List<CallElement> loopSequence;
	private int loopCount;
	private int startIndexInBranchSequence;
	private int endIndexInBranchSequence;
	private int numberOfReplacedElements;
	
	public LoopElement() {
		loopSequence = new ArrayList<CallElement>();
	}
	
	public void addCallElementToLoopSequence(CallElement callElement) {
		loopSequence.add(callElement);
	}

	public List<CallElement> getLoopSequence() {
		return loopSequence;
	}

	public void setLoopSequence(List<CallElement> loopSequence) {
		this.loopSequence = loopSequence;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public int getStartIndexInBranchSequence() {
		return startIndexInBranchSequence;
	}

	public void setStartIndexInBranchSequence(int startIndexInBranchSequence) {
		this.startIndexInBranchSequence = startIndexInBranchSequence;
	}

	public int getEndIndexInBranchSequence() {
		return endIndexInBranchSequence;
	}

	public void setEndIndexInBranchSequence(int endIndexInBranchSequence) {
		this.endIndexInBranchSequence = endIndexInBranchSequence;
	}

	public int getNumberOfReplacedElements() {
		return numberOfReplacedElements;
	}

	public void setNumberOfReplacedElements(int numberOfReplacedElements) {
		this.numberOfReplacedElements = numberOfReplacedElements;
	}

	@Override
	public int getAbsoluteCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAbsoluteCount(int absoluteCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClassSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOperationSignature() {
		// TODO Auto-generated method stub
		return null;
	}



	
	
	
	
	

}
