package com.nonograms.nonogramsgenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Nonogram {

    int width, height;

    List<Integer>[] rowList, colList;

    public Nonogram(int width, int height){
        this.width = width;
        this.height = height;

        this.rowList = new ArrayList[this.height];
        this.colList = new ArrayList[this.width];

    }

    public void createNonogramFromTable(boolean[][] tab){
        //przechodze po kolumnach
        for(int i=0;i<width;i++){
            this.colList[i] = new ArrayList<>();
            int act = 0;
            for(int j=0;j<height;j++){
                //jezeli pole jes zaznaczone to zwiekszam licznik
                if(tab[i][j]){
                    act += 1;
                    //jelsi jest to ostatnie pole to zapisuje licznik do listy
                    if(j == height-1){
                        this.colList[i].add(act);
                    }
                }
                //jesli w polu nic nie ma i licznik>0 to zapisuje licznik a potem go zeruje
                else{
                    if(act > 0){
                        this.colList[i].add(act);
                    }
                    act = 0;
                }
            }
        }

        //przechodze po wierszach
        for(int j=0;j<height;j++){
            this.rowList[j] = new ArrayList<>();
            int act = 0;
            for(int i=0;i<width;i++){
                //jezeli pole jest zaznaczone to zwiekszam licznik
                if(tab[i][j]){
                    act += 1;
                    //jelsi jest to ostatnie pole to zapisuje licznik do listy
                    if(i == width-1){
                        this.rowList[j].add(act);
                    }
                }
                //jesli w polu nic nie ma i licznik>0 to zapisuje licznik a potem go zeruje
                else{
                    if(act > 0){
                        this.rowList[j].add(act);
                    }
                    act = 0;
                }
            }
        }
        System.out.println(rowList[3]);
        System.out.println(colList[2]);
    }


    public void save(){
        int imgWidth = 1000;
        int imgHeight = 1000;
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        try {
            if (ImageIO.write(img, "png", new File("./nonogram_image.png")))
            {
                System.out.println("Nonogram saved!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
