package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.ArrayList;
import java.util.List;

public class Branch {
	
	private List<BranchElement> branchSequence;
	private double branchLikelihood;
	private List<Branch> childBranches;
	private int loopCount = 1;
	
	public Branch() {
		branchSequence = new ArrayList<BranchElement>();
		childBranches = new ArrayList<Branch>();
	}
	
	public void addBranch(Branch childBranch) {
		childBranches.add(childBranch);
	}
	
	public List<BranchElement> getBranchSequence() {
		return branchSequence;
	}

	public void setBranchSequence(List<BranchElement> branchSequence) {
		this.branchSequence = branchSequence;
	}

	public double getBranchLikelihood() {
		return branchLikelihood;
	}

	public void setBranchLikelihood(double branchLikelihood) {
		this.branchLikelihood = branchLikelihood;
	}

	public List<Branch> getChildBranches() {
		return childBranches;
	}

	public void setChildBranches(List<Branch> childBranches) {
		this.childBranches = childBranches;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}
	
	
	
	
	
}
