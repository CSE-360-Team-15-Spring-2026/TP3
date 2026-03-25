package entityClasses;

/*******
 * <p> Title: Reply Class </p>
 *
 *
 *
 * @version 1.00   2026-03-24   Initial implementation
 */
public class Reply extends Post {

	/*
	 * These are the private attributes for this entity object
	 */

	private int parentPostID;


	/*-*******************************************************************************************
	 Constructors
	 */

	/*****
	 * <p> Method: Reply() </p>
	 *
	 */
	public Reply() {
		super();
		this.parentPostID = -1;
		super.setParentPostID(-1);
		super.setTitle(null);
	}

	/*****
	 * <p> Method: Reply(int parentPostID, String username, String body) </p>
	 *
	 *
	 * @param parentPostID the postID of the Post or Reply being replied to; must be positive
	 * @param username     the userName of the student creating this reply
	 * @param body         the reply content
	 */
	public Reply(int parentPostID, String username, String body) {
		super(username, null, body, null, null);
		this.parentPostID = parentPostID;
		super.setParentPostID(parentPostID);
		super.setTitle(null);
		super.setThreadName("General");
	}


	/*-*******************************************************************************************
	 Getters and Setters
	 */

	/*****
	 * <p> Method: getParentPostID() </p>
	 *
	 *
	 * @return the postID of the parent
	 */
	public int getParentPostID() { return parentPostID; }

	/*****
	 * <p> Method: setParentPostID(int parentPostID) </p>
	 *
	 * @param parentPostID the postID of the parent Post or Reply
	 */
	public void setParentPostID(int parentPostID) {
		this.parentPostID = parentPostID;
		super.setParentPostID(parentPostID);
	}
}