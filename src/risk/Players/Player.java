package risk.Players;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import risk.Action.Action;
import risk.Action.ActionBuilder;
import risk.Enums.PlayerColor;
import risk.Enums.PlayerType;
import risk.Enums.StringGlobals;
import risk.Model.Country;
import risk.Model.GameModel;

import java.awt.*;
import java.util.ArrayList;

public abstract class Player {
    protected GameModel gameModel;

    private final String name;
    protected ArrayList<String> countriesOwned;
    private final Color playerColor;
    private int placeableArmies = 0;
    private int index;

    private PlayerType playerType;

    protected ActionBuilder actionBuilder;

    public Player(String name, int index, PlayerType playerType, GameModel gameModel) {
        this.name = name;
        this.index = index;
        countriesOwned = new ArrayList<>();
        playerColor = PlayerColor.getPlayerColor(index);
        this.playerType = playerType;
        this.gameModel = gameModel;
        actionBuilder = new ActionBuilder();
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

    /**
     * Choose two countries to move armies from and to.
     * Make sure that the countries have a path between them.
     * The number of armies is between 1 and first country armies minus 1.
     *
     * @return a movement action
     */
    protected ActionBuilder getEndCommand() {
        return new ActionBuilder();
    }

    public int getTotalArmies(){
        int totalArmies = 0;
        for (String countryName : this.getCountries()){
            Country playerCountry = gameModel.getCountry(countryName);
            totalArmies += playerCountry.getArmies();
        }
        return totalArmies;
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

    public boolean ownsCountry(Country country) {
        return countriesOwned.contains(country.getName());
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
     * @return true if player has lost the game (e.g. has no countries).
     */
    public boolean hasLost() {
        return countriesOwned.isEmpty();
    }

    public int getIndex() {
        return index;
    }


    public ActionBuilder getActionBuilder(){
        return actionBuilder;
    }


    public abstract void inputTroopCount(String msg, int min, int max);

    public void setFirstCountryOfAction(Country country){
        actionBuilder.setFirstCountry(country);
    }

    public Country getFirstCountryOfAction(){
        return actionBuilder.getFirstCountry();
    }


    public void setSecondCountryOfAction(Country country){
        actionBuilder.setSecondCountry(country);
    }

    public void setNumTroopsOfAction(int numTroopsOfAction){
        actionBuilder.setNumTroops(numTroopsOfAction);
    }

    public PlayerType getPlayerType(){
        return playerType;
    }


    /**
     * Handle inputs
     * @return
     */
    public abstract Action getAction();

    public JsonObject savePlayer() {
        JsonObject player = new JsonObject();

        // Save some fields
        player.addProperty(StringGlobals.playerName, name);
        player.addProperty(StringGlobals.playerIndex, index);
        player.addProperty(StringGlobals.playerType, playerType.toString());

        // Save countries owned
        JsonArray countryArray = new JsonArray();
        for (String countryString : countriesOwned) {
            Country country = gameModel.getCountry(countryString);

            // Save country name and number of armies, the owner is contained by player
            JsonObject countryObj = new JsonObject();
            countryObj.addProperty(StringGlobals.countryName, countryString);
            countryObj.addProperty(StringGlobals.countryArmies, country.getArmies());

            countryArray.add(countryObj);
        }
        player.add(StringGlobals.countries, countryArray);

        return player;
    }

    public Player(JsonObject playerObj, GameModel gameModel) {
        this.gameModel = gameModel;
        name = playerObj.get(StringGlobals.playerName).getAsString();
        index = playerObj.get(StringGlobals.playerIndex).getAsInt();
        playerType = PlayerType.valueOf(playerObj.get(StringGlobals.playerType).getAsString());
        playerColor = PlayerColor.getPlayerColor(index);

        JsonArray countries = playerObj.get(StringGlobals.countries).getAsJsonArray();
        countriesOwned = new ArrayList<>(countries.size());
        actionBuilder = new ActionBuilder();

        // Add each country to player and set country player and num armies.
        for (JsonElement element : countries) {
            JsonObject countryObj = element.getAsJsonObject();

            Country country = gameModel.getCountry(countryObj.get(StringGlobals.countryName).getAsString());
            country.setPlayer(this, countryObj.get(StringGlobals.countryArmies).getAsInt());
        }
    }

}
