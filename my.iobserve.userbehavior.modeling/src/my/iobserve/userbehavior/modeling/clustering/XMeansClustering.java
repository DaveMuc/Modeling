package my.iobserve.userbehavior.modeling.clustering;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import weka.clusterers.XMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class XMeansClustering extends AbstractClustering {
	
	public List<EntryCallSequenceModel> clusterSessionsWithXMeans(EntryCallSequenceModel sequenceModel){
		
		try {
			
			Instances instances = createInstances(sequenceModel.getUserSessions());
			
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

			// set assignments and clustersize
			for (int s = 0; s < instances.numInstances(); s++) {
				assignments[s] = xmeans.clusterInstance(instances.instance(s));
				clustersize[xmeans.clusterInstance(instances.instance(s))]++;
			}

			clusteredEntryCallSequenceModels = createForEveryClusterASequenceModel(clustersize.length, assignments, sequenceModel);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clusteredEntryCallSequenceModels;
		
	}

}
