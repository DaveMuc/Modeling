package my.iobserve.userbehavior.modeling.testcases;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.iobserve.userbehavior.modeling.UserBehaviorModeling;
import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.EntryCallSequenceModel;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class TestOfUserBehaviorModeling {
	
	static String[] operationSigantures = new String [] {"search","browse","add","pay","delete","get","set","clear"};
	static EntryCallSequenceModel entryCallSequenceModel;
	static int numberOfUserSessions = 500;
	static int minNumberOfCallEvents = 4;
	static int maxNumberOfCallEvents = 10;
	
	/**
	 * Creates a entryCallSequenceModel that consists of a defined number of user sessions
	 * Each user session consists of a number of call events in the range of min and max number of call events
	 * The first operation signature of each call event is "login", the last is "logout"
	 * Between the fist and the last call event randomly operation signatures from the operation signature list are set
	 */
	@BeforeClass
	public static void createCallSequenceModel() {
		List<UserSession> userSessions = new ArrayList<UserSession>();
		for(int i=0;i<numberOfUserSessions;i++) {
			String hostname = "PC";
			String sessionId = "session"+i;
			UserSession userSession = new UserSession(hostname, sessionId);
			int numberOfCallEvents = (int)(Math.random() * ((maxNumberOfCallEvents - minNumberOfCallEvents) + 1)) + minNumberOfCallEvents;
			for(int j=0;j<numberOfCallEvents;j++) {
				int index = j;
				long entrytime = j;
				long exitTime = j+1;
				String classSignature = "class";
				String operationSignature = "";
				if(j==0)
					operationSignature = "login";
				else if(j==numberOfCallEvents-1)
					operationSignature = "logout";
				else {
					int operationSignatureNumber = (int)(Math.random() * ((operationSigantures.length-1 - 0) + 1)) + 0;
					operationSignature = operationSigantures[operationSignatureNumber];
				}
				EntryCallEvent entryCallEvent = new EntryCallEvent(index, entrytime, exitTime, operationSignature, classSignature, sessionId, hostname);
				userSession.add(entryCallEvent,true);
			}
			userSessions.add(userSession);
		}
		entryCallSequenceModel = new EntryCallSequenceModel(userSessions);
	}
	

	@Test
	public void test() throws IOException {
		UserBehaviorModeling userBehaviorModeling = new UserBehaviorModeling(entryCallSequenceModel);
		userBehaviorModeling.modelUserBehavior();
	}

}
