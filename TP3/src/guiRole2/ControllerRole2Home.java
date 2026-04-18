package guiRole2;

import entityClasses.Post;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

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
     * <p> Loads every top-level post into the staff post table. </p>
     */
    public static void loadAllPosts() {
        List<Post> posts = ModelRole2Home.getAllPosts();
        ViewRole2Home.populatePostTable(posts);
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
     * <p> Prompts the staff member to create a new thread or delete an existing one.
     * Uses simple text-input dialogs backed by the fully implemented thread CRUD in Database. </p>
     */
    protected static void performManageThreads() {
        // Ask what they want to do
        Alert choice = new Alert(Alert.AlertType.CONFIRMATION);
        choice.setTitle("Manage Threads");
        choice.setHeaderText("What would you like to do?");
        choice.setContentText("Choose an action for discussion threads.");
        ButtonType btnCreate = new ButtonType("Create Thread");
        ButtonType btnDelete = new ButtonType("Delete Thread");
        ButtonType btnCancel = ButtonType.CANCEL;
        choice.getButtonTypes().setAll(btnCreate, btnDelete, btnCancel);

        Optional<ButtonType> action = choice.showAndWait();
        if (action.isEmpty() || action.get() == btnCancel) {
            return;
        }

        if (action.get() == btnCreate) {
            // Create a new thread
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Create Thread");
            nameDialog.setHeaderText("Enter a name for the new thread:");
            nameDialog.setContentText("Thread name:");
            Optional<String> name = nameDialog.showAndWait();
            if (name.isEmpty() || name.get().isBlank()) {
                return;
            }
            boolean created = ModelRole2Home.createThread(name.get().trim());
            if (created) {
                showAlert("Success", "Thread \"" + name.get().trim() + "\" created.");
            } else {
                showAlert("Failed",
                        "Could not create thread. It may already exist or the name is invalid.");
            }

        } else {
            // Delete an existing thread
            List<String> threads = ModelRole2Home.getAllThreads();
            threads.remove("General"); // General cannot be deleted

            if (threads.isEmpty()) {
                showAlert("No Threads", "There are no deletable threads (the General thread cannot be removed).");
                return;
            }

            // Show the list as a comma-separated hint
            TextInputDialog delDialog = new TextInputDialog();
            delDialog.setTitle("Delete Thread");
            delDialog.setHeaderText("Available threads (excluding General):\n"
                    + String.join(", ", threads));
            delDialog.setContentText("Thread to delete (exact name):");
            Optional<String> delName = delDialog.showAndWait();
            if (delName.isEmpty() || delName.get().isBlank()) {
                return;
            }

            String target = delName.get().trim();
            if (target.equals("General")) {
                showAlert("Not Allowed", "The General thread cannot be deleted.");
                return;
            }
            if (!threads.contains(target)) {
                showAlert("Not Found", "No thread named \"" + target + "\" exists.");
                return;
            }

            Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDel.setTitle("Confirm Delete Thread");
            confirmDel.setHeaderText(null);
            confirmDel.setContentText(
                    "Delete thread \"" + target + "\"?\n\nAll posts and replies in that thread will also be permanently removed.");
            Optional<ButtonType> confResult = confirmDel.showAndWait();
            if (confResult.isEmpty() || confResult.get() != ButtonType.OK) {
                return;
            }

            boolean deleted = ModelRole2Home.deleteThread(target);
            if (deleted) {
                showAlert("Deleted", "Thread \"" + target + "\" deleted.");
                loadAllPosts();
            } else {
                showAlert("Error", "Failed to delete thread \"" + target + "\".");
            }
        }
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