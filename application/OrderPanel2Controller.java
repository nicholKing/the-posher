package application;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;


public class OrderPanel2Controller {

    @FXML
    private Label foodLabel;
    @FXML
    private RadioButton opt1;
    @FXML
    private RadioButton opt2;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView img;
    @FXML
    private Button selectBtn;
    ToggleGroup toggleGroup;
    String foodName;
    String size;
    int price;
    public MenuItem selectedMenuItem;
    public void setItemDetails(MenuItem item) throws SQLException {
        foodLabel.setText(item.getFoodName());
        priceLabel.setText(String.valueOf(item.getPrice()));
        img.setImage(DatabaseHelper.getImageFromDatabase(item.getFoodName()));
    }
   
    public Button getSelectBtn() {
    	return selectBtn;
    }
    public void setName(String foodName) {
    	this.foodName = foodName;
    }
    public void setPrice(String price) {
        priceLabel.setText(price);
    }
    public int getPrice() {
    	return price;
    }
    
    

}
