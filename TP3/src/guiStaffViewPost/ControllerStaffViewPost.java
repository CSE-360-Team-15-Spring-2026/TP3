package guiStaffViewPost;

import java.util.List;
import java.util.Optional;

import entityClasses.Post;
import guiGradingStats.ViewGradingStats;
import guiRole1.ControllerRole1Home;
import guiRole1.ModelRole1Home;
import guiRole1.ViewRole1Home;
import guiSearchPosts.ModelSearchPosts;
import guiSearchPosts.ViewSearchPosts;
import guiViewPost.ViewViewPost;

/*******
 * <p> Title: ControllerStaffViewPost Class. </p>
 *
 * <p> Description: The Java/FX-based Staff View Post Controller. Handles all user
 * interactions related to flags and threads management.
 *
 * This controller connects the View Staff post view with the View staff post view model and
 * database functionality. </p>
 */
public class ControllerStaffViewPost {

    /**
     * <p> Default constructor is not used. </p>
     */
	public ControllerStaffViewPost() {}
	
    /**
     * <p> Loads all the posts into the table </p>
     */
    public static void loadAllPosts() {
        List<Post> posts = ModelStaffViewPost.getAllPosts();
        ViewStaffViewPost.populatePostTable(posts);
    }
    
    /**
     * <p> Loads all the threads into the threads_table </p>
     */
    public static void loadAllThreads() {
        List<String> threads = ModelStaffViewPost.getAllThreads();
        ViewStaffViewPost.populateThreadsTable(threads);
    }
    
    /**
     * <p> filters posts table by thread </p>
     */
    public static void filterPosts() {
    	ViewStaffViewPost.ThreadDisplay selected = 
    			ViewStaffViewPost.table_Threads.getSelectionModel().getSelectedItem();
    	
        if (selected == null) {
        	ViewStaffViewPost.showAlert("No Selection", 
                "Please select a Thread to continue.");
            return;
        }
    	
        String threadName = selected.getThread().split("\\|")[1];

        // Call model to search
        List<Post> results = ModelStaffViewPost.filter(threadName);


        // Populate results table
        ViewStaffViewPost.populatePostTable(results);

    }
    
