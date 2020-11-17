package risk.Players;

import risk.Action.Action;
import risk.Action.Attack;
import risk.Action.Fortify;
import risk.Action.Deploy;
import risk.Enums.PlayerType;
import risk.Model.GameModel;

public class AIPlayer extends Player {


    public AIPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }


    /**
     * used to set the num troops to move in actionBuilder if a country is won in an attack
     * @param msg
     * @param min
     * @param max
     */
    @Override
    public void inputTroopCount(String msg, int min, int max) {

    }

    /**
     * Logic for ai to choose action depending on game phase
     * @return
     */
    @Override
    public Action getAction(){
        return null;
    }
}
