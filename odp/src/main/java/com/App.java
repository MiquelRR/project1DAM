package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.model.Rank;
import com.model.Section;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static Rank defaultRank=null;
    public static Section defaulSection=null;

    public static Section getDefaulSection() {
        return defaulSection;
    }
    public static void setDefaulSection(Section defaulSection) {
        App.defaulSection = defaulSection;
    }
    static void setDefaultRank(Rank rank){
        defaultRank=rank;
    }
    public static Rank getDefaultRank() {
        return defaultRank;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("adminMenu"), 600,600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OndPlan Beta MiquelRRdev");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }



    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println(">".repeat(100)+fxml+ ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}