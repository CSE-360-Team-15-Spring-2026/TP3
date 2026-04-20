package guiStaffRequests;

import java.util.List; 

import entityClasses.adminRequests;

/*******
 * <p> Title: ControllerStaffRequests Class. </p>
 *
 * <p> Description: The Java/FX-based Staff Requests Controller.
 * Handles all user interactions related to staff requests, including
 * sending new requests and loading existing requests from the system.
 *
 * This controller connects the Staff Requests with the staff requests
 * model and database functionality.</p>
 *
 */
public class ControllerStaffRequests {
	
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerStaffRequests() {}
	/**
	 * <p> will create a request in database</p>
	 * 
	 * @param user current user
	 * @param admin admin who is receiving request
	 * @param body text of the request
	 */
	protected static void sendRequest(String user, String admin, String body) {
		applicationMain.FoundationsMain.database.createRequest(user, admin, body);
		loadRequests();
	}
	
	/**
	 * loads currently all requests made into table
	 */
	public static void loadRequests() {
		List<adminRequests> list = ModelStaffRequests.getRequests();
		ViewStaffRequests.populateTable(list);
		
	}
}