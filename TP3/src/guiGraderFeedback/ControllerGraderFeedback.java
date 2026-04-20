package guiGraderFeedback;

import entityClasses.Post; 
import guiGraderFeedback.ViewGraderFeedback;
import guiRole2.ControllerRole2Home;
import guiRole2.ViewRole2Home;


/*******
 * <p> Title: ControllerGraderFeedback Class. </p>
 * 
 * <p> Description: The Java/FX-based GraderFeedback.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page is a stub for establish future roles for the application.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */
public class ControllerGraderFeedback {
	
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerGraderFeedback() {}
	
	/**********
	 * <p> Method: studentfeedback() </p>
	 * 
	 * <p> Description: takes the selected post by grader in order to them write a feedback reply to show the student. </p>
	 * 
	 */
	public static void studentfeedback() {
		Post post = ViewGraderFeedback.selectedPost;
		String feedback = ViewGraderFeedback.textarea_Feedback.getText();
		
		if (post == null || ViewGraderFeedback.currentUser == null) {
			ViewGraderFeedback.showAlert("Error", "No post was selected/found");
			return;
		}
		
		if (feedback == null || feedback.trim().isEmpty()) {
			ViewGraderFeedback.showAlert("Error", "Feedback cannot be empty");
			return;
		}
		
		String grader = ViewGraderFeedback.currentUser.getUserName();
		
		boolean success = ModelGraderFeedback.saveFeedback(post, feedback, grader);
		
		if (success) {
			ViewGraderFeedback.selectedPost.setFeedback(feedback);
			ViewGraderFeedback.showAlert("Success", "Feedback successfully saved");
		} else {
			ViewGraderFeedback.showAlert("Error", "Feedback failed to save");
		}
		
		ControllerRole2Home.loadAllPosts();
		
	}
	

	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewGraderFeedback.theStage);
	}
}