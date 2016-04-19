package my.iobserve.userbehavior.modeling.branchextraction;

import my.iobserve.userbehavior.modeling.data.WorkloadIntensity;

public class BranchOperationModel {
	
	private Branch rootBranch;
	private final WorkloadIntensity workloadIntensity;
	
	public BranchOperationModel(WorkloadIntensity workloadIntensity) {
		this.workloadIntensity = workloadIntensity;
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
	
	

}
