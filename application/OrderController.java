package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import application.MenuItem;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OrderController implements Initializable{
	@FXML
	private Button selectBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ImageView menu;
    @FXML
    private ImageView menuClose;
    @FXML
    private ImageView account;
    @FXML
    private ImageView accountClose;
    @FXML
    private AnchorPane slider2;
    @FXML
    private AnchorPane slider;
    @FXML
    private Label headerLabel;
    @FXML
    private Label nameLabel;

    //ORDER SYSTEM
    @FXML
    private Button addToCartBtn;
    @FXML
    private TextField qtyTextField;
    @FXML
    private Button backBtn;
    @FXML 
    private Button viewCartBtn; 
    @FXML
    private ImageView plusBtn;
    @FXML
    private ImageView minusBtn;
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
    private RadioButton opt1;
    @FXML
    private RadioButton opt2;
    @FXML
    private Label foodNameLabel;
    @SuppressWarnings("exports")
	@FXML 
    public Label priceSlidingLabel;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private ImageView imgSelect;
    private MenuItem selectedItem;
    //JAVA FX
    private Stage stage;
	private Scene scene;
	private Parent root;
	
	List<OrderData> orderList = new ArrayList<>();
	
	int id;
	String dbName;
	String role;
	String orderPage = "OrderPage.fxml";
    String homePage = "HomePage.fxml";
    String accPage = "AccountDetails.fxml";
    String cartPage = "MyCart.fxml";
    String tablePage = "TableReservationPage.fxml";
    String rewardsPage = "RewardsPage.fxml";
    String previousClickedBtn = ""; // Initialize with an empty string
    
	boolean hasAccount = false;
	boolean isHomeBtn = false;
    boolean isSideBtn = false;
    boolean isOrderBtn = false;
    boolean isCartBtn = false;
    boolean isTableBtn = false;
    boolean isAccBtn = false;
    boolean isRewardBtn = false;
 
    Connection con;
	PreparedStatement pst;
	ResultSet rs;
	String query = "SELECT name FROM user_tbl WHERE id = ?";
	
    //FOOD VARIABLES
	String currentCategory;
	String recordedBtn;
	String foodName;
	String option;
	String qty; // for data transferring
	
	boolean clicked = false;
	boolean added = false;
	boolean selected = false;
	boolean pref1 = false;
	boolean pref2 = false;
	
	int quantity = 0; //for counting
	int initialPrice;
	
	char category;
	private static final int COLUMNS = 5;
	
	//TOP BUTTON
	public void homeBtn(ActionEvent event) throws IOException, SQLException {
		isHomeBtn = true;
		changeScene(event, homePage);
	}
	public void orderBtn(ActionEvent event) throws IOException, SQLException {
		this.setOrders(orderList);
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
	public void showAccount(ActionEvent event) throws IOException, SQLException {
		if(hasAccount) {
			isAccBtn = true;
			changeScene(event, "AccountDetails.fxml");
		}
		else {showAlert("Login or register to edit your information", AlertType.INFORMATION);}
	}
	public void showCart(ActionEvent event) throws IOException, SQLException {
		isCartBtn = true;
		changeScene(event, "MyCart.fxml");
	}
	public void showTable(ActionEvent event) throws IOException, SQLException {
		isTableBtn = true;
		changeScene(event, "TableReservationPage.fxml");
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
		                FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderPanel2.fxml"));
		                VBox orderBox = loader.load();
		                OrderPanel2Controller controller = loader.getController();  
		                Map<String, Integer> optionsMap = DatabaseHelper.searchMenuItemOptions(foodName);
		                
		                controller.setName(foodName);
		                controller.setItemDetails(item);
		                int cheapestPrice = getCheapestOptionPrice(optionsMap);
		                controller.setPrice(String.valueOf(cheapestPrice));
		                
		                
		                if(DatabaseHelper.isItemOutOfStock(foodName)) {
		                	controller.getSelectBtn().setText("OUT OF STOCK");
		                }
		                	 controller.getSelectBtn().setOnAction(event -> {
									try {
										if(!DatabaseHelper.isItemOutOfStock(foodName)) {
												selectMenuItem(item);
										}
										else {
											showAlert("Sorry this item is out of stock right now", AlertType.INFORMATION);
										}
										
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});
           		                
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
	
	private int getCheapestOptionPrice(Map<String, Integer> options) {
	    return options.values().stream().min(Integer::compareTo).orElse(0);
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
	private void loadMenuItems() {
	    try {
	        List<MenuItem> items = DatabaseHelper.getMenuItems();
	        displayOrders(items);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private void selectMenuItem(MenuItem item) throws SQLException {
		
	    selectedItem = item;
	    foodName = selectedItem.getFoodName();
	    foodNameLabel.setText(selectedItem.getFoodName());
	    
	    imgSelect.setImage(DatabaseHelper.getImageFromDatabase(foodName));
	    priceSlidingLabel.setText(String.valueOf(selectedItem.getPrice()));
	    if(!selected) {
	    	slideWindow();
	    	
	    }
	    recordButtonClick(foodName);
	    selectOption(item);
	}
	public void selectOption(MenuItem item) throws SQLException {
	    ToggleGroup toggleGroup = new ToggleGroup();
	    opt1.setToggleGroup(toggleGroup);
	    opt2.setToggleGroup(toggleGroup);

	    // Retrieve options for the specific food item from the database
	    Map<String, Integer> optionsMap = DatabaseHelper.searchMenuItemOptions(item.getFoodName());

	    // Set default option to the cheapest
	    String defaultOption = getDefaultOption(optionsMap);

	    // Set text of radio buttons with options and ensure opt1 is cheaper
	    setRadioButtonOptions(optionsMap, defaultOption);

	    // Add listener to toggle group to handle selection change
	    toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            RadioButton selectedRadioButton = (RadioButton) newValue;
	            String selectedOption = selectedRadioButton.getText();
	            // Get price for the selected option from the options map
	            int price = optionsMap.get(selectedOption);
	            initialPrice = price;
	            // Update UI with the selected option's price
	            priceSlidingLabel.setText(String.valueOf(price));
	            option = selectedOption;
	        }
	    });
	}

	private String getDefaultOption(Map<String, Integer> optionsMap) {
	    return optionsMap.entrySet().stream()
	        .min(Map.Entry.comparingByValue())
	        .map(Map.Entry::getKey)
	        .orElse("");
	}

	private void setRadioButtonOptions(Map<String, Integer> optionsMap, String defaultOption) {
	    if (optionsMap.size() == 1) {
	        // When there's only one option, set it to opt1 and hide opt2
	        String singleOptionKey = optionsMap.keySet().iterator().next();
	        opt1.setText(singleOptionKey);
	        opt1.setVisible(true);
	        opt1.setSelected(true);
	        opt2.setVisible(false);
	        
	        priceSlidingLabel.setText(String.valueOf(optionsMap.get(singleOptionKey)));
	        initialPrice = optionsMap.get(singleOptionKey);
	        option = singleOptionKey;
	    } else {
	        // When there are multiple options, determine which one is cheaper
	        String[] keys = optionsMap.keySet().toArray(new String[0]);
	        String option1Key = keys[0];
	        String option2Key = keys[1];

	        // Ensure opt1 is set to the cheaper option
	        if (optionsMap.get(option2Key) < optionsMap.get(option1Key)) {
	            option1Key = keys[1];
	            option2Key = keys[0];
	        }

	        // Set opt1 with the cheaper option
	        opt1.setText(option1Key);
	        opt1.setVisible(true);
	        opt1.setSelected(true);
	        priceSlidingLabel.setText(String.valueOf(optionsMap.get(option1Key)));
	        initialPrice = optionsMap.get(option1Key);
	        option = option1Key;

	        // Set opt2 with the other option
	        opt2.setText(option2Key);
	        opt2.setVisible(true);
	    }
	}

	//SETTER METHODS
	public void setOrders(List<OrderData> orderList) {
		this.orderList = orderList;
	}
	public void setUserDetails(String role, boolean hasAccount, String dbName, int id) {
	    this.role = role;
	    this.hasAccount = hasAccount;
	    this.dbName = dbName;
	    this.id = id;
	}
	public void setDisplay(boolean toggle) {
		qtyTextField.setVisible(toggle);
		minusBtn.setVisible(toggle);
		plusBtn.setVisible(toggle);
		addToCartBtn.setVisible(toggle);
		viewCartBtn.setVisible(toggle);
	}
	
	//HELPER METHODS
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
	private String recordButtonClick(String clickedButton) {
	       
		String newClickedBtn = clickedButton;
		if(previousClickedBtn.equals("")) {
			quantity++;
			qtyTextField.setText(String.valueOf(quantity));
		}
		else if (previousClickedBtn.equals(newClickedBtn)) {
		  quantity++;
		  qtyTextField.setText(String.valueOf(quantity));
		  
		}
		else {
			quantity = 1;
			qtyTextField.setText(String.valueOf(quantity));
		}
		// Update previousClickedBtn for next comparison
		previousClickedBtn = newClickedBtn;
		qty = String.valueOf(quantity);
		selected = true;
		return foodName = newClickedBtn;
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
			homePage.displayName();
		}
		else if(isOrderBtn) {
			OrderController orderPage = loader.getController();
			orderPage.setOrders(orderList);
			orderPage.setUserDetails(role, hasAccount, dbName, id);
		}else if(isTableBtn) {
			TableReservationController tablePage = loader.getController();
			tablePage.setOrderList(orderList);
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
			rewardPage.displayName();
			rewardPage.showCoins();
		}else {
			CartController cartPage = loader.getController();
			cartPage.setOrders(orderList);
			cartPage.setUserDetails(role, hasAccount, dbName, id);
			cartPage.displayName();
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

       	slider2.setTranslateX(225);
       	accountClose.setOnMouseClicked(event -> {
       		
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);

            slide.setToX(0);
            slide.play();

            slider2.setTranslateX(225);

            slide.setOnFinished((ActionEvent e)-> {
            	
                account.setVisible(false);
                accountClose.setVisible(true);
            });
        });

       account.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);

            slide.setToX(225);
            slide.play();

            slider2.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
            	selected = false;
            	clicked = false;
            	account.setVisible(true);
                accountClose.setVisible(false);
            });
        });
	}
	public void slideWindow() {
		 slider2.setTranslateX(225);
            TranslateTransition slide = new TranslateTransition();
           
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider2);
            
            slide.setToX(0);
            slide.play();
            
            slide.setOnFinished((ActionEvent e)-> {

            });
        
	}
	
	private void funcBtns() {
		addToCartBtn.setOnAction(event ->{
			if(quantity == 0) {
				showAlert("Please select an order first", AlertType.INFORMATION);
			}else {
				OrderData orderData = new OrderData();
				orderData.setQty(String.valueOf(quantity));
				orderData.setFoodName(foodName);
				orderData.setOption(option);
				orderData.setPrice(initialPrice);
				orderList.add(orderData);
				qtyTextField.setText("0");
				quantity = 0;
			}

		});
		
		plusBtn.setOnMouseClicked(event ->{
			quantity++;
			qtyTextField.setText(String.valueOf(quantity));
			selected = true;
		});
		
		minusBtn.setOnMouseClicked(event ->{
			if(quantity <= 0) {qtyTextField.setText("0");} 
			else {
				quantity--;
				qtyTextField.setText(String.valueOf(quantity));
			} 
		});
    	
		viewCartBtn.setOnAction(event ->{try {showCart(event);} catch (IOException | SQLException e) {e.printStackTrace();}});
		
	}
	private void setupMenu() {
	    loadMenuItems();
	    setSlides();
	    setDisplay(true);
	    funcBtns();
	}
	private void setupCategoryButtons() {
	    cat1.setOnAction(event -> loadOrdersByCategory("Rice Meals"));
	    cat2.setOnAction(event -> loadOrdersByCategory("Pasta"));
	    cat3.setOnAction(event -> loadOrdersByCategory("Cakes"));
	    cat4.setOnAction(event -> loadOrdersByCategory("Burger"));
	    cat5.setOnAction(event -> loadOrdersByCategory("Coffee"));
	    cat6.setOnAction(event -> loadOrdersByCategory("Frappe"));
	    cat7.setOnAction(event -> loadOrdersByCategory("Tea"));
	    cat8.setOnAction(event -> loadOrdersByCategory("Appetizers"));
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	    setupMenu();
	    setupCategoryButtons();
	    loadOrdersByCategory("Rice Meals");
	    rearrangeGridPane();
	}
	
}
