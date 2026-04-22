package guiStaffRequests;

import database.Database;
import entityClasses.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import entityClasses.adminRequests;

import java.time.format.DateTimeFormatter;
import java.util.List;

/*******
 * <p> Title: ViewStaffRequests Class. </p>
 *
 * <p> Description: The Java/FX-based Staff Requests View. Allows staff users to submit
 * requests to admins and view previously submitted requests in a table format. The view
 * communicates with the controller to process actions and update the displayed data. </p>
 */
public class ViewStaffRequests {

    /** The width of the application window. */
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;

    /** The height of the application window. */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    /** The primary stage on which this page is displayed. */
    protected static Stage theStage;

    /** The currently logged-in user. */
    protected static User theUser;

    /** The root pane containing all widgets for this page. */
    private static Pane root;

    /** The JavaFX scene for this page. */
    private static Scene scene;

    /** TableView displaying all requests submitted by the current staff user. */
    protected static TableView<RequestDisplay> table_Requests = new TableView<>();

    /** Observable data backing the requests TableView. */
    protected static ObservableList<RequestDisplay> requestData = FXCollections.observableArrayList();

    /** Label displaying the page title. */
    private static Label label_Title = new Label("Staff Requests");

    /** Label displaying the currently logged-in user's name. */
    private static Label label_User = new Label();

    /** Text area for entering the body of a new request. */
    private static TextArea text_Request = new TextArea();

    /** ComboBox for selecting the admin who will receive the request. */
    private static ComboBox<String> combo_Admin = new ComboBox<>();

    /** Button to submit the new request. */
    private static Button button_Send = new Button("Send Request");

    /** Button to return to the Role 2 Home page. */
    private static Button button_Return = new Button("Return");


    /*-*******************************************************************************************

    Constructor

     */

    /**
     * Default constructor is not used (all methods are static).
     */
    public ViewStaffRequests() {}


    /*-*******************************************************************************************

    Entry point

     */

    /**
     * <p> Displays the Staff Requests page on the given stage for the given user. </p>
     *
     * @param stage the primary stage on which to display the page
     * @param user  the currently logged-in user
     */
    public static void display(Stage stage, User user) {

        theStage = stage;
        theUser = user;

        root = new Pane();
        scene = new Scene(root, width, height);

        ModelStaffRequests.initialize(user.getUserName());

        label_User.setText("User: " + user.getUserName());

        setupLabelUI(label_Title, "Arial", 24, width, Pos.CENTER, 0, 10);
        setupLabelUI(label_User, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 50);

        setupTableView();

        text_Request.setPromptText("Enter request description...");
        text_Request.setLayoutX(20);
        text_Request.setLayoutY(480);
        text_Request.setPrefWidth(400);
        text_Request.setPrefHeight(80);

        combo_Admin.setPromptText("Select Admin");
        combo_Admin.setLayoutX(450);
        combo_Admin.setLayoutY(480);
        combo_Admin.setPrefWidth(150);

        setupButtonUI(button_Send, "Dialog", 16, 150, Pos.CENTER, 600, 500);
        setupButtonUI(button_Return, "Dialog", 16, 150, Pos.CENTER, 600, 540);

        combo_Admin.getItems().clear();
        combo_Admin.getItems().addAll(applicationMain.FoundationsMain.database.getAllAdmins());

        button_Send.setOnAction(e -> {
            String body = text_Request.getText();
            String admin = combo_Admin.getValue();

            if (body.isEmpty() || admin == null) {
                showAlert("Error", "Fill in both fields");
                return;
            }

            ControllerStaffRequests.sendRequest(theUser.getUserName(), admin, body);

            text_Request.clear();
            combo_Admin.getItems().clear();
            combo_Admin.getItems().addAll(applicationMain.FoundationsMain.database.getAllAdmins());

            ControllerStaffRequests.loadRequests(); // refresh table
        });

        // Return button
        button_Return.setLayoutX(620);
        button_Return.setLayoutY(540);

        button_Return.setOnAction(e ->
                guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser)
        );

        root.getChildren().addAll(
                label_Title,
                label_User,
                table_Requests,
                text_Request,
                combo_Admin,
                button_Send,
                button_Return
        );

