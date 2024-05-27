package com;

import java.io.IOException;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.Worker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static final String WORKERS_FOLDER=".";
    static AdminModel adminModel = AdminModel.getAdminModel();


    @SuppressWarnings("exports")
    public static Worker editedWorker = new Worker();
    public static boolean workerProfModeAdd;
    public static boolean isWorkerProfModeAdd() {
        return workerProfModeAdd;
    }

    public static void setWorkerProfModeAdd(boolean workerProfModeAdd) {
        App.workerProfModeAdd = workerProfModeAdd;
    }

    private static Scene scene;

    @SuppressWarnings("exports")
    public static Rank defaultRank = null;
    @SuppressWarnings("exports")
    public static Section defaultSection = null;

    @SuppressWarnings("exports")
    public static Section getDefaultSection() {
        if(defaultSection==null) 
        defaultSection=adminModel.getLastSection();
        return defaultSection;
    }

    public static void setDefaultSection(Section defaulSection) {
        App.defaultSection = defaulSection;
    }

    static void setDefaultRank(Rank rank) {
        defaultRank = rank;
    }

    @SuppressWarnings("exports")
    public static Rank getDefaultRank() {
        if(defaultRank==null)
        defaultRank=adminModel.getLastRank();
        return defaultRank;
    }

    public static Stage st;

    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("workerProfile"), 600, 600);
        st=stage;
        scene = new Scene(loadFXML("login"), 600, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OndPlan Beta MiquelRRdev");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // System.out.println(">".repeat(100)+fxml+ ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showDialog(String st) {
        if (st == null)
            st = "null";
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Operación no posible");
        alert.setHeaderText("revise las siguientes condiciones");
        alert.setContentText(st);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}