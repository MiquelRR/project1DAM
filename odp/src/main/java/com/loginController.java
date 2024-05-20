package com;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.model.Accesdb;
import com.model.RolAndId;
import com.model.Worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField user;

    @FXML
    private Label user_label;

    @FXML
    private Label passwd_label;

    @FXML
    private PasswordField passwd;

    @FXML
    private void hideUserLabel() {
        check();
        user_label.setText(null);
    }

    @FXML
    private void hidePwLabel() {
        check();
        passwd_label.setText(null);
    }

    @FXML
    void startSession(ActionEvent event) throws IOException {
        RolAndId rolAndId = Accesdb.trustWorker(user.getText(), passwd.getText());
        if (rolAndId.getRol().equals("ADMIN")) {
            App.setRoot("adminMenu");
            System.out.println("hola");
        } else {
            Alert alert = new Alert(AlertType.ERROR); // WARNING, ERROR
            alert.setTitle("ERROR DE ACCESO");
            alert.setHeaderText("Usuario o clave incorrecta"); // ó null si no queremos cabecera
            alert.setContentText("Error");
            alert.showAndWait();
        }
        
    }

    @FXML
    void check() {
        if (user.getText() == null || user.getText().equals(""))
            user_label.setText("Usuario");
        else
            user_label.setText(null);
        if (passwd.getText() == null || passwd.getText().equals(""))
            passwd_label.setText("Clave de acceso");
        else
            passwd_label.setText(null);

    }

    @FXML
    void initialize() {
        user_label.setText("Usuario");
        passwd_label.setText("Clave de acceso");

    }

}