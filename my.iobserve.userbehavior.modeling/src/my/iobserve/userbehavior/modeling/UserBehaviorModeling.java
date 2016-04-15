package my.iobserve.userbehavior.modeling;

import java.io.IOException;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.usergroupextraction.EmClustering;
import my.iobserve.userbehavior.modeling.usergroupextraction.UserGroupExtraction;
import my.iobserve.userbehavior.modeling.usergroupextraction.KMeansClustering;
import my.iobserve.userbehavior.modeling.usergroupextraction.XMeansClustering;
import my.iobserve.userbehavior.modeling.utils.CSVWriter;
import my.iobserve.userbehavior.modeling.utils.SequenceModelBuilder;

public class UserBehaviorModeling {
		
	private EntryCallSequenceModel entryCallSequenceModel;
	
	public UserBehaviorModeling (EntryCallSequenceModel entryCallSequenceModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
	}

	/**
	 * Implementation of the thesis´ concept
	 * It generates from the input entry call sequence model a complex user behavior model
	 * At that different user groups are detected and for each group its own model is created
	 */
	public void modelUserBehavior() throws IOException {
		
		/**
		 * Chapter 4.3.3 The extraction of user groups
		 * It clusters the entry call sequence model to detect different user groups within the user sessions
		 * The result is one entry call sequence model for each detected user group
		 * Each entry call sequence model contains the user sessions that are assigned to the user group
		 */
		final UserGroupExtraction extractionOfUserGroups = new UserGroupExtraction(entryCallSequenceModel);
		extractionOfUserGroups.extractUserGroups();
		final List<EntryCallSequenceModel> entryCallSequenceModels = extractionOfUserGroups.getEntryCallSequenceModelsOfUserGroups();
		
		/**
		 * Chapter 4.3.4 The creation of the branch operation model
		 */
		
		
		int i = 1;
		i++;

	}
	


}
