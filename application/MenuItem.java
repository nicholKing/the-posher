package application;


public class MenuItem {
    private int id;
    private String foodName;
    private int price;
    private int stock;
    private String options;
    private String category;
    private byte[] imageBytes;

    public MenuItem(int id, String foodName, int stock, int price, String options, String category) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.stock = stock;
        this.options = options;
        this.category = category;
    }
    public MenuItem(int id, String foodName, int stock, int price, String options, String category, byte[] imageBytes) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.stock = stock;
        this.options = options;
        this.category = category;
        this.imageBytes = imageBytes;
    }
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setimageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setSelectedItem(MenuItem selectedItem) {
        this.foodName = selectedItem.foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
}


