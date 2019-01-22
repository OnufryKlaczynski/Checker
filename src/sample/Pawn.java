package sample;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;


public class Pawn extends Cell{


    private Player player;
    private boolean swapedToQueen = false;

    public Pawn(Point point, int size, int indexI, int indexJ, Player player){
        super(indexI, indexJ, point, size);
        this.player = player;
    }

    public Pawn(Cell cell, Player player){
        super(cell);
        this.player = player;

    }
    public Pawn(Pawn pawn){
        super(pawn.indexI, pawn.indexJ, pawn.point, pawn.size);
        this.player = pawn.player;

    }


    public void drawPawn(GraphicsContext gc){
        Color color = this.player.getColor();
        gc.setFill(color);
        gc.fillOval(point.x, point.y, size-1, size-1);

    }

    protected boolean checkIfCoordinatesInBorder(int i, int j, int length){
        if ((i < 0 || i >= length || j < 0 || j >= length)) {
            return false;
        }
        return true;
    }

    public ArrayList<int[]> checkMovesWithoutBeating(Cell[][] boardArray, int[][] moves)
    {
        ArrayList<int[]> allowedMoves = new ArrayList<int[]>();

        for (int[] move : moves) {
            int i = move[0] + this.getIndexI();
            int j = move[1] + this.getIndexJ();

            //Check boundaries of board
            if ((i < 0 || i >= boardArray.length || j < 0 || j >= boardArray.length)) {
                continue;
            }
            Cell cell = boardArray[i][j];
            if (cell instanceof Pawn == false) {
                int[] currentMove = {i, j};
                allowedMoves.add(currentMove);
            }
        }
        return allowedMoves;
    }

    public ArrayList<int[]> checkBeating(Cell[][] boardArray, int[][] moves) {
        ArrayList<int[]> allowedMoves = new ArrayList<>();

        for (int[] move : moves) {
            int i = move[0] + this.getIndexI();
            int j = move[1] + this.getIndexJ();

            //Check boundaries of board
            if(!checkIfCoordinatesInBorder(i, j, boardArray.length)){
                continue;
            }
            Cell cell = boardArray[i][j];
            if (cell instanceof Pawn) {
                Pawn otherPawn = (Pawn) cell;
                if (!otherPawn.getPlayer().equals(this.getPlayer())) {
                    int newI = move[0] * 2 + this.getIndexI();
                    int newJ = move[1] * 2 + this.getIndexJ();
                    if (!checkIfCoordinatesInBorder(newI, newJ, boardArray.length)) {
                        continue;
                    }
                    cell = boardArray[newI][newJ];
                    if (cell instanceof Pawn == false) {

                        int[] currentMove = {newI, newJ};
                        allowedMoves.add(currentMove);

                    }
                }
            }
        }
        return allowedMoves;
    }


    public Player getPlayer(){
        return this.player;
    }

    public boolean isSwapedToQueen() {
        return swapedToQueen;
    }

    public void setSwapedToQueen(boolean swapedToQueen) {
        this.swapedToQueen = swapedToQueen;
    }
}
