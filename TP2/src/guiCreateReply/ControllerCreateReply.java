package guiCreateReply;

/*******
 * <p> Title: ControllerCreateReply Class. </p>
 * 
 * <p> Description: The Java/FX-based Create Reply Page controller. This class
 * provides the controller actions based on the user's use of the JavaFX GUI widgets
 * defined by the View class.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author OpenAI
 * 
 * @version 1.00        2026-03-24 Initial implementation
 */

public class ControllerCreateReply {

    /**
     * Default constructor is not used.
     */
    public ControllerCreateReply() {
    }

    /**********
     * <p> Method: performCreateReply() </p>
     * 
     * <p> Description: Validates and submits the reply. </p>
     * 
     */
    protected static void performCreateReply() {
        if (ViewCreateReply.thePost == null) {
            ViewCreateReply.showAlert("Error", "No post is selected for reply.");
            return;
        }

        if (ViewCreateReply.theUser == null || ViewCreateReply.theUser.getUserName() == null
                || ViewCreateReply.theUser.getUserName().isBlank()) {
            ViewCreateReply.showAlert("Error", "No valid user is logged in.");
            return;
        }

        String replyBody = ViewCreateReply.text_ReplyBody.getText();

        if (replyBody == null || replyBody.trim().isEmpty()) {
            ViewCreateReply.showAlert("Validation Error", "Reply body cannot be empty.");
            return;
        }

        String threadName = ViewCreateReply.thePost.getThreadName();
        if (threadName == null || threadName.isBlank()) {
            threadName = "General";
        }

        boolean success = ModelCreateReply.createReply(
                ViewCreateReply.thePost.getPostID(),
                ViewCreateReply.theUser.getUserName(),
                replyBody,
                threadName
        );

        if (success) {
            ViewCreateReply.showAlert("Success", "Reply created successfully.");
            guiViewPost.ViewViewPost.displayViewPost(
                    ViewCreateReply.theStage,
                    ViewCreateReply.theUser,
                    ViewCreateReply.thePost
            );
        } else {
            ViewCreateReply.showAlert("Error", "Failed to create reply.");
        }
    }

    /**********
     * <p> Method: performSubmitReply() </p>
     * 
     * <p> Description: Wrapper method used by the View submit button to create the reply. </p>
     * 
     */
    protected static void performSubmitReply() {
        performCreateReply();
    }

    /**********
     * <p> Method: performCancel() </p>
     * 
     * <p> Description: Returns the user to the View Post page without creating a reply. </p>
     * 
     */
    protected static void performCancel() {
        guiViewPost.ViewViewPost.displayViewPost(
                ViewCreateReply.theStage,
                ViewCreateReply.theUser,
                ViewCreateReply.thePost
        );
    }

    /**********
     * <p> Method: performUpdate() </p>
     * 
     * <p> Description: Sends the user to the account update page. </p>
     * 
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
                ViewCreateReply.theStage,
                ViewCreateReply.theUser
        );
    }

    /**********
     * <p> Method: performLogout() </p>
     * 
     * <p> Description: Logs the user out and returns to the login page. </p>
     * 
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreateReply.theStage);
    }

    /**********
     * <p> Method: performQuit() </p>
     * 
     * <p> Description: Terminates the application. </p>
     * 
     */
    protected static void performQuit() {
        System.exit(0);
    }
}