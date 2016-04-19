package my.iobserve.userbehavior.modeling.branchextraction;

public class BranchElement {

	final boolean isBranchElementACallElement;
	final CallElement callElement;
	final LoopElement loopElement;
	final ExitElement exitElement;
	
	public BranchElement(CallElement callElement) {
		this.callElement = callElement;
		loopElement = null;
		exitElement = null;
		this.isBranchElementACallElement = true;
	}
	
	public BranchElement(LoopElement loopElement) {
		this.loopElement = loopElement;
		this.callElement = null;
		this.exitElement = null;
		this.isBranchElementACallElement = false;
	}
	
	public BranchElement(ExitElement exitElement) {
		this.exitElement = exitElement;
		this.loopElement = null;
		this.callElement = null;
		this.isBranchElementACallElement = false;
	}

	public boolean isBranchElementACallElement() {
		return isBranchElementACallElement;
	}

	public CallElement getCallElement() {
		return callElement;
	}

	public LoopElement getLoopElement() {
		return loopElement;
	}

	public ExitElement getExitElement() {
		return exitElement;
	}
	
	
}
