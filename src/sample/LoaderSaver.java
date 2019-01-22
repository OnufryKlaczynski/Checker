package sample;

import java.io.*;
import java.util.ArrayList;

public class LoaderSaver {


    static ArrayList<Player> loadPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        try{
            FileInputStream fi = new FileInputStream(new File("players.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            try{
               players = (ArrayList<Player>) oi.readObject();
            }finally {
                oi.close();
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return players;
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("Error initializing stream");
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return players;
    }





    static void savePlayers(ArrayList<Player> players){
        try{
            ObjectOutputStream o = null;
            try{
                FileOutputStream f = new FileOutputStream(new File("players.txt"));
                o = new ObjectOutputStream(f);
                o.writeObject(players);

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




}
