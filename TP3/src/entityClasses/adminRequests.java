package entityClasses;

import java.time.LocalDateTime;

public class adminRequests {
	
	private int requestID;
	private String body;
	private String requestSubmiter;
	private String recievingAdmin;
	private boolean completed;
	private LocalDateTime timestamp;
	private Integer firstRequestID;
	
	public adminRequests() {
		this.requestID = -1;
		this.body = "";
		this.requestSubmiter = "";
		this.recievingAdmin = "";
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
	}
	
	public adminRequests(String requestSubmiter, String body, String admin) {
		this.requestID = -1;
		this.body = body;
		this.requestSubmiter = requestSubmiter;
		this.recievingAdmin = admin;
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
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
	
	public Integer getFirstRequestID() {
		return firstRequestID;
	}
	
	public void setFirstRequestID(int firstID) {
		this.firstRequestID = firstID;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getRequestSubmiter() {
		return requestSubmiter;
	}
	
	public void setRequestSubmiter(String submiter) {
		this.requestSubmiter = submiter;
	}
	
	public String getRecievingAdmin() {
		return recievingAdmin;
	}
	
	public void setRecievingAdmin(String admin) {
		this.recievingAdmin = admin;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean complete) {
		this.completed = complete;
	}
	
	public LocalDateTime getTimeStamp() {
		return timestamp;
	}
	
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timestamp = timeStamp;
	}
	
}