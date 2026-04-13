package guiGradingStats;

import java.util.List;
import entityClasses.Post;
import gradingTools.gradingStatistics;


/*******
 * <p> Title: ModelGradingStats Class. </p>
 * 
 * <p> Description: The ModelGradingStats Page Model.  This class is a stub for future expansion.
 * 
 * This class is not used as there is no unique data manipulation for this GUI page.</p>
 * 
 * 
 * @author Lynn Robert Carter
 * @author Fauzan Amaan Mohammed
 * 
 *  
 */
public class ModelGradingStats {


	/**
	 * Default constructor. This Model class is a stub; no data is
	 * directly managed by this MVC component beyond what the database handles.
	 */
	public ModelGradingStats() {
	}
	
	/**
	 * <p> Loads every post via the database class </p>
	 *
	 * @return the list
	 */
	public static List<Post> loadAllPosts(){
		return applicationMain.FoundationsMain.database.getAllPosts();
	}
	
	/**
	 * <p> Generates a string that summarizes each students participation statistic </p>
	 *
	 * @param username the username
	 * @param listOfAllPosts the list of all posts
	 * @return the student statistics summary
	 */
	public static String getStudentStatisticsSummary(String username, List<Post> listOfAllPosts) {
		int count = gradingStatistics.countUniqueAuthors(username, listOfAllPosts);
		boolean check = gradingStatistics.minimumRequirement(count);
		
		int totalReplies = 0;
		for (int i = 0; i < listOfAllPosts.size(); i++) {
			Post tempPost = listOfAllPosts.get(i);
			
			if ((tempPost.getUsername().equals(username)) && (tempPost.getParentPostID() != -1) && (!tempPost.isDeleted())) {
				totalReplies++;
			}
		}
		
		String gradingRequirement;
		if (check) {
			gradingRequirement = "Yes";
		}
		
		else {
			gradingRequirement = "No";
		}
		
		String output = username + " |====| Replies: " + totalReplies + " |====| Replies Qualified: " + count + " |====| Grading Requirement Met: " + gradingRequirement;
		
		return output;
	}
	
	/**
	 * <p> Gets the student replies via the gradingStatistics class </p>
	 *
	 * @param username the username
	 * @param listOfAllPosts the list of all posts
	 * @return the user replies
	 */
	public static List<String> getUserReplies(String username, List<Post> listOfAllPosts){
		return gradingStatistics.getRepliesByStudent(username, listOfAllPosts);
	}

}
