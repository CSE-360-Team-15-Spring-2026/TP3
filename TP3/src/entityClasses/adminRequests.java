package entityClasses;

import java.time.LocalDateTime;
/*******
 * <p> Title: Post Class </p>
 *
 * <p> Description: An class under the entityclasses Package that implements all the necessary attributes and functionality for the adminRequests object for a staff request area.</p>
 * <p> The operations in this class provide input validation and changes the data values which is used by the different GUI packages and the database file to implement the staff request</p>
 * <p> All the attributes and functionality was decided and implemented using the Student User Stories as well as the Staff EPICS</p>
 * <p> The class will contain the CRUD Functionality: </p>
 * <ul>
 * 	<li> Create: Using the Constructors</li>
 * 	<li> Read: Using the Getter Functions</li>
 * 	<li> Update: Using any of the Setter Functions</li>
 *  <li> Delete: Using the setter / getter function for the Delete-Related Attributes. And the changedelete() function</li>
 * </ul>
 * 
 * @version 1.00    
 */
public class adminRequests {
	/** ID of request*/
	private int requestID;
	/** text where staffs request is held*/
	private String body;
	/** user who submitted the request*/
	private String requestSubmiter;
	/** admin who received the request*/
	private String recievingAdmin;
	/** marks request as completed when true*/
	private boolean completed;
	/** time request submitted*/
	private LocalDateTime timestamp;
	/** first request ID*/
	private Integer firstRequestID;
	
	/**
	 * <p> Default constructor initializes an empty request </p>
	 */
	public adminRequests() {
		this.requestID = -1;
		this.body = "";
		this.requestSubmiter = "";
		this.recievingAdmin = "";
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
	}
	
	/**
	 * <p> Constructs a request with a sender, message body, and receiving admin. </p>
	 * 
	 * @param requestSubmiter the user who submitted the request
	 * @param body text of the request
	 * @param admin admin who received the request
	 */
	public adminRequests(String requestSubmiter, String body, String admin) {
		this.requestID = -1;
		this.body = body;
		this.requestSubmiter = requestSubmiter;
		this.recievingAdmin = admin;
		this.completed = false;
		this.timestamp = LocalDateTime.now();
		this.firstRequestID = null;
	}
	
	/*****
	 * <p> CRUD Operations </p>
	 *
	 * <p> These methods implement the create, read, update, and delete
	 * behaviors required by the Students User Stories. </p>
	 */
	
	
	/*****
	 * <p> Checks whether the title provided is valid: Meaning it can't be empty </p>
	 *
	 * @return true if valid, false otherwise
	 */
	public  boolean validateBody() {
		return body != null && !body.isBlank();
	}
	
	/*****
	 * <p> Changes the completed Boolean Value </p>
	 * <p> Useful for marking requests as completed</p>
	 *
	 */
	public void completeRequests() {
		this.completed = true;
	}
	
	/**
	 * <p> gets the request ID </p>
	 * 
	 * @return requestID ID number of requests
	 */
	public int getRequestID() {
		return requestID;
	}
	/**
	 * <p> sets Requests ID </p>
	 * 
	 * @param ID request number
	 */
	public void setRequestID(int ID) {
		this.requestID = ID;
	}
	
	/**
	 * <p> gets the first request ID </p>
	 * 
	 * @return value of first request ID
	 */
	public Integer getFirstRequestID() {
		return firstRequestID;
	}
	/**
	 * <p> set the first request ID</p>
	 * 
	 * @param firstID value of the firstRequestID
	 */
	public void setFirstRequestID(int firstID) {
		this.firstRequestID = firstID;
	}
	/**
	 * <p> get body value</p>
	 * 
	 * @return body text of the request class 
	 */
	public String getBody() {
		return body;
	}
	/**
	 * <p> set the body of request</p>
	 * 
	 * @param body text of the request
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * <p> get user who made request</p>
	 * 
	 * @return requestSubmiter user who submitted request
	 */
	public String getRequestSubmiter() {
		return requestSubmiter;
	}
	/**
	 * <p> set the user who made request</p>
	 * 
	 * @param submiter user who submitted request
	 */
	public void setRequestSubmiter(String submiter) {
		this.requestSubmiter = submiter;
	}
	/**
	 * <p> get the admin who received the request</p>
	 * 
	 * @return recievingAdmin admin who received request
	 */
	public String getRecievingAdmin() {
		return recievingAdmin;
	}
	
	/**
	 * <p> set the admin who recieved the request</p>
	 * 
	 * @param admin admin set to receive request
	 */
	public void setRecievingAdmin(String admin) {
		this.recievingAdmin = admin;
	}
	
	/**
	 * <p> get the value of completed</p>
	 * 
	 * @return true if admin has completed the request
	 */
	public boolean getCompleted() {
		return completed;
	}
	
	/**
	 * <p> set value of completed</p>
	 * 
	 * @param complete sets request as completed
	 */
	public void setCompleted(boolean complete) {
		this.completed = complete;
	}
	/**
	 * <p> get the time request was submitted</p>
	 * 
	 * @return timestamp sets timestamp as completed
	 */
	public LocalDateTime getTimeStamp() {
		return timestamp;
	}
	/**
	 * <p> set the timestamp with the time requests are submitted</p>
	 * 
	 * @param timeStamp sets the time request submitted
	 */
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timestamp = timeStamp;
	}
	
}