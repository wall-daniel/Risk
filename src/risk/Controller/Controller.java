package risk.Controller;

import risk.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class Controller implements MouseListener {


    private GameModel gameModel;
    private JFrame gameView;
    private Random rand;


    public Controller(GameModel gameModel, JFrame view) {
        this.gameModel = gameModel;
        this.gameView = view;
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

    public void clickedInCountry(Country country) {
        switch (gameModel.gameStatus) {
            case WAITING:
                break;
            case TROOP_PLACEMENT_PHASE:
                placeTroops(country);
                break;
            case SELECT_ATTACKING_PHASE:
                break;
            case SELECT_DEFENDING_PHASE:
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                break;
        }
    }

    private void placeTroops(Country country) {
        try {
            int troops = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            gameView,
                            "How many troops do you want to move here?",
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );

            if (gameModel.placeTroops(country, troops)) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        for (Country country : gameModel.getCountries()) {
            if (country.getPolygon().contains(mouseEvent.getX(), mouseEvent.getY())) {
                System.out.println(country.getName());
            }
         }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
