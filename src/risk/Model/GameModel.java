package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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
import java.util.List;

public class GameModel {

    private List<Player> players;
    private List<Continent> continents;
    private List<Country> countries;
    private List<GameActionListener> gameActionListeners;
    private List<GameModelListener> gameModelListeners;

    private int currentPlayer = 0;
    private boolean isGameOver = false;

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

        // Add the players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + i));
        }

        // Load the map
        loadMap(JsonParser.parseReader(new FileReader("map.txt")).getAsJsonArray());
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

    public void addActionListener(GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    public void addGameModelListener(GameModelListener listener) {
        gameModelListeners.add(listener);
    }

    public void updateGame() {
//        listeners.forEach(it -> it.);
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
}
