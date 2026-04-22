package guiStaffViewPost;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.Post;
import entityClasses.Reply;


/*******
 * <p> Title: ModelStaffViewPost Class. </p>
 *
 * <p> Description: The Staff Post View Model. Provides data-access methods for managing threads and post flags
 *
 * This model class connects the staff view post GUI to the database. </p>
 */
public class ModelStaffViewPost {
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ModelStaffViewPost() {}
	
    /** Initializes the current username with an empty String */
    private static String currentUser = "";

    /**
     * <p> Initializes with current user </p>
     * @param username: the current user's username
     */
    public static void initialize(String username) {
        currentUser = username;
    }
    
	/**
	 * <p> get current user </p>
	 * @return string of current user name
	 */
    public static String getCurrentUser() {
        return currentUser;
    }
    
	/**
	 * <p> get all posts in a post list from the database </p>
	 * @return a post list of top level post
	 */
    public static List<Post> getAllPosts() {
        database.Database db = applicationMain.FoundationsMain.database;
        List<Post> allPosts = db.getAllPosts();
        
        // Filter out replies (only show top-level posts)
        List<Post> topLevelPosts = new ArrayList<>();
        for (Post post : allPosts) {
            
            if (post.getParentPostID() == -1) {  // -1 means top-level post
                topLevelPosts.add(post);
            }
        }
        return topLevelPosts;
    }
	/**
	 * <p> get all threads in a string list from the database </p>
	 * @return a String list of all the thread names
	 */
    public static List<String> getAllThreads() {
        database.Database db = applicationMain.FoundationsMain.database;
        List<String> allThreads = db.getAllThreads();
        

        return allThreads;
    }
    
	/**
	 * <p> get a filter post list with given thread name </p>
	 * @param thread: string name of thread
	 */
    public static List<Post> filter(String thread) {
        return applicationMain.FoundationsMain.database.searchPosts(thread);
    }
    
	/**
	 * <p> flags an existing post in the database and inputs a reason </p>
	 * @param postID: the integer value of the post ID
	 * @return boolean value true if deleted, false if not
	 */
    public static boolean flagPost(int postId) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        
        post.setFlag(false);
        return applicationMain.FoundationsMain.database.flagPost(post);
    }
	/**
	 * <p> flags an existing post in the database and inputs a reason </p>
	 * @param postID: the integer value of the post ID
	 * @return boolean value true if deleted, false if not
	 */
    public static boolean deletePost(int postId) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        
        post.changeDelete();
        return applicationMain.FoundationsMain.database.deletePost(post);
    }
	/**
	 * <p> removes an existing post in the database </p>
	 * @param postID: the integer value of post ID
	 * @return boolean value true if removed, false if not
	 */
    public static boolean removePost(int postId) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        
        post.changeDelete();
        return applicationMain.FoundationsMain.database.removePost(post);
    }
    
	/**
	 * <p> Creates a new thread and inputs it in the database </p>
	 * @param name: string name of new thread
	 * @return true if created new thread, else false
	 */
    protected static boolean createThread(String name) {
    	
    	return applicationMain.FoundationsMain.database.createThread(name, currentUser);
    }
    
	/**
	 * <p> deletes a thread from the database </p>
	 * @param thread: String name of thread to be deleted
	 * @return boolean value true if deleted, false if not
	 */
    protected static boolean deleteThread(String thread) {
    	return applicationMain.FoundationsMain.database.deleteThread(thread);
    }
    
	/**
	 * <p> formats date data from database for the table view</p>
	 * @param post: post to have date formated
	 * @return string of formated time stamp
	 */
    public static String getFormattedTimestamp(Post post) {
        if (post.getTimestamp() == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return post.getTimestamp().format(formatter);
    }
    
	/**
	 * <p> get post by postID </p>
	 * @param postId: integer value of post ID
	 * @return post
	 */
    public static Post getPostById(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getPostByID(postId);
    }
}
