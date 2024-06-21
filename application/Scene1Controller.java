package application;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Scene1Controller implements Initializable{
	
    String password;
    String username = "";
    String name = "";
    String query = "SELECT id, name FROM user_tbl WHERE username = ?";
    
    boolean hasCorrectPassword = false;
    boolean hasCorrectUsername = false;
    boolean usernameFound = false;
    boolean passwordFound = false;
    boolean hasCorrectName = false;
    boolean hasAccount = false;
    
    @FXML
    TextField usernameField;
    @FXML
    PasswordField pass_hidden;
    @FXML
    TextField pass_text;
    @FXML
    CheckBox pass_toggle;
    @FXML
    Button signInBtn;
 
    private Stage stage;
    private Scene scene;
    private Parent root;
	
    Connection con;
    
    //MAIN BUTTONS
    public void login(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
		Connect();
		checkUsernameField();
		if(hasCorrectUsername) {
			checkPasswordField();
			hasCorrectUsername = false;
		}
		if(hasCorrectPassword) {
			hasCorrectPassword = false;
			hasAccount = true;
			changeScene(event);
		}
    }
    public void signUp(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
		Parent root = loader.load();
			
		SignUpController signUpPage = loader.getController();
	    signUpPage.Connect();
	
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
    //CHECKER METHODS
    public void checkUsernameField() throws IOException, SQLException{
		PreparedStatement pst;
		ResultSet rs;
		username = usernameField.getText();

		//if the username is empty
		if(username.isEmpty()) showAlert("Please enter your username", AlertType.ERROR); 
		else if(username.length() < 8) showAlert("Username should be atleast 8 characters", AlertType.ERROR);
		else {
			pst = con.prepareStatement("SELECT * FROM user_tbl");
			rs = pst.executeQuery();
			while(rs.next()) {
				String uname = rs.getString("username");
				if((username.equals(uname))) {
					hasCorrectUsername = true;
					usernameFound = true;
					return;
				}
			}
			if(usernameFound == false) {
				showAlert("Username does not exist", AlertType.ERROR);
				hasCorrectUsername = false;
			}
			usernameFound = false;
		}
	}
    public void checkPasswordField() throws IOException{
		PreparedStatement pst;
		ResultSet rs;
		password = passwordValue();
		
		//IF THE PASSWORD IS EMPTY
		if(password.isEmpty()) showAlert("Please enter your password", AlertType.ERROR);
		else if(password.length() < 8) showAlert("Password should be atleast 8 characters", AlertType.ERROR);
		else {
			try {
				pst = con.prepareStatement("SELECT * FROM user_tbl");
				rs = pst.executeQuery();
				while(rs.next()) {
					String pword = rs.getString("password");
					if((password.equals(pword))) {
						hasCorrectPassword = true;
						passwordFound = true;
						return;
					}
				}
				if(passwordFound == false) {
					showAlert("Incorrect Password", AlertType.ERROR);
					hasCorrectPassword = false;
				}
				passwordFound = false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    //HELPER METHODS
    private void showAlert(String contentText, AlertType alertType) {

        Alert alert = new Alert(alertType);
        alert.setHeaderText("");
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
        }, 1 * 1000);
    }
    public void changeScene(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
    	
    	Connection con;
    	PreparedStatement pst;
    	ResultSet rs;
    	int id = 0;
    	Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "root", "");
		String page;
		
		if(username.equals("admin123") || (DatabaseHelper.verifyUser(username, password))) {
			page = "AdHomePage.fxml";
		}
		else {
			page = "HomePage.fxml";
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		if(username.equals("admin123")) {
			AdminController adminPage = loader.getController();
			pst = con.prepareStatement(query);
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
				id = rs.getInt("id");
				}
			adminPage.setUserDetails("Owner", hasAccount, name, 2);
			adminPage.displayName();
			
		}
		else if(DatabaseHelper.verifyUser(username, password)) {
			
			AdminController employeePage = loader.getController();
			pst = con.prepareStatement(query);
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
				id = rs.getInt("id");
				}
			employeePage.setUserDetails("Employee", hasAccount, name, id);
			employeePage.displayName();
		}
		else {
			HomeController homePage = loader.getController();
			pst = con.prepareStatement(query);
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
				id = rs.getInt("id");
				}
			
			homePage.setUserDetails("Customer", hasAccount, name, id);
			homePage.displayName();
			homePage.initializeAds();

		}
		
	}
    public void Connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "root", "");
	}
    
    public void orderAsGuest(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		HomeController homePage = loader.getController();
		homePage.setUserDetails("Customer", hasAccount, "Guest", 0);
    }
    
    //SHOW PASSWORD METHODS
    public void togglevisiblePassword(ActionEvent event) throws IOException{
	    if (pass_toggle.isSelected()) {
	        pass_text.setText(pass_hidden.getText());
	        pass_text.setVisible(true);
	        pass_hidden.setVisible(false);
	        return;
	    }
	    pass_hidden.setText(pass_text.getText());
	    pass_hidden.setVisible(true);
	    pass_text.setVisible(false);
	}
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
	    try {
			this.togglevisiblePassword(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    enableEnterKeyLogin(usernameField, pass_hidden, signInBtn);
	   
	    signInBtn.setOnAction(event -> {
			try {
				login(event);
			} catch (ClassNotFoundException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }
   
    private String passwordValue() {
	    return pass_toggle.isSelected()?
	       pass_text.getText(): pass_hidden.getText();
	}
    
    private void enableEnterKeyLogin(TextField usernameField, PasswordField passwordField, Button loginButton) {
        EventHandler<KeyEvent> enterKeyHandler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        };
        
        usernameField.setOnKeyPressed(enterKeyHandler);
        passwordField.setOnKeyPressed(enterKeyHandler);
    }
}
