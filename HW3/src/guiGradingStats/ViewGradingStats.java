package guiGradingStats;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;

// Entity Classes
import entityClasses.User;
import entityClasses.Post;

// List View and Observable View
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.List;
import java.util.ArrayList;



/*******
 * <p> Title: ViewGradingStats Class. </p>
 * 
 * <p> Description: The Java/FX-based Grading Statistics Page.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @author Fauzan Amaan Mohammed
 * 
 * @version 1.00		2025-04-20 Initial version
 *  
 */

public class ViewGradingStats {
	
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

	// GUI ARea 2: Contains widgets that display statistics to the user. 
	// Only Applicable to the Staff Role!
	
	protected static Label label_StudentList = new Label();
	
	protected static ListView<String> list_Students = new ListView<>();
	
	protected static ObservableList<String> studentData = FXCollections.observableArrayList();
	
	protected static Label label_ReplyExpansion = new Label();
	
	protected static ListView<String> list_Replies = new ListView<>();
	
	protected static ObservableList<String> repliesData = FXCollections.observableArrayList();
	
	protected static Button button_Refresh = new Button("Refresh");
	
	protected static List<Post> tempPosts = new ArrayList<>();
	
	protected static List<String> tempUsernames = new ArrayList<>();
	
	protected static Button button_back = new Button("Back to Staff Home Page");
	
	
	
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewGradingStats theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	
	private static Scene theGradingStatsScene;		// The shared Scene each invocation populates
	protected static final int theRole = 3;		// Admin: 1; Student: 2; Staff: 3

	/*-*******************************************************************************************

	Constructors
	
	 */

	/**********
	 * <p> Method: displayRole2Home(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role2 Home page to be displayed.
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
	public static void displayGradingStats(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewGradingStats();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());// Set the username

		ControllerGradingStats.loadStatistics();
		
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Staff Grading Statistics Page");
		theStage.setScene(theGradingStatsScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewGradingStats() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayGradingStats method.</p>
	 * 
	 */
	private ViewGradingStats() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theGradingStatsScene = new Scene(theRootPane, width, height);	// Create the scene
		
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Staff Grading Statistics Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerGradingStats.performUpdate(); });
		
		// GUI Area 2
		label_StudentList.setText("Student Discussion Replies Summary");
		setupLabelUI(label_StudentList, "Arial", 14, width - 40, Pos.BASELINE_LEFT, 20, 105);
		
		list_Students.setItems(studentData);
		setupListViewUI(list_Students, width - 40, 160, 20, 125);
		list_Students.setOnMouseClicked((_) -> { ControllerGradingStats.selectStudent(); });
		
		setupButtonUI(button_Refresh, "Dialog", 14, 100, Pos.CENTER, width - 130, 293);
		button_Refresh.setOnAction((_) -> { ControllerGradingStats.loadStatistics(); });
		
		label_ReplyExpansion.setText("Click on a student to get full replies");
		setupLabelUI(label_ReplyExpansion, "Arial", 14, width - 160, Pos.BASELINE_LEFT, 20, 293);
		
		list_Replies.setItems(repliesData);
		setupListViewUI(list_Replies, width - 40, 195, 20, 315);
		
		setupButtonUI(button_back, "Dialog", 18, 250, Pos.CENTER, 580, 540);
		button_back.setOnAction((_) -> { ControllerGradingStats.performBack(); });
		
		
		
		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerGradingStats.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerGradingStats.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
        theRootPane.getChildren().addAll(
    			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
    			label_StudentList, list_Students,
    			button_Refresh, label_ReplyExpansion, list_Replies,
    	        line_Separator4, button_Logout, button_Quit, button_back);
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
	
	private static void setupListViewUI(ListView<String> lv, double w, double h, double x, double y) {
	    lv.setPrefSize(w, h);
	    lv.setLayoutX(x);
	    lv.setLayoutY(y);
	}
}