package application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class AdStockController implements Initializable{
	@FXML
	private Button employmentBtn;
	@FXML
	private Button uploadBtn;
	@FXML
	private Label roleLabel;
	@FXML
	private Button addBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private TextField foodnameField;
	@FXML
	private TextField optionField;	
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ScrollPane scrollPane; // ScrollPane for containing VBox
    @FXML
    private VBox itemsVBox; // VBox for displaying items
	@FXML
	private Label nameLabel;
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
  
    private MenuItem selectedItem;
    
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();

	int id;
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
	
	//STOCKS VARIABLE
	String foodname;
	String option;
	String category;
	
    int price;
    int stock;
	
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	

	
	//TOP BUTTONS
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
	
	//SIDE BUTTONS
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
	public void showCart(ActionEvent event) throws IOException, SQLException {
			
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
		
	//STOCK METHODS
	private void loadMenuItemsByCategory(String category) {
	        try {
	            List<MenuItem> items;
	            if ("All".equals(category)) {
	                items = DatabaseHelper.getMenuItems();
	            } else {
	                items = DatabaseHelper.filterMenuItemsByCategory(category);
	            }
	            displayMenuItems(items);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	private void loadMenuItems() {
	    try {
	        List<MenuItem> items = DatabaseHelper.getMenuItems();
	        displayMenuItems(items);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private void displayMenuItems(List<MenuItem> items) {
	    // Sort items alphabetically by food name
		items.sort(Comparator
		        .comparing(MenuItem::getFoodName)
		        .thenComparing(MenuItem::getCategory)
		        .thenComparing(item -> getFirstOption(item.getOptions())));

	    // Clear existing items
	    itemsVBox.getChildren().clear();

	    // Loop through sorted items and add them to the VBox
	    for (MenuItem item : items) {
	        try {
	            // Load the FXML for each item
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdStockVbox.fxml"));
	            HBox itemBox = loader.load();

	            // Get the controller and set item details
	            AdStockPaneController controller = loader.getController();
	            controller.setItemDetails(item);

	            // Set an on-click event for the item box
	            itemBox.setOnMouseClicked(event -> selectMenuItem(item));

	            // Add the item box to the VBox
	            itemsVBox.getChildren().add(itemBox);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	private String getFirstOption(String options) {
	    if (options != null && !options.isEmpty()) {
	        String[] optionArray = options.split(",");
	        return optionArray[0].trim();
	    }
	    return "";
	}
	private void selectMenuItem(MenuItem item) {
		    selectedItem = item;
		    foodnameField.setText(item.getFoodName());
		    priceField.setText(String.valueOf(item.getPrice()));
		    stockField.setText(String.valueOf(item.getStock()));
		    categoryComboBox.setValue(item.getCategory());
		    optionField.setText(item.getOptions());
		    imageView.setImage(DatabaseHelper.getImageFromDatabase(item.getFoodName()));
		}
	private void addMenuItem() {
		    try {
		    	 String foodName = foodnameField.getText();
		         String option = optionField.getText();
		         String priceText = priceField.getText();
		         String stockText = stockField.getText();
		         String category = categoryComboBox.getValue();
		        // Check if the number of options for the food item exceeds 2
		        int optionCount = DatabaseHelper.getOptionCountForFoodName(foodName, category);
		        if (DatabaseHelper.isItemExists(foodName, option)) {
		            // Display an alert to inform the user that the item already exists
		            showAlert("An item with the same name and option already exists.", AlertType.ERROR);
		            return; // Exit the method
		        }
		        else if (optionCount >= 2) {
		            // Display an alert to the user
		        	showAlert("You can only add up to 2 options for each food item.", AlertType.ERROR);
		            return; // Exit the method
		        }
		        else if (foodName.isEmpty() || option.isEmpty() || priceField.getText().isEmpty() || stockField.getText().isEmpty() || category == null) {
		            // Display an alert to inform the user to fill in all fields
		            showAlert("Please fill in all required fields.", AlertType.ERROR);
		            return; // Exit the method
		        }
		        else if (!priceText.matches("\\d+") || !stockText.matches("\\d+")) {
		            // Display an alert to inform the user that price and stock must be integers
		        	showAlert("Price and stock must be integers.", AlertType.ERROR);
		            return; // Exit the method
		        }
		        
		        int price = Integer.parseInt(priceText);
		        int stock = Integer.parseInt(stockText);

		        
		        MenuItem item = new MenuItem(0, foodName, stock, price, option, category, imageBytes);
		    
		        DatabaseHelper.addMenuItem(item);
		      
			    
		        clearFields();
		        loadMenuItems();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	private void updateMenuItem() {
	        try {
	            if (selectedItem != null) {
	                String foodname = foodnameField.getText();
	                String option = optionField.getText();
	                int price = Integer.parseInt(priceField.getText());
	                int stock = Integer.parseInt(stockField.getText());
	                String category = categoryComboBox.getValue();
	                

	                MenuItem updatedItem = new MenuItem(selectedItem.getId(), foodname, stock, price, option, category);
	                DatabaseHelper.updateMenuItem(updatedItem);
	                if (foodname != null && imageBytes != null) {
				        DatabaseHelper.updateMenuItemImage(foodname, imageBytes);
				    }
	                clearFields();
	                loadMenuItems();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	public void deleteMenuItem(MenuItem item) {
	        try {
	            DatabaseHelper.deleteMenuItem(item.getId()); // Add method to delete from database
	            loadMenuItems(); // Reload items after deletion
	            clearFields();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	 }
	private void clearFields() {
	        foodnameField.clear();
	        optionField.clear();
	        priceField.clear();
	        stockField.clear();
	        categoryComboBox.setValue(null);
	        selectedItem = null;
	        imageView.setImage(null);
	    }
	
	//SETTER METHODS
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
		 this.role = role;
		 this.hasAccount = hasAccount;
		 this.dbName = dbName;
		 this.id = id;
	}
	public void setFoodname(String foodName) {
		this.foodname = foodName;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public void setOrderList(List<OrderData> orderList) {
		this.orderList = orderList;
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
			}else if(isOrderBtn) {
				AdOrderController adOrderPage = loader.getController();
				adOrderPage.setUserDetails(role, hasAccount, dbName, id);
				
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
	private void initializeComboBox() {
		categoryComboBox.setItems(FXCollections.observableArrayList(
	            "Rice Meals", "Pasta", "Cakes", "Burger",
	            "Coffee", "Frappe", "Tea", "Appetizers"
	        ));
			filterComboBox.setItems(FXCollections.observableArrayList(
					"All", "Rice Meals", "Pasta", "Cakes", "Burger",
		            "Coffee", "Frappe", "Tea", "Appetizers"
		        ));
			categoryComboBox.setValue("Choose Category");
			filterComboBox.setValue("Filter");
	        loadMenuItemsByCategory("All");
	        
			filterComboBox.setOnAction(event -> {
	            String selectedCategory = filterComboBox.getValue();
	            loadMenuItemsByCategory(selectedCategory);
	        });
	}
	
	 
	 private byte[] imageBytes;
	@FXML
	private ImageView imageView;
	
	private void handleUploadButtonAction(Window window) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile != null) {
            try {
                FileInputStream fis = new FileInputStream(selectedFile);
                imageBytes = fis.readAllBytes();
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                imageView.setImage(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            // Set default image if no image is uploaded
            imageBytes = getDefaultImageBytes();
            Image defaultImage = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(defaultImage);
        }
    }
	public static byte[] getDefaultImageBytes() throws FileNotFoundException {
        try (FileInputStream fis = new FileInputStream("src\\pictures\\fast-food.png")) {
            return fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			uploadBtn.setOnAction(e -> {
				try {
					handleUploadButtonAction(uploadBtn.getScene().getWindow());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		
	        initializeComboBox();
			Connect();
			slideWindow();
			addBtn.setOnAction(event -> addMenuItem());
	        updateBtn.setOnAction(event -> updateMenuItem());
	        deleteBtn.setOnAction(event -> deleteMenuItem(selectedItem));
		}
	
}
