package entityClasses;

/*******
 * <p> Title: Reply Class </p>
 * 
 * <p> Description: Reply Class is the implementation for the reply aspect of the Student Discussion Post</p>
 * <p> The Reply class inherits the attributes and methods from the Post Class</p>
 *
 *
 *
 * @version 1.00  
 */
public class Reply extends Post {

	/*
	 * These are the private attributes for this entity object
	 */

	/**Unique Identifier for the ParentPostID to link to the Post*/
	private int parentPostID;


	/*-*******************************************************************************************
	 Constructors
	 */

	/*****
	 * <p> Constructs the basic Reply </p>
	 *
	 */
	public Reply() {
		super();
		this.parentPostID = -1;
		super.setParentPostID(-1);
		super.setTitle(null);
	}

	/*****
	 * <p> Constructs a Reply Object with the given paramters </p>
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
	 * <p> Returns the parent post ID </p>
	 *
	 *
	 * @return the postID of the parent
	 */
	public int getParentPostID() { return parentPostID; }

	/*****
	 * <p> Sets the Parent Post ID </p>
	 *
	 * @param parentPostID the postID of the parent Post or Reply
	 */
	public void setParentPostID(int parentPostID) {
		this.parentPostID = parentPostID;
		super.setParentPostID(parentPostID);
	}
}