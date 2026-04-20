package guiParticipationReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import database.Database;
import entityClasses.Post;
import entityClasses.User;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ViewParticipationReport Class </p>
 *
 * <p> Description: The JavaFX-based Student Participation Report page.  A staff
 * member selects a student from the drop-down list and immediately sees that
 * student's complete participation picture: how many posts they created, how many
 * replies they posted, how many unique peers they replied to, whether the grading
 * threshold is met, and a line-by-line listing of each post and reply showing any
 * flags or grader feedback attached to that content. </p>
 *
 * <p> This page satisfies User Story #7 – Student Participation Report Generation.
 * It pulls together data from the gradingStatistics utility (for counts and threshold)
 * and the Post entity's feedback and flag fields (added in TP3) so the grader gets
 * the complete picture in one place. </p>
 *
 * <p> Layout (top to bottom): </p>
 * <ul>
 *   <li> Area 1 – page title, logged-in user label, Account Update button </li>
 *   <li> Area 2 – student selector (ComboBox) </li>
 *   <li> Area 3 – one-line participation summary </li>
 *   <li> Area 4 – scrollable list of posts/replies with flag and feedback status </li>
 *   <li> Area 5 – Back to Staff Home, Logout, Quit </li>
 * </ul>
 *
 * <p> Tested by: ModelParticipationReportTest (JUnit 5) — GUI behavior covered by manual tests </p>
 *
 * @author Jack Ding
 * @version 1.00  2026-04-19  Initial implementation for TP3
 */
public class ViewParticipationReport {

    /*-*******************************************************************************************

    Attributes – window dimensions

     */

    /** Window width pulled from the shared application settings. */
    private static double width  = applicationMain.FoundationsMain.WINDOW_WIDTH;

    /** Window height pulled from the shared application settings. */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    /*-*******************************************************************************************

    Attributes – GUI widgets

     */

    // --- Area 1: Header ---

    /** Page title shown at the top of the window. */
    protected static Label  label_PageTitle      = new Label();

    /** Shows the username of the currently logged-in staff member. */
    protected static Label  label_UserDetails    = new Label();

    /** Opens the Account Update page. */
    protected static Button button_UpdateThisUser = new Button("Account Update");

