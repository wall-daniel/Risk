package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import risk.Players.Player;

import java.awt.*;
import java.util.ArrayList;

public class Country {

    private final String name;
    private int numArmies = 1;
    private Player controlledBy = null;

    private ArrayList<String> neighbourNames;
    private Continent continent;

    private Polygon polygon;

    public Country(String name) {
        this.name = name;
        neighbourNames = new ArrayList<>();
    }

    public Country(String name, ArrayList<String> neighbourNames, Continent continent) {
        this.name = name;
        this.neighbourNames = (ArrayList<String>) neighbourNames.clone();
        this.continent = continent;
    }

    public Polygon getPolygon(){
        return polygon;
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

    public void setContinent(Continent continent){
        this.continent = continent;
    }

    public Continent getContinent(){
        return continent;
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

        // Add the points
        JsonArray pointArr = new JsonArray();
        for (int i = 0; i < polygon.npoints; i++) {
            JsonObject point = new JsonObject();
            point.addProperty("x", polygon.xpoints[i]);
            point.addProperty("y", polygon.ypoints[i]);
            pointArr.add(point);
        }
        json.add("points", pointArr);

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
        JsonArray arr = json.get("neighbours").getAsJsonArray();
        this.neighbourNames = new ArrayList<>(arr.size());
        arr.forEach((item) -> neighbourNames.add(item.getAsString()));

        // Get the points
        Polygon polygon = new Polygon();
        JsonArray pointArr = json.get("points").getAsJsonArray();
        for (int i = 0; i < pointArr.size(); i++) {
            JsonObject point = pointArr.get(i).getAsJsonObject();
            polygon.addPoint(point.get("x").getAsInt(), point.get("y").getAsInt());
        }
    }
}
