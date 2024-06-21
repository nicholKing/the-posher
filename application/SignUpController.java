package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SignUpController {
	
	@FXML
	TextField nameField;
	@FXML
	TextField usernameField;
	@FXML
	PasswordField pass_hidden;
	@FXML
	TextField pass_text;
	@FXML
	CheckBox pass_toggle;

	String name;
	String password;
	String username;
	
	boolean hasCorrectPassword = false;
	boolean hasCorrectUsername = false;
	boolean hasCorrectName = false;
	boolean newUsername = true;
	boolean newPassword = true;
	boolean newName = true;
	boolean loginSuccessful = false;
	boolean hasCompleteForm = false;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	Connection con;
	
	public void signUp(ActionEvent event) throws IOException{
			
			checkNameField();
			if (hasCorrectName) {
				checkUsernameField();
				hasCorrectName = false;
			}
			if(hasCorrectUsername) {
				checkPasswordField();
				hasCorrectUsername = false;
			}
			if(hasCorrectPassword) {
				registerAccount(event);
			}
			
	}
	
	//CHECKER METHODS
	public void checkNameField() {
		PreparedStatement pst;
		ResultSet rs;
		name = nameField.getText();
		
		if(name.isEmpty()) showAlert("Please enter a name", AlertType.ERROR); 
		else if(name.length() > 23) showAlert("Please enter a shorter name", AlertType.ERROR); 
		else {
			try {
				pst = con.prepareStatement("SELECT * FROM user_tbl");
				rs = pst.executeQuery();
				while(rs.next()) {
					String dbName = rs.getString("name");
					if((name.equals(dbName))) {
						showAlert("Name already exists", AlertType.ERROR);
						hasCorrectName = false;
						newName = false;
						return;
					}
					else {
						newName = true;
					}
					
				}
				if(newName == true) { //
					System.out.println("Valid name!");
					hasCorrectName = true;
				}
				newName = false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return;
	}
	public void checkUsernameField() {
		
		PreparedStatement pst;
		ResultSet rs;
		username = usernameField.getText();
		
		if(username.isEmpty()) {showAlert("Please enter a username", AlertType.ERROR);}
		else if(username.length() < 8) {showAlert("Username should be atleast 8 characters", AlertType.ERROR); return;}
		else if(username.length() > 23) {showAlert("Please enter a shorter username", AlertType.ERROR); return;}
		else {
			try {
				pst = con.prepareStatement("SELECT * FROM user_tbl");
				rs = pst.executeQuery();
	
				while(rs.next()) {
					String dbUsername = rs.getString("username");
					if((username.equals(dbUsername))) {
						showAlert("Username already exists", AlertType.ERROR);
						hasCorrectUsername = false;
						newUsername = false;
						return;
					}
					else {
						newUsername = true;
					}
				}
				if(newUsername == true) {
					hasCorrectUsername = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void checkPasswordField() {
		password = passwordValue();
		if(password.length() == 0) {showAlert("Please enter your password", AlertType.ERROR);}
		else if(password.length() < 8) {showAlert("Password should be atleast 8 characters", AlertType.ERROR);}
		else {hasCorrectPassword = true;}
		return;
	}
	public void registerAccount(ActionEvent event) throws IOException {
		PreparedStatement pst;
		ResultSet rs;
		String newName = name;
		String newUsername = username;
		String newPassword = password;
		try {
			
			pst = con.prepareStatement("INSERT INTO user_tbl (name, username, password)VALUES(?,?,?)");
			pst.setString(1, newName);
			pst.setString(2, newUsername);
			pst.setString(3, newPassword);
			
			int k = pst.executeUpdate();
			
			if(k==1) {
				showAlert("Your account has been registered!", AlertType.INFORMATION);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeScene(event);
	}
	
	//HELPER METHODS
	public void Connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "root", "");
	}
	public void changeScene(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		
		Scene1Controller loginPage = loader.getController();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
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
	
	//SHOW PASSWORD METHODS
	public void togglevisiblePassword(ActionEvent event) {
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
	public void initialize(URL location, ResourceBundle resources) {
	    this.togglevisiblePassword(null);
	}
	private String passwordValue() {
	    return pass_toggle.isSelected()?
	       pass_text.getText(): pass_hidden.getText();
	}
}
