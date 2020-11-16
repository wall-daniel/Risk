package risk.Players;

import risk.Action.Action;
import risk.Action.Attack;
import risk.Action.Move;
import risk.Action.Place;
import risk.Enums.PlayerColor;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.awt.*;
import java.util.ArrayList;

public class AIPlayer extends Player {


    public AIPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }

    /**
     * Logic for ai to choose action depending on game phase
     * @return
     */
    @Override
    public Action getAction(){
        if (gameModel.gameStatus == GameModel.GameStatus.TROOP_PLACEMENT_PHASE)
            return new Place(3, gameModel.getCountry(countriesOwned.get(3)));

        if (gameModel.gameStatus == GameModel.GameStatus.SELECT_ATTACKING_PHASE)
            return new Attack(3, gameModel.getCountry(countriesOwned.get(0)), gameModel.getCountry("Madagascar"));

        if (gameModel.gameStatus == GameModel.GameStatus.SELECT_TROOP_MOVING_FROM_PHASE)
            return new Move(3, gameModel.getCountry(countriesOwned.get(0)), gameModel.getCountry("Madagascar"));
        return null;
    }


}
