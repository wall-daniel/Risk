import java.util.ArrayList;
import java.util.List;

public class Country {

    private final String name;
    private int numArmies = 1;
    private final List<String> neighbours;
    private final Continent continent;
    private Player controlledBy = null;

    public Country(String name, Continent continent) {
        this.name = name;
        this.continent = continent;
        this.neighbours = new ArrayList<>();
    }

    public boolean canAttack() { return numArmies > 1; }

    public String[] getNeighbourNames() {
        String[] names = new String[neighbours.size()];

        for (int i = 0; i < neighbours.size(); i++) {
            names[i] = neighbours.get(i);
        }

        return names;
    }

    public void addNeighbour(String neighbour) {
        neighbours.add(neighbour);
    }

    public void addArmies(int newArmies) {
        this.numArmies += newArmies;
    }

    public String getName() {
        return name;
    }

    public int getArmies() {
        return numArmies;
    }

    public void setPlayer(Player p) {
        // Remove current player from controlling it
        if (controlledBy != null) {
            this.controlledBy.removeCountry(this);
        }

        this.controlledBy = p;
        p.getCountries().add(this);
    }
}
