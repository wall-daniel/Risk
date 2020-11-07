package risk.Controller;

import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.util.HashMap;

public class MovementController {

    private JFrame frame;
    private GameModel gameModel;

    private Country fromCountry;
    private Country toCountry;

    public MovementController(GameModel gameModel, JFrame frame) {
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
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Country"
            );
        }
    }

    public void setMovingToCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer() && countriesAreConnected()) {
            System.out.println("Moving to " + country);
            toCountry = country;

            getArmiesToMove();
            gameModel.nextPhase();
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Country"
            );
        }
    }

    private boolean countriesAreConnected() {
        HashMap<String, Country> ss = new HashMap<>();
        ss.put(fromCountry.getName(), fromCountry);
        return checkCountry(fromCountry, ss);
    }

    private boolean checkCountry(Country currentCountry, HashMap<String, Country> ss) {
        if (currentCountry.getName().equals(toCountry.getName()))
            return true;

        for (String countryName : currentCountry.getNeighbours()){
            Country country = gameModel.getCountry(countryName);
            if (country.getPlayer().getIndex() == fromCountry.getPlayer().getIndex() && !ss.containsKey(countryName)){
                ss.put(countryName, country);
                return checkCountry(country, ss);
            }
        }
        return false;
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
