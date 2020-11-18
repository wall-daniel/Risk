package risk.Action;

import risk.Controller.FortifyController;
import risk.Model.Country;
import risk.Model.GameModel;

public class Fortify implements Action {
    int numTroops;
    Country firstCountry, secondCountry;

    public Fortify(Country firstCountry, Country secondCountry, int numTroops) {
        this.numTroops = numTroops;
        this.firstCountry = firstCountry;
        this.secondCountry = secondCountry;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public Country getFirstCountry() {
        return firstCountry;
    }

    public void setFirstCountry(Country firstCountry) {
        this.firstCountry = firstCountry;
    }

    public Country getSecondCountry() {
        return secondCountry;
    }

    public void setSecondCountry(Country secondCountry) {
        this.secondCountry = secondCountry;
    }

    public String toString(){
        return "Fortify " + secondCountry.getName() + " from " + firstCountry.getName() + " with " + numTroops;
    }

    @Override
    public void doAction(GameModel gameModel) {
        new FortifyController(gameModel, this).initiateFortify();
        gameModel.nextPhase();
    }
}
