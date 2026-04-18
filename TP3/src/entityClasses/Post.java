package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Post Class </p>
 *
 * <p> Description: An class under the entityclasses Package that implements all the necessary attributes and functionality for the Post object for a student discussion system.</p>
 * <p> The operations in this class provide input validation and changes the data values which is used by the different GUI packages and the database file to implement the Student Post</p>
 * <p> All the attributes and functionality was decided and implemented using the Student User Stories as well as the Staff EPICS</p>
 * <p> The class will contain the CRUD Functionality: </p>
 * <ul>
 * 	<li> Create: Using the Constructors</li>
 * 	<li> Read: Using the Getter Functions</li>
 * 	<li> Update: Using any of the Setter Functions</li>
 *  <li> Delete: Using the setter / getter function for the Delete-Related Attributes. And the changedelete() function</li>
 * </ul>
 * 
 * @version 1.00    
 */
public class Post {

	/** Unique integer identifier for each Post */
	private int postID;

	/** Parent post ID; -1 for top-level posts and link to PostID for replies to main posts */
	private int parentPostID;

	/** Username of the author of the Post */
	private String username;

	/** Title for the Post */
	private String title;

	/** Body of the Post */
	private String body;

	/** Thread name that is Post is under */
	private String threadName;

	/** Time of the Post Creation */
	private LocalDateTime timestamp;

	/** Check whether Post id deleted */
	private boolean isDeleted;

	/** List of keywords related to the Post */
	private String keywords;

	/** Staff feedback text for TP3 epics. */
	private String feedback;

	/** Username of staff member who gave feedback. */
	private String feedbackAuthor;

	/** Flag if post was flagged by staff. */
	private boolean isFlagged;

	/** Reason for staff flagging. */
	private String reason;
	



	/*-*******************************************************************************************
	 Constructors
	 */

	/*****
	 *
	 * <p> Constructs a default Post Object with an empty title, body, username, keywords, feedback, feedbackAuthor, reason as well as
	 * defaults the ID's to -1 and uses default variables values for threadName, isDeleted, isFlagged </p>
	 */
	public Post() {
		this.postID = -1;
		this.parentPostID = -1;
		this.username = "";
		this.title = "";
		this.body = "";
		this.keywords = "";
		this.threadName = "General";
		this.timestamp = LocalDateTime.now();
		this.isDeleted = false;
		this.feedback = "";
		this.feedbackAuthor = "";
		this.isFlagged = false;
		this.reason = "";
	}

	/*****
	 *
	 * <p> Constructs a Post Object with a given username, title, body, keywords and threadName.
	 * If threadName is empty, it will default to "General"</p>
	 *
	 * @param username   the userName of the student creating this post
	 * @param title      the post title
	 * @param body       the post body content
	 * @param keywords   comma-separated search keywords; may be empty string
	 * @param threadName the thread name; defaults to "General" if null or blank
	 */
	public Post(String username, String title, String body,
				String keywords, String threadName) {
		this.postID = -1;
		this.parentPostID = -1;
		this.username = username;
		this.title = title;
		this.body = body;
		this.keywords = keywords;
		this.threadName = (threadName == null || threadName.isBlank()) ? "General" : threadName;
		this.timestamp = LocalDateTime.now();
		this.isDeleted = false;
		this.feedback = "";
		this.feedbackAuthor = "";
		this.isFlagged = false;
		this.reason = "";
	}

	/*****
	 * <p> CRUD Operations </p>
	 *
	 * <p> These methods implement the create, read, update, and delete
	 * behaviors required by the Students User Stories. </p>
	 */



	/*****
	 * <p> Changes the Delete Boolean Value </p>
	 * <p> Useful for soft-delete and when used by staff</p>
	 *
	 */
	public void changeDelete() {
		this.isDeleted = true;
	}

	/*****
	 * <p> Checks whether the title provided is valid: Meaning it can't be empty or to long! </p>
	 *
	 * @return true if valid or this is a reply, false otherwise
	 */
	public boolean validateTitle() {
		if (title == null) return true;
		return !title.isBlank() && title.length() <= 200;
	}

	/*****
	 * <p> Checks whether the title provided is valid: Meaning it can't be empty </p>
	 *
	 * @return true if valid, false otherwise
	 */
	public boolean validateBody() {
		return body != null && !body.isBlank();
	}


	/*-*******************************************************************************************
	 Getters and Setters
	 */

	/*****
	 * <p> Returns the PostID </p>
	 * @return the postID
	 */
	public int getPostID() { 
		return postID; 
		}

	/*****
	 * <p> Sets the PostID from the given parameter </p>
	 * @param postID the auto-incremented ID assigned by the database
	 */
	public void setPostID(int postID) { 
		this.postID = postID; 
		}

	/*****
	 * <p> Returns the Parent PostID. If it is a Post, will return -1 which is the default value given by the Database  </p>
	 * @return the parentPostID
	 */
	public int getParentPostID() { 
		return parentPostID; 
		}

	/*****
	 * <p> Sets the ParentPostID from the given parameter </p>
	 * @param parentPostID the parent post ID, or -1 for top-level posts
	 */
	public void setParentPostID(int parentPostID) { 
		this.parentPostID = parentPostID; 
		}

	/*****
	 * <p> Returns the username of the author of the Post </p>
	 * @return the userName of the author
	 */
	public String getUsername() { 
		return username; 
		}

