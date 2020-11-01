package risk.Players;

import risk.Model.Country;
import risk.Model.Countries;

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<String> countriesOwned;
    private boolean lost = false;

    public Player(String name) {
        this.name = name;
        countriesOwned = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getCountries() {
        return countriesOwned;
    }

    public String getCountriesAsString() {
        StringBuilder sb = new StringBuilder(countriesOwned.get(0));
        for (int i = 1; i < countriesOwned.size(); i++) {
            sb.append(", ").append(countriesOwned.get(i));
        }
        return sb.toString();
    }

    public String getCountriesAsStringWithArmies() {
        StringBuilder sb = new StringBuilder();
        for (String s : countriesOwned) {
            sb.append(s).append(": ").append(Countries.getCountry(s).getArmies()).append('\n');
        }
        return sb.toString();
    }

    public void removeCountry(String countryName) {
        countriesOwned.remove(countryName);
    }

    public void addCountry(String countryName) {
        countriesOwned.add(countryName);
    }

    /**
     * Number of armies to add at start of turn.
     * Is equal to countries divided by 3 rounded down.
     *
     * @return number of arimes to add
     */
    public int newArmiesOnTurn() {
        return Math.max(3, countriesOwned.size() / 3);
    }

    public boolean addArmies(String countryName, int numArmies) {
        Country country = Countries.getCountry(countryName);

        if (country.getPlayer() != this)
            return false;

        country.addArmies(numArmies);

        return true;
    }

    public boolean ownsCountry(String country) {
        return countriesOwned.contains(country);
    }

    /**
     * @return true if player has lost the game.
     */
    public boolean hasLost() {
        return lost;
    }

    /**
     * Set the player as lost.
     */
    public void setLost() {
        this.lost = true;
    }


}
