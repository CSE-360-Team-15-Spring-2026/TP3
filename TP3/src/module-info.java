/**
 * The FoundationsF25 module for the CSE 360 user management application.
 */
module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	requires org.junit.jupiter.api;
	requires javafx.base;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
	exports applicationMain;
    
    opens guiListUsers to javafx.base;
    opens guiManageInvitations to javafx.base;
    opens guiSearchPosts to javafx.base;
    opens guiCreatePost to javafx.base;
    opens guiEditPost to javafx.base;
    opens guiEditReply to javafx.base;
    opens guiViewPost to javafx.base;
    opens guiRole1 to javafx.base;
    opens guiRole2 to javafx.base;
    opens guiStaffViewPost to javafx.base;
}