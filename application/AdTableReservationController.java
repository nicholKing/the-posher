package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;

public class AdTableReservationController implements Initializable {
	
    @FXML
    private AnchorPane slider;
    @FXML
    private ImageView menuClose;
    @FXML
    private ImageView menu;
    @FXML
    private AnchorPane slider2;
    @FXML
    private ImageView accountClose;
    @FXML
    private ImageView account;
    @FXML
    private VBox orderLayout;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private VBox vBox;
    @FXML
    private Button timeButton;
    @FXML
    private Label tableNumberLabel;
    @FXML
    private ImageView oneSeat;
    @FXML
    private ImageView twoSeat;
    @FXML
    private ImageView fourSeat;
    @FXML
    private ImageView fourSeat2;
    @FXML
    private ImageView fiveSeat;
    @FXML
    private ImageView sixSeat;
    @FXML
    private ImageView eightSeat;
    @FXML
    private ImageView eightSeat2;
    @FXML
    private ImageView tenSeat;
    @FXML
    private Button bookNow;
    @FXML
    private Label tryLabel;
    @FXML
    private TableView<Reservation> tableView;
    @FXML
    private TableColumn<Reservation, String> emailColumn;
    @FXML
    private TableColumn<Reservation, String> nameColumn;
    @FXML
    private TableColumn<Reservation, String> dateColumn;
    @FXML
    private TableColumn<Reservation, String> timeColumn;
    @FXML
    private TableColumn<Reservation, Integer> tableColumn;
    @FXML
    private TableColumn<Reservation, Boolean> reservedColumn;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private TextField nameField;
    @FXML
    private CheckBox reservedCheckBox;
    @FXML
    private Button updateButton;

    private final ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
    
   
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();
	OrderData orderData = new OrderData();
	

	String name;
	String username = "";
    String rewardsPage = "RewardsPage.fxml";
    String query = "SELECT name FROM user_tbl WHERE id = ?";
    public int reservedTable;
	
	boolean clicked = false;
	
	String role;
	String dbName;
	String orderPage = "AdOrderPage.fxml";
    String homePage = "AdHomePage.fxml";
    String accPage = "AccountDetails.fxml";
    String stockPage = "AdStockPage.fxml";
    String tablePage = "AdTableReservationPage.fxml";
    String employmentPage = "EmploymentPage.fxml";
    
    int id;
    
    boolean hasAccount = false;
	boolean isHomeBtn = false;
    boolean isSideBtn = false;
    boolean isOrderBtn = false;
    boolean isCartBtn = false;
    boolean isTableBtn = false;
    boolean isAccBtn = false;
    boolean isEmploymentBtn = false;
	
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
    
    public void initialize() {
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().TableNumberProperty().asObject());
        reservedColumn.setCellValueFactory(cellData -> cellData.getValue().reservedProperty().asObject());
        
        
        tableView.setItems(reservationList);

        loadReservations();
        
        reservedCheckBox.setSelected(false); // Initialize checkbox state

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                datePicker.setValue(parseDate(newSelection.getDate()));
                timeField.setText(newSelection.getTime());
                nameField.setText(newSelection.getName());
                timeField.setText(newSelection.getTime());
                reservedCheckBox.setSelected(newSelection.isReserved());
                updateButton.setDisable(false);
            }
        });
        
        updateButton.setOnAction(event -> updateReservation());
        updateButton.setDisable(true);
    }
    
    
        
    private void loadReservations() {
        List<Reservation> reservations = DatabaseHelper.fetchAllReservations();
        reservationList.setAll(reservations);
    }
    
    

    private void updateReservation() {
        Reservation selectedReservation = tableView.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            showAlert(Alert.AlertType.ERROR, "No Selection", "No Reservation Selected", "Please select a reservation to update.");
            return;
        }

        String email = selectedReservation.getEmail();
        String name = nameField.getText();
        LocalDate date = datePicker.getValue();
        String time = timeField.getText();
        int table_number = selectedReservation.getTableNumber();
        boolean reserved = reservedCheckBox.isSelected();

        if (name.isEmpty() || date == null || time.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter all fields.", "All fields must be filled to update a reservation.");
            return;
        }
        
        if (!reservedCheckBox.isSelected()) {
            DatabaseHelper.deleteReservation(email);
            reservationList.remove(selectedReservation);
            showAlert(Alert.AlertType.INFORMATION, "Reservation Deleted", "Reservation has been deleted successfully.", "Thanks");
        } else {

	        DatabaseHelper.updateReservation(email, name, date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), time, table_number, reserved);
	
	        selectedReservation.setEmail(email);
	        selectedReservation.setName(name);
	        selectedReservation.setDate(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
	        selectedReservation.setTime(time);
	        selectedReservation.setTableNumber(table_number);
	        selectedReservation.setReserved(reserved);
	        tableView.refresh();
	
	        showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation Updated", "The reservation has been updated successfully.");
        }
    }
    
    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
   
    public void tableNumber() {
    	
    	EventHandler<MouseEvent> clearHighlight = event -> {
            oneSeat.setStyle("");
            twoSeat.setStyle("");
            fourSeat.setStyle("");
            fourSeat2.setStyle("");
            fiveSeat.setStyle("");
            sixSeat.setStyle("");
            eightSeat.setStyle("");
            eightSeat2.setStyle("");
            tenSeat.setStyle("");
        };
    	
    	oneSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	oneSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("1");
                // Update int reservedTable
                reservedTable = 1;
                if(!clicked) {
                	 slideWindow();
                	 clicked = true;
                }
               
            }
        });

    	twoSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	twoSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("2");
                reservedTable = 2;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	fourSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	fourSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("4");
                reservedTable = 40;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	fourSeat2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	fourSeat2.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("4");
                reservedTable = 41;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	fiveSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	fiveSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("5");
                reservedTable = 5;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	sixSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	sixSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("6");
                reservedTable = 6;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	eightSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	eightSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("8");
                reservedTable = 80;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	eightSeat2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	eightSeat2.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("8");
                reservedTable = 81;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
    	
    	tenSeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	clearHighlight.handle(event);
            	tenSeat.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-border-color: #561C24; -fx-border-width: 2;");
                // Update the label text
                tableNumberLabel.setText("10");
                reservedTable = 10;
                if(!clicked) {
               	 slideWindow();
               	 clicked = true;
               }
            }
        });
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
	public void slideWindow() {
		 slider2.setTranslateX(400);
           TranslateTransition slide = new TranslateTransition();
          
           slide.setDuration(Duration.seconds(0.5));
           slide.setNode(slider2);
           
           slide.setToX(0);
           slide.play();
           
           slide.setOnFinished((ActionEvent e)-> {
        	   account.setVisible(false);
               accountClose.setVisible(true);
           });
       
	}
	private void setSlides() {
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
        
        
//       
//		account.setOnMouseClicked(event -> {
//            TranslateTransition slide = new TranslateTransition();
//            slide.setDuration(Duration.seconds(0.5));
//            slide.setNode(slider2);
//            
//            slide.setToX(0);
//            slide.play();
//           
//            slider2.setTranslateX(400);
//           
//            slide.setOnFinished((ActionEvent e)-> {
//                account.setVisible(false);
//                accountClose.setVisible(true);
//            });
//        });
		
		accountClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);

            slide.setToX(400);
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
		// TODO Auto-generated method stub
	}
	public void setHasAccount(boolean hasAccount) {
		this.hasAccount = hasAccount;
}
	public void setName(String dbName) {
		this.dbName = dbName;
}
	public void setOrderList(List<OrderData> orderList) {
		this.orderList = orderList;
	}
}