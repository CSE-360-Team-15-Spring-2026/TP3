package gradingTools;

import entityClasses.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * <p> Title: Grading Statistics Class </p>
 * 
 * <p> Description: To be able to compute the statistics regarding student Participation. </p>
 * <p> Takes in the list of posts and returns the number of times each user have replied to posts uniquely and also not to their own posts. </p>
 * @author Fauzan Amaan Mohammed
 * 
 *  */
public class gradingStatistics{
	
	public static int countUniqueAuthors(String studentUsername, List<Post> listOfAllPosts) {
		
		// Username empty check
		if (studentUsername == null || studentUsername.isBlank() || studentUsername.isEmpty()) {
			return 0;
			
		}
		
		// No Posts Check
		if (listOfAllPosts == null || listOfAllPosts.isEmpty()) {
			return 0;
		}
		
		// Set of Strings of all unique Authors
		Set<String> uniqueUsernames = new HashSet<>();
		
		// Loop - To get all authors who replied to people
		for (int i = 0; i < listOfAllPosts.size(); i++) {
			Post post = listOfAllPosts.get(i);
			String parentAuthor = findParentAuthor(post.getParentPostID(), listOfAllPosts);
			
			// Check if reply
			if (post.getParentPostID() == -1) {
				continue;
			}
			
			// Needs to be the student being checked
			if (!post.getUsername().equals(studentUsername)) {
				continue;
			}
			
			// Skips Deleted Replies
			if (post.isDeleted()) {
				continue;
			}
			
			// parentAuthor can't be null => unlikely case
			if (parentAuthor == null) {
				continue;
			}
			
			// Can't be same the username of the Post Author
			if (parentAuthor.equals(studentUsername)) {
				continue;
			}
			
			// If no if statement worked, add username
			uniqueUsernames.add(parentAuthor);
		}
		
		return uniqueUsernames.size();
		
	}
	
	private static String findParentAuthor(int parentPostID, List<Post> listOfAllPosts) {
		for (int i = 0; i < listOfAllPosts.size(); i++) {
			Post tempPost = listOfAllPosts.get(i);
			// Find parent Post
			if (tempPost.getPostID() == parentPostID) {
				return tempPost.getUsername();
			}
		}
		
		// No Parent Post found
		return null;
	}
	
	public static boolean minimumRequirement(int uniqueCount) {
		if (uniqueCount >= 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static List<String> getRepliesByStudent(String studentUsername, List<Post> listOfAllPosts){
		List<String> possibleReplies = new ArrayList<>();
		
		// Username empty check
		if (studentUsername == null || studentUsername.isBlank() || studentUsername.isEmpty()) {
			return possibleReplies;
			
		}
		
		// No Posts Check
		if (listOfAllPosts == null || listOfAllPosts.isEmpty()) {
			return possibleReplies;
		}
		
		// Set of Strings of all unique Authors
		Set<String> uniqueUsernames = new HashSet<>();
		
		// Loop - To get all authors who replied to people
		for (int i = 0; i < listOfAllPosts.size(); i++) {
			Post post = listOfAllPosts.get(i);
			String parentAuthor = findParentAuthor(post.getParentPostID(), listOfAllPosts);
			
			// Check if reply
			if (post.getParentPostID() == -1) {
				continue;
			}
			
			// Needs to be the student being checked
			if (!post.getUsername().equals(studentUsername)) {
				continue;
			}
			
			// Skips Deleted Replies
			if (post.isDeleted()) {
				continue;
			}
			
			// parentAuthor can't be null => unlikely case
			if (parentAuthor == null) {
				continue;
			}
			
			// Can't be same the username of the Post Author
			if (parentAuthor.equals(studentUsername)) {
				continue;
			}
			
			String bodyContent = post.getBody();
			possibleReplies.add("Reply to " + parentAuthor + ": " + bodyContent);
			
		}
		
		return possibleReplies;
	}
	
	public static List<String> getAllStudentUsernames(List<Post> listOfAllPosts){
		List<String> listOfUsernames = new ArrayList<>();
		Set<String> setOfUsernames = new HashSet<>();
		
		if (listOfAllPosts == null || listOfAllPosts.isEmpty()) {
			return listOfUsernames;
		}
		
		for (int i = 0; i < listOfAllPosts.size(); i++) {
			String username = listOfAllPosts.get(i).getUsername();
			
			if (!setOfUsernames.contains(username)) {
				listOfUsernames.add(username);
				setOfUsernames.add(username);
			}
		}
		
		return listOfUsernames;
	}
	
}