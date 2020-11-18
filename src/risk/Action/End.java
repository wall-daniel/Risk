package risk.Action;

import risk.Model.GameModel;

public class End implements Action {
    public String toString(){
        return "End Phase Action";
    }

    @Override
    public void doAction(GameModel gameModel) {
        gameModel.nextPhase();
    }
}
