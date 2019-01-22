package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;


import java.util.ArrayList;
import java.util.Random;


public class GameLogic {



    public Cell[][] boardArray;
    private Canvas boardCanvas;
    private Canvas pawnCanvas;
    private int quantityInRow;
    private int width;
    private int height;
    private Player player1;
    private Player player2;
    private int player1Quantity = 0;
    private int player2Quantity = 0;
    private ArrayList<Pawn> pawnsWithBeating;

    
    public GameLogic(int width, int height, int quantityInRow,
                     Player player1, Player player2){

        this.quantityInRow = quantityInRow;

        this.player1 = player1;
        this.player2 = player2;

        this.width = width<=height?width:height;
        this.height = width<=height?width:height;

        boardArray = createStartingBoard();
        this.boardCanvas = new Canvas(this.width, this.height);
        GraphicsContext gc = this.boardCanvas.getGraphicsContext2D();
        new Board(this.width, this.height, quantityInRow).draw(gc);
        this.pawnCanvas = drawPawnsCanvas();

    }
    public Cell cellClicked(double x, double y){
        for(Cell[] array1D: boardArray){
            for(Cell cell: array1D){
                if(cell.getBoundary().intersects(x, y, 1, 1)){
                    return cell;
                }

            }
        }
        return null;
    }

    public Cell[][] createStartingBoard(){
        Cell[][] boardArray =  new Cell[quantityInRow][quantityInRow];
        boolean white = true;

        for(int i=0; i<boardArray.length; i++){
            white = i%2==0?true:false;
            for(int j=0; j<boardArray.length; j++){
                int size = this.height/quantityInRow;
                Point pawnPoint = new Point(i, j, this.width, this.height, this.quantityInRow);
                if(j<3 || j>boardArray.length-3-1){
                    //TODO: rename
                    if(!white) {
                        if(j<3){
                            Pawn pawn = new Pawn(pawnPoint, size, i, j,player1);
                            player1Quantity += 1;
                            boardArray[i][j] = pawn;
                        }
                        else{
                            boardArray[i][j] = new Pawn(pawnPoint, size, i, j, player2);
                            player2Quantity += 1;
                        }
                    }else{
                        boardArray[i][j] = new Cell(i, j, pawnPoint, size );
                    }
                }
                else{
                    boardArray[i][j] = new Cell(i, j, pawnPoint, size );
                }


                white = white?false:true;
            }

        }
        return boardArray;
    }



    public ArrayList<int[]> calculateAllowedMoves(Pawn pawn){
        ArrayList<int[]> allowedMoves = new ArrayList<int[]>();
//        if(pawn.isSwapedToQueen()){
//            pawn.setAllowedMoves(allowedMoves);
//            pawn.setSwapedToQueen(false);
//            return;
//        }
        //TODO
        allowedMoves.addAll(calculateBeatings(pawn));

        //If there is no compulsory beatings we check if we can do normal forward move
        if(allowedMoves.isEmpty()){
            int[][] forwardMoves = pawn.getPlayer().getMoves().getMovesDirections();
            allowedMoves.addAll(pawn.checkMovesWithoutBeating(boardArray, forwardMoves));
        }

        return allowedMoves;
    }

    public  ArrayList<int[]> calculateBeatings(Pawn pawn){
        ArrayList<int[]> allowedMoves = new ArrayList<int[]>();
        int[][] forwardMoves = pawn.getPlayer().getMoves().getMovesDirections();
        int[][] backwardMoves = pawn.getPlayer().getMoves().getBackwardDirections();

        allowedMoves.addAll(pawn.checkBeating(boardArray, forwardMoves));
        allowedMoves.addAll(pawn.checkBeating(boardArray, backwardMoves));
        return allowedMoves;
    }




    public ArrayList<Pawn> calculatePawnWithBeating(Player player){

        ArrayList<Pawn> pawnsWithBeating = new ArrayList<>();

        for(Cell[] row: boardArray) {
            for (Cell cell : row) {
                if(cell instanceof Pawn){
                    Pawn pawn = (Pawn)cell;
                    if(pawn.getPlayer().equals(player)){
                        if(calculateBeatings(pawn).isEmpty() == false){
                            pawnsWithBeating.add(pawn);
                        }
                    }
                }
            }
        }
        return pawnsWithBeating;
    }


