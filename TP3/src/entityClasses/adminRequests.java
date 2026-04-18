package entityClasses;

import java.time.LocalDateTime;

public class adminRequests {
	
	private int requestID;
	private String body;
	private String requestSubmiter;
	private String recievingAdmin;
	private String adminActions;
	private boolean completed;
	private LocalDateTime timestamp;
	private Integer firstRequestID;
	
	public adminRequests() {
		this.requestID = -1;
		this.body = "";
		this.requestSubmiter = "";
		this.recievingAdmin = "";
		this.adminActions = "";
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
	}
	
	public adminRequests(String requestSubmiter, String body, String admin) {
		this.requestID = -1;
		this.body = body;
		this.requestSubmiter = requestSubmiter;
		this.recievingAdmin = admin;
		this.adminActions = "";
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
	}
	
	public void actions(String actions, String admin) {
		this.adminActions += admin + ": " + actions + "\n";
	}
	
	public void completeRequests() {
		this.completed = true;
	}
	
	public int getRequestID() {
		return requestID;
	}
	
	public void setRequestID(int ID) {
		this.requestID = ID;
	}
	
	public void setFirstRequestID(int firstID) {
		this.firstRequestID = firstID;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getRequestSubmiter() {
		return requestSubmiter;
	}
	
	public String getRecievingAdmin() {
		return recievingAdmin;
	}
	
	public String getAdminActions() {
		return adminActions;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public LocalDateTime getTimeStamp() {
		return timestamp;
	}
	
	public void reviewRequest(int firstRequestID, String nBody) {
		this.firstRequestID = firstRequestID;
		this.body = nBody;
		this.completed = false;
	}
}