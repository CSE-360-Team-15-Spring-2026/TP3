package guiAdminRequests;

import java.util.List;

import database.Database;
import entityClasses.adminRequests;

public class ModelAdminRequests {

    private static Database database =
        applicationMain.FoundationsMain.database;

    private static String currentAdmin = "";

    public static void initialize(String admin) {
        currentAdmin = admin;
    }

    public static List<adminRequests> getRequests() {
        return database.getRequestsForAdmin(currentAdmin);
    }
}