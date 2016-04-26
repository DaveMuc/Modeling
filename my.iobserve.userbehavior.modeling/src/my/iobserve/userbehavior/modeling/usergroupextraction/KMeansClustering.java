package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.List;

import my.iobserve.userbehavior.modeling.modelingdata.UserSessionAsAbsoluteTransitionMatrix;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class KMeansClustering extends AbstractClustering {
	
	public ClusteringResults clusterSessionsWithKMeans(List<String> listOfDistinctOperationSignatures, List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel) {
		
		ClusteringResults kMeansClusteringResults = null;
		
		try {
			
			Instances instances = createInstances(absoluteTransitionModel, listOfDistinctOperationSignatures);
			
			SimpleKMeans kmeans = new SimpleKMeans();
			DistanceFunction euclideanDistance = new EuclideanDistance();
			euclideanDistance.setInstances(instances);		
			kmeans.setDistanceFunction(euclideanDistance);
			kmeans.setPreserveInstancesOrder(true);
	
			int[] assignments = null;
			
			int numberOfClusters = 2;
			kmeans.setNumClusters(numberOfClusters);

			kmeans.buildClusterer(instances);

			assignments = kmeans.getAssignments();
			
			ClusteringMetrics clusteringMetrics = new ClusteringMetrics(kmeans.getClusterCentroids(), instances, assignments);
			clusteringMetrics.calculateSimilarityMetrics();
			
			kMeansClusteringResults = new ClusteringResults("K-Means", kmeans.getClusterCentroids().numInstances(), assignments, clusteringMetrics);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return kMeansClusteringResults;
	}

	
}
