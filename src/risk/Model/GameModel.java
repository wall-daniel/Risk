package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.OneCountryEvent;
import risk.View.Views.GameActionListener;
import risk.View.Views.GameModelListener;
import risk.Players.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class GameModel {
    public void updateEditor() {
        gameModelListeners.forEach(it -> {
            countries.values().forEach(country -> it.onNewCountry(new OneCountryEvent(this, country)));
            continents.values().forEach(continent -> it.onNewContinent(new ContinentEvent(this, continent)));
        });
    }

    public enum GameStatus { TROOP_PLACEMENT_PHASE, SELECT_ATTACKING_PHASE, SELECT_DEFENDING_PHASE, SELECT_TROOP_MOVING_FROM_PHASE, SELECT_TROOP_MOVING_TO_PHASE, WAITING }

    private List<Player> players;
    private HashMap<String, Continent> continents;
    private HashMap<String, Country> countries;
    private List<GameActionListener> gameActionListeners;
    private List<GameModelListener> gameModelListeners;

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
     * @param numPlayers
     * @throws FileNotFoundException
     */
    public GameModel(int numPlayers) throws FileNotFoundException {
        players = new ArrayList<>(numPlayers);
        continents = new HashMap<>();
        countries = new HashMap<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();

        // Add the players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + i, i));
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

    private void setupMap() {
        int currentPlayer = 0;
        int initArmies = getInitialArmies(players.size());

        //Random allocation of countries to each player
        ArrayList<String> tempCountryNames = getCountriesNames();
        Random rand = new Random(System.currentTimeMillis());

        while (!tempCountryNames.isEmpty()){
            String countryName = tempCountryNames.remove(rand.nextInt(tempCountryNames.size()));
            getCountry(countryName).setPlayer(players.get(currentPlayer));
            currentPlayer= (currentPlayer + 1) % players.size();
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

        this.currentPlayer = 0;
    }

    private int getInitialArmies(int size) {
        //TODO magic numbers fix
        HashMap<Integer, Integer> initialArmySizes = new HashMap<>();
        initialArmySizes.put(2, 50);
        initialArmySizes.put(3, 35);
        initialArmySizes.put(4, 30);
        initialArmySizes.put(5, 25);
        initialArmySizes.put(6, 20);
        return initialArmySizes.get(size);
    }


    public int[] getCurrentNeighboursOfCountry(Country country){
        int[] arr = new int[country.getNeighbours().size()];
        int i = 0;
        ArrayList<String> countryNames = getCountriesNames();

        for (String s: country.getNeighbours())
            arr[i++] = countryNames.indexOf(s);

        return arr;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addActionListener(GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    public void addGameModelListener(GameModelListener listener) {
        gameModelListeners.add(listener);
    }

    public void updateGame() {
        gameActionListeners.forEach(it -> it.updateMap(this));
    }

    public void addCountry(Country country) {
        this.countries.put(country.getName(), country);
        gameModelListeners.forEach(it -> it.onNewCountry(new OneCountryEvent(this, country)));
    }

    public Country getCountry(String countryName){
        return countries.get(countryName);
    }

    public void addContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
        gameModelListeners.forEach(it -> it.onNewContinent(new ContinentEvent(this, continent)));
    }

    public void saveMap(String filename) throws IOException {
        JsonArray json = new JsonArray();

        this.continents.values().forEach(continent -> json.add(continent.toJson()));

        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(json.toString());
        fileWriter.flush();
        fileWriter.close();
    }





    //for placing troops, selecting attacking from, selecting moving from country
    public ArrayList<String> getPlaceableCountries(){
        return new ArrayList<>(players.get(currentPlayer).getCountries());
    }

    //for choosing defendingCountry
    public ArrayList<String> getAttackableCountries(Country fromCountry){
        return new ArrayList<>(fromCountry.getNeighbours().stream().filter(e->
            fromCountry.getPlayer().getIndex()!=countries.get(e).getPlayer().getIndex()
        ).collect(Collectors.toList()));
    }

    //for choosing toCountry
    public ArrayList<String> getMoveTroopsToCountries(Country fromCountry){
        HashMap<String, Country> ss = new HashMap<>();
        //ss.put(fromCountry.getName(), fromCountry);
        return addCountries(fromCountry, ss, fromCountry.getPlayer().getIndex());
    }

    private ArrayList<String> addCountries(Country currentCountry, HashMap<String, Country> ss, int playerIndex) {
        for (String countryName : currentCountry.getNeighbours()){
            Country country = getCountry(countryName);
            if (country.getPlayer().getIndex() == playerIndex && !ss.containsKey(countryName)){
                ss.put(countryName, country);
                addCountries(country, ss, playerIndex);
            }
        }
        return new ArrayList<>(ss.keySet());
    }

    public Continent getContinent(String name){
        return continents.get(name);
    }

    public ArrayList<Continent>getContinents() {
        return new ArrayList<>(continents.values());
    }

    public ArrayList<Country> getCountries() {
        return new ArrayList<>(countries.values());
    }

    public ArrayList<Country> getCountriesInLayerOrder(){
        ArrayList<Country> countriesLayerSort = getCountries();
        countriesLayerSort.sort(Comparator.comparingInt(Country::getLayer));
        Collections.reverse(countriesLayerSort);
        return countriesLayerSort;
    }

    public ArrayList<String> getCountriesNames(){
        return new ArrayList<>(countries.keySet());
    }

    public void editCountry(Country country, ArrayList<String> names, Continent continent) {
        country.setContinent(continent);
        country.setNeighbourNames(names);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public void placeArmies(Country country, int armies) {
        getCurrentPlayer().placeArmies(country, armies);
        updateGame();
    }

    public void startGame() {
        initializeGame();
        updateGame();
    }

    private void initializeGame() {
        currentPlayer = 0;
        gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;
        players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
    }

    public void nextTurn() {
        getCurrentPlayer().endTurn();
        currentPlayer = (currentPlayer + 1) % players.size();
        System.out.println("Start of " + players.get(currentPlayer).getName() + " turn.");
        gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;

        if (players.get(currentPlayer).hasLost()) {
            nextTurn();
        } else {
            players.get(currentPlayer).startTurn(getContinentBonuses(players.get(currentPlayer)));
        }
    }

    public boolean donePlacingArmies() {
        return getCurrentPlayer().getPlaceableArmies() <= 0;
    }

    public void nextPhase() {
        switch (gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                gameStatus = GameStatus.SELECT_ATTACKING_PHASE;
                break;
            case SELECT_ATTACKING_PHASE:
                gameStatus = GameStatus.SELECT_DEFENDING_PHASE;
                break;
            case SELECT_DEFENDING_PHASE:
                gameStatus = GameStatus.SELECT_ATTACKING_PHASE;
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                gameStatus = GameStatus.SELECT_TROOP_MOVING_TO_PHASE;
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                nextTurn();
                break;
        }

        for (Country country : countries.values())
            System.out.println(country);


        updateGame();
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

    public void startEndTurn() {
        gameStatus = GameStatus.SELECT_TROOP_MOVING_FROM_PHASE;
    }
}
