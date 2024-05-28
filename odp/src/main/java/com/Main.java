package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear el layout
        Pane root = new Pane();

        // Crear un botón para añadir un ToggleButton
        Button addButton = new Button("Añadir ToggleButton");
        addButton.setLayoutX(150);
        addButton.setLayoutY(50);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Coordenadas variables para el nuevo ToggleButton
                double x = 100; // Coordenada X
                double y = 100; // Coordenada Y

                // Crear el nuevo ToggleButton
                ToggleButton toggleButton = new ToggleButton("A0");
                toggleButton.setLayoutX(x);
                toggleButton.setLayoutY(y);
                toggleButton.setPrefWidth(135);
                toggleButton.setPrefHeight(52);
                toggleButton.setId("A0");
                toggleButton.setStyle("-fx-text-fill: #7c7c7c;");
                toggleButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        checkSelectedTask();
                    }
                });

                // Establecer el orden de visualización si es necesario
                toggleButton.setViewOrder(1);

                // Añadir el ToggleButton al layout
                root.getChildren().add(toggleButton);
            }
        });

        // Añadir el botón al layout
        root.getChildren().add(addButton);

        // Configurar la escena y el stage
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Ejemplo de ToggleButton");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Definir la función checkSelectedTask()
    private void checkSelectedTask() {
        System.out.println("ToggleButton pulsado");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
