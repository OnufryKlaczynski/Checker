package sample;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Match implements Serializable {

    private final Player player1;

    private final Player player2;
    private String score;

    public Match(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
//        player1Name = new SimpleStringProperty(player1.getName());
//        player2Name = new SimpleStringProperty(player2.getName());
        this.score ="-";
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
