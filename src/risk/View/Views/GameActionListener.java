package risk.View.Views;

import risk.Model.GameModel;

public interface GameActionListener {
    void updateMap(GameModel gameModel);

    void displayMessage(String message);
}