    public void movePawnToLocation(Pawn pawn, int i, int j){
        Cell[][] board = this.boardArray.clone();
        Pawn tempPawn = new Pawn(board[i][j], pawn.getPlayer());


        board[i][j] = tempPawn;


        int jumpedI = (pawn.getIndexI() + i)/2;
        int jumpedJ = (pawn.getIndexJ() + j)/2;
        board[pawn.getIndexI()][pawn.getIndexJ()] = new Cell(pawn.getIndexI(), pawn.getIndexJ(), pawn.getPoint(), pawn.getSize());

        if(jumpedI != i && jumpedJ != j && pawn.getIndexI() != jumpedI && pawn.getIndexJ() != jumpedJ){
            Pawn enemyPawn = (Pawn)board[jumpedI][jumpedJ];
            board[jumpedI][jumpedJ] = new Cell(jumpedI, jumpedJ, enemyPawn.getPoint(), enemyPawn.getSize());
        }
        boolean isBeating = !calculateBeatings(pawn).isEmpty();
        if(pawn.getPlayer().getMoves().getSide() == Moves.Side.TOP && j==boardArray.length-1 && !isBeating){
            board[i][j] = new Queen(tempPawn);
            ((Pawn)board[i][j]).setSwapedToQueen(true);
        }else if(pawn.getPlayer().getMoves().getSide() == Moves.Side.BOTTOM && j==0 && !isBeating){
            board[i][j] = new Queen(tempPawn);
            ((Pawn)board[i][j]).setSwapedToQueen(true);
        }
        this.boardArray = board;
    }

    public void moveQueenToLocation(Queen queen, int i, int j){

        Cell[][] board = this.boardArray.clone();
        int newI = i-queen.getIndexI();
        int newJ = j-queen.getIndexJ();

        int vectorI = newI>0?1:-1;
        int vectorJ = newJ>0?1:-1;

        for(int iter = 1 ;(vectorI*iter)!= newI; iter++){
            Cell cell = board[queen.getIndexI() + vectorI*iter][queen.getIndexJ() + vectorJ*iter];
            if(cell instanceof Pawn){
                Pawn cellPawn = (Pawn)cell;
                if(!cellPawn.getPlayer().equals(queen.getPlayer())){
                    board[queen.getIndexI() + vectorI*iter][queen.getIndexJ() + vectorJ*iter] =
                            new Cell(cell.getIndexI(), cell.getIndexJ(), cell.getPoint(), cell.getSize());

                }
            }
        }
        Queen newQueen = new Queen(new Pawn(board[i][j], queen.getPlayer()));
        board[i][j] = newQueen;
        board[queen.getIndexI()][queen.getIndexJ()] = new Cell(queen.getIndexI(), queen.getIndexJ(), queen.getPoint(), queen.getSize());


        this.boardArray = board;
    }


    public Pawn getRandomPawnThatCanMove(Player player){
        Random random = new Random();
        ArrayList<Pawn> playersPawns = new ArrayList<>();
        for(Cell[] row: boardArray) {
            for (Cell cell : row) {
                if(cell instanceof Pawn && ((Pawn) cell).getPlayer().equals(player)){
                    calculateAllowedMoves((Pawn)cell);
                    if(calculateAllowedMoves((Pawn) cell).isEmpty() == false){
                        playersPawns.add((Pawn)cell);
                    }



                }
            }
        }
        int index = random.nextInt(playersPawns.size());
        return playersPawns.get(index);
    }

    public Canvas getBoardCanvas(){
        return boardCanvas;
    }

    public Canvas drawPawnsCanvas(){
        Canvas pawnCanvas = new Canvas(this.width, this.height);
        GraphicsContext gc = pawnCanvas.getGraphicsContext2D();
        for(Cell[] array1D: boardArray){
            for(Cell cell: array1D){
                if(cell instanceof Pawn){
                    Pawn pawn = (Pawn)cell;

                    pawn.drawPawn(gc);
                }
            }
        }
        return pawnCanvas;
    }

    public Cell[][] getBoardArray(){ return this.boardArray;}

    public void setBoardArray(Cell[][] boardArray) {
        this.boardArray = boardArray;
    }
    public Canvas getPawnCanvas() {
        return pawnCanvas;
    }

    public void setPawnCanvas(Canvas pawnCanvas) {
        this.pawnCanvas = pawnCanvas;
    }

    public Canvas markAllowedPositions(Pawn pawn){
        Canvas canvas = new Canvas(this.width, this.height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        calculateAllowedMoves(pawn);
        for(int[] ij: calculateAllowedMoves(pawn)){
            drawBorder(gc, ij[0], ij[1]);
        }
        return canvas;
    }

    private void drawBorder(GraphicsContext gc, int i, int j){
        Rectangle2D boundaries = boardArray[i][j].getBoundary();
        gc.setStroke(Color.GREENYELLOW);
        gc.setLineWidth(5);
        gc.strokeRect(boundaries.getMinX(), boundaries.getMinY(), boundaries.getWidth(), boundaries.getWidth());

    }



    public int getPlayer1Quantity() {
        return player1Quantity;
    }

    public void setPlayer1Quantity(int player1Quantity, GameEnd gamEnd) {
        this.player1Quantity = player1Quantity;
        if(this.player1Quantity <= 0){
            gamEnd.run(player1, player2, "-1");
        }
    }

    public int getPlayer2Quantity() {
        return player2Quantity;
    }

    public void setPlayer2Quantity(int player2Quantity, GameEnd gamEnd) {
        this.player2Quantity = player2Quantity;
        if(this.player2Quantity <= 0){
            gamEnd.run(player1, player2, "1");
        }
    }


}

