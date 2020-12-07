package risk.Model;

import javax.swing.*;
import java.io.FileNotFoundException;

public class EditableGameModel extends GameModel {

    private final DefaultListModel<Continent> continentsDLM = new DefaultListModel<>();

    public EditableGameModel() {
        super();
    }

    public EditableGameModel(String filename) throws FileNotFoundException {
        super(filename, false);
    }

    public void addCountry(Country country) {
        this.countries.put(country.getName(), country);
        updateEditor();
    }

    public void deleteCountry(Country country){

    }

    public void editCountryName(Country country, String countryName) {
        if (!countries.containsKey(countryName)) {
            countries.remove(country.getName()); //remove old entry
            countries.put(countryName, country); //add new entry
            country.setName(countryName);
            updateEditor();
        }
    }

    public void editCountryContinent(Country country, Continent continent){
        if (country.getContinent()!=null)
            country.getContinent().removeCountry(country);

        country.setContinent(continent);
        continent.addCountry(country);
        updateEditor();
    }

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
