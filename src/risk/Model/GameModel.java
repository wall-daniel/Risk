package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import risk.Action.Action;
import risk.Action.Attack;
import risk.Action.End;
import risk.Action.Fortify;
import risk.Enums.PlayerType;
import risk.Enums.StringGlobals;
import risk.Players.AIPlayer;
import risk.Players.HumanPlayer;
import risk.Players.Player;
import risk.Players.RandomPlayer;
import risk.View.Main.MainGUI;
import risk.View.Views.GameActionListener;

import javax.swing.*;

import java.io.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    public enum GameStatus {
        TROOP_PLACEMENT_PHASE,
        SELECT_ATTACKING_PHASE,
        SELECT_DEFENDING_PHASE,
        SELECT_TROOP_MOVING_FROM_PHASE,
        SELECT_TROOP_MOVING_TO_PHASE
    }

    private List<Player> players;
    protected final HashMap<String, Continent> continents;
    protected final HashMap<String, Country> countries;
    private final List<GameActionListener> gameActionListeners;

    private int currentPlayer = 0;

    public GameStatus gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;

    public String mapName;
    /**
     * Used for new/existing map in editor
     */
    public GameModel() {
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        players = new ArrayList<>();

        // Add a few players
        players.add(new HumanPlayer("human", 0, this));
        players.add(new HumanPlayer("random", 1, this));
        players.add(new HumanPlayer("ai", 2, this));
    }

    /**
     * Used for loading saved game
     */
    public GameModel(String filename) throws FileNotFoundException {
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();

        players = new ArrayList<>();
        JsonObject savedGame = JsonParser.parseReader(new FileReader(StringGlobals.savesFolder + filename)).getAsJsonObject();
        this.mapName = StringGlobals.mapsFolder + savedGame.get(StringGlobals.gamemodelMapLocation).getAsString();

        loadMap(JsonParser.parseReader(new FileReader(mapName)).getAsJsonArray());
        loadGameState(savedGame);

        currentPlayer = savedGame.get(StringGlobals.gamemodelCurrentPlayer).getAsInt();
        gameStatus = GameStatus.valueOf(savedGame.get(StringGlobals.gamemodelPhase).getAsString());

    }

    /**
     * Used for new game.
     *
     * @param numPlayers number of players that are playing the game
     * @throws FileNotFoundException when file is not found
     */
    public GameModel(String mapName, int numPlayers, String[] playerNames, PlayerType[] playerTypes) throws FileNotFoundException {
        players = new ArrayList<>(numPlayers);
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        this.mapName = mapName;

        // Add the players
        for (int i = 0; i < numPlayers; i++) {
            if (playerTypes[i] == PlayerType.AI_PLAYER)
                players.add(new AIPlayer(playerNames[i], i, playerTypes[i], this));
            else if (playerTypes[i] == PlayerType.HUMAN_PLAYER)
                players.add(new HumanPlayer(playerNames[i], i, this));
            else if (playerTypes[i] == PlayerType.RANDOM_PLAYER)
                players.add(new RandomPlayer(playerNames[i], i, this));
        }

        // Load the map
        loadMap(JsonParser.parseReader(new FileReader(StringGlobals.mapsFolder + mapName)).getAsJsonArray());

        // Setup the map with players
        setupMap();
    }


    /**
     * @param json contains the continents and countries for the map
     */
    protected void loadMap(JsonArray json) {
        // Loop through every continent
        for (int i = 0; i < json.size(); i++) {
            // The continent json contains the countries in it.
            Continent continent = new Continent(json.get(i).getAsJsonObject());
            continents.put(continent.getName(), continent);

            for (Country country : continent.getCountries()) {
                countries.put(country.getName(), country);
            }
        }
    }

    public void addContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
    }

    private void loadGameState(JsonObject savedGame) {
        savedGame.get(StringGlobals.gamemodelPlayers).getAsJsonArray().forEach(e -> {
            switch (PlayerType.valueOf(e.getAsJsonObject().get(StringGlobals.playerType).getAsString())) {
                case AI_PLAYER:
                    players.add(new AIPlayer(this, e.getAsJsonObject()));
                    break;
                case HUMAN_PLAYER:
                    players.add(new HumanPlayer(this, e.getAsJsonObject()));
                    break;
                case RANDOM_PLAYER:
                    players.add(new RandomPlayer(this, e.getAsJsonObject()));
                    break;
            }
        });
    }

    public void saveMap(String filename) throws IOException {
        if (!mapIsValid())
            return;

        JsonArray json = new JsonArray();

        this.continents.values().forEach(continent -> json.add(continent.toJson()));

        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(json.toString());
        fileWriter.flush();
        fileWriter.close();
    }


    private void setupMap() {
        int currentPlayer = 0;
        int initArmies = getInitialArmies(players.size());

        //Random allocation of countries to each player
        ArrayList<String> tempCountryNames = getCountriesNames();
        Random rand = new Random(System.currentTimeMillis());

        while (!tempCountryNames.isEmpty()) {
            String countryName = tempCountryNames.remove(rand.nextInt(tempCountryNames.size()));
            getCountry(countryName).setPlayer(players.get(currentPlayer), 1);
            currentPlayer = (currentPlayer + 1) % players.size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player p : players) {
            ArrayList<String> countriesOwned = p.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                getCountry(countriesOwned.get(rand.nextInt(countriesOwned.size()))).addArmies(1);
                currentArmies -= 1;
            }
        }
    }

    public boolean mapIsValid(){
        String errorMessage = "";
        boolean valid = true;

        //minimum 6 countries
        if (countries.size() < 6){
            errorMessage+="Needs at least 6 countries\n";
            valid = false;
        }

        //every country has a continent
        for (Country country : countries.values())
            if (country.getContinent()==null){
                errorMessage+= country.getName() + " needs a continent assigned to it\n";
                valid = false;
            }

        //every country is connected to other country
        for (Country country : countries.values()){
            HashMap<String, Country> ss = new HashMap<>();
            ss.put(country.getName(), country);
            ArrayList<String> list = getCountriesConnected(country, ss);
            if (list.size() < countries.size()) {
                errorMessage+= country.getName() + " is not connected to every country\n";
                valid = false;
            }
        }

        if (!valid){
            String finalErrorMessage = errorMessage;
            gameActionListeners.forEach(it -> it.displayMessage(finalErrorMessage));
        }

        return valid;
    }

    public ArrayList<String> getCountriesConnected(Country currentCountry, HashMap<String, Country> ss){
        for (String countryName : currentCountry.getNeighbours()) {
            Country country = getCountry(countryName);
            if (!ss.containsKey(countryName)) {
                ss.put(countryName, country);
                getCountriesConnected(country, ss);
            }
        }

        return new ArrayList<>(ss.keySet());
    }


    private int getInitialArmies(int numPlayers) {
        int INITIAL_ARMY_6_PLAYERS = 50;
        int INITIAL_ARMY_5_PLAYERS = 35;
        int INITIAL_ARMY_4_PLAYERS = 30;
        int INITIAL_ARMY_3_PLAYERS = 25;
        int INITIAL_ARMY_2_PLAYERS = 20;

        switch (numPlayers) {
            case 6:
                return INITIAL_ARMY_6_PLAYERS;
            case 5:
                return INITIAL_ARMY_5_PLAYERS;
            case 4:
                return INITIAL_ARMY_4_PLAYERS;
            case 3:
                return INITIAL_ARMY_3_PLAYERS;
            default:
                return INITIAL_ARMY_2_PLAYERS;

        }
    }

    public void startGame() {
        players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
        updateGame();

        if (getCurrentPlayer().getPlayerType() != PlayerType.HUMAN_PLAYER) {
            SwingUtilities.invokeLater(this::aiGameLoop);
        }
    }

    public void nextTurn() {
        // Find next player who is playing
        int lastPlayer = currentPlayer;
        do {
            currentPlayer = (currentPlayer + 1) % players.size();
            System.out.println("Start of " + players.get(currentPlayer).getName() + " turn.");

            // Check if all players besides one has lost
            if (currentPlayer == lastPlayer) {
                JOptionPane.showMessageDialog(null, "Congrats " + getCurrentPlayer().getName() + ", you have won the game! :D");
                System.exit(0);
                break;
            }
        } while (players.get(currentPlayer).hasLost());
        gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;

        players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));

        // Call this, will automatically get out if not ai player
        if (getCurrentPlayer().getPlayerType() != PlayerType.HUMAN_PLAYER) {
            aiGameLoop();
            nextTurn();
        }
    }

    private void aiGameLoop() {
        while (true) { //if only computer players, then will cause infinite recursion
            Action action = getCurrentPlayer().getAction();
            sleep(1);

            if (action instanceof End) {
                return;
            } else {
                doAction(action);
            }
        }
    }

    public void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * controller performs action, then game moves to next phase or next turn
     *
     * @param action to be completed
     */
    public void doAction(Action action) {
        System.out.println(action);
        action.doAction(this);
    }

    public boolean donePlacingArmies() {
        return getCurrentPlayer().getPlaceableArmies() <= 0;
    }

    public Country getCountry(String countryName) {
        return countries.get(countryName);
    }

    public int[] getCurrentNeighboursOfCountry(Country country) {
        int[] arr = new int[country.getNeighbours().size()];
        int i = 0;
        ArrayList<String> countryNames = getCountriesNames();

        for (String s : country.getNeighbours())
            arr[i++] = countryNames.indexOf(s);

        return arr;
    }

    public ArrayList<Country> getCountries() {
        return new ArrayList<>(countries.values());
    }

    public ArrayList<Country> getCountriesInLayerOrder() {
        ArrayList<Country> countriesLayerSort = getCountries();
        countriesLayerSort.sort(Comparator.comparingInt(Country::getLayer));
        Collections.reverse(countriesLayerSort);
        return countriesLayerSort;
    }

    public ArrayList<String> getCountriesNames() {
        return new ArrayList<>(countries.keySet());
    }


    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public Continent getContinent(String name) {
        return continents.get(name);
    }

    public ArrayList<Continent> getContinents() {
        return new ArrayList<>(continents.values());
    }

    /**
     * Get the continent bonus for the player at the start of the turn.
     *
     * @param p is player you want to check
     * @return the bonus gotten
     */
    public int getContinentBonuses(Player p) {
        int bonus = 0;
        for (Continent continent : continents.values()) {
            boolean allCountries = true;
            for (Country country : continent.getCountries()) {
                if (country.getPlayer() != p) {
                    allCountries = false;
                    break;
                }
            }

            if (allCountries) {
                bonus += continent.getBonus();
            }
        }

        return bonus;
    }

    public void addActionListener(GameActionListener listener) {
        gameActionListeners.add(listener);
    }


    public void updateEditor() {
        gameActionListeners.forEach(it -> it.updateMap(this));
    }

    public void updateGame() {
        resetClickableCountries();
        if (getCurrentPlayer().getPlayerType() == PlayerType.HUMAN_PLAYER)
            updateClickableCountries();
        gameActionListeners.forEach(it -> it.updateMap(this));
    }

    public void updateClickableCountries() {
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
            case SELECT_ATTACKING_PHASE:
            case SELECT_TROOP_MOVING_FROM_PHASE:
                updatePlaceableCountries();
                break;
            case SELECT_DEFENDING_PHASE:
                updateAttackableCountries(((Attack) getCurrentPlayer().getAction()).getAttackingCountry());
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                updateMoveTroopsToCountries(((Fortify) getCurrentPlayer().getAction()).getFirstCountry());
                break;
        }
    }

    private void resetClickableCountries() {
        for (Country country : countries.values())
            country.resetClickable();
    }


    //for placing troops, selecting attacking from, selecting moving from country
    public void updatePlaceableCountries() {
        for (String s : new ArrayList<>(players.get(currentPlayer).getCountries()))
            countries.get(s).setClickable();
    }

    //for choosing defendingCountry
    public void updateAttackableCountries(Country fromCountry) {
        for (String s : fromCountry.getNeighbours().stream().filter(e ->
                fromCountry.getPlayer().getIndex() != countries.get(e).getPlayer().getIndex()
        ).collect(Collectors.toCollection(ArrayList::new)))
            countries.get(s).setClickable();
    }

    //for choosing toCountry
    public void updateMoveTroopsToCountries(Country fromCountry) {
        HashMap<String, Country> ss = new HashMap<>();
        ss.put(fromCountry.getName(), fromCountry);
        ArrayList<String> list = addCountries(fromCountry, ss, fromCountry.getPlayer().getIndex());
        list.remove(fromCountry.getName());
        for (String s : list)
            countries.get(s).setClickable();
    }

    /**
     * Recursively gets all connected countries for fortification.
     *
     * @param currentCountry, country moving troops from
     * @param ss,             the list of countries that are connected
     * @param playerIndex,    current player index
     * @return a list of countries connected
     */
    private ArrayList<String> addCountries(Country currentCountry, HashMap<String, Country> ss, int playerIndex) {
        for (String countryName : currentCountry.getNeighbours()) {
            Country country = getCountry(countryName);
            if (country.getPlayer().getIndex() == playerIndex && !ss.containsKey(countryName)) {
                ss.put(countryName, country);
                addCountries(country, ss, playerIndex);
            }
        }

        return new ArrayList<>(ss.keySet());
    }


    /**
     * the logic when back-button is clicked when in 2nd part of two part phases. It resets to the first part
     */
    public void resetPhase() {
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                break;
            case SELECT_ATTACKING_PHASE:
            case SELECT_DEFENDING_PHASE:
                gameStatus = GameStatus.SELECT_ATTACKING_PHASE;
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                gameStatus = GameStatus.SELECT_TROOP_MOVING_FROM_PHASE;
                break;
        }
        updateGame();
    }

    /**
     * the logic when 1st part of two part phases is completed. Continues the game to the 2nd part
     */
    public void continuePhase() {
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
            case SELECT_DEFENDING_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                break;
            case SELECT_ATTACKING_PHASE:
                gameStatus = GameStatus.SELECT_DEFENDING_PHASE;
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                gameStatus = GameStatus.SELECT_TROOP_MOVING_TO_PHASE;
                break;
        }
        updateGame();
    }

    /**
     * the logic when next-button is clicked. Continues the game to the the next phase
     */
    public void nextPhase() {
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                if (donePlacingArmies())
                    gameStatus = GameStatus.SELECT_ATTACKING_PHASE;
                break;
            case SELECT_ATTACKING_PHASE:
            case SELECT_DEFENDING_PHASE:
                gameStatus = GameStatus.SELECT_TROOP_MOVING_FROM_PHASE;
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                nextTurn();
                break;
        }
        updateGame();
    }

    private JsonObject getGameStateJson() {
        JsonObject obj = new JsonObject();

        JsonArray playerArr = new JsonArray(players.size());
        for (Player player : players) {
            playerArr.add(player.savePlayer());
        }
        obj.add(StringGlobals.gamemodelPlayers, playerArr);

        // Add current player
        obj.addProperty(StringGlobals.gamemodelCurrentPlayer, currentPlayer);
        obj.addProperty(StringGlobals.gamemodelPhase, gameStatus.toString());
        obj.addProperty(StringGlobals.gamemodelMapLocation, this.mapName);

        // Save game phase, but only one of 3 different ones to avoid nulls when loading
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                obj.addProperty(StringGlobals.gamemodelPhase, GameStatus.TROOP_PLACEMENT_PHASE.toString());
                break;
            case SELECT_ATTACKING_PHASE:
            case SELECT_DEFENDING_PHASE:
                obj.addProperty(StringGlobals.gamemodelPhase, GameStatus.SELECT_ATTACKING_PHASE.toString());
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                obj.addProperty(StringGlobals.gamemodelPhase, GameStatus.SELECT_TROOP_MOVING_TO_PHASE.toString());
                break;
        }

        return obj;
    }

    /**
     *
     */
    public void saveGameState(String filename) {
        try {
            FileWriter fileWriter = new FileWriter(new File(StringGlobals.savesFolder +filename+".txt"));
            fileWriter.write(getGameStateJson().toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
