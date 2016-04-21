package my.iobserve.userbehavior.modeling.branchextraction;

public interface BranchElement {
	
	public int getAbsoluteCount();
	public void setAbsoluteCount(int absoluteCount);
	public String getClassSignature();
	public String getOperationSignature();

}
