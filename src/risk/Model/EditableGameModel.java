package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import risk.Enums.StringGlobals;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class EditableGameModel extends GameModel {

    protected DefaultListModel<Continent> continentsDLM = new DefaultListModel<>();

    public EditableGameModel() {
        super();
    }

    public EditableGameModel(String filename) throws FileNotFoundException {
        super();
        loadMap(JsonParser.parseReader(new FileReader(StringGlobals.mapsFolder + filename)).getAsJsonArray());
        this.mapName = filename;
        updateEditor();
    }

    @Override
    protected void loadMap(JsonArray json) {
        // Loop through every continent
        for (int i = 0; i < json.size(); i++) {
            // The continent json contains the countries in it.
            Continent continent = new Continent(json.get(i).getAsJsonObject());
            addContinent(continent);

            for (Country country : continent.getCountries()) {
                countries.put(country.getName(), country);
            }
        }
    }

    public void addCountry(Country country) {
        this.countries.put(country.getName(), country);
        updateEditor();
    }


    public void editCountryContinent(Country country, Continent continent){
        if (country.getContinent()!=null)
            country.getContinent().removeCountry(country);

        country.setContinent(continent);
        continent.addCountry(country);
        updateEditor();
    }

    @Override
    public void addContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
        continentsDLM.addElement(continent);
        updateEditor();
    }

    public void deleteContinent(Continent continent){
        this.continents.remove(continent.getName());
        continentsDLM.removeElement(continent);
        updateEditor();
    }

    public void editContinentProperties(Continent continent, String continentName, int continentBonus){
        if (!continents.containsKey(continentName))
            continent.setName(continentName);
        continent.setContinentBonus(continentBonus);
        updateEditor();
    }

    public DefaultListModel<Continent> getContinentListModel(){
        return continentsDLM;
    }

    public void toggleNeighbourToCountry(Country country, Country neighbour){
        country.toggleNeighbour(neighbour);
        updateEditor();
    }
}
