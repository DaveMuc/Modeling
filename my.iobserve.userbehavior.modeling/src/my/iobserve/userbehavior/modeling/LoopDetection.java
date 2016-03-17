package my.iobserve.userbehavior.modeling;

import java.util.List;

import my.iobserve.userbehavior.modeling.data.EntryCallEvent;
import my.iobserve.userbehavior.modeling.data.UserSession;

public class LoopDetection {
	
	public void detectLoopsInUserSessions(List<UserSession> sessions) {
		
		for(final UserSession userSession:sessions) {
			
			List<EntryCallEvent> events = userSession.getEvents();
			
			if(events==null||events.size()<2) {
				return;
			}
			
			int indexOfEventInEventList = 0;
			for(final EntryCallEvent event:events) {
				
				for(int i=0;i<((events.size()-indexOfEventInEventList)/2);i++) {
					
					int elementsToCheck = ((events.size()-indexOfEventInEventList)/2) - i;
					String headString = "";
					String tailString = "";
					
					for(int k=0;k<elementsToCheck;k++) {
						headString = headString+events.get(indexOfEventInEventList+k).getClassSignature()+"-";
						tailString = tailString+events.get(indexOfEventInEventList+elementsToCheck+k).getClassSignature()+"-";
					}
					
					if(headString.equals(tailString)) {
						System.out.println(headString);
						System.out.println();
					}

				}
				
				indexOfEventInEventList++;
			}
			
			
		}
		
		
	}

}
