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

    private ArrayList<String> neighbours;
    private Continent continent;

    private Polygon polygon;

    public Country(String name, Polygon polygon) {
        this.name = name;
        this.polygon = polygon;
        neighbours = new ArrayList<>();
    }

    public Country(String name, ArrayList<String> neighbourNames, Continent continent) {
        this.name = name;
        for (String neighbourName: neighbourNames){

        }


        this.neighbours = (ArrayList<String>) neighbourNames.clone();
        this.continent = continent;
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
        this.neighbours = new ArrayList<>(arr.size());
        arr.forEach((item) -> neighbours.add(item.getAsString()));

        // Get the points
        polygon = new Polygon();
        JsonArray pointArr = json.get("points").getAsJsonArray();
        for (int i = 0; i < pointArr.size(); i++) {
            JsonObject point = pointArr.get(i).getAsJsonObject();
            polygon.addPoint(point.get("x").getAsInt(), point.get("y").getAsInt());
        }
    }

    public Polygon getPolygon(){
        return polygon;
    }



    public void setContinent(Continent continent){
        if (this.continent != null) {
            this.continent.removeCountry(this);
        }

        this.continent = continent;

        if (this.continent != null) {
            this.continent.addCountry(this);
        }
    }

    public Continent getContinent(){
        return continent;
    }

    public ArrayList<String> getNeighbours() {
        return neighbours;
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
        if (this.numArmies < 1) {
            return;
//            throw new ArithmeticException("You cannot have less that 1 army");
        }

        this.numArmies -= armies;
    }

    public void setNeighbourNames(ArrayList<String> neighbourNames) {
        this.neighbours = neighbourNames;
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
        neighbours.forEach(arr::add);
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

    public boolean isNeighbour(Country country) {
        for (String s : neighbours) {
            if (s.equals(country.getName())) {
                return true;
            }
        }

        return false;
    }
}
