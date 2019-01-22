package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Queen extends Pawn {

    public Queen(Pawn pawn){
        super(pawn);
    }

    @Override
    public ArrayList<int[]> checkMovesWithoutBeating(Cell[][] boardArray, int[][] forwardMoves)
    {
        ArrayList<int[]> allowedMoves = new ArrayList<int[]>();
        int[][] backwardMoves = this.getPlayer().getMoves().getBackwardDirections();


         outter: for (int[] move : forwardMoves) {
             for (int multiplier = 1; multiplier < 10; multiplier++) {
                 int i = move[0] * multiplier + this.getIndexI();
                 int j = move[1] * multiplier + this.getIndexJ();

                 //Check boundaries of board
                 if ((i < 0 || i >= boardArray.length || j < 0 || j >= boardArray.length)) {
                     continue;
                 }
                 Cell cell = boardArray[i][j];
                 if (cell instanceof Pawn == false && cell instanceof Queen == false) {
                     int[] currentMove = {i, j};
                     allowedMoves.add(currentMove);
                 }else{
                     continue outter;
                 }
             }
         }
        outter2: for (int[] move : backwardMoves) {
            for(int multiplier = 1; multiplier<10 ;multiplier++){
                int i = move[0]*multiplier + this.getIndexI();
                int j = move[1]*multiplier + this.getIndexJ();

                //Check boundaries of board
                if ((i < 0 || i >= boardArray.length || j < 0 || j >= boardArray.length)) {
                    continue;
                }
                Cell cell = boardArray[i][j];
                if (cell instanceof Pawn == false && cell instanceof  Queen == false) {
                    int[] currentMove = {i, j};
                    allowedMoves.add(currentMove);
                }else{
                    continue outter2;
                }
            }

        }
        return allowedMoves;
    }


    private ArrayList<int []> checkDiagonal(Cell[][] boardArray, int[] vector, int startingRow, int startingColumn ) {
        ArrayList<int[]> checkedDiagonal = new ArrayList<int[]>();
        boolean foundEnemy = false;
        for (int multiplier = 1; multiplier < 10; multiplier++) {
            int i = (vector[0] * multiplier)+ startingRow;
            int j = (vector[1] * multiplier) + startingColumn;


            if (!checkIfCoordinatesInBorder(i, j, boardArray.length)) {
                continue;
            }
            Cell cell = boardArray[i][j];
            if (cell instanceof Pawn == false && cell instanceof Queen == false) {
                checkedDiagonal.add(new int[]{i, j});
            } else if(cell instanceof Pawn) {
                return checkedDiagonal;
            }

        }

        return checkedDiagonal;
    }

    public ArrayList<int[]> checkBeating(Cell[][] boardArray, int[][] moves) {
        ArrayList<int[]> allowedMoves = new ArrayList<int[]>();

        outter: for (int[] move : moves) {
            for(int multiplier = 1; multiplier<10 ;multiplier++){
                int i = move[0]*multiplier + this.getIndexI();
                int j = move[1]*multiplier + this.getIndexJ();


                if(!checkIfCoordinatesInBorder(i, j, boardArray.length)){
                    continue;
                }
                Cell cell = boardArray[i][j];
                if (cell instanceof Pawn) {
                    Pawn otherPawn = (Pawn) cell;
                    if (!otherPawn.getPlayer().equals(this.getPlayer())) {
                        ArrayList<int []> diagonal =checkDiagonal(boardArray, move, otherPawn.getIndexI(), otherPawn.getIndexJ());


                        allowedMoves.addAll(diagonal);

                        continue outter;


                    }
                    else if(otherPawn.getPlayer().equals(this.getPlayer())){
                        continue outter;
                    }
                }
            }
        }
        return allowedMoves;
    }

}
