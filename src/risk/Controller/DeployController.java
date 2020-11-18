package risk.Controller;

import risk.Action.Action;
import risk.Action.Attack;
import risk.Action.Deploy;
import risk.Model.Country;
import risk.Model.GameModel;

public class DeployController {

    private final GameModel gameModel;
    private Deploy deploy;

    public DeployController(GameModel gameModel, Deploy deploy){
        this.gameModel = gameModel;
        this.deploy = deploy;
    }


    public void initiateDeploy(){
        Country firstCountry = deploy.getCountry();
        firstCountry.getPlayer().placeArmies(firstCountry, deploy.getNumTroops());

        gameModel.updateGame();
        gameModel.nextPhase();
    }




}
