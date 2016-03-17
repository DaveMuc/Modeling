/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package my.iobserve.userbehavior.modeling.data;


/**
 * @author Reiner Jung
 * 
 * @since 1.0
 */
public class EntryCallEvent {
	

	
	/* property declarations */
	private final long entryTime;
	private final long exitTime;
	private final String operationSignature;
	private final String classSignature;
	private final String sessionId;
	private final String hostname;
	
	 

	/* added by Alessandro Giusa at 19.12.2015
	 * Reason: when entryTime and exitTime accidently is the same, there is no way of
	 * sorting the entry call events, therefore this index is used.
	 * */
	private int index;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * @param index index of this entry call event
	 * @param entryTime
	 *            entryTime
	 * @param exitTime
	 *            exitTime
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param sessionId
	 *            sessionId
	 * @param hostname
	 *            hostname
	 */
	public EntryCallEvent(final int index, final long entryTime,
			final long exitTime, final String operationSignature,
			final String classSignature, final String sessionId, final String hostname) {
		this.index = index;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.operationSignature = operationSignature == null?"":operationSignature;
		this.classSignature = classSignature == null?"":classSignature;
		this.sessionId = sessionId == null?"":sessionId;
		this.hostname = hostname == null?"":hostname;
	}

	
	public int getIndex() {
		return this.index;
	}

	public final long getEntryTime() {
		return this.entryTime;
	}
	
	public final long getExitTime() {
		return this.exitTime;
	}
	
	public final String getOperationSignature() {
		return this.operationSignature;
	}
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
}
