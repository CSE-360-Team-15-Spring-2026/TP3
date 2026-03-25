package guiEditPost;

/**
 * <p> Title: ControllerEditPost Class </p>
 *
 * <p> Description: Controller for Edit Post page </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 2.00 2025-03-25 Complete edit functionality
 */
public class ControllerEditPost {

    /**
     * Save changes to the post
     */
    protected static void performSaveChanges() {
        String title = ViewEditPost.text_PostTitle.getText();
        String body = ViewEditPost.text_PostBody.getText();
        String threadName = ViewEditPost.comboBox_ThreadName.getValue();
        
        if (title == null || title.trim().isEmpty()) {
            ViewEditPost.showAlert("Validation Error", "Post title cannot be empty.");
            return;
        }
        
        if (body == null || body.trim().isEmpty()) {
            ViewEditPost.showAlert("Validation Error", "Post body cannot be empty.");
            return;
        }
        
        if (threadName == null || threadName.isBlank()) {
            threadName = "General";
        }
        
        boolean success = ModelEditPost.updatePost(
            ViewEditPost.thePost,
            title,
            body,
            threadName
        );
        
        if (success) {
            ViewEditPost.showAlert("Success", "Post updated successfully!");
            performCancel();  // Return to home
        } else {
            ViewEditPost.showAlert("Error", "Failed to update post.");
        }
    }
    
    /**
     * Cancel editing and return to home
     */
    protected static void performCancel() {
        if (applicationMain.FoundationsMain.activeHomePage == 2) {
            guiRole1.ViewRole1Home.displayRole1Home(
                ViewEditPost.theStage,
                ViewEditPost.theUser
            );
        } else {
            guiRole2.ViewRole2Home.displayRole2Home(
                ViewEditPost.theStage,
                ViewEditPost.theUser
            );
        }
    }
    
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
            ViewEditPost.theStage,
            ViewEditPost.theUser
        );
    }

    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewEditPost.theStage);
    }

    protected static void performQuit() {
        System.exit(0);
    }
}