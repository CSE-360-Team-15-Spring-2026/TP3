package guiStaffRequests;

import java.util.List;
import entityClasses.adminRequests;

/*******
 * <p> Title: ControllerStaffRequests Class. </p>
 *
 * <p> Description: The Java/FX-based Staff Requests Controller. Handles all user
 * interactions related to staff requests, including sending new requests and loading
 * existing requests from the system.
 *
 * This controller connects the Staff Requests view with the staff requests model and
 * database functionality. </p>
 */
public class ControllerStaffRequests {

    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are static).
     */
    public ControllerStaffRequests() {}


    /*-*******************************************************************************************

    Request actions

     */

    /**
     * <p> Creates a new request in the database and refreshes the requests table. </p>
     *
     * @param user  the username of the staff member sending the request
     * @param admin the username of the admin receiving the request
     * @param body  the text body of the request
     */
    protected static void sendRequest(String user, String admin, String body) {
        applicationMain.FoundationsMain.database.createRequest(user, admin, body);
        loadRequests();
    }

    /**
     * <p> Loads all current requests from the model and populates the table in the view. </p>
     */
    public static void loadRequests() {
        List<adminRequests> list = ModelStaffRequests.getRequests();
        ViewStaffRequests.populateTable(list);
    }
}
