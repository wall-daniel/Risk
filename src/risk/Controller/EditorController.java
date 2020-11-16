package risk.Controller;

import risk.Model.Continent;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.View.MapCreator.EditableCountryPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

public class EditorController extends Controller {

    public EditorController(GameModel gameModel, JFrame view) {
        super(gameModel, view);
    }

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

    public void updateEditor() {
        gameModel.updateEditor();
    }

    public void saveMap() {
        try {
            gameModel.saveMap("RiskMap.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewCountry(String name, Polygon polygon) {
        gameModel.addCountry(new Country(name, polygon));
    }

    public void createNewContinent(String continentName, int continentBonus) {
        gameModel.addContinent(new Continent(continentName, continentBonus));
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

        DefaultListModel<Country> countryListModel = new DefaultListModel<>();
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

        JList<Continent> continentJList = new JList<>(continentListModel);//GameModel.getContinentNamesDefaultListModel());
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

    /**
     * Finds highest layered country and sends a click of that country to determine what action is to be done.
     *
     * @param mouseEvent, the mouse event with x, y coordinates
     */
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

            // Make sure that it found a country
            if (editableCountryPanel != null)
                editCountryDetails(editableCountryPanel.getCountry());
        }
    }

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
