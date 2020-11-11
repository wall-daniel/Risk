package risk.Players;

import risk.Action.Action;
import risk.Enums.PlayerType;
import risk.Model.GameModel;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }

    @Override
    public Action getAction(){
        return null;
    }

}
