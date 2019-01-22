package sample;
import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;

public class Board {
    private int width;
    private int height;
    private int quantityInRow;

    public Board(int width, int height, int quantityInRow){
        this.width = width;
        this.height = height;
        this.quantityInRow = quantityInRow;
    }

    public void draw(GraphicsContext gc){

        int size = this.width/quantityInRow;
        boolean white = true;
        int x = 0;
        for(int i =0; i<quantityInRow; i++){

            int y = 0;
            white = i%2==0?true:false;
            for(int j=0; j<quantityInRow; j++){

                if(white){
                    gc.setFill(Color.WHITE);

                }else{
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(x, y, size, size);

                white = white? false:true;
                y+= size;
            }
            x += size;

        }
    }
}
