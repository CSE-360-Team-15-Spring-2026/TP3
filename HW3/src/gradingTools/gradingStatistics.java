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
 * <p> This class has multiple functionality like: Checking for whether grading requirements were met, calculating the number of replies a student did (where only unique replies are counted: No Self-Replies, No Deleted Messages, No mutliple replies to the same person) </p>
 * @author Fauzan Amaan Mohammed
 * 
 *  */
public class gradingStatistics{
	
	/**
	 * <p> Default Constructor for the class => Unused in the program </p>
	 */
	public gradingStatistics() {}
	
	/**
	 * <p> Computes the number of people a user replied to and also excluding self-replies, multiple replies to the same author and deleted messages </p>
	 *
	 * @param studentUsername the student username
	 * @param listOfAllPosts the list of all posts
	 * @return the int
	 */
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
	
	/**
	 * Finds the username of the post
	 *
	 * @param parentPostID the parent post ID
	 * @param listOfAllPosts the list of all posts
	 * @return the string
	 */
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
	
	/**
	 * <p> Computes whether the student had replied to atleast 3 people </p>
	 *
	 * @param uniqueCount the unique count
	 * @return true, if successful
	 */
	public static boolean minimumRequirement(int uniqueCount) {
		// Fulfills requirement listed in HW3
		if (uniqueCount >= 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * <p> Returns the list of all the qualified replied students made </p>
	 *
	 * @param studentUsername the student username
	 * @param listOfAllPosts the list of all posts
	 * @return the replies by student
	 */
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
	
	/**
	 * <p> Lists all of the usernames of people who made posts </p>
	 *
	 * @param listOfAllPosts the list of all posts
	 * @return the all student usernames
	 */
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