package guiRole2;

import java.util.List;

import entityClasses.Post;

import guiStaffViewPost.*;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 2 Home Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page is a stub for establish future roles for the application.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerRole2Home {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole2Home() {
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}
	
	/**
     * <p> Loads all the posts into the table </p>
     */
    public static void loadAllPosts() {
        List<Post> posts = ModelRole2Home.getAllPosts();
        ViewRole2Home.populatePostTable(posts);
    }

	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
	
	protected static void performGradingStats() {
		guiGradingStats.ViewGradingStats.displayGradingStats(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}
	
	protected static void performFeedback() {
		ViewRole2Home.PostDisplay selected = ViewRole2Home.table_Posts.getSelectionModel().getSelectedItem();
		
		if (selected == null) {
			ViewRole2Home.showAlert("Error", "No post was selected");
			return;
		}
		
		Post post = selected.getPost();
		
		guiGraderFeedback.ViewGraderFeedback.display(ViewRole2Home.theStage, ViewRole2Home.theUser, post);
	}
	
	protected static void performViewPosts() {
		guiStaffViewPost.ViewStaffViewPost.displayViewStaffViewPost(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}
}