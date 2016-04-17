package my.iobserve.userbehavior.modeling.data;

import java.util.List;

import my.iobserve.userbehavior.modeling.branchextraction.Branch;

public class UserBehaviorModel {
	
	// input from iObserve 
	private EntryCallSequenceModel entryCallSequenceModel;
	
	// result of the user group extraction
	private List<EntryCallSequenceModel> entryCallSequenceModels;

	// result of the branch extraction
	private List<Branch> branchOperationModels;
	
	
	public EntryCallSequenceModel getEntryCallSequenceModel() {
		return entryCallSequenceModel;
	}

	public void setEntryCallSequenceModel(EntryCallSequenceModel entryCallSequenceModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
	}

	public List<EntryCallSequenceModel> getEntryCallSequenceModels() {
		return entryCallSequenceModels;
	}

	public void setEntryCallSequenceModels(List<EntryCallSequenceModel> entryCallSequenceModels) {
		this.entryCallSequenceModels = entryCallSequenceModels;
	}

	public List<Branch> getBranchOperationModels() {
		return branchOperationModels;
	}

	public void setBranchOperationModels(List<Branch> branchOperationModels) {
		this.branchOperationModels = branchOperationModels;
	}

	
	
}
