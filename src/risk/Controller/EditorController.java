package risk.Controller;

import risk.Model.Continent;
import risk.Model.Country;
import risk.Model.EditableGameModel;
import risk.Model.GameModel;
import risk.View.MapCreator.EditableCountryPanel;
import risk.View.MapCreator.MapEditorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class EditorController extends Controller {

    private EditableGameModel gameModel;

    private Country selectedCountry;

    public EditorController(EditableGameModel gameModel, JFrame view) {
        super(view);
        this.gameModel = gameModel;
        selectedCountry = null;
    }

    public void updateAllComponentLocations() {
        for (Component component : gameView.getLayeredPane().getComponents()) {
            if (component instanceof EditableCountryPanel) {
                gameModel.getCountry(component.getName()).setPolygonPoint(new Point(component.getLocationOnScreen().x, component.getLocationOnScreen().y));
                gameModel.getCountry(component.getName()).setLayer(gameView.getLayeredPane().getLayer(component));
                Point labelPoint = new Point(((EditableCountryPanel) component).getCountryLabel().getLocationOnScreen().x, ((EditableCountryPanel) component).getCountryLabel().getLocationOnScreen().y);
                gameModel.getCountry(component.getName()).setLabelPoint(labelPoint);
            }
        }
    }

    public void updateEditor() {
        gameModel.updateEditor();
    }

    public void saveMap(String fileName) {
        try {
            gameModel.saveMap(fileName + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewCountry(String name, Polygon polygon) {
        gameModel.addCountry(new Country(name, polygon));
    }

    public void deleteCountry(Country country) {
        gameModel.deleteCountry(country);
    }

    public void editCountryName(Country country, String countryName) {
        gameModel.editCountryName(country, countryName);
    }

    public void editCountryContinent(Country country, Continent continent) {
        gameModel.editCountryContinent(country, continent);
    }

    public void createNewContinent(String continentName, int continentBonus) {
        gameModel.addContinent(new Continent(continentName, continentBonus));
    }

    public void deleteContinent(Continent continent) {
        gameModel.deleteContinent(continent);
    }

    public void editContinentProperties(Continent continent, String continentName, int continentBonus) {
        gameModel.editContinentProperties(continent, continentName, continentBonus);

    }


    @Override
    protected void countryClicked(MouseEvent mouseEvent) {
        int highestLayer = -1;
        EditableCountryPanel editableCountryPanel = null;

        for (Component component : gameView.getLayeredPane().getComponents()) {
            if (component instanceof EditableCountryPanel) {
                EditableCountryPanel ecc = (EditableCountryPanel) component;

                Polygon translated = new Polygon(ecc.getCountry().getPolygon().xpoints, ecc.getCountry().getPolygon().ypoints, ecc.getCountry().getPolygon().npoints);
                translated.translate(ecc.getLocationOnScreen().x, ecc.getLocationOnScreen().y);

                int layer = gameView.getLayeredPane().getLayer(ecc);
                if (layer > highestLayer && translated.contains(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen())) {
                    highestLayer = layer;
                    editableCountryPanel = ecc;
                }
            }
        }

        // Make sure that it found a country
        if (editableCountryPanel != null) {
            if (selectedCountry == null) {
                System.out.println("selected: " + editableCountryPanel.getCountry());
                ((MapEditorGUI) gameView).setCountryInformation(editableCountryPanel.getCountry());
                selectedCountry = editableCountryPanel.getCountry();
            } else if (selectedCountry == editableCountryPanel.getCountry()) {
                System.out.println("de-selected: " + editableCountryPanel.getCountry());
                selectedCountry = null;
                ((MapEditorGUI) gameView).resetCountryInformation();
            } else if (((MapEditorGUI) gameView).isToggleNeighbours()) {
                System.out.println("neighbour: " + editableCountryPanel.getCountry());
                gameModel.toggleNeighbourToCountry(selectedCountry, editableCountryPanel.getCountry());
            } else {
                System.out.println("select new country: " + editableCountryPanel.getCountry());
                ((MapEditorGUI) gameView).setCountryInformation(editableCountryPanel.getCountry());
                selectedCountry = editableCountryPanel.getCountry();
                ((MapEditorGUI) gameView).resetToggleNeighbours();
            }
        }
    }

    public void updateNeighbours() {
        Component[] components = gameView.getLayeredPane().getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof EditableCountryPanel) {
                Country c1 = gameModel.getCountry(components[i].getName());
                Polygon translatedPolygon1 = new Polygon(c1.getPolygon().xpoints, c1.getPolygon().ypoints, c1.getPolygon().npoints);
                translatedPolygon1.translate(c1.getPolygonPoint().x, c1.getPolygonPoint().y);
                Area area1 = new Area(translatedPolygon1);

                for (int j = i + 1; j < components.length; j++) {
                    if (components[j] instanceof EditableCountryPanel) {
                        Country c2 = gameModel.getCountry(components[j].getName());
                        Polygon translatedPolygon2 = new Polygon(c2.getPolygon().xpoints, c2.getPolygon().ypoints, c2.getPolygon().npoints);
                        translatedPolygon2.translate(c2.getPolygonPoint().x, c2.getPolygonPoint().y);
                        Area area2 = new Area(translatedPolygon2);

                        area2.intersect(area1);
                        if (!area2.isEmpty()) {
                            c1.addNeighbour(c2.getName());
                            c2.addNeighbour(c1.getName());
                        }
                    }
                }
            }
        }

        updateEditor();
    }


}
