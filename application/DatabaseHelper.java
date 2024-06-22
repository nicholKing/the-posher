package application;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

import com.mysql.jdbc.Connection;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
	@SuppressWarnings("exports")
	public static Connection connect() throws SQLException {
        return (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static Map<String, Integer> searchMenuItemOptions(String name) throws SQLException {
        Map<String, Integer> optionsMap = new HashMap<>();
        String query = "SELECT options, price FROM menu_items WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Retrieve options and price from the ResultSet
                    String options = rs.getString("options");
                    int price = rs.getInt("price");
                    // Add each option and its price to the map
                    optionsMap.put(options, price);
                }
            }
        }
        return optionsMap;
        
    }
    
    public static List<MenuItem> filterMenuItemsByCategory(String category) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT * FROM menu_items WHERE category = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getInt("price"),
                        rs.getString("options"),
                        rs.getString("category")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }
    
    public static boolean isItemOutOfStock(String itemName) throws SQLException {
        boolean outOfStock = true; // Assume out of stock until proven otherwise
        String query = "SELECT stock FROM menu_items WHERE name = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    outOfStock = stock <= 0; // If stock is less than or equal to 0, item is out of stock
                }
            }
        }
        
        return outOfStock;
    }
    
    public static void decreaseStock(String itemName, String option, int quantity) throws SQLException {
        String query = "UPDATE menu_items SET stock = stock - ? WHERE name = ? AND options = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, itemName);
            stmt.setString(3, option);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                // Handle the case where the item or option is not found
                System.out.println("Item or option not found: " + itemName + " - " + option);
            }
        }
    }
    public static int getAvailableStock(String itemName, String option) throws SQLException {
        String query = "SELECT stock FROM menu_items WHERE name = ? AND options = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setString(2, option);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                } else {
                    throw new SQLException("Item or option not found: " + itemName + " - " + option);
                }
            }
        }
    }

    public static int getOptionCountForFoodName(String foodName, String category) throws SQLException {
        int optionCount = 0;
        String query = "SELECT COUNT(*) AS optionCount FROM menu_items WHERE name = ? AND category = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, foodName);
            pstmt.setString(2, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    optionCount = rs.getInt("optionCount");
                }
            }
        }
        return optionCount;
    }
    
    public static void insertReservation(String email, String dateTimeString, String name, int table_number, boolean reserved) {
        String query = "INSERT INTO appointments (email, appointment_date_time, name, table_number, reserved) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, dateTimeString);
            pstmt.setString(3, name);
            pstmt.setInt(4, table_number);
            pstmt.setBoolean(5, reserved);
            pstmt.executeUpdate();

            System.out.println("Date, Time, Email, and Name saved to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Reservation> fetchAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT email, appointment_date_time, name, table_number, reserved FROM appointments";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("email");
                String dateTimeString = rs.getString("appointment_date_time");
                String name = rs.getString("name");
                int table_number = rs.getInt("table_number");
                boolean reserved = rs.getBoolean("reserved");


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                String date = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

                Reservation reservation = new Reservation(email, name, date, time, table_number, reserved);
                reservations.add(reservation);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
   
    public static List<Reservation> fetchExistingReservations(LocalDate ldate) {
        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT email, appointment_date_time, name, table_number, reserved FROM appointments";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	String email = rs.getString("email");
                String dateTimeString = rs.getString("appointment_date_time");
                String name = rs.getString("name");
                int table_number = rs.getInt("table_number");
                boolean reserved = rs.getBoolean("reserved");
                
                System.out.println("dateTimeString");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                String date = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

                Reservation reservation = new Reservation(email, name, date, time, table_number, reserved);
                reservations.add(reservation);

                // Debug print statement
                System.out.println("Fetched: " + reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }
    
    public static void updateReservation(String email, String name, String date, String time, int table_number, boolean reserved) {
        
    	String query = "UPDATE `appointments` SET `email`= ?,`appointment_date_time`= ?,`name`= ?,`reserved`= ? WHERE `table_number`  = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
        	
            String dateTimeString = date + " " + time;
            pstmt.setString(1, email);
            pstmt.setString(2, dateTimeString);
            pstmt.setString(3, name);
            pstmt.setBoolean(4, reserved);
            pstmt.setInt(5, table_number);
            
            System.out.println(email + "\n" + name + "\n" + dateTimeString + "\n" + table_number + "\n" + reserved);
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteReservation(String email) {
        String sql = "DELETE FROM appointments WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.executeUpdate();
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean checkExistingFoodName(String foodName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM menu_items WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, foodName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0; // If count > 0, the food name already exists
                }
            }
        }
        return false; // Default to false if an error occurs or no rows are returned
    }

    public static List<MenuItem> getMenuItems() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT * FROM menu_items";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getInt("price"),
                        rs.getString("options"),
                        rs.getString("category")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }
    
    public static List<MenuItem> getMenuItemsByCategory(String category) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT * FROM menu_items WHERE category = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Fetch options for this menu item
                    MenuItem item = new MenuItem(
                        rs.getInt("id"),      // Fetch the id
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getInt("price"),
                        rs.getString("options"), // Assign fetched options to the menu item
                        rs.getString("category")
                    );
                    
                    System.out.println("Fetched MenuItem from DB: " + item);
                    
                    items.add(item);
                }
            }
        }
        return items;
    }
    
    public static boolean isItemExists(String foodName, String option) throws SQLException {
        String query = "SELECT COUNT(*) FROM menu_items WHERE name = ? AND options = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, foodName);
            stmt.setString(2, option);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count > 0, item already exists
            }
            return false;
        }
    }
    
    public static void addMenuItem(MenuItem item) throws SQLException {
        String query = "INSERT INTO menu_items (name, price, stock, options, category, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getFoodName());
            stmt.setInt(2, item.getPrice());
            stmt.setInt(3, item.getStock());
            stmt.setString(4, item.getOptions());
            stmt.setString(5, item.getCategory());
            stmt.setBytes(6, item.getImageBytes());
            stmt.executeUpdate();
        }
    }

    public static void updateMenuItem(MenuItem item) throws SQLException {
        String query = "UPDATE menu_items SET name = ?, stock = ?, price = ?, options = ?, category = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getFoodName());
            stmt.setInt(2, item.getStock());
            stmt.setInt(3, item.getPrice());
            stmt.setString(4, item.getOptions());
            stmt.setString(5, item.getCategory());
            stmt.setInt(6, item.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteMenuItem(int id) throws SQLException {
        String query = "DELETE FROM menu_items WHERE id = ?";
        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                success = true;
            } catch (SQLException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw e;
                }
             
            }
        }
    }
    
    public static void updateMenuItemImage(String foodName, byte[] imageBytes) {
        String sql = "UPDATE menu_items SET image = ? WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, imageBytes);
            pstmt.setString(2, foodName);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Image getImageFromDatabase(String name) {
   
        String sql = "SELECT image FROM menu_items WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("image");
                if (imageData != null && imageData.length > 0) {
                    return new Image(new ByteArrayInputStream(imageData));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Return default image if no image is found or if there's an error accessing the database
        return getDefaultImage();
    }
    
    public static void deleteImageFromDatabase(String name) {
      
        String sql = "DELETE FROM menu_items WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    public static Image getDefaultImage() {
        try {
            return new Image(new FileInputStream("src\\pictures\\fast-food.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null; // Return null if default image cannot be loaded
        }
    }
    
    //EMPLOYMENT PAGE
    public static boolean insertEmployee(Employee employee) {
        String checkQuery = "SELECT COUNT(*) FROM employee_list WHERE id = ?";
        String insertQuery = "INSERT INTO employee_list (id, name, email, age, gender, salary, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Check if the ID already exists
            checkStmt.setInt(1, employee.getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
            	showAlert("ID already exists.", AlertType.ERROR);
                return false; // Indicate failure
            }
            else {
            	insertStmt.setInt(1, employee.getId());
                insertStmt.setString(2, employee.getName());
                insertStmt.setString(3, employee.getEmail());
                insertStmt.setInt(4, employee.getAge());
                insertStmt.setString(5, employee.getGender());
                insertStmt.setDouble(6, employee.getSalary());
                insertStmt.setString(7, employee.getRole());

                insertStmt.executeUpdate();
                return true; // Indicate success
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Indicate failure
        }
    }

    public static ObservableList<Employee> getAllEmployees() throws SQLException {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        // Assuming you have a connection object named "connection"
        String query = "SELECT * FROM employee_list"; // Modify this query according to your database schema
        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");
                double salary = resultSet.getDouble("salary");
                String role = resultSet.getString("role");
                String gender = resultSet.getString("gender");
                employees.add(new Employee(id, name, email, age, salary, role, gender));
            }
        }
        return employees;
    }
    public static void updateEmployee(Employee employee) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = connect(); // Get connection to the database
            
            String query = "UPDATE employee_list SET name=?, email=?, age=?, salary=?, role=?, gender=? WHERE id=?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setInt(3, employee.getAge());
            stmt.setDouble(4, employee.getSalary());
            stmt.setString(5, employee.getRole());
            stmt.setString(6, employee.getGender());
            stmt.setInt(7, employee.getId());
            
            stmt.executeUpdate();
        } finally {
            // Close resources
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    public static void deleteEmployee(Employee employee) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = connect();// Get connection to the database
            
            String query = "DELETE FROM employee_list WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, employee.getId());
            
            stmt.executeUpdate();
        } finally {
            // Close resources
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    public static void deleteAllEmployees() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connect();// Get connection to the database

            String query = "DELETE FROM employee_list";
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } finally {
            // Close resources
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    public static boolean verifyUser(String inputUsername, String inputPassword) {
        try (Connection connection = connect()) {
            // Check if username and password match in the user table
            String query = "SELECT * FROM user_tbl WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, inputUsername);
                statement.setString(2, inputPassword);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        // Check if the username corresponds to an employee name
                        if (isEmployee(name)) {
                            return true; // Username is both a valid user and an employee
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log any database errors
        }
        return false;
    }
    private static boolean isEmployee(String name) throws SQLException {
        try (Connection connection = connect()) {
            String query = "SELECT * FROM employee_list WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Return true if the username exists in the employee table
                }
            }
        }
    }
    
    public static void storeTotalPrice(int id, int totalPrice) throws SQLException {
        String updateQuery = "UPDATE user_tbl SET total_spent = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, totalPrice);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public static int calculateRewardCoins(int totalPrice) {
        int rewardCoins = totalPrice / 1000;
        return rewardCoins;
    }
    public static void storeRewardCoins(int id, int rewardCoins) throws SQLException {
        String updateQuery = "UPDATE user_tbl SET reward_coins = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
        	System.out.println(rewardCoins + " test");
            pstmt.setInt(1, rewardCoins);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public static int showRewardCoins(int id) throws SQLException {
        String selectQuery = "SELECT reward_coins FROM user_tbl WHERE id = ?";
        System.out.println(id);
        int coins = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                coins = rs.getInt("reward_coins");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return coins;
    }
    public static void decreaseRewardCoins(int id, int coins) throws SQLException {
        String sql = "UPDATE user_tbl SET reward_coins = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coins); // Set the amount to decrease (coins)
            pstmt.setInt(2, id);     // Set the user ID
            pstmt.executeUpdate();  // Execute the update
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception for handling at a higher level
        }
    }
    private static void showAlert(String contentText, AlertType alertType) {

        Alert alert = new Alert(alertType);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
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
}

    
    


