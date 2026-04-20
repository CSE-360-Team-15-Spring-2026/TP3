package guiAdminRequests;

import java.util.List;

import database.Database;
import entityClasses.adminRequests;

/*******
 * <p> Title: ModelAdminRequests Class. </p>
 *
 * <p> Description: The Admin Requests Model.
 * Provides data-access functionality for retrieving requests
 * assigned to the current user admin.
 *
 */
public class ModelAdminRequests {
	//database access
    private static Database database = applicationMain.FoundationsMain.database;
    //current admin user
    private static String currentAdmin = "";
    /**
     * <p> initialize currentAdmin with admin who currently is logged in</p>
     * 
     * @param admin
     */
    public static void initialize(String admin) {
        currentAdmin = admin;
    }
    
    /**
     * <p> get requests that were specifically sent to the current admin</p>
     * 
     * @return requests sent to admins
     */
    public static List<adminRequests> getRequests() {
        return database.getRequestsForAdmin(currentAdmin);
    }
}