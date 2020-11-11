package risk.Action;

import risk.Model.Country;

public class Place extends Action{

    int numTroops;
    Country country;

    public Place(int numTroops, Country country) {
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


}
