package sample;


import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;


public class GameController {
    public int WIDTH = 1276;
    public int HEIGHT = 800;
    public boolean pawnWasClicked = false;
    public Pawn chosenPawn = null;
    public Canvas latestPawnCanvas = null;
    public Canvas latestMarkedCanvas = null;
    public GameLogic game;
    public Player currentPlayer;
    public ObservableObjectValue<Player> observableCurrentPlayer;
    private Cell currentCellClicked;
    public Player player1;
    public Player player2;
    public Stage primaryStage;
    private GameEnd invokeMenu;
    public StackPane board;
    private BorderPane layout;

    public GameController(Player player1, Player player2, int size, GameEnd invokeMenu){
        this.player1 = player1;
        this.player2 = player2;
        this.game = new GameLogic(WIDTH, HEIGHT, size, player1, player2);
        this.currentPlayer = player1;
        this.currentCellClicked = null;
        this.invokeMenu = invokeMenu;
    }


    public void start(Stage primaryStage){

        board = new StackPane();
        board.getChildren().add(game.getBoardCanvas());

        this.latestPawnCanvas = game.drawPawnsCanvas();
        board.getChildren().add(this.latestPawnCanvas);

        VBox player1Info = playerInfo(player1);
        VBox player2Info = playerInfo(player2);

        game.calculatePawnWithBeating(player1);

        layout = new BorderPane();
        gameInfo();
        layout.setCenter(board);
        layout.setLeft(player1Info);
        layout.setRight(player2Info);


        board.setOnMouseClicked(
            event -> {
                currentCellClicked = game.cellClicked(event.getX(), event.getY());
                if(currentPlayer.getName().equals("AI") == false){

                    if (currentCellClicked instanceof Pawn == false) {
                        if(pawnWasClicked) {
                            pawnIsChosenAndCellIsClicked();

                        }
                    }
                    if (currentCellClicked instanceof Pawn && ((Pawn) currentCellClicked).getPlayer().equals(currentPlayer)) {
                        playerClicked();

                    }
                }

            }
        );


        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);

    }
    private void pawnIsChosenAndCellIsClicked(){
        if (checkIfMoveIsAllowed()) {

            int[] move = {this.currentCellClicked.getIndexI(), this.currentCellClicked.getIndexJ()};
            boolean pawnHadToBeat = !game.calculateBeatings(this.chosenPawn).isEmpty();
            if(chosenPawn instanceof Queen){
                game.moveQueenToLocation(new Queen(chosenPawn), move[0], move[1]);

            }
            else if(chosenPawn instanceof Pawn){
                game.movePawnToLocation(chosenPawn, move[0], move[1]);
            }
            resetValuesAndRedrawCanvas();
            if(pawnHadToBeat){
                if(currentPlayer.equals(player1)){
                    game.setPlayer2Quantity(game.getPlayer2Quantity() -1, invokeMenu);
                }else if(currentPlayer.equals(player2)){
                    game.setPlayer1Quantity(game.getPlayer1Quantity()-1, invokeMenu);
                }

                ArrayList<Pawn> beatings = game.calculatePawnWithBeating(this.currentPlayer);

                if(beatings.isEmpty()){
                    changePlayer();

                    if(this.currentPlayer.getName().equals("AI")){
                        AIturn();
                    }
                }
            }
            else{

                changePlayer();
                if (this.currentPlayer.getName().equals("AI")) {
                    AIturn();
                }
            }
        }
    }


    private void delayedAiMove(){
        PauseTransition pauseTransition = new PauseTransition(new Duration(500));
        pauseTransition.setOnFinished(event -> {
            boolean firstTour = true;
            boolean isBeating = true;
            while(isBeating) {
                AImove(firstTour);
                resetValuesAndRedrawCanvas();

                if (game.calculatePawnWithBeating(currentPlayer).isEmpty()) {
                    isBeating = false;

                }else{
                    isBeating = true;
                }
                firstTour = false;
            }
            changePlayer();
        });
        pauseTransition.play();
    }

    private void AIturn(){
        delayedAiMove();


    }


    private void AImove(boolean firstTour){
        ArrayList<Pawn> beatings = game.calculatePawnWithBeating(this.currentPlayer);
        boolean isBeating = !beatings.isEmpty();
        Pawn pawn;
        if(isBeating){
            Random random = new Random();

            int index = random.nextInt(beatings.size());
            pawn = beatings.get(index);
            //TODO not hardcoded
            game.setPlayer1Quantity(game.getPlayer1Quantity()-1, invokeMenu);
        }else if(firstTour){
            pawn = game.getRandomPawnThatCanMove(currentPlayer);
            isBeating = false;
        }else{
            return;
        }

        Random random = new Random();
        ArrayList<int[]> allowedMoves = game.calculateAllowedMoves(pawn);
        int index = random.nextInt(allowedMoves.size());
        int[] move = allowedMoves.get(index);
        if(pawn instanceof Queen){
            game.moveQueenToLocation((Queen)pawn, move[0], move[1]);

        }else{

            game.movePawnToLocation(pawn, move[0], move[1]);
        }

    }

    private boolean checkIfMoveIsAllowed(){
        int[] move = {currentCellClicked.getIndexI(), currentCellClicked.getIndexJ()};
        for (int[] allowedMove : game.calculateAllowedMoves(chosenPawn)) {
            if (allowedMove[0] == move[0] && allowedMove[1] == move[1]) {
                return true;
            }
        }
        return false;
    }



    private void playerClicked(){
        ArrayList<Pawn> beatings = game.calculatePawnWithBeating(this.currentPlayer);
        if(beatings.isEmpty()){
            setChosenPawnAndDrawBorder();
        } else if(beatings.contains((Pawn)currentCellClicked)){
            setChosenPawnAndDrawBorder();
        }
    }

    private void setChosenPawnAndDrawBorder(){
        Pawn pawnClicked = (Pawn) currentCellClicked;
        if (this.pawnWasClicked) {
            removePawnBorder();
        }
        this.chosenPawn = pawnClicked;
        drawAndAddPawnBorder();
        this.pawnWasClicked = true;

    }

    private void drawAndAddPawnBorder(){
        this.latestMarkedCanvas = game.markAllowedPositions(chosenPawn);
        board.getChildren().add(this.latestMarkedCanvas);
    }

    private void changePlayer(){
        this.currentPlayer = this.currentPlayer.equals(player1) ? player2 : player1;
        gameInfo();
    }

    private void resetValuesAndRedrawCanvas(){
        this.chosenPawn = null;
        this.pawnWasClicked = false;
        removePawnBorder();
        ChangePawnCanvas();

    }

    private void removePawnBorder(){
        board.getChildren().remove(this.latestMarkedCanvas);
        this.latestMarkedCanvas = null; //TODO is it necessary?
    }

    private void ChangePawnCanvas(){
        board.getChildren().remove(this.latestPawnCanvas);
        this.latestPawnCanvas = game.drawPawnsCanvas();
        board.getChildren().add(this.latestPawnCanvas);
    }

    private VBox playerInfo(Player player){

        VBox informations = new VBox();
        Label playerName = new Label(player.getName());
        playerName.setAlignment(Pos.CENTER);
        playerName.setFont(new Font(25));


        Circle showPawn = new Circle(10);
        showPawn.setFill(player.getColor());
        Rectangle whiteCell = new Rectangle(0, 0, 12 ,12);
        whiteCell.setFill(Color.WHITE);
        StackPane playerPawn = new StackPane();
        playerPawn.getChildren().setAll(whiteCell, showPawn);

        informations.getChildren().addAll(playerName, playerPawn);


        return informations;
    }

    private void gameInfo(){

        Label tourStaticText = new Label("Tura gracza: ");
        Label tourDynamicText = new Label(this.currentPlayer.getName());
        tourStaticText.setFont(new Font(30));
        tourDynamicText.setFont(new Font(30));
        HBox gameInformation = new HBox();
        gameInformation.getChildren().addAll(tourStaticText ,tourDynamicText);
        gameInformation.setAlignment(Pos.CENTER);

        layout.setTop(gameInformation);
    }


}