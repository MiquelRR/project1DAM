package com.model;

import java.io.File;
import java.net.URI;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PdfViewerApp extends Application {
    static URI uri;
    static File selectedFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        String initFoldePath=".";
        Button selectFolderButton = new Button("Seleccionar carpeta inicial");
        Button selectFileButton = new Button("Seleccionar archivo");
        Button viewFileButton = new Button("Ver archivo");

        selectFolderButton.setOnAction(e -> { // FOLDER SELECT
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecciona una carpeta");
            directoryChooser.setInitialDirectory(new File("."));
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
        });

        selectFileButton.setOnAction(e -> { // FILE SELECT
            FileChooser fileChooser = new FileChooser();
            File fold = new File(initFoldePath);
            fileChooser.setInitialDirectory(fold);
            fileChooser.setTitle("Selecciona un archivo PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (uri != null) {
                System.out.println("Archivo PDF seleccionado: " + uri);
            }
        });

        viewFileButton.setOnAction(e -> { // FILE VIEW

            HostServices hostServices = getHostServices();
            hostServices.showDocument(selectedFile.getAbsolutePath());
            // FileMan.showPdf(uri);
        });

        VBox root = new VBox(10, selectFolderButton, selectFileButton, viewFileButton);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Visor de PDF");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}