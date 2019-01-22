package sample;

import javafx.geometry.Rectangle2D;

public class Cell {
    protected int indexI;
    protected int indexJ;
    protected Point point;
    protected int size;



    public Cell(int indexI, int indexJ, Point point, int size){
        this.indexI = indexI;
        this.indexJ = indexJ;
        this.point = point;
        this.size = size;
    }
    public Cell(Cell cell){
        this.indexI = cell.getIndexI();
        this.indexJ = cell.getIndexJ();
        this.point = cell.getPoint();
        this.size = cell.getSize();
    }

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }

    public int getIndexI(){
        return this.indexI;
    }
    public void setIndexI(int indexI) {
        this.indexI = indexI;
    }

    public int getIndexJ() {
        return indexJ;
    }
    public void setIndexJ(int indexJ) {
        this.indexJ = indexJ;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "index i:" + this.getIndexI() + " j:" + this.getIndexJ();
    }
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(point.x,point.y,size,size);
    }



    public boolean equals(Cell otherCell) {
        return otherCell.getIndexI() == this.indexI && otherCell.getIndexJ() == this.indexJ;
    }
}
