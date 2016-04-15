package my.iobserve.userbehavior.modeling.clustering;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.UserSessionAsAbsoluteTransitionMatrix;
import weka.clusterers.EM;
import weka.core.Instances;

public class EmClustering  extends AbstractClustering  {
	
	public ClusteringResults clusterSessionsWithEM(List<String> listOfDistinctOperationSignatures, List<UserSessionAsAbsoluteTransitionMatrix> absoluteTransitionModel) {
		
		ClusteringResults emClusteringResults = null;
		
		try {
			
			Instances instances = createInstances(absoluteTransitionModel, listOfDistinctOperationSignatures);
			
			String[] options = new String[2];
//			options[0] = "-I";                 // max. iterations
//			options[1] = "100";
			options[0] = "-N";                 // number of clusters
			options[1] = "2";
			EM clusterer = new EM();   // new instance of clusterer
			clusterer.setOptions(options);     // set the options
			clusterer.buildClusterer(instances);

			
		    int[] clustersize = null;
			// create new assignments
			int[] assignments = new int[instances.numInstances()];
			
			int numberOfClusters = clusterer.numberOfClusters();
			
			// clusterSize
			clustersize = new int[numberOfClusters];

			// set assignments and clustersize
			for (int s = 0; s < instances.numInstances(); s++) {
				assignments[s] = clusterer.clusterInstance(instances.instance(s));
				clustersize[clusterer.clusterInstance(instances.instance(s))]++;
			}
			
			emClusteringResults = new ClusteringResults("EM", numberOfClusters, assignments, null);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return emClusteringResults;
	}


}
