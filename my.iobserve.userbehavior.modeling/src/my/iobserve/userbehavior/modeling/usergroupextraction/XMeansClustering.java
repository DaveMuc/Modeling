package my.iobserve.userbehavior.modeling.usergroupextraction;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import weka.clusterers.XMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class XMeansClustering extends AbstractClustering {
	
	public ClusteringResults clusterSessionsWithXMeans(List<String> listOfDistinctOperationSignatures, List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel){
		
		ClusteringResults xMeansClusteringResults = null;
		
		try {
				
			Instances instances = createInstances(absoluteTransitionModel, listOfDistinctOperationSignatures);
			
			XMeans xmeans = new XMeans();
			xmeans.setSeed(5);
			DistanceFunction euclideanDistance = new EuclideanDistance();
			euclideanDistance.setInstances(instances);
			xmeans.setDistanceF(euclideanDistance);
			
			int[] clustersize = null;
			int[] assignments = new int[instances.numInstances()];
			
			int numberOfClustersMin = 2;
			int numberOfClustersMax = 4;
			
			xmeans.setMinNumClusters(numberOfClustersMin);
			xmeans.setMaxNumClusters(numberOfClustersMax);

			xmeans.buildClusterer(instances);
			
			clustersize = new int[xmeans.getClusterCenters().numInstances()];

			for (int s = 0; s < instances.numInstances(); s++) {
				assignments[s] = xmeans.clusterInstance(instances.instance(s));
				clustersize[xmeans.clusterInstance(instances.instance(s))]++;
			}
			
			ClusteringMetrics clusteringMetrics = new ClusteringMetrics(xmeans.getClusterCenters(), instances, assignments);
			clusteringMetrics.calculateSimilarityMetrics();

			xMeansClusteringResults = new ClusteringResults("X-Means", xmeans.getClusterCenters().numInstances(), assignments, clusteringMetrics);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return xMeansClusteringResults;
		
	}

}
