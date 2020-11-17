package risk.Action;

import risk.Model.Country;

public class Deploy extends Action{

    int numTroops;
    Country country;

    public Deploy(Country country, int numTroops) {
        this.numTroops = numTroops;
        this.country = country;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String toString(){
        return "Deploy: " + numTroops + " to " + country.getName();
    }


}
