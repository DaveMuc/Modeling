package my.iobserve.userbehavior.modeling.clustering;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class KMeansClustering extends AbstractClustering {
	
	public List<EntryCallSequenceModel> clusterSessionsWithKMeans(EntryCallSequenceModel sequenceModel) {
		
		
		try {
		
			Instances instances = createInstances(sequenceModel.getUserSessions());
			
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
