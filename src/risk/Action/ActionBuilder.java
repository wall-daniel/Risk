package risk.Action;

import risk.Model.Country;

public class ActionBuilder {
    private Country firstCountry, secondCountry;
    private int numTroops;

    public ActionBuilder(){

    }

    public ActionBuilder(Country firstCountry, int numTroops){
        this.firstCountry = firstCountry;
        this.numTroops =  numTroops;
    }

    public ActionBuilder(Country firstCountry, Country secondCountry, int numTroops) {
        this.firstCountry = firstCountry;
        this.secondCountry = secondCountry;
        this.numTroops = numTroops;
    }

    /**
     * @return true if the countries are neighbours
     */
    public boolean countriesConnected() {
        String secondCountryName = secondCountry.getName();
        for (String neighbourName : firstCountry.getNeighbours()) {
            if (neighbourName.equals(secondCountryName)) {
                return true;
            }
        }

        return false;
    }

    public Country getFirstCountry(){
        return firstCountry;
    }

    public void setFirstCountry(Country firstCountry) {
        this.firstCountry = firstCountry;
    }

    public void setSecondCountry(Country secondCountry) {
        this.secondCountry = secondCountry;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public boolean isTroopsSet() {
        return numTroops > 0;
    }

    public Deploy buildDeploy(){
        return new Deploy(firstCountry, numTroops);
    }

    public Attack buildAttack() {
//        if (countriesConnected()) {
//            throw new Exception("Not neighbours.");
//        }
        return new Attack(firstCountry, secondCountry, numTroops);
    }

    public Fortify buildFortify(){
        return new Fortify(firstCountry, secondCountry, numTroops);
    }

    public End buildEnd(){
        return new End();
    }

    public Reset buildReset() {
        return new Reset();
    }
}
