package risk.Model;

import java.util.ArrayList;

// Not useful right now, but later for bonuses and gui will be.
public class Continent {

    private String name;
    private ArrayList<String> countries;

    public Continent(String name) {
        this.name = name;
    }

    public void printContinentHelper(StringBuilder sb) {
        for (String countryName : countries) {
            sb.append(countryName)
                    .append(": ")
                    .append(Countries.getCountry(countryName).getPlayer().getName())
                    .append(", ")
                    .append(Countries.getCountry(countryName).getArmies())
                    .append("\n");
        }
    }
}
