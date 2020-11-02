package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.xml.internal.bind.v2.TODO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import risk.Players.Player;

import java.util.ArrayList;

public class Country {

    private final String name;
    private int numArmies = 1;
    private Player controlledBy = null;

    private ArrayList<String> neighbourNames;
    private String continentName = "";

    public Country(String name) {
        this.name = name;
        neighbourNames = new ArrayList<>();
    }

    public Country(String name, ArrayList<String> neighbourNames, String continentName) {
        this.name = name;
        this.neighbourNames = (ArrayList<String>) neighbourNames.clone();
        this.continentName = continentName;
    }

//    public boolean canAttack() { return numArmies > 1; }

    public void addNeighbour(String name, Country country){
        neighbourNames.add(name);
    }

    public int[] getNeighbours(){
        if (neighbourNames.isEmpty())
            return new int[0];

        int [] neighbourIndicies = new int[neighbourNames.size()];
        int i = 0;
        for (String s : neighbourNames)
            neighbourIndicies[i++] = Countries.getIndexOfCountry(s);
        return neighbourIndicies;
    }

    public void setContinentName(String name){
        continentName = name;
    }

    public String getContinentName(){
        return continentName;
    }

    public ArrayList<String> getNeighbourNames() {
        return neighbourNames;
    }

    public Player getPlayer() {
        return controlledBy;
    }

    public String getName() {
        return name;
    }

    public void setPlayer(Player p) {
        setPlayer(p, 1);
    }

    public void setPlayer(Player p, int numArmies) {
        // Remove current player from controlling it
        if (controlledBy != null) {
            this.controlledBy.removeCountry(name);
        }

        this.controlledBy = p;
        this.numArmies = numArmies;
        p.addCountry(name);
    }

    public void addArmies(int newArmies) {
        this.numArmies += newArmies;
    }

    public int getArmies() {
        return numArmies;
    }

    public void removeArmies(int armies) {
        if (this.numArmies - armies < 1) {
            throw new ArithmeticException("You cannot have less that 1 army");
        }

        this.numArmies -= armies;
    }

    public void setNeighbours(ArrayList<String> neighbourNames) {
        this.neighbourNames = (ArrayList<String>) neighbourNames.clone();
    }

    public String toString(){
        return name;
    }

    /**
     * @return the json representation of the country which has name, neighbours, and the coordinates of points.
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        // Add country name
        json.addProperty("name", name);

        // Add the neighbours
        JsonArray arr = new JsonArray();
        neighbourNames.forEach(arr::add);
        json.add("neighbours", arr);

        return json;
    }

    /**
     * Contructor from json object.
     *
     * @param json, contains info about the name, neighoburs, and the polygon points
     */
    public Country(JsonObject json) {
        this.name = json.get("name").getAsString();

        // Get the neighbours
        JsonArray arr = json.getAsJsonArray();
        this.neighbourNames = new ArrayList<>(arr.size());
        arr.forEach((item) -> neighbourNames.add(item.getAsString()));

        // Get the points
        // TODO
    }
}
