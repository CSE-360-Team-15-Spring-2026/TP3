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

//ViewStaffViewPost
//ModelStaffViewPost

public class ControllerStaffViewPost {

	public ControllerStaffViewPost() {}
	
    /**
     * <p> Loads all the posts into the table </p>
     */
    public static void loadAllPosts() {
        List<Post> posts = ModelStaffViewPost.getAllPosts();
        ViewStaffViewPost.populatePostTable(posts);
    }
    
    public static void filterPosts() {
        // Get input from text fields
        String thread = ViewStaffViewPost.comboBox_ThreadName.getValue();


        // Call model to search
        List<Post> results = ModelStaffViewPost.filter(thread);


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
                "Please select a post to flag.");
            return;
        }
        
        int postId = selected.getPostId();
        Post post = ModelStaffViewPost.getPostById(postId);
        
        if (post == null) {
        	ViewStaffViewPost.showAlert("Error", "Post not found.");
            return;
        }
        
        
        if (post.isFlag()) {
        	ViewStaffViewPost.showAlert("Error", "Post already flagged.");
            return;
        }
        
        // Confirmation dialog
        boolean confirmed = ViewStaffViewPost.showConfirmation("Confirm Flag", 
            "Are you sure you want to flag this post?\n\n" +
            "Title: " + post.getTitle() + "\n\n" +
            "Note: post will remain visible untill deleted.");
        
        
        if (confirmed) {
        	String reason = ViewStaffViewPost.giveReason("Reason","Input a reason");
        	
            boolean success = ModelStaffViewPost.flagPost(postId, reason);
            
            if (success) {
            	ViewStaffViewPost.showAlert("Success", "Post flagged.");
                loadAllPosts(); // Refreshes the table
            } else {
            	ViewStaffViewPost.showAlert("Error", "Failed to flag post.");
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
    }
    
    protected static void deleteThread(String thread) {
    	
    	boolean confirmed = ViewStaffViewPost.showConfirmation("Confirm Delete Thread", 
                "Are you sure you want to Delete this Thread?\n\n" +
                "Title: " + thread + "\n\n" +
                "Note: all posts within thisthread will be deleted.");
    	
    	if (confirmed) {
            boolean success = ModelStaffViewPost.deleteThread(thread);
            
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
