package risk.Controller;

import risk.Model.*;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    public void editCountry(String countryName, ArrayList<Country> neighbours, Continent continent) {
        // Get the neighbour names
        ArrayList<String> names = new ArrayList<>();
        neighbours.forEach(it -> names.add(it.getName()));

        // Get the country and then edit it
        for (Country c : gameModel.getCountries()) {
            if (c.getName().equals(countryName)) {
                gameModel.editCountry(c, names, continent);
            }
        }
    }


    public void createNewCountry(String name, Polygon polygon) {
        gameModel.addCountry(new Country(name, polygon));
    }


    public void createNewContinent(String continentName, int continentBonus) {
        gameModel.addContinent(new Continent(continentName, continentBonus));
    }

    public void saveMap() {
        try {
            gameModel.saveMap("map.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Continent> getContinents() {
        return gameModel.getContinents();
    }

    public List<Country> getCountriesForNeigbhours(String countryName) {
        return gameModel.getCountries();
    }

    public void updateGame() {
        gameModel.updateGame();
    }
}
