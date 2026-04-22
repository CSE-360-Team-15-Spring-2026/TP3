package guiStaffRequests;

import java.util.List;
import entityClasses.adminRequests;

/*******
 * <p> Title: ModelStaffRequests Class. </p>
 *
 * <p> Description: The Staff Requests Model. Provides data-access methods for retrieving
 * requests submitted by the currently logged-in staff user. </p>
 */
public class ModelStaffRequests {

    /** The username of the staff member currently viewing the requests page. */
    private static String currentUser = "";


    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are static).
     */
    public ModelStaffRequests() {}


    /*-*******************************************************************************************

    Initialization

     */

    /**
     * <p> Sets the current user so that subsequent data queries are scoped to that
     * staff member. </p>
     *
     * @param username the username of the currently logged-in staff member
     */
    public static void initialize(String username) {
        currentUser = username;
    }


    /*-*******************************************************************************************

    Data access

     */

    /**
     * <p> Retrieves all requests submitted by the current staff user from the database. </p>
     *
     * @return list of all requests associated with the current user
     */
    public static List<adminRequests> getRequests() {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getRequests(currentUser);
    }
}
