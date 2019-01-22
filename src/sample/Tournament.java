package sample;

import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_zh_CN;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Tournament implements Serializable {
    private ArrayList<Player> players;

    private ArrayList<Match> bracket;
    private int currentMatch;
    private transient GameOptions options;
    private transient Stage stage;
    private transient GameEnd gameEnd;

    public Tournament(ArrayList<Player> players, Stage stage, GameOptions options, GameEnd gameEnd){
        this.players = players;
        this.options = options;
        this.stage = stage;
        this.gameEnd = gameEnd;
        createBracket();


    }

    public void setTransistenValues(GameOptions options, Stage stage, GameEnd gameEnd){
        this.options = options;
        this.stage = stage;
        this.gameEnd = gameEnd;
    }


    public void createBracket(){
        this.bracket = new ArrayList<>();

        for(int i=0; i<players.size()-1; i++){
            for(int j=i+1; j<players.size(); j++){
                Match match = new Match(players.get(i), players.get(j));
                bracket.add(match);
            }
        }
        currentMatch = 0;
    }


    public void showBracket(){
        new Thread(){
            public void run() {
                saveTournamentState();
                System.out.println("zapisuje");
            }
        }.start();


        ObservableList<Match> matches = FXCollections.observableArrayList(bracket);
        TableView bracketView = new TableView();
        TableColumn player1 = new TableColumn("Gracz 1");
        player1.setCellValueFactory(new PropertyValueFactory<>("player1"));
        TableColumn player2 = new TableColumn("Gracz 2");
        player2.setCellValueFactory(new PropertyValueFactory<>("player2"));
        TableColumn score = new TableColumn("Wynik");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        bracketView.getColumns().addAll(player1, player2, score);
        bracketView.setItems(matches);

        HBox hbox = new HBox(bracketView);

        if(currentMatch >= bracket.size() == false){
            Button gameOptions = new Button("Paremetry Gry");
            gameOptions.setOnAction(event -> startMatch());
            hbox.getChildren().add(gameOptions);
        }

        Scene scene = new Scene(hbox);
        stage.setScene(scene);
    }

    public void startMatch(){

        Match match = bracket.get(currentMatch);

        options.run(match.getPlayer1(), match.getPlayer2(), gameEnd);
    }

    public Match getMatch(){
        return bracket.get(currentMatch);
    }

    public void nextMatch(){
        this.currentMatch += 1;
    }

    public int getCurrentMatch() {
        return currentMatch;
    }


    public ArrayList<Match> getBracket() {
        return bracket;
    }

    public void saveTournamentState(){
        try{
            ObjectOutputStream o = null;
            try{
                FileOutputStream f = new FileOutputStream(new File("tournament.txt"));
                o = new ObjectOutputStream(f);
                o.writeObject(this);

            }
            catch (FileNotFoundException e) {
                System.out.println("File not found");

            }finally {
                o.flush();
                o.close();
            }

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream");
        }

    }

    static Tournament loadTournamentState(){
        Tournament tournament = null;
        try{
            FileInputStream fi = new FileInputStream(new File("tournament.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            try{
                tournament = (Tournament) oi.readObject();
            }finally {
                oi.close();
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return tournament;
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("Error initializing stream");
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tournament;
    }



}
