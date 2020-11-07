package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import risk.Controller.Controller;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Listeners.GameActionListener;
import risk.Listener.Listeners.GameModelListener;
import risk.Players.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModel {

    public enum GameStatus { TROOP_PLACEMENT_PHASE, SELECT_ATTACKING_PHASE, SELECT_DEFENDING_PHASE, SELECT_TROOP_MOVING_FROM_PHASE, SELECT_TROOP_MOVING_TO_PHASE, WAITING }

    private List<Player> players;
    private List<Continent> continents;
    private List<Country> countries;
    private List<GameActionListener> gameActionListeners;
    private List<GameModelListener> gameModelListeners;

    private int currentPlayer = 0;
    private boolean isGameOver = false;

    public GameStatus gameStatus = GameStatus.TROOP_PLACEMENT_PHASE;

    /**
     * Used for the editor
     */
    public GameModel() {
        continents = new ArrayList<>();
        countries = new ArrayList<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();
    }

    /**
     * Used for the game.
     *
     * @param numPlayers
     * @throws FileNotFoundException
     */
    public GameModel(int numPlayers) throws FileNotFoundException {
        players = new ArrayList<>(numPlayers);
        continents = new ArrayList<>();
        countries = new ArrayList<>();
        gameActionListeners = new ArrayList<>();
        gameModelListeners = new ArrayList<>();

        // Add the players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + i));
        }

        // Load the map
        loadMap(JsonParser.parseReader(new FileReader("map.txt")).getAsJsonArray());

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
            continents.add(continent);
            countries.addAll(continent.getCountries());
        }
    }

    private void setupMap() {
        // Choose player that owns each country
        // TODO make random
        int currentPlayer = 0;
        for (Country c : countries) {
            c.setPlayer(players.get(0));
            currentPlayer = (currentPlayer + 1) % players.size();
            c.addArmies(1);
        }

        // TODO Setup the armies
        for (Player p : players) {

        }
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
        this.countries.add(country);
        gameModelListeners.forEach(it -> it.onNewCountry(new OneCountryEvent(this, country)));
    }

    public void addContinent(Continent continent) {
        this.continents.add(continent);
        gameModelListeners.forEach(it -> it.onNewContinent(new ContinentEvent(this, continent)));
    }

    public void saveMap(String filename) throws IOException {
        JsonArray json = new JsonArray();

        this.continents.forEach(continent -> json.add(continent.toJson()));

        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(json.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public List<Country> getCountries() {
        return new ArrayList<>(countries);
    }

    public void editCountry(Country country, ArrayList<String> names, Continent continent) {
        country.setContinent(continent);
        country.setNeighbourNames(names);

        gameModelListeners.forEach(it -> it.onNewCountry(new OneCountryEvent(this, country)));
    }

    public boolean placeTroops(Country country, int armies) {
        return players.get(currentPlayer).placeArmies(country, armies);
    }
}
