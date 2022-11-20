package com.olexandryudin.musicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    public static double xPos=0;
    public static double yPos=0;
    public static Stage primaryStage=null;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        root.setOnMousePressed(event -> {
            xPos = event.getSceneX();
            yPos = event.getSceneY();
        });

        primaryStage.setTitle("Music Player");
        Scene scene=new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xPos);
            primaryStage.setY(event.getScreenY() - yPos);
        });
        primaryStage.setScene(scene);
        Main.primaryStage =primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
