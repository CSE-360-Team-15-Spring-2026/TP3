package guiAdminRequests;

import java.util.List;

import entityClasses.adminRequests;

public class ControllerAdminRequests {

    public static void loadRequests() {

        List<adminRequests> list =
            ModelAdminRequests.getRequests();

        ViewAdminRequests.populateTable(list);
    }

}