package guiEditPost;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Post;

/**
 * <p> Title: ViewEditPost Class </p>
 * 
 * <p> Description: Edit an existing post </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @version 2.00 2025-03-25 Complete edit post functionality
 */
public class ViewEditPost {
    
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // GUI Area 1: Header
    protected static Label label_PageTitle = new Label();
    protected static Label label_UserDetails = new Label();
    protected static Button button_UpdateThisUser = new Button("Account Update");
    
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    // GUI Area 2: Edit Form
    protected static Label label_PostTitle = new Label("Post Title:");
    protected static TextField text_PostTitle = new TextField();
    
    protected static Label label_PostBody = new Label("Post Body:");
    protected static TextArea text_PostBody = new TextArea();
    
    protected static Label label_ThreadName = new Label("Thread:");
    protected static ComboBox<String> comboBox_ThreadName = new ComboBox<>();
    
    protected static Button button_SaveChanges = new Button("Save Changes");
    protected static Button button_Cancel = new Button("Cancel");
    
    protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
    
    // GUI Area 3: Navigation
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    private static ViewEditPost theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static User theUser;
    protected static Post thePost;  // The post being edited

    private static Scene theViewEditPostScene;
    protected static final int theRole = 2;

    /**
     * Display Edit Post page with the post to edit
     */
    public static void displayEditPost(Stage ps, User user, Post post) {
        theStage = ps;
        theUser = user;
        thePost = post;
        
        if (theView == null) {
            theView = new ViewEditPost();
        }
        
        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;
        
        label_UserDetails.setText("User: " + theUser.getUserName());
        
        // Load existing post data
        if (thePost != null) {
            text_PostTitle.setText(thePost.getTitle() == null ? "" : thePost.getTitle());
            text_PostBody.setText(thePost.getBody() == null ? "" : thePost.getBody());
            
            // Load threads and select current thread
            loadThreads();
            if (thePost.getThreadName() != null && !thePost.getThreadName().isBlank()) {
                comboBox_ThreadName.setValue(thePost.getThreadName());
            }
        } else {
            text_PostTitle.clear();
            text_PostBody.clear();
            loadThreads();
        }
        
        theStage.setTitle("CSE 360 Foundations: Edit Post");
        theStage.setScene(theViewEditPostScene);
        theStage.show();
    }
    
    /**
     * Load available threads into combo box
     */
    private static void loadThreads() {
        comboBox_ThreadName.getItems().clear();
        java.util.List<String> threads = theDatabase.getAllThreads();
        if (threads != null && !threads.isEmpty()) {
            comboBox_ThreadName.getItems().addAll(threads);
        }
        if (comboBox_ThreadName.getItems().isEmpty()) {
            comboBox_ThreadName.getItems().add("General");
        }
        if (comboBox_ThreadName.getValue() == null) {
            comboBox_ThreadName.setValue("General");
        }
    }
    
    /**
     * Constructor - creates all GUI elements
     */
    private ViewEditPost() {
        theRootPane = new Pane();
        theViewEditPostScene = new Scene(theRootPane, width, height);
        
        // GUI Area 1: Header
        label_PageTitle.setText("Edit Post");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
        
        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> {ControllerEditPost.performUpdate(); });
        
        // GUI Area 2: Edit Form
        setupLabelUI(label_PostTitle, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 115);
        
        text_PostTitle.setFont(Font.font("Arial", 16));
        text_PostTitle.setLayoutX(20);
        text_PostTitle.setLayoutY(145);
        text_PostTitle.setMinWidth(740);
        text_PostTitle.setMaxWidth(740);
        text_PostTitle.setPromptText("Enter post title...");
        
        setupLabelUI(label_PostBody, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 185);
        
        text_PostBody.setFont(Font.font("Arial", 16));
        text_PostBody.setLayoutX(20);
        text_PostBody.setLayoutY(215);
        text_PostBody.setMinWidth(740);
        text_PostBody.setMaxWidth(740);
        text_PostBody.setPrefHeight(200);
        text_PostBody.setWrapText(true);
        text_PostBody.setPromptText("Enter post body...");
        
        setupLabelUI(label_ThreadName, "Arial", 18, 200, Pos.BASELINE_LEFT, 20, 425);
        
        comboBox_ThreadName.setLayoutX(20);
        comboBox_ThreadName.setLayoutY(455);
        comboBox_ThreadName.setPrefWidth(300);
        comboBox_ThreadName.setPromptText("Select thread");
        
        setupButtonUI(button_SaveChanges, "Dialog", 16, 180, Pos.CENTER, 20, 490);
        button_SaveChanges.setOnAction((_) -> {ControllerEditPost.performSaveChanges(); });
        
        setupButtonUI(button_Cancel, "Dialog", 16, 180, Pos.CENTER, 220, 490);
        button_Cancel.setOnAction((_) -> {ControllerEditPost.performCancel(); });
        
        // GUI Area 3: Navigation
        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerEditPost.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerEditPost.performQuit(); });

        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
            label_PostTitle, text_PostTitle,
            label_PostBody, text_PostBody,
            label_ThreadName, comboBox_ThreadName,
            button_SaveChanges, button_Cancel,
            line_Separator4, button_Logout, button_Quit);
    }
    
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }
    
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}