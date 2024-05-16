module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com to javafx.fxml;
    opens com.model;
    exports com;
}
