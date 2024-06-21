package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdStockPaneController {
	@FXML 
	private Label foodnameLabel;
	@FXML 
	private Label priceLabel;
	@FXML 
	private Label optionLabel;
	@FXML
	private Label stocksLabel;
	@FXML
	private Label categoryLabel;
	
	 public void setItemDetails(MenuItem item) {
	        foodnameLabel.setText(item.getFoodName());
	        optionLabel.setText(item.getOptions().toString());
	        priceLabel.setText(String.valueOf(item.getPrice()));
	        stocksLabel.setText(String.valueOf(item.getStock()));
	        categoryLabel.setText(item.getCategory());
	    }
	
  
}
