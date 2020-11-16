package risk.Players;

import risk.Action.Action;
import risk.Enums.PlayerColor;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    protected GameModel gameModel;

    private final String name;
    protected ArrayList<String> countriesOwned;
    private boolean lost = false;
    private final Color playerColor;
    private int placeableArmies = 0;
    private int index;

    private PlayerType playerType;

    public Player(String name, int index, PlayerType playerType, GameModel gameModel) {
        this.name = name;
        this.index = index;
        countriesOwned = new ArrayList<>();
        playerColor = PlayerColor.getPlayerColor(index);
        this.playerType = playerType;
        this.gameModel = gameModel;
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

    /**
     * Starts the players turn by adding placeable armies
     * @param continentBonus, bonus from owning all of a continent
     */
    public void startTurn(int continentBonus) {
        // Number of armies is equal to countries divided by 3 rounded down, minimum 3.
        placeableArmies = Math.max(3, countriesOwned.size() / 3) + continentBonus;
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

    public Action getAction(){
        return null;
    }

    public PlayerType getPlayerType(){
        return playerType;
    }

}
