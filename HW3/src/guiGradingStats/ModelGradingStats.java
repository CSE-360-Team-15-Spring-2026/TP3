package guiGradingStats;

import java.util.List;
import entityClasses.Post;
import gradingTools.gradingStatistics;


/**
 * The Class ModelRole2Home.
 */
public class ModelGradingStats {

/*******
 * <p> Title: ModelGradingStats Class. </p>
 * 
 * <p> Description: The ModelGradingStats Page Model.  This class is a stub for future expansion.
 * 
 * This class is not used as there is no unique data manipulation for this GUI page.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @author Fauzan Amaan Mohammed
 * 
 * @version 1.00		2025-08-15 Initial version
 * @version 1.01		2025-09-13 Updated JavaDoc description
 *  
 */
	/**
	 * Default constructor. This Model class is a stub; no data is
	 * directly managed by this MVC component beyond what the database handles.
	 */
	public ModelGradingStats() {
	}
	
	public static List<Post> loadAllPosts(){
		return applicationMain.FoundationsMain.database.getAllPosts();
	}
	
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
	
	public static List<String> getUserReplies(String username, List<Post> listOfAllPosts){
		return gradingStatistics.getRepliesByStudent(username, listOfAllPosts);
	}

}
