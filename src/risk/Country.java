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

    public Player getPlayer() {
        return controlledBy;
    }

    public CountryEnum getName() {
        return name;
    }

    public void setPlayer(Player p) {
        setPlayer(p, 1);
    }

    public void setPlayer(Player p, int numArmies) {
        // Remove current player from controlling it
        if (controlledBy != null) {
            this.controlledBy.removeCountry(name);
        }

        this.controlledBy = p;
        this.numArmies = numArmies;
        p.addCountry(name);
    }

    public void addArmies(int newArmies) {
        this.numArmies += newArmies;
    }

    public int getArmies() {
        return numArmies;
    }

    public void removeArmies(int armies) {
        if (this.numArmies - armies < 1) {
            throw new ArithmeticException("You cannot have less that 1 army");
        }

        this.numArmies -= armies;
    }
}
