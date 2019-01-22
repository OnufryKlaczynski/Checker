package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main extends Application {
    private int width = 1276;
    private int height = 720;
    ArrayList<Player> players;
    private Player loggedPlayer;
    private Player computerPlayer;
    private Player enemyPlayer;
    public Stage primaryStage;
    public Tournament tournament;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        players = LoaderSaver.loadPlayers();
        logOrRegister();
        this.computerPlayer = new Player("AI", "lolki");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



    public void menuScene(){

        Button playerVsComputerButton = new Button("Graj przeciwko komputerowi");
        Button playerVsPlayerButton = new Button("Gracz vs Gracz");
        Button loadTorunament = playCurrentTournament();
        Button tournamentButton = new Button("Stwórz turniej");
        Button statisticsButton = new Button("Statystyki");

        playerVsComputerButton.setOnAction(event -> {
            this.enemyPlayer = this.computerPlayer;
            gameOption(this.loggedPlayer, computerPlayer, this::gameEnd);
        });

        playerVsPlayerButton.setOnAction( event->{
            chooseEnemyPlayer();
        });

        tournamentButton.setOnAction(event ->{
            createTournament();
        });

        statisticsButton.setOnAction(event -> ranking());

        VBox buttonsVBox = new VBox(5);
        buttonsVBox.getChildren().setAll(playerVsComputerButton,
                playerVsPlayerButton,loadTorunament,tournamentButton, statisticsButton);
        buttonsVBox.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        border.setCenter(buttonsVBox);



        Scene menuScene = new Scene(border, width, height);
        primaryStage.setScene(menuScene);
        menuScene.getStylesheets().add("css/elements.css");

    }

    public void startGame(Stage primaryStage, Player player1, Player player2, int size, GameEnd gameEnd){
        GameController gameController = new GameController(player1, player2, size, gameEnd);
        gameController.start(primaryStage );
    }


    public void registartionScene() {

        TextField name = new TextField();
        name.setPromptText("login");
        name.setMaxWidth(200);
        PasswordField password = new PasswordField();
        password.setPromptText("hasło");
        password.setMaxWidth(200);
        Button register = new Button("Zarejestruj");
        register.setOnAction(event -> {
            String login = name.getCharacters().toString();
            String pass = password.getCharacters().toString();
            if(login.equals("AI")) {
                throw new SecurityException();
            }
            Player newPlayer = new Player(login, pass);
            for(Player player: players){
                if(player.equals(newPlayer)){
                    System.out.println("Taki gracz już istnieje");
                }
            }
            players.add(newPlayer);
            new Thread(){
                public void run() {
                    LoaderSaver.savePlayers(players);
                }
            }.start();

            this.loggedPlayer = newPlayer;
            menuScene();
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(name, password, register);
        Scene scene = new Scene(vbox, width, height);
        scene.getStylesheets().add("css/elements.css");
        primaryStage.setScene(scene);

    }

    void logInScene(){
        TextField name = new TextField();
        name.setPromptText("login");
        name.setMaxWidth(200);
        PasswordField password = new PasswordField();
        password.setPromptText("hasło");
        password.setMaxWidth(200);
        Button register = new Button("Zaloguj");
        register.setAlignment(Pos.BOTTOM_RIGHT);
        register.setOnAction(event -> {
            String login = name.getCharacters().toString();
            String pass = password.getCharacters().toString();


            for(Player player: players){
                if(player.getName().equals(login)){
                    if(player.getPassword().equals(pass)){
                        this.loggedPlayer = player;
                        menuScene();
                        break;
                    }else{
                        System.out.println("podano zle haslo");
                    }
                }
            }


        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(name, password, register);

        Scene scene = new Scene(vbox, width, height);
        primaryStage.setScene(scene);

    }


    public void gameOption(Player player1, Player player2, GameEnd gameEnd) {
        Label bsize = new Label("Rozmiar planszy: ");
        TextField sizeField = new TextField("10");

        HBox sizehbox = new HBox(bsize, sizeField);
        sizehbox.setAlignment(Pos.CENTER);
        VBox player1PreGameOptions= playerPreGameOptions(player1);
        VBox player2PreGameOptions= playerPreGameOptions(player2);


        player1.setMoves(new Moves(Moves.Side.TOP));
        player2.setMoves(new Moves(Moves.Side.BOTTOM));


        Button startGame = new Button("Zacznij rozgrywke");
        startGame.setAlignment(Pos.CENTER);
        startGame.setOnAction(event -> {
            int size = Integer.parseInt(sizeField.getText());
            startGame(primaryStage, player1, player2, size, gameEnd);
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(sizehbox, startGame);

        BorderPane layout = new BorderPane();
        layout.setCenter(vbox);
        layout.setRight(player1PreGameOptions);
        layout.setLeft(player2PreGameOptions);

        Scene scene = new Scene(layout, width, height);
        scene.getStylesheets().add("css/elements.css");
        primaryStage.setScene(scene);
    }
    static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(100, 20);
            if (item != null) {
                rect.setFill(Color.web(item));
                setGraphic(rect);
            }
        }
    }

    private VBox playerPreGameOptions(Player player){
        VBox playerVbox = colorList(player);
        Label playerName = new Label(player.getName());
        playerName.setAlignment(Pos.CENTER);
        playerName.setFont(new Font(25));
        playerVbox.getChildren().add(0, playerName);
        return playerVbox;
    }



    private VBox colorList(Player player){
        VBox vbox = new VBox();
        ListView<String> list = new ListView<>();
        ObservableList<String> data = FXCollections.observableArrayList(
                "pink", "salmon", "gold", "coral", "darkorchid",
                "darkgoldenrod", "lightsalmon", "red", "rosybrown", "blue",
                "blueviolet", "wheat");
        vbox.getChildren().addAll(list);
        list.setItems(data);
        list.setCellFactory( (event)->{
            return new ColorRectCell();
        });
        list.getSelectionModel().selectedItemProperty().addListener((arg, oldVal, newVal)->{
            player.setColor(Color.web(newVal));
        });


        return vbox;
    }




    public void chooseEnemyPlayer(){
        ListView<Player> list = new ListView<>();
        ObservableList<Player> items = FXCollections.observableArrayList(players);
        list.setItems(items);


        Button customizeGame = new Button("Parametry gry");
        customizeGame.setOnAction(event -> {
            this.enemyPlayer = list.getSelectionModel().getSelectedItem();
            gameOption(this.loggedPlayer, this.enemyPlayer, this::gameEnd);
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(list, customizeGame, backButton());

        Scene scene = new Scene(hbox);
        scene.getStylesheets().add("css/elements.css");
        primaryStage.setScene(scene);
    }



    public void createTournament(){
        ListView<Player> list = new ListView<>();
        ObservableList<Player> items = FXCollections.observableArrayList(players);
        list.setItems(items);
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        HBox hbox = new HBox();
        hbox.getChildren().add(list);


        Button customizeGame = new Button("Rozpocznij gre");
        customizeGame.setOnAction(event -> {
            ArrayList<Player> tournamentPlayers = new ArrayList<>();
            tournamentPlayers.addAll(list.getSelectionModel().getSelectedItems());
            this.tournament = new Tournament(tournamentPlayers,primaryStage, this::gameOption, this::tournamentGameEnd);
            this.tournament.showBracket();

        });
        hbox.getChildren().add(customizeGame);
        hbox.getChildren().add(backButton());


        Scene scene = new Scene(hbox);
        scene.getStylesheets().add("css/elements.css");
        primaryStage.setScene(scene);
    }

    public Button playCurrentTournament(){
        Button playTournamnet= new Button("Graj ostatni truniej");
        playTournamnet.setOnAction(event -> {

            this.tournament = Tournament.loadTournamentState();
            this.tournament.setTransistenValues(this::gameOption, primaryStage, this::tournamentGameEnd);
            this.tournament.showBracket();
        });
        return playTournamnet;
    }





    public void logOrRegister(){
        Button LogIn = new Button("Zaloguj");
        Button Register = new Button("Zarejstruj");

        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(LogIn, Register);

        LogIn.setOnAction( event -> {
            logInScene();
        });

        Register.setOnAction( event -> {
            registartionScene();
        });
        BorderPane border = new BorderPane();
        border.setCenter(hbox);
        Scene scene = new Scene(border, width, height);
        scene.getStylesheets().add("css/elements.css");
        primaryStage.setScene(scene);

    }



    public void gameEnd(Player player1, Player player2, String score){
        //TODO
        Player winner;
        if(score.equals("1")){
            player1.setWins(player1.getWins() +1 );
            winner = player1;
//            player2.setLoses(player2.getLoses() +1 );
        }else if(score.equals("0")){
            winner = null; //TODO
//            player1.setDraw(player1.getDraw() +1);
//            player2.setDraw(player2.getDraw() +1);
        }else{
            winner = player2;
            player2.setWins(player2.getWins() + 1);
//            player1.setLoses(player1.getLoses() + 1);

        }



        Label winText = new Label("Wygrał gracz jakis");
        Label playerName = new Label(winner.getName());


        Button returningButton = new Button("Powrót do menu");
        returningButton.setOnAction(event -> {
            menuScene();
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(winText, playerName, returningButton);


        Scene scene = new Scene(hbox);
        primaryStage.setScene(scene);
    }

    public void tournamentGameEnd(Player player1, Player player2, String score){
        if(score.equals("1")){
            player1.setWins(player1.getWins() +1 );
            player2.setLoses(player2.getLoses() +1 );
            this.tournament.getMatch().setScore("1-0");
        }else if(score.equals("0")){
            player1.setDraws(player1.getDraws() +1);
            player2.setDraws(player2.getDraws() +1);
            this.tournament.getMatch().setScore("0-0");
        }else{
            player2.setWins(player2.getWins() + 1);
            player1.setLoses(player1.getLoses() + 1);
            this.tournament.getMatch().setScore("0-1");

        }
        this.tournament.nextMatch();
        this.tournament.showBracket();
    }

    public TableView statsTable(ArrayList<Player> players){
        ArrayList<PlayerWithProperties> playersWithProp = new ArrayList<>();
        for(Player player: players){
            playersWithProp.add(new PlayerWithProperties(player));
        }
        ObservableList<PlayerWithProperties> playersObservable = FXCollections.observableArrayList(playersWithProp);
        TableView ranking = new TableView();
        TableColumn name = new TableColumn("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn wins = new TableColumn("Wygrane");
        wins.setCellValueFactory(new PropertyValueFactory<>("wins"));

        TableColumn loses = new TableColumn("Przegrane");
        loses.setCellValueFactory(new PropertyValueFactory<>("loses"));

        TableColumn draws = new TableColumn("Remisy");
        draws.setCellValueFactory(new PropertyValueFactory<>("draws"));

        ranking.getColumns().addAll(name, wins, loses, draws);
        ranking.setItems(playersObservable);
        return ranking;
    }

    public void ranking(){
        HBox hbox = new HBox();
        hbox.getChildren().addAll(statsTable(players), backButton());
        Scene scene = new Scene( hbox, width, height);
        primaryStage.setScene(scene);
    }

    public Button backButton(){
        Button returnBtn = new Button("Powrót do menu");
        returnBtn.setOnAction((event -> {
            menuScene();
        }));
        return returnBtn;
    }





}
