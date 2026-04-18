package guiGraderFeedback;


import entityClasses.Post;
import entityClasses.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewGraderFeedback {
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    protected static Label label_PageTitle = new Label();
    protected static Label label_UserDetails = new Label();
    protected static Button button_UpdateUser = new Button("Account Update");

    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    protected static Label label_PostTitle = new Label("Post:");
    protected static Label label_PostBody = new Label();

    protected static Label label_Feedback = new Label("Feedback:");
    protected static TextArea textarea_Feedback = new TextArea();


    protected static Button button_SaveFeedback = new Button("Save Feedback");

    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);

    protected static Button button_Return = new Button("Return");
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    private static ViewGraderFeedback theView;
    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static User currentUser;
    protected static Post selectedPost;

    private static Scene scene;
	
	public static void display(Stage ps, User user, Post post) {
		theStage = ps;
		currentUser = user;
		selectedPost = post;
		
		if (theView == null) theView = new ViewGraderFeedback();

        label_UserDetails.setText("User: " + user.getUserName());

        if (post != null) {
            label_PostTitle.setText("Post: " + post.getTitle());
            label_PostBody.setText(post.getBody());
            if (post.getFeedback() == null || post.getFeedback().isBlank()) {
            	textarea_Feedback.setText("");
            	textarea_Feedback.setEditable(true);
            } else if (post.feedbackVisibility(user.getUserName())) {
            	textarea_Feedback.setText(post.getFeedback());
            } else {
            	textarea_Feedback.setText("You do not have accessibility to this content");
            	textarea_Feedback.setEditable(false);
            }
        }

        theStage.setScene(scene);
        theStage.setTitle("Grader Feedback");
        theStage.show();
	}
	
	private ViewGraderFeedback() {
        theRootPane = new Pane();
        scene = new Scene(theRootPane, width, height);

        // ===== AREA 1 =====
        label_PageTitle.setText("Grader Feedback");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        setupButtonUI(button_UpdateUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);

        // ===== AREA 2 =====
        setupLabelUI(label_PostTitle, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 120);
        setupLabelUI(label_PostBody, "Arial", 16, 700, Pos.BASELINE_LEFT, 20, 150);

        setupLabelUI(label_Feedback, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 220);
        textarea_Feedback.setLayoutX(20);
        textarea_Feedback.setLayoutY(250);
        textarea_Feedback.setPrefWidth(740);
        textarea_Feedback.setPrefHeight(100);

        setupButtonUI(button_SaveFeedback, "Dialog", 18, 200, Pos.CENTER, 20, 420);
        button_SaveFeedback.setOnAction(e -> ControllerGraderFeedback.studentfeedback());


        // ===== AREA 3 =====
        setupButtonUI(button_Return, "Dialog", 18, 200, Pos.CENTER, 240, 420);
        button_Return.setOnAction(e -> {
            guiRole2.ViewRole2Home.displayRole2Home(theStage, currentUser);
        });

        setupButtonUI(button_Logout, "Dialog", 18, 200, Pos.CENTER, 20, 480);
        button_Logout.setOnAction(e -> ControllerGraderFeedback.performLogout());

        setupButtonUI(button_Quit, "Dialog", 18, 200, Pos.CENTER, 240, 480);
        button_Quit.setOnAction(e -> System.exit(0));

        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateUser, line_Separator1,
            label_PostTitle, label_PostBody,
            label_Feedback, textarea_Feedback,
            button_SaveFeedback,
            line_Separator4, button_Return, button_Logout, button_Quit
        );
    }
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * <p> Private local method to initialize the standard fields for a label </p>
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
	 * <p> Private local method to initialize the standard fields for a button </p>
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

	
	public static void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
}