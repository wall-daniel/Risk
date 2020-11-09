package risk.Players;

import risk.Enums.PlayerColor;
import risk.Model.Country;

import java.awt.*;
import java.util.ArrayList;

public class Player {

    private final String name;
    private ArrayList<String> countriesOwned;
    private boolean lost = false;
    private final Color playerColor;
    private int placeableArmies = 0;
    private int index;
    private boolean playingTurn = false;

    public Player(String name, int index) {
        this.name = name;
        this.index = index;
        countriesOwned = new ArrayList<>();
        playerColor = PlayerColor.getPlayerColor(index);
    }

    public Color getPlayerColor() {
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

    public void endTurn() {
        playingTurn = false;
    }

    /**
     * Starts the players turn by adding placeable armies
     * @param continentBonus, bonus from owning all of a continent
     */
    public void startTurn(int continentBonus) {
        playingTurn = true;

        // Number of armies is equal to countries divided by 3 rounded down, minimum 3.
        placeableArmies = Math.max(3, countriesOwned.size() / 3) + continentBonus;
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

    /**
     * Place armies on country during army placement at start of turn.
     *
     * @param country, country for armies to be placed on.
     * @param armies, number armies, cannot be larger than current placeable armies
     */
    public void placeArmies(Country country, int armies) {
        if (placeableArmies >= armies) {
            country.addArmies(armies);
            placeableArmies -= armies;
        }
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


    public int getIndex() {
        return index;
    }
}
