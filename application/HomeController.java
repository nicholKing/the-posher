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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController implements Initializable{
	
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
    private ImageView ad1;
    @FXML
    private ImageView ad2;
    @FXML
    private ImageView ad3;
    @FXML
    private ImageView ad4;
    
    @FXML
    private HBox signHbox;
    @FXML
    private Button logBtn;
    @FXML
    private Button signupBtn;
    
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();
	
	String role = "Customer";
	String name;
	String username = "";
	String orderPage = "OrderPage.fxml";
    String homePage = "HomePage.fxml";
    String accPage = "AccountDetails.fxml";
    String cartPage = "MyCart.fxml";
    String tablePage = "TableReservationPage.fxml";
    String rewardsPage = "RewardsPage.fxml";
    
    String query = "SELECT name FROM user_tbl WHERE id = ?";
    String dbName = "Guest";
	boolean hasAccount = false;
	boolean isHomeBtn = false; //reset the condition
	boolean isOrderBtn = false;
	boolean isAccBtn = false;
	boolean isTableBtn = false;
	boolean isRewardBtn = false;
	
	int id;
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	//RIGHT PANEL
	public void displayName() throws SQLException {
		if(dbName.length() >= 12 && dbName.length() <= 15) {
			nameLabel.setFont(new Font(18));
		}
		else if(dbName.length() > 15) {
			nameLabel.setFont(new Font(15));
		}
		nameLabel.setText(dbName);
		roleLabel.setText(role);
	}
	public void showAccount(ActionEvent event) throws IOException, SQLException {
		if(hasAccount) {
			isAccBtn = true;
			changeScene(event, accPage);}
			else {showAlert("Login or register to edit your information.", AlertType.INFORMATION);}
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
	
	public void initializeAds() {
		
		addHoverEffect(ad1);
        addHoverEffect(ad2);
        addHoverEffect(ad3);
        addHoverEffect(ad4);

	}
	
	private DropShadow dropShadow = new DropShadow(10, javafx.scene.paint.Color.GRAY);

	
	private void addHoverEffect(ImageView imageView) {
        imageView.setOnMouseEntered(event -> {
        	imageView.setEffect(dropShadow);
            imageView.setScaleX(1.03); // Scale to 120% of original size
            imageView.setScaleY(1.03);
        });

        imageView.setOnMouseExited(event -> {
        	imageView.setEffect(null);
            imageView.setScaleX(1.0); // Reset scale to original size
            imageView.setScaleY(1.0);
        });
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
	public void showCart(ActionEvent event) throws IOException, SQLException {
			changeScene(event,cartPage);
	}
	public void showTable(ActionEvent event) throws IOException, SQLException {
		isTableBtn = true;
		changeScene(event, tablePage);
	}
	public void showRewards(ActionEvent event) throws IOException, SQLException {
			if(hasAccount) {
				isRewardBtn = true;
				changeScene(event, rewardsPage);
			}
			else {
				showAlert("Create an account to unlock exciting rewards!", AlertType.INFORMATION);
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
				HomeController homePage = loader.getController();
				homePage.setOrderList(orderList);
				homePage.setUserDetails(role, hasAccount, dbName, id);
				homePage.initializeAds();
			}
			else if(isOrderBtn) {
				OrderController orderPage = loader.getController();
				orderPage.setOrders(orderList);
				orderPage.setUserDetails(role, hasAccount, dbName, id);
			}else if(isTableBtn) {
				TableReservationController tablePage = loader.getController();
				tablePage.setUserDetails(role, hasAccount, dbName, id);
				tablePage.initialize();
			}else if(isAccBtn) {
				AccountDetailsController accPage = loader.getController();
				accPage.setOrders(orderList);
				accPage.setUserDetails(role, hasAccount, dbName, id);
				accPage.displayName();
			}else if(isRewardBtn) {
				RewardsController rewardPage = loader.getController();
				rewardPage.setOrderList(orderList);
				rewardPage.setUserDetails(role, hasAccount, dbName, id);
				rewardPage.showCoins();
				rewardPage.displayName();
			}else {
				CartController cartPage = loader.getController();
				cartPage.setOrders(orderList);
				cartPage.setUserDetails(role, hasAccount, dbName, id);
				cartPage.displayName();
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
	
	private void changeScene2(MouseEvent event, String page) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
		root = loader.load();

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		if(isHomeBtn) {
			HomeController homePage = loader.getController();
			homePage.setOrderList(orderList);
			homePage.setUserDetails(role, hasAccount, dbName, id);
			homePage.initializeAds();
			homePage.displayName();
		}
		else if(isOrderBtn) {
			OrderController orderPage = loader.getController();
			orderPage.setOrders(orderList);
			orderPage.setUserDetails(role, hasAccount, dbName, id);
		}else if(isTableBtn) {
			TableReservationController tablePage = loader.getController();
			tablePage.setUserDetails(role, hasAccount, dbName, id);
			tablePage.initialize();
		}else if(isAccBtn) {
			AccountDetailsController accPage = loader.getController();
			accPage.setOrders(orderList);
			accPage.setUserDetails(role, hasAccount, dbName, id);
			accPage.displayName();
		}else if(isRewardBtn) {
			RewardsController rewardPage = loader.getController();
			rewardPage.setOrderList(orderList);
			rewardPage.setUserDetails(role, hasAccount, dbName, id);
			rewardPage.showCoins();
			rewardPage.displayName();
		}	
		else {
			CartController cartPage = loader.getController();
			cartPage.setOrders(orderList);
			cartPage.setUserDetails(role, hasAccount, dbName, id);
			cartPage.displayName();
		}
}
	private void switchPage() {
		
		ad1.setOnMouseClicked(event -> {
			try {
				isOrderBtn = true;
				changeScene2(event, orderPage);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		ad2.setOnMouseClicked(event -> {
			try {
				if(hasAccount) {
					isRewardBtn = true;
					changeScene2(event, rewardsPage);
				}
				else {
					showAlert("Create an account to unlock exciting rewards!", AlertType.INFORMATION);
				}
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		ad3.setOnMouseClicked(event -> {
			try {
				isOrderBtn = true;
				changeScene2(event, orderPage);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		ad4.setOnMouseClicked(event -> {
			try {
				isTableBtn = true;
				changeScene2(event, tablePage);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	//SETTER METHODS
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
		    this.role = role;
		    this.hasAccount = hasAccount;
		    this.dbName = dbName;
		    this.id = id;
		}
	public void setOrderList(List<OrderData> orderList) {
			this.orderList = orderList;
		}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			try {
				switchPage();
				displayName();
				displayLogout();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connect();
			slideWindow();
	    }
}
