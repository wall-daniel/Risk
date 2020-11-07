package risk.Controller;

import risk.Model.Country;

import java.util.ArrayList;
import java.util.HashMap;

public class MovementController {

    private  Country firstCountry, secondCountry;
    private  Controller controller;
    private HashMap<String, Country> checkedCountries = new HashMap<>();

    public MovementController(Controller controller){
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

        checkedCountries.clear();
        if (!areCountriesConnected(firstCountry))
            return false;

        return true;
    }


    public void setSecondCountry(Country secondCountry){
        this.secondCountry = secondCountry;
    }

    //get
    public boolean areCountriesConnected(Country currentCountry){
        if (currentCountry.getName().equals(secondCountry))
            return true;

        if (checkedCountries.size() == controller.getGameModel().getCountries().size())
            return false;

        for (String countryName : currentCountry.getNeighbours()){
            Country c = controller.getGameModel().getCountry(countryName);
            if (c.getPlayer().getIndex() != firstCountry.getPlayer().getIndex())
                continue;

            if (checkedCountries.containsKey(countryName))
                continue;

            areCountriesConnected(c);
        }

        return false;
    }



}
