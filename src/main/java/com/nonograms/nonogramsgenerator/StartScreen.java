package com.nonograms.nonogramsgenerator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreen extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartScreen.class.getResource("startView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Nonograms generator");
        stage.setScene(scene);
        stage.show();

        Button startButton = (Button)scene.lookup("#startButton");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                startNewGeneration();
            }
        });



    }

    public void startNewGeneration(){
        DrawingWindow simWin = new DrawingWindow(10,10);
        Thread newWidowThread = new Thread(simWin);
        newWidowThread.start();
    }



    public static void main(String[] args) {
        launch();
    }



}

