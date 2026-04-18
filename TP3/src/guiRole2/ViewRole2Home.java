package guiRole2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import entityClasses.Post;
import java.util.List;

/*******
 * <p> Title: ViewRole2Home Class. </p>
 *
 * <p> Description: The Java/FX-based Staff Home Page.  Staff members can browse all discussion
 * posts, perform full CRUD on any post or thread, search content, view grading statistics, and
 * access future staff-only features.  Buttons for features that are not yet implemented display
 * a clear "Not Implemented" message rather than failing silently. </p>
 *
 * <p> Layout (top to bottom): </p>
 * <ul>
 *   <li> Area 1 – page title, logged-in user, Account Update button </li>
 *   <li> Area 2 – post-management action row (Create, View All, Search, Manage Threads) </li>
 *   <li> Area 3 – post table </li>
 *   <li> Area 4 – post-level action row (View Post, Edit Post, Delete Post) </li>
 *   <li> Area 5 – staff-tools row (Grading Stats, Private Feedback*, Grading Params*, Admin Requests*) </li>
 *   <li> Area 6 – Logout / Quit </li>
 * </ul>
 * <p> * = not yet implemented </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Agastya Raghav Iyengar
 *
 * @version 1.00    2025-04-20 Initial version for TP3
 */
public class ViewRole2Home {

    /*-*******************************************************************************************

    Attributes – application dimensions

     */

    /** Window width, shared from the main application. */
    private static double width  = applicationMain.FoundationsMain.WINDOW_WIDTH;

    /** Window height, shared from the main application. */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    /*-*******************************************************************************************

    Attributes – GUI widgets

     */

    // --- Area 1: Header ---
    /** Page title label. */
    protected static Label  label_PageTitle      = new Label();
    /** Displays the logged-in staff user's username. */
    protected static Label  label_UserDetails    = new Label();
    /** Opens the Account Update page. */
    protected static Button button_UpdateThisUser = new Button("Account Update");

