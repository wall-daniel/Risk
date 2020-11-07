package risk.Players;

import risk.Enums.PlayerColor;
import risk.Model.Country;
import risk.Model.Countries;

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<String> countriesOwned;
    private boolean lost = false;
    private PlayerColor playerColor;
    private int placeableArmies = 0;

    public Player(String name) {
        this.name = name;
        countriesOwned = new ArrayList<>();
        playerColor = PlayerColor.PLAYER_1_COLOR; //TODO must change so each player gets distinct color
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getCountries() {
        return countriesOwned;
    }

    public void removeCountry(String countryName) {
        countriesOwned.remove(countryName);
    }

    public void addCountry(String countryName) {
        countriesOwned.add(countryName);
    }

    private void startTurn() {
        // Number of armies is equal to countries divided by 3 rounded down, minimum 3.
        placeableArmies = Math.max(3, countriesOwned.size() / 3);
    }

    public boolean addArmies(Country country, int numArmies) {
        if (country.getPlayer() == this) {
            country.addArmies(numArmies);
            return true;
        }

        return false;
    }

    public int getPlaceableArmies() {
        return placeableArmies;
    }

    public boolean placeArmies(Country country, int armies) {
        if (placeableArmies >= armies) {
            if (addArmies(country, armies)) {
                placeableArmies -= armies;
                return true;
            }
        }

        return false;
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
