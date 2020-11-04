package risk.Model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GameModel {
    private static HashMap<String, Continent> continents = new HashMap<>();
    private static HashMap<String, Country> countries = new HashMap<>();
    private static HashMap<Integer, Player> players = new HashMap<>();

    public static ArrayList<Player> getPlayers(){
        return (ArrayList<Player>) players.values();
    }

    public static DefaultListModel<String> getContinentNamesDefaultListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String s: continents.keySet())
            listModel.addElement(s);
        return listModel;
    }

    public static HashMap<String, Continent> getContinents(){
        return continents;
    }


    public static void addContinent(String name, Continent continent){
        continents.put(name, continent);
    }

    public static Continent getContinent(String name){
        return continents.get(name);
    }

    public static Set<String> getContinentNames(){
        return continents.keySet();
    }

    public static DefaultListModel<String> getCountryNamesDefaultListModel(String currentName){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String s: countries.keySet())
            if (!s.equals(currentName))
                listModel.addElement(s);
        return listModel;
    }

    public static Set<String> getCountryNames(){
        return countries.keySet();
    }

    public static HashMap<String, Country> getCountries(){
        return countries;
    }

    public static void addCountry(String name, Country country){
        countries.put(name, country);
    }

    public static Country getCountry(String name){
        return countries.get(name);
    }

    public static int getIndexOfCountry(String name){
        int i = 0;
        for (String n : countries.keySet()){
            if (n.equals(name))
                return i;
            i++;
        }
        return 0;
    }


    public static void editCountry(String name, ArrayList<String> neighbourNames, String continentName) {
        if (countries.containsKey(name)){
            Country c = countries.get(name);
            c.setContinentName(continentName);
            c.setNeighbours(neighbourNames);
            countries.replace(name, c);
        } else {
            Country c  = new Country(name, neighbourNames, continentName);
            countries.put(name, c);
        }
    }

    public static boolean countryExists(String countryName) {
        return countries.containsKey(countryName);
    }


}
