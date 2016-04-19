package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;

public class BranchExtraction {
	
	private List<EntryCallSequenceModel> entryCallSequenceModels;
	private List<BranchOperationModel> branchOperationModels = null;
	
	public BranchExtraction(List<EntryCallSequenceModel> entryCallSequenceModels) {
		this.entryCallSequenceModels = entryCallSequenceModels;
	}
	
	public void createBranchOperationModel() {
		
		BranchOperationModelCreator modelCreator = new BranchOperationModelCreator();
		branchOperationModels = new ArrayList<BranchOperationModel>();
		
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			BranchOperationModel branchOperationModel = modelCreator.createBranchOperationModel(entryCallSequenceModel);
			branchOperationModels.add(branchOperationModel);
		}
		
	}

	public List<BranchOperationModel> getBranchOperationModels() {
		return branchOperationModels;
	}

}
