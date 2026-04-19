package guiRole2;

import entityClasses.Post;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 *
 * <p> Description: The Java/FX-based Role 2 (Staff) Home Page Controller.  Handles all user
 * interactions from the Staff Home page.
 *
 * Staff-specific actions that are connected to fully implemented back-end code are wired through;
 * features that have not yet been implemented by the team show a "Not Implemented" alert instead
 * of silently doing nothing.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Agastya Raghav Iyengar
 *
 * @version 1.00    2025-04-20 Initial version for TP3
 */
public class ControllerRole2Home {

    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are static).
     */
    public ControllerRole2Home() {
    }

    /*-*******************************************************************************************

    Post-table loading

     */

    /**
     * <p> Opens the Staff View Post page which shows all posts with flag management. </p>
     */
    public static void loadAllPosts() {
        guiStaffViewPost.ViewStaffViewPost.displayViewStaffViewPost(
                ViewRole2Home.theStage, ViewRole2Home.theUser);
    }

    /*-*******************************************************************************************

    Post CRUD actions (implemented)

     */

    /**
     * <p> Opens the Create Post page so the staff member can write a new discussion post. </p>
     */
    protected static void performCreatePost() {
        guiCreatePost.ViewCreatePost.displayCreatePost(
                ViewRole2Home.theStage, ViewRole2Home.theUser);
    }

    /**
     * <p> Opens the Search Posts page. </p>
     */
    protected static void performSearch() {
        guiSearchPosts.ViewSearchPosts.displaySearchPosts(ViewRole2Home.theStage);
    }

    /**
     * <p> Opens the selected post's detail/reply view.
     * Shows an alert if no row is selected in the table. </p>
     */
    protected static void performViewPost() {
        ViewRole2Home.PostDisplay selected =
                ViewRole2Home.table_Posts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a post from the table first.");
            return;
        }

        Post post = ModelRole2Home.getPostById(selected.getPostId());
        if (post == null) {
            showAlert("Error", "Could not retrieve the selected post.");
            return;
        }

        guiViewPost.ViewViewPost.displayViewPost(
                ViewRole2Home.theStage, ViewRole2Home.theUser, post);
    }

    /**
     * <p> Opens the Edit Post page for the selected post.
     * Unlike the student role, staff may edit any post regardless of authorship. </p>
     */
    protected static void performEditPost() {
        ViewRole2Home.PostDisplay selected =
                ViewRole2Home.table_Posts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a post from the table first.");
            return;
        }

        Post post = ModelRole2Home.getPostById(selected.getPostId());
        if (post == null) {
            showAlert("Error", "Could not retrieve the selected post.");
            return;
        }
        if (post.isDeleted()) {
            showAlert("Error", "That post has already been deleted.");
            return;
        }

        guiEditPost.ViewEditPost.displayEditPost(
                ViewRole2Home.theStage, ViewRole2Home.theUser, post);
    }

    /**
     * <p> Soft-deletes the selected post after a confirmation dialog.
     * Staff may delete posts authored by any user. </p>
     */
    protected static void performDeletePost() {
        ViewRole2Home.PostDisplay selected =
                ViewRole2Home.table_Posts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a post from the table first.");
            return;
        }

        Post post = ModelRole2Home.getPostById(selected.getPostId());
        if (post == null) {
            showAlert("Error", "Could not retrieve the selected post.");
            return;
        }
        if (post.isDeleted()) {
            showAlert("Already Deleted", "That post has already been deleted.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText(
                "Delete this post?\n\nTitle: " + post.getTitle()
                + "\nAuthor: " + post.getUsername()
                + "\n\nReplies will remain visible.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        boolean ok = ModelRole2Home.deletePost(selected.getPostId());
        if (ok) {
            showAlert("Deleted", "Post deleted successfully.");
            loadAllPosts();
        } else {
            showAlert("Error", "Failed to delete the post.");
        }
    }

    /*-*******************************************************************************************

    Thread management (implemented)

     */

    /**
     * <p> Placeholder for the Manage Threads feature.
     * Shows a "Not Implemented" alert until the back-end is ready. </p>
     */
    protected static void performManageThreads() {
        showNotImplemented("Manage Threads",
                "Creating and deleting discussion threads has not been implemented yet.");
    }

    /*-*******************************************************************************************

    Staff tools — implemented

     */

    /**
     * <p> Opens the Grading Statistics page, already implemented by a teammate. </p>
     */
    protected static void performGradingStats() {
        guiGradingStats.ViewGradingStats.displayGradingStats(
                ViewRole2Home.theStage, ViewRole2Home.theUser);
    }

    /*-*******************************************************************************************

    Staff tools — not yet implemented (show informational alert)

     */

    /**
     * <p> Opens the Grader Feedback page for the selected post so the staff member can
     * write or update private feedback for the post's author. </p>
     */
    protected static void performPrivateFeedback() {
        ViewRole2Home.PostDisplay selected =
                ViewRole2Home.table_Posts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a post from the table first.");
            return;
        }

        Post post = ModelRole2Home.getPostById(selected.getPostId());
        if (post == null) {
            showAlert("Error", "Could not retrieve the selected post.");
            return;
        }
        if (post.isDeleted()) {
            showAlert("Deleted Post", "Cannot give feedback on a deleted post.");
            return;
        }

        guiGraderFeedback.ViewGraderFeedback.display(
                ViewRole2Home.theStage, ViewRole2Home.theUser, post);
    }

    /**
     * <p> Placeholder for the Grading Parameters CRUD feature.
     * Shows a "Not Implemented" alert until the back-end is ready. </p>
     */
    protected static void performGradingParameters() {
        showNotImplemented("Grading Parameters",
                "Creating and managing grading parameters has not been implemented yet.");
    }

    /**
     * <p> Placeholder for the Evaluate Performance / Assign Marks feature.
     * Shows a "Not Implemented" alert until the back-end is ready. </p>
     */
    protected static void performEvaluatePerformance() {
        showNotImplemented("Evaluate Performance",
                "Evaluating student discussion performance and assigning marks has not been implemented yet.");
    }

    /**
     * <p> Placeholder for the Admin Requests feature.
     * Shows a "Not Implemented" alert until the back-end is ready. </p>
     */
    protected static void performAdminRequests() {
        showNotImplemented("Admin Requests",
                "Submitting and viewing admin requests has not been implemented yet.");
    }

    /*-*******************************************************************************************

    Navigation

     */

    /**
     * <p> Opens the User Update page so the staff member can change account settings. </p>
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
                ViewRole2Home.theStage, ViewRole2Home.theUser);
    }

    /**
     * <p> Logs out the current staff user and returns to the login page. </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
    }

    /**
     * <p> Terminates the application. </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }

    /*-*******************************************************************************************

    Private helpers

     */

    /**
     * <p> Displays a standard informational alert. </p>
     *
     * @param title   the dialog title
     * @param message the message body
     */
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * <p> Displays a "Not Implemented" alert for stub features. </p>
     *
     * @param feature short feature name shown in the title
     * @param detail  additional context shown in the message body
     */
    private static void showNotImplemented(String feature, String detail) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not Implemented – " + feature);
        alert.setHeaderText("This feature has not been implemented yet.");
        alert.setContentText(detail);
        alert.showAndWait();
    }
}