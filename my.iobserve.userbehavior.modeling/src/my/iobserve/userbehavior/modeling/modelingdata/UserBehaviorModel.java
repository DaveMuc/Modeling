package my.iobserve.userbehavior.modeling.modelingdata;

import java.util.List;

import my.iobserve.userbehavior.modeling.iobservedata.EntryCallSequenceModel;

public class UserBehaviorModel {
	
	// input from iObserve 
	private EntryCallSequenceModel entryCallSequenceModel;
	
	// result of the user group extraction
	private List<EntryCallSequenceModel> entryCallSequenceModels;

	// result of the branch extraction
	private List<CallBranchModel> callBranchModels;
	
	// result of the loop detection
	private List<CallBranchModel> callLoopBranchModels;
	
	
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

	public List<CallBranchModel> getBranchOperationModels() {
		return callBranchModels;
	}

	public void setBranchOperationModels(List<CallBranchModel> branchOperationModels) {
		this.callBranchModels = branchOperationModels;
	}

	
	
}
