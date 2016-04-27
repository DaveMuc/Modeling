package my.iobserve.userbehavior.modeling.loopdetection;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.modelingdata.CallBranchModel;

public class LoopDetection {
	
	private final List<CallBranchModel> callBranchModels;
	private List<CallBranchModel> callLoopBranchModels;
	
	public LoopDetection(List<CallBranchModel> callBranchModels) {
		this.callBranchModels = callBranchModels;
	}
	
	public void createCallLoopBranchModels() {
		
		CallLoopBranchModelCreator modelCreator = new CallLoopBranchModelCreator();
		callLoopBranchModels = new ArrayList<CallBranchModel>();
		
		for(CallBranchModel callBranchModel : callBranchModels) {
			CallBranchModel callLoopBranchModel = modelCreator.detectLoopsInCallBranchModel(callBranchModel);
			callLoopBranchModels.add(callLoopBranchModel);
		}
		
	}

	public List<CallBranchModel> getCallLoopBranchModels() {
		return callLoopBranchModels;
	}
	


}
