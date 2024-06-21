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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class EmploymentController implements Initializable{
	@FXML
	private Button updateBtn;
	@FXML
	private Button addBtn;
	@FXML
	private Button removeBtn;
	@FXML
	private Button removeAllBtn;
	@FXML
    private TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    @FXML
    private TableColumn<Employee, Integer> ageColumn;

    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    @FXML
    private TableColumn<Employee, String> roleColumn;

    @FXML
    private TableColumn<Employee, String> genderColumn;

	
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
	
	
	int employee_id;
	String employee_name;
	
	
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
	
	//LEFT PANEL
	public void homeBtn(ActionEvent event) throws IOException, SQLException {
		System.out.println("Home");
		isHomeBtn = true;
		changeScene(event, homePage);
}
	public void orderBtn(ActionEvent event) throws IOException, SQLException {
		System.out.println("Order");
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
		isEmploymentBtn = true;
		changeScene(event, employmentPage);
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
			try {

				ObservableList<Employee> employees = DatabaseHelper.getAllEmployees();

	            // Set data to the TableView
	            tableView.setItems(employees);

	            // Set cell value factory for each column
	            idColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getId()).asObject());
	            nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
	            emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
	            ageColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getAge()).asObject());
	            salaryColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getSalary()).asObject());
	            roleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRole()));
	            genderColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGender()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			updateBtn.setOnAction(event -> {
				try {
					handleUpdateButton();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			removeAllBtn.setOnAction(event -> handleRemoveAllButton());
			addBtn.setOnAction(event -> handleAddButton());
			removeBtn.setOnAction(event -> handleRemoveButton());
			Connect();
			slideWindow();
	    }
	
	public void handleAddButton() {
		boolean validId = false;
	    EmployeeInputDialog dialog = new EmployeeInputDialog();
	    Employee emp = dialog.showAndWait();
	    
	    if (emp != null) {
	        validId = DatabaseHelper.insertEmployee(emp);
	        if(validId) {
	        	tableView.getItems().add(emp);}
	    } 
	}

	 
	private boolean isValidNumber(String input) {
	    try {
	        int age = Integer.parseInt(input);
	        // Check if age is a valid integer and does not exceed 100
	        return age >= 0 && age <= 100;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	 private boolean isGenderValid(String gender) {
	        return gender != null && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
	    }


	private boolean isValidDouble(String input) {
	    try {
	        Double.parseDouble(input);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	private void handleUpdateButton() throws SQLException {
	    Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
	    if (selectedEmployee != null) {
	        // Create a dialog for editing employee details
	        Dialog<Pair<String, Employee>> dialog = new Dialog<>();
	        dialog.setTitle("Edit Employee");
	        dialog.setHeaderText("Edit Employee Details");

	        // Set the button types
	        ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
	        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

	        // Create labels and text fields for each detail
	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(10);

	        TextField idField = new TextField(Integer.toString(selectedEmployee.getId())); // ID field (disabled)
	        TextField nameField = new TextField(selectedEmployee.getName());
	        TextField emailField = new TextField(selectedEmployee.getEmail());
	        TextField ageField = new TextField(Integer.toString(selectedEmployee.getAge()));
	        TextField salaryField = new TextField(Double.toString(selectedEmployee.getSalary()));
	        TextField roleField = new TextField(selectedEmployee.getRole());
	        TextField genderField = new TextField(selectedEmployee.getGender());

	        idField.setDisable(true); // Disable editing of ID field

	        grid.add(new Label("ID:"), 0, 0);
	        grid.add(idField, 1, 0);
	        grid.add(new Label("Name:"), 0, 1);
	        grid.add(nameField, 1, 1);
	        grid.add(new Label("Email:"), 0, 2);
	        grid.add(emailField, 1, 2);
	        grid.add(new Label("Age:"), 0, 3);
	        grid.add(ageField, 1, 3);
	        grid.add(new Label("Salary:"), 0, 4);
	        grid.add(salaryField, 1, 4);
	        grid.add(new Label("Role:"), 0, 5);
	        grid.add(roleField, 1, 5);
	        grid.add(new Label("Gender:"), 0, 6);
	        grid.add(genderField, 1, 6);

	        dialog.getDialogPane().setContent(grid);

	        // Convert the result to a pair of button type and employee
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == updateButtonType) {
	                // Validate age, salary, and gender fields
	                if (!isValidNumber(ageField.getText())) {
	                    showAlert("Age must be a valid integer.", AlertType.ERROR);
	                    return null; // Return null to not update employee
	                }
	                if (!isValidDouble(salaryField.getText())) {
	                    showAlert("Salary must be a valid number.", AlertType.ERROR);
	                    return null; // Return null to not update employee
	                }
	                if (!isGenderValid(genderField.getText())) {
	                    showAlert("Gender should be either 'male' or 'female'.", AlertType.ERROR);
	                    return null; // Return null to not update employee
	                }

	                // Update employee details
	                selectedEmployee.setName(nameField.getText());
	                selectedEmployee.setEmail(emailField.getText());
	                selectedEmployee.setAge(Integer.parseInt(ageField.getText()));
	                selectedEmployee.setSalary(Double.parseDouble(salaryField.getText()));
	                selectedEmployee.setRole(roleField.getText());
	                selectedEmployee.setGender(genderField.getText());
	                return new Pair<>("Update", selectedEmployee);
	            }
	            return null;
	        });

	        // Show the dialog and wait for user input
	        dialog.showAndWait().ifPresent(result -> {
	            if (result != null) {
	                // Update employee in the database
	                try {
	                    DatabaseHelper.updateEmployee(result.getValue());
	                    // Refresh TableView or any other UI updates
	                    tableView.refresh();
	                } catch (SQLException e) {
	                    e.printStackTrace(); // Handle exception appropriately
	                }
	            }
	        });
	    } else {
	        // No employee selected, show an alert
	        showAlert("Please select an employee to update.", AlertType.ERROR);
	    }
	}


    
	
	 private void handleRemoveButton() {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Employee");
            alert.setContentText("Are you sure you want to delete the selected employee?");

            // Show confirmation dialog and wait for user response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Delete employee from database
                    DatabaseHelper.deleteEmployee(selectedEmployee);
                    // Remove employee from TableView
                    tableView.getItems().remove(selectedEmployee);
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exception appropriately
                }
            }
        } else {
            // No employee selected, show an alert
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Employee Selected");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee to delete.");
            alert.showAndWait();
        }
    }
	 private void handleRemoveAllButton() {
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Confirm Delete");
	        alert.setHeaderText("Delete All Employees");
	        alert.setContentText("Are you sure you want to delete all employees?");

	        // Show confirmation dialog and wait for user response
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            try {
	                // Delete all employees from the database
	                DatabaseHelper.deleteAllEmployees();
	                // Clear TableView
	                tableView.getItems().clear();
	            } catch (SQLException e) {
	                e.printStackTrace(); // Handle exception appropriately
	            }
	        }
	    }
	
	
}
