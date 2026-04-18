package guiGraderFeedback;

import applicationMain.FoundationsMain;
import database.Database;
import entityClasses.Post;

/**
 * <p> Title: ModelGraderFeedback Class </p>
 *
 * <p> Description: Model for Grader feedback, manages feedback</p>
 */
public class ModelGraderFeedback {
	
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ModelGraderFeedback() {}
	
	private static Database db = applicationMain.FoundationsMain.database;
	
	/**
	 * 
	 * @param postID posts identification
	 * @param feedback reply from grader
	 * @param grader name of person who graded post
	 * 
	 * @return adds feedback into database
	 */
	public static boolean saveFeedback(Post post, String feedback, String grader) {
		if (post == null || feedback == null || feedback.isBlank()) {
			return false;
		}
		
		try {
			post.setFeedback(feedback);
			post.setFeedbackAuthor(grader);
			
			return db.updatePost(post);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}