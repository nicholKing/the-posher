package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderSummaryDialogController {

    @FXML
    private VBox contentBox;
    @FXML
    private Label totalPriceLabel;
    private String totalPrice;
    
    private Stage dialogStage;
    private List<OrderData> cartItems;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCartItems(List<OrderData> cartItems) {
        this.cartItems = cartItems;
       
        displayCartItems();
    }

    private void displayCartItems() {
    	 totalPriceLabel.setText(totalPrice);
        for (OrderData cartItem : cartItems) {
            Label itemLabel = new Label(cartItem.getFoodName() + " (" + cartItem.getOption() + ") - Quantity: " + cartItem.getQty());
            
            contentBox.getChildren().add(itemLabel);
            
        }
       
    }
    public void setPrice(String totalPrice) {
    	this.totalPrice = totalPrice;
    	
    }
    @FXML
    private void handleOk() {
        dialogStage.close();
    }
   
}
