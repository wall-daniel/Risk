package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import risk.Enums.StringGlobals;
import risk.Players.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Country {

    private String name;
    private int numArmies = 1;
    private Player controlledBy = null;

    private List<String> neighbours;
    private Continent continent;

    private boolean visited;

    private Polygon polygon;

    private Point labelPoint;
    private int layer;
    private Point polygonPoint;

    private boolean clickable;

    public Country(String name, Polygon polygon) {
        this.name = name;
        this.polygon = polygon;
        neighbours = new ArrayList<>();
        labelPoint = new Point();
        polygonPoint = new Point();
        layer = -1;
        clickable = false;
    }

    public Country(String name, Polygon polygon, Point labelPoint, int layer) {
        this.name = name;
        this.polygon = polygon;
        neighbours = new ArrayList<>();
        this.visited = false;
        this.labelPoint = labelPoint;
        this.layer = layer;
        clickable = false;
    }

    /**
     * Contructor from json object.
     *
     * @param json, contains info about the name, neighoburs, and the polygon points
     */
    public Country(JsonObject json) {
        this.name = json.get(StringGlobals.countryName).getAsString();

        // Get the neighbours
        JsonArray arr = json.get(StringGlobals.countryNeighbours).getAsJsonArray();
        this.neighbours = new ArrayList<>(arr.size());
        arr.forEach((item) -> neighbours.add(item.getAsString()));

        // Get the points
        polygon = new Polygon();
        JsonArray pointArr = json.get(StringGlobals.countryPoints).getAsJsonArray();
        for (int i = 0; i < pointArr.size(); i++) {
            JsonObject point = pointArr.get(i).getAsJsonObject();
            polygon.addPoint(point.get(StringGlobals.countryX).getAsInt(), point.get(StringGlobals.countryY).getAsInt());
        }

        // Get where it is in on the screen, the label position, and the layer
        JsonObject polygonPoint = json.get(StringGlobals.countryPolygonPoint).getAsJsonObject();
        this.polygonPoint = new Point(polygonPoint.get(StringGlobals.countryX).getAsInt(), polygonPoint.get(StringGlobals.countryY).getAsInt());

        JsonObject labelPoint = json.get(StringGlobals.countryLabelPoint).getAsJsonObject();
        this.labelPoint = new Point(labelPoint.get(StringGlobals.countryX).getAsInt(), labelPoint.get(StringGlobals.countryY).getAsInt());

        this.layer = json.get(StringGlobals.countryLayer).getAsInt();
        clickable = false;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Point getLabelPoint() {
        return labelPoint;
    }

    public Point getPolygonPoint() {
        return polygonPoint;
    }

    public int getLayer() {
        return layer;
    }

    public void setClickable(){
        clickable = true;
    }

    public void resetClickable(){
        clickable = false;
    }

    public boolean isClickable(){
        return clickable;
    }

    /**
     * Adds the coutnry the new continent and removos from old continent if it was previously set.
     *
     * @param continent - new continent to be added to, can be null
     */
    public void setContinent(Continent continent) {
        // Remove from old continent if it has one
        if (this.continent != null) {
            this.continent.removeCountry(this);
        }

        this.continent = continent;

        if (this.continent != null) {
            this.continent.addCountry(this);
        }
    }

    public Continent getContinent() {
        return continent;
    }

    public List<String> getNeighbours() {
        return neighbours;
    }

    public Player getPlayer() {
        return controlledBy;
    }

    public String getName() {
        return name;
    }

    public void setPlayer(Player p){
        this.controlledBy = p;
        System.out.println(name + " is controlled by " + p.getIndex());
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
        }

        this.numArmies -= armies;
    }

    public void setNeighbourNames(ArrayList<String> neighbourNames) {
        this.neighbours = neighbourNames;
    }

    @Override
    public String toString() {
        return name;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean getVisited(){
        return visited;
    }


    /**
     * @return the json representation of the country which has name, neighbours, and the coordinates of points.
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        // Add country name
        json.addProperty(StringGlobals.countryName, name);

        // Add the neighbours
        JsonArray arr = new JsonArray();
        neighbours.forEach(arr::add);
        json.add(StringGlobals.countryNeighbours, arr);

        // Add the points
        JsonArray pointArr = new JsonArray();
        for (int i = 0; i < polygon.npoints; i++) {
            JsonObject point = new JsonObject();
            point.addProperty(StringGlobals.countryX, polygon.xpoints[i]);
            point.addProperty(StringGlobals.countryY, polygon.ypoints[i]);
            pointArr.add(point);
        }
        json.add(StringGlobals.countryPoints, pointArr);

        JsonObject polygonPoint = new JsonObject();
        polygonPoint.addProperty(StringGlobals.countryX, this.polygonPoint.x);
        polygonPoint.addProperty(StringGlobals.countryY, this.polygonPoint.y);
        json.add(StringGlobals.countryPolygonPoint, polygonPoint);

        JsonObject labelPoint = new JsonObject();
        labelPoint.addProperty(StringGlobals.countryX, this.labelPoint.x);
        labelPoint.addProperty(StringGlobals.countryY, this.labelPoint.y);
        json.add(StringGlobals.countryLabelPoint, labelPoint);

        json.addProperty(StringGlobals.countryLayer, layer);

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

    public void setPolygonPoint(Point locationOnScreen) {
        polygonPoint = locationOnScreen;
    }

    public void setLabelPoint(Point locationOnScreen) {
        labelPoint = locationOnScreen;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void addNeighbour(String countryName) {
        if (!neighbours.contains(countryName))
            neighbours.add(countryName);
    }

    public void removeNeighbour(String countryName){
        neighbours.remove(countryName);
    }

    public void toggleNeighbour(Country neighbour) {
        if (neighbours.contains(neighbour.getName()))
            removeNeighbour(neighbour.getName());
        else
            addNeighbour(neighbour.getName());
    }

    public void setName(String countryName) {
        name = countryName;
    }
}
