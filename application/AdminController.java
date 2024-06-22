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
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminController implements Initializable{
	
	@FXML
	private Label nameLabel;
	@FXML
	private Label roleLabel;
    @FXML
    private AnchorPane slider;
    @FXML
    private ImageView menuClose;
    @FXML
    private ImageView menu;
    @FXML
    private ImageView account;
    @FXML
    private ImageView accountClose;
    @FXML
    private AnchorPane slider2;
    @FXML
	private Button employmentBtn;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();
	
	String role;
	String name;
	String username = "";
	String orderPage = "AdOrderPage.fxml";
    String homePage = "AdHomePage.fxml";
    String accPage = "AccountDetails.fxml";
    String stockPage = "AdStockPage.fxml";
    String tablePage = "AdTableReservationPage.fxml";
    String employmentPage = "EmploymentPage.fxml";
    
    String query = "SELECT name FROM user_tbl WHERE id = ?";
    String dbName = "Guest";
	boolean hasAccount = false;
	boolean isHomeBtn = false; //reset the condition
	boolean isOrderBtn = false;
	boolean isAccBtn = false;
	boolean isTableBtn = false;
	boolean isEmploymentBtn = false;
	
	int id;
	
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

	//LEFT PANEL
	public void homeBtn(ActionEvent event) throws IOException, SQLException {
		isHomeBtn = true;
		changeScene(event, homePage);
	}
	public void orderBtn(ActionEvent event) throws IOException, SQLException {
		isOrderBtn = true;
		changeScene(event, orderPage);
	}
	public void showStock(ActionEvent event) throws IOException, SQLException {
			changeScene(event,stockPage);
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
				AdTableReservationController adtablePage = loader.getController();
				adtablePage.setUserDetails(role, hasAccount, dbName, id);
				adtablePage.initialize();
			}else if(isAccBtn) {
				AccountDetailsController accPage = loader.getController();
				accPage.setUserDetails(role, hasAccount, dbName, id);
				accPage.displayName();
			}else if(isEmploymentBtn) {
				EmploymentController rewardPage = loader.getController();
				rewardPage.setUserDetails(role, hasAccount, dbName, id);
				rewardPage.displayName();
			}else {
				AdStockController stockPage = loader.getController();
				stockPage.setUserDetails(role, hasAccount, dbName, id);
				stockPage.displayName();
			}	
	}
	
	//SETTER METHODS
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
	    this.role = role;
	    this.hasAccount = hasAccount;
	    this.dbName = dbName;
	    this.id = id;
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
        
        
        slider2.setTranslateX(215);
       
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			
			Connect();
			slideWindow();
	}		
}
