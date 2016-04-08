package my.iobserve.userbehavior.modeling.clustering;

import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class ClusteringMetrics {
	
	// Distance between the clusters
	private double interClusterSimilarity = 0;
	// Distance within the clusters
	private double intraClusterSimilarity = 0;
	
	private Instances centroids;
	private Instances instances; 
	private int[] assignments;
	
	public ClusteringMetrics (Instances centroids, Instances instances, int[] assignments) {
		this.centroids = centroids;
		this.instances = instances;
		this.assignments = assignments;
	}
	
	public void calculateSimilarityMetrics () {
		this.interClusterSimilarity = calculateInterClusterSimilarity();
		this.intraClusterSimilarity = calculateIntraClusterSimilarity();
	}
	
	// Calculate distance between the clusters
	private double calculateInterClusterSimilarity () {
		
		DistanceFunction euclideanDistance = new EuclideanDistance();
		euclideanDistance.setInstances(centroids);

		double k = (double) centroids.numInstances();
		double sumDistance = 0;

		for (int i = 0; i < k; i++) {
			for (int j = i + 1; j < k; j++) {
				sumDistance += euclideanDistance.distance(
						centroids.instance(i), centroids.instance(j));
			}
		}

		return (1 / (k * (k - 1) / 2)) * sumDistance;
	}
	
	// Calculate distance within the clusters
	private double calculateIntraClusterSimilarity () {
		
		DistanceFunction euclideanDistance = new EuclideanDistance();
		euclideanDistance.setInstances(instances);

		double[] avgIntraClusterSimilarity = new double[centroids
				.numInstances()];
		double k = (double) centroids.numInstances();
		double sumDistance = 0;
		double counter = 0;
		double sumDistanceAllClusters = 0;

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < instances.numInstances(); j++) {
				if (assignments[j] == i) {
					sumDistance += euclideanDistance.distance(
							instances.instance(j), centroids.instance(i));
					counter += 1;
				}
			}
			avgIntraClusterSimilarity[i] = (1 / counter) * sumDistance;
			sumDistance = 0;
			counter = 0;
		}

		for (double clusterDistance : avgIntraClusterSimilarity) {
			sumDistanceAllClusters += clusterDistance;
		}

		return (1 / k) * sumDistanceAllClusters;	
	}
	
	public void printSimilarityMetrics() {
		if (interClusterSimilarity == 0 && intraClusterSimilarity == 0) {
			System.out.println("Metrics have not been calculated");
		} else {
			System.out.println("Mean distance beetween the clusters: " + interClusterSimilarity);
			System.out.println("Mean distance within the clusters: " + intraClusterSimilarity);
		}
	}

	public double getInterClusterSimilarity() {
		return interClusterSimilarity;
	}

	public double getIntraClusterSimilarity() {
		return intraClusterSimilarity;
	}
	

}
