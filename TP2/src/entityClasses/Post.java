package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Post Class </p>
 *
 * </p>
 *
 *
 *
 * @version 1.00   2026-03-24   Initial implementation
 */
public class Post {

	/*
	 * These are the private attributes for this entity object
	 */

	private int postID;
	private int parentPostID;
	private String username;
	private String title;
	private String body;
	private String threadName;
	private LocalDateTime timestamp;
	private boolean isDeleted;
	private String keywords;

	// --- Staff Epic attributes (TP3 readiness) ---

	private String feedback;
	private String feedbackAuthor;
	private boolean isFlagged;
	private String reason;


	/*-*******************************************************************************************
	 Constructors
	 */

	/*****
	 * <p> Method: Post() </p>
	 *
	 * <p> Description: Default constructor. Initialises all fields to safe default
	 * values. Follows the same pattern as the User default constructor. </p>
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
	 * <p> Method: Post(String username, String title, String body,
	 *                   String keywords, String threadName) </p>
	 *
	 * <p> Description: Constructor for creating a new top-level Post.
	 * Sets postID to -1 (assigned by DB on insert), timestamp to now,
	 * isDeleted to false, and all staff epic fields to defaults.
	 * Defaults threadName to "General" if null or blank. </p>
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


	/*-*******************************************************************************************
	 CRUD Operations
	 */

	/*****
	 * <p> Method: changeDelete() </p>
	 *
	 */
	public void changeDelete() {
		this.isDeleted = true;
	}

	/*****
	 * <p> Method: validateTitle() </p>
	 *
	 * @return true if valid or this is a reply, false otherwise
	 */
	public boolean validateTitle() {
		if (title == null) return true;
		return !title.isBlank() && title.length() <= 200;
	}

	/*****
	 * <p> Method: validateBody() </p>
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
	 * <p> Method: getPostID() </p>
	 * @return the postID
	 */
	public int getPostID() { return postID; }

	/*****
	 * <p> Method: setPostID(int postID) </p>
	 * @param postID the auto-incremented ID assigned by the database
	 */
	public void setPostID(int postID) { this.postID = postID; }

	/*****
	 * <p> Method: getParentPostID() </p>
	 * @return the parentPostID
	 */
	public int getParentPostID() { return parentPostID; }

	/*****
	 * <p> Method: setParentPostID(int parentPostID) </p>
	 * @param parentPostID the parent post ID, or -1 for top-level posts
	 */
	public void setParentPostID(int parentPostID) { this.parentPostID = parentPostID; }

	/*****
	 * <p> Method: getUsername() </p>
	 * @return the userName of the author
	 */
	public String getUsername() { return username; }

	/*****
	 * <p> Method: setUsername(String username) </p>
	 * <p> Description: Sets the userName of the author. </p>
	 * @param username the userName of the author
	 */
	public void setUsername(String username) { this.username = username; }

	/*****
	 * <p> Method: getTitle() </p>
	 * @return the title
	 */
	public String getTitle() { return title; }

	/*****
	 * <p> Method: setTitle(String title) </p>
	 * @param title the new title
	 */
	public void setTitle(String title) { this.title = title; }

	/*****
	 * <p> Method: getBody() </p>
	 * @return the body content
	 */
	public String getBody() { return body; }

	/*****
	 * <p> Method: setBody(String body) </p>
	 * @param body the new body content
	 */
	public void setBody(String body) { this.body = body; }

	/*****
	 * <p> Method: getThreadName() </p>
	 * @return the thread name
	 */
	public String getThreadName() { return threadName; }

	/*****
	 * <p> Method: setThreadName(String threadName) </p>
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = (threadName == null || threadName.isBlank()) ? "General" : threadName;
	}

	/*****
	 * <p> Method: getTimestamp() </p>
	 * @return the creation timestamp
	 */
	public LocalDateTime getTimestamp() { return timestamp; }

	/*****
	 * <p> Method: setTimestamp(LocalDateTime timestamp) </p>
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

	/*****
	 * <p> Method: isDeleted() </p>
	 * @return true if soft-deleted
	 */
	public boolean isDeleted() { return isDeleted; }

	/*****
	 * <p> Method: setDeleted(boolean isDeleted) </p>
	 * @param isDeleted true if deleted
	 */
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

	/*****
	 * <p> Method: getKeyWords() </p>
	 * @return the keywords
	 */
	public String getKeyWords() { return keywords; }

	/*****
	 * <p> Method: setKeyWords(String keywords) </p>
	 * @param keywords the new comma-separated keywords
	 */
	public void setKeyWords(String keywords) { this.keywords = keywords; }

	/*****
	 * <p> Method: getKeywords() </p>
	 * @return the keywords
	 */
	public String getKeywords() { return keywords; }

	/*****
	 * <p> Method: setKeywords(String keywords) </p>
	 * @param keywords the new comma-separated keywords
	 */
	public void setKeywords(String keywords) { this.keywords = keywords; }

	/*****
	 * <p> Method: getFeedback() </p>
	 * @return the staff feedback text
	 */
	public String getFeedback() { return feedback; }

	/*****
	 * <p> Method: setFeedback(String feedback) </p>
	 * @param feedback the feedback text written by the staff member
	 */
	public void setFeedback(String feedback) { this.feedback = feedback; }

	/*****
	 * <p> Method: getFeedbackAuthor() </p>
	 * @return the staff member's userName
	 */
	public String getFeedbackAuthor() { return feedbackAuthor; }

	/*****
	 * <p> Method: setFeedbackAuthor(String feedbackAuthor) </p>
	 * @param feedbackAuthor the staff member's userName
	 */
	public void setFeedbackAuthor(String feedbackAuthor) { this.feedbackAuthor = feedbackAuthor; }

	/*****
	 * <p> Method: isFlag() </p>
	 * @return true if flagged
	 */
	public boolean isFlag() { return isFlagged; }

	/*****
	 * <p> Method: isFlagged() </p>
	 * @return true if flagged
	 */
	public boolean isFlagged() { return isFlagged; }

	/*****
	 * <p> Method: setFlag(boolean isFlagged) </p>
	 * @param isFlagged true to flag this post, false to unflag
	 */
	public void setFlag(boolean isFlagged) {
		this.isFlagged = isFlagged;
		if (!isFlagged) this.reason = "";
	}

	/*****
	 * <p> Method: setFlagged(boolean isFlagged) </p>
	 * @param isFlagged true to flag this post, false to unflag
	 */
	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
		if (!isFlagged) this.reason = "";
	}

	/*****
	 * <p> Method: getReason() </p>
	 * @return the flag reason
	 */
	public String getReason() { return reason; }

	/*****
	 * <p> Method: setReason(String reason) </p>
	 * @param reason the reason for flagging
	 */
	public void setReason(String reason) { this.reason = reason; }
}