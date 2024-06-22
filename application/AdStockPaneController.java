package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
