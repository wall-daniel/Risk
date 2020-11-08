package risk.Controller;

import risk.View.Map.CountryLabel;
import risk.View.Map.CountryPanel;
import risk.View.MapCreator.EditableCountryPanel;
import risk.View.MapCreator.MoveableCountryLabel;
import risk.View.Views.GameActionListener;
import risk.View.Views.GameModelListener;

import risk.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.*;

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
        this.movementController = new MovementController(gameModel, view);
        rand = new Random(System.currentTimeMillis());
    }

    public GameModel getGameModel(){
        return gameModel;
    }


    private void gameOver() {
//        System.out.println("The game is over, " + players.get(currentPlayerPosition).getName() + " won. Congrats!");
        System.exit(0);
    }

    public void editCountry(String countryName, ArrayList<Country> neighbours, Continent continent) {
        System.out.println("Edit: " + countryName + " " + neighbours + " " + continent.getName());


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
            gameModel.saveMap("RiskMap.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Continent> getContinents() {
        return gameModel.getContinents();
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
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                movementController.setMovingFromCountry(country);
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                movementController.setMovingToCountry(country);
                break;
        }
    }

    private void placeTroops(Country country) {
        // Make sure that the country is owned by the player
        if (country.getPlayer() == gameModel.getCurrentPlayer()) {
            try {
                String result = JOptionPane.showInputDialog(
                        gameView,
                        "How many troops do you want to move here?",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // If it returns null then they closed or cancelled
                if (result == null) {
                    return;
                }

                // Update the armies in the country
                gameModel.placeArmies(country, Integer.parseInt(result));

                // Check if done placing troops
                if (gameModel.donePlacingArmies()) {
                    gameModel.nextPhase();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error getting number: " + e.getMessage());
            }
        }
    }

    /**
     * @param errorMessage to be displayed to user.
     */
    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(gameView, errorMessage, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof EditableCountryPanel){
            int highestLayer = -1;
            EditableCountryPanel editableCountryPanel = null;

            for (Component component : gameView.getLayeredPane().getComponents()){
                if (component instanceof  EditableCountryPanel){
                    EditableCountryPanel ecc = (EditableCountryPanel) component;

                    Polygon translated = new Polygon(ecc.getCountry().getPolygon().xpoints, ecc.getCountry().getPolygon().ypoints, ecc.getCountry().getPolygon().npoints);
                    translated.translate(ecc.getLocationOnScreen().x, ecc.getLocationOnScreen().y);

                    int layer = gameView.getLayeredPane().getLayer(ecc);
                    if (layer > highestLayer && translated.contains(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen())){
                        highestLayer = layer;
                        editableCountryPanel = ecc;
                    }
                }
            }

            editCountryDetails(editableCountryPanel.getCountry());
        } else if (mouseEvent.getSource() instanceof CountryPanel){


            int highestLayer = -1;
            Country clickedCountry = null;


            for (Country country : gameModel.getCountries()) {
                Polygon translated = new Polygon(country.getPolygon().xpoints, country.getPolygon().ypoints, country.getPolygon().npoints);
                translated.translate(country.getPolygonPoint().x, country.getPolygonPoint().y);


                if (translated.contains(mouseEvent.getX(), mouseEvent.getY()) && country.getLayer() > highestLayer) {
                    highestLayer = country.getLayer();
                    clickedCountry = country;
                }
            }

            clickedInCountry(clickedCountry);
        } else {

        }
    }

    private void editCountryDetails(Country clickedCountry) {
        JPanel countryInfoPanel = new JPanel(new GridLayout(2, 3));

        JLabel countryNameLabel = new JLabel("Country Name");
        JLabel neighboursLabel = new JLabel("Select Neighbours");
        JLabel continentLabel = new JLabel("Select Continent");

        JTextField countryNameTextField = new JTextField(clickedCountry.getName());

        DefaultListModel<Country> countryListModel = new DefaultListModel<Country>();
        gameModel.getCountries().forEach(e -> {
                    if (!e.getName().equals(clickedCountry.getName()))
                        countryListModel.addElement(e);
                });

        JList<Country> neighboursJList = new JList<>(countryListModel);//GameModel.getCountryNamesDefaultListModel(countryName));
        neighboursJList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        neighboursJList.setSelectedIndices(gameModel.getCurrentNeighboursOfCountry(clickedCountry));

        DefaultListModel<Continent> continentListModel = new DefaultListModel<>();
        gameModel.getContinents().forEach(continentListModel::addElement);

        JList<Continent> continentJList = new JList<Continent>(continentListModel);//GameModel.getContinentNamesDefaultListModel());
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if (gameModel.getCountry(clickedCountry.getName()).getContinent()!=null)
            continentJList.setSelectedValue(clickedCountry.getContinent(), true);

        countryInfoPanel.add(countryNameLabel);
        countryInfoPanel.add(neighboursLabel);
        countryInfoPanel.add(continentLabel);

        countryInfoPanel.add(countryNameTextField);
        countryInfoPanel.add(neighboursJList);
        countryInfoPanel.add(continentJList);

        JOptionPane.showMessageDialog(gameView, countryInfoPanel);

        String name = countryNameTextField.getText();
        ArrayList<Country> neighbours = neighboursJList.isSelectionEmpty() ? new ArrayList<>() : (ArrayList<Country>) neighboursJList.getSelectedValuesList();
        Continent continent = continentJList.getSelectedValue();

        editCountry(name, neighbours, continent);
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


    public ArrayList<String> getClickableCountries(){
        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
            case SELECT_ATTACKING_PHASE:
            case SELECT_TROOP_MOVING_FROM_PHASE:
                return gameModel.getPlaceableCountries();
            case SELECT_DEFENDING_PHASE:
                return gameModel.getAttackableCountries(attackController.getAttackingCountry());
            case SELECT_TROOP_MOVING_TO_PHASE:
                return gameModel.getMoveTroopsToCountries(movementController.getFromCountry());
            default:
                return new ArrayList<>();
        }
    }


    public void updateAllComponentLocations() {
        for (Component component : gameView.getLayeredPane().getComponents()){
            if (component instanceof EditableCountryPanel) {
                gameModel.getCountry(component.getName()).setPolygonPoint(component.getLocationOnScreen());
                gameModel.getCountry(component.getName()).setLayer(gameView.getLayeredPane().getLayer(component));
                gameModel.getCountry(component.getName()).setLabelPoint(((EditableCountryPanel) component).getCountryLabel().getLocationOnScreen());
            }
        }
    }
}
