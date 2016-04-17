package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;

public class BranchExtraction {
	
	private List<EntryCallSequenceModel> entryCallSequenceModels;
	private List<Branch> branchOperationModels = null;
	
	public BranchExtraction(List<EntryCallSequenceModel> entryCallSequenceModels) {
		this.entryCallSequenceModels = entryCallSequenceModels;
	}
	
	public void createBranchOperationModel() {
		
		BranchOperationModelCreator modelCreator = new BranchOperationModelCreator();
		
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			Branch branchOperationModel = modelCreator.createBranchOperationModel(entryCallSequenceModel);
			branchOperationModels.add(branchOperationModel);
		}
		
	}

	public List<Branch> getBranchOperationModels() {
		return branchOperationModels;
	}

}
