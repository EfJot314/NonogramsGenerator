package com.nonograms.nonogramsgenerator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DrawingWindow implements Runnable {



    int width, height;
    Stage drawStage;

    List<Button> tileList = new ArrayList<>();

    boolean[][] boardTab;

    public DrawingWindow(int width, int height){
        this.width = width;
        this.height = height;

        this.boardTab = new boolean[this.width][this.height];

        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);

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
                int x = j;
                int y = i;
                tileButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        markTile(x,y);
                    }
                });
                tileList.add(tileButton);
                row.getChildren().add(tileButton);
            }
            board.getChildren().add(row);
        }

        mainBox.getChildren().add(board);

        Button generateButton = new Button("Generate Nonogram!");
        generateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                generateNonogram();
            }
        });

        mainBox.getChildren().add(generateButton);

        Scene scene = new Scene(mainBox, 700, 700);

        drawStage = new Stage();
        drawStage.setScene(scene);
        drawStage.show();
    }

    private void markTile(int x, int y){
        int ind = x+y*this.height;
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
        Nonogram non = new Nonogram(width, height);
        non.createNonogramFromTable(boardTab);
    }

    @Override
    public void run() {

    }
}
