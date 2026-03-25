package guiCreateReply;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Post;


/*******
 * <p> Title: GUIReviewerHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Role1 Home Page.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-20 Initial version
 *  
 */

public class ViewCreateReply {
	
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
	// would contain the widgets needed for the user to play the assigned role.
	protected static Label label_ReplySectionTitle = new Label();
	protected static Label label_OriginalPostTitle = new Label();
	protected static Label label_OriginalPostBody = new Label();
	protected static TextArea text_ReplyBody = new TextArea();
	protected static Button button_SubmitReply = new Button("Submit Reply");
	protected static Button button_CancelReply = new Button("Cancel");
	protected static Post thePost;
	
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewCreateReply theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	

	private static Scene theViewCreateReplyScene;	// The shared Scene each invocation populates
	protected static final int theRole = 2;		// Admin: 1; Role1: 2; Role2: 3

	/*-*******************************************************************************************

	Constructors
	
	 */


	/**********
	 * <p> Method: displayCreateReply(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role1 Home page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayCreateReply(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewCreateReply();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		label_PageTitle.setText("Create Reply");
		label_ReplySectionTitle.setText("Write Your Reply");
		text_ReplyBody.clear();

		if (thePost != null) {
			String postTitle = thePost.getTitle();
			String postBody = thePost.getBody();

			if (postTitle == null || postTitle.isBlank()) postTitle = "(No Title)";
			if (postBody == null || postBody.isBlank()) postBody = "(No content)";

			label_OriginalPostTitle.setText("Replying to: " + postTitle);
			label_OriginalPostBody.setText(postBody);
		} else {
			label_OriginalPostTitle.setText("Replying to: (No post selected)");
			label_OriginalPostBody.setText("");
		}
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Create Reply Page");
		theStage.setScene(theViewCreateReplyScene);
		theStage.show();
	}
	
	/**********
	 * <p> Overloaded Method: displayCreateReply(Stage ps, User user, Post post) </p>
	 * 
	 * <p> Description: Displays the Create Reply page with the selected post context. </p>
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and its methods
	 * @param user specifies the User for this GUI and its methods
	 * @param post specifies the Post being replied to
	 * 
	 */
	public static void displayCreateReply(Stage ps, User user, Post post) {
		thePost = post;
		displayCreateReply(ps, user);
	}
	
	/**********
	 * <p> Method: ViewCreateReply() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayRole2Home method.</p>
	 * 
	 */
	private ViewCreateReply() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theViewCreateReplyScene = new Scene(theRootPane, width, height);	// Create the scene
		
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Student Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: ");
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerCreateReply.performUpdate(); });
		
		// GUI Area 2
		
			// This is a stub, so this area is empty
		label_ReplySectionTitle.setText("Write Your Reply");
		setupLabelUI(label_ReplySectionTitle, "Arial", 22, 300, Pos.BASELINE_LEFT, 20, 115);

		label_OriginalPostTitle.setText("Replying to:");
		setupLabelUI(label_OriginalPostTitle, "Arial", 16, 740, Pos.BASELINE_LEFT, 20, 155);

		label_OriginalPostBody.setText("");
		setupLabelUI(label_OriginalPostBody, "Arial", 14, 740, Pos.BASELINE_LEFT, 20, 185);

		text_ReplyBody.setFont(Font.font("Arial", 16));
		text_ReplyBody.setLayoutX(20);
		text_ReplyBody.setLayoutY(240);
		text_ReplyBody.setMinWidth(740);
		text_ReplyBody.setMaxWidth(740);
		text_ReplyBody.setPrefWidth(740);
		text_ReplyBody.setPrefHeight(220);
		text_ReplyBody.setWrapText(true);
		text_ReplyBody.setPromptText("Type your reply here...");

		setupButtonUI(button_SubmitReply, "Dialog", 16, 180, Pos.CENTER, 20, 475);
		button_SubmitReply.setOnAction((_) -> {ControllerCreateReply.performSubmitReply(); });

		setupButtonUI(button_CancelReply, "Dialog", 16, 180, Pos.CENTER, 220, 475);
		button_CancelReply.setOnAction((_) -> {ControllerCreateReply.performCancel(); });
		
		
		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerCreateReply.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerCreateReply.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
         theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_ReplySectionTitle, label_OriginalPostTitle, label_OriginalPostBody,
			text_ReplyBody, button_SubmitReply, button_CancelReply,
	        line_Separator4, button_Logout, button_Quit);
}
	
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	protected static void showAlert(String title, String message) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
				javafx.scene.control.Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}