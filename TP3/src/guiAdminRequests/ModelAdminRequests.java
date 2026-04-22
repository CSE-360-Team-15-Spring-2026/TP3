package guiAdminRequests;

import java.util.List;

import database.Database;
import entityClasses.adminRequests;

/*******
 * <p> Title: ModelAdminRequests Class. </p>
 *
 * <p> Description: The Admin Requests Model.
 * Provides data-access functionality for retrieving requests
 * assigned to the current user admin. </p>
 *
 */
public class ModelAdminRequests {
	/** database */
    private static Database database = applicationMain.FoundationsMain.database;
    /** current user admin*/
    private static String currentAdmin = "";
    
	/**
	 * <p> Constructor - Not utilized </p>
	 * 
	 */
	public ModelAdminRequests() {}
    
    /**
     * <p> initialize currentAdmin with admin who currently is logged in</p>
     * 
     * @param admin current user admin
     */
    public static void initialize(String admin) {
        currentAdmin = admin;
    }
    
	/**********
	 * <p> Method: getRequests() </p>
	 * 
	 * <p> gets request tables. </p>
	 * 
	 * @return request based on if addressed to current admin
	 */
    public static List<adminRequests> getRequests() {
        return database.getRequestsForAdmin(currentAdmin);
    }
}