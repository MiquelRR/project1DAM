package com;

import java.io.File;
import java.io.IOException;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.Type;
import com.model.Worker;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static final String WORKERS_FOLDER = ".";
    static AdminModel adminModel = AdminModel.getAdminModel();

    private static BorderStroke borderStroke = new BorderStroke(
            Color.ORANGE, // Color del borde
            BorderStrokeStyle.SOLID, // Estilo del borde (puedes usar DASHED, DOTTED, etc.)
            CornerRadii.EMPTY, // Esquinas redondeadas (puedes definir un radio para las esquinas)
            new BorderWidths(1.5)// Ancho del borde
    );
    protected final static Border ORANGE_BORDER = new Border(borderStroke);
    private static BorderStroke borderStroke2 = new BorderStroke(
            Color.RED, // Color del borde
            BorderStrokeStyle.SOLID, // Estilo del borde (puedes usar DASHED, DOTTED, etc.)
            CornerRadii.EMPTY, // Esquinas redondeadas (puedes definir un radio para las esquinas)
            new BorderWidths(1.5) // Ancho del borde
    );
    protected final static Border RED_BORDER = new Border(borderStroke2);
    private static BorderStroke borderStroke3 = new BorderStroke(
            Color.ORANGE, // Color del borde
            BorderStrokeStyle.SOLID, // Estilo del borde (puedes usar DASHED, DOTTED, etc.)
            CornerRadii.EMPTY, // Esquinas redondeadas (puedes definir un radio para las esquinas)
            new BorderWidths(2.5)// Ancho del borde
    );
    protected final static Border ORANGE_BORDER_B = new Border(borderStroke3);

    @SuppressWarnings("exports")
    public static Worker editedWorker = new Worker();
    public static boolean workerProfModeAdd;
    @SuppressWarnings("exports")
    public static Type editedType = new Type();
    @SuppressWarnings("exports")
    public static Type editedModel = new Type();

    public static boolean isWorkerProfModeAdd() {
        return workerProfModeAdd;
    }

    public static void setWorkerProfModeAdd(boolean workerProfModeAdd) {
        App.workerProfModeAdd = workerProfModeAdd;
    }

    @SuppressWarnings("exports")
    @FXML
    public static String chooseFolder(String base, Stage sta) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona una carpeta");
        directoryChooser.setInitialDirectory(new File(base));
        File selectedDirectory = directoryChooser.showDialog(sta);
        return (selectedDirectory == null) ? null : selectedDirectory.getPath();
    }

    @FXML
    public static String chooseFile(String base, Stage sta) {
        FileChooser fileChooser = new FileChooser();
        File fold = new File(base);
        fileChooser.setInitialDirectory(fold);
        fileChooser.setTitle("Selecciona un archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(sta);
        return (selectedFile.getPath() == null) ? null : selectedFile.getPath();
    }

    private static Scene scene;

    @SuppressWarnings("exports")
    public static Rank defaultRank = null;
    @SuppressWarnings("exports")
    public static Section defaultSection = null;

    @SuppressWarnings("exports")
    public static Section getDefaultSection() {
        if (defaultSection == null)
            defaultSection = adminModel.getLastSection();
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
        if (defaultRank == null)
            defaultRank = adminModel.getLastRank();
        return defaultRank;
    }

    public static Stage st;

    @Override
    public void start(Stage stage) throws IOException {
        // scene = new Scene(loadFXML("workerProfile"), 600, 600);
        st = stage;
        scene = new Scene(loadFXML("login"), 600, 600);
        // scene = new Scene(loadFXML("typeEdit"), 1200, 600);
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

    public static void showDialog(String showedText) {
        if (showedText == null)
            showedText = "null";
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Operación no posible");
        alert.setHeaderText("revise las siguientes condiciones");
        alert.setContentText(showedText);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}