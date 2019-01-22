package sample;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String password;
    private int wins = 0;
    private int loses = 0;
    private int draws = 0;
    transient Moves moves = null;
    transient Color color = null;

    public Player(String name, String password){
        this.name = name;
        this.password = password;

    }

    public boolean equals(Player other) {
        if(this.name.equals(other.name)){
            return true;
        }
        return false;
    }


    public Color getColor(){
        return this.color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public Moves getMoves() {
        return moves;
    }
    public void setMoves(Moves moves) {
        this.moves = moves;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
