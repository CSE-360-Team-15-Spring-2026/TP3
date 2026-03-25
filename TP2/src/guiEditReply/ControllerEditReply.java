package guiEditReply;

/**
 * <p> Title: ControllerEditReply Class </p>
 *
 * <p> Description: Controller for Edit Reply page </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 1.00 2025-03-25 Reply edit functionality
 */
public class ControllerEditReply {

    /**
     * Save changes to the reply
     */
    protected static void performSaveChanges() {
        String body = ViewEditReply.text_ReplyBody.getText();
        
        if (body == null || body.trim().isEmpty()) {
            ViewEditReply.showAlert("Validation Error", "Reply body cannot be empty.");
            return;
        }
        
        boolean success = ModelEditReply.updateReply(
            ViewEditReply.theReply,
            body
        );
        
        if (success) {
            ViewEditReply.showAlert("Success", "Reply updated successfully!");
            performCancel();  // Return to view post
        } else {
            ViewEditReply.showAlert("Error", "Failed to update reply.");
        }
    }
    
    /**
     * Cancel editing and return to view post
     */
    protected static void performCancel() {
        guiViewPost.ViewViewPost.displayViewPost(
            ViewEditReply.theStage,
            ViewEditReply.theUser,
            ViewEditReply.thePost
        );
    }
    
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
            ViewEditReply.theStage,
            ViewEditReply.theUser
        );
    }

    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewEditReply.theStage);
    }

    protected static void performQuit() {
        System.exit(0);
    }
}