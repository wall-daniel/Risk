package risk.Controller;

import risk.View.Map.CountryPanel;
import risk.View.MapCreator.EditableCountryPanel;
import risk.View.MapCreator.MapEditorGUI;
import risk.View.Views.GameActionListener;
import risk.View.Views.GameModelListener;

import risk.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
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
            case TROOP_PLACEMENT_PHASE:
                placeArmies(country);
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

    private void placeArmies(Country country) {
        // Make sure that the country is owned by the player
        if (country.getPlayer() == gameModel.getCurrentPlayer()) {
            try {
                String result = JOptionPane.showInputDialog(
                        gameView,
                        "How many armies do you want to place in " + country.getName() + "?",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // If it returns null then they closed or cancelled
                if (result == null) {
                    return;
                }

                // Update the armies in the country
                int armies = Integer.parseInt(result);
                if (armies < 1) {
                    showErrorMessage("Have to supply a number greater than 0.");
                    return;
                }
                gameModel.placeArmies(country, armies);

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

    /**
     * Finds highest layered country and sends a click of that country to determine what action is to be done.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Find the thing clicked on that has the highest layer (e.g. one on top)
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

            // Make sure that a country was clicked
            if (clickedCountry != null) clickedInCountry(clickedCountry);
        }
    }

    /**
     * Edit the country's name, neighbours, and continent.
     *
     * @param clickedCountry, country clicked on
     */
    private void editCountryDetails(Country clickedCountry) {
        JPanel countryInfoPanel = new JPanel(new GridLayout(2, 3));
        countryInfoPanel.setPreferredSize(new Dimension(600,500));
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
        JScrollPane neighboursScrollPane = new JScrollPane(neighboursJList);

        DefaultListModel<Continent> continentListModel = new DefaultListModel<>();
        gameModel.getContinents().forEach(continentListModel::addElement);

        JList<Continent> continentJList = new JList<Continent>(continentListModel);//GameModel.getContinentNamesDefaultListModel());
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if (gameModel.getCountry(clickedCountry.getName()).getContinent()!=null)
            continentJList.setSelectedValue(clickedCountry.getContinent(), true);
        JScrollPane continentsScrollPane = new JScrollPane(continentJList);


        countryInfoPanel.add(countryNameLabel);
        countryInfoPanel.add(neighboursLabel);
        countryInfoPanel.add(continentLabel);

        countryInfoPanel.add(countryNameTextField);
        countryInfoPanel.add(neighboursScrollPane);
        countryInfoPanel.add(continentsScrollPane);

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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Back")){
            switch (gameModel.gameStatus) {
                case TROOP_PLACEMENT_PHASE:
                case SELECT_ATTACKING_PHASE:
                    break;
                case SELECT_DEFENDING_PHASE:
                    attackController.resetController();
                    gameModel.resetPhase();
                    break;
                case SELECT_TROOP_MOVING_FROM_PHASE:
                    break;
                case SELECT_TROOP_MOVING_TO_PHASE:
                    movementController.resetController();
                    gameModel.resetPhase();
                    break;
            }
        } else {
            switch (gameModel.gameStatus) {
                case TROOP_PLACEMENT_PHASE:
                    break;
                case SELECT_ATTACKING_PHASE:
                case SELECT_TROOP_MOVING_FROM_PHASE:
                    gameModel.nextPhase();
                    break;
                case SELECT_DEFENDING_PHASE:
                    attackController.resetController();
                    gameModel.nextPhase();
                    break;
                case SELECT_TROOP_MOVING_TO_PHASE:
                    movementController.resetController();
                    gameModel.nextPhase();
                    break;
            }
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


    public void updateEditor() {
        gameModel.updateEditor();
    }


    /**
     * For use during MapEditing, to update the component locations before saving the map
     */
    public void updateAllComponentLocations() {
        for (Component component : gameView.getLayeredPane().getComponents()){
            if (component instanceof EditableCountryPanel) {
                gameModel.getCountry(component.getName()).setPolygonPoint(new Point(component.getLocationOnScreen().x, component.getLocationOnScreen().y));
                gameModel.getCountry(component.getName()).setLayer(gameView.getLayeredPane().getLayer(component));
                Point labelPoint = new Point(((EditableCountryPanel) component).getCountryLabel().getLocationOnScreen().x,  ((EditableCountryPanel) component).getCountryLabel().getLocationOnScreen().y);
                gameModel.getCountry(component.getName()).setLabelPoint(labelPoint);
            }
        }
    }


    /**
     * For use during MapEditing, to automatically generate the neighbours before saving the map
     */
    public void updateNeighbours() {
        Component[] components = gameView.getLayeredPane().getComponents();
        for (int i = 0; i < components.length; i++){
            if (components[i] instanceof EditableCountryPanel){
                Country c1 = gameModel.getCountry(components[i].getName());
                Polygon translatedPolygon1 = new Polygon(c1.getPolygon().xpoints, c1.getPolygon().ypoints, c1.getPolygon().npoints);
                translatedPolygon1.translate(c1.getPolygonPoint().x, c1.getPolygonPoint().y);
                Area area1 = new Area(translatedPolygon1);

                for (int j = i+1; j < components.length; j++){
                    if (components[j] instanceof EditableCountryPanel){
                        Country c2 = gameModel.getCountry(components[j].getName());
                        Polygon translatedPolygon2 = new Polygon(c2.getPolygon().xpoints, c2.getPolygon().ypoints, c2.getPolygon().npoints);
                        translatedPolygon2.translate(c2.getPolygonPoint().x, c2.getPolygonPoint().y);
                        Area area2 = new Area(translatedPolygon2);

                        area2.intersect(area1);
                        if (!area2.isEmpty()){
                            c1.addNeighbour(c2.getName());
                            c2.addNeighbour(c1.getName());
                        }
                    }
                }
            }

        }
    }
}