    /** Horizontal separator below the header. */
    protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);

    // --- Area 2: Student selector ---

    /** Label prompting the user to pick a student. */
    protected static Label label_SelectStudent = new Label("Select a Student:");

    /** Drop-down list of all student usernames found in the post data. */
    protected static ComboBox<String> comboBox_Students = new ComboBox<>();

    /** Horizontal separator below the selector row. */
    protected static Line line_Separator2 = new Line(20, 145, width - 20, 145);

    // --- Area 3: Participation summary ---

    /** Static label above the summary line. */
    protected static Label label_SummaryTitle = new Label("Participation Summary:");

    /** Dynamically updated with the selected student's stats. */
    protected static Label label_Stats = new Label("Select a student above to view their report.");

    /** Horizontal separator below the summary. */
    protected static Line line_Separator3 = new Line(20, 210, width - 20, 210);

    // --- Area 4: Content detail list ---

    /** Label above the content ListView. */
    protected static Label label_ContentTitle =
            new Label("Posts and Replies  (Flag and Feedback Status):");

    /** Scrollable list showing each post/reply with flag and feedback info. */
    protected static ListView<String> list_Content = new ListView<>();

    /** Backing data for the content ListView; updated when a student is selected. */
    protected static ObservableList<String> contentData = FXCollections.observableArrayList();

    // --- Area 5: Navigation ---

    /** Horizontal separator above the navigation buttons. */
    protected static Line line_Separator4 = new Line(20, 497, width - 20, 497);

    /** Returns to the Staff Home page. */
    protected static Button button_Back   = new Button("Back to Staff Home");

    /** Logs out the current user. */
    protected static Button button_Logout = new Button("Logout");

    /** Terminates the application. */
    protected static Button button_Quit   = new Button("Quit");

    /*-*******************************************************************************************

    Attributes – shared references

     */

    /** Cached post list so we don't hit the database on every student selection. */
    protected static List<Post> allPosts = new ArrayList<>();

    /** Singleton instance; null until the page is first displayed. */
    private static ViewParticipationReport theView;

    /** Reference to the shared database. */
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /** The JavaFX Stage shared across all pages. */
    protected static Stage theStage;

    /** The root Pane that holds every widget. */
    protected static Pane theRootPane;

    /** The currently logged-in user. */
    protected static User theUser;

    /** The Scene for this page; created once and reused. */
    private static Scene theScene;

    /** Role constant used by the home-page dispatcher. */
    protected static final int theRole = 3;

    /*-*******************************************************************************************

    Entry point

     */

    /*****
     * <p> Method: displayParticipationReport(Stage ps, User user) </p>
     *
     * <p> Description: The single entry point called by the Staff Home page controller
     * to show this report.  On the first call it builds the widget tree (via the private
     * constructor) and on every subsequent call it simply refreshes the dynamic content
     * before making the Scene visible. </p>
     *
     * @param ps   the JavaFX Stage supplied by the application
     * @param user the currently logged-in staff member
     */
    public static void displayParticipationReport(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewParticipationReport();

        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;

        label_UserDetails.setText("User: " + theUser.getUserName());

        // Reload the student list each time the page is opened so new posts appear
        ControllerParticipationReport.loadStudents();

        theStage.setTitle("CSE 360 Foundations: Student Participation Report");
        theStage.setScene(theScene);
        theStage.show();
    }

    /*-*******************************************************************************************

    Constructor – builds the static widget tree

     */

    /*****
     * <p> Method: ViewParticipationReport() </p>
     *
     * <p> Description: Initialises every GUI widget once: position, size, font, and
     * event handlers.  Dynamic content (student list, stats, detail rows) is populated
     * by the Controller after this constructor finishes. </p>
     */
    private ViewParticipationReport() {
        theRootPane = new Pane();
        theScene    = new Scene(theRootPane, width, height);

        // --- Area 1: Header ---
        label_PageTitle.setText("Student Participation Report");
        setupLabelUI(label_PageTitle, "Arial", 26, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: " + theUser.getUserName());
        setupLabelUI(label_UserDetails, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 58);

        setupButtonUI(button_UpdateThisUser, "Dialog", 16, 170, Pos.CENTER, 610, 48);
        button_UpdateThisUser.setOnAction((_) -> { ControllerParticipationReport.performUpdate(); });

        // --- Area 2: Student selector ---
        setupLabelUI(label_SelectStudent, "Arial", 14, 160, Pos.BASELINE_LEFT, 20, 108);

        // ComboBox does not use setupButtonUI; position and width are set directly
        comboBox_Students.setPrefWidth(370);
        comboBox_Students.setLayoutX(185);
        comboBox_Students.setLayoutY(103);
        comboBox_Students.setOnAction((_) -> { ControllerParticipationReport.selectStudent(); });

        // --- Area 3: Participation summary ---
        setupLabelUI(label_SummaryTitle, "Arial", 13, width - 40, Pos.BASELINE_LEFT, 20, 153);
        setupLabelUI(label_Stats,        "Arial", 13, width - 40, Pos.BASELINE_LEFT, 20, 175);

        // --- Area 4: Content detail list ---
        setupLabelUI(label_ContentTitle, "Arial", 13, width - 40, Pos.BASELINE_LEFT, 20, 218);

        list_Content.setItems(contentData);
        // Width fills the pane; height spans from y=240 up to the separator at y=497
        list_Content.setPrefSize(width - 40, 247);
        list_Content.setLayoutX(20);
        list_Content.setLayoutY(240);

        // --- Area 5: Navigation ---
        setupButtonUI(button_Logout, "Dialog", 16, 220, Pos.CENTER,  20, 540);
        setupButtonUI(button_Quit,   "Dialog", 16, 220, Pos.CENTER, 270, 540);
        setupButtonUI(button_Back,   "Dialog", 16, 220, Pos.CENTER, 555, 540);

        button_Back  .setOnAction((_) -> { ControllerParticipationReport.performBack();   });
        button_Logout.setOnAction((_) -> { ControllerParticipationReport.performLogout(); });
        button_Quit  .setOnAction((_) -> { ControllerParticipationReport.performQuit();   });

        // Add every widget to the root pane
        theRootPane.getChildren().addAll(
                label_PageTitle, label_UserDetails, button_UpdateThisUser,
                line_Separator1,
                label_SelectStudent, comboBox_Students,
                line_Separator2,
                label_SummaryTitle, label_Stats,
                line_Separator3,
                label_ContentTitle, list_Content,
                line_Separator4,
                button_Back, button_Logout, button_Quit);
    }

    /*-*******************************************************************************************

    Helper methods

     */

    /*****
     * <p> Initialises the standard visual properties of a Label. </p>
     *
     * @param l  the Label to configure
     * @param ff font family name
     * @param f  font size
     * @param w  minimum width
     * @param p  text alignment
     * @param x  left edge (x-axis)
     * @param y  top edge (y-axis)
     */
    private static void setupLabelUI(Label l, String ff, double f, double w,
                                     Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /*****
     * <p> Initialises the standard visual properties of a Button. </p>
     *
     * @param b  the Button to configure
     * @param ff font family name
     * @param f  font size
     * @param w  minimum width
     * @param p  text alignment
     * @param x  left edge (x-axis)
     * @param y  top edge (y-axis)
     */
    private static void setupButtonUI(Button b, String ff, double f, double w,
                                      Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
