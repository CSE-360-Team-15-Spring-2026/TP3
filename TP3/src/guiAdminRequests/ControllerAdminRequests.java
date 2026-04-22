package guiAdminRequests;

import java.util.List;

import entityClasses.adminRequests;

/*******
 * <p> Title: ControllerAdminRequests Class. </p>
 *
 * <p> Description: The Java/FX-based Admin Requests Controller.
 * Responsible for handling admins actions taken to complete
 * staffs requests, including loading requests and updating the view.
 *
 * This controller connects the view with the model
 * and ensures that requests are properly displayed.</p>
 */
public class ControllerAdminRequests {
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ControllerAdminRequests() {}
	
	/**********
	 * <p> Method: loadRequests() </p>
	 * 
	 * <p> retrieves data from request table. </p>
	 * 
	 */
    public static void loadRequests() {
        List<adminRequests> list = ModelAdminRequests.getRequests();
        ViewAdminRequests.populateTable(list);
    }

}