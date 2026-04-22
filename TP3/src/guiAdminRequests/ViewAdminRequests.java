package guiAdminRequests;

import java.time.format.DateTimeFormatter;
import java.util.List;

import entityClasses.User;
import entityClasses.adminRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewAdminRequests Class. </p>
 *
 * <p> Description: The Java/FX-based Admin Requests View.
 * Displays all requests sent by staff members to the currently
 * logged in admin in a table .</p>
 */
public class ViewAdminRequests {
	/** window width*/
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    /** window height*/
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
    /** stage*/ 
    protected static Stage theStage;
    /** current user*/
    protected static User theUser;
    /** root*/
    private static Pane root;
    /** scene*/
    private static Scene scene;
    /** table*/
    protected static TableView<RequestDisplay> table = new TableView<>();
    /** observable*/
    protected static ObservableList<RequestDisplay> data =
        FXCollections.observableArrayList();
    /** label for title*/
    private static Label label_Title = new Label("Admin Requests");
    /** label for user*/
    private static Label label_User = new Label();
    /** button for return*/
    private static Button button_Return = new Button("Return");
    
	/**
	 * <p> Constructor - Not utilized </p>
	 */
	public ViewAdminRequests() {}
    
    /**
     * <p> displays adminRequests window</p>
     * 
     * @param stage
     * @param user
     */
    public static void display(Stage stage, User user) {
    	
        theStage = stage;
        theUser = user;

        ModelAdminRequests.initialize(user.getUserName());

        root = new Pane();
        scene = new Scene(root, width, height);

        label_Title.setText("Admin Requests");
        setupLabelUI(label_Title, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_User.setText("User: " + user.getUserName());
        setupLabelUI(label_User, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        setupTable();

        setupButtonUI(button_Return, "Dialog", 18, 200, Pos.CENTER, 20, 520);

        button_Return.setOnAction(e ->
            guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser)
        );

        root.getChildren().addAll(
            label_Title,
            label_User,
            table,
            button_Return
        );

        stage.setScene(scene);
        stage.show();

        ControllerAdminRequests.loadRequests();
    }
    /**
     * <p> sets up the ui for table</p>
     */
    private static void setupTable() {

        table.getColumns().clear();

        TableColumn<RequestDisplay, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        colID.setPrefWidth(60);

        TableColumn<RequestDisplay, String> colUser = new TableColumn<>("From");
        colUser.setCellValueFactory(new PropertyValueFactory<>("requester"));
        colUser.setPrefWidth(150);

        TableColumn<RequestDisplay, String> colBody = new TableColumn<>("Request");
        colBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        colBody.setPrefWidth(350);

        TableColumn<RequestDisplay, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTime.setPrefWidth(180);
        
        TableColumn<RequestDisplay, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(120);
        
        TableColumn<RequestDisplay, Void> colAction = new TableColumn<>("Action");

        colAction.setCellFactory(param -> new TableCell<>() {

            private final Button btn = new Button("Complete");

            {
                btn.setOnAction(event -> {

                    RequestDisplay data = getTableView().getItems().get(getIndex());

                    applicationMain.FoundationsMain.database.requestCompletion(
                        data.getRequestID()
                    );

                    ControllerAdminRequests.loadRequests();
                });
            }
            /**
             * <p> logic for button in the table(button used to mark staff requests as completed)</p>
             * 
             * @param item
             * @param empty
             */
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    RequestDisplay data = getTableView().getItems().get(getIndex());

                    if (data.getStatus().equals("COMPLETED")) {
                        btn.setDisable(true);
                    } else {
                        btn.setDisable(false);
                    }

                    setGraphic(btn);
                }
            }
        });

        table.getColumns().addAll(colID, colUser, colBody, colTime, colStatus, colAction);

        table.setItems(data);

        table.setLayoutX(20);
        table.setLayoutY(120);
        table.setPrefWidth(760);
        table.setPrefHeight(400);
    }
    
    /**
     * <p> populates table with a list of the requests sent to admin currently logged in</p>
     * 
     * @param list
     */
    protected static void populateTable(List<adminRequests> list) {

        data.clear();

        for (adminRequests r : list) {
            data.add(new RequestDisplay(r));
        }
    }
    /**
     * <p> used to get values used in requests table.</p>
     */
    public static class RequestDisplay {
    	/** requests id*/
        private final int requestID;
        /** user who made request*/
        private final String requester;
        /** text of the request*/
        private final String body;
        /** time request submitted*/
        private final String timestamp;
        /** Status of completion*/
        private final String status;
        
        /**
         * <p> method to set data from requests pulled</p>
         * 
         * @param request request pulled from table
         */
        public RequestDisplay(adminRequests request) {

            DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

            this.requestID = request.getRequestID();
            this.requester = request.getRequestSubmiter();
            this.body = request.getBody();
            this.timestamp = request.getTimeStamp().format(formatter);
            this.status = request.getCompleted() ? "COMPLETED" : "OPEN";
        }
        /** gets request ID*/
        public int getRequestID() { return requestID; }
        /** gets user who sent the request*/
        public String getRequester() { return requester; }
        /** gets the body*/
        public String getBody() { return body; }
        /** gets the time submitted*/
        public String getTimestamp() { return timestamp; }
        /** gets the status of completion*/
        public String getStatus() { return status; }
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