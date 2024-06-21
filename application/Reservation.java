package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private final SimpleStringProperty email;
    private final SimpleStringProperty name;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleIntegerProperty table_number;
    private final BooleanProperty reserved;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER_12HR = DateTimeFormatter.ofPattern("h:mm a");

    public Reservation(String email, String name, String date, String time, int table_number, boolean reserved) {
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.table_number = new SimpleIntegerProperty(table_number);
        this.reserved = new SimpleBooleanProperty(reserved);
    }

    public int getTableNumber() {
        return table_number.get();
    }

    public IntegerProperty TableNumberProperty() {
        return table_number;
    }

    public void setTableNumber(int table_number) {
        this.table_number.set(table_number);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
    
    public boolean isReserved() {
        return reserved.get();
    }

    public BooleanProperty reservedProperty() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved.set(reserved);
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "table_number=" + table_number.get() +
                ", email='" + email.get() + '\'' +
                ", name='" + name.get() + '\'' +
                ", date='" + date.get().formatted(DATE_FORMATTER) +
                ", time=" + time.get().formatted(TIME_FORMATTER_12HR) +
                ", reserved=" + reserved.get() +
                '}';
    }
}