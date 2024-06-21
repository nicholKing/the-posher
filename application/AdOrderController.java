package application;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class AdOrderController implements Initializable{
	@FXML
	private Button logoutBtn;
	@FXML
	private ImageView menu;
	@FXML
	private ImageView menuClose;
	@FXML
	private AnchorPane slider;
	@FXML
	private Label headerLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private Button employmentBtn;
	@FXML
	private Button cat1;
	@FXML
	private Button cat2;
	@FXML
	private Button cat3;
	@FXML
	private Button cat4;
	@FXML
	private Button cat5;
	@FXML
	private Button cat6;
	@FXML
	private Button cat7;
	@FXML
	private Button cat8;	
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField stockField;
   
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane rootGridPane;
    //JAVA FX
    private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();
	
	String role;
	String dbName;
	String orderPage = "AdOrderPage.fxml";
    String homePage = "AdHomePage.fxml";
    String accPage = "AccountDetails.fxml";
    String tablePage = "AdTableReservationPage.fxml";
    String employmentPage = "EmploymentPage.fxml";
    String previousClickedBtn = ""; 
    
    int id;
    
	boolean hasAccount = false;
	boolean isHomeBtn = false;
    boolean isSideBtn = false;
    boolean isOrderBtn = false;
    boolean isCartBtn = false;
    boolean isTableBtn = false;
    boolean isAccBtn = false;
    boolean isEmploymentBtn = false;
 
    //FOOD VARIABLES
    String foodName;
	String qty; // for data transferring
	boolean clicked = false;
	boolean added = false;
	boolean selected = false;
	boolean pref1 = false;
	boolean pref2 = false;
	int quantity = 0; //for counting
	int price;
	char category;
    private static final int COLUMNS = 6;
    String currentCategory;

    //RIGHT PANEL
    public void showAccount(ActionEvent event) throws IOException, SQLException {
		if(hasAccount) {
			isAccBtn = true;
			changeScene(event, "AccountDetails.fxml");
		}
		else {showAlert("Login or register to edit your information", AlertType.INFORMATION);}
	}
	public void signIn(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
	    root = loader.load();
		if(hasAccount) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("You're about to logout");
			alert.setContentText("Are you sure you want to logout?");
			if(alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			    scene = new Scene(root);
			    stage.setScene(scene);
			    stage.show();
			    hasAccount = false;
			}
		}
		else {
	    Scene1Controller loginPage = loader.getController();
	    loginPage.Connect();
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		}
	}
	public void signUp(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
	    root = loader.load();
		if(hasAccount) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("You're about to logout");
			alert.setContentText("Are you sure you want to logout?");
			if(alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			    scene = new Scene(root);
			    stage.setScene(scene);
			    stage.show();
			    hasAccount = false;
			}
			
		}
		else {
			SignUpController signUpPage = loader.getController();
			signUpPage.Connect();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	//LEFT PANEL
	public void homeBtn(ActionEvent event) throws IOException, SQLException {
		isHomeBtn = true;
		changeScene(event, homePage);
	}
	public void orderBtn(ActionEvent event) throws IOException, SQLException {
		System.out.println("Order");
		isOrderBtn = true;
		changeScene(event, orderPage);
	}
	public void showStocks(ActionEvent event) throws IOException, SQLException {
		isCartBtn = true;
		changeScene(event, "AdStockPage.fxml");
	}
	public void showTable(ActionEvent event) throws IOException, SQLException {
		isTableBtn = true;
		changeScene(event, tablePage);
		}
	public void showEmployment(ActionEvent event) throws IOException, SQLException {
		if(hasAccount && role.equals("Owner")) {
			isEmploymentBtn = true;
			changeScene(event, employmentPage);
		}
		else {
			showAlert("This page is only for owner.", AlertType.INFORMATION);
		}
	}
	public void logout(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		
		if(!hasAccount) {
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			Scene1Controller loginPage = loader.getController();
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("You're about to logout");
			alert.setContentText("Are you sure you want to logout?");
			
			if(alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
			Scene1Controller loginPage = loader.getController();
		}
	}

	//ORDER METHODS
	private void loadOrdersByCategory(String category) {
        currentCategory = category;
        try {
            List<MenuItem> items = DatabaseHelper.getMenuItemsByCategory(category);
            displayOrders(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	private void displayOrders(List<MenuItem> items) throws SQLException {
	    rootGridPane.getChildren().clear();

	    Set<String> displayedNames = new HashSet<>(); // To store displayed food names

	    for (int i = 0; i < items.size(); i++) {
	        MenuItem item = items.get(i);
	        int col = i % COLUMNS;
	        int row = i / COLUMNS;

	        String foodName = item.getFoodName();

	        // Check if the food name has already been displayed
	        if (!displayedNames.contains(foodName)) {
	            try {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderPanel.fxml"));
	                VBox orderBox = loader.load();
	                OrderPanelController controller = loader.getController();
	                controller.setItemDetails(item);
	               
	                // Add the VBox to the GridPane
	                rootGridPane.add(orderBox, col, row);

	                // Add the food name to the set of displayed names
	                displayedNames.add(foodName);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    // Rearrange the GridPane to ensure there are no gaps
	    rearrangeGridPane();
	}
	private void rearrangeGridPane() {
	    List<Node> nodes = new ArrayList<>(rootGridPane.getChildren());
	    rootGridPane.getChildren().clear();

	    for (int i = 0; i < nodes.size(); i++) {
	        int col = i % COLUMNS;
	        int row = i / COLUMNS;
	        rootGridPane.add(nodes.get(i), col, row);
	    }
	}

	//HELPER METHODS
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
	    this.role = role;
	    this.hasAccount = hasAccount;
	    this.dbName = dbName;
	    this.id = id;
	}
    public void changeScene(ActionEvent event, String page) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
		root = loader.load();

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		if(isHomeBtn) {
			AdminController homePage = loader.getController();
			homePage.setUserDetails(role, hasAccount, dbName, id);
			homePage.displayName();
		}
		else if(isOrderBtn) {
			AdOrderController orderPage = loader.getController();
			orderPage.setUserDetails(role, hasAccount, dbName, id);
			
		}else if(isTableBtn) {
			AdTableReservationController tablePage = loader.getController();
			tablePage.setUserDetails(role, hasAccount, dbName, id);
			tablePage.initialize();
		}else if(isAccBtn) {
			AccountDetailsController accPage = loader.getController();
			accPage.setUserDetails(role, hasAccount, dbName, id);
			accPage.displayName();
			
		}else if(isEmploymentBtn) {
			EmploymentController employmentPage = loader.getController();
			employmentPage.setUserDetails(role, hasAccount, dbName, id);
			employmentPage.displayName();
		}else {
			AdStockController stockPage = loader.getController();
			stockPage.setUserDetails(role, hasAccount, dbName, id);
			stockPage.displayName();
			
		}
		
	}
    private void setSlides() {
		menu.setVisible(true);
		menuClose.setVisible(false);
		
		
		menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-200);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(false);
                menuClose.setVisible(true);
            });
        });

        menuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-200);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(true);
                menuClose.setVisible(false);
            });
        });
	}
    private void showAlert(String contentText, AlertType alertType) {

		 Alert alert = new Alert(alertType);
	        alert.setTitle("Notice");
	        alert.setHeaderText(null);
	        alert.setContentText(contentText);
	        Scene scenes = alert.getDialogPane().getScene();
	        scenes.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
	        alert.show();
	  
	        Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	Platform.runLater(() -> {
	                   alert.close();
	                });
	                
	                timer.cancel(); // Cancel the timer after closing the alert
	            }
	        }, 2 * 1000);
	    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cat1.setOnAction(event -> loadOrdersByCategory("Rice Meals"));
        cat2.setOnAction(event -> loadOrdersByCategory("Pasta"));
        cat3.setOnAction(event -> loadOrdersByCategory("Cakes"));
        cat4.setOnAction(event -> loadOrdersByCategory("Burger"));
        cat5.setOnAction(event -> loadOrdersByCategory("Coffee"));
        cat6.setOnAction(event -> loadOrdersByCategory("Frappe"));
        cat7.setOnAction(event -> loadOrdersByCategory("Tea"));
        cat8.setOnAction(event -> loadOrdersByCategory("Appetizers"));
       
        loadOrdersByCategory("Rice Meals");
		setSlides();
        rearrangeGridPane();
       
	}
}
