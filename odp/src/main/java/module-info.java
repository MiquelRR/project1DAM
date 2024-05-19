module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics; 

    opens com to javafx.fxml;
    opens com.model;
    exports com;
}
