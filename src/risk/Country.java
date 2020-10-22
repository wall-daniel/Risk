package risk;

import risk.Enums.CountryEnum;
import risk.Players.Player;

public class Country {

    private final CountryEnum name;
    private int numArmies = 1;
    private Player controlledBy = null;

    public Country(CountryEnum name) {
        this.name = name;
        name.country = this;
    }

//    public boolean canAttack() { return numArmies > 1; }

    public String getNeighbourNames() {
        return name.getNeighbours().toString();
    }

    public CountryEnum getName() {
        return name;
    }

    public void setPlayer(Player p) {
        // Remove current player from controlling it
        if (controlledBy != null) {
            this.controlledBy.removeCountry(name);
        }

        this.controlledBy = p;
        p.addCountry(name);
    }

    public void addArmies(int newArmies) {
        this.numArmies += newArmies;
    }

    public int getArmies() {
        return numArmies;
    }
}
