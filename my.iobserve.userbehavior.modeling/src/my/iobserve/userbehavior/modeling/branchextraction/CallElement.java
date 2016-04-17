package my.iobserve.userbehavior.modeling.branchextraction;

public class CallElement {
	
	private String classSignature;
	private String operationSignature;
	
	public CallElement(String classSignature, String operationSignature) {
		this.classSignature = classSignature;
		this.operationSignature = operationSignature;
	}

	public String getClassSignature() {
		return classSignature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

	
}
