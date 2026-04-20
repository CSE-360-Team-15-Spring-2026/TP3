package guiStaffRequests;

import java.util.List;
import entityClasses.adminRequests;
/*******
 * <p> Title: ModelStaffRequests Class. </p>
 *
 * <p> Description: The Staff Requests Model.
 * Provides data-access methods for retrieving requests submitted
 * by the currently logged-in staff user.</p>
 *
 */
public class ModelStaffRequests {
	//current user when visiting staff requests
	private static String currentUser = "";
	/**
	 * <p> sets currentUser with the username of the user in staff requests</p>
	 * 
	 * @param username current user
	 */
	public static void initialize(String username) {
		currentUser = username;
	}
	/**
	 * <p> get requests used in table</p>
	 * 
	 * @return all current requests
	 */
	public static List<adminRequests> getRequests() {
		database.Database db = applicationMain.FoundationsMain.database;
		return db.getRequests(currentUser);
	}
}