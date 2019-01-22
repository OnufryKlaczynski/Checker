package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerWithProperties {
    private SimpleStringProperty name;
    private SimpleIntegerProperty wins = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty loses = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty draws = new SimpleIntegerProperty(0);


    public PlayerWithProperties(Player player){
        draws = new SimpleIntegerProperty(player.getDraws());
        loses= new SimpleIntegerProperty(player.getLoses());
        wins = new SimpleIntegerProperty(player.getWins());
        name = new SimpleStringProperty(player.getName());
    }


    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getWins() {
        return wins.get();
    }

    public SimpleIntegerProperty winsProperty() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins.set(wins);
    }

    public int getLoses() {
        return loses.get();
    }

    public SimpleIntegerProperty losesProperty() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses.set(loses);
    }

    public int getDraws() {
        return draws.get();
    }

    public SimpleIntegerProperty drawsProperty() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws.set(draws);
    }
}




