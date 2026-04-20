package guiStaffRequests;

import java.util.List;
import entityClasses.adminRequests;

public class ModelStaffRequests {
	
	private static String currentUser = "";
	
	public static void initialize(String username) {
		currentUser = username;
	}
	
	public static List<adminRequests> getRequests() {
		database.Database db = applicationMain.FoundationsMain.database;
		return db.getRequests(currentUser);
	}
}