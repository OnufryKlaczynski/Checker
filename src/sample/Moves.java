package sample;

public class Moves {
    Side side;

    public enum Side{
        TOP, BOTTOM


    }
    public Moves(Side side){
        this.side = side;
    }

    public int[][] getMovesDirections(){
        int[][] moves;
        if(side == Side.TOP){
            moves = new int[][]{{-1, 1},  {1, 1}};
        }else{
            moves = new int[][]{{-1, -1},  {1,-1}};
        }
        return moves;
    }
    public int[][] getBackwardDirections(){
        int[][] moves;
        if(side == Side.BOTTOM){
            moves = new int[][]{{-1, 1},  {1, 1}};
        }else{
            moves = new int[][]{{-1, -1},  {1,-1}};
        }
        return moves;
    }

    public Side getSide() {
        return side;
    }
}
