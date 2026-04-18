package guiStaffViewPost;

import java.util.List;
import java.util.Optional;

import entityClasses.Post;
import guiGradingStats.ViewGradingStats;
import guiRole1.ControllerRole1Home;
import guiRole1.ModelRole1Home;
import guiRole1.ViewRole1Home;
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
     * <p> Returns back to the Role1 home page </p>
     */
    protected static void performReturn() {
		guiRole2.ViewRole2Home.displayRole2Home(ViewStaffViewPost.theStage, ViewStaffViewPost.theUser);
    }
}
