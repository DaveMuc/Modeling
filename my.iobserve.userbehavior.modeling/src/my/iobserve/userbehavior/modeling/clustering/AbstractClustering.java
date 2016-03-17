package my.iobserve.userbehavior.modeling.clustering;

import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class AbstractClustering {
	
	List<EntryCallSequenceModel> clusteredEntryCallSequenceModels = new ArrayList<EntryCallSequenceModel>();
	
	protected Instances createInstances(List<UserSession> sessions) {
		
		Attribute attribute1 = new Attribute("amountOfEvents");
		FastVector fvWekaAttributes = new FastVector(1);
		fvWekaAttributes.addElement(attribute1);
		Instances isTrainingSet = new Instances("Relation", fvWekaAttributes, sessions.size());
		
		for(final UserSession userSession:sessions) {
			
			Instance instance = new Instance(1);
			instance.setValue((Attribute)fvWekaAttributes.elementAt(0), userSession.getEvents().size());
			isTrainingSet.add(instance);
			
		}
		
		return isTrainingSet;
	}
	
	protected List<EntryCallSequenceModel> createForEveryClusterASequenceModel(int numberOfClusters, int[] assignments, EntryCallSequenceModel sequenceModel) {
		
		List<EntryCallSequenceModel> entryCallSequenceModels = new ArrayList<EntryCallSequenceModel>(numberOfClusters);
		
		for(int k=0;k<numberOfClusters;k++) {
			
			List<UserSession> sessions = new ArrayList<UserSession>();
			String instancesBelongingToCluster = "Cluster "+k+": ";
			int instanceNumber = 0;
			
			for(int clusterNum : assignments) {
				
				if(clusterNum==k) {
					sessions.add(sequenceModel.getUserSessions().get(instanceNumber));
					instancesBelongingToCluster = instancesBelongingToCluster + "Instance"+instanceNumber+" ";
				}
				instanceNumber++;
			}
			
			entryCallSequenceModels.add(new EntryCallSequenceModel(sessions));
			System.out.println(instancesBelongingToCluster);
		}
		
		return entryCallSequenceModels;
		
	}

}
