package application;


import java.sql.SQLException;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;


public class OrderPanelController {

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

    public void setItemDetails(MenuItem item) throws SQLException {
        foodLabel.setText(item.getFoodName());   

        // Set toggle group for radio buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        opt1.setToggleGroup(toggleGroup);
        opt2.setToggleGroup(toggleGroup);

        // Retrieve options for the specific food item from the database
        Map<String, Integer> optionsMap = DatabaseHelper.searchMenuItemOptions(item.getFoodName());

        // Set text of radio buttons with options and set option 1 as selected
        int optionIndex = 1; // Index to keep track of radio button
        for (String option : optionsMap.keySet()) {
            RadioButton radioButton;
            if (optionIndex == 1) {
                radioButton = opt1;
                radioButton.setSelected(true); // Select option 1
            } else {
                radioButton = opt2;
            }
            radioButton.setText(option);
            radioButton.setVisible(true); // Make the radio button visible
            optionIndex++;
        }

        // If there is only one option, hide the second radio button
        if (optionsMap.size() == 1) {
            opt2.setVisible(false);
        } else {
            opt2.setVisible(true); // Ensure the second radio button is visible
        }

        // Get the price of option 1
        int defaultPrice = optionsMap.get(opt1.getText());

        // Set default price label
        priceLabel.setText(String.valueOf(defaultPrice));

        // Add listener to toggle group to handle selection change
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String selectedOption = selectedRadioButton.getText();
                // Get price for the selected option from the options map
				int price = optionsMap.get(selectedOption);
				priceLabel.setText(String.valueOf(price));
            }
        });
        
        img.setImage(DatabaseHelper.getImageFromDatabase(item.getFoodName()));
        
    }

    


    
    
    public void setName(String name) {
        foodLabel.setText(name);
    }


    public void setPrice(String price) {
        priceLabel.setText(price);
    }
 

}
