package guiGradingRubric;

import java.util.ArrayList;
import java.util.List;

import entityClasses.User;
import grading.RubricCriterion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewGradingRubric Class. </p>
 *
 * <p> Description: The Java/FX-based Grading Rubric Page. Defines and lays out all GUI
 * widgets for the grading rubric view, including the student list, criterion spinners,
 * running total label, and action buttons. </p>
 */
public class ViewGradingRubric {

    /** The width of the application window. */
    private static double width  = applicationMain.FoundationsMain.WINDOW_WIDTH;

    /** The height of the application window. */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Header
    protected static Label label_PageTitle   = new Label("Grading Rubric");
    protected static Label label_UserDetails = new Label();
    protected static Line  line_Sep1         = new Line(20, 95, width - 20, 95);

    // Left column – student list
    protected static Label                  label_StudentsHeader = new Label("Students");
    protected static ListView<String>       list_Students        = new ListView<>();
    protected static ObservableList<String> studentData          = FXCollections.observableArrayList();
    protected static List<String>           tempUsernames        = new ArrayList<>();

    // Right column – rubric
    protected static Label              label_RubricHeader    = new Label("Rubric Criteria");
    protected static Label              label_SelectedStudent = new Label("No student selected");
    protected static List<Label>        criterionLabels       = new ArrayList<>();
    protected static List<Label>        descLabels            = new ArrayList<>();
    protected static List<Spinner<Integer>> criterionSpinners = new ArrayList<>();
    protected static Label              label_Total           = new Label("Total: 0 / 100");

    // Buttons
    /** Button to save the current spinner values as grades for the selected student. */
    protected static Button button_SaveGrades  = new Button("Save Grades");

    /** Button to clear all spinner values for the selected student. */
    protected static Button button_ClearGrades = new Button("Clear");

    /** Button to navigate back to the Role 2 Home page. */
    protected static Button button_Back        = new Button("Back");

    /** Button to log out the current user. */
    protected static Button button_Logout      = new Button("Logout");
    protected static Line   line_Sep2          = new Line(20, height - 85, width - 20, height - 85);

    // Singleton state
    private  static ViewGradingRubric theView;
    protected static Stage  theStage;
    protected static Pane   theRootPane;
    protected static User   theUser;
    protected static String currentStudent;
    private  static Scene   theScene;


    /*-*******************************************************************************************

    Entry point

     */

    /**
     * <p> Displays the Grading Rubric page on the given stage for the given user. </p>
     *
     * @param ps   the primary stage on which to display the page
     * @param user the currently logged-in user
     */
    public static void displayGradingRubric(Stage ps, User user) {
        theStage = ps;
        theUser  = user;
        if (theView == null) theView = new ViewGradingRubric();
        label_UserDetails.setText("User: " + user.getUserName());
        ControllerGradingRubric.initialize();
        theStage.setTitle("CSE 360 – Grading Rubric");
        theStage.setScene(theScene);
        theStage.show();
    }


    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used externally. Builds and lays out all GUI widgets.
     */
    private ViewGradingRubric() {
        theRootPane = new Pane();
        theScene    = new Scene(theRootPane, width, height);

        // Header
        setupLabel(label_PageTitle,   "Arial", 28, FontWeight.BOLD,   width, Pos.CENTER,       0,  5);
        setupLabel(label_UserDetails, "Arial", 16, FontWeight.NORMAL, 400,   Pos.BASELINE_LEFT, 20, 60);

        // Left: student list
        setupLabel(label_StudentsHeader, "Arial", 16, FontWeight.BOLD, 200, Pos.CENTER, 20, 105);
        list_Students.setItems(studentData);
        list_Students.setLayoutX(20);
        list_Students.setLayoutY(130);
        list_Students.setPrefWidth(230);
        list_Students.setPrefHeight(390);
        list_Students.getSelectionModel().selectedIndexProperty()
            .addListener((_obs, _old, _new) -> ControllerGradingRubric.selectStudent());

        // Right: rubric criteria rows
        setupLabel(label_RubricHeader,    "Arial", 16, FontWeight.BOLD,   420, Pos.CENTER,       270, 105);
        setupLabel(label_SelectedStudent, "Arial", 15, FontWeight.NORMAL, 420, Pos.BASELINE_LEFT, 270, 130);

        List<RubricCriterion> criteria = ModelGradingRubric.getCriteria();
        int startY = 158;
        int rowH   = 52;

        for (int i = 0; i < criteria.size(); i++) {
            RubricCriterion c = criteria.get(i);

            Label nameLabel = new Label(c.getName() + "  (max " + c.getMaxPoints() + " pts)");
            setupLabel(nameLabel, "Arial", 13, FontWeight.BOLD, 320, Pos.BASELINE_LEFT,
                       270, startY + i * rowH);
            criterionLabels.add(nameLabel);

            Label descLabel = new Label(c.getDescription());
            descLabel.setStyle("-fx-text-fill: #555555;");
            setupLabel(descLabel, "Arial", 11, FontWeight.NORMAL, 330, Pos.BASELINE_LEFT,
                       270, startY + i * rowH + 18);
            descLabels.add(descLabel);

            SpinnerValueFactory<Integer> svf =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, c.getMaxPoints(), 0);
            Spinner<Integer> spinner = new Spinner<>(svf);
            spinner.setLayoutX(620);
            spinner.setLayoutY(startY + i * rowH);
            spinner.setPrefWidth(90);
            spinner.setEditable(true);
            // Delegate live total refresh entirely to the controller
            spinner.valueProperty().addListener((_obs, _old, _new) ->
                ControllerGradingRubric.refreshTotal());
            criterionSpinners.add(spinner);
        }

