package risk.Players;

import risk.Action.*;
import risk.Action.Action;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }

    private int getIntegerInput(String msg, int min, int max){
        int input = 0;
        do {
            String val = JOptionPane.showInputDialog(msg + "(" + min + " - " + max + ")");

            if (val == null)
                return 0;

            input = Integer.parseInt(val);
        } while (input < min || input > max);
        return input;
    }

    /**
     * Gets the number of troops to place and sets it in action builder
     * @return
     */
    public void inputTroopCount(String msg, int min, int max){
        int val = getIntegerInput(msg, min, max);
        setNumTroopsOfAction(val);
    }


    @Override
    public Action getAction() {
        switch (gameModel.gameStatus){
            case TROOP_PLACEMENT_PHASE:
                return actionBuilder.buildDeploy();
            case SELECT_DEFENDING_PHASE:
                return actionBuilder.buildAttack();
            case SELECT_TROOP_MOVING_TO_PHASE:
                return actionBuilder.buildFortify();
        }
        return null;
    }


}
