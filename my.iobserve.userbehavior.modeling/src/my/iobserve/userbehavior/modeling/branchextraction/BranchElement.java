package my.iobserve.userbehavior.modeling.branchextraction;

public class BranchElement {

	boolean isBranchElementACallElement;
	CallElement callElement = null;
	LoopElement loopElement = null;
	
	public BranchElement(CallElement callElement) {
		this.callElement = callElement;
		this.isBranchElementACallElement = true;
	}
	
	public BranchElement(LoopElement loopElement) {
		this.loopElement = loopElement;
		this.isBranchElementACallElement = false;
	}

	public boolean isBranchElementaCallElement() {
		return isBranchElementACallElement;
	}

	public CallElement getCallElement() {
		return callElement;
	}

	public LoopElement getLoopElement() {
		return loopElement;
	}
	
	
}