    /** Horizontal separator below the header. */
    protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);

    // --- Area 2: Post management row ---
    /** Opens the Create Post page. */
    protected static Button button_CreatePost    = new Button("Create Post");
    /** Reloads all posts into the table. */
    protected static Button button_ViewAllPosts  = new Button("View All Posts");
    /** Opens the Search Posts page. */
    protected static Button button_SearchPosts   = new Button("Search Posts");
    /** Allows creating or deleting discussion threads. */
    protected static Button button_ManageThreads = new Button("Manage Threads");

    /** Horizontal separator between the action row and the post table. */
    protected static Line line_Separator2 = new Line(20, 150, width - 20, 150);

    // --- Area 3: Post table ---
    /** Table showing all discussion posts. */
    protected static TableView<PostDisplay>      table_Posts = new TableView<>();
    /** Backing list for the post table. */
    protected static ObservableList<PostDisplay> postData    = FXCollections.observableArrayList();

    /** Horizontal separator between the table and the post-action row. */
    protected static Line line_Separator3 = new Line(20, 430, width - 20, 430);

    // --- Area 4: Per-post action row ---
    /** Opens the View Post & Replies page for the selected post. */
    protected static Button button_ViewPost   = new Button("View Post & Replies");
    /** Opens the Edit Post page for the selected post (staff may edit any post). */
    protected static Button button_EditPost   = new Button("Edit Post");
    /** Soft-deletes the selected post (staff may delete any post). */
    protected static Button button_DeletePost = new Button("Delete Post");

    /** Horizontal separator between post actions and staff tools. */
    protected static Line line_Separator4 = new Line(20, 475, width - 20, 475);

    // --- Area 5: Staff tools row ---
    /** Opens the Grading Statistics page (implemented). */
    protected static Button button_GradingStats        = new Button("Grading Statistics");
    /** Placeholder – Private Feedback (not implemented). */
    protected static Button button_PrivateFeedback     = new Button("Private Feedback *");
    /** Placeholder – Grading Parameters CRUD (not implemented). */
    protected static Button button_GradingParameters   = new Button("Grading Parameters *");
    /** Placeholder – Admin Requests (not implemented). */
    protected static Button button_AdminRequests       = new Button("Admin Requests *");

    /** Horizontal separator above the Logout / Quit row. */
    protected static Line line_Separator5 = new Line(20, 525, width - 20, 525);

    // --- Area 6: Navigation ---
    /** Logs out the current user. */
    protected static Button button_Logout = new Button("Logout");
    /** Terminates the application. */
    protected static Button button_Quit   = new Button("Quit");

    /*-*******************************************************************************************

    Singleton bookkeeping

     */

    /** Singleton guard – null until first instantiation. */
    private static ViewRole2Home theView = null;

    /** Reference to the in-memory H2 database. */
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /** The JavaFX Stage owned by the application. */
    protected static Stage theStage;
    /** Root pane containing all widgets. */
    protected static Pane  theRootPane;
    /** The currently logged-in staff User. */
    public static User  theUser;

    /** Scene reused across invocations. */
    private static Scene theRole2HomeScene = null;
    /** Role identifier: Admin=1, Student=2, Staff=3. */
    protected static final int theRole = 3;

    /*-*******************************************************************************************

    Public entry point

     */

    /**
     * <p> Method: displayRole2Home(Stage ps, User user) </p>
     *
     * <p> Description: Single entry point from outside this package.  Sets up shared state,
     * instantiates the singleton on first call, refreshes dynamic content (user label, post
     * table), then places the scene on the stage and shows it. </p>
     *
     * @param ps   the JavaFX Stage to display this page on
     * @param user the currently logged-in staff User
     */
    public static void displayRole2Home(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) {
            theView = new ViewRole2Home();
        }

        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;

        // Refresh dynamic content
        label_UserDetails.setText("User: " + theUser.getUserName());
        ModelRole2Home.initialize(theUser.getUserName());
        ControllerRole2Home.loadAllPosts();

        theStage.setTitle("CSE 360 Foundations: Staff Home Page");
        theStage.setScene(theRole2HomeScene);
        theStage.show();
    }

    /*-*******************************************************************************************

    Constructor (singleton – called at most once)

     */

    /**
     * <p> Method: ViewRole2Home() </p>
     *
     * <p> Description: Initialises every widget: position, font, size, and event handler.
     * Runs exactly once; subsequent calls to displayRole2Home() only update the dynamic fields. </p>
     */
    private ViewRole2Home() {

        theRootPane        = new Pane();
        theRole2HomeScene  = new Scene(theRootPane, width, height);

        // ── Area 1: Header ────────────────────────────────────────────────────────
        label_PageTitle.setText("Staff Home Page");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: ");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        setupButtonUI(button_UpdateThisUser, "Dialog", 16, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> { ControllerRole2Home.performUpdate(); });

        // ── Area 2: Post-management action row ────────────────────────────────────
        // Four buttons evenly across the window
        setupButtonUI(button_CreatePost,    "Dialog", 14, 172, Pos.CENTER,  20, 108);
        setupButtonUI(button_ViewAllPosts,  "Dialog", 14, 172, Pos.CENTER, 207, 108);
        setupButtonUI(button_SearchPosts,   "Dialog", 14, 172, Pos.CENTER, 394, 108);
        setupButtonUI(button_ManageThreads, "Dialog", 14, 172, Pos.CENTER, 581, 108);

        button_CreatePost   .setOnAction((_) -> { ControllerRole2Home.performCreatePost();    });
        button_ViewAllPosts .setOnAction((_) -> { ControllerRole2Home.loadAllPosts();          });
        button_SearchPosts  .setOnAction((_) -> { ControllerRole2Home.performSearch();         });
        button_ManageThreads.setOnAction((_) -> { ControllerRole2Home.performManageThreads(); });

        // ── Area 3: Post table ────────────────────────────────────────────────────
        setupTableView();

        // ── Area 4: Per-post action row ───────────────────────────────────────────
        setupButtonUI(button_ViewPost,   "Dialog", 14, 220, Pos.CENTER,  20, 440);
        setupButtonUI(button_EditPost,   "Dialog", 14, 220, Pos.CENTER, 260, 440);
        setupButtonUI(button_DeletePost, "Dialog", 14, 220, Pos.CENTER, 500, 440);

        button_ViewPost  .setOnAction((_) -> { ControllerRole2Home.performViewPost();   });
        button_EditPost  .setOnAction((_) -> { ControllerRole2Home.performEditPost();   });
        button_DeletePost.setOnAction((_) -> { ControllerRole2Home.performDeletePost(); });

        // ── Area 5: Staff-tools row ───────────────────────────────────────────────
        // Grading Statistics is implemented; the other three are stubs
        setupButtonUI(button_GradingStats,      "Dialog", 13, 172, Pos.CENTER,  20, 484);
        setupButtonUI(button_PrivateFeedback,   "Dialog", 13, 172, Pos.CENTER, 207, 484);
        setupButtonUI(button_GradingParameters, "Dialog", 13, 172, Pos.CENTER, 394, 484);
        setupButtonUI(button_AdminRequests,      "Dialog", 13, 172, Pos.CENTER, 581, 484);

        button_GradingStats     .setOnAction((_) -> { ControllerRole2Home.performGradingStats();        });
        button_PrivateFeedback  .setOnAction((_) -> { ControllerRole2Home.performPrivateFeedback();     });
        button_GradingParameters.setOnAction((_) -> { ControllerRole2Home.performGradingParameters();  });
        button_AdminRequests    .setOnAction((_) -> { ControllerRole2Home.performAdminRequests();       });

        // Style the stub buttons slightly differently so it's obvious they are placeholders
        String stubStyle = "-fx-text-fill: gray; -fx-font-style: italic;";
        button_PrivateFeedback  .setStyle(stubStyle);
        button_GradingParameters.setStyle(stubStyle);
        button_AdminRequests    .setStyle(stubStyle);

        // ── Area 6: Navigation ────────────────────────────────────────────────────
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER,  20, 535);
        setupButtonUI(button_Quit,   "Dialog", 18, 250, Pos.CENTER, 300, 535);

        button_Logout.setOnAction((_) -> { ControllerRole2Home.performLogout(); });
        button_Quit  .setOnAction((_) -> { ControllerRole2Home.performQuit();   });

        // ── Assemble root pane ────────────────────────────────────────────────────
        theRootPane.getChildren().addAll(
                label_PageTitle, label_UserDetails, button_UpdateThisUser,
                line_Separator1,
                button_CreatePost, button_ViewAllPosts, button_SearchPosts, button_ManageThreads,
                line_Separator2,
                table_Posts,
                line_Separator3,
                button_ViewPost, button_EditPost, button_DeletePost,
                line_Separator4,
                button_GradingStats, button_PrivateFeedback,
                button_GradingParameters, button_AdminRequests,
                line_Separator5,
                button_Logout, button_Quit
        );
    }

    /*-*******************************************************************************************

    Table setup & population

     */

    /**
     * <p> Configures the post TableView with columns for ID, Title, Thread, Author,
     * Replies, Status, and Date. </p>
     */
    private void setupTableView() {
        TableColumn<PostDisplay, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("postId"));
        colId.setPrefWidth(45);
        colId.setMaxWidth(45);
        colId.setMinWidth(45);

        TableColumn<PostDisplay, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(230);

        TableColumn<PostDisplay, String> colThread = new TableColumn<>("Thread");
        colThread.setCellValueFactory(new PropertyValueFactory<>("thread"));
        colThread.setPrefWidth(100);

        TableColumn<PostDisplay, String> colAuthor = new TableColumn<>("Author");
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colAuthor.setPrefWidth(120);

        TableColumn<PostDisplay, Integer> colReplies = new TableColumn<>("Replies");
        colReplies.setCellValueFactory(new PropertyValueFactory<>("replyCount"));
        colReplies.setPrefWidth(65);

        TableColumn<PostDisplay, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(70);

        TableColumn<PostDisplay, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colDate.setPrefWidth(130);

        table_Posts.getColumns().addAll(
                colId, colTitle, colThread, colAuthor, colReplies, colStatus, colDate);
        table_Posts.setItems(postData);
        table_Posts.setLayoutX(20);
        table_Posts.setLayoutY(160);
        table_Posts.setPrefWidth(760);
        table_Posts.setPrefHeight(255);
    }

    /**
     * <p> Populates the post table from the supplied list, wrapping each Post in a PostDisplay. </p>
     *
     * @param posts the list of posts to show (may include deleted posts, shown as [Deleted])
     */
    protected static void populatePostTable(List<Post> posts) {
        postData.clear();
        for (Post post : posts) {
            postData.add(new PostDisplay(post));
        }
    }

    /*-*******************************************************************************************

    Helper methods – UI setup

     */

    /**
     * <p> Initialises a Label's font, minimum width, alignment, and position. </p>
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

    /**
     * <p> Initialises a Button's font, minimum width, alignment, and position. </p>
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

    /*-*******************************************************************************************

    Inner class – PostDisplay (wraps Post for TableView binding)

     */

    /**
     * <p> Wraps a Post object for display in the staff post table.
     * Provides JavaFX-compatible property getters for each column. </p>
     */
    public static class PostDisplay {
        /** Unique post ID. */
        private final int    postId;
        /** Title shown in the table (or "[Deleted]"). */
        private String       title;
        /** Thread the post belongs to. */
        private final String thread;
        /** Username of the post's author. */
        private final String author;
        /** Number of replies on this post. */
        private final int    replyCount;
        /** "READ" or "UNREAD" from the current staff user's perspective. */
        private final String status;
        /** Formatted creation timestamp. */
        private final String timestamp;
        /** The underlying Post object. */
        private final Post   post;

        /**
         * <p> Constructs a PostDisplay from the given Post, pulling all display data
         * from the model. </p>
         *
         * @param post the Post to wrap
         */
        public PostDisplay(Post post) {
            this.post       = post;
            this.postId     = post.getPostID();
            this.thread     = post.getThreadName();
            this.title      = post.isDeleted() ? "[Deleted]" : post.getTitle();
            this.author     = post.getUsername();
            this.replyCount = ModelRole2Home.getReplyCount(post.getPostID());
            this.status     = ModelRole2Home.isRead(post.getPostID()) ? "READ" : "UNREAD";
            this.timestamp  = ModelRole2Home.getFormattedTimestamp(post);
        }

        // --- Getters for TableView PropertyValueFactory ---

        /** @return the post's unique ID */
        public int    getPostId()     { return postId;     }
        /** @return the post title (or "[Deleted]") */
        public String getTitle()      { return title;      }
        /** @return the thread name */
        public String getThread()     { return thread;     }
        /** @return the author's username */
        public String getAuthor()     { return author;     }
        /** @return number of replies */
        public int    getReplyCount() { return replyCount; }
        /** @return "READ" or "UNREAD" */
        public String getStatus()     { return status;     }
        /** @return formatted timestamp string */
        public String getTimestamp()  { return timestamp;  }
        /** @return the underlying Post object */
        public Post   getPost()       { return post;       }
    }
}