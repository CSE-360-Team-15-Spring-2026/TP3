package guiStaffRequests;

import java.util.List; 

import entityClasses.adminRequests;


public class ControllerStaffRequests {
	
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerStaffRequests() {}
	
	protected static void sendRequest(String user, String admin, String body) {
		applicationMain.FoundationsMain.database.createRequest(user, admin, body);
		loadRequests();
	}

	public static void loadRequests() {
		List<adminRequests> list = ModelStaffRequests.getRequests();
		ViewStaffRequests.populateTable(list);
		
	}
}