package risk.Controller;

import risk.Model.Country;

import java.util.ArrayList;
import java.util.HashMap;

public class MapController {

    public Country firstCountry, secondCountry;
    public Controller controller;

    public MapController(Controller controller){
        this.controller = controller;
    }

    public boolean isFirstCountryValidChoice(Country firstCountry){
        if (controller.getGameModel().getCurrentPlayer() != firstCountry.getPlayer().getIndex())
            return false;
        return true;
    }

    public void setFirstCountry(Country firstCountry){
        this.firstCountry = firstCountry;
    }

    public boolean isSecondCountryValidChoice(Country secondCountry){
        if (controller.getGameModel().getCurrentPlayer() != firstCountry.getPlayer().getIndex())
            return false;

        ArrayList<Country> checkedCountries = new ArrayList<Country>();
        if (!areCountriesConnected(checkedCountries, firstCountry))
            return false;

        return true;
    }


    public void setSecondCountry(Country secondCountry){
        this.secondCountry = secondCountry;
    }

    public boolean areCountriesConnected(ArrayList<Country> checkedCountries, Country currentCountry){
        if (currentCountry.getName().equals(secondCountry))
            return true;

        if (checkedCountries.size() == controller.getGameModel().getCountries().size())
            return false;

        for (String countryName : currentCountry.getNeighbours()){



           //Country c = controller.getGameModel().getCountry(countryNames);
        }


        return true;
    }



}
