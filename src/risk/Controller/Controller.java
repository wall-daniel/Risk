package risk.Controller;

import risk.Model.*;
import risk.Listener.Listeners.GameActionListener;
import risk.Listener.Listeners.GameModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class Controller implements MouseListener, ActionListener {


    private GameModel gameModel;
    private JFrame gameView;
    private Random rand;

    private AttackController attackController;
    private MovementController movementController;

    public Controller(GameModel gameModel, JFrame view) {
        this.gameModel = gameModel;
        this.gameView = view;
        this.attackController = new AttackController(gameModel, view);
        this.movementController = new MovementController(view, gameModel);
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

    public void clickedInCountry(Country country) {
        switch (gameModel.gameStatus) {
            case WAITING:
                break;
            case TROOP_PLACEMENT_PHASE:
                placeTroops(country);
                break;
            case SELECT_ATTACKING_PHASE:
                attackController.setAttackingCountry(country);
                break;
            case SELECT_DEFENDING_PHASE:
                attackController.setDefendingCountry(country);
                attackController.resetController();
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                movementController.setMovingToCountry(country);
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                movementController.setMovingFromCountry(country);
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

            if (!gameModel.placeTroops(country, troops)) {
                System.out.println("That didn't work, you can't do that.");
            }

            // Check if done placing troops
            if (gameModel.donePlacingArmies()) {
                gameModel.nextPhase();
            }

            gameModel.updateGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        for (Country country : gameModel.getCountries()) {
            if (country.getPolygon().contains(mouseEvent.getX(), mouseEvent.getY())) {
                System.out.println(country.getName());
                clickedInCountry(country);
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

    public void startGame() {
        gameModel.startGame();
    }

    public void addAsGameActionListener(GameActionListener gameActionListener) {


    }

    public void addAsGameModelListener(GameModelListener gameModelListener) {


    }

    public void removeAsGameActionListener(GameActionListener gameActionListener) {


    }

    public void removeAsGameModelListener(GameModelListener gameModelListener) {


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Clicked");
        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                return;
            case SELECT_ATTACKING_PHASE:
                gameModel.startEndTurn();
                break;
            case SELECT_DEFENDING_PHASE:
                attackController.resetController();
                gameModel.nextPhase();
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                gameModel.nextTurn();
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                gameModel.gameStatus = GameModel.GameStatus.SELECT_TROOP_MOVING_FROM_PHASE;
                break;
            case WAITING:
                return;
        }

        gameModel.updateGame();
    }
}
