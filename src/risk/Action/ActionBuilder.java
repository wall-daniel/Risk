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

    public Deploy buildDeploy(){
        return new Deploy(firstCountry, numTroops);
    }

    public Attack buildAttack(){
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
