package guiCreateReply;

import database.Database;

/*******
 * <p> Title: ModelCreateReply Class. </p>
 * 
 * <p> Description: The Create Reply Page Model. This class handles the database
 * actions needed to create a reply for an existing post.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author OpenAI
 * 
 * @version 1.00        2026-03-24 Initial implementation
 */

public class ModelCreateReply {

    // Reference for the in-memory database so this package has access
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**********
     * <p> Method: createReply(...) </p>
     * 
     * <p> Description: Creates and stores a reply for an existing parent post. </p>
     * 
     * @param parentPostID the post being replied to
     * @param username the logged-in user creating the reply
     * @param replyBody the reply body text
     * @param threadName the thread name for the parent post
     * 
     * @return true if successful, else false
     * 
     */
    protected static boolean createReply(int parentPostID, String username, String replyBody, String threadName) {
        try {
            if (parentPostID < 0) return false;
            if (username == null || username.isBlank()) return false;
            if (replyBody == null || replyBody.isBlank()) return false;
            if (threadName == null || threadName.isBlank()) threadName = "General";

            theDatabase.createReply(username, replyBody.trim(), "", threadName, parentPostID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}