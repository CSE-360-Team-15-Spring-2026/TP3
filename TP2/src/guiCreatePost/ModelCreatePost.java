package guiCreatePost;

/**
 * The Class ModelCreatePost.
 */
public class ModelCreatePost {

/*******
 * <p> Title: ModelCreatePost Class. </p>
 * 
 * <p> Description: The Role1Home Page Model.  This class is a stub for future expansion.
 * 
 * This class is not used as there is no unique data manipulation for this GUI page.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 * @version 1.01		2025-09-13 Updated JavaDoc description
 *  
 */
	
	/**
	 * Default constructor. This Model class is a stub; no data is
	 * directly managed by this MVC component beyond what the database handles.
	 */
	public ModelCreatePost() {
	}

	/**********
	 * <p> Method: createPost(String username, String title, String body, String threadName) </p>
	 * 
	 * <p> Description: Creates a new post using the shared database object. </p>
	 * 
	 * @param username the logged-in user creating the post
	 * @param title the title of the post
	 * @param body the body of the post
	 * @param threadName the selected thread name
	 * 
	 * @return true if successful, else false
	 * 
	 */
	protected static boolean createPost(String username, String title, String body, String threadName) {
		try {
			if (username == null || username.isBlank()) return false;
			if (title == null || title.isBlank()) return false;
			if (body == null || body.isBlank()) return false;
			if (threadName == null || threadName.isBlank()) threadName = "General";

			applicationMain.FoundationsMain.database.createPost(
					username,
					title.trim(),
					body.trim(),
					"",
					threadName
			);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}