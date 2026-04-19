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

public class ViewStaffRequests {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	protected static Stage theStage;
	protected static User theUser;
	
	private static Pane root;
	private static Scene scene;
	
    protected static TableView<RequestDisplay> table_Requests = new TableView<>();
    protected static ObservableList<RequestDisplay> requestData = FXCollections.observableArrayList();

    private static Label label_Title = new Label("Staff Requests");
    private static Label label_User = new Label();

    private static TextArea text_Request = new TextArea();
    private static ComboBox<String> combo_Admin = new ComboBox<>();

    private static Button button_Send = new Button("Send Request");
    private static Button button_Return = new Button("Return");
    
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
    
    
    public static void populateTable(List<adminRequests> list) {
        requestData.clear();
        for (adminRequests r : list) {
            requestData.add(new RequestDisplay(r));
        }
    }
    
    public static class RequestDisplay {

        private final int requestID;
        private final String body;
        private final String admin;
        private final String status;
        private final String timestamp;

        public RequestDisplay(adminRequests request) {
            this.requestID = request.getRequestID();
            this.body = request.getBody();
            this.admin = request.getRecievingAdmin();
            this.status = request.getCompleted() ? "COMPLETED" : "OPEN";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            this.timestamp = request.getTimeStamp().format(formatter);
        }

        public int getRequestID() { return requestID; }
        public String getBody() { return body; }
        public String getAdmin() { return admin; }
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
    }
    
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
}