package guiRole2;

import entityClasses.Post;
import entityClasses.Reply;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ModelRole2Home Class. </p>
 *
 * <p> Description: The Role2 (Staff) Home Page Model. Provides data-access helpers for the
 * staff home page. Staff members can view, create, edit, and delete any post or thread —
 * they are not restricted to their own content the way students are.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Agastya Raghav Iyengar
 *
 * @version 1.00    2025-04-20 Initial version for TP3
 */
public class ModelRole2Home {

    /** The username of the currently logged-in staff member. */
    private static String currentUser = "";

    /**
     * Default constructor – not used directly (all methods are static).
     */
    public ModelRole2Home() {
    }

    /**
     * <p> Stores the current staff user's username for later use in data-access methods. </p>
     * @param username the username of the logged-in staff member
     */
    public static void initialize(String username) {
        currentUser = username;
    }

    /**
     * <p> Returns the currently stored staff username. </p>
     * @return the current staff member's username
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * <p> Retrieves all top-level posts from the database. Staff can see every post. </p>
     * @return a list of top-level Post objects
     */
    public static List<Post> getAllPosts() {
        database.Database db = applicationMain.FoundationsMain.database;
        List<Post> allPosts = db.getAllPosts();
        List<Post> topLevel = new ArrayList<>();
        for (Post p : allPosts) {
            if (p.getParentPostID() == -1) {
                topLevel.add(p);
            }
        }
        return topLevel;
    }

    /**
     * <p> Fetches a single post by its unique ID. </p>
     * @param postId the postID to look up
     * @return the matching Post, or null if not found
     */
    public static Post getPostById(int postId) {
        return applicationMain.FoundationsMain.database.getPostByID(postId);
    }

    /**
     * <p> Soft-deletes any post. Staff can delete posts authored by any user. </p>
     * @param postId the postID of the post to delete
     * @return true if the delete succeeded, false otherwise
     */
    public static boolean deletePost(int postId) {
        Post post = getPostById(postId);
        if (post == null || post.isDeleted()) {
            return false;
        }
        post.changeDelete();
        return applicationMain.FoundationsMain.database.deletePost(post);
    }

    /**
     * <p> Returns the number of replies attached to the given post. </p>
     * @param postId the postID whose replies should be counted
     * @return the reply count
     */
    public static int getReplyCount(int postId) {
        List<Reply> replies =
                applicationMain.FoundationsMain.database.getRepliesForPost(postId);
        return replies.size();
    }

    /**
     * <p> Formats the post's timestamp as a human-readable string. </p>
     * @param post the post whose timestamp should be formatted
     * @return a formatted date-time string, or an empty string if no timestamp exists
     */
    public static String getFormattedTimestamp(Post post) {
        if (post.getTimestamp() == null) {
            return "";
        }
        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return post.getTimestamp().format(fmt);
    }

    /**
     * <p> Checks whether the current staff user has already read the specified post. </p>
     * @param postId the postID to check
     * @return true if the post has been marked read for this user
     */
    public static boolean isRead(int postId) {
        return applicationMain.FoundationsMain.database.isPostRead(currentUser, postId);
    }

    /**
     * <p> Creates a new discussion thread in the database. </p>
     * @param threadName the name for the new thread (must be unique and non-blank)
     * @return true if the thread was created, false if it already exists or is invalid
     */
    public static boolean createThread(String threadName) {
        if (threadName == null || threadName.isBlank()) {
            return false;
        }
        return applicationMain.FoundationsMain.database.createThread(threadName.trim(), currentUser);
    }

    /**
     * <p> Deletes a thread and all of its posts from the database.
     * The "General" thread cannot be deleted. </p>
     * @param threadName the name of the thread to delete
     * @return true if the thread was deleted, false otherwise
     */
    public static boolean deleteThread(String threadName) {
        if (threadName == null || threadName.equals("General")) {
            return false;
        }
        return applicationMain.FoundationsMain.database.deleteThread(threadName);
    }

    /**
     * <p> Returns all thread names currently in the database. </p>
     * @return a list of thread name strings
     */
    public static List<String> getAllThreads() {
        return applicationMain.FoundationsMain.database.getAllThreads();
    }
}