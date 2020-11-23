package risk.Action;

import risk.Model.GameModel;

public class Reset implements Action {

    public String toString(){
        return "Reset Phase Action";
    }

    @Override
    public void doAction(GameModel gameModel) {
        gameModel.resetPhase();
    }
}
