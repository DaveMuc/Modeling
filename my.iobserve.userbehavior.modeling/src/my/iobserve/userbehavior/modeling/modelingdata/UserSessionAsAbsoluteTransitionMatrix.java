package my.iobserve.userbehavior.modeling.modelingdata;

public class UserSessionAsAbsoluteTransitionMatrix {
	
	private String sessionId;
	private int [] [] absoluteTransitionMatrix;
	
	public UserSessionAsAbsoluteTransitionMatrix (String sessionId, int numberOfDistinctMethods) {
		this.sessionId = sessionId;
		this.absoluteTransitionMatrix = new int [numberOfDistinctMethods][numberOfDistinctMethods];
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int[][] getAbsoluteTransitionMatrix() {
		return absoluteTransitionMatrix;
	}

	public void setAbsoluteTransitionMatrix(int[][] absoluteTransitionMatrix) {
		this.absoluteTransitionMatrix = absoluteTransitionMatrix;
	}

	
}
