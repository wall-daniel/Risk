package risk.Model;

import com.google.gson.JsonArray;
import risk.Listener.Listeners.GameModelListener;
import risk.Players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModel {

    private List<Player> players;
    private Map<String, Continent> continents;
    private List<Country> countries;
    private List<GameModelListener> listeners;

    private int currentPlayer = 0;
    private boolean isGameOver = false;

    public GameModel(JsonArray json) {
        players = new ArrayList<>();
        continents = new HashMap<>();
        countries = new ArrayList<>();
        listeners = new ArrayList<>();

        loadMap(json);
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
            countries.addAll(continent.getCountries());
        }
    }

    public void addListener(GameModelListener listener) {
        listeners.add(listener);
    }
}
