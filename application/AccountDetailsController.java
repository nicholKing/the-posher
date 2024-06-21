package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AccountDetailsController implements Initializable{
	@FXML
	private Button btn1;
	@FXML
	private Button btn2;
	@FXML
	private ImageView img1;
	@FXML
	private ImageView img2;
	@FXML
    private Button addAddressBtn;
    @FXML
    private Button addEmailBtn;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField emailField;
    @FXML
    private Button logoutBtn;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameTextField;
	@FXML
	private Label nameLabel;
	@FXML
	private Label roleLabel;
    @FXML
    private AnchorPane slider;
    @FXML
    private ImageView menuClose;
    @FXML
    private ImageView  menu;
    @FXML
    private AnchorPane slider2;
    @FXML
    private ImageView accountClose;
    @FXML
    private ImageView  account;
    @FXML
    private VBox orderLayout;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private VBox vBox;
    
    @FXML 
    private Button editBtn;
    @FXML 
    private Button saveBtn;
   
    @FXML
    private HBox signHbox;
    @FXML
    private Button logBtn;
    @FXML
    private Button signupBtn;
    
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Image stockImg = new Image("/pictures/ready-stock.png");
	private Image employmentImg = new Image("/pictures/headhunting.png");
	
	List<OrderData> orderList = new ArrayList<>();
	OrderData orderData = new OrderData();
	
	int id;
	String role;
	String name;
	String username = "";
	String orderPage = "OrderPage.fxml";
	String adOrderPage = "AdOrderPage.fxml";
	String adTablePage = "AdTableReservationPage.fxml";
	String adStockPage = "AdStockPage.fxml";
	String adHomePage = "AdHomePage.fxml";
    String homePage = "HomePage.fxml";
    String employmentPage = "EmploymentPage.fxml";
    String accPage = "AccountDetails.fxml";
    String cartPage = "MyCart.fxml";
    String tablePage = "TableReservationPage.fxml";
    String rewardsPage = "RewardsPage.fxml";
    String query = "SELECT name FROM user_tbl WHERE id = ?";
    String dbName;
    
	boolean hasAccount = false;
	boolean isEditable = false;
	boolean isHomeBtn = false;
	boolean isOrderBtn = false;
	boolean isTableBtn = false;
	boolean isRewardBtn = false;
	boolean isAccBtn = false;
	boolean isCartBtn = false;
	
	//ACCOUNT SYSTEM
	String txtName;
	String txtPass;
	String txtUName;
	String txtAddress;
	String txtEmail;
	boolean hasCorrectName = false;
	boolean newName = true;
	boolean isSuccessEdit = false;
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	//RIGHT PANEL
	public void displayName() throws SQLException {
	    // Calculate font size based on name length
	    if (dbName.length() >= 12 && dbName.length() <= 15) {
	        nameLabel.setFont(new Font(18));
	    } else if (dbName.length() > 15) {
	        nameLabel.setFont(new Font(12));
	    } else {
	        nameLabel.setFont(new Font(24)); // Default font size for shorter names
	    }

	    // Set text and enable wrapping
	    nameLabel.setText(dbName);
	    nameLabel.setWrapText(true);
	    nameLabel.setMaxWidth(Double.MAX_VALUE);
	    
	    // Optionally, set alignment if needed
	    nameLabel.setAlignment(Pos.CENTER_LEFT);
	    
	    // Set role text
	    roleLabel.setText(role);
	}
	public void showAccount(ActionEvent event) throws IOException, SQLException {
		if(hasAccount) {
			isAccBtn = true;
			changeScene(event, accPage);
		}
		else {
			showAlert("Login or register to edit your information.", AlertType.INFORMATION);
		}
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
		changeScene(event, (role.equals("Owner") || role.equals("Employee")) ? adHomePage : homePage);
	}
	public void orderBtn(ActionEvent event) throws IOException, SQLException {
		isOrderBtn = true;
		changeScene(event, (role.equals("Owner") || role.equals("Employee")) ? adOrderPage : orderPage);
	}
	public void showCart(ActionEvent event) throws IOException, SQLException {
		isCartBtn = true;
		changeScene(event, (role.equals("Owner") || role.equals("Employee")) ? adStockPage : cartPage);
	}
	public void showTable(ActionEvent event) throws IOException, SQLException {
		isTableBtn = true;
		changeScene(event, (role.equals("Owner") || role.equals("Employee")) ? adTablePage : tablePage);
	}
	public void showRewards(ActionEvent event) throws IOException, SQLException {
			if(hasAccount) {
				isRewardBtn = true;
				if(role.equals("Owner")) {
					changeScene(event, employmentPage);
				}
				else if(role.equals("Employee")){
					showAlert("This page is only for owner", AlertType.INFORMATION);
				}
				else {
					changeScene(event, rewardsPage);
				}
			}
			else {showAlert("Create an account to unlock exciting rewards!", AlertType.INFORMATION);}
	}
	public void logout(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
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
	
	//ACCOUNT SYSTEM
	public void editDetails() throws SQLException {
		editBtn.setOnAction(event ->{
			nameTextField.setEditable(true);
		
			usernameTextField.setEditable(true);
		
			passwordField.setEditable(true);
		
			emailField.setEditable(true);
			
			addressTextField.setEditable(true);
			
			saveBtn.setDisable(false);
			
		});
	}
	public void saveDetails() {
	    saveBtn.setOnAction(event -> handleSaveAction(saveBtn));
	}
	private void handleSaveAction(Button saveButton) {
	    try {
	        	checkNameField();
	        	checkPasswordField();
	        	checkEmailField();
	        	checkUsernameField();
	        	checkAddressField();
	        if (isSuccessEdit) {
	            saveButton.setDisable(true);
	            showAlert("Account information updated!", AlertType.INFORMATION);
	            isSuccessEdit = false;
	            nameTextField.setEditable(false);
	    		
				usernameTextField.setEditable(false);
			
				passwordField.setEditable(false);
			
				emailField.setEditable(false);
				
				addressTextField.setEditable(false);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	@FunctionalInterface
	interface CheckedSQLExceptionRunnable {
	    void run() throws SQLException;
	}
	public void displayDetails() throws SQLException {

		    try {
		        // Prepare the query
		        pst = con.prepareStatement("SELECT * FROM user_tbl WHERE id = ?");
		        
		        // Set the parameter for the query
		        pst.setInt(1, id);

		        // Execute the query
		        rs = pst.executeQuery();

		        // Process the result set
		        if (rs.next()) {
		            txtName = rs.getString("name");
		            txtUName = rs.getString("username");
		            txtPass = rs.getString("password");
		            txtEmail = rs.getString("email");
		            txtAddress = rs.getString("address");
		           
		        } 

		        // Set the text fields with retrieved values
		       nameTextField.setText(txtName);
		       usernameTextField.setText(txtUName);
		       passwordField.setText(txtPass);
		       emailField.setText(txtEmail);
		       addressTextField.setText(txtAddress);

		    }catch (Exception e) {
				e.printStackTrace();
			}
		}
	public void checkNameField() throws SQLException {
		PreparedStatement pst;
		txtName = nameTextField.getText();
		
		if(txtName.isEmpty()) showAlert("Please enter a name", AlertType.ERROR); 
		else if(txtName.length() > 23) showAlert("Please enter a shorter name", AlertType.ERROR); 
		else {
			
				pst = con.prepareStatement("UPDATE user_tbl SET name=? WHERE id = ?");
					
				pst.setString(1, txtName);
				pst.setInt(2, id);
				int k = pst.executeUpdate();
				if(k == 1) {
					dbName = txtName;
					displayName();
					isSuccessEdit = true;
				}
				else {
					isSuccessEdit = false;
				}
		}
	}
	public void checkUsernameField() throws SQLException {
		PreparedStatement pst;
		txtUName = usernameTextField.getText();
		
		if(txtUName.isEmpty()) showAlert("Please enter a username", AlertType.ERROR); 
		else if (txtUName.length() < 8) showAlert("Username should be at least 8 characters", AlertType.ERROR);
		else if(txtUName.length() > 23) showAlert("Please enter a shorter username", AlertType.ERROR); 
		else {
			
				pst = con.prepareStatement("UPDATE user_tbl SET username=? WHERE id = ?");
					
				pst.setString(1, txtUName);
				pst.setInt(2, id);
				int k = pst.executeUpdate();
				if(k == 1) {
					isSuccessEdit = true;
				}
				else {
					isSuccessEdit = false;
				}
		}
	}
	public void checkPasswordField() throws SQLException {
		PreparedStatement pst;
		txtPass = passwordField.getText();
		
		if(txtPass.isEmpty()) showAlert("Please enter a password", AlertType.ERROR); 
		else if (txtPass.length() < 8) showAlert("Password should be at least 8 characters", AlertType.ERROR);
		else if(txtPass.length() > 23) showAlert("Please enter a shorter password", AlertType.ERROR); 
		else {
			
				pst = con.prepareStatement("UPDATE user_tbl SET password=? WHERE id = ?");
					
				pst.setString(1, txtPass);
				pst.setInt(2, id);
				int k = pst.executeUpdate();
				if(k == 1) {
					isSuccessEdit = true;
				}
				else {
					isSuccessEdit = false;
				}
		}
	}
	public void checkEmailField() throws SQLException {
		PreparedStatement pst;
		txtEmail = emailField.getText();
		if(txtEmail == null) {
			isSuccessEdit = true;
			return;
		}
		
		if(txtEmail.length() < 18) { 
			emailField.setEditable(true);
			showAlert("Email should be atleast 8 characters", AlertType.ERROR); 
		}
		else if(!isGmailAddress(txtEmail)) { 
			
			showAlert("Please enter a valid email address", AlertType.ERROR); 
		}
		else if(txtName.length() > 23) {
			showAlert("Please enter a shorter email", AlertType.ERROR); 
		}
		else {
				pst = con.prepareStatement("UPDATE user_tbl SET email=? WHERE id = ?");
			
				pst.setString(1, txtEmail);
				pst.setInt(2, id);
				
				int k = pst.executeUpdate();
				if(k == 1) {
					isSuccessEdit = true;
				}
				else {
					isSuccessEdit = false;
				}
		}
	}
	public void checkAddressField() throws SQLException {
		PreparedStatement pst;
		txtAddress = addressTextField.getText();
		if(txtAddress == null) {
			isSuccessEdit = true;
			return;
		}
		if(txtAddress.length() > 50) showAlert("Please enter a shorter address", AlertType.ERROR); 
		else {
				pst = con.prepareStatement("UPDATE user_tbl SET address=? WHERE id = ?");
					
				pst.setString(1, txtAddress);
				pst.setInt(2, id);
				int k = pst.executeUpdate();
				if(k == 1) {
					isSuccessEdit = true;
				}
				else {
					isSuccessEdit = false;
				}
		}
	}
	public boolean isGmailAddress(String email) {
	    return email != null && email.endsWith("@gmail.com");
	}
	
	//HELPER METHODS
	public void Connect() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "root", "");
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void changeScene(ActionEvent event, String page) throws IOException, SQLException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
	    Parent root = loader.load();

	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();

	    if (role.equals("Customer")) {
	        handleCustomerControllers(loader);
	  } else if (role.equals("Owner") || role.equals("Employee")) {
	        handleAdminControllers(loader);
	    }
	}
	private void handleCustomerControllers(FXMLLoader loader) throws SQLException {
	    if (isHomeBtn) {
	        HomeController homePage = loader.getController();
	        homePage.setOrderList(orderList);
	        homePage.setUserDetails(role, hasAccount, dbName, id);   
	        homePage.displayName();
	        homePage.initializeAds();
	    } else if (isOrderBtn) {
	        OrderController orderPage = loader.getController();
	        orderPage.setOrders(orderList);
	        orderPage.setUserDetails(role, hasAccount, dbName, id);  
	    } else if (isTableBtn) {
	        TableReservationController tablePage = loader.getController();
	        tablePage.setUserDetails(role, hasAccount, dbName, id);  
	        tablePage.initialize();
	        tablePage.setOrderList(orderList);
	    } else if (isRewardBtn) {
	        RewardsController rewardPage = loader.getController();
	        rewardPage.setOrderList(orderList);
	        rewardPage.setUserDetails(role, hasAccount, dbName, id);  
	        rewardPage.displayName();
	        rewardPage.showCoins();
	    } else if (isAccBtn) {
		    AccountDetailsController accPage = loader.getController();
		    accPage.setOrders(orderList);
		    accPage.setUserDetails(role, hasAccount, dbName, id);  
		    accPage.displayName();
	    } else {
	        CartController cartPage = loader.getController();
	        cartPage.setOrders(orderList);
	        cartPage.setUserDetails(role, hasAccount, dbName, id);  
	        cartPage.displayName();
	    
	    }
	}
	private void handleAdminControllers(FXMLLoader loader) throws SQLException {
		if (isHomeBtn) {
	        AdminController homePage = loader.getController();
	        homePage.setUserDetails(role, hasAccount, dbName, id);   
	        homePage.displayName();
		}
		else if (isOrderBtn) {
	        AdOrderController adOrderPage = loader.getController();
	        adOrderPage.setUserDetails(role, hasAccount, dbName, id);  
	    } else if (isCartBtn) {
	        AdStockController stockPage = loader.getController();
	        stockPage.setUserDetails(role, hasAccount, dbName, id);
	        stockPage.displayName();
	    } else if (isRewardBtn) {
	    		EmploymentController employmentPage = loader.getController();
	 	        employmentPage.setUserDetails(role, hasAccount, dbName, id);
	 	        employmentPage.displayName();
	    } else if (isAccBtn) {
	        AccountDetailsController accPage = loader.getController();
	        accPage.setUserDetails(role, hasAccount, dbName, id);
	        accPage.displayName();
	    }
	    else if (isTableBtn) {
	        AdTableReservationController tablePage = loader.getController();
	        tablePage.setUserDetails(role, hasAccount, dbName, id);
	        tablePage.initialize();
	    }
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
	public void showTooltip(String text, TextField field) {
		Tooltip tooltip = new Tooltip("Please enter your " + text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px;");
        // Set the show delay (in milliseconds)
        tooltip.setShowDelay(javafx.util.Duration.ZERO);

        // Setting tooltip to the text field
        Tooltip.install(field, tooltip);
	}
	private void slideWindow() {
		
		slider.setTranslateX(-200);
		menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-200);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(false);
                menuClose.setVisible(true);
            });
        });

        menuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-200);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(true);
                menuClose.setVisible(false);
            });
        });
        
        
        account.setVisible(false);
		accountClose.setVisible(true);
		
		account.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);
            
            slide.setToX(0);
            slide.play();
           
            slider2.setTranslateX(215);
           
            slide.setOnFinished((ActionEvent e)-> {
                account.setVisible(false);
                accountClose.setVisible(true);
            });
        });
		
		accountClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);

            slide.setToX(215);
            slide.play();
            
            slider2.setTranslateX(0);
            
     
            slide.setOnFinished((ActionEvent e)-> {
            	account.setVisible(true);
            	accountClose.setVisible(false);
            });
        });
	}
	public void toggleTextField(boolean toggle) {
		nameTextField.setEditable(toggle);
		usernameTextField.setEditable(toggle);
		passwordField.setEditable(toggle);
		addressTextField.setEditable(toggle);
		emailField.setEditable(toggle);
	}

	
	//SETTERS 
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
	    this.role = role;
	    this.hasAccount = hasAccount;
	    this.dbName = dbName;
	    this.id = id;
	}
	public void setOrders(List<OrderData> orderList) {
		this.orderList = orderList;
	}
	
	public void setField() {
		toggleTextField(false);
		showTooltip("name", nameTextField);
		showTooltip("username", usernameTextField);
		showTooltip("password", passwordField);
		showTooltip("email", emailField);
		showTooltip("address", addressTextField);	
	}
	 
	 public void displayIcon() {
			if(role.equals("Owner") || role.equals("Employee")) {
				img1.setImage(stockImg);
				img2.setImage(employmentImg);
				btn1.setText("Stocks");
				btn2.setText("Employment");
			}
		}
	 private void displayLogout() {
			if(!hasAccount) {
				logBtn.setText("Sign in");
			}
			else {
				logBtn.setText("Logout");
				signHbox.getChildren().remove(signupBtn);
			}
		}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Connect();
		slideWindow();
		
		try {
			editDetails();
			saveDetails();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 PauseTransition delay = new PauseTransition(Duration.millis(10));
		        delay.setOnFinished(event -> {
		        	try {
		        		displayLogout();
		        		displayIcon();
		        		setField();
						displayDetails();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        });
		        delay.play();

	}
	
	
	
}
