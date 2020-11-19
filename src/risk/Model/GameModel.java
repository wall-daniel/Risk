package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import risk.Action.*;
import risk.Action.Action;
import risk.Controller.AttackController;
import risk.Controller.DeployController;
import risk.Controller.FortifyController;
import risk.Enums.PlayerType;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.CountryEvent;
import risk.Players.AIPlayer;
import risk.Players.HumanPlayer;
import risk.Players.Player;
import risk.Players.RandomPlayer;
import risk.View.Views.GameActionListener;
import risk.View.Views.GameModelListener;

import javax.swing.*;
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
    private final HashMap<String, Continent> continents;
    private final HashMap<String, Country> countries;
    private final List<GameActionListener> gameActionListeners;
    private final List<GameModelListener> gameModelListeners;

    private int currentPlayer = 0;
    private boolean isGameOver = false;

    public GameStatus gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;


    /**
     * Used for the editor
     */
    public GameModel() {
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();
    }

    /**
     * Used for the editor
     */
    public GameModel(String filename) throws FileNotFoundException {
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();

        loadMap(JsonParser.parseReader(new FileReader(filename)).getAsJsonArray());
    }

    /**
     * Used for the game.
     *
     * @param numPlayers number of players that are playing the game
     * @throws FileNotFoundException when file is not found
     */
    public GameModel(int numPlayers, String[] playerNames, PlayerType[] playerTypes) throws FileNotFoundException {
        players = new ArrayList<>(numPlayers);
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();

        // Add the players
        for (int i = 0; i < numPlayers; i++) {
            if (playerTypes[i] == PlayerType.AI_PLAYER)
                players.add(new AIPlayer(playerNames[i], i, playerTypes[i], this));
            else if (playerTypes[i] == PlayerType.HUMAN_PLAYER)
                players.add(new HumanPlayer(playerNames[i], i, playerTypes[i], this));
            else if (playerTypes[i] == PlayerType.RANDOM_PLAYER)
                players.add(new RandomPlayer(playerNames[i], i, this));
        }

        // Load the map
        loadMap(JsonParser.parseReader(new FileReader("RiskMap.txt")).getAsJsonArray());

        // Setup the map with players
        setupMap();
    }

    /**
     * @param json contains the continents and countries for the map
     */
    private void loadMap(JsonArray json) {
        // Loop through every continent
        for (int i = 0; i < json.size(); i++) {
            // The continent json contains the countries in it.
            Continent continent = new Continent(json.get(i).getAsJsonObject());

            // Add the continent and all the countries
            continents.put(continent.getName(), continent);

            for (Country country : continent.getCountries()) {
                countries.put(country.getName(), country);
            }
        }
    }

    public void saveMap(String filename) throws IOException {
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


    private int getInitialArmies(int numPlayers) {
        int INITIAL_ARMY_6_PLAYERS = 50;
        int INITIAL_ARMY_5_PLAYERS = 35;
        int INITIAL_ARMY_4_PLAYERS = 30;
        int INITIAL_ARMY_3_PLAYERS = 25;
        int INITIAL_ARMY_2_PLAYERS = 20;

        switch(numPlayers){
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
        currentPlayer = 0;
        gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;
        players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
        updateGame();

        SwingUtilities.invokeLater(() -> {
            players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
            while (getCurrentPlayer().getPlayerType() != PlayerType.HUMAN_PLAYER){ //if only computer players, then will cause infinite recursion
                Action action = getCurrentPlayer().getAction();
                sleep(500);
                doAction(action);
            }
        });
    }

    public void nextTurn() {
        currentPlayer = (currentPlayer + 1) % players.size();
        System.out.println("Start of " + players.get(currentPlayer).getName() + " turn.");
        gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;

        if (players.get(currentPlayer).hasLost()) {
            nextTurn();
        } else {
            players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
            while (getCurrentPlayer().getPlayerType() != PlayerType.HUMAN_PLAYER){ //if only computer players, then will cause infinite recursion
                Action action = getCurrentPlayer().getAction();
                sleep(500);
                doAction(action);
            }
        }
    }


    public void sleep(long delay){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * controller performs action, then game moves to next phase or next turn
     * @param action to be completed
     */
    public void doAction(Action action) {
        System.out.println(action);
        action.doAction(this);
    }

    public boolean donePlacingArmies() {
        return getCurrentPlayer().getPlaceableArmies() <= 0;
    }

    public void addCountry(Country country) {
        this.countries.put(country.getName(), country);
        gameModelListeners.forEach(it -> it.onNewCountry(new CountryEvent(this, country)));
    }


    public void editCountry(Country country, ArrayList<String> names, Continent continent) {
        country.setContinent(continent);
        country.setNeighbourNames(names);
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

    public void addContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
        gameModelListeners.forEach(it -> it.onNewContinent(new ContinentEvent(this, continent)));
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

    public void addGameModelListener(GameModelListener listener) {
        gameModelListeners.add(listener);
    }

    public void updateEditor() {
        gameModelListeners.forEach(it -> {
            countries.values().forEach(country -> it.onNewCountry(new CountryEvent(this, country)));
            continents.values().forEach(continent -> it.onNewContinent(new ContinentEvent(this, continent)));
        });
    }


    public void updateGame() {
        resetClickableCountries();
        if (getCurrentPlayer().getPlayerType()==PlayerType.HUMAN_PLAYER)
            updateClickableCountries();
        gameActionListeners.forEach(it -> it.updateMap(this));
    }

    public void updateClickableCountries(){
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
            case SELECT_ATTACKING_PHASE:
            case SELECT_TROOP_MOVING_FROM_PHASE:
                updatePlaceableCountries();
                break;
            case SELECT_DEFENDING_PHASE:
                updateAttackableCountries(((Attack)getCurrentPlayer().getAction()).getAttackingCountry());
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                updateMoveTroopsToCountries(((Fortify)getCurrentPlayer().getAction()).getFirstCountry());
                break;
        }
    }

    private void resetClickableCountries(){
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
    public void resetPhase(){
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
    public void continuePhase(){
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
     * */
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
}
