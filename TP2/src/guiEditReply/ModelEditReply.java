package guiEditReply;

import entityClasses.Reply;
import database.Database;

/**
 * <p> Title: ModelEditReply Class </p>
 *
 * <p> Description: Model for editing replies </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 1.00 2025-03-25 Reply edit functionality
 */
public class ModelEditReply {

    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**
     * Update an existing reply
     */
    protected static boolean updateReply(Reply reply, String body) {
        if (reply == null) {
            return false;
        }
        if (body == null || body.isBlank()) {
            return false;
        }
        
        try {
            // Update the reply object
            reply.setBody(body.trim());
            
            // Use the existing updatePost method from Database
            // (works for replies too since Reply extends Post)
            return theDatabase.updatePost(reply);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}