package risk.Action;

import risk.Controller.AttackController;
import risk.Controller.DeployController;
import risk.Model.Country;
import risk.Model.GameModel;

public class Attack implements Action{
    private int attackingArmies;
    private Country attackingCountry, defendingCountry;

    public Attack(Country firstCountry, Country secondCountry, int attackingArmies) {
        this.attackingArmies = attackingArmies;
        this.attackingCountry = firstCountry;
        this.defendingCountry = secondCountry;
    }

    public int getAttackingArmies() {
        return attackingArmies;
    }

    public void setAttackingArmies(int attackingArmies) {
        this.attackingArmies = attackingArmies;
    }

    public Country getAttackingCountry() {
        return attackingCountry;
    }

    public void setAttackingCountry(Country firstCountry) {
        this.attackingCountry = firstCountry;
    }

    public Country getDefendingCountry() {
        return defendingCountry;
    }

    public void setDefendingCountry(Country secondCountry) {
        this.defendingCountry = secondCountry;
    }

    public void removeAttackingArmies(int armies){
        this.attackingArmies -= armies;
    }

    public String toString(){
        return "Attack " + defendingCountry.getName() + " from " + attackingCountry.getName() + " with " + attackingArmies;
    }

    @Override
    public void doAction(GameModel gameModel) {
        new AttackController(gameModel, this).initiateAttack();
    }
}
