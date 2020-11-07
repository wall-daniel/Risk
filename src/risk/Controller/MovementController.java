package risk.Controller;

import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;

public class MovementController {

    private JFrame frame;
    private GameModel gameModel;

    private Country fromCountry;
    private Country toCountry;

    public MovementController(JFrame frame, GameModel gameModel) {
        this.frame = frame;
        this.gameModel = gameModel;
    }

    private void resetController() {
        fromCountry = null;
        toCountry = null;
    }

    public void setMovingFromCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer()) {
            System.out.println("Moving from " + country);
            fromCountry = country;
            gameModel.nextPhase();
        }
    }

    public void setMovingToCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer()) {
            System.out.println("Moving to " + country);
            toCountry = country;

            getArmiesToMove();
            gameModel.nextPhase();
        }
    }

    private void getArmiesToMove() {
        while (true) {
            try {
                int armies = Integer.parseInt(
                        JOptionPane.showInputDialog(
                                frame,
                                "How many armies do you want to move?",
                                JOptionPane.INFORMATION_MESSAGE
                        )
                );

                if (armies < fromCountry.getArmies() && armies > 0) {
                    fromCountry.removeArmies(armies);
                    toCountry.addArmies(armies);
                    gameModel.updateGame();
                    resetController();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
