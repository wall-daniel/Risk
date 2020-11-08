package risk.Controller;

import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.util.ArrayList;

public class MovementController {

    private JFrame frame;
    private GameModel gameModel;

    private Country fromCountry;
    private Country toCountry;
    private ArrayList<String> moveableToCountries;

    public MovementController(GameModel gameModel, JFrame frame) {
        this.frame = frame;
        this.gameModel = gameModel;
        this.moveableToCountries = new ArrayList<>();
    }

    private void resetController() {
        fromCountry = null;
        toCountry = null;
        moveableToCountries.clear();
    }

    public void setMovingFromCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer()) {
            System.out.println("Moving from " + country);
            fromCountry = country;
            moveableToCountries = gameModel.getMoveTroopsToCountries(fromCountry);
            gameModel.nextPhase();
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Country"
            );
        }
    }

    public void setMovingToCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer() && moveableToCountries.contains(country.getName())) {
            System.out.println("Moving to " + country);
            toCountry = country;
            getArmiesToMove();
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Country"
            );
        }
    }


    public Country getFromCountry() {
        return fromCountry;
    }

    /*
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
     */


    private void getArmiesToMove() {
        try {
            String response = JOptionPane.showInputDialog(
                    frame,
                    "How many armies do you want to move to " + toCountry.getName() + " from " + fromCountry.getName() + "(0-" + (fromCountry.getArmies() - 1) + ")?",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // If they cancel or close this then exit this
            if (response == null) {
                return;
            }

            int armies = Integer.parseInt(response);

            if (armies < fromCountry.getArmies() && armies > 0) {
                fromCountry.removeArmies(armies);
                toCountry.addArmies(armies);
                gameModel.updateGame();
                gameModel.nextPhase();
                resetController();
            } else {
                JOptionPane.showMessageDialog(frame, "You can not move " + armies + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