        // Total
        setupLabel(label_Total, "Arial", 16, FontWeight.BOLD, 300, Pos.BASELINE_LEFT,
                   270, startY + criteria.size() * rowH + 8);

        // Buttons
        setupButton(button_SaveGrades,  "Dialog", 14, 150, Pos.CENTER, 270, height - 70);
        setupButton(button_ClearGrades, "Dialog", 14, 100, Pos.CENTER, 435, height - 70);
        setupButton(button_Back,        "Dialog", 14, 120, Pos.CENTER, 550, height - 70);
        setupButton(button_Logout,      "Dialog", 14, 120, Pos.CENTER, 685, height - 70);

        button_SaveGrades .setOnAction(_e -> ControllerGradingRubric.saveGrades());
        button_ClearGrades.setOnAction(_e -> ControllerGradingRubric.clearGrades());
        button_Back       .setOnAction(_e -> ControllerGradingRubric.performBack());
        button_Logout     .setOnAction(_e -> ControllerGradingRubric.performLogout());

        // Add everything to pane
        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, line_Sep1,
            label_StudentsHeader, list_Students,
            label_RubricHeader, label_SelectedStudent,
            label_Total, line_Sep2,
            button_SaveGrades, button_ClearGrades, button_Back, button_Logout
        );
        theRootPane.getChildren().addAll(criterionLabels);
        theRootPane.getChildren().addAll(descLabels);
        theRootPane.getChildren().addAll(criterionSpinners);
    }


    /*-*******************************************************************************************

    Private helpers

     */

    /**
     * <p> Configures a Label with the given font, size, weight, width, alignment,
     * and position. </p>
     *
     * @param l      the label to configure
     * @param font   the font family name
     * @param size   the font size
     * @param weight the font weight
     * @param minW   the minimum width
     * @param pos    the alignment
     * @param x      the x layout position
     * @param y      the y layout position
     */
    private static void setupLabel(Label l, String font, double size, FontWeight weight,
                                   double minW, Pos pos, double x, double y) {
        l.setFont(Font.font(font, weight, size));
        l.setMinWidth(minW);
        l.setAlignment(pos);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**
     * <p> Configures a Button with the given font, size, width, alignment,
     * and position. </p>
     *
     * @param b    the button to configure
     * @param font the font family name
     * @param size the font size
     * @param minW the minimum width
     * @param pos  the alignment
     * @param x    the x layout position
     * @param y    the y layout position
     */
    private static void setupButton(Button b, String font, double size,
                                    double minW, Pos pos, double x, double y) {
        b.setFont(Font.font(font, size));
        b.setMinWidth(minW);
        b.setAlignment(pos);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**
     * <p> Displays a standard informational alert dialog. </p>
     *
     * @param title   the dialog title
     * @param message the message body
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
