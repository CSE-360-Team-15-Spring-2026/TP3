package guiGradingStats;

import java.util.List;
import java.util.ArrayList;
import entityClasses.Post;
import gradingTools.gradingStatistics;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: Copied File from Role 2 with more functionality for the Grading Statistics Functionality
 * 
 * Provides GUI implementation and controls for loading all the necessary statistics via the other View, Model and the seperate gradingStatistic package.
 * 
 * 
 * </p>
 * 
 * 
 * @author Lynn Robert Carter
 * @author Fauzan Amaan Mohammed
 * 
 */

public class ControllerGradingStats {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * <p> Default constructor is not used. </p>
	 */
	public ControllerGradingStats() {
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewGradingStats.theStage, ViewGradingStats.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewGradingStats.theStage);
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
	
	/**
	 * <p> Method: PerformBack() </p>
	 * 
	 * <p> Description: This method directs the user back to the guiRole2Home page to the user will be able to go back to navigation portal for more info. </p>
	 */
	protected static void performBack() {
		guiRole2.ViewRole2Home.displayRole2Home(ViewGradingStats.theStage, ViewGradingStats.theUser);
	}
	
	/**
	 * <p> Method: loadStatistics() </p>
	 * 
	 * <p> Description: This method loads all the statistics for every line of the ListView. 
	 * The method will get string to be listed in every row through a generated string method from the Model class. </p>
	 * 
	 * 
	 */
	protected static void loadStatistics() {
		List<Post> listOfAllPosts = ModelGradingStats.loadAllPosts();
		
		List<String> listOfUsernames = gradingStatistics.getAllStudentUsernames(listOfAllPosts);
		
		ViewGradingStats.studentData.clear();
		ViewGradingStats.repliesData.clear();
		
		if (listOfUsernames.isEmpty()) {
			ViewGradingStats.studentData.add("No students in the system");
			return;
		}
		
		for (int i = 0; i < listOfUsernames.size(); i++) {
			String temp = ModelGradingStats.getStudentStatisticsSummary(listOfUsernames.get(i),listOfAllPosts);
			ViewGradingStats.studentData.add(temp);
		}
		
		ViewGradingStats.tempPosts = listOfAllPosts;
		ViewGradingStats.tempUsernames = listOfUsernames;
		
	}
	
	
	/**
	 * <p> Method: selectStudent() </p>
	 * 
	 * <p> Description: This method checks whether the user has clicked on a row of the ListView.
	 * If the user has clicked, the second ListView for the replies will be updated with all the qualifying replies for that particular student </p>
	 */
	protected static void selectStudent() {
		int selectedIndex = ViewGradingStats.list_Students.getSelectionModel().getSelectedIndex();
		
		if (selectedIndex < 0) {
			return;
		}
		
		if (ViewGradingStats.tempUsernames == null || selectedIndex >= ViewGradingStats.tempUsernames.size()) {
			return;
		}
		
		String selectedUsername = ViewGradingStats.tempUsernames.get(selectedIndex);
		
		ViewGradingStats.repliesData.clear();
		
		List<String> replies = ModelGradingStats.getUserReplies(selectedUsername,  ViewGradingStats.tempPosts);
		
		if (replies.isEmpty()) {
			ViewGradingStats.repliesData.add("No replies (part of requirements) found for the username: " + selectedUsername);
			
		}
		else {
			for (int i = 0; i < replies.size(); i++) {
				ViewGradingStats.repliesData.add(replies.get(i));
			}
		}
		
		int count = gradingStatistics.countUniqueAuthors(selectedUsername, ViewGradingStats.tempPosts);
		
		boolean check = gradingStatistics.minimumRequirement(count);
		
		String outputMessage;
		if (check) {
			outputMessage = "Met";
		}
		else {
			outputMessage = "Not Met";
		}
		
		ViewGradingStats.label_ReplyExpansion.setText("Information about " + selectedUsername + " => Requirements " + outputMessage);
		
		
		
		
		
		
		
	}
}