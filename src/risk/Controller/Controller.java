package risk.Controller;

import risk.Command;
import risk.CommandWord;
import risk.Model.*;
import risk.Parser;
import risk.Players.Player;

import java.awt.*;
import java.util.*;

public class Controller {

    private GameModel gameModel;
    private Random rand;


    public Controller(GameModel gameModel) {
        this.gameModel = gameModel;
        rand = new Random(System.currentTimeMillis());
    }

    private void gameOver() {
//        System.out.println("The game is over, " + players.get(currentPlayerPosition).getName() + " won. Congrats!");
        System.exit(0);
    }

    public void editCountry(String name, ArrayList<String> neighbourNames, String continentName) {

    }


    public void createNewCountry(String name, Polygon polygon) {
        gameModel.addCountry(new Country(name, polygon));
    }


    public void createNewContinent(String continentName, int continentBonus) {
        gameModel.addContinent(new Continent(continentName, continentBonus));
    }

    public void saveMap() {
        gameModel.saveMap("map.txt");
    }
}
