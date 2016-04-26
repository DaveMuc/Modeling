package my.iobserve.userbehavior.modeling.loopdetection;

import java.util.List;

import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;

public class LoopDetection {
	
	private final List<CallBranchModel> callBranchModels;
	
	public LoopDetection(List<CallBranchModel> callBranchModels) {
		this.callBranchModels = callBranchModels;
	}
	
	public void createCallLoopBranchModels() {
		
		CallLoopBranchModelCreator modelCreator = new CallLoopBranchModelCreator();
		
		for(CallBranchModel callBranchModel : callBranchModels) {
			
			CallBranchModel callLoopBranchModel = modelCreator.detectLoopsInCallBranchModel(callBranchModel);
			
		}
		
		
		
	}


}
