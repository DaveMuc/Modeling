package my.iobserve.userbehavior.modeling.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class CSVWriter {
	
	public void writeSequenceModelToCSV(List<UserSession> sessions) {
		
		/** 
		 * Ordner, wo die csv Files gespeichert werden sollen
		 */
		final String csvSaveDirectory = "/Users/David/Uni/Masterarbeit/CsvFilesOutput/";
		int csvFileIndex = 0;
		
		for(final UserSession userSession:sessions) {
			
			csvFileIndex++;
			String fileName = csvSaveDirectory+"userSession"+String.valueOf(csvFileIndex)+".csv";
			FileWriter writer = null;
			try {
				writer = new FileWriter(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			
			while(iteratorEvents.hasNext()) {
				
				final EntryCallEvent event = iteratorEvents.next();
				try {
					writer.append(String.valueOf(event.getIndex()));
					writer.append(",");
					writer.append(String.valueOf(event.getEntryTime()));
					writer.append(",");
					writer.append(String.valueOf(event.getExitTime()));
					writer.append(",");
					writer.append(event.getOperationSignature());
					writer.append(",");
					writer.append(event.getClassSignature());
					writer.append(",");
					writer.append(event.getSessionId());
					writer.append(",");
					writer.append(event.getHostname());
					writer.append("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			    
				
			}
			
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
