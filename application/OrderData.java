package application;

public class OrderData {
    String foodName;
    String qty;
    String option;
    int price;
    

    public String getOption() {
    	return option;
    }
    public void setOption(String option) {
    	this.option = option;
    }
    public int getFinalPrice() {
        return price;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getQty() {
        return qty;
    }
}
	
		

