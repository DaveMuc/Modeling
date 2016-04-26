package my.iobserve.userbehavior.modeling.modelingdata;

public class CallBranchModel {
	
	private Branch rootBranch;
	private final WorkloadIntensity workloadIntensity;
	private final double likelihoodOfUserGroup;
	private int numberOfBranches;
	
	public CallBranchModel(WorkloadIntensity workloadIntensity, double likelihoodOfUserGroup) {
		this.workloadIntensity = workloadIntensity;
		this.likelihoodOfUserGroup = likelihoodOfUserGroup;
	}

	public Branch getRootBranch() {
		return rootBranch;
	}

	public void setRootBranch(Branch rootBranch) {
		this.rootBranch = rootBranch;
	}

	public WorkloadIntensity getWorkloadIntensity() {
		return workloadIntensity;
	}

	public double getLikelihoodOfUserGroup() {
		return likelihoodOfUserGroup;
	}

	public int getNumberOfBranches() {
		return numberOfBranches;
	}

	public void setNumberOfBranches(int numberOfBranches) {
		this.numberOfBranches = numberOfBranches;
	}
	
	
		

}
