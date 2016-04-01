package my.iobserve.userbehavior.modeling.clustering;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class KMeansClustering extends AbstractClustering {
	
	public List<EntryCallSequenceModel> clusterSessionsWithKMeans(EntryCallSequenceModel sequenceModel) {
		
		
		try {
			
			List<String> calledMethods = createListOfDistinctCalledMethods(sequenceModel.getUserSessions());
			List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel = createAbsoluteTransitionUserSessions(sequenceModel.getUserSessions(), calledMethods);
			
//			Instances instances = createInstances(sequenceModel.getUserSessions());
			Instances instances = createInstances(absoluteTransitionModel, calledMethods);
			
			SimpleKMeans kmeans = new SimpleKMeans();
			DistanceFunction euclideanDistance = new EuclideanDistance();
			
			euclideanDistance.setInstances(instances);		
			kmeans.setDistanceFunction(euclideanDistance);
			kmeans.setPreserveInstancesOrder(true);
	
			int[] clustersize = null;
			int[] assignments = null;
			
			int numberOfClusters = 2;
			kmeans.setNumClusters(numberOfClusters);

			// build cluster
			kmeans.buildClusterer(instances);

			clustersize = kmeans.getClusterSizes();
			assignments = kmeans.getAssignments();
			
			clusteredEntryCallSequenceModels = createForEveryClusterASequenceModel(numberOfClusters, assignments, sequenceModel);
			
//			Instances resultingCentroids = kmeans.getClusterCentroids();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return clusteredEntryCallSequenceModels;
	}

	
}