        stage.setScene(scene);
        stage.show();

        ControllerStaffRequests.loadRequests(); // initial load
    }


    /*-*******************************************************************************************

    Table population

     */

    /**
     * <p> Clears the table and repopulates it with the given list of requests. </p>
     *
     * @param list the list of requests to display in the table
     */
    public static void populateTable(List<adminRequests> list) {
        requestData.clear();
        for (adminRequests r : list) {
            requestData.add(new RequestDisplay(r));
        }
    }


    /*-*******************************************************************************************

    Inner class

     */

    /**
     * <p> Display wrapper class used to expose request fields as JavaFX-compatible
     * properties for the TableView columns. </p>
     */
    public static class RequestDisplay {

        /** The unique ID of this request. */
        private final int requestID;

        /** The text body of this request. */
        private final String body;

        /** The username of the admin receiving this request. */
        private final String admin;

        /** The completion status of this request, either "COMPLETED" or "OPEN". */
        private final String status;

        /** The formatted timestamp of when this request was created. */
        private final String timestamp;

        /**
         * <p> Constructs a RequestDisplay from an adminRequests entity. </p>
         *
         * @param request the adminRequests entity to wrap
         */
        public RequestDisplay(adminRequests request) {
            this.requestID = request.getRequestID();
            this.body = request.getBody();
            this.admin = request.getRecievingAdmin();
            this.status = request.getCompleted() ? "COMPLETED" : "OPEN";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            this.timestamp = request.getTimeStamp().format(formatter);
        }

        /**
         * <p> Returns the request ID. </p>
         *
         * @return the request ID
         */
        public int getRequestID() { return requestID; }

        /**
         * <p> Returns the body text of the request. </p>
         *
         * @return the request body
         */
        public String getBody() { return body; }

        /**
         * <p> Returns the username of the admin receiving the request. </p>
         *
         * @return the admin username
         */
        public String getAdmin() { return admin; }

        /**
         * <p> Returns the completion status of the request. </p>
         *
         * @return "COMPLETED" or "OPEN"
         */
        public String getStatus() { return status; }

        /**
         * <p> Returns the formatted timestamp of the request. </p>
         *
         * @return the timestamp string
         */
        public String getTimestamp() { return timestamp; }
    }


    /*-*******************************************************************************************

    Private helpers

     */

    /**
     * <p> Sets up the TableView columns and binds it to the observable data list. </p>
     */
    private static void setupTableView() {
        table_Requests.getColumns().clear();

        TableColumn<RequestDisplay, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        colID.setPrefWidth(60);

        TableColumn<RequestDisplay, String> colBody = new TableColumn<>("Request");
        colBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        colBody.setPrefWidth(350);

        TableColumn<RequestDisplay, String> colAdmin = new TableColumn<>("Admin");
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        colAdmin.setPrefWidth(150);

        TableColumn<RequestDisplay, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTime.setPrefWidth(180);

        TableColumn<RequestDisplay, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);

        table_Requests.getColumns().addAll(colID, colBody, colAdmin, colTime, colStatus);
        table_Requests.setItems(requestData);

        table_Requests.setLayoutX(20);
        table_Requests.setLayoutY(80);
        table_Requests.setPrefWidth(760);
        table_Requests.setPrefHeight(380);
    }

    /**********
     * <p> Private local method to initialize the standard fields for a label. </p>
     *
     * @param l  the Label object to be initialized
     * @param ff the font to be used
     * @param f  the size of the font to be used
     * @param w  the width of the Label
     * @param p  the alignment (e.g. left, centered, or right)
     * @param x  the location from the left edge (x axis)
     * @param y  the location from the top (y axis)
     */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
            double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**********
     * <p> Private local method to initialize the standard fields for a button. </p>
     *
     * @param b  the Button object to be initialized
     * @param ff the font to be used
     * @param f  the size of the font to be used
     * @param w  the width of the Button
     * @param p  the alignment (e.g. left, centered, or right)
     * @param x  the location from the left edge (x axis)
     * @param y  the location from the top (y axis)
     */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
            double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**
     * <p> Displays a standard informational alert dialog. </p>
     *
     * @param title   the text displayed at the top of the dialog window
     * @param message the text in the body of the dialog
     */
    protected static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
