package risk.Model;


import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {

    private String name;
    private int numArmies = 1;
    private int controlledBy = -1;

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
            neighbourIndicies[i++] = GameModel.getIndexOfCountry(s);
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

    public int getPlayer() {
        return controlledBy;
    }

    public String getName() {
        return name;
    }

    public void setPlayer(int playerNum) {
        setPlayer(playerNum, 1);
    }

    public void setPlayer(int playerNum, int numArmies) {
        // Remove current player from controlling it
        if (controlledBy != -1) {
            GameModel.getPlayers().get(controlledBy).removeCountry(name);
        }

        this.controlledBy = playerNum;
        this.numArmies = numArmies;
        GameModel.getPlayers().get(playerNum).addCountry(name);
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



}
