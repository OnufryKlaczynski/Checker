package sample;

public class Point {
    public int x;
    public int y;


    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(int i, int j, int width, int height, int quantityInRow){
        this.x = width/quantityInRow * i;
        this.y = height/quantityInRow * j;
    }



}
