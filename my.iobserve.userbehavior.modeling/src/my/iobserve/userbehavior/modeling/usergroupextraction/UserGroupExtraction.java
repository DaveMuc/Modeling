package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.List;

import my.iobserve.userbehavior.modeling.iobservedata.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.modelingdata.UserSessionAsAbsoluteTransitionMatrix;

public class UserGroupExtraction {
	
	private final EntryCallSequenceModel entryCallSequenceModel;
	private final int numberOfUserGroupsFromInputUsageModel;
	private List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = null;
	
	public UserGroupExtraction(EntryCallSequenceModel entryCallSequenceModel, int numberOfUserGroupsFromInputUsageModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
		this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
	}
	
	public void extractUserGroups() {
		
		ClusteringPrePostProcessing clusteringProcessing = new ClusteringPrePostProcessing();
		XMeansClustering xMeansClustering = new XMeansClustering();
		KMeansClustering kMeansClustering = new KMeansClustering();
		ClusteringResults xMeansClusteringResults;
		ClusteringResults kMeansClusteringResults;
		
		/**
		 * Chapter 4.3.3.1 Extraction of distinct system operations
		 * Creates a list of the distinct system operations of the entryCallSequenceModel
		 */
		List<String> listOfDistinctOperationSignatures = clusteringProcessing.getListOfDistinctOperationSignatures(entryCallSequenceModel.getUserSessions());
	
		/**
		 * Chapter 4.3.3.2 Transformation to the absolute transition model
		 * Transforms the call sequences of the user sessions to absolute transition matrices
		 */
		List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel = clusteringProcessing.getAbsoluteTransitionModel(entryCallSequenceModel.getUserSessions(), listOfDistinctOperationSignatures);
	
		/**
		 * Chapter 4.3.3.3 Clustering of user sessions
		 * Clustering of the absolute transition matrices to obtain user groups 
		 */
		xMeansClusteringResults = xMeansClustering.clusterSessionsWithXMeans(listOfDistinctOperationSignatures, absoluteTransitionModel, numberOfUserGroupsFromInputUsageModel);
//		kMeansClusteringResults = kMeansClustering.clusterSessionsWithKMeans(listOfDistinctOperationSignatures, absoluteTransitionModel, numberOfUserGroupsFromInputUsageModel);
		
		/**
		 * Chapter 4.3.3.4 Obtaining the user groups´ call sequence models 
		 * Creates for each cluster resp. user group its own entry call sequence model 
		 */
		xMeansClusteringResults.printClusteringResults();
		List<EntryCallSequenceModel> entryCallSequenceModelsOfXMeansClustering = clusteringProcessing.getForEachUserGroupAEntryCallSequenceModel(xMeansClusteringResults, entryCallSequenceModel);
//		kMeansClusteringResults.printClusteringResults();
//		List<EntryCallSequenceModel> entryCallSequenceModelsOfKMeansClustering = clusteringProcessing.getForEachUserGroupAEntryCallSequenceModel(kMeansClusteringResults, entryCallSequenceModel);
		
		/**
		 * Chapter 4.3.3.5 Obtaining the user groups´ workload intensity 
		 * Calculates and sets for each user group its own workload intensity parameters
		 */
		clusteringProcessing.setTheWorkloadIntensityForTheEntryCallSequenceModels(entryCallSequenceModelsOfXMeansClustering);
		
		this.entryCallSequenceModelsOfUserGroups = entryCallSequenceModelsOfXMeansClustering;
	}
	
	public List<EntryCallSequenceModel> getEntryCallSequenceModelsOfUserGroups() {
		return this.entryCallSequenceModelsOfUserGroups;
	}
	 

}
