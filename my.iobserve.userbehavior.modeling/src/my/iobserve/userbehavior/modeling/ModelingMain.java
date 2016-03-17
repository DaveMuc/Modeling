package my.iobserve.userbehavior.modeling;

import java.io.IOException;
import java.util.List;

import my.iobserve.userbehavior.modeling.clustering.KMeansClustering;
import my.iobserve.userbehavior.modeling.clustering.XMeansClustering;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.parser.CSVWriter;
import my.iobserve.userbehavior.modeling.parser.SequenceModelBuilder;

public class ModelingMain {
	
	// Test 
	
	private final String csvDirectoryPath = "/Users/David/Uni/Masterarbeit/CsvFilesInput";

	public static void main(String[] args) throws IOException {
		
		final ModelingMain modelingApplication = new ModelingMain();
		final SequenceModelBuilder sequenceModelBuilder = new SequenceModelBuilder();
		final CSVWriter csvWriter = new CSVWriter();
		final LoopDetection loopDetection = new LoopDetection();
		final KMeansClustering kMeansClustering = new KMeansClustering();
		final XMeansClustering xMeansClustering = new XMeansClustering();
		
		EntryCallSequenceModel sequenceModel = sequenceModelBuilder.createSequenceModel(modelingApplication.csvDirectoryPath);
		List<EntryCallSequenceModel> clusteredEntryCallSequenceModels = kMeansClustering.clusterSessionsWithKMeans(sequenceModel);
//		xMeansClustering.clusterSessionsWithXMeans(sequenceModel);
		
//		loopDetection.detectLoopsInUserSessions(sequenceModel.getUserSessions());
//		csvWriter.writeSequenceModelToCSV(callSequenceModel.getUserSessions());
		 
		
		int i = 1;
		i++;

	}
	


}
