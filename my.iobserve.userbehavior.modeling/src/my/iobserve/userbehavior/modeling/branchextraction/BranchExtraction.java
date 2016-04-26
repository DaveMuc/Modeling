package my.iobserve.userbehavior.modeling.branchextraction;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.iobservedata.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;

public class BranchExtraction {
	
	private final List<EntryCallSequenceModel> entryCallSequenceModels;
	private List<CallBranchModel> branchOperationModels = null;
	
	public BranchExtraction(List<EntryCallSequenceModel> entryCallSequenceModels) {
		this.entryCallSequenceModels = entryCallSequenceModels;
	}
	
	public void createCallBranchModels() {
		
		CallBranchModelCreator modelCreator = new CallBranchModelCreator();
		branchOperationModels = new ArrayList<CallBranchModel>();
		
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			/**
			 * Chapter 4.3.4.2 Transformation to the call branch model
			 */
			CallBranchModel branchOperationModel = modelCreator.createCallBranchModel(entryCallSequenceModel);
			/**
			 * Chapter 4.3.4.3 Calculation of the branch likelihoods
			 */
			modelCreator.calculateLikelihoodsOfBranches(branchOperationModel);
			
			branchOperationModels.add(branchOperationModel);
		}

	}

	public List<CallBranchModel> getBranchOperationModels() {
		return branchOperationModels;
	}

}
