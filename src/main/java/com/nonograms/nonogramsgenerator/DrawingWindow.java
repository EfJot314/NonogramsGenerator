package com.nonograms.nonogramsgenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DrawingWindow implements Runnable {

    int width, height;
    Stage drawStage;
    VBox mainBox;
    Label saveLabel;
    Label uniqLabel;

    List<Button> tileList = new ArrayList<>();

    boolean[][] boardTab;

    Thread saveThread;
    Thread uniqThread;



    public DrawingWindow(int width, int height){
        this.width = width;
        this.height = height;

        this.boardTab = new boolean[this.width][this.height];

        this.mainBox = new VBox();
        this.mainBox.setAlignment(Pos.CENTER);

        this.saveLabel = new Label("Nonogram jest niezapisany!");
        this.mainBox.getChildren().add(this.saveLabel);

        this.uniqLabel = new Label("Nie sprawdzono unikalności nonogramu!");
        this.mainBox.getChildren().add(this.uniqLabel);

        VBox board = new VBox();
        board.setAlignment(Pos.CENTER);
        for(int i=0;i<height;i++){
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);
            for(int j=0;j<width;j++){
                Button tileButton = new Button();
                tileButton.setMinSize(30,30);
                tileButton.setMaxSize(30,30);
                tileButton.setStyle("-fx-background-radius: 0; -fx-background-color: White; -fx-border-color: Black");
                final int xi = j;
                final int yi = i;
                tileButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        markTile(xi,yi);
                    }
                });
                tileList.add(tileButton);
                row.getChildren().add(tileButton);
            }
            board.getChildren().add(row);
        }
        this.mainBox.getChildren().add(board);


        Button checkUniqButton = new Button("Check uniqueness");
        checkUniqButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                checkNonogramUniq();
            }
        });
        this.mainBox.getChildren().add(checkUniqButton);

        Button generateButton = new Button("Save Nonogram!");
        generateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                saveNonogram();
            }
        });

        this.mainBox.getChildren().add(generateButton);

        Scene scene = new Scene(mainBox, 1300, 900);

        drawStage = new Stage();
        drawStage.setScene(scene);
        drawStage.show();
    }

    private void markTile(int x, int y){
        int ind = x+y*this.width;
        Button markedTile = tileList.get(ind);
        //jezeli juz jest zaznaczony
        if(boardTab[x][y]){
            markedTile.setStyle("-fx-background-radius: 0; -fx-background-color: White; -fx-border-color: Black");
            boardTab[x][y] = false;
        }
        //jezeli nie jest zaznaczony
        else{
            markedTile.setStyle("-fx-background-radius: 0; -fx-background-color: Black; -fx-border-color: Black");
            boardTab[x][y] = true;
        }
    }

    public void saveNonogram(){

        Nonogram non = new Nonogram(this, width, height, boardTab, "saving");

        killNonogram(this.saveThread);
        this.saveThread = new Thread(non);
        this.saveThread.start();

    }

    public void checkNonogramUniq(){
        Nonogram non = new Nonogram(this, width, height, boardTab, "checking");

        killNonogram(this.uniqThread);
        this.uniqThread = new Thread(non);
        this.uniqThread.start();

    }

    public void killNonogram(Thread nonogramThread){
        if(nonogramThread != null){
            nonogramThread.interrupt();
        }
    }

    public void showLabel(String text, int id){
        if(id == 0){
            this.saveLabel.setText(text);
        }
        else if(id == 1){
            this.uniqLabel.setText(text);
        }
    }

    @Override
    public void run() {

    }
}