	/*****
	 * <p> Sets the username of the author of the Post </p>
	 * @param username the userName of the author
	 */
	public void setUsername(String username) { 
		this.username = username; 
		}

	/*****
	 * <p> Returns the title of the Post </p>
	 * @return the title
	 */
	public String getTitle() { 
		return title; 
		}

	/*****
	 * <p> Sets the title of the Post from the given parameter </p>
	 * @param title the new title
	 */
	public void setTitle(String title) { 
		this.title = title; 
		}

	/*****
	 * <p> Returns the body of the Post </p>
	 * @return the body content
	 */
	public String getBody() { 
		return body; 
		}

	/*****
	 * <p> Sets the body of the Post from the given parameter </p>
	 * @param body the new body content
	 */
	public void setBody(String body) { 
		this.body = body; 
		}

	/*****
	 * <p> Returns the threadName the Post is linked to </p>
	 * @return the thread name
	 */
	public String getThreadName() { 
		return threadName; 
		}

	/*****
	 * <p> Sets the ThreadName to the Post </p>
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = (threadName == null || threadName.isBlank()) ? "General" : threadName;
	}

	/*****
	 * <p> Returns the timestamp of the Post </p>
	 * @return the creation timestamp
	 */
	public LocalDateTime getTimestamp() { 
		return timestamp; 
		}

	/*****
	 * <p> Sets the timestamp of the Post </p>
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDateTime timestamp) { 
		this.timestamp = timestamp; 
		}

	/*****
	 * <p> Returns if the post is deleted </p>
	 * @return true if soft-deleted
	 */
	public boolean isDeleted() { 
		return isDeleted; 
		}

	/*****
	 * <p> Sets the boolean value of whether the Post is deleted </p>
	 * @param isDeleted true if deleted
	 */
	public void setDeleted(boolean isDeleted) { 
		this.isDeleted = isDeleted; 
		}

	/*****
	 * <p> Returns the keywords linked to the Post </p>
	 * @return the keywords
	 */
	public String getKeyWords() { 
		return keywords; 
		}

	/*****
	 * <p> Sets the keywords linked to the Post </p>
	 * @param keywords the new comma-separated keywords
	 */
	public void setKeyWords(String keywords) { 
		this.keywords = keywords; 
		}

//	/*****
//	 * <p> Method: getKeywords() </p>
//	 * @return the keywords
//	 */
//	public String getKeywords() { return keywords; }
//
//	/*****
//	 * <p> Method: setKeywords(String keywords) </p>
//	 * @param keywords the new comma-separated keywords
//	 */
//	public void setKeywords(String keywords) { this.keywords = keywords; }

	/*****
	 * <p> Returns Staff Feedback </p>
	 * <p> Staff EPICS Functionality </p>
	 * @return the staff feedback text
	 */
	public String getFeedback() { 
		return feedback; 
		}

	/*****
	 * <p> Sets the Feedback given by the Staff </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param feedback the feedback text written by the staff member
	 */
	public void setFeedback(String feedback) { 
		this.feedback = feedback; 
		}

	/*****
	 * <p> Returns the author of the Feedback given to the Post </p>
	 * <p> Staff EPICS Functionality </p>
	 * @return the staff member's userName
	 */
	public String getFeedbackAuthor() { 
		return feedbackAuthor; 
		}

	/*****
	 * <p> Sets the author of the Feedback given to a Post </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param feedbackAuthor the staff member's userName
	 */
	public void setFeedbackAuthor(String feedbackAuthor) { 
		this.feedbackAuthor = feedbackAuthor; 
		}

	/*****
	 * <p> Returns whether the Post has been flagged by the Staff </p>
	 * <p> Staff EPICS Functionality </p>
	 * @return true if flagged
	 */
	public boolean isFlag() { 
		return isFlagged; 
		}

	/*****
	 * <p> Sets the boolean value if the Post is supposed to be Flagged or not </p>
	 * <p> Staff EPICS Functionality </p>
	 * @return true if flagged
	 */
	public boolean isFlagged() { 
		return isFlagged; 
		}

	/*****
	 * <p> Method: setFlag(boolean isFlagged) </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param isFlagged true to flag this post, false to unflag
	 */
	public void setFlag(boolean isFlagged) {
		this.isFlagged = isFlagged;
		if (!isFlagged) {
			this.reason = "";
		}
	}

	/*****
	 * <p> Sets the boolean value if the Post is supposed to be Flagged or not </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param isFlagged true to flag this post, false to unflag
	 */
	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
		if (!isFlagged) {
			this.reason = "";
		}
	}

	/*****
	 * <p> Returns the reason for the Flag </p>
	 * <p> Staff EPICS Functionality </p>
	 * @return the flag reason
	 */
	public String getReason() { 
		return reason; 
		}

	/*****
	 * <p> Sets the reason for the Flag </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param reason the reason for flagging
	 */
	public void setReason(String reason) { 
		this.reason = reason; 
		}
	
	/*****
	 * <p> Checks to see whos allowed to see grader feedback </p>
	 * <p> Staff EPICS Functionality </p>
	 * @param current user
	 */
	public boolean feedbackVisibility(String currentUser) {
	    if (feedback == null || feedback.isBlank()) {
	    	return true;
	    }

	    return currentUser.equals(this.username) ||   // student
	           currentUser.equals(this.feedbackAuthor); // staff
	}
	
	
}