    /**
     * <p> Views the selected post by calling the guiViewPost package </p>
     * <p> Shows an alert message if no post is selected </p>
     */
    protected static void viewPost() {
    	ViewStaffViewPost.PostDisplay selected = 
        		ViewStaffViewPost.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
        	ViewStaffViewPost.showAlert("No Selection", 
                "Please select a post to view.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelRole1Home.getPostById(postId);
        
        if (post == null) {
        	ViewStaffViewPost.showAlert("Error", "Post not found.");
            return;
        }
        
        
        // Open view post dialog
        guiViewPost.ViewViewPost.displayViewPost(ViewStaffViewPost.theStage, 
        	ViewStaffViewPost.theUser, post);
    }
    
    /**
     * <p> Flags the selected post with confirmation and input a reason for it</p>
     */
    protected static void flagPost() {
    	ViewStaffViewPost.PostDisplay selected = 
    			ViewStaffViewPost.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
        	ViewStaffViewPost.showAlert("No Selection", 
                "Please select a post to continue.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelStaffViewPost.getPostById(postId);
        
        if (post == null) {
        	ViewStaffViewPost.showAlert("Error", "Post not found.");
            return;
        }
        
        
        if (!post.isFlag()) {
        	ViewStaffViewPost.showAlert("Error", "Post already unflagged.");
            return;
        }
        
        // Confirmation dialog
        boolean ok = ViewStaffViewPost.showConfirmation("Confirm Flag", 
            "Are you sure you want to unflag this post?\n\n" +
            "Title: " + post.getTitle() + "\n\n" +
            "Note: will be visible to all users now.");
        
        
        if (ok) {
            boolean success = ModelStaffViewPost.flagPost(postId);
            
            if (success) {
            	ViewStaffViewPost.showAlert("Success", "Post unflagged.");
                loadAllPosts(); // Refreshes the table
            } else {
            	ViewStaffViewPost.showAlert("Error", "Failed to unflag post.");
            }
        }
    }
    
    /**
     * <p> deletes flagged posts, if already deleted(soft deleted) it will permanently remove from database</p>
     */
    protected static void deletePost() {
    	ViewStaffViewPost.PostDisplay selected = 
    			ViewStaffViewPost.table_Posts.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
        	ViewStaffViewPost.showAlert("No Selection", 
                "Please select a post to remove.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelStaffViewPost.getPostById(postId);
        
        if (post == null) {
        	ViewStaffViewPost.showAlert("Error", "Post not found.");
            return;
        }
        
        // Check if post is flagged/deleted
        if (!post.isFlag()) {
        	ViewStaffViewPost.showAlert("Review", 
                "You can only remove flagged posts.");
            return;
        }
        
        
        // Confirmation dialog
        boolean confirmed;
        if(!post.isDeleted()) {
        	confirmed = ViewStaffViewPost.showConfirmation("Confirm remove", 
                    "Are you sure you want to Remove this post?\n\n" +
                    "Title: " + post.getTitle() + "\n\n" +
                    "Note: Replies to this post will remain visible.");
        } else {
        	confirmed = ViewStaffViewPost.showConfirmation("Confirm remove", 
                    "Are you sure you want to Remove this post?\n\n" +
                    "By: " + post.getUsername() + "\n\n" +
                    "Note: post will be permanently removed from database.");
        }
        
        if (confirmed) {
        	boolean success;
        	if(post.isDeleted() )success = ModelStaffViewPost.removePost(postId);
        	else success = ModelStaffViewPost.deletePost(postId);
            
            if (success) {
            	ViewStaffViewPost.showAlert("Success", "Post deleted successfully.");
                loadAllPosts(); // Refreshes the table
            } else {
            	ViewStaffViewPost.showAlert("Error", "Failed to delete post.");
            }
        }
    }
    
    /**
     * <p> Pop-up for the staff to give a name for new thread.</p>
     */
    protected static void createThread() {
    	String threadName = ViewStaffViewPost.giveReason("Create Thread", "Give it a name");
    	
    	boolean success = ModelStaffViewPost.createThread(threadName);
    	
    	if (success) {
    		ViewStaffViewPost.showAlert("Success", "New Thread Created.");
    	} else {
    		ViewStaffViewPost.showAlert("Error", "Failed to create thread.");
    	}
    }//String result = str.split("\\|")[1];
    
    /**
     * <p> Deletes the selected Thread after a confirmation dialog </p>.
     */
    protected static void deleteThread() {
    	
    	ViewStaffViewPost.ThreadDisplay selected = 
    			ViewStaffViewPost.table_Threads.getSelectionModel().getSelectedItem();
    	
        if (selected == null) {
        	ViewStaffViewPost.showAlert("No Selection", 
                "Please select a Thread to continue.");
            return;
        }
        
    	String threadName = selected.getThread().split("\\|")[1];
    	
    	
    	boolean confirmed = ViewStaffViewPost.showConfirmation("Confirm Delete Thread", 
                "Are you sure you want to Delete this Thread?\n\n" +
                "Title: " + threadName + "\n\n" +
                "Note: all posts within this thread will be deleted.");
    	
    	if (confirmed) {
            boolean success = ModelStaffViewPost.deleteThread(threadName);
            
            if (success) {
            	ViewStaffViewPost.showAlert("Success", "Thread Deleted.");
                loadAllPosts(); // Refreshes the table
            } else {
            	ViewStaffViewPost.showAlert("Error", "Failed to Delete Thread.");
            }
        }
    	
    }

    
    /**
     * <p> Returns back to the Role1 home page </p>
     */
    protected static void performReturn() {
		guiRole2.ViewRole2Home.displayRole2Home(ViewStaffViewPost.theStage, ViewStaffViewPost.theUser);
    }
}
