package com.nonograms.nonogramsgenerator;

import javafx.application.Application;
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
    Label noticeLabel;
    CheckBox checkUniq;

    List<Button> tileList = new ArrayList<>();

    boolean[][] boardTab;

    Thread nonogramThread;


    public DrawingWindow(int width, int height){
        this.width = width;
        this.height = height;

        this.boardTab = new boolean[this.width][this.height];

        this.mainBox = new VBox();
        this.mainBox.setAlignment(Pos.CENTER);

        this.noticeLabel = new Label("");
        this.mainBox.getChildren().add(this.noticeLabel);

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

        this.checkUniq = new CheckBox("Check uniqueness");
        this.mainBox.getChildren().add(this.checkUniq);

        Button generateButton = new Button("Generate Nonogram!");
        generateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                generateNonogram();
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

    public void generateNonogram(){
        showLabel("");

        Nonogram non = new Nonogram(this, width, height, boardTab, this.checkUniq.isSelected());

        this.killNonogram();
        this.nonogramThread = new Thread(non);
        this.nonogramThread.start();


    }

    public void killNonogram(){
        if(this.nonogramThread != null){
            this.nonogramThread.interrupt();
        }
    }

    public void showLabel(String text){
        this.noticeLabel.setText(text);
    }

    @Override
    public void run() {

    }
}
