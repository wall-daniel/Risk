package risk.Model;


import javax.swing.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Continents {

    private static HashMap<String, Continent> continents = new HashMap<>();

    public static DefaultListModel<String> getContinentNamesDefaultListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String s: continents.keySet())
            listModel.addElement(s);
        //listModel.addAll(continents.keySet());
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

}
