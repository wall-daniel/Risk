package risk.Players;

import risk.Action.Action;
import risk.Action.Attack;
import risk.Action.Move;
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
     * Logic for ai to choose action
     * @return
     */
    @Override
    public Action getAction(){
        //return new Place(3, gameModel.getCountry(countriesOwned.get(3)));

        return new Attack(3, gameModel.getCountry(countriesOwned.get(0)), gameModel.getCountry("Madagascar"));

        //return new Move(3, gameModel.getCountry(countriesOwned.get(0)), gameModel.getCountry("Madagascar"));
    }


}
