package my.iobserve.userbehavior.modeling;

/**
 * Copyright 2016 David Peter
 */

import java.io.IOException;
import java.util.List;

import my.iobserve.userbehavior.modeling.branchextraction.BranchExtraction;
import my.iobserve.userbehavior.modeling.iobservedata.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.loopdetection.LoopDetection;
import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;
import my.iobserve.userbehavior.modeling.usergroupextraction.UserGroupExtraction;

public class UserBehaviorModeling {
		
	private final EntryCallSequenceModel inputEntryCallSequenceModel;
	private final int numberOfUserGroupsFromInputUsageModel;
	
	public UserBehaviorModeling (EntryCallSequenceModel inputEntryCallSequenceModel, int numberOfUserGroupsFromInputUsageModel) {
		this.inputEntryCallSequenceModel = inputEntryCallSequenceModel;
		this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
	}

	/**
	 * Implementation of the thesis´ concept
	 * It generates from the input entry call sequence model a complex user behavior model
	 * At that different user groups are detected and for each group its own model is created
	 */
	public void modelUserBehavior() throws IOException {
				
		/**
		 * Chapter 4.3.3 The extraction of user groups
		 * It clusters the entry call sequence model to detect different user groups within the user sessions
		 * The result is one entry call sequence model for each detected user group
		 * Each entry call sequence model contains: 
		 * - the user sessions that are assigned to the user group
		 * - the likelihood of its user group
		 * - the parameters for the workload intensity
		 */
		final UserGroupExtraction extractionOfUserGroups = new UserGroupExtraction(inputEntryCallSequenceModel, numberOfUserGroupsFromInputUsageModel);
		extractionOfUserGroups.extractUserGroups();
		final List<EntryCallSequenceModel> entryCallSequenceModels = extractionOfUserGroups.getEntryCallSequenceModelsOfUserGroups();
		
		/**
		 * Chapter 4.3.4 The aggregation of the call sequences
		 * It aggregates each call sequence model into a call branch model 
		 * At that, it detects branches and the branch likelihoods
		 */
		final BranchExtraction branchExtraction = new BranchExtraction(entryCallSequenceModels);
		branchExtraction.createCallBranchModels();
		final List<CallBranchModel> callBranchModels = branchExtraction.getBranchOperationModels();
		
		/**
		 * Chapter 4.3.5 The detection of iterated behavior
		 * It detects loops within the branch sequences
		 */
		final LoopDetection loopDetection = new LoopDetection(callBranchModels);
		loopDetection.createCallLoopBranchModels();
		final List<CallBranchModel> callLoopBranchModels = loopDetection.getCallLoopBranchModels();
		
		// to set a breakpoint
		// To delete
		int debugStopper = 1;
		debugStopper++;

	}
	


}
