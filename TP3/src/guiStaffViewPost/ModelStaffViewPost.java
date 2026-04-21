package guiStaffViewPost;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.Post;
import entityClasses.Reply;

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
	 */
    public static String getCurrentUser() {
        return currentUser;
    }
    
	/**
	 * <p> get all posts in a post list from the database </p>
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
	 */
    public static List<String> getAllThreads() {
        database.Database db = applicationMain.FoundationsMain.database;
        List<String> allThreads = db.getAllThreads();
        

        return allThreads;
    }
    
	/**
	 * <p> get a filter post list with given thread name </p>
	 */
    public static List<Post> filter(String thread) {
        return applicationMain.FoundationsMain.database.searchPosts(thread);
    }
    
	/**
	 * <p> flags an existing post in the database and inputs a reason </p>
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
	 * <p> flags an existing post in the database and inputs a reason </p>
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
	 */
    protected static boolean createThread(String name) {
    	
    	return applicationMain.FoundationsMain.database.createThread(name, currentUser);
    }
    
	/**
	 * <p> deletes a thread from the database </p>
	 */
    protected static boolean deleteThread(String thread) {
    	return applicationMain.FoundationsMain.database.deleteThread(thread);
    }
    
	/**
	 * <p> formates date data from database for the table view</p>
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
	 */
    public static Post getPostById(int postId) {
        database.Database db = applicationMain.FoundationsMain.database;
        return db.getPostByID(postId);
    }
}
