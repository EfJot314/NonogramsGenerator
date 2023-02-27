package com.nonograms.nonogramsgenerator;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Nonogram implements Runnable{

    DrawingWindow window;

    int width, height;

    final boolean[][] T;

    List<Integer>[] rowList, colList;

    public Nonogram(DrawingWindow father, int width, int height, boolean[][] tab){

        this.window = father;

        this.width = width;
        this.height = height;

        this.T = tab;

    }

    private List<Integer>[] createRowList(boolean[][] tab){
        List<Integer>[] rL = new ArrayList[this.height];
        //przechodze po wierszach
        for(int j=0;j<height;j++){
            rL[j] = new ArrayList<>();
            int act = 0;
            for(int i=0;i<width;i++){
                //jezeli pole jest zaznaczone to zwiekszam licznik
                if(tab[i][j]){
                    act += 1;
                    //jelsi jest to ostatnie pole to zapisuje licznik do listy
                    if(i == width-1){
                        rL[j].add(act);
                    }
                }
                //jesli w polu nic nie ma i licznik>0 to zapisuje licznik a potem go zeruje
                else{
                    if(act > 0){
                        rL[j].add(act);
                    }
                    act = 0;
                }
            }
        }
        return rL;
    }

    private List<Integer>[] createColList(boolean[][] tab){
        List<Integer>[] cL = new ArrayList[this.width];
        //przechodze po kolumnach
        for(int i=0;i<width;i++){
            cL[i] = new ArrayList<>();
            int act = 0;
            for(int j=0;j<height;j++){
                //jezeli pole jest zaznaczone to zwiekszam licznik
                if(tab[i][j]){
                    act += 1;
                    //jelsi jest to ostatnie pole to zapisuje licznik do listy
                    if(j == height-1){
                        cL[i].add(act);
                    }
                }
                //jesli w polu nic nie ma i licznik>0 to zapisuje licznik a potem go zeruje
                else{
                    if(act > 0){
                        cL[i].add(act);
                    }
                    act = 0;
                }
            }
        }
        return cL;
    }

    private boolean areListsEqual(List<Integer> a, List<Integer> b){
        if(a.size() != b.size()){
            return false;
        }
        for(int i=0;i<a.size();i++){
            if(a.get(i) != b.get(i)){
                return false;
            }
        }
        return true;
    }



    private void createNonogramFromTable(){

        this.rowList = this.createRowList(this.T);
        this.colList = this.createColList(this.T);

        if(isUnique()){
            //zapis
            boolean success = save();

            if(success){
                Platform.runLater(() -> {
                    this.window.showLabel("Pomyslnie zapisano!");
                    this.window.killNonogram();
                });
            }

        }
        else{
            Platform.runLater(() -> {
                this.window.showLabel("Nonogram nie jest jednoznaczny!");
                this.window.killNonogram();
            });
        }
    }

    private boolean[][] copyTab(boolean[][] tab){
        boolean[][] tap = new boolean[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                tap[i][j] = tab[i][j];
            }
        }
        return tap;
    }

    private int reku(boolean[][] tab, int x, int y){
        String message = "Sprawdzanie jednoznaczności";
        int nOfDots = (x+y)%7+1;
        for(int i=0;i<nOfDots;i++){
            message += ".";
        }
        String finalMessage = message;
        Platform.runLater(() -> {
            this.window.showLabel(finalMessage);
        });

        //jezeli doszedlem na koniec rzedu
        if(x == width){
            //sprawdzam czy nowo powstaly rzad jest w porzadku
            if(!areListsEqual(createRowList(tab)[y], this.rowList[y])){
                //jesli nie to od razu odrzucam ten model
                return 0;
            }
            //zeruje x (czyli przechodze znow na lewa strone) i zwiekszajac y przechodze do kolejnego rzedu
            x = 0;
            y += 1;
            //jesli dotarlem do konca obrazka
            if(y == height){
                //sprawdzam czy kolumny sie zgadzaja, jesli tak to zwracam 1 znaleziona kombinacje, a przy choc jednej blednej kolumnie odrzucam te kombinacje
                List<Integer>[] cL = createColList(tab);
                for(int i=0;i<height;i++){
                    if(!areListsEqual(cL[i], this.colList[i])){
                        return 0;
                    }
                }
                return 1;
            }
        }
        //jesli jestem gdzies w srodku rzedu to ide dalej wywolujac sie rekurencyjnie dla dwoch mozliwych polozen
        int toReturn = 0;
        toReturn += reku(copyTab(tab), x+1, y);
        tab[x][y] = true;
        toReturn += reku(copyTab(tab), x+1, y);
        return toReturn;
    }

    public boolean isUnique(){
        boolean[][] tab = new boolean[this.width][this.height];

        int nOfOptions = reku(tab,0,0);

        return (nOfOptions == 1);
    }


    public boolean save(){
        int imgWidth = 2000;
        int imgHeight = 2000;
        int tileSize = 50;
        float fontSize = 25f;
        int defWidth = 3;
        int incWidth = 7;

        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        //biale tlo
        g.setColor(Color.WHITE);
        g.fillRect(0,0,imgWidth, imgHeight);

        //wyznaczanie przesuniecia obrazka
        int maxRowLen = 0;
        int maxColLen = 0;

        for(int i=0;i<height;i++){
            int cur = this.rowList[i].size();
            if(cur > maxRowLen){
                maxRowLen = cur;
            }

        }

        for(int i=0;i<width;i++){
            int cur = this.colList[i].size();
            if(cur > maxColLen){
                maxColLen = cur;
            }
        }

        int deltaX = 10+maxRowLen*tileSize;
        int deltaY = 10+maxColLen*tileSize;


        //linie siatki
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(defWidth));
        for(int i=0;i<=this.width;i++){
            //pogrubianie co 5 linii
            if(i % 5 == 0){
                g.setStroke(new BasicStroke(incWidth));
                g.drawLine(i*tileSize+deltaX, deltaY, i*tileSize+deltaX, height*tileSize+deltaY);
                g.setStroke(new BasicStroke(defWidth));
            }
            else{
                g.drawLine(i*tileSize+deltaX, deltaY, i*tileSize+deltaX, height*tileSize+deltaY);
            }

        }
        for(int j=0;j<=this.height;j++){
            //pogrubianie co 5 linii
            if(j % 5 == 0){
                g.setStroke(new BasicStroke(incWidth));
                g.drawLine(deltaX, j*tileSize+deltaY, width*tileSize+deltaX, j*tileSize+deltaY);
                g.setStroke(new BasicStroke(defWidth));
            }
            else{
                g.drawLine(deltaX, j*tileSize+deltaY, width*tileSize+deltaX, j*tileSize+deltaY);
            }
        }

        //linie numerkow
        for(int i=0;i<width;i++){
            int curSize = this.colList[i].size();
            g.drawLine(i*tileSize+deltaX, deltaY, i*tileSize+deltaX, deltaY-curSize*tileSize);
            g.drawLine((i+1)*tileSize+deltaX, deltaY, (i+1)*tileSize+deltaX, deltaY-curSize*tileSize);
        }

        for(int i=0;i<height;i++){
            int curSize = this.rowList[i].size();
            g.drawLine(deltaX, i*tileSize+deltaY, deltaX-curSize*tileSize, i*tileSize+deltaY);
            g.drawLine(deltaX, (i+1)*tileSize+deltaY, deltaX-curSize*tileSize, (i+1)*tileSize+deltaY);
        }

        //numerki
        g.setFont(g.getFont().deriveFont(fontSize));
        for(int i=0;i<width;i++){
            int curSize = this.colList[i].size();
            for(int j=0;j<curSize;j++){
                String toShow = this.colList[i].get(j).toString();
                int textWidth = g.getFontMetrics().stringWidth(toShow);
                int textHeight = g.getFontMetrics().getHeight();
                g.drawString(toShow, i*tileSize+(tileSize-textWidth)/2+deltaX, deltaY-(curSize-1)*tileSize+j*tileSize-(tileSize-textHeight)/2);
            }
        }

        for(int i=0;i<height;i++){
            int curSize = this.rowList[i].size();
            for(int j=0;j<curSize;j++){
                String toShow = this.rowList[i].get(j).toString();
                int textWidth = g.getFontMetrics().stringWidth(toShow);
                int textHeight = g.getFontMetrics().getHeight();
                g.drawString(toShow, deltaX-curSize*tileSize+j*tileSize+(tileSize-textWidth)/2, (i+1)*tileSize+deltaY-(tileSize-textHeight)/2);
            }
        }


        //zapisywanie obrazka
        try {
            if (ImageIO.write(img, "png", new File("./nonogram_image.png")))
            {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    @Override
    public void run() {
        createNonogramFromTable();
    }
}
