package guiStaffViewPost;

import java.util.List;
import java.util.Optional;

import database.Database;
import entityClasses.Post;
import entityClasses.User;
import guiRole1.ControllerRole1Home;
import guiRole1.ModelRole1Home;
import guiRole1.ViewRole1Home;
import guiRole1.ViewRole1Home.PostDisplay;
import guiRole2.ControllerRole2Home;
import guiRole2.ViewRole2Home;
import guiViewPost.ControllerViewPost;
import guiViewPost.ViewViewPost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewStaffViewPost {
	/** Width of the GUI page */
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    
    /** Height of the GUI page */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Page title and user info
    /** Title of the page */
    protected static Label label_PageTitle = new Label();
    /** Username and Role of the user */
    protected static Label label_UserDetails = new Label();
    
    /** Line Separator between the page title, user details and account update button and role1 home gui */
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
    
 
    
    // Post table
    /** Table that displays all Posts */
    protected static TableView<PostDisplay> table_Posts = new TableView<>();
    /** List of all the Posts to be displayed in the table */
    protected static ObservableList<PostDisplay> postData = FXCollections.observableArrayList();
    
    /** Line Separator between the table and the buttons below it */
    protected static Line line_Separator3 = new Line(20, 480, width-20, 480);
    
    // Post action buttons
    /** Button that takes the user to the ViewPost GUI page for a specific post */
    protected static Button button_ViewPost = new Button("View Post & Replies");
    /** Button that allows the user to flag a post, given that it is theirs */
    protected static Button button_FlagPost = new Button("Flag Post");
    
    /** Line Separator between the buttons above and the log out and quit buttons */
    protected static Line line_Separator4 = new Line(20, 545, width-20, 545);
    
    // Navigation
    /** Button that logs the user out and takes them to the login page */
    protected static Button button_Logout = new Button("Logout");
    /** Button that terminates the program */
    protected static Button button_Quit = new Button("Quit");
    /** Button to return to the previous page */
    protected static Button button_Return = new Button("Return");

    /** Singleton instance of ViewViewPost */
    private static ViewStaffViewPost theView;
    /** JavaFX Stage used to display this page */
    protected static Stage theStage;
    /** Root pane containing all GUI elements */
    protected static Pane theRootPane;
    /** Currently logged-in user */
    protected static User theUser;
    /** The post currently being viewed */
    protected static Post thePost;
    /** Scene used for this page */
    private static Scene theScene = null;
    /** Tracks whether the user came from the main page or search page */
    public static String previousPageType = "main"; 
    
    private static TextInputDialog dialogProvideReason;

    /**
     * <p> Diplay post view for staff. </p>
     * <p> Calls ModelViewStaffViewPost and ControllerViewStaffViewPost to get the username and posts </p>
     * <p> Sets the title and scene </p>
     * @param ps: the Stage
     * @param user: the current user
     */
    public static void displayViewStaffViewPost(Stage ps, User user) {
        theStage = ps;
        theUser = user;
        
        if (theView == null) {
            theView = new ViewStaffViewPost();
        }
        
        // Initialize the model with current user
        ModelStaffViewPost.initialize(theUser.getUserName());
        
        // Load all posts by default
        ControllerStaffViewPost.loadAllPosts();
        

        
        label_UserDetails.setText("User: " + theUser.getUserName());
        
        theStage.setTitle("CSE 360 Foundations: Student Discussion System");
        theStage.setScene(theScene);
        theStage.show();
    }
    
    /**
     * <p> Constructor - creates all GUI elements, such as the Labels, Buttons and Table </p>
     */
    private ViewStaffViewPost() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);
        
        // Page title
        label_PageTitle.setText("Student Discussion System");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
        

        dialogProvideReason = new TextInputDialog("");

        // Table
        setupTableView();
        
        // Post actions
        setupButtonUI(button_ViewPost, "Dialog", 16, 180, Pos.CENTER, 20, 495);
        button_ViewPost.setOnAction((_) -> {ControllerStaffViewPost.viewPost(); });
        
        setupButtonUI(button_FlagPost, "Dialog", 16, 150, Pos.CENTER, 220, 495);
        button_FlagPost.setOnAction((_) -> {ControllerStaffViewPost.flagPost(); });
        
        // Navigation
        setupButtonUI(button_Return, "Dialog", 16, 150, Pos.CENTER, (width/2)-75, 555);
        button_Return.setOnAction((e) -> { ControllerStaffViewPost.performReturn(); });
        
        theRootPane.getChildren().addAll(
            label_PageTitle, 
            label_UserDetails,
            line_Separator1,
            table_Posts, 
            line_Separator3,
            button_ViewPost, button_FlagPost, line_Separator4,
            button_Return);
    }

    /**
     * <p> Setups the TableView with columns: ID, Title, Thread, Author, Replies, Status and Date. </p>
     */
    private void setupTableView() {
    	// ID Column (50px)
    	TableColumn<PostDisplay, Integer> colId = new TableColumn<>("ID");
    	colId.setCellValueFactory(new PropertyValueFactory<>("postId"));
    	colId.setPrefWidth(50);
    	colId.setMaxWidth(50);
    	colId.setMinWidth(50);

    	// Title Column (350px -> reduce this to make room for Thread)
    	TableColumn<PostDisplay, String> colTitle = new TableColumn<>("Title");
    	colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    	colTitle.setPrefWidth(250);  // CHANGED from 350 to 250

    	// Thread Column (100px) - NEW!
    	TableColumn<PostDisplay, String> colThread = new TableColumn<>("Thread");
    	colThread.setCellValueFactory(new PropertyValueFactory<>("thread"));
    	colThread.setPrefWidth(100);

    	// Author Column (120px)
    	TableColumn<PostDisplay, String> colAuthor = new TableColumn<>("Author");
    	colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    	colAuthor.setPrefWidth(120);

    	// Flags Column (80px)
    	TableColumn<PostDisplay, String> colFlags = new TableColumn<>("Flagged");
    	colFlags.setCellValueFactory(new PropertyValueFactory<>("flagged"));
    	colFlags.setPrefWidth(80);

    	// Status Column (80px)
    	TableColumn<PostDisplay, String> colStatus = new TableColumn<>("Status");
    	colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    	colStatus.setPrefWidth(80);

    	// Date Column (160px)
    	TableColumn<PostDisplay, String> colDate = new TableColumn<>("Date");
    	colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    	colDate.setPrefWidth(160);

    	// Add all columns to the table
    	table_Posts.getColumns().addAll(colId, colTitle, colThread, colAuthor, colFlags, colStatus, colDate);
        table_Posts.setItems(postData);
        
        table_Posts.setLayoutX(20);
        table_Posts.setLayoutY(115);
        table_Posts.setPrefWidth(760);
        table_Posts.setPrefHeight(355);
    }
    
    /**
     * <p> Populates the table with all the posts. </p>
     * @param posts: ArrayList of Posts to be displayed
     */
    protected static void populatePostTable(List<Post> posts) {
        
        postData.clear();
        for (Post post : posts) {
               postData.add(new PostDisplay(post));
        }
    }
    
    /**
     * <p> Shows alert dialogs </p>
     * @param title: text displayed at the top of the dialog window
     * @param message: the text in the body of the dialog
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * <p> Shows confirmation dialogs. </p>
     * 
     * @param title: the text displayed at the top of the dialog window
     * @param message: the text in the body of the dialog
     * @return true if the user click OK, false if the user clicks Cancel or closes the dialog window
     */
    protected static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        java.util.Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    
    /**
     * <p> Shows input dialogs. </p>
     * 
     * @param title: the text displayed at the top of the dialog window
     * @param message: the text in the body of the dialog
     * @return String reason to be inputted into the db
     */
    protected static String giveReason(String title, String message) {
    	dialogProvideReason.setTitle(title);
    	dialogProvideReason.setHeaderText(message);
    	
    	String reason = dialogProvideReason.showAndWait().get();
        return reason;
    }
    

    // Helper methods
    /**
     * <p> Helps setup Labels </p>
     * @param l: label name
     * @param ff: font name
     * @param f: font size
     * @param w: minimum width
     * @param p: alignment position
     * @param x: location on the x axis
     * @param y: location on the y axis
     */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }
    
    /**
     * <p> Helps setup Buttons </p>
     * @param b: button name
     * @param ff: font name
     * @param f: font size
     * @param w: minimum width
     * @param p: alignment position
     * @param x: location on the x axis
     * @param y: location on the y axis
     */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }
    
    /**
     * Inner class for TableView display
     * 
     */
    public static class PostDisplay {
    	/** Unique ID for the post */
        private final int postId;
        /** Title of the post */
        private String title;
        /** Thread the post is a part of */
        private final String thread;    
        /** Username of the author of the post */
        private final String author;
        /** Status of post flag */
        private final String flagged;
        /** Status of the post relative to the user viewing it */
        private final String status;
        /** Time and date the post was posted on */
        private final String timestamp;
        /** The Post object */
        private final Post post;

        /**
         * <p> Gets all the details of the Posts for the TableVie>w </p>
         * @param post: object of the current post
         */
        public PostDisplay(Post post) {
            this.post = post;
            this.postId = post.getPostID();
            this.thread = post.getThreadName();  // NEW!
            //if(post.isDeleted()) this.title = "[Deleted]";
            //else 				 this.title = post.getTitle();
            this.title = post.isDeleted() ? "[Deleted]" : post.getTitle();
            this.author = post.getUsername();
            this.flagged = post.isFlag() ? "YES" : "NO";
            this.status = ModelStaffViewPost.isRead(post.getPostID()) ? "READ" : "UNREAD";
            this.timestamp = ModelStaffViewPost.getFormattedTimestamp(post);
        }

        // Getters
        /** 
         * <p> Gets PostID </p> 
         * @return PostID
         */
        public int getPostId() { return postId; }
        /** 
         * <p> Gets Post Title </p> 
         * @return Title
         */
        public String getTitle() { return title; }
        /** 
         * <p> Gets the Thread </p> 
         * @return thread name
         */
        public String getThread() { return thread; } 
        /** 
         * <p> Gets the Username of the author </p> 
         * @return username of the author
         */
        public String getAuthor() { return author; }
        /** 
         * <p> Gets the flag status of post </p> 
         * @return flag status
         */
        public String getFlagged() { return flagged; }
        /** 
         * <p> Gets status: if read or unread </p> 
         * @return if the post has been read or not
         */
        public String getStatus() { return status; }
        /** 
         * <p> Gets Date and time of posting </p> 
         * @return date and time of posting
         */
        public String getTimestamp() { return timestamp; }
        /** 
         * <p> Gets the current Post's object </p> 
         * @return the post object
         */
        public Post getPost() { return post; }
    }
}
