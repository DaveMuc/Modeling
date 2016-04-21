package my.iobserve.userbehavior.modeling.branchextraction;

import my.iobserve.userbehavior.modeling.data.WorkloadIntensity;

public class BranchOperationModel {
	
	private Branch rootBranch;
	private final WorkloadIntensity workloadIntensity;
	private final double likelihoodOfUserGroup;
	
	public BranchOperationModel(WorkloadIntensity workloadIntensity, double likelihoodOfUserGroup) {
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
		

}
