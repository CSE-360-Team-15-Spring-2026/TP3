package guiEditPost;

import entityClasses.Post;
import database.Database;

/**
 * <p> Title: ModelEditPost Class </p>
 *
 * <p> Description: Model for editing posts </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 2.00 2025-03-25 Complete edit functionality with database update
 */
public class ModelEditPost {

    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**
     * Update an existing post
     */
    protected static boolean updatePost(Post post, String title, String body, String threadName) {
        if (post == null) {
            return false;
        }
        if (title == null || title.isBlank()) {
            return false;
        }
        if (body == null || body.isBlank()) {
            return false;
        }
        if (threadName == null || threadName.isBlank()) {
            threadName = "General";
        }
        
        try {
            // Update the post object fields
            post.setTitle(title.trim());
            post.setBody(body.trim());
            post.setThreadName(threadName);
            
            // CRITICAL: Save changes back to database
            return theDatabase.updatePost(post);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}