package risk.Model;

import java.util.ArrayList;

public class Continent {

    private String name;
    private ArrayList<String> countries;
    private int continentBonus;

    public Continent(String name, int continentBonus) {
        this.name = name;
        this.countries = new ArrayList<>();
        this.continentBonus = continentBonus;
    }

    public String getName(){
        return name;
    }
}
