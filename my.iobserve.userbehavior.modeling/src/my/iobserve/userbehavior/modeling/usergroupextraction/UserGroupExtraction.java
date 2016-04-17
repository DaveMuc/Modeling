package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;

public class UserGroupExtraction {
	
	private EntryCallSequenceModel entryCallSequenceModel;
	private List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = null;
	
	public UserGroupExtraction(EntryCallSequenceModel entryCallSequenceModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
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
		xMeansClusteringResults = xMeansClustering.clusterSessionsWithXMeans(listOfDistinctOperationSignatures, absoluteTransitionModel);
		kMeansClusteringResults = kMeansClustering.clusterSessionsWithKMeans(listOfDistinctOperationSignatures, absoluteTransitionModel);
		
		/**
		 * Chapter 4.3.3.4 Obtaining the user groups´ call sequence models 
		 * Creates for each cluster resp. user group its own entry call sequence model 
		 */
		xMeansClusteringResults.printClusteringResults();
		List<EntryCallSequenceModel> entryCallSequenceModelsOfXMeansClustering = clusteringProcessing.getForEachUserGroupAEntryCallSequenceModel(xMeansClusteringResults, entryCallSequenceModel);
		kMeansClusteringResults.printClusteringResults();
		List<EntryCallSequenceModel> entryCallSequenceModelsOfKMeansClustering = clusteringProcessing.getForEachUserGroupAEntryCallSequenceModel(kMeansClusteringResults, entryCallSequenceModel);
		
		/**
		 * Chapter 4.3.3.5 Obtaining the user groups´ workload intensity 
		 * Calculates and sets for each user group its own workload intensity
		 * The intensity consists of an interarrival time for an open workload definition
		 * and the maximum number of concurrent users for a closed workload definition
		 */
		clusteringProcessing.setTheWorkloadIntensityForTheEntryCallSequenceModels(entryCallSequenceModelsOfXMeansClustering);
		
		this.entryCallSequenceModelsOfUserGroups = entryCallSequenceModelsOfXMeansClustering;
	}
	
	public List<EntryCallSequenceModel> getEntryCallSequenceModelsOfUserGroups() {
		return this.entryCallSequenceModelsOfUserGroups;
	}
	 

}
