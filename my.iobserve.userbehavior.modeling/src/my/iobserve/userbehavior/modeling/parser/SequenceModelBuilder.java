package my.iobserve.userbehavior.modeling.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class SequenceModelBuilder {
	
	
	public EntryCallSequenceModel createSequenceModel(String csvDirectoryPath) throws IOException {
		
		Scanner scanner;
		List<UserSession> sessions = new ArrayList<UserSession>();
		File folder = new File(csvDirectoryPath);
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".csv");
		    }
		});

		for (File csvFile : listOfFiles) {
		
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line;
			
			UserSession userSession = new UserSession();
			
			while ((line = br.readLine()) != null) {
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				
				int index = 0;
				long entryTime = 0;
				long exitTime = 0;
				String operationSignature = "";
				String classSignature = "";
				String sessionId = "";
				String hostname = "";
				
				int csvIndex = 1;
				
				while(scanner.hasNext()){
					
					switch(csvIndex){
			        case 1:
			        	index = Integer.parseInt(scanner.next());
			            break;
			        case 2:
			        	entryTime = Long.parseLong(scanner.next(), 10); 
			            break;
			        case 3:
			        	exitTime = Long.parseLong(scanner.next(), 10);
			            break;
			        case 4:
			        	operationSignature = scanner.next();
			            break; 
			        case 5:
			        	classSignature = scanner.next();
			            break;
			        case 6:
			        	sessionId = scanner.next();
			            break;
			        case 7:
			        	hostname = scanner.next();
			            break;
					}
					
					csvIndex++;
				}
				
				EntryCallEvent callEvent = new EntryCallEvent(index, entryTime, exitTime, operationSignature, classSignature, sessionId, hostname);
				userSession.add(callEvent, true);
				userSession.setHost(hostname);
				userSession.setSessionId(sessionId);
				
			}
			
			sessions.add(userSession);
		}
		
		return new EntryCallSequenceModel(sessions);
	}
	
	 

}
