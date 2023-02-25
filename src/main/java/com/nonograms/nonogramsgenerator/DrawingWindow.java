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
                Vector2d position = new Vector2d(j,i);
                tileButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        markTile(position);
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

    private void markTile(Vector2d position){
        int x = position.x;;
        int y = position.y;
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
        //na razie w konsoli

        //przechodze po osi pionowej (rzedach)
        System.out.println("os pionowa:");
        for(int i=0;i<height;i++){
            //dla kazdego rzedu zliczam zaznaczone pola
            int act = 0;
            for(int j=0;j<width;j++){
                //jesli cos tam jest to zwiekszam licznik
                if(boardTab[j][i]){
                    act += 1;
                    //jesli jestem na ostatnim polu to to wypisuje
                    if(j == width-1){
                        System.out.print(act);
                    }
                }
                //jesli nie, to jesli cos bylo to wypisuje a jak nie to tylko zeruje licznik
                else{
                    if(act > 0){
                        System.out.print(act);
                        System.out.print(" ");
                    }
                    act = 0;
                }
            }
            System.out.println();
        }

        //przechodze po osi poziomej (kolumnach)
        System.out.println("os pozioma:");
        for(int j=0;j<width;j++){
            //dla kazdej kolumny zliczam zaznaczone pola
            int act = 0;
            for(int i=0;i<height;i++){
                //jesli cos tam jest to zwiekszam licznik
                if(boardTab[j][i]){
                    act += 1;
                    //jesli jestem na ostatnim polu to to wypisuje
                    if(i == height-1){
                        System.out.print(act);
                    }
                }
                //jesli nie, to jesli cos bylo to wypisuje a jak nie to tylko zeruje licznik
                else{
                    if(act > 0){
                        System.out.print(act);
                        System.out.print(" ");
                    }
                    act = 0;
                }
            }
            System.out.println();
        }
    }

    @Override
    public void run() {

    }
}
