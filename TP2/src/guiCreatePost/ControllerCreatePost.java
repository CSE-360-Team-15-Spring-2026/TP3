package guiCreatePost;


/*******
 * <p> Title: ControllerCreatePost Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 1 Home Page.  This class provides the controller
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

public class ControllerCreatePost {

	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerCreatePost() {
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewCreatePost.theStage, ViewCreatePost.theUser);
	}	

	/**********
	 * <p> Method: performCreatePost() </p>
	 * 
	 * <p> Description: Validates and creates a new post. </p>
	 * 
	 */
	protected static void performCreatePost() {
		if (ViewCreatePost.theUser == null || ViewCreatePost.theUser.getUserName() == null
				|| ViewCreatePost.theUser.getUserName().isBlank()) {
			ViewCreatePost.showAlert("Error", "No valid user is logged in.");
			return;
		}

		String title = ViewCreatePost.text_PostTitle.getText();
		String body = ViewCreatePost.text_PostBody.getText();
		String threadName = null;

		if (ViewCreatePost.comboBox_ThreadName != null) {
			threadName = ViewCreatePost.comboBox_ThreadName.getValue();
		}

		if (title == null || title.trim().isEmpty()) {
			ViewCreatePost.showAlert("Validation Error", "Post title cannot be empty.");
			return;
		}

		if (body == null || body.trim().isEmpty()) {
			ViewCreatePost.showAlert("Validation Error", "Post body cannot be empty.");
			return;
		}

		if (threadName == null || threadName.isBlank()) {
			threadName = "General";
		}

		boolean success = ModelCreatePost.createPost(
				ViewCreatePost.theUser.getUserName(),
				title.trim(),
				body.trim(),
				threadName
		);

		if (success) {
			ViewCreatePost.showAlert("Success", "Post created successfully.");

			if (applicationMain.FoundationsMain.activeHomePage == 3) {
				guiRole2.ViewRole2Home.displayRole2Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
			} else {
				guiRole1.ViewRole1Home.displayRole1Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
			}
		} else {
			ViewCreatePost.showAlert("Error", "Failed to create post.");
		}
	}

	/**********
	 * <p> Method: performCancel() </p>
	 * 
	 * <p> Description: Cancels post creation and returns to the correct home page. </p>
	 * 
	 */
	protected static void performCancel() {
		if (applicationMain.FoundationsMain.activeHomePage == 3) {
			guiRole2.ViewRole2Home.displayRole2Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
		} else {
			guiRole1.ViewRole1Home.displayRole1Home(ViewCreatePost.theStage, ViewCreatePost.theUser);
		}
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreatePost.theStage);
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
}