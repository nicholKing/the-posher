module SampleJavaFX {
	requires javafx.controls;
	
    requires javafx.fxml;
    requires transitive javafx.graphics;
	opens application to javafx.graphics, javafx.fxml;
	requires javafx.base;
	
	requires mysql.connector.java;
	requires java.sql;
	opens pictures;
	exports application;
	
}
